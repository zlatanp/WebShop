package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Korpa;
import beans.Korpe;

@Path("/korpa")
public class KorpaRest {
	
	@Context
	ServletConfig config;

	@GET
	@Path("/dodaj/{covek}/{sifra}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Korpa dodaj(@PathParam("covek") String username, @PathParam("sifra") String sifra) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		
		
		String s = config.getServletContext().getRealPath("");
		Korpe kor = vratikorpu(s);
		Korpa zavratiti = new Korpa();
		String path = FilePaths.getPath(s).getPath();
		if(username.isEmpty())
			return zavratiti;
	
		zavratiti.setCena("");
		zavratiti.setSifra(sifra);
		zavratiti.setDostavljac("");
		zavratiti.setProdavnica("");
		zavratiti.setUser(username);
		zavratiti.setProizvodi(null);
		
		
		kor.getKorpa().add(zavratiti);
		
		PrintWriter writer = new PrintWriter(path + "\\Korpa.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<kor.getKorpa().size();i++){
			zapisiUbazu(s, kor.getKorpa().get(i).getUser(), kor.getKorpa().get(i).getSifra(), kor.getKorpa().get(i).getCena(), kor.getKorpa().get(i).getDostavljac(), kor.getKorpa().get(i).getProdavnica(), kor.getKorpa().get(i).getProizvodi());
		}
		
		return zavratiti;
		
	}
	
	@GET
	@Path("/dajsve/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Korpa> vratiproizvode(@PathParam("user") String user) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		
		ArrayList<Korpa> zavracanje = new ArrayList<Korpa>();
		String s = config.getServletContext().getRealPath("");
		Korpe kor = vratikorpu(s);
		for(int i =0;i<kor.getKorpa().size();i++){
			if(kor.getKorpa().get(i).getUser().equals(user))
				zavracanje.add(kor.getKorpa().get(i));
		}
		
		return zavracanje;
		
	}
	
	@GET
	@Path("/kupio/{covek}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Korpa jedanlik(@PathParam("covek") String username, Korpa k) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		
		
		String s = config.getServletContext().getRealPath("");
		Korpe kor = vratikorpu(s);
		Korpa zavratiti = new Korpa();
		String path = FilePaths.getPath(s).getPath();
		if(username.isEmpty())
			return zavratiti;
	
		zavratiti.setCena(k.getCena());
		zavratiti.setSifra(k.getSifra());
		zavratiti.setDostavljac(k.getDostavljac());
		zavratiti.setProdavnica(k.getProdavnica());
		zavratiti.setUser(username);
		zavratiti.setProizvodi(k.getProizvodi());
		
		
		kor.getKorpa().add(zavratiti);
		
		PrintWriter writer = new PrintWriter(path + "\\Korpa.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<kor.getKorpa().size();i++){
			zapisiUbazu(s, kor.getKorpa().get(i).getUser(), kor.getKorpa().get(i).getSifra(), kor.getKorpa().get(i).getCena(), kor.getKorpa().get(i).getDostavljac(), kor.getKorpa().get(i).getProdavnica(), kor.getKorpa().get(i).getProizvodi());
		}
		
		return zavratiti;
		
	}
	
	@GET
	@Path("/obrisi/{covek}/{proizvod}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Korpa obrisiizkorpe(@PathParam("covek") String username,@PathParam("proizvod") String proizvod) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		
		
		String s = config.getServletContext().getRealPath("");
		Korpe kor = vratikorpu(s);
		Korpa zavratiti = new Korpa();
		String path = FilePaths.getPath(s).getPath();
		if(username.isEmpty())
			return zavratiti;
	
		for(int i=0;i<kor.getKorpa().size();i++){
			if(kor.getKorpa().get(i).getUser().equals(username)){
				if(kor.getKorpa().get(i).getSifra()!=null){
					if(kor.getKorpa().get(i).getSifra().equals(proizvod)){
					kor.getKorpa().remove(i);
					}
				}
			}
		}
		
	
		
		PrintWriter writer = new PrintWriter(path + "\\Korpa.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<kor.getKorpa().size();i++){
			zapisiUbazu(s, kor.getKorpa().get(i).getUser(), kor.getKorpa().get(i).getSifra(), kor.getKorpa().get(i).getCena(), kor.getKorpa().get(i).getDostavljac(), kor.getKorpa().get(i).getProdavnica(), kor.getKorpa().get(i).getProizvodi());
		}
		
		return zavratiti;
		
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String user,
			String sifra, String cena, String dostavljac, String prodavnica, ArrayList<String> proizvodi) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("user", user);
		obj.put("proizvodi", proizvodi);
		obj.put("prodavnica", prodavnica);
		obj.put("dostavljac", dostavljac);
		obj.put("cena", cena);
		obj.put("sifra", sifra);
		

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Korpa.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Korpe vratikorpu(String gde) throws JsonParseException, JsonMappingException, IOException {
		Korpe pr = new Korpe();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Korpa.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON1(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		for (int i = 0; i < jsons.size(); i++) {
			String iuser = (String) jsons.get(i).get("user");
			String iprodavnica = (String) jsons.get(i).get("prodavnica");
			String idostavljac = (String) jsons.get(i).get("dostavljac");
			String icena = (String) jsons.get(i).get("cena");
			String isifra = (String) jsons.get(i).get("sifra");
			
			ArrayList<String> proizvodi = (ArrayList<String>) jsons.get(i).get("proizvodi");

			Korpa k = new Korpa();
			k.setUser(iuser);
			k.setProdavnica(iprodavnica);
			k.setDostavljac(idostavljac);
			k.setCena(icena);
			k.setSifra(isifra);
			k.setProizvodi(proizvodi);
			
			pr.getKorpa().add(k);
		}

		return pr;
	}

	// pomocni metod za citanje objekata
	@SuppressWarnings("resource")
	public static synchronized ArrayList<JSONObject> ReadJSON1(File MyFile, String Encoding)
			throws FileNotFoundException, ParseException {
		Scanner scn = new Scanner(MyFile, Encoding);
		ArrayList<JSONObject> json = new ArrayList<JSONObject>();
		// Reading and Parsing Strings to Json
		while (scn.hasNext()) {
			JSONObject obj = (JSONObject) new JSONParser().parse(scn.nextLine());
			json.add(obj);
		}
		// Here Printing Json Objects
		// for(JSONObject obj : json){
		// System.out.println((String)obj.get("username")+" :
		// "+(String)obj.get("password")+" : "+(String)obj.get("ime"));
		// }
		return json;
	}

	
}

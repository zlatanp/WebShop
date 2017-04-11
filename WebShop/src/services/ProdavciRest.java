package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Prodavac;
import beans.Prodavci;
import beans.Prodavnica;
import beans.Prodavnice;

@Path("/prodavci")
public class ProdavciRest {
	
	@Context
	ServletConfig config;
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Prodavac> sviProdavci() {
		
		String s = config.getServletContext().getRealPath("");
		Prodavci p = listaProdavaca(s);
		
		return p.getListaProdavaca();
	}
	
	@GET
	@Path("/daj/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<String> vratiProdavnice(@PathParam("username") String username) {
		
		String s = config.getServletContext().getRealPath("");
		Prodavci p = listaProdavaca(s);
		ArrayList<String> prodavnice = new ArrayList<String>();
		if(username.isEmpty())
			return null;
		
		for(int i =0;i<p.getListaProdavaca().size();i++){
			if(p.getListaProdavaca().get(i).getUsername().equals(username)){
				prodavnice = p.getListaProdavaca().get(i).getProdavnice();
			}
		}
		
		return prodavnice;
	}
	
	@GET
	@Path("/obrisi/{username}")
	public Response brisiProdavca(@PathParam("username") String username) throws URISyntaxException, FileNotFoundException {
		
		String s = config.getServletContext().getRealPath("");
		String path = FilePaths.getPath(s).getPath();
		Prodavci p = listaProdavaca(s);
		URI uriSucces = new URI("/WebShop/kontrolPanel.html");
		
		if(username.isEmpty()){
			return Response.seeOther(uriSucces).build();
		}
		
		for(int i=0;i<p.getListaProdavaca().size();i++){
			if(p.getListaProdavaca().get(i).getUsername().equals(username))
				p.getListaProdavaca().remove(i); //obrisao prodavnicu iz kolekcije, sad je upisi kolekciju nazad u fajl.
		}
		
		 //******************* sad treba u prodavnici dodati tog lika
		 Prodavnice prodavnice = new Prodavnice();
		 prodavnice = listaProdavnica(s);
		 for(int i =0;i<prodavnice.getListaprodavnica().size();i++){
			 if(prodavnice.getListaprodavnica().get(i).getOdgovorniProdavac().equals(username)){
				 prodavnice.getListaprodavnica().get(i).setOdgovorniProdavac("");
				 System.out.println("uso da brisi?");
			 	}
			 
		 }
		 PrintWriter writerr = new PrintWriter(path + "\\Prodavnice.txt");
			writerr.print("");
			writerr.close();
		 
		 for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				zapisiUbazu(s, prodavnice.getListaprodavnica().get(i).getSifra(), prodavnice.getListaprodavnica().get(i).getNaziv(), prodavnice.getListaprodavnica().get(i).getAdresa(), prodavnice.getListaprodavnica().get(i).getDrzava(), prodavnice.getListaprodavnica().get(i).getTelefon(), prodavnice.getListaprodavnica().get(i).getEmail(), prodavnice.getListaprodavnica().get(i).getOcena(), prodavnice.getListaprodavnica().get(i).getRecenzije(), prodavnice.getListaprodavnica().get(i).getOdgovorniProdavac());
			}
			
		 //*****************************************************
		
		
		
		PrintWriter writer = new PrintWriter(path + "\\Prodavci.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<p.getListaProdavaca().size();i++){
			zapisiUbazu(s, p.getListaProdavaca().get(i).getUsername(), p.getListaProdavaca().get(i).getPass(), p.getListaProdavaca().get(i).getProdavnice());
		}
		
		
		
		return Response.seeOther(uriSucces).build();
		
	}
	
	@POST
	@Path("/dodaj")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Prodavac dodajProdavca(Prodavac pr) throws JsonParseException, JsonMappingException, IOException {
		 String s = config.getServletContext().getRealPath("");
		 String path = FilePaths.getPath(s).getPath();
		 Prodavci prodavci = listaProdavaca(s);
		 
		 if(pr.equals(null))
			 return pr;
		 
		 Prodavac p = new Prodavac();
		 p.setPass(pr.getPass());
		 p.setUsername(pr.getUsername());
		 p.setProdavnice(pr.getProdavnice());
		 prodavci.getListaProdavaca().add(p);
		 
		 //******************* sad treba u prodavnici dodati tog lika
		 Prodavnice prodavnice = new Prodavnice();
		 prodavnice = listaProdavnica(s);
		 for(int i =0;i<prodavnice.getListaprodavnica().size();i++){
			 for(int j=0;j<p.getProdavnice().size();j++){
			 if(prodavnice.getListaprodavnica().get(i).getNaziv().equals(p.getProdavnice().get(j))){
				 prodavnice.getListaprodavnica().get(i).setOdgovorniProdavac(p.getUsername());
				 System.out.println("uso?");
			 	}
			 }
		 }
		 PrintWriter writerr = new PrintWriter(path + "\\Prodavnice.txt");
			writerr.print("");
			writerr.close();
		 
		 for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				zapisiUbazu(s, prodavnice.getListaprodavnica().get(i).getSifra(), prodavnice.getListaprodavnica().get(i).getNaziv(), prodavnice.getListaprodavnica().get(i).getAdresa(), prodavnice.getListaprodavnica().get(i).getDrzava(), prodavnice.getListaprodavnica().get(i).getTelefon(), prodavnice.getListaprodavnica().get(i).getEmail(), prodavnice.getListaprodavnica().get(i).getOcena(), prodavnice.getListaprodavnica().get(i).getRecenzije(), prodavnice.getListaprodavnica().get(i).getOdgovorniProdavac());
			}
			
		 //*****************************************************
		 PrintWriter writer = new PrintWriter(path + "\\Prodavci.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<prodavci.getListaProdavaca().size();i++){
				zapisiUbazu(s, prodavci.getListaProdavaca().get(i).getUsername(), prodavci.getListaProdavaca().get(i).getPass(), prodavci.getListaProdavaca().get(i).getProdavnice());
			}
			
			return pr;
		}

	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String username, String pass, ArrayList<String> prod ) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("pass", pass);
		obj.put("prodavnice", prod);
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Prodavci.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje prodavnica
	@SuppressWarnings("unchecked")
	public static synchronized Prodavci listaProdavaca(String gde) {
		Prodavci pr = new Prodavci();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Prodavci.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsons.size(); i++) {
			String user = (String) jsons.get(i).get("username");
			String pass = (String) jsons.get(i).get("pass");

			ArrayList<String> prod = (ArrayList<String>) jsons.get(i).get("prodavnice");
			Prodavac prodaja = new Prodavac();
			
			prodaja.setPass(pass);
			prodaja.setUsername(user);
			prodaja.setProdavnice(prod);
			

			pr.getListaProdavaca().add(prodaja);
		}

		return pr;
	}

	// pomocni metod za citanje objekata
	@SuppressWarnings("resource")
	public static synchronized ArrayList<JSONObject> ReadJSON(File MyFile, String Encoding)
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
//**************************************************************************
	//za prodavnice
	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String sifra, String naziv, String adresa, String drzava, String telefon, String email, String ocena, ArrayList<String> rec, String odgovorniProdavac) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("sifra", sifra);
		obj.put("naziv", naziv);
		obj.put("adresa", adresa);
		obj.put("drzava", drzava);
		obj.put("telefon", telefon);
		obj.put("email", email);
		obj.put("ocena", ocena);
		obj.put("recenzije", rec);
		obj.put("odgovorni", odgovorniProdavac);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Prodavnice.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje prodavnica
	@SuppressWarnings("unchecked")
	public static synchronized Prodavnice listaProdavnica(String gde) {
		Prodavnice pr = new Prodavnice();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Prodavnice.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON1(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsons.size(); i++) {
			String isifra = (String) jsons.get(i).get("sifra");
			String inaziv = (String) jsons.get(i).get("naziv");
			String iadresa = (String) jsons.get(i).get("adresa");
			String idrzava = (String) jsons.get(i).get("drzava");
			String itelefon = (String) jsons.get(i).get("telefon");
			String iemail = (String) jsons.get(i).get("email");
			String iocena = (String) jsons.get(i).get("ocena");
			ArrayList<String> irecenzije = (ArrayList<String>) jsons.get(i).get("recenzije");
			String iodgovorni = (String) jsons.get(i).get("odgovorni");
			Prodavnica p = new Prodavnica();
			
			
			
			p.setSifra(isifra);
			p.setNaziv(inaziv);
			p.setAdresa(iadresa);
			p.setDrzava(idrzava);
			p.setTelefon(itelefon);
			p.setEmail(iemail);
			p.setOcena(iocena);
			p.setRecenzije(irecenzije);
			p.setOdgovorniProdavac(iodgovorni);
			

			pr.getListaprodavnica().add(p);
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
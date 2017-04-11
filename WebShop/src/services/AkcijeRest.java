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

import beans.Akcija;
import beans.Akcije;

@Path("/akcije")
public class AkcijeRest {
	@Context
	ServletConfig config;
	
	@GET
	@Path("/all") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Akcija> vratiakcije() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Akcije p =sveakcije(s);
		
	
		
		return p.getSveakcije();
	}
	
	@GET
	@Path("/brisiakciju/{id}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Akcija> vratiakcijee(@PathParam("id") String id) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Akcije p =sveakcije(s);
		String path = FilePaths.getPath(s).getPath();
		
	for(int i =0;i<p.getSveakcije().size();i++){
		if(p.getSveakcije().get(i).getId().equals(id))
			p.getSveakcije().get(i).setAktivno("ne");
	}
	
	PrintWriter writer = new PrintWriter(path + "\\Akcije.txt");
	writer.print("");
	writer.close();
	
	for(int i=0;i<p.getSveakcije().size();i++){
		zapisiUbazu(s, p.getSveakcije().get(i).getPocetak(), p.getSveakcije().get(i).getKraj(), p.getSveakcije().get(i).getKategorija(),
				 p.getSveakcije().get(i).getProcenat(),  p.getSveakcije().get(i).getSifreproizvoda(), p.getSveakcije().get(i).getAktivno(), p.getSveakcije().get(i).getProdavnica(), p.getSveakcije().get(i).getId());
	}
		
		return p.getSveakcije();
	}
	
	@POST
	@Path("/dodaj")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public synchronized Response register(Akcija a)
			throws IOException, URISyntaxException {
		
		String s = config.getServletContext().getRealPath("");
		String path = FilePaths.getPath(s).getPath();
		URI uriSucces = new URI("/WebShop/seller.html");
		Akcije ak = sveakcije(s);
		
		Akcija akcja = new Akcija();
		akcja.setAktivno(a.getAktivno());
		akcja.setPocetak(a.getPocetak());
		akcja.setKraj(a.getKraj());
		akcja.setProdavnica(a.getProdavnica());
		akcja.setId(a.getId());
		
		
		if(a.getKategorija().length()<5){
			akcja.setKategorija(null);
			akcja.setProcenat(null);
			
			akcja.setSifreproizvoda(a.getSifreproizvoda());
		}else{
			akcja.setSifreproizvoda(null);
			akcja.setKategorija(a.getKategorija());
			akcja.setProcenat(a.getProcenat());
		}
		
		
		ak.getSveakcije().add(akcja);
		
		//nakon sto napravio novu prodavnicu ubaci je u bazu
		PrintWriter writer = new PrintWriter(path + "\\Akcije.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<ak.getSveakcije().size();i++){
			zapisiUbazu(s, ak.getSveakcije().get(i).getPocetak(), ak.getSveakcije().get(i).getKraj(), ak.getSveakcije().get(i).getKategorija(),
					 ak.getSveakcije().get(i).getProcenat(),  ak.getSveakcije().get(i).getSifreproizvoda(), ak.getSveakcije().get(i).getAktivno(), ak.getSveakcije().get(i).getProdavnica(), ak.getSveakcije().get(i).getId());
		}
		
		return Response.seeOther(uriSucces).build();
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String pocetak, String kraj, String kategorija, String procenat,
			ArrayList<String> sifreproizvoda, String aktivno, ArrayList<String> prodavnica, String id) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("pocetak", pocetak);
		obj.put("kraj", kraj);
		obj.put("procenat", procenat);
		obj.put("sifreproizvoda", sifreproizvoda);
		obj.put("kategorija", kategorija );
		obj.put("aktivno", aktivno );
		obj.put("prodavnica",prodavnica);
		obj.put("id", id);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Akcije.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje registrovanih
	@SuppressWarnings("unchecked")
	public static synchronized Akcije sveakcije(String gde) {
		Akcije ak = new Akcije();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Akcije.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsons.size(); i++) {
			String ipocetak = (String) jsons.get(i).get("pocetak");
			String ikraj = (String) jsons.get(i).get("kraj");
			String iprocenat = (String) jsons.get(i).get("procenat");
			String ikategorija = (String) jsons.get(i).get("kategorija");
			ArrayList<String> isifreproizvoda = (ArrayList<String>) jsons.get(i).get("sifreproizvoda");
			ArrayList<String> iprodavnica = (ArrayList<String>) jsons.get(i).get("prodavnica");
			String iaktivno = (String) jsons.get(i).get("aktivno");
			String iid = (String) jsons.get(i).get("id");
			
			Akcija akcija = new Akcija();
			akcija.setPocetak(ipocetak);
			akcija.setKraj(ikraj);
			akcija.setProcenat(iprocenat);
			akcija.setSifreproizvoda(isifreproizvoda);
			akcija.setAktivno(iaktivno);
			akcija.setKategorija(ikategorija);
			akcija.setProdavnica(iprodavnica);
			akcija.setId(iid);
			
			ak.getSveakcije().add(akcija);
		}

		return ak;
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
}

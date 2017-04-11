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
import javax.ws.rs.FormParam;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import beans.Korisnici;
import beans.Korisnik;
import beans.Proizvod;
import beans.Proizvodi;
import beans.Recenzija;

@Path("/register")
public class RegistrationService {

	@Context
	ServletConfig config;

	
	@GET
	@Path("/brisizelju/{covek}/{sifra}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Korisnik brisizelju(@PathParam("covek") String username,@PathParam("sifra") String sifra) throws URISyntaxException, FileNotFoundException {
		
		String s = config.getServletContext().getRealPath("");
		Korisnici kor = listaRegistrovanih(s);
		String path = FilePaths.getPath(s).getPath();
		Korisnik zavratiti = new Korisnik();
		
		if(username.isEmpty())
			return zavratiti;
		
		for(int i =0; i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username)){
				for(int j=0;j<kor.getListakorisnika().get(i).getZelim().size();j++){
					if(kor.getListakorisnika().get(i).getZelim().get(j).equals(sifra))
						kor.getListakorisnika().get(i).getZelim().remove(j);
				}
			}
		}
		
		PrintWriter writer = new PrintWriter(path + "\\RegistrovaniKorisnici.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<kor.getListakorisnika().size();i++){
			zapisiUbazu(s, kor.getListakorisnika().get(i).getUsername(), kor.getListakorisnika().get(i).getPassword(), kor.getListakorisnika().get(i).getIme(), kor.getListakorisnika().get(i).getPrezime(), kor.getListakorisnika().get(i).getTelefon(), kor.getListakorisnika().get(i).getEmail(), kor.getListakorisnika().get(i).getAdresa(), kor.getListakorisnika().get(i).getDrzava(), kor.getListakorisnika().get(i).getZelim(), kor.getListakorisnika().get(i).getKupio());
		}
		
		
		return zavratiti;
		
	}
	
	@GET
	@Path("/dajlika/{covek}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Korisnik jedanlik(@PathParam("covek") String username) throws URISyntaxException, FileNotFoundException {
		
		String s = config.getServletContext().getRealPath("");
		Korisnici kor = listaRegistrovanih(s);
		Korisnik zavratiti = new Korisnik();
		
		if(username.isEmpty())
			return zavratiti;
		
		for(int i =0; i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username)){
				zavratiti = kor.getListakorisnika().get(i);
			}
		}
		
	
		
		return zavratiti;
		
	}
	
	@GET
	@Path("/dajzelje/{covek}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Proizvod> vratizelje(@PathParam("covek") String username) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		
		String s = config.getServletContext().getRealPath("");
		Korisnici kor = listaRegistrovanih(s);
		ArrayList<Proizvod> zavratiti = new ArrayList<Proizvod>();
		ArrayList<String> sifre = new ArrayList<String>();
		
		if(username.isEmpty())
			return zavratiti;
		
		for(int i =0; i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username)){
				sifre = kor.getListakorisnika().get(i).getZelim();
			}
		}
		
		Proizvodi svi = listaProizvoda(s);
	
		for(int i=0;i<svi.getPr().size();i++){
			for(int j=0;j<sifre.size();j++){
			if(svi.getPr().get(i).getSifra().equals(sifre.get(j))){
				zavratiti.add(svi.getPr().get(i));
			}
		}
		}
		
		return zavratiti;
		
	}
	
	@GET
	@Path("/zelim/{sifra}/{covek}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Response zelim(@PathParam("sifra") String sifraproizvoda, @PathParam("covek") String username) throws URISyntaxException, FileNotFoundException {
		
		String s = config.getServletContext().getRealPath("");
		String path = FilePaths.getPath(s).getPath();
		Korisnici kor = listaRegistrovanih(s);
		URI uriSucces = new URI("/WebShop/home.html");
		
		
		for(int i =0; i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username)){
				kor.getListakorisnika().get(i).getZelim().add(sifraproizvoda);
			}
		}
		
		PrintWriter writer = new PrintWriter(path + "\\RegistrovaniKorisnici.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<kor.getListakorisnika().size();i++){
			zapisiUbazu(s, kor.getListakorisnika().get(i).getUsername(), kor.getListakorisnika().get(i).getPassword(), kor.getListakorisnika().get(i).getIme(), kor.getListakorisnika().get(i).getPrezime(), kor.getListakorisnika().get(i).getTelefon(), kor.getListakorisnika().get(i).getEmail(), kor.getListakorisnika().get(i).getAdresa(), kor.getListakorisnika().get(i).getDrzava(), kor.getListakorisnika().get(i).getZelim(), kor.getListakorisnika().get(i).getKupio());
		}
		
		return Response.seeOther(uriSucces).build();
		
	}
	
	@POST
	@Path("/reg")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Response register(@FormParam("username") String username, @FormParam("password") String lozinka,
			@FormParam("ime") String ime, @FormParam("prezime") String prezime, @FormParam("telefon") String telefon,
			@FormParam("email") String email, @FormParam("adresa") String adresa, @FormParam("drzava") String drzava)
			throws IOException, URISyntaxException {
		// return "/rest/register/reg received @FormParam('ime') " + username +
		// ", and @FormParam('lozinka'): " + lozinka + "ime: " + ime + "prez: "
		// + prezime + "tel: " + telefon +"mail: " + email +"adr: " + adresa
		// +"state: "+ drzava ;
		
		URI uriFailed = new URI("/WebShop/registerFailed.html");
		URI uriSucces = new URI("/WebShop/registerSucces.html");
		if (username.isEmpty() || lozinka.isEmpty() || ime.isEmpty() || prezime.isEmpty() || telefon.isEmpty()
				|| email.isEmpty() || adresa.isEmpty() || drzava.isEmpty()) {
			
			return Response.seeOther(uriFailed).build(); //ako su prazna polja salji me na failed
		}
		String s = config.getServletContext().getRealPath("");
		Korisnici kor = listaRegistrovanih(s);
		for(int i=0;i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username))
				
				return Response.seeOther(uriFailed).build(); //ako postoji takav korisnik salji me na failed
		}
		
		ArrayList<String> prazna = new ArrayList<String>();

		//ako je sve ok, ide dalje proces
		zapisiUbazu(s, username, lozinka, ime, prezime, telefon, email, adresa, drzava, prazna, prazna);
		return Response.seeOther(uriSucces).build();
	}
	
	
	

	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String username, String lozinka, String ime, String prezime,
			String telefon, String email, String adresa, String drzava, ArrayList<String> zelim, ArrayList<String> kupio) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", lozinka);
		obj.put("ime", ime);
		obj.put("prezime", prezime);
		obj.put("uloga", "0");
		obj.put("telefon", telefon);
		obj.put("email", email);
		obj.put("adresa", adresa);
		obj.put("drzava", drzava);
		obj.put("zelim", zelim);
		obj.put("kupio", kupio);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\RegistrovaniKorisnici.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje registrovanih
	@SuppressWarnings("unchecked")
	public static synchronized Korisnici listaRegistrovanih(String gde) {
		Korisnici kor = new Korisnici();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\RegistrovaniKorisnici.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsons.size(); i++) {
			String iusername = (String) jsons.get(i).get("username");
			String ilozinka = (String) jsons.get(i).get("password");
			String iime = (String) jsons.get(i).get("ime");
			String iprezime = (String) jsons.get(i).get("prezime");
			String iiuloga = (String) jsons.get(i).get("uloga");
			// int iuloga = Integer.parseInt(iiuloga);
			String itelefon = (String) jsons.get(i).get("telefon");
			String iemail = (String) jsons.get(i).get("email");
			String iadresa = (String) jsons.get(i).get("adresa");
			String idrzava = (String) jsons.get(i).get("drzava");
			ArrayList<String> zelim = (ArrayList<String>) jsons.get(i).get("zelim");
			ArrayList<String> kupio = (ArrayList<String>) jsons.get(i).get("kupio");
			Korisnik k = new Korisnik();
			k.setUsername(iusername);
			k.setPassword(ilozinka);
			k.setIme(iime);
			k.setPrezime(iprezime);
			k.setUloga(Integer.parseInt(iiuloga));
			k.setTelefon(itelefon);
			k.setEmail(iemail);
			k.setAdresa(iadresa);
			k.setDrzava(idrzava);
			k.setZelim(zelim);
			k.setKupio(kupio);
			kor.getListakorisnika().add(k);
		}

		return kor;
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
//************************************************
	
	public static synchronized Proizvodi listaProizvoda(String gde) throws JsonParseException, JsonMappingException, IOException {
		Proizvodi pr = new Proizvodi();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Proizvodi.txt";
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
			String iboja = (String) jsons.get(i).get("boja");
			String idimenzije = (String) jsons.get(i).get("dimenzije");
			String itezina = (String) jsons.get(i).get("tezina");
			String izemlja = (String) jsons.get(i).get("zemlja");
			String iproizvodjac = (String) jsons.get(i).get("proizvodjac");
			String ijedinicnaCena = (String) jsons.get(i).get("jedinicnaCena");
			String ikategorija = (String) jsons.get(i).get("kategorija");
			String islika = (String) jsons.get(i).get("slika");
			String ivideo = (String) jsons.get(i).get("video");
			String iocena = (String) jsons.get(i).get("ocena");
			String ikolicina = (String) jsons.get(i).get("magacin");
			JSONArray irecenzije= (JSONArray) jsons.get(i).get("recenzije");
			JSONArray iprodavnice= (JSONArray) jsons.get(i).get("prodavnice");
			
			
			
			Proizvod p = new Proizvod();
			p.setSifra(isifra);
			p.setNaziv(inaziv);
			p.setBoja(iboja);
			p.setDimenzije(idimenzije);
			p.setTezina(itezina);
			p.setZemljaproizvodnje(izemlja);
			p.setProizvodjac(iproizvodjac);
			p.setJedinicnacena(ijedinicnaCena);
			p.setKategorija(ikategorija);
			p.setSlika(islika);
			p.setVideo(ivideo);
			p.setOcena(iocena);
			p.setMagacin(ikolicina);
			if(irecenzije == null){
				p.setRecenzije(null);
			}else{
				String jsonString = irecenzije.toJSONString();
				ObjectMapper mapper = new ObjectMapper();
				ArrayList<Recenzija> list = mapper.readValue(jsonString, 
						    new TypeReference<ArrayList<Recenzija>>() {});
			
			p.setRecenzije(list);
						
			
			}
			
			if(iprodavnice == null){
				p.setProdavnica(null);
			}else{
				String jsonString = iprodavnice.toJSONString();
				ObjectMapper mapper = new ObjectMapper();
				ArrayList<String> list = mapper.readValue(jsonString, 
						    new TypeReference<ArrayList<String>>() {});
			
			p.setProdavnica(list);
			
			}
			
			
			

			pr.getPr().add(p);
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

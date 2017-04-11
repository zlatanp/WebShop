package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Korisnici;
import beans.Korisnik;
import beans.Prodavac;
import beans.Prodavci;

@Path("/login")
public class LoginService {
	@Context
	ServletConfig config;

	@POST
	@Path("/trylogin")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Response register(@CookieParam("username") javax.ws.rs.core.Cookie id, @FormParam("username") String username, @FormParam("password") String lozinka) throws IOException, URISyntaxException {
		int z = 0;		
		URI uriFailed = new URI("/WebShop/loginFailed.html");
		URI uriSucces = new URI("/WebShop/home.html");
		URI prodavac = new URI("/WebShop/seller.html");
		
		if (username.isEmpty() || lozinka.isEmpty()) {
			
			return Response.seeOther(uriFailed).build(); //ako su prazna polja salji me na failed
		}
		String s = config.getServletContext().getRealPath("");
		Korisnici kor = listaRegistrovanih(s);
		for(int i=0;i<kor.getListakorisnika().size();i++){
			if(kor.getListakorisnika().get(i).getUsername().equals(username) && kor.getListakorisnika().get(i).getPassword().equals(lozinka)){
				
					//return Response.seeOther(uriSucces).cookie(ck).build();
				z++;
				return Response.seeOther(uriSucces).build(); //ako postoji takav korisnik uspesno se ulogovao
			}
		}

		if(z==0){
			Prodavci p = listaProdavaca(s);
			for(int i=0;i<p.getListaProdavaca().size();i++){
				if(p.getListaProdavaca().get(i).getUsername().equals(username) && p.getListaProdavaca().get(i).getPass().equals(lozinka)){
					z=0;
					System.out.println("ulogovao se prodavac");
					return Response.seeOther(prodavac).build();
				}
			}
			
		}
		//inace je failed
		
		return Response.seeOther(uriFailed).build();
	}
	
	/*
	@GET
	@Path("/logon")
	@Produces(MediaType.TEXT_HTML)
	public Response dajkolacic(@CookieParam("username") javax.ws.rs.core.Cookie id) throws URISyntaxException {
		URI uriSucces = new URI("loginSucces.html");
		if (id == null) {
			NewCookie ck = new NewCookie("username", "Perin kolacic");
			return Response.seeOther(uriSucces).cookie(ck).build();
		}
		return Response.seeOther(uriSucces).build();
	}
*/
	// Funkcija za citanje registrovanih
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
//**************** lista prodavaca
	@SuppressWarnings("unchecked")
	public static synchronized Prodavci listaProdavaca(String gde) {
		Prodavci pr = new Prodavci();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Prodavci.txt";
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON1(new File(FileName), "UTF-8");
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
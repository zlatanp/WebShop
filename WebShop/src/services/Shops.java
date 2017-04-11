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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Prodavnica;
import beans.Prodavnice;

@Path("/prodavnice")
public class Shops {
	@Context
	ServletConfig config;
	
	//DAJE SPISAK SVIH PRODAVNICA
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Prodavnica> sveProdavnice() {
		
		String s = config.getServletContext().getRealPath("");
		Prodavnice p = listaProdavnica(s);
		
		return p.getListaprodavnica();
	}
	
	//VRACA PRODAVNICU KOJU HTEO DA MENJA PA ODUSTAO
	@GET
	@Path("/odredjena/{sifraProdavnice}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Prodavnica vratiProdavnicu(@PathParam("sifraProdavnice") String sifraProdavnice) {
		String s = config.getServletContext().getRealPath("");
		Prodavnice p = listaProdavnica(s);
		Prodavnica prod = new Prodavnica();
		String z = new String();
		for(int i =1;i<sifraProdavnice.length()-1;i++)
			z+=sifraProdavnice.toCharArray()[i];
		
		for(int i=0;i<p.getListaprodavnica().size();i++){
			if(p.getListaprodavnica().get(i).getSifra().equals(z))
					prod=p.getListaprodavnica().get(i);
		}
		return prod;
	}
	
	@GET
	@Path("/trazim/{stringneki}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Prodavnica> vratiProdavnice(@PathParam("stringneki") String stringneki) {
		String s = config.getServletContext().getRealPath("");
		Prodavnice p = listaProdavnica(s);
		Prodavnice zavracanje = new Prodavnice();

		String pravoime = new String();
		String veliki = stringneki.toUpperCase();
		for(int i =0;i<1;i++)
			pravoime+=veliki.toCharArray()[i];
		
		for(int i=1;i<stringneki.length();i++)
			pravoime+=stringneki.toCharArray()[i];
		
		System.out.println(pravoime);
		
		for(int i=0;i<p.getListaprodavnica().size();i++){
			if(p.getListaprodavnica().get(i).getNaziv().contains(pravoime))
					zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
		}
		return zavracanje.getListaprodavnica();
	}
	
	@GET
	@Path("/trazim/{ime}/{ocena}/{drzava}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Prodavnica> vratiProdavnicee(@PathParam("ime") String ime,@PathParam("ocena") String ocena, @PathParam("drzava") String drzava) {
		String s = config.getServletContext().getRealPath("");
		Prodavnice p = listaProdavnica(s);
		Prodavnice zavracanje = new Prodavnice();
		boolean nemaga = true;
		String pravoime = new String();
		String veliki = ime.toUpperCase();
		for(int i =0;i<1;i++)
			pravoime+=veliki.toCharArray()[i];
		
		for(int i=1;i<ime.length();i++)
			pravoime+=ime.toCharArray()[i];
		
		
		
		String pravadrzava = new String();
		String velika = drzava.toUpperCase();
		for(int i =0;i<1;i++)
			pravadrzava+=velika.toCharArray()[i];
		
		for(int i=1;i<drzava.length();i++)
			pravadrzava+=drzava.toCharArray()[i];
		
		
		if(!pravoime.equals("Nema")){
			for(int i=0;i<p.getListaprodavnica().size();i++){
				if(p.getListaprodavnica().get(i).getNaziv().contains(pravoime)){
					if(zavracanje.getListaprodavnica().size()==0){
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}else{
						for(int z=0;z<zavracanje.getListaprodavnica().size();z++){
							if(zavracanje.getListaprodavnica().get(z).getSifra().equals(p.getListaprodavnica().get(i).getSifra()))
								nemaga = false;
						}
						if(nemaga)
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}
				}
			}
		}
		
		if(!pravadrzava.equals("Nema")){
			for(int i=0;i<p.getListaprodavnica().size();i++){
				if(p.getListaprodavnica().get(i).getDrzava().contains(pravadrzava)){
					if(zavracanje.getListaprodavnica().size()==0){
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}else{
						for(int z=0;z<zavracanje.getListaprodavnica().size();z++){
							if((zavracanje.getListaprodavnica().get(z).getSifra().equals(p.getListaprodavnica().get(i).getSifra())))
								nemaga = false;
						}
						if(nemaga)
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}
				}
			}
		}
		
		if(!ocena.equals("nema")){
			for(int i=0;i<p.getListaprodavnica().size();i++){
				if(p.getListaprodavnica().get(i).getOcena().equals(ocena)){
					if(zavracanje.getListaprodavnica().size()==0){
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}else{
						for(int z=0;z<zavracanje.getListaprodavnica().size();z++){
							if((zavracanje.getListaprodavnica().get(z).getSifra().equals(p.getListaprodavnica().get(i).getSifra()))){
								nemaga = false;
							}
						}
						if(nemaga)
						zavracanje.getListaprodavnica().add(p.getListaprodavnica().get(i));
					}
				}
			}
		}
		
			
			
		return zavracanje.getListaprodavnica();
	}
	
	//MENJA JEDNU PRODAVNICU
	@POST
	@Path("/izmeni/{sifraProdavnice}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Prodavnica menjanjeProdavnice(@PathParam("sifraProdavnice") String sifraProdavnice, Prodavnica pr) throws FileNotFoundException {
		 Prodavnica p = pr;
		 String s = config.getServletContext().getRealPath("");
		 String path = FilePaths.getPath(s).getPath();
		 Prodavnice prodavnice = listaProdavnica(s);
		 
		 for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				if(prodavnice.getListaprodavnica().get(i).getSifra().equals(sifraProdavnice)){
						prodavnice.getListaprodavnica().get(i).setDrzava(p.getDrzava());
						prodavnice.getListaprodavnica().get(i).setAdresa(p.getAdresa());
						prodavnice.getListaprodavnica().get(i).setTelefon(p.getTelefon());
						prodavnice.getListaprodavnica().get(i).setEmail(p.getEmail());
						prodavnice.getListaprodavnica().get(i).setOcena(p.getOcena());
						prodavnice.getListaprodavnica().get(i).setRecenzije(p.getRecenzije());
						prodavnice.getListaprodavnica().get(i).setOdgovorniProdavac(p.getOdgovorniProdavac());
						prodavnice.getListaprodavnica().get(i).setNaziv(p.getNaziv());
						break;
				}
			}
		 
		 PrintWriter writer = new PrintWriter(path + "\\Prodavnice.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				zapisiUbazu(s, prodavnice.getListaprodavnica().get(i).getSifra(), prodavnice.getListaprodavnica().get(i).getNaziv(), prodavnice.getListaprodavnica().get(i).getAdresa(), prodavnice.getListaprodavnica().get(i).getDrzava(), prodavnice.getListaprodavnica().get(i).getTelefon(), prodavnice.getListaprodavnica().get(i).getEmail(), prodavnice.getListaprodavnica().get(i).getOcena(), prodavnice.getListaprodavnica().get(i).getRecenzije(), prodavnice.getListaprodavnica().get(i).getOdgovorniProdavac());
			}
			
			return p;
		}
	
	@POST
	@Path("/izmeniSeller/{sifraProdavnice}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Prodavnica menjanjeProdavniceSeller(@PathParam("sifraProdavnice") String sifraProdavnice, Prodavnica pr) throws FileNotFoundException {
		 Prodavnica p = pr;
		 String s = config.getServletContext().getRealPath("");
		 String path = FilePaths.getPath(s).getPath();
		 Prodavnice prodavnice = listaProdavnica(s);
		 
		 for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				if(prodavnice.getListaprodavnica().get(i).getSifra().equals(sifraProdavnice)){
						prodavnice.getListaprodavnica().get(i).setDrzava(p.getDrzava());
						prodavnice.getListaprodavnica().get(i).setAdresa(p.getAdresa());
						prodavnice.getListaprodavnica().get(i).setTelefon(p.getTelefon());
						prodavnice.getListaprodavnica().get(i).setEmail(p.getEmail());
						prodavnice.getListaprodavnica().get(i).setOcena(p.getOcena());
						prodavnice.getListaprodavnica().get(i).setRecenzije(p.getRecenzije());
						prodavnice.getListaprodavnica().get(i).setNaziv(p.getNaziv());
						break;
				}
			}
		 
		 PrintWriter writer = new PrintWriter(path + "\\Prodavnice.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<prodavnice.getListaprodavnica().size();i++){
				zapisiUbazu(s, prodavnice.getListaprodavnica().get(i).getSifra(), prodavnice.getListaprodavnica().get(i).getNaziv(), prodavnice.getListaprodavnica().get(i).getAdresa(), prodavnice.getListaprodavnica().get(i).getDrzava(), prodavnice.getListaprodavnica().get(i).getTelefon(), prodavnice.getListaprodavnica().get(i).getEmail(), prodavnice.getListaprodavnica().get(i).getOcena(), prodavnice.getListaprodavnica().get(i).getRecenzije(), prodavnice.getListaprodavnica().get(i).getOdgovorniProdavac());
			}
			
			return p;
		}
	
	//BRISE JEDNU PRODAVNICU
	@GET
	@Path("/obrisi/{sifraProdavnice}")
	public Response brisiProdavnicu(@PathParam("sifraProdavnice") String sifraProdavnice) throws URISyntaxException, FileNotFoundException {
		
		String s = config.getServletContext().getRealPath("");
		String path = FilePaths.getPath(s).getPath();
		Prodavnice p = listaProdavnica(s);
		URI uriSucces = new URI("/WebShop/shops.html");
		
		for(int i=0;i<p.getListaprodavnica().size();i++){
			if(p.getListaprodavnica().get(i).getSifra().equals(sifraProdavnice))
					p.getListaprodavnica().remove(i); //obrisao prodavnicu iz kolekcije, sad je upisi kolekciju nazad u fajl.
		}
		
		PrintWriter writer = new PrintWriter(path + "\\Prodavnice.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<p.getListaprodavnica().size();i++){
			zapisiUbazu(s, p.getListaprodavnica().get(i).getSifra(), p.getListaprodavnica().get(i).getNaziv(), p.getListaprodavnica().get(i).getAdresa(), p.getListaprodavnica().get(i).getDrzava(), p.getListaprodavnica().get(i).getTelefon(), p.getListaprodavnica().get(i).getEmail(), p.getListaprodavnica().get(i).getOcena(), p.getListaprodavnica().get(i).getRecenzije(), p.getListaprodavnica().get(i).getOdgovorniProdavac());
		}
		
		
		
		return Response.seeOther(uriSucces).build();
		
	}
	
	//DODAJ NOVU PRODAVNICU
	
	@POST
	@Path("/add")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized Response register(@FormParam("ime") String ime, @FormParam("sifra") String sifra,
			@FormParam("telefon") String telefon,
			@FormParam("email") String email, @FormParam("adresa") String adresa, @FormParam("drzava") String drzava,
			@FormParam("ocena") String ocena, @FormParam("prodavac") String prodavac)
			throws IOException, URISyntaxException {
		
		String s = config.getServletContext().getRealPath("");
		Prodavnice p = listaProdavnica(s);
		String path = FilePaths.getPath(s).getPath();
		URI uriFailed = new URI("/WebShop/shopEnterFailed.html");
		URI uriSucces = new URI("/WebShop/shopEnterSucces.html");
		
		for(int j = 0; j<p.getListaprodavnica().size();j++){
			if(p.getListaprodavnica().get(j).getSifra().equals(sifra))
				return Response.seeOther(uriFailed).build(); 
		}
		
		for(int j = 0; j<p.getListaprodavnica().size();j++){
			if(p.getListaprodavnica().get(j).getNaziv().equals(ime))
				return Response.seeOther(uriFailed).build(); 
		}
		
		if (ime.isEmpty() || sifra.isEmpty() || telefon.isEmpty() || email.isEmpty() || adresa.isEmpty() || drzava.isEmpty() || prodavac.isEmpty()) {
			
			return Response.seeOther(uriFailed).build(); //ako su prazna polja salji me na failed
		}
		
		
		Prodavnica novaprodavnica = new Prodavnica();
		novaprodavnica.setNaziv(ime);
		novaprodavnica.setSifra(sifra);
		novaprodavnica.setDrzava(drzava);
		novaprodavnica.setTelefon(telefon);
		novaprodavnica.setEmail(email);
		novaprodavnica.setAdresa(adresa);
		novaprodavnica.setOdgovorniProdavac(prodavac);
		novaprodavnica.setRecenzije(null);
		if(ocena.equals("")){
			novaprodavnica.setOcena("");
		}else{
			novaprodavnica.setOcena(ocena);
		}
		p.getListaprodavnica().add(novaprodavnica);
		//nakon sto napravio novu prodavnicu ubaci je u bazu
		PrintWriter writer = new PrintWriter(path + "\\Prodavnice.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<p.getListaprodavnica().size();i++){
			zapisiUbazu(s, p.getListaprodavnica().get(i).getSifra(), p.getListaprodavnica().get(i).getNaziv(), p.getListaprodavnica().get(i).getAdresa(), p.getListaprodavnica().get(i).getDrzava(), p.getListaprodavnica().get(i).getTelefon(), p.getListaprodavnica().get(i).getEmail(), p.getListaprodavnica().get(i).getOcena(), p.getListaprodavnica().get(i).getRecenzije(), p.getListaprodavnica().get(i).getOdgovorniProdavac());
		}
		
		return Response.seeOther(uriSucces).build();
	}
	
	
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
			jsons = ReadJSON(new File(FileName), "UTF-8");
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
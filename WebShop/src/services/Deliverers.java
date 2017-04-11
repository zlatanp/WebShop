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

import beans.Dostavljac;
import beans.Dostavljaci;

@Path("/dostavljaci")
public class Deliverers {
	@Context
	ServletConfig config;
	//DAJE SPISAK SVIH DOSTAVLJACAS
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Dostavljac> sviDostavljaci() {
		
		String s = config.getServletContext().getRealPath("");
		Dostavljaci d = listaDostavljaca(s);
		
		return d.getListaDostavljaca();
	}
	
	//VRACA DOSTAVLJACA KOJi HTEO DA MENJA PA ODUSTAO
		@GET
		@Path("/odredjena/{sifraDostavljaca}") //za izmenu, kada posnisti menjanje odredjene prodavnice
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Dostavljac vratiDostavljaca(@PathParam("sifraDostavljaca") String sifraDostavljaca) {
			String s = config.getServletContext().getRealPath("");
			Dostavljaci dost = listaDostavljaca(s);
			Dostavljac d = new Dostavljac();
			String z = new String();
			for(int i =1;i<sifraDostavljaca.length()-1;i++)
				z+=sifraDostavljaca.toCharArray()[i];
			
			for(int i=0;i<dost.getListaDostavljaca().size();i++){
				if(dost.getListaDostavljaca().get(i).getSifra().equals(z))
						d=dost.getListaDostavljaca().get(i);
			}
			return d;
		}
	
	//BRISE JEDNOG DOSTAVLJACA
		@GET
		@Path("/obrisi/{sifraDostavljaca}")
		public Response brisiDostavljaca(@PathParam("sifraDostavljaca") String sifraDostavljaca) throws URISyntaxException, FileNotFoundException {
			
			String s = config.getServletContext().getRealPath("");
			String path = FilePaths.getPath(s).getPath();
			Dostavljaci d = listaDostavljaca(s);
			URI uriSucces = new URI("/WebShop/deliverers.html");
			
			for(int i=0;i<d.getListaDostavljaca().size();i++){
				if(d.getListaDostavljaca().get(i).getSifra().equals(sifraDostavljaca))
						d.getListaDostavljaca().remove(i); //obrisao prodavnicu iz kolekcije, sad je upisi kolekciju nazad u fajl.
			}
			
			PrintWriter writer = new PrintWriter(path + "\\Dostavljaci.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<d.getListaDostavljaca().size();i++){
				zapisiUbazu(s, d.getListaDostavljaca().get(i).getSifra(), d.getListaDostavljaca().get(i).getNaziv(), d.getListaDostavljaca().get(i).getOpis(), d.getListaDostavljaca().get(i).getDrzave(), d.getListaDostavljaca().get(i).getTarife());
			}
			
			
			
			return Response.seeOther(uriSucces).build();
			
		}
		
		//MENJA JEDNOG DOSTAVLJACA
		@POST
		@Path("/izmeni/{sifraDostavljaca}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Dostavljac menjanjeDostavljaca(@PathParam("sifraDostavljaca") String sifraDostavljaca, Dostavljac d) throws FileNotFoundException {
			 Dostavljac dd = d;
			 String s = config.getServletContext().getRealPath("");
			 String path = FilePaths.getPath(s).getPath();
			 Dostavljaci dostavljaci = listaDostavljaca(s);
			 
			 for(int i=0;i<dostavljaci.getListaDostavljaca().size();i++){
					if(dostavljaci.getListaDostavljaca().get(i).getSifra().equals(sifraDostavljaca)){
						dostavljaci.getListaDostavljaca().get(i).setNaziv(dd.getNaziv());
						dostavljaci.getListaDostavljaca().get(i).setDrzave(dd.getDrzave());
						dostavljaci.getListaDostavljaca().get(i).setOpis(dd.getOpis());
						dostavljaci.getListaDostavljaca().get(i).setTarife(dd.getTarife());
							break;
					}
				}
			 
			 PrintWriter writer = new PrintWriter(path + "\\Dostavljaci.txt");
				writer.print("");
				writer.close();
				
				for(int i=0;i<dostavljaci.getListaDostavljaca().size();i++){
					zapisiUbazu(s, dostavljaci.getListaDostavljaca().get(i).getSifra(), dostavljaci.getListaDostavljaca().get(i).getNaziv(), dostavljaci.getListaDostavljaca().get(i).getOpis(), dostavljaci.getListaDostavljaca().get(i).getDrzave(), dostavljaci.getListaDostavljaca().get(i).getTarife());
				}
				
				return dd;
			}
	
		//DODAJ NOVOG DOSTAVLJACA
		
		@POST
		@Path("/add")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public synchronized Response register(@FormParam("naziv") String naziv, @FormParam("sifra") String sifra,
				@FormParam("opis") String opis,
				@FormParam("drzaveposlovanja") String drzave, @FormParam("tarife") String tarife)
				throws IOException, URISyntaxException {
			
			String s = config.getServletContext().getRealPath("");
			Dostavljaci p = listaDostavljaca(s);
			String path = FilePaths.getPath(s).getPath();
			
			URI uriFailed = new URI("/WebShop/delivererEnterFailed.html");
			URI uriSucces = new URI("/WebShop/delivererEnterSucces.html");
			
			for(int j =0;j<p.getListaDostavljaca().size();j++){		//ako pokusa dodati dostavljaca sa vec postojecom sifrom
				if(p.getListaDostavljaca().get(j).getSifra().equals(sifra))
					return Response.seeOther(uriFailed).build();
			}
			
			if (naziv.isEmpty() || sifra.isEmpty() || opis.isEmpty() || drzave.isEmpty() || tarife.isEmpty()) {
				
				return Response.seeOther(uriFailed).build(); //ako su prazna polja salji me na failed
			}
			
			
			Dostavljac d = new Dostavljac();
			d.setNaziv(naziv);
			d.setDrzave(drzave);
			d.setOpis(opis);
			d.setSifra(sifra);
			d.setTarife(tarife);
			
			p.getListaDostavljaca().add(d);
			//nakon sto napravio novu prodavnicu ubaci je u bazu
			PrintWriter writer = new PrintWriter(path + "\\Dostavljaci.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<p.getListaDostavljaca().size();i++){
				zapisiUbazu(s, p.getListaDostavljaca().get(i).getSifra(), p.getListaDostavljaca().get(i).getNaziv(), p.getListaDostavljaca().get(i).getOpis(), p.getListaDostavljaca().get(i).getDrzave(), p.getListaDostavljaca().get(i).getTarife());
			}
			
			return Response.seeOther(uriSucces).build();
		}
	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String sifra, String naziv, String opis, String drzave, String tarife) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("sifra", sifra);
		obj.put("naziv", naziv);
		obj.put("opis", opis);
		obj.put("drzave", drzave);
		obj.put("tarife", tarife);
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Dostavljaci.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje prodavnica
	public static synchronized Dostavljaci listaDostavljaca(String gde) {
		Dostavljaci dost = new Dostavljaci();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Dostavljaci.txt";
		
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
			String iopis = (String) jsons.get(i).get("opis");
			String idrzave = (String) jsons.get(i).get("drzave");
			String itarife = (String) jsons.get(i).get("tarife");
			
			Dostavljac d = new Dostavljac();
			

			
			d.setSifra(isifra);
			d.setNaziv(inaziv);
			d.setOpis(iopis);
			d.setDrzave(idrzave);
			d.setTarife(itarife);
						

			dost.getListaDostavljaca().add(d);
		}

		return dost;
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

	

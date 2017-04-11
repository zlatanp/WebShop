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

import beans.Kategorija;
import beans.Kategorije;

@Path("/kategorije")
public class Category {
	@Context
	ServletConfig config;
	
	//DAJE SPISAK SVIH KATEGORIJA
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public synchronized ArrayList<Kategorija> sveKategorije() throws JsonParseException, JsonMappingException, IOException {
		
		
		String s = config.getServletContext().getRealPath("");
		Kategorije d = listaKategorija(s);
		
		return d.getListaKategorija();
	}
	
	//DODAJ NOVU KATEGORIJU
	
		@POST
		@Path("/add")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public synchronized Response dodajnovukategoriju(@FormParam("naziv") String naziv, @FormParam("opis") String opis,
				@FormParam("imalPodkategorije") String imalpodkategorije, @FormParam("nazivpodkategorije") String nazivpodkategorije,
				@FormParam("opispodkategorije") String opispodkategorije)
				throws IOException, URISyntaxException {
			
		
			String s = config.getServletContext().getRealPath("");
			Kategorije p = listaKategorija(s);
			String path = FilePaths.getPath(s).getPath();
			URI uriSucces = new URI("/WebShop/kontrolPanel.html");
			URI uriFailed = new URI("/WebShop/kontrolPanel.html");
			
			Kategorija kat = new Kategorija();
			Kategorija podkategorijaKategorije = new Kategorija();
			ArrayList<Kategorija> podkat = new ArrayList<Kategorija>();
			
			for(int j = 0; j<p.getListaKategorija().size();j++){
				if(p.getListaKategorija().get(j).getNaziv().equals(naziv))
					return Response.seeOther(uriFailed).build(); 
			}

			if (naziv.isEmpty() || opis.isEmpty()) {
				
				return Response.seeOther(uriFailed).build(); //ako su prazna polja salji me na failed
			}
			
			if(imalpodkategorije.equals("nema")){
				kat.setNaziv(naziv);
				kat.setOpis(opis);
				kat.setPodkategorija(null);
				
			}else{
				
				podkategorijaKategorije.setNaziv(nazivpodkategorije);
				podkategorijaKategorije.setOpis(opispodkategorije);
				podkategorijaKategorije.setPodkategorija(null);
				podkat.add(podkategorijaKategorije);
							
				
				kat.setNaziv(naziv);
				kat.setOpis(opis);
				kat.setPodkategorija(podkat);
				
				
			}
			
				p.getListaKategorija().add(kat);
			
			//nakon sto napravio novu prodavnicu ubaci je u bazu
			PrintWriter writer = new PrintWriter(path + "\\Kategorije.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<p.getListaKategorija().size();i++){
				zapisiUbazu(s, p.getListaKategorija().get(i).getNaziv(),p.getListaKategorija().get(i).getOpis(),p.getListaKategorija().get(i).getPodkategorija());
			}
			
			return Response.seeOther(uriSucces).build();
		}
		//BRISE JEDNU KATEGORIJU
		@GET
		@Path("/obrisi/{id}")
		public Response brisiKategoriju(@PathParam("id") String imeKategorije) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
			
			String s = config.getServletContext().getRealPath("");
			String path = FilePaths.getPath(s).getPath();
			Kategorije k = listaKategorija(s);
			URI uriSucces = new URI("/WebShop/home.html");
			
			String z = new String();
			for(int i =1;i<imeKategorije.length()-1;i++)
				z+=imeKategorije.toCharArray()[i];
			
			if(z.isEmpty())
				return Response.seeOther(uriSucces).build(); 
			
			for(int i=0;i<k.getListaKategorija().size();i++){
				System.out.println(imeKategorije);
				if(k.getListaKategorija().get(i).getNaziv().equals(z))
					k.getListaKategorija().remove(i); //obrisao prodavnicu iz kolekcije, sad je upisi kolekciju nazad u fajl.

			}
			
			PrintWriter writer = new PrintWriter(path + "\\Kategorije.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<k.getListaKategorija().size();i++){
				zapisiUbazu(s, k.getListaKategorija().get(i).getNaziv(),k.getListaKategorija().get(i).getOpis(),k.getListaKategorija().get(i).getPodkategorija());
				}
			
			
			
			return Response.seeOther(uriSucces).build();
			
		}
		
		@GET
		@Path("/izmeni/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Kategorija vratiKategoriju(@PathParam("id") String id) throws JsonParseException, JsonMappingException, IOException {
			 Kategorija kat = new Kategorija();
			 String s = config.getServletContext().getRealPath("");
			 Kategorije kategorije = listaKategorija(s);
			 
			 String z = new String();
				for(int i =1;i<id.length()-1;i++)
					z+=id.toCharArray()[i];
			 
				if(z.isEmpty()){
					return kat;
				}
				
			 for(int i=0;i<kategorije.getListaKategorija().size();i++){
					if(kategorije.getListaKategorija().get(i).getNaziv().equals(z)){
						kat = kategorije.getListaKategorija().get(i);
						break;
					}
				}
			 				
				return kat;
			}
		
		//brise podkategoriju kategorije
		@GET
		@Path("/obrisiPodkat/{idKat}/{idPodkat}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Kategorija brisiPodkategoriju(@PathParam("idKat") String idKat, @PathParam("idPodkat") String idPodkat) throws JsonParseException, JsonMappingException, IOException {
			 Kategorija kat = new Kategorija();
			 String s = config.getServletContext().getRealPath("");
			 String path = FilePaths.getPath(s).getPath();
			 Kategorije kategorije = listaKategorija(s);
			 
			 String z = new String(); //ime kategorije
				for(int i =1;i<idKat.length()-1;i++)
					z+=idKat.toCharArray()[i];
			 
			String z1 = new String(); //ime podkategorije
				for(int i =1;i<idPodkat.length()-1;i++)
					z1+=idPodkat.toCharArray()[i];
			 
				if(z.isEmpty() || z.isEmpty()){
					return kat;
				}
				
			 for(int i=0;i<kategorije.getListaKategorija().size();i++){
					if(kategorije.getListaKategorija().get(i).getNaziv().equals(z)){
						kat = kategorije.getListaKategorija().get(i);
						break;
					}
				}
			 
			 for(int i =0;i<kat.getPodkategorija().size();i++){
				 if(kat.getPodkategorija().get(i).getNaziv().equals(z1)){
					 kat.getPodkategorija().remove(i);
				 }
			 }
			 
			 //kategorije.getListaKategorija().add(kat);
			 
			 PrintWriter writer = new PrintWriter(path + "\\Kategorije.txt");
				writer.print("");
				writer.close();
				
				for(int i=0;i<kategorije.getListaKategorija().size();i++){
					zapisiUbazu(s, kategorije.getListaKategorija().get(i).getNaziv(),kategorije.getListaKategorija().get(i).getOpis(),kategorije.getListaKategorija().get(i).getPodkategorija());
					}
				
				
			 				
				return kat;
			}
		
		@GET
		@Path("/dodajPodkategoriju/{idKat}/{idPodkat}/{novoImeKat}/{noviOpisKat}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Kategorija dodajPodkategoriju(@PathParam("idKat") String idKat, @PathParam("idPodkat") String idPodkat, @PathParam("novoImeKat") String novoimekat, @PathParam("noviOpisKat") String noviopiskat) throws JsonParseException, JsonMappingException, IOException {
			 Kategorija podkategorija = new Kategorija();
			 Kategorija kategorija = new Kategorija();
			 String s = config.getServletContext().getRealPath("");
			 String path = FilePaths.getPath(s).getPath();
			 Kategorije kategorije = listaKategorija(s);
			 
			 String z = new String(); //ime kategorije
				for(int i =1;i<idKat.length()-1;i++)
					z+=idKat.toCharArray()[i];
			 
			String z1 = new String(); //ime podkategorije
				for(int i =1;i<idPodkat.length()-1;i++)
					z1+=idPodkat.toCharArray()[i];
				
				String novoime = new String(); //novo ime podkategorije
				for(int i =1;i<novoimekat.length()-1;i++)
					novoime+=novoimekat.toCharArray()[i];
				
				String noviopis = new String(); //novi opis podkategorije
				for(int i =1;i<noviopiskat.length();i++)
					noviopis+=noviopiskat.toCharArray()[i];
			 
				if(z.isEmpty() || z1.isEmpty() || novoime.isEmpty() || noviopis.isEmpty()){
					return kategorija;
				}
				
				if(!z1.equals("null")){
					for(int i=0;i<kategorije.getListaKategorija().size();i++){
						if(kategorije.getListaKategorija().get(i).getNaziv().equals(z1)){
							podkategorija = kategorije.getListaKategorija().get(i);
							System.out.println("podkategorija " + podkategorija.getNaziv());
							break;
						}
					}
			 
			 //kat je ta kategorija koja postaje podkategorija
			 
			 
			 for(int i=0;i<kategorije.getListaKategorija().size();i++){
					if(kategorije.getListaKategorija().get(i).getNaziv().equals(z)){
						kategorija = kategorije.getListaKategorija().get(i);
						System.out.println("kategorija " + kategorija.getNaziv());
						break;
					}
				}
			 
			 //katkojojdodajes je kategorija koja dobija novu podkategoriju
			 if(kategorija.getPodkategorija()==null){
				 ArrayList<Kategorija> listapodkategorija = new ArrayList<Kategorija>();
				 listapodkategorija.add(podkategorija);
				 kategorija.setPodkategorija(listapodkategorija);
			 }else{
				 kategorija.getPodkategorija().add(podkategorija); 
			 }

				
			 for(int i =0;i<kategorije.getListaKategorija().size();i++){
				 if(kategorije.getListaKategorija().get(i).getNaziv().equals(z)){
					 kategorije.getListaKategorija().get(i).setPodkategorija(kategorija.getPodkategorija());
						break;
				 }
			 }
			 
				}
				
			 for(int i =0;i<kategorije.getListaKategorija().size();i++){
				 if(kategorije.getListaKategorija().get(i).getNaziv().equals(z)){

						 kategorije.getListaKategorija().get(i).setNaziv(novoime);
						 kategorije.getListaKategorija().get(i).setOpis(noviopis);
						 break;
				 }
			 }
			 
			 PrintWriter writer = new PrintWriter(path + "\\Kategorije.txt");
				writer.print("");
				writer.close();
				
				for(int i=0;i<kategorije.getListaKategorija().size();i++){
					zapisiUbazu(s, kategorije.getListaKategorija().get(i).getNaziv(),kategorije.getListaKategorija().get(i).getOpis(),kategorije.getListaKategorija().get(i).getPodkategorija());
					}
				
				
			 				
				return kategorija;
			}
	////////////////////////////////////////
		@SuppressWarnings("unchecked")
		public static synchronized void zapisiUbazu(String gde, String naziv, String opis, ArrayList<Kategorija> kat) {

			String path = FilePaths.getPath(gde).getPath();
			JSONObject obj = new JSONObject();
			obj.put("naziv", naziv);
			obj.put("opis", opis);
			
			if(kat == null){
			obj.put("podkategorija", kat);
	
			}else{
				JSONArray podkategorija = new JSONArray();
				
				for(int k = 0;k<kat.size();k++){
				  JSONObject pod = new JSONObject();
					pod.put("naziv", kat.get(k).getNaziv());
					pod.put("opis", kat.get(k).getOpis());
					pod.put("podkategorija", null);
			
					podkategorija.add(pod);
				}
				
			
				obj.put("podkategorija", podkategorija);
			}
			
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(path + "\\Kategorije.txt", true));
				writer.write(obj.toJSONString());
				writer.newLine();
				writer.close();
			} catch (Exception e) {
				javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}

		}

	// Funkcija za citanje prodavnica
	public static synchronized Kategorije listaKategorija(String gde) throws JsonParseException, JsonMappingException, IOException {
		Kategorije kat = new Kategorije();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Kategorije.txt";
		
		ArrayList<JSONObject> jsons = new ArrayList<JSONObject>();
		try {
			jsons = ReadJSON(new File(FileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < jsons.size(); i++) {
			String inaziv = (String) jsons.get(i).get("naziv");
			String iopis = (String) jsons.get(i).get("opis");
			JSONArray ipodkategorije= (JSONArray) jsons.get(i).get("podkategorija");
			//System.out.println(ipodkategorije);
			Kategorija k = new Kategorija();
			k.setNaziv(inaziv);
			k.setOpis(iopis);
		
			if(ipodkategorije == null){
				k.setPodkategorija(null);
			}else{
				String jsonString = ipodkategorije.toJSONString();
				ObjectMapper mapper = new ObjectMapper();
				ArrayList<Kategorija> list = mapper.readValue(jsonString, 
						    new TypeReference<ArrayList<Kategorija>>() {});
			
			k.setPodkategorija(list);
						
			}
			kat.getListaKategorija().add(k);
		
			}
		return kat;
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



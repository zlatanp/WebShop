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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Proizvod;
import beans.Proizvodi;
import beans.Recenzija;

@Path("/item")
public class Itemi {

	@Context
	ServletConfig config;
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public synchronized Response register(Proizvod p)
			throws IOException, URISyntaxException {
		
		String s = config.getServletContext().getRealPath("");
		String path = FilePaths.getPath(s).getPath();
		URI uriFailed = new URI("/WebShop/addItemFailed.html");
		URI uriSucces = new URI("/WebShop/addItem.html");
		Proizvodi proizvodi = listaProizvoda(s);
		/*if(p.getBoja().isEmpty() || p.getDimenzije().isEmpty() || p.getJedinicnacena().isEmpty() || p.getKategorija().isEmpty() || p.getMagacin().isEmpty() || p.getNaziv().isEmpty() || p.getProizvodjac().isEmpty()){
			return Response.seeOther(uriFailed).build();
	}*/
		
		for(int i =0;i<proizvodi.getPr().size();i++){
			if(p.getSifra().equals(proizvodi.getPr().get(i).getSifra())){
				return Response.seeOther(uriFailed).build();
			}
		}
		
		Proizvod proizv = new Proizvod();
		proizv.setNaziv(p.getNaziv());
		proizv.setSifra(p.getSifra());
		proizv.setBoja(p.getBoja());
		proizv.setDimenzije(p.getDimenzije());
		proizv.setTezina(p.getTezina());
		proizv.setZemljaproizvodnje(p.getZemljaproizvodnje());
		proizv.setProizvodjac(p.getProizvodjac());
		proizv.setJedinicnacena(p.getJedinicnacena());
		proizv.setKategorija(p.getKategorija());
		proizv.setSlika(p.getSlika());
		proizv.setVideo(p.getVideo());
		if(p.getOcena().isEmpty()){
		proizv.setOcena("");
		}else{
			proizv.setOcena(p.getOcena());
		}
		
		proizv.setRecenzije(null);

		
		proizv.setMagacin(p.getMagacin());
		if(p.getProdavnica().size() != 0){
			for(int i =0;i<p.getProdavnica().size();i++){
				proizv.getProdavnica().add(p.getProdavnica().get(i));
				
		}
		}else{
			proizv.setProdavnica(null);
		}
		
		
		
		proizvodi.getPr().add(proizv);
		
		//nakon sto napravio novu prodavnicu ubaci je u bazu
		PrintWriter writer = new PrintWriter(path + "\\Proizvodi.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<proizvodi.getPr().size();i++){
			zapisiUbazu(s, proizvodi.getPr().get(i).getSifra(), proizvodi.getPr().get(i).getNaziv(),
					proizvodi.getPr().get(i).getBoja(), proizvodi.getPr().get(i).getDimenzije(), proizvodi.getPr().get(i).getTezina(),
					proizvodi.getPr().get(i).getZemljaproizvodnje(), proizvodi.getPr().get(i).getProizvodjac(),
					proizvodi.getPr().get(i).getJedinicnacena(), proizvodi.getPr().get(i).getKategorija(), proizvodi.getPr().get(i).getSlika(),
					proizvodi.getPr().get(i).getVideo(), proizvodi.getPr().get(i).getOcena(), proizvodi.getPr().get(i).getRecenzije(),
					proizvodi.getPr().get(i).getMagacin(), proizvodi.getPr().get(i).getProdavnica());
		}
		
		return Response.seeOther(uriSucces).build();
	}
	
	
	@GET
	@Path("/trazim/{naziv}/{mincena}/{maxcena}/{kategorija}/{zemlja}/{boja}/{ocena}/{rec}/{kolicina}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Proizvod> vratiProizvodd(@PathParam("naziv") String naziv,
			@PathParam("mincena") String mincena,@PathParam("maxcena") String maxcena,@PathParam("kategorija") String kategorija,
			@PathParam("zemlja") String zemlja,@PathParam("boja") String boja,@PathParam("ocena") String ocena,
			@PathParam("rec") String recenzija,@PathParam("kolicina") String kolicina) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi p =listaProizvoda(s);
		Proizvodi zavracanje = new Proizvodi();
		if(naziv.isEmpty() || mincena.isEmpty() || maxcena.isEmpty() || kategorija.isEmpty() || zemlja.isEmpty() || boja.isEmpty() || ocena.isEmpty() ||recenzija.isEmpty() || kolicina.isEmpty()){
			return zavracanje.getPr();
		}
		
		boolean nemaga = true;
		String pravoime = new String();
		String veliki = naziv.toUpperCase();
		for(int i =0;i<1;i++)
			pravoime+=veliki.toCharArray()[i];
		
		for(int i=1;i<naziv.length();i++)
			pravoime+=naziv.toCharArray()[i];

		//IMEEE
		
		if(!pravoime.equals("Nema")){
			for(int i=0;i<p.getPr().size();i++){
				if(p.getPr().get(i).getNaziv().contains(pravoime)){
					if(zavracanje.getPr().size()==0){
						zavracanje.getPr().add(p.getPr().get(i));
					}else{
						for(int z=0;z<zavracanje.getPr().size();z++){
							if(zavracanje.getPr().get(z).getSifra().equals(p.getPr().get(i).getSifra()))
								nemaga = false;
						}
						if(nemaga)
						zavracanje.getPr().add(p.getPr().get(i));
					}
				}
			}
		}
		
		//MINCENAAA
		
		if(!mincena.equals("nema")){
			if(!pravoime.equals("Nema")){
			for(int i=0;i<zavracanje.getPr().size();i++){
				if(Integer.parseInt(zavracanje.getPr().get(i).getJedinicnacena()) < Integer.parseInt(mincena)){
					zavracanje.getPr().remove(i);
			}
		}
		}else{
			System.out.println("uso");
			for(int i=0;i<p.getPr().size();i++){
				if(Integer.parseInt(p.getPr().get(i).getJedinicnacena()) > Integer.parseInt(mincena)){
					System.out.println(Integer.parseInt(p.getPr().get(i).getJedinicnacena()));
						zavracanje.getPr().add(p.getPr().get(i));
					}
				}
			}
		}
		
		//MAXCENAA
		
		if(!maxcena.equals("nema")){
			if(!pravoime.equals("Nema") || !mincena.equals("nema")){
			for(int i=0;i<zavracanje.getPr().size();i++){
				if(Integer.parseInt(zavracanje.getPr().get(i).getJedinicnacena()) > Integer.parseInt(maxcena)){
					zavracanje.getPr().remove(i);
			}
		}
		}else{

			for(int i=0;i<p.getPr().size();i++){
				if(Integer.parseInt(p.getPr().get(i).getJedinicnacena()) < Integer.parseInt(maxcena)){

						zavracanje.getPr().add(p.getPr().get(i));
					}
				}
			}
		}
		
		//kategorija
		if(!kategorija.equals("nema")){
			if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema")){
			for(int i=0;i<zavracanje.getPr().size();i++){
				if(!zavracanje.getPr().get(i).getKategorija().contains(kategorija)){
					zavracanje.getPr().remove(i);
			}
		}
		}else{

			for(int i=0;i<p.getPr().size();i++){
				if(p.getPr().get(i).getKategorija().contains(kategorija)){

						zavracanje.getPr().add(p.getPr().get(i));
					}
				}
			}
		}
		
		//zemlja
				if(!zemlja.equals("nema")){
					if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema") || !kategorija.equals("nema")){
					for(int i=0;i<zavracanje.getPr().size();i++){
						if(!zavracanje.getPr().get(i).getZemljaproizvodnje().contains(zemlja)){
							zavracanje.getPr().remove(i);
					}
				}
				}else{

					for(int i=0;i<p.getPr().size();i++){
						if(p.getPr().get(i).getZemljaproizvodnje().contains(zemlja)){

								zavracanje.getPr().add(p.getPr().get(i));
							}
						}
					}
				}
		
				//boja
				if(!boja.equals("nema")){
					if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema") || !kategorija.equals("nema") || !zemlja.equals("nema")){
					for(int i=0;i<zavracanje.getPr().size();i++){
						if(!zavracanje.getPr().get(i).getBoja().contains(boja)){
							zavracanje.getPr().remove(i);
					}
				}
				}else{

					for(int i=0;i<p.getPr().size();i++){
						if(p.getPr().get(i).getBoja().contains(boja)){

								zavracanje.getPr().add(p.getPr().get(i));
							}
						}
					}
				}
				//ocena
				if(!ocena.equals("nema")){
					if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema") || !kategorija.equals("nema") || !zemlja.equals("nema") || !boja.equals("nema")){
					for(int i=0;i<zavracanje.getPr().size();i++){
						if(Integer.parseInt(p.getPr().get(i).getOcena()) != Integer.parseInt(ocena)){
							zavracanje.getPr().remove(i);
					}
				}
				}else{

					for(int i=0;i<p.getPr().size();i++){
						if(Integer.parseInt(p.getPr().get(i).getOcena()) == Integer.parseInt(ocena)){

								zavracanje.getPr().add(p.getPr().get(i));
							}
						}
					}
				}
				
				//broj rec
				if(!recenzija.equals("nema")){
					if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema") || !kategorija.equals("nema") || !zemlja.equals("nema") || !boja.equals("nema") || !ocena.equals("nema")){
					for(int i=0;i<zavracanje.getPr().size();i++){
						if(zavracanje.getPr().get(i).getRecenzije().size() != Integer.parseInt(recenzija)){
							zavracanje.getPr().remove(i);
					}
				}
				}else{

					for(int i=0;i<p.getPr().size();i++){
						if(p.getPr().get(i).getRecenzije().size() == Integer.parseInt(recenzija)){

								zavracanje.getPr().add(p.getPr().get(i));
							}
						}
					}
				}
				//kolicina
				if(!kolicina.equals("nema")){
					if(!pravoime.equals("Nema") || !mincena.equals("nema") || !maxcena.equals("nema") || !kategorija.equals("nema") || !zemlja.equals("nema") || !boja.equals("nema") || !ocena.equals("nema") || !recenzija.equals("nema")){
					for(int i=0;i<zavracanje.getPr().size();i++){
						if(Integer.parseInt(zavracanje.getPr().get(i).getMagacin()) != Integer.parseInt(kolicina)){
							zavracanje.getPr().remove(i);
					}
				}
				}else{

					for(int i=0;i<p.getPr().size();i++){
						if(Integer.parseInt(p.getPr().get(i).getMagacin()) == Integer.parseInt(kolicina)){

								zavracanje.getPr().add(p.getPr().get(i));
							}
						}
					}
				}
		
		return zavracanje.getPr();
	}
	
	@GET
	@Path("/vratijednog/{sifra}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Proizvod vratiProizvod(@PathParam("sifra") String sifra) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi p =listaProizvoda(s);
		Proizvod zaVracanje = new Proizvod();
		if(sifra.isEmpty()){
			return zaVracanje;
		}
	
		for(int i=0;i<p.getPr().size();i++){
			if(p.getPr().get(i).getSifra().equals(sifra))
				zaVracanje = p.getPr().get(i);
		}
		
		return zaVracanje;
	}
	
	@GET
	@Path("/vratisve") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Proizvod> vratiProizvod() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi p =listaProizvoda(s);
		
	
		
		return p.getPr();
	}
	
	@GET
	@Path("/{naziv}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ArrayList<Proizvod> vratiProizvode(@PathParam("naziv") String nazivKategorije) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi p =listaProizvoda(s);
		Proizvodi zaVracanje = new Proizvodi();
		
	
		for(int i=0;i<p.getPr().size();i++){
			if(!p.getPr().get(i).getKategorija().isEmpty()){
				if(p.getPr().get(i).getKategorija().equals(nazivKategorije)){
					zaVracanje.getPr().add(p.getPr().get(i));

				}
			}
		}
		//System.out.println(zaVracanje.get);
		//URI uriSucces = new URI("/WebShop/home.html");
		//return Response.seeOther(uriSucces).build();
		return zaVracanje.getPr();
	}
	
	@GET
	@Path("izmeni/{sifra}/{boja}/{dimenzije}/{tezina}/{cena}/{kategorija}/{ocena}/{proizv}/{zemlja}/{kolicina}/{prodavnica}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response izmeni(@PathParam("sifra") String sifra,@PathParam("boja") String boja,@PathParam("prodavnica") String prodavnica,@PathParam("dimenzije") String dimenzije,@PathParam("tezina") String tezina,@PathParam("cena") String cena,
			@PathParam("kategorija") String kategorija,@PathParam("ocena") String ocena,@PathParam("proizv") String proizv,@PathParam("zemlja") String zemlja,@PathParam("kolicina") String kolicina) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String r = config.getServletContext().getRealPath("");
		Proizvodi proizvodi =listaProizvoda(r);
		URI uriSucces = new URI("/WebShop/home.html");
		String path = FilePaths.getPath(r).getPath();
		boolean nemaje = true;
		
		
		if(sifra.isEmpty() || boja.isEmpty() || dimenzije.isEmpty() || tezina.isEmpty() || kategorija.isEmpty())
			return Response.seeOther(uriSucces).build();
		
		for(int s=0;s<proizvodi.getPr().size();s++){
			if(proizvodi.getPr().get(s).getSifra().equals(sifra)){
				proizvodi.getPr().get(s).setBoja(boja);
				proizvodi.getPr().get(s).setDimenzije(dimenzije);
				proizvodi.getPr().get(s).setTezina(tezina);
				proizvodi.getPr().get(s).setJedinicnacena(cena);
				proizvodi.getPr().get(s).setKategorija(kategorija);
				proizvodi.getPr().get(s).setOcena(ocena);
				proizvodi.getPr().get(s).setProizvodjac(proizv);
				proizvodi.getPr().get(s).setZemljaproizvodnje(zemlja);
				proizvodi.getPr().get(s).setMagacin(kolicina);
				
					if(!prodavnica.equals("nema")){
						for(int i=0;i<proizvodi.getPr().get(s).getProdavnica().size();i++){
							if(proizvodi.getPr().get(s).getProdavnica().get(i).equals(prodavnica)){
								nemaje = false;
								break;
							}
							}
						if(nemaje){
							proizvodi.getPr().get(s).getProdavnica().add(prodavnica);
						}
					}
					
				
				}
		}
			
			PrintWriter writer = new PrintWriter(path + "\\Proizvodi.txt");
			writer.print("");
			writer.close();
			
			for(int i=0;i<proizvodi.getPr().size();i++){
				zapisiUbazu(r, proizvodi.getPr().get(i).getSifra(), proizvodi.getPr().get(i).getNaziv(),
						proizvodi.getPr().get(i).getBoja(), proizvodi.getPr().get(i).getDimenzije(), proizvodi.getPr().get(i).getTezina(),
						proizvodi.getPr().get(i).getZemljaproizvodnje(), proizvodi.getPr().get(i).getProizvodjac(),
						proizvodi.getPr().get(i).getJedinicnacena(), proizvodi.getPr().get(i).getKategorija(), proizvodi.getPr().get(i).getSlika(),
						proizvodi.getPr().get(i).getVideo(), proizvodi.getPr().get(i).getOcena(), proizvodi.getPr().get(i).getRecenzije(),
						proizvodi.getPr().get(i).getMagacin(), proizvodi.getPr().get(i).getProdavnica());
			}
			
			return Response.seeOther(uriSucces).build();
	
	}
	
	@GET
	@Path("/delete/{prodavnica}/{sifra}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response obrisiprodavnicu(@PathParam("sifra") String sifra, @PathParam("prodavnica") String prodavnica) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi proizvodi =listaProizvoda(s);
		URI uriSucces = new URI("/WebShop/home.html");
		String path = FilePaths.getPath(s).getPath();
		if(sifra.isEmpty() || prodavnica.isEmpty()){
			return Response.seeOther(uriSucces).build();
		}
	
		for(int i=0;i<proizvodi.getPr().size();i++){
			if(proizvodi.getPr().get(i).getSifra().equals(sifra)){
				for(int j =0;j<proizvodi.getPr().get(i).getProdavnica().size();j++){
					if(proizvodi.getPr().get(i).getProdavnica().get(j).equals(prodavnica))
						proizvodi.getPr().get(i).getProdavnica().remove(j);
				}
			}
		}
		
		PrintWriter writer = new PrintWriter(path + "\\Proizvodi.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<proizvodi.getPr().size();i++){
			zapisiUbazu(s, proizvodi.getPr().get(i).getSifra(), proizvodi.getPr().get(i).getNaziv(),
					proizvodi.getPr().get(i).getBoja(), proizvodi.getPr().get(i).getDimenzije(), proizvodi.getPr().get(i).getTezina(),
					proizvodi.getPr().get(i).getZemljaproizvodnje(), proizvodi.getPr().get(i).getProizvodjac(),
					proizvodi.getPr().get(i).getJedinicnacena(), proizvodi.getPr().get(i).getKategorija(), proizvodi.getPr().get(i).getSlika(),
					proizvodi.getPr().get(i).getVideo(), proizvodi.getPr().get(i).getOcena(), proizvodi.getPr().get(i).getRecenzije(),
					proizvodi.getPr().get(i).getMagacin(), proizvodi.getPr().get(i).getProdavnica());
		}
		
		return Response.seeOther(uriSucces).build();
	}
	
	@GET
	@Path("/obrisi/{sifra}") //za izmenu, kada posnisti menjanje odredjene prodavnice
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response obrisiitem(@PathParam("sifra") String sifra) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String s = config.getServletContext().getRealPath("");
		Proizvodi proizvodi =listaProizvoda(s);
		String path = FilePaths.getPath(s).getPath();
		URI uriSucces = new URI("/WebShop/home.html");
	
		if(sifra.isEmpty())
			return Response.seeOther(uriSucces).build();
		
		for(int i=0;i<proizvodi.getPr().size();i++){
			if(proizvodi.getPr().get(i).getSifra().equals(sifra))
				proizvodi.getPr().remove(i);
		}
		
		PrintWriter writer = new PrintWriter(path + "\\Proizvodi.txt");
		writer.print("");
		writer.close();
		
		for(int i=0;i<proizvodi.getPr().size();i++){
			zapisiUbazu(s, proizvodi.getPr().get(i).getSifra(), proizvodi.getPr().get(i).getNaziv(),
					proizvodi.getPr().get(i).getBoja(), proizvodi.getPr().get(i).getDimenzije(), proizvodi.getPr().get(i).getTezina(),
					proizvodi.getPr().get(i).getZemljaproizvodnje(), proizvodi.getPr().get(i).getProizvodjac(),
					proizvodi.getPr().get(i).getJedinicnacena(), proizvodi.getPr().get(i).getKategorija(), proizvodi.getPr().get(i).getSlika(),
					proizvodi.getPr().get(i).getVideo(), proizvodi.getPr().get(i).getOcena(), proizvodi.getPr().get(i).getRecenzije(),
					proizvodi.getPr().get(i).getMagacin(), proizvodi.getPr().get(i).getProdavnica());
		}
		
		//System.out.println(zaVracanje.get);
		//URI uriSucces = new URI("/WebShop/home.html");
		//return Response.seeOther(uriSucces).build();
		return Response.seeOther(uriSucces).build();
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized void zapisiUbazu(String gde, String sifra, String naziv, 
			String boja, String dimenzije, String tezina, String zemlja, String proizvodjac,
			String jedinicnaCena, String kategorija, String slika, String video, String ocena, ArrayList<Recenzija> rec, 
			String kolicina, ArrayList<String> prod) {

		String path = FilePaths.getPath(gde).getPath();
		JSONObject obj = new JSONObject();
		obj.put("sifra", sifra);
		obj.put("naziv", naziv);
		obj.put("boja", boja);
		obj.put("dimenzije", dimenzije);
		obj.put("tezina", tezina);
		obj.put("zemlja", zemlja);
		obj.put("proizvodjac", proizvodjac);
		obj.put("jedinicnaCena", jedinicnaCena);
		obj.put("kategorija", kategorija);
		obj.put("slika", slika);
		obj.put("video", video);
		obj.put("ocena", ocena);
		obj.put("magacin", kolicina);
		if(rec == null){
			obj.put("recenzije", rec);
		}else{
			JSONArray recenzije = new JSONArray();
			
			for(int k = 0;k<rec.size();k++){
			  JSONObject pod = new JSONObject();
			  pod.put("sifra", rec.get(k).getSifra());
			  pod.put("korisnik", rec.get(k).getKorisnik());
			  pod.put("datum", rec.get(k).getDatum());
			  pod.put("ocena", rec.get(k).getOcena());
			pod.put("komentar", rec.get(k).getKomentar());
		
			recenzije.add(pod);
			}
			
		
			obj.put("recenzije", recenzije);
		}
		
		if(prod == null){
			obj.put("prodavnice", prod);
		}else{/*
			JSONArray prodavnice = new JSONArray();
			
			for(int k = 0;k<prod.size();k++){
			  JSONObject pod = new JSONObject();
			  pod.put("naziv", prod.get(k));
		
			  prodavnice.add(pod);
			}*/
			
		
			obj.put("prodavnice", prod);
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path + "\\Proizvodi.txt", true));
			writer.write(obj.toJSONString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			javax.ws.rs.core.Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	// Funkcija za citanje prodavnica
	public static synchronized Proizvodi listaProizvoda(String gde) throws JsonParseException, JsonMappingException, IOException {
		Proizvodi pr = new Proizvodi();
		String path = FilePaths.getPath(gde).getPath();

		String FileName = path + "\\Proizvodi.txt";
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

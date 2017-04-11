package beans;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class Prodavnica {
	private String sifra;
	private String naziv;
	private String adresa;
	private String drzava;
	private String telefon;
	private String email;
	private String ocena;
	private ArrayList<String> recenzije;
	private String odgovorni;
	
	public Prodavnica(){
		
	}
	
	public Prodavnica(String sifra, String naziv, String adresa, String drzava, String telefon, String email, String ocena, ArrayList<String> rec, String prodavac){
		this.sifra = sifra;
		this.naziv = naziv;
		this.adresa = adresa;
		this.drzava = drzava;
		this.telefon = telefon;
		this.email = email;
		this.ocena = ocena;
		this.recenzije = rec;
		this.odgovorni = prodavac;
	}
	@JsonProperty("sifra")
	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}
	@JsonProperty("naziv")
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	@JsonProperty("adresa")
	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	@JsonProperty("drzava")
	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}
	@JsonProperty("telefon")
	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@JsonProperty("ocena")
	public String getOcena() {
		return ocena;
	}

	public void setOcena(String ocena) {
		this.ocena = ocena;
	}
	@JsonProperty("recenzije")
	public ArrayList<String> getRecenzije() {
		return recenzije;
	}

	public void setRecenzije(ArrayList<String> recenzije) {
		this.recenzije = recenzije;
	}
	@JsonProperty("odgovorni")
	public String getOdgovorniProdavac() {
		return odgovorni;
	}

	public void setOdgovorniProdavac(String odgovorniProdavac) {
		this.odgovorni = odgovorniProdavac;
	}


}

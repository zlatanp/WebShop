package beans;

import java.util.ArrayList;

public class Proizvod {
	private String sifra;//unique
	private String naziv;
	private String boja;
	private String dimenzije;
	private String tezina;
	private String zemljaproizvodnje;
	private String proizvodjac;
	private String jedinicnacena;
	private String kategorija;
	private String slika;
	private String video;
	private String ocena;
	private ArrayList<Recenzija> recenzije;
	private String magacin;
	private ArrayList<String> prodavnica;
	
	public Proizvod(){
		prodavnica = new ArrayList<String>();
	}
	
	public Proizvod(String sifra, String naziv, String boja, String dimenz, String tezina, String zemlja, String proizv, String jedinicna,
			String kat, String slika, String video, String ocena, ArrayList<Recenzija> rec, String magac, ArrayList<String> prod)
	{
		this.sifra = sifra;
		this.naziv = naziv;
		this.boja = boja;
		this.dimenzije = dimenz;
		this.tezina = tezina;
		this.zemljaproizvodnje = zemlja;
		this.proizvodjac = proizv;
		this.kategorija = kat;
		this.slika = slika;
		this.video = video;
		this.ocena = ocena;
		this.recenzije = rec;
		this.magacin = magac;
		this.prodavnica = prod;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getBoja() {
		return boja;
	}

	public void setBoja(String boja) {
		this.boja = boja;
	}

	public String getDimenzije() {
		return dimenzije;
	}

	public void setDimenzije(String dimenzije) {
		this.dimenzije = dimenzije;
	}

	public String getTezina() {
		return tezina;
	}

	public void setTezina(String tezina) {
		this.tezina = tezina;
	}

	public String getZemljaproizvodnje() {
		return zemljaproizvodnje;
	}

	public void setZemljaproizvodnje(String zemljaproizvodnje) {
		this.zemljaproizvodnje = zemljaproizvodnje;
	}

	public String getProizvodjac() {
		return proizvodjac;
	}

	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	public String getJedinicnacena() {
		return jedinicnacena;
	}

	public void setJedinicnacena(String jedinicnacena) {
		this.jedinicnacena = jedinicnacena;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getOcena() {
		return ocena;
	}

	public void setOcena(String ocena) {
		this.ocena = ocena;
	}

	public ArrayList<Recenzija> getRecenzije() {
		return recenzije;
	}

	public void setRecenzije(ArrayList<Recenzija> recenzije) {
		this.recenzije = recenzije;
	}

	public String getMagacin() {
		return magacin;
	}

	public void setMagacin(String magacin) {
		this.magacin = magacin;
	}

	public ArrayList<String> getProdavnica() {
		return prodavnica;
	}

	public void setProdavnica(ArrayList<String> prodavnica) {
		this.prodavnica = prodavnica;
	}
	
	
}

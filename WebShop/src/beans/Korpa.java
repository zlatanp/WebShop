package beans;

import java.util.ArrayList;

public class Korpa {
	private String user;
	private ArrayList<String> proizvodi;
	private String prodavnica;
	private String dostavljac;
	private String cena;
	private String sifra;
	
	public Korpa(){
		proizvodi = new ArrayList<String>();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<String> getProizvodi() {
		return proizvodi;
	}

	public void setProizvodi(ArrayList<String> proizvodi) {
		this.proizvodi = proizvodi;
	}

	public String getProdavnica() {
		return prodavnica;
	}

	public void setProdavnica(String prodavnica) {
		this.prodavnica = prodavnica;
	}

	public String getDostavljac() {
		return dostavljac;
	}

	public void setDostavljac(String dostavljac) {
		this.dostavljac = dostavljac;
	}

	public String getCena() {
		return cena;
	}

	public void setCena(String cena) {
		this.cena = cena;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}
	
	
}

package beans;

import java.util.ArrayList;

public class Akcija {
	private String pocetak, kraj;
	private String kategorija;
	private String procenat;
	private ArrayList<String> sifreproizvoda;
	private String aktivno;
	private ArrayList<String> prodavnica;
	private String id;
	
	public Akcija(){
		sifreproizvoda = new ArrayList<String>();
		prodavnica = new ArrayList<String>();
	}

	public String getPocetak() {
		return pocetak;
	}

	public void setPocetak(String pocetak) {
		this.pocetak = pocetak;
	}

	public String getKraj() {
		return kraj;
	}

	public void setKraj(String kraj) {
		this.kraj = kraj;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public String getProcenat() {
		return procenat;
	}

	public void setProcenat(String procenat) {
		this.procenat = procenat;
	}

	public ArrayList<String> getSifreproizvoda() {
		return sifreproizvoda;
	}

	public void setSifreproizvoda(ArrayList<String> sifreproizvoda) {
		this.sifreproizvoda = sifreproizvoda;
	}

	public String getAktivno() {
		return aktivno;
	}

	public void setAktivno(String aktivno) {
		this.aktivno = aktivno;
	}

	public ArrayList<String> getProdavnica() {
		return prodavnica;
	}

	public void setProdavnica(ArrayList<String> prodavnica) {
		this.prodavnica = prodavnica;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}

package beans;

import java.util.ArrayList;

public class Kategorija {
	private String naziv;
	private String opis;
	private ArrayList<Kategorija> podkategorija;
	
	public Kategorija(){
	}
	
	public Kategorija(String naziv, String opis, ArrayList<Kategorija> podkategorija){
		this.naziv = naziv;
		this.opis = opis;
		this.podkategorija = podkategorija;
	}
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public ArrayList<Kategorija> getPodkategorija() {
		return podkategorija;
	}
	public void setPodkategorija(ArrayList<Kategorija> podkategorija) {
		this.podkategorija = podkategorija;
	}
	
	
}

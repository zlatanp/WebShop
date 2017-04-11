package beans;

import java.util.ArrayList;

public class Korisnici {
	private ArrayList<Korisnik> listakorisnika = new ArrayList<Korisnik>();

	public Korisnici(){
		
	}
	
	
	public ArrayList<Korisnik> getListakorisnika() {
		return listakorisnika;
	}

	public void setListakorisnika(ArrayList<Korisnik> listakorisnika) {
		this.listakorisnika = listakorisnika;
	}
	
	
}

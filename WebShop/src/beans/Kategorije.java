package beans;

import java.util.ArrayList;

public class Kategorije {
	private ArrayList<Kategorija> listaKategorija = new ArrayList<Kategorija>();

	public Kategorije(){
		
	}
	
	
	public ArrayList<Kategorija> getListaKategorija() {
		return listaKategorija;
	}

	public void setListaKategorija(ArrayList<Kategorija> listaKategorija) {
		this.listaKategorija = listaKategorija;
	}
}

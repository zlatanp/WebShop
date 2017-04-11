package beans;

import java.util.ArrayList;

public class Prodavnice {
	private ArrayList<Prodavnica> listaprodavnica = new ArrayList<Prodavnica>();

	public Prodavnice(){
		
	}
	
	
	public ArrayList<Prodavnica> getListaprodavnica() {
		return listaprodavnica;
	}

	public void setListaprodavnica(ArrayList<Prodavnica> listaprodavnica) {
		this.listaprodavnica = listaprodavnica;
	}
}

package beans;

import java.util.ArrayList;

public class Prodavci {
	ArrayList<Prodavac> listaProdavaca;

	public Prodavci(){
		listaProdavaca = new ArrayList<Prodavac>();
	}
	
	public Prodavci(ArrayList<Prodavac> prod){
		this.listaProdavaca = prod;
	}
	
	public ArrayList<Prodavac> getListaProdavaca() {
		return listaProdavaca;
	}

	public void setListaProdavaca(ArrayList<Prodavac> listaProdavaca) {
		this.listaProdavaca = listaProdavaca;
	}
	
	
}

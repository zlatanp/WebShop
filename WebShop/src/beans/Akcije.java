package beans;

import java.util.ArrayList;

public class Akcije {
	private ArrayList<Akcija> sveakcije;
	
	public Akcije(){
		sveakcije = new ArrayList<Akcija>();
	}

	public ArrayList<Akcija> getSveakcije() {
		return sveakcije;
	}

	public void setSveakcije(ArrayList<Akcija> sveakcije) {
		this.sveakcije = sveakcije;
	}
	
	
}

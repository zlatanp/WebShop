package beans;

import java.util.ArrayList;

public class Proizvodi {

	private ArrayList<Proizvod> pr;
	
	public Proizvodi(){
		pr = new ArrayList<Proizvod>();
	}
	
	public Proizvodi(ArrayList<Proizvod> proiz){
		this.pr = proiz;
	}

	public ArrayList<Proizvod> getPr() {
		return pr;
	}

	public void setPr(ArrayList<Proizvod> pr) {
		this.pr = pr;
	}
	
	
}
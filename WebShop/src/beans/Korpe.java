package beans;

import java.util.ArrayList;

public class Korpe {
	private ArrayList<Korpa> korpa;
	
	public Korpe(){
		korpa = new ArrayList<Korpa>();
	}

	public ArrayList<Korpa> getKorpa() {
		return korpa;
	}

	public void setKorpa(ArrayList<Korpa> korpa) {
		this.korpa = korpa;
	}
	
	
}

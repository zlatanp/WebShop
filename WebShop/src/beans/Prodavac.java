package beans;

import java.util.ArrayList;

public class Prodavac {
	private String username;
	private String pass;
	private ArrayList<String> prodavnice;
	
	public Prodavac(){
		prodavnice = new ArrayList<String>();
	}
	
	public Prodavac(String user, String pas, ArrayList<String> prod){
		this.username = user;
		this.pass = pas;
		this.prodavnice = prod;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public ArrayList<String> getProdavnice() {
		return prodavnice;
	}
	public void setProdavnice(ArrayList<String> prodavnice) {
		this.prodavnice = prodavnice;
	}
	
	
}

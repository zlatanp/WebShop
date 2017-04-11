package beans;

import java.util.ArrayList;

/**
 * @author Zlatan
 *
 */
public class Korisnik {

	private String username;
	private String password;
	private String ime;
	private String prezime;
	private int uloga;
	private String telefon;
	private String email;
	private String adresa;
	private String drzava;
	private ArrayList<String> zelim;
	private ArrayList<String> kupio;
	
	/**
	 * Enumeracija korisnika
	 * uloga: 0 kupac
	 * uloga: 1 prodavac
	 * uloga: 2 admin
	 *
	 */
	
	public Korisnik(){
		zelim = new ArrayList<String>();
		kupio = new ArrayList<String>();
	}
	
	public Korisnik(String username, String pass, String ime, String prez, int uloga, String telefon, String email, String adresa, String drzava, ArrayList<String> zelim, ArrayList<String> kupio){
		this.username = username;
		this.password = pass;
		this.ime = ime;
		this.prezime = prez;
		this.uloga = uloga;
		this.telefon = telefon;
		this.email = email;
		this.adresa = adresa;
		this.drzava = drzava;
		this.zelim = zelim;
		this.kupio = kupio;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public int getUloga() {
		return uloga;
	}
	public void setUloga(int uloga) {
		this.uloga = uloga;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getDrzava() {
		return drzava;
	}
	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	public ArrayList<String> getZelim() {
		return zelim;
	}

	public void setZelim(ArrayList<String> zelim) {
		this.zelim = zelim;
	}

	public ArrayList<String> getKupio() {
		return kupio;
	}

	public void setKupio(ArrayList<String> kupio) {
		this.kupio = kupio;
	}
	
	
}

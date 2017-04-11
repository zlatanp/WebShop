package beans;

public class Dostavljac {
	private String sifra;
	private String naziv;
	private String opis;
	private String drzave;
	private String tarife;
	
	public Dostavljac(){
		
	}
	
	public Dostavljac(String sifra, String naziv, String opis, String drzave){
		this.sifra = sifra;
		this.naziv = naziv;
		this.opis = opis;
		this.drzave = drzave;
	}
	
	public String getSifra() {
		return sifra;
	}
	public void setSifra(String sifra) {
		this.sifra = sifra;
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
	public String getDrzave() {
		return drzave;
	}
	public void setDrzave(String drzave) {
		this.drzave = drzave;
	}
	public String getTarife() {
		return tarife;
	}
	public void setTarife(String tarife) {
		this.tarife = tarife;
	}
	
}

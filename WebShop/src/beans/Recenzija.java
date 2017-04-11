package beans;

public class Recenzija {
	private String sifra;
	private String korisnik;
	private String datum;
	private String ocena;
	private String komentar;
	
	public Recenzija(){
		
	}
	
	public Recenzija(String sif, String kor, String dat, String ocen, String koment){
		this.sifra = sif;
		this.korisnik = kor;
		this.datum = dat;
		this.ocena = ocen;
		this.komentar = koment;
	}
	
	public String getSifra() {
		return sifra;
	}
	public void setSifra(String sifra) {
		this.sifra = sifra;
	}
	public String getKorisnik() {
		return korisnik;
	}
	public void setKorisnik(String korisnik) {
		this.korisnik = korisnik;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public String getOcena() {
		return ocena;
	}
	public void setOcena(String ocena) {
		this.ocena = ocena;
	}
	public String getKomentar() {
		return komentar;
	}
	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}
	
	
}

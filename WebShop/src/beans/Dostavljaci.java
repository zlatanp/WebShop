package beans;

import java.util.ArrayList;

public class Dostavljaci {
	private ArrayList<Dostavljac> listaDostavljaca = new ArrayList<Dostavljac>();
	
	public Dostavljaci(){
		
	}

	public ArrayList<Dostavljac> getListaDostavljaca() {
		return listaDostavljaca;
	}

	public void setListaDostavljaca(ArrayList<Dostavljac> listaDostavljaca) {
		this.listaDostavljaca = listaDostavljaca;
	}
}

package org.springframework.samples.hibernate.beans;

import java.util.ArrayList;
import java.util.List;

public class City {

	protected long id;
	protected String denominazione;
	protected String codice;
	protected String cap;
	
	//protected List<Villages> villages;
	
	public City() 
	{
		//this.frazioni=new ArrayList<Frazione>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	
	/*public List<Frazione> getFrazioni() {
		return frazioni;
	}
	public void setFrazioni(List<Frazione> frazioni) {
		this.frazioni = frazioni;
	}*/

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}
}

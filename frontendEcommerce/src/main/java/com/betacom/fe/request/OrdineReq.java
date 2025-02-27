package com.betacom.fe.request;

import java.util.List;


public class OrdineReq {

	private Integer id;
	private String indirizzoDiSpedizione;
	private String cap;
	private String citta;
	private String dataOrdine;
	private Integer carrelloId;
	private Integer utenteId;
	private Integer quantitaTotale;
	private Double prezzoTotale;
	
	
	
	
	public OrdineReq(Integer id, String indirizzoDiSpedizione, String cap, String citta, String dataOrdine,
			Integer carrelloId, Integer utenteId, Integer quantitaTotale, Double prezzoTotale) {
		super();
		this.id = id;
		this.indirizzoDiSpedizione = indirizzoDiSpedizione;
		this.cap = cap;
		this.citta = citta;
		this.dataOrdine = dataOrdine;
		this.carrelloId = carrelloId;
		this.utenteId = utenteId;
		this.quantitaTotale = quantitaTotale;
		this.prezzoTotale = prezzoTotale;
		
	}



	public OrdineReq(Integer id, String indirizzoDiSpedizione, String cap, String citta) {
		super();
		this.id = id;
		this.indirizzoDiSpedizione = indirizzoDiSpedizione;
		this.cap = cap;
		this.citta = citta;
	}



	public OrdineReq(Integer id, String indirizzoDiSpedizione, String cap, String citta, String dataOrdine,
			Integer carrelloId, Integer utenteId) {
		super();
		this.id = id;
		this.indirizzoDiSpedizione = indirizzoDiSpedizione;
		this.cap = cap;
		this.citta = citta;
		this.dataOrdine = dataOrdine;
		this.carrelloId = carrelloId;
		this.utenteId = utenteId;
	}

	

	public OrdineReq() {
		
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getIndirizzoDiSpedizione() {
		return indirizzoDiSpedizione;
	}

	public void setIndirizzoDiSpedizione(String indirizzoDiSpedizione) {
		this.indirizzoDiSpedizione = indirizzoDiSpedizione;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}



	public String getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(String dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public Integer getCarrelloId() {
		return carrelloId;
	}

	public void setCarrelloId(Integer carrelloId) {
		this.carrelloId = carrelloId;
	}

	public Integer getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(Integer utenteId) {
		this.utenteId = utenteId;
	}



	public Integer getQuantitaTotale() {
		return quantitaTotale;
	}



	public void setQuantitaTotale(Integer quantitaTotale) {
		this.quantitaTotale = quantitaTotale;
	}



	public Double getPrezzoTotale() {
		return prezzoTotale;
	}



	public void setPrezzoTotale(Double prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}



	

	

}
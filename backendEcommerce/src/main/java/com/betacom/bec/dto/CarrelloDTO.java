package com.betacom.bec.dto;

import java.sql.Timestamp;
//gjm,u


public class CarrelloDTO {

	private Integer id;
    private Timestamp dataCreazione;
    private Timestamp dataUltimoAggiornamento;
    private String stato;
    private Integer idProdotto;
    private Integer quantita;
    private Double prezzo;
    private UtenteDTO utente;
    
	public CarrelloDTO(Integer id, Timestamp dataCreazione, Timestamp dataUltimoAggiornamento,
			String stato, Integer idProdotto, Integer quantita, Double prezzo, UtenteDTO utente) {
		super();
		this.id = id;
		this.dataCreazione = dataCreazione;
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
		this.stato = stato;
		this.idProdotto = idProdotto;
		this.quantita = quantita;
		this.prezzo = prezzo;
		this.utente = utente;
	}

	public CarrelloDTO() {
		super();
	}
	
	

	public CarrelloDTO(Integer id, Timestamp dataCreazione) {
		super();
		this.id = id;
		this.dataCreazione = dataCreazione;
	}

	@Override
	public String toString() {
		return "CarrelloDTO [id=" + id  + ", dataCreazione=" + dataCreazione
				+ ", dataUltimoAggiornamento=" + dataUltimoAggiornamento + ", stato=" + stato + ", idProdotto="
				+ idProdotto + ", quantita=" + quantita + ", prezzo=" + prezzo + ", utente=" + utente + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(Timestamp dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Integer getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public UtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDTO utente) {
		this.utente = utente;
	}
    
	
    
    

}

package com.betacom.bec.dto;

import java.util.Date;

public class RecensioneDTO {

	private Integer id;
    private Integer valutazione;
    private String commento;
    private String dataRecensione;
    private UtenteDTO utente; 
    private ProdottoDTO prodotto;
	    




		public RecensioneDTO(Integer id, Integer valutazione, String commento, String dataRecensione, UtenteDTO utente,
			ProdottoDTO prodotto) {
		super();
		this.id = id;
		this.valutazione = valutazione;
		this.commento = commento;
		this.dataRecensione = dataRecensione;
		this.utente = utente;
		this.prodotto = prodotto;
	}



		public RecensioneDTO() {
			super();
		}



		@Override
		public String toString() {
			return "RecensioneDTO [id=" + id + ", valutazione=" + valutazione + ", commento=" + commento
					+ ", dataRecensione=" + dataRecensione + ", utente=" + utente + ", prodotto=" + prodotto + "]";
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getValutazione() {
			return valutazione;
		}

		public void setValutazione(Integer valutazione) {
			this.valutazione = valutazione;
		}

		public String getCommento() {
			return commento;
		}

		public void setCommento(String commento) {
			this.commento = commento;
		}



		public String getDataRecensione() {
			return dataRecensione;
		}

		public void setDataRecensione(String dataRecensione) {
			this.dataRecensione = dataRecensione;
		}



		public UtenteDTO getUtente() {
			return utente;
		}



		public void setUtente(UtenteDTO utente) {
			this.utente = utente;
		}



		public ProdottoDTO getProdotto() {
			return prodotto;
		}



		public void setProdotto(ProdottoDTO prodotto) {
			this.prodotto = prodotto;
		}




		

}
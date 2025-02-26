package com.betacom.bec.dto;

import java.util.Date;

public class PagamentoDTO {

    private Integer id;
    private String metodoDiPagamento;
    private String numeroCarta;
    private Date dataScadenza;
    private Integer idUtente;



	public PagamentoDTO(Integer id, String metodoDiPagamento, String numeroCarta, Date dataScadenza,Integer idUtente) {
        super();
        this.id = id;
        this.metodoDiPagamento = metodoDiPagamento;
        this.setNumeroCarta(numeroCarta);
        this.dataScadenza = dataScadenza;
        this.idUtente = idUtente;
    }

    public PagamentoDTO() {
        super();
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

    public String getMetodoDiPagamento() {
        return metodoDiPagamento;
    }

    public void setMetodoDiPagamento(String metodoDiPagamento) {
        this.metodoDiPagamento = metodoDiPagamento;
    }

    public String getNumeroCarta() {
        return "**** **** **** " + numeroCarta.substring(numeroCarta.length() - 4);
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }
}
package com.betacom.bec.services.interfaces;

import java.time.format.DateTimeParseException;
import java.util.List;

import com.betacom.bec.dto.PagamentoDTO;
import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.models.Pagamento;
import com.betacom.bec.request.PagamentoReq;

public interface PagamentoServices{
	
	void create (PagamentoReq req) throws Exception;
	
	boolean isNumeroCartaValido(String numeroCarta) throws Exception;
	
	boolean isDataScadenzaValida(String dataScadenza) throws DateTimeParseException;

	void removePagamento(PagamentoReq req) throws Exception;

	void update(PagamentoReq req) throws Exception;

	List<Pagamento> getAllPagamenti();

	List<PagamentoDTO> ottieniPagamenti(Integer idUtente);

	List<Pagamento> getPagamentiByUserId(Integer userId);

}
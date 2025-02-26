package com.betacom.bec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.betacom.bec.services.interfaces.PagamentoServices;
import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.dto.PagamentoDTO;
import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.models.Pagamento;
import com.betacom.bec.request.PagamentoReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;
import com.betacom.bec.response.ResponseObject;

@RestController
@RequestMapping("/rest/pagamento")
public class PagamentoController {

    @Autowired
    PagamentoServices pagamentoS;

    @Autowired
    org.slf4j.Logger log;

    // Metodo per creare un pagamento
    @PostMapping("/create")
    public ResponseBase create(@RequestBody PagamentoReq req) {
        log.debug("create: " + req);
        ResponseBase r = new ResponseBase();
        r.setRc(true);
        try {
            pagamentoS.create(req);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setRc(false);
        }
        return r;
    }

	@GetMapping("/listByUserId")
	public ResponseList<PagamentoDTO> listByUserId(@RequestParam Integer idUtente) {

	    ResponseList<PagamentoDTO> response = new ResponseList<PagamentoDTO>();
	    response.setRc(true);
	    try {
	    	response.setDati(pagamentoS.ottieniPagamenti(idUtente));

	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        response.setRc(false);
	    }
	    return response;
	}
	

    // Metodo per rimuovere un pagamento
    @PostMapping("/remove")
    public ResponseBase remove(@RequestBody PagamentoReq req) {
        log.debug("remove: " + req);
        ResponseBase r = new ResponseBase();
        r.setRc(true);
        try {
            pagamentoS.removePagamento(req);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setRc(false);
        }
        return r;
    }
    
    @PostMapping("/update")
    public ResponseBase update(@RequestBody PagamentoReq req) {
        log.debug("update: " + req);
        ResponseBase r = new ResponseBase();
        r.setRc(true);
        try {
            pagamentoS.update(req);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setRc(false);
        }
        return r;
    }
    
 // Metodo per ottenere tutti i pagamenti
    @GetMapping("/listAll")
    public ResponseList<Pagamento> listAll() {
        log.debug("Recupero della lista completa dei pagamenti");
        ResponseList<Pagamento> r = new ResponseList<>();
        try {
            r.setDati(pagamentoS.getAllPagamenti()); // Supponendo che esista questo metodo in PagamentoServices
            r.setRc(true);
        } catch (Exception e) {
            log.error("Errore nel recupero dei pagamenti: ", e);
            r.setMsg(e.getMessage());
            r.setRc(false);
        }
        return r;
    }
}
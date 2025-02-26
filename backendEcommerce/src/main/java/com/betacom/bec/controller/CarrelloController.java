package com.betacom.bec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;
import com.betacom.bec.response.ResponseObject;
import com.betacom.bec.services.interfaces.CarrelloServices;

@RestController
@RequestMapping("/rest/carrello")
public class CarrelloController {
	
	@Autowired
	CarrelloServices carrelloS;
	
	@Autowired
	org.slf4j.Logger log;
	

	@GetMapping("/lista")
	public ResponseList<CarrelloDTO> getAllCarrelli(@RequestParam Integer utenteId) {

	    ResponseList<CarrelloDTO> response = new ResponseList<CarrelloDTO>();
	    response.setRc(true);
	    try {
	    	response.setDati(carrelloS.ottieniCarrello(utenteId));

	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        response.setRc(false);
	    }
	    return response;
	}

	
	
	@PostMapping("/rimuovi")
	public ResponseBase rimuoviProdotto(int carrelloId, int prodottoId,int quantitaDaRimuovere) {

	    ResponseBase response = new ResponseBase();
	    response.setRc(true);
	    try {
	        carrelloS.rimuoviProdotto(carrelloId, prodottoId, quantitaDaRimuovere);
	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        response.setRc(false);
	    }
	    return response;
	}
	
	@PostMapping("/aggiungi")
    public ResponseObject<Carrello> aggiungiProdotto(@RequestParam int prodottoId, @RequestParam int carrelloId, @RequestParam int quantita) {

        ResponseObject<Carrello> response = new ResponseObject<>();
        response.setRc(true);
        try {
            Carrello carrello = carrelloS.aggiungiProdottoAlCarrello(carrelloId, prodottoId, quantita);
            
            response.setDati(carrello);

        } catch (Exception e) {
            response.setMsg(e.getMessage());
            response.setRc(false);
        }
        return response;
    }
	
	@PostMapping("/eliminaCarrello")
	public ResponseBase eliminaCarrello(@RequestParam Integer carrelloId) {

	    ResponseBase response = new ResponseBase();
	    response.setRc(true);
	    try {
	        carrelloS.eliminaCarrello(carrelloId);
	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        response.setRc(false);
	    }
	    return response;
	}




}
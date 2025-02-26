package com.betacom.fe.controller;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;


import com.betacom.fe.dto.CarrelloDTO;
import com.betacom.fe.dto.OrdineDTO;
import com.betacom.fe.request.OrdineReq;
import com.betacom.fe.request.PagamentoReq;
import com.betacom.fe.request.ProdottoReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseList;
import com.betacom.fe.response.ResponseObject;

@Controller
public class OrdineController {
	
	
	@Autowired
	RestTemplate rest;
	
	@Value("${jpa.backend}")
	String backend;
	
	@Autowired
	Logger log;
	
	@GetMapping("/listByUtente")
	public ModelAndView listByUtente(@RequestParam Integer idUtente) {
	    
	    ModelAndView mav = new ModelAndView("profilo");

	    URI uri = UriComponentsBuilder
	            .fromUriString(backend + "ordine/listByUtente")
	            .queryParam("id", idUtente)
	            .buildAndExpand().toUri();

	    log.debug("URI: " + uri);

	    ResponseEntity<ResponseObject> responseEntity = rest.getForEntity(uri, ResponseObject.class);
	    ResponseObject<?> responseObject = responseEntity.getBody();
	    
	    if (responseObject != null && responseObject.getDati() != null) {
	        mav.addObject("ordine", responseObject.getDati());
	    } else {
	        mav.addObject("ordine", null);
	    }

	    return mav;
	}
	
	
	
	@GetMapping("/ordineSuccesso")
	public ModelAndView ordineSuccesso() {
	    ModelAndView mav = new ModelAndView("ordineSuccesso");
	    mav.addObject("ordine", new OrdineReq());
	    return mav;
	}


	@PostMapping("/saveOrdine")
	public Object saveOrdine(@ModelAttribute OrdineReq req) {
	    log.debug("SaveOrdine " + req);

	    URI uriOrdine = UriComponentsBuilder
	            .fromUriString(backend + "ordine/create") 
	            .build()
	            .toUri();

	    ResponseBase rc = rest.postForEntity(uriOrdine, req, ResponseBase.class).getBody();

	    log.debug("rc " + rc.getRc());

	    if (!rc.getRc()) {
	        ModelAndView mav = new ModelAndView("inserisciPagamento");
	        mav.addObject("pagamento", new PagamentoReq());
	        mav.addObject("errorMessage", rc.getMsg());
	        return mav;
	    }

	    // SVUOTO IL CARRELLO SOLO SE L’ORDINE È STATO CREATO CON SUCCESSO
	    try {
	    	URI uriCarrello = UriComponentsBuilder
	    	        .fromUriString(backend + "carrello/eliminaCarrello")
	    	        .queryParam("carrelloId", req.getCarrelloId())  
	    	        .build()
	    	        .toUri();

	    	ResponseBase rCarrello = rest.postForEntity(uriCarrello, null, ResponseBase.class).getBody();
	        
	        if (!rCarrello.getRc()) {
	            log.error("Errore nello svuotamento del carrello: " + rCarrello.getMsg());
	        }
	    } catch (Exception e) {
	        log.error("Eccezione durante l'eliminazione del carrello", e);
	    }

	    return "redirect:/ordineSuccesso";
	}


}
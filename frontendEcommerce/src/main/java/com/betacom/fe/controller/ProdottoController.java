package com.betacom.fe.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.dto.ProdottoDTO;
import com.betacom.fe.response.ResponseList;
import com.betacom.fe.response.ResponseObject;

@Controller
public class ProdottoController {
	
	
	@Autowired
	RestTemplate rest;
	
	@Value("${jpa.backend}")
	String backend;
	
	@Autowired
	Logger log;
	
	
	
	@GetMapping("/listById")
	public ModelAndView dettaglioProdotto(@RequestParam Integer id) {
	    
	    ModelAndView mav = new ModelAndView("dettagliprodotto");

	    URI uri = UriComponentsBuilder
	            .fromUriString(backend + "prodotto/listById")
	            .queryParam("id", id)
	            .buildAndExpand().toUri();

	    log.debug("URI: " + uri);

	    ResponseEntity<ResponseObject> responseEntity = rest.getForEntity(uri, ResponseObject.class);
	    ResponseObject<?> responseObject = responseEntity.getBody();
	    
	    if (responseObject != null && responseObject.getDati() != null) {
	        mav.addObject("prodotto", responseObject.getDati());
	    } else {
	        mav.addObject("prodotto", null);
	    }

	    return mav;
	}

	
	

}
package com.betacom.fe.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.dto.UtenteDTO;
import com.betacom.fe.request.UtenteReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseObject;

@Controller
public class RegisterController {

    @Autowired
    private RestTemplate rest;

    @Value("${jpa.backend}")
    private String backend;

    @Autowired
    private Logger log;

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("registrazione");
        mav.addObject("utente", new UtenteReq()); // Assicura che l'oggetto esista nel modello
        return mav;
    }


    
	@PostMapping("/create")
	public Object saveUtente(@ModelAttribute("utente") UtenteReq req) {
	    log.debug("req: " + req);

	    // Creazione dell'URI per il backend
	    URI uri = UriComponentsBuilder
	            .fromUriString(backend + "utente/create")
	            .buildAndExpand().toUri();
	    log.debug("URI: " + uri);

	    // Chiamata al backend per creare l'utente
	    ResponseBase resp = rest.postForEntity(uri, req, ResponseBase.class).getBody();
	    log.debug("create rc:" + resp.getRc());

	    // Se la creazione non è andata a buon fine, mostra il messaggio di errore
	    if (!resp.getRc()) {
	        ModelAndView mav = new ModelAndView("registrazione");
	        mav.addObject("error", resp.getMsg());  // Puoi aggiungere il messaggio di errore
	        return mav;
	    }

	    // Se la creazione ha successo, fai il redirect alla pagina di login
	    return "redirect:/login";
	}

    
    
    
}

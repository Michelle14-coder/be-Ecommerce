package com.betacom.fe.controller;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.dto.CarrelloDTO;
import com.betacom.fe.dto.ProdottoDTO;
import com.betacom.fe.dto.UtenteDTO;
import com.betacom.fe.request.ProdottoReq;
import com.betacom.fe.request.RecensioneReq;
import com.betacom.fe.request.UtenteReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseList;
import com.betacom.fe.response.ResponseObject;

@Controller
public class RecensioneController {
	
    @Autowired
    RestTemplate rest;

    @Value("${jpa.backend}")
    String backend;

    @Autowired
    Logger log;
    
    @PostMapping("/saveRecensione")
    public Object saveRecensione(@ModelAttribute RecensioneReq req, @RequestParam String username, @RequestParam Integer prodottoId) {
        log.debug("createRecensione " + req);

        ModelAndView mav = new ModelAndView("dettagliprodotto");

        // Recupera l'utente
        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            mav.addObject("errorMessage", "Utente non trovato");
            return mav;
        }

        UtenteDTO utente = new UtenteDTO();
        utente.setId(userId);
        req.setUtente(utente);

        // Recupera il prodotto
        ProdottoDTO prodotto = new ProdottoDTO();
        prodotto.setId(prodottoId);
        req.setProdotto(prodotto);


        URI uri = UriComponentsBuilder.fromUriString(backend + "recensione/create").build().toUri();
        ResponseBase rc = rest.postForEntity(uri, req, ResponseBase.class).getBody();

        log.debug("rc " + rc.getRc());

        if (!rc.getRc()) {
            mav.addObject("req", new RecensioneReq());
            mav.addObject("utente", new UtenteReq());
            mav.addObject("errorMessage", rc.getMsg());
        }

        return new ModelAndView("redirect:/listById?id=" + prodottoId + "&errorMessage=" + rc.getMsg());


    }


   

    
    private Integer getUserIdByUsername(String username) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(backend + "utente/listByUsername")
                    .queryParam("userName", username)
                    .build().toUri();

            log.debug("Recupero ID per utente: " + username);

            HashMap<String, Object> userResponse = rest.getForObject(uri, HashMap.class);

            // Verifica se i dati sono validi e restituisci l'ID utente
            if (userResponse != null && userResponse.containsKey("dati")) {
                HashMap<String, Object> dati = (HashMap<String, Object>) userResponse.get("dati");
                return (Integer) dati.get("id");
            }

            return null;
        } catch (Exception e) {
            log.error("Errore nel recupero dell'ID utente", e);
            return null;
        }
    }





}

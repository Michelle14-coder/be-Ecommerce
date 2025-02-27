package com.betacom.fe.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.configuration.UtenteService;
import com.betacom.fe.dto.OrdineDTO;
import com.betacom.fe.dto.PagamentoDTO;
import com.betacom.fe.dto.UtenteDTO;
import com.betacom.fe.request.UtenteReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseObject;
import static com.betacom.fe.utilities.WebUtils.convertInObject;

@Controller
public class UtenteController {
    
    @Value("${jpa.backend}")
    String backend;
    
    @Autowired
    RestTemplate rest;
    
    @Autowired
    Logger log;
    
    @Autowired
    UtenteService utenteService;
    
    @GetMapping("/create") 
    public ModelAndView createUtente() {
        ModelAndView mav = new ModelAndView("registrazione");
        mav.addObject("req", new UtenteReq());
        mav.addObject("errorMsg", null);
        return mav;
    }
    
    @GetMapping("/profilo")
    public ModelAndView mostraProfilo(@RequestParam String userName) {
        ModelAndView mav = new ModelAndView("profilo");

        // Recupera l'utente tramite username
        URI uriUtente = UriComponentsBuilder.fromUriString(backend + "utente/listByUsername")
                .queryParam("userName", userName).build().toUri();

        ResponseObject<UtenteDTO> responseUtente = rest.exchange(
        	    uriUtente, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseObject<UtenteDTO>>() {}).getBody();

        if (responseUtente == null || responseUtente.getDati() == null) {
            mav.addObject("error", "Utente non trovato");
            return mav;
        }

        UtenteDTO utente = responseUtente.getDati();
        mav.addObject("utente", utente);

        // Recupera gli ordini dell'utente tramite l'ID
        URI uriOrdini = UriComponentsBuilder.fromUriString(backend + "ordine/listByUtente")
                .queryParam("id", utente.getId()).build().toUri();

        ResponseObject<List<OrdineDTO>> responseOrdini = rest.exchange(
        	    uriOrdini, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseObject<List<OrdineDTO>>>() {}).getBody();


        if (responseOrdini != null && responseOrdini.getDati() != null) {
            mav.addObject("ordini", responseOrdini.getDati());
        } else {
            mav.addObject("ordini", Collections.emptyList());  // Se non ci sono ordini
        }

        // Recupera i pagamenti dell'utente
        URI uriPagamenti = UriComponentsBuilder.fromUriString(backend + "pagamento/listByUser/" + utente.getId()).build().toUri();
        ResponseObject<List<PagamentoDTO>> responsePagamenti = rest.exchange(
        	    uriPagamenti, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseObject<List<PagamentoDTO>>>() {}).getBody();

        if (responsePagamenti != null && responsePagamenti.getDati() != null) {
            mav.addObject("pagamenti", responsePagamenti.getDati());
        } else {
            mav.addObject("pagamenti", Collections.emptyList());  // Se non ci sono pagamenti
        }

        return mav;
    }

    
    
    
    @PostMapping("/save")
    public Object saveUtente(@ModelAttribute UtenteReq req) {
        log.debug("saveUtente " + req);
        
        String operation = (req.getId() == null) ? "create" : "update";
        
        URI uri = UriComponentsBuilder
                .fromUriString(backend + "utente/" + operation)
                .buildAndExpand().toUri();
        
        log.debug("URI: " + uri);
        
        ResponseBase r = rest.postForEntity(uri, req, ResponseBase.class).getBody();
        log.debug("rc: " + r.getRc());
        
        if (!r.getRc()) {
            ModelAndView mav = new ModelAndView("registrazione");
            mav.addObject("req", req);
            mav.addObject("errorMsg", r.getMsg());
            return mav;            
        }
        utenteService.updateUtente(req);
        
        return "redirect:/login";
    }
    
    @PostMapping("/saveUtente")
    public Object save(@ModelAttribute UtenteReq req) {
        log.debug("saveUtente " + req);
        
        String operation = (req.getId() == null) ? "create" : "update";
        
        URI uri = UriComponentsBuilder
                .fromUriString(backend + "utente/" + operation)
                .buildAndExpand().toUri();
        
        log.debug("URI: " + uri);
        
        ResponseBase r = rest.postForEntity(uri, req, ResponseBase.class).getBody();
        log.debug("rc: " + r.getRc());
        
        if (!r.getRc()) {
            ModelAndView mav = new ModelAndView("profilo");
            mav.addObject("utente", req);
            mav.addObject("errorMsg", r.getMsg());
            return mav;            
        }
        utenteService.updateUtente(req);
        
        return "redirect:/profilo?userName=" + req.getUserName();
    }
    
    @GetMapping("/update") 
    public ModelAndView updateUtente(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("registrazione");
        
        URI uri = UriComponentsBuilder
                .fromUriString(backend + "utente/listById") 
                .queryParam("id", id)
                .buildAndExpand().toUri();
        
        log.debug("URI: " + uri);
        
        ResponseObject<?> utRest = rest.getForEntity(uri, ResponseObject.class).getBody();
        UtenteDTO ut = (UtenteDTO) convertInObject(utRest.getDati(), UtenteDTO.class);
        
        UtenteReq u = new UtenteReq(
                ut.getId(),
                ut.getNome(),
                ut.getCognome(),
                ut.getEmail(),
                ut.getUserName(),
                ut.getPassword(),
                ut.getRuolo(),
                ut.getNumeroTelefono(),
                ut.getIndirizzoDiSpedizione(),
                ut.getIndirizzoDiFatturazione()
        );

        mav.addObject("req", u);
        return mav;
    }
}

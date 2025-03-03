package com.betacom.fe.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.response.ResponseObject;

@Controller
public class ControllerHome {

	@Autowired
	RestTemplate rest;
	
	@Value("${jpa.backend}")
	String backend;
	
	@Autowired
	Logger log;
	
	@GetMapping("/home")
	public ModelAndView showHomepage() {
		
		ModelAndView home = new ModelAndView("homepage");
		
		
        URI uri = UriComponentsBuilder
                .fromUriString(backend + "recensione/listAll")
                .buildAndExpand().toUri();

        log.debug("URI: " + uri);

        ResponseEntity<ResponseObject> responseEntity = rest.getForEntity(uri, ResponseObject.class);
        ResponseObject<?> responseObject = responseEntity.getBody();

        if (responseObject != null && responseObject.getDati() != null) {
        	home.addObject("recensioni", responseObject.getDati());
        } else {
        	home.addObject("recensioni", null);
        }
        
        return home;
		
	}
	
}

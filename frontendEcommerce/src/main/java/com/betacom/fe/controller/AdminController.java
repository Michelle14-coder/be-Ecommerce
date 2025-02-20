package com.betacom.fe.controller;

import static com.betacom.fe.utilities.WebUtils.convertInObject;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;


import com.betacom.fe.configuration.UtenteService;
import com.betacom.fe.dto.UtenteDTO;
import com.betacom.fe.request.UtenteReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseList;
import com.betacom.fe.response.ResponseObject;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Value("${jpa.backend}")
	String backend;
	
	@Autowired
	RestTemplate rest;
	
	@Autowired
	Logger log;
	
	@Autowired
	UtenteService utenteService;
	
	@GetMapping("/listUtente")
	public ModelAndView listUtente() {
		ModelAndView mav = new ModelAndView("admin/listUtente");
		
		URI uri = UriComponentsBuilder
				.fromUriString(backend + "utente/list")
				.buildAndExpand().toUri();
		
		log.debug("URI: " + uri);
		
		ResponseList<?> lUt= rest.getForEntity(uri, ResponseList.class).getBody();
		
		mav.addObject("utente", lUt);
		
		return mav;
	}
	
	@GetMapping("/createUtente")
	public ModelAndView createUtente() {
		ModelAndView mav = new ModelAndView("admin/createUtente");
		mav.addObject("req", new UtenteReq());
		mav.addObject("errorMsg", null);
		
		return mav;
	}
	
	@PostMapping("/saveUtente")
	public Object saveUtente(@ModelAttribute UtenteReq req) {
		log.debug("saveUtente" + req);
		
		String operation = (req.getId() == null) ? "create" : "update";
		
		URI uri = UriComponentsBuilder
				.fromUriString(backend + "utente/" + operation)
				.buildAndExpand().toUri();
		
		log.debug("URI: " + uri);
		
		ResponseBase r = rest.postForEntity(uri, req, ResponseBase.class).getBody();
		log.debug("rc: " + r.getRc());
		
		if (!r.getRc()) {
			ModelAndView mav = new ModelAndView("createUtente");
			mav.addObject("req", req);
			mav.addObject("errorMsg", r.getMsg());
			return mav;			
		}
		utenteService.updateUtente(req);
		
		return "redirect:/admin/listUtente";
	}
	
	@GetMapping("/updateUtente")
	public ModelAndView updateUtente(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView("admin/createUtente");
		//log.debug("socioID: " + socioID);
		
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
				ut.getIndirizzoDiFatturazione());

		 
		//u.setErrorMsg(null);
		mav.addObject("req" , u);
		
		return mav;
	}
	
	@GetMapping("/deleteUtente")
	public Object deleteUtente(@RequestParam Integer id,@RequestParam String userName) {
	    ModelAndView mav = new ModelAndView("admin/listUtente");
	    
	    URI uri = UriComponentsBuilder
	            .fromUriString(backend + "utente/remove")
	            .buildAndExpand().toUri();
	    
	    log.debug("URI: " + uri);
	    
	    UtenteReq req = new UtenteReq();
	    req.setId(id);
	    req.setUserName(userName);
	    mav.addObject("req", req);
	    
	    ResponseBase r = rest.postForEntity(uri, req, ResponseBase.class).getBody();
	    log.debug("delete utente rc: " + r.getRc());
	    utenteService.deleteUtente(req);
	    
	    mav.addObject("ErrorMsg", null);
	    
		if (!r.getRc()) {
			return "redirect:/admin/listUtente?errorMsg=" + r.getMsg();
		}

	    return "redirect:/admin/listUtente";
	}

}

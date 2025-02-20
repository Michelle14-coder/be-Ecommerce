package com.betacom.bec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.bec.services.interfaces.UtenteServices;
import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.request.UtenteReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;

@RestController
@RequestMapping("/rest/utente")
public class UtenteController {
	
	@Autowired
	UtenteServices utenteS;
	
	@Autowired
	org.slf4j.Logger log;
	
	@GetMapping("/list")
	public ResponseList<UtenteDTO> list() {
		log.debug("List ");
		ResponseList<UtenteDTO> r = new ResponseList<UtenteDTO>();
		r.setRc(true);
		try {
			r.setDati (utenteS.list());
		} catch (Exception e) {
			log.debug(e.getMessage());
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
		
	}
	
	@GetMapping("/listSecurity")
	public List<UtenteDTO> listSecurity() {
		log.debug("listSecurity ");
		return utenteS.list();
		
	}

	
	@PostMapping("/create")
	public ResponseBase create(@RequestBody (required = true) UtenteReq req) {
		log.debug("create: " + req);
		ResponseBase r = new ResponseBase();
		r.setRc(true);
		try {
			utenteS.create(req);
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
	}
	
	@PostMapping("/update")
	public ResponseBase update(@RequestBody (required = true) UtenteReq req) {
		log.debug("update: " + req);
		ResponseBase r = new ResponseBase();
		r.setRc(true);
		try {
			utenteS.update(req);
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
	}
	
	@PostMapping("/remove")
	public ResponseBase remove(@RequestBody (required = true) UtenteReq req) {
		log.debug("remove: " + req);
		ResponseBase r = new ResponseBase();
		r.setRc(true);
		try {
			utenteS.remove(req);
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
	}
	

}
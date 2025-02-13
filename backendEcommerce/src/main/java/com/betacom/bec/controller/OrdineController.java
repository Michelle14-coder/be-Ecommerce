package com.betacom.bec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.bec.services.interfaces.OrdineServices;
import com.betacom.bec.dto.OrdineDTO;
import com.betacom.bec.request.OrdineReq;
import com.betacom.bec.response.ResponseBase;
import com.betacom.bec.response.ResponseList;

@RestController
@RequestMapping("/rest/ordine")
public class OrdineController {
	
	@Autowired
	OrdineServices ordineS;
	
	@Autowired
	org.slf4j.Logger log;
	
	@GetMapping("/listByUtente")
	public ResponseList<OrdineDTO> listByUtente(@RequestParam("id") Integer idUtente) {

		ResponseList<OrdineDTO> r = new ResponseList<OrdineDTO>();
		r.setRc(true);

		try {
			r.setDati(ordineS.listByUtente(idUtente));
		} catch (Exception e) {
			log.debug(e.getMessage());
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
		
	}
	

	
	@PostMapping("/create")
	public ResponseBase create(@RequestBody (required = true) OrdineReq req) {
		log.debug("create: " + req);
		ResponseBase r = new ResponseBase();
		r.setRc(true);
		try {
			ordineS.create(req);
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			r.setRc(false);
		}
		return r;
	}
	
	

}
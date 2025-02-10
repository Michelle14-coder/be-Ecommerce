package com.betacom.bec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.bec.services.interfaces.ProdottoServices;
import com.betacom.bec.services.interfaces.UtenteServices;
import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.request.ProdottoReq;
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
	
//	@GetMapping("/list")
//	public ResponseList<PrdoDTO> list(Integer id,String nome,String cognome,String attivita) {
//		log.debug("List " + id + "/" + nome + "/" + cognome + "/" + attivita);
//		ResponseList<SocioDTO> r = new ResponseList<SocioDTO>();
//		r.setRc(true);
//		List<SocioDTO> resp = null;
//		try {
//			r.setDati (socioS.list(id, nome, cognome, attivita));
//		} catch (Exception e) {
//			log.debug(e.getMessage());
//			r.setMsg(e.getMessage());
//			r.setRc(false);
//		}
//		return r;
//		
//	}
//	

	
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
		log.debug("delete: " + req);
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

package com.betacom.bec.services.interfaces;

import java.util.List;

import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.request.UtenteReq;



public interface UtenteServices {


	void create(UtenteReq req) throws Exception;

	List<UtenteDTO> list();

	void remove(UtenteReq req) throws Exception;

	void update(UtenteReq req) throws Exception;
}
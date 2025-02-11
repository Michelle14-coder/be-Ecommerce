package com.betacom.bec.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.models.Carrello;

public interface CarrelloServices {
	
    
    List<CarrelloDTO> ottieniCarrello(int utenteId);
    	
	Carrello aggiungiProdottoAlCarrello(Integer utenteId, Integer prodottoId, Integer quantita);

	Carrello rimuoviProdotto(Integer utenteId, Integer prodottoId, Integer quantitaDaRimuovere);


}
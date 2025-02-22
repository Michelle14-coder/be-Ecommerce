package com.betacom.bec.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.models.Carrello;

public interface CarrelloServices {
	
    
    List<CarrelloDTO> ottieniCarrello(Integer utenteId);
    	
	//Carrello aggiungiProdottoAlCarrello(Integer utenteId, Integer prodottoId, Integer quantita);

	//Carrello rimuoviProdotto(Integer utenteId, Integer prodottoId, Integer quantitaDaRimuovere);

	Carrello rimuoviProdotto(Integer utenteId, Integer carrelloId, Integer prodottoId, Integer quantitaDaRimuovere);

	Carrello aggiungiProdottoAlCarrello(Integer utenteId, Integer carrelloId, Integer prodottoId, Integer quantita);


}
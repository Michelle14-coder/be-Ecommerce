package com.betacom.bec.services.implementations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.dto.CarrelloProdottoDTO;
import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.CarrelloProdotto;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.repositories.CarrelloProdottoRepository;
import com.betacom.bec.repositories.CarrelloRepository;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.request.ProdottoReq;
import com.betacom.bec.services.interfaces.CarrelloServices;
import com.betacom.bec.services.interfaces.MessaggioServices;

import jakarta.transaction.Transactional;

import static com.betacom.bec.utils.Utilities.buildCarrelloProdottoDTO;

@Service
public class CarrelloImpl implements CarrelloServices{

	@Autowired
    private CarrelloRepository carrelloRepository;
	
	@Autowired
    private ProdottoRepository prodottoRepository;
	
    @Autowired
    CarrelloProdottoRepository cpR;
	
    @Autowired
    private MessaggioServices msgS;
	
    
    @Autowired
    private CarrelloProdottoRepository carrelloProdottoRepository;
	
	@Autowired
	Logger log;
	
	@Override
	public Carrello aggiungiProdottoAlCarrello(Integer userId, Integer prodottoId, Integer quantita) {
	    // Trova il carrello specifico per l'utente
	    List<Carrello> carrelloOptional = carrelloRepository.findByUtenteId(userId);
	    Optional<Prodotto> prodottoOptional = prodottoRepository.findById(prodottoId);

	    if (!prodottoOptional.isPresent()) {
	        throw new RuntimeException("Prodotto con ID " + prodottoId + " non trovato.");
	    }

	    Carrello carrello = carrelloOptional.get(0);
	    Prodotto prodotto = prodottoOptional.get();

	    if (carrello.getCarrelloProdotti() == null) {
	        carrello.setCarrelloProdotti(new ArrayList<>());
	    }

	    // Verifica se il prodotto è già presente nel carrello
	    Optional<CarrelloProdotto> optionalCarrelloProdotto = carrelloProdottoRepository.findByCarrelloAndProdotto(carrello, prodotto);
	    CarrelloProdotto carrelloProdotto = optionalCarrelloProdotto.orElseGet(() -> {
	        CarrelloProdotto nuovoCarrelloProdotto = new CarrelloProdotto();
	        nuovoCarrelloProdotto.setCarrello(carrello);
	        nuovoCarrelloProdotto.setProdotto(prodotto);
	        nuovoCarrelloProdotto.setQuantita(0);
	        carrello.getCarrelloProdotti().add(nuovoCarrelloProdotto);
	        return nuovoCarrelloProdotto;
	    });

	    carrelloProdotto.setQuantita(carrelloProdotto.getQuantita() + quantita);
	    carrelloProdottoRepository.save(carrelloProdotto);

	    // Aggiorna i totali del carrello
	    carrello.aggiornaTotali();
	    return carrelloRepository.save(carrello);
	}

    

	@Override
	public Carrello rimuoviProdotto(Integer carrelloId, Integer prodottoId, Integer quantitaDaRimuovere) {
	    // Trova il carrello specifico per l'utente
	    Optional<Carrello> carrelloOptional = carrelloRepository.findById(carrelloId);
	    Optional<Prodotto> prodottoOptional = prodottoRepository.findById(prodottoId);

	    if (!prodottoOptional.isPresent()) {
	        throw new RuntimeException("Prodotto con ID " + prodottoId + " non trovato.");
	    }

	    Carrello carrello = carrelloOptional.get();
	    Prodotto prodotto = prodottoOptional.get();

	    // Trova il prodotto nel carrello
	    Optional<CarrelloProdotto> carrelloProdottoOptional = carrelloProdottoRepository.findByCarrelloAndProdotto(carrello, prodotto);

	    if (!carrelloProdottoOptional.isPresent()) {
	        throw new RuntimeException("Prodotto non presente nel carrello.");
	    }

	    CarrelloProdotto carrelloProdotto = carrelloProdottoOptional.get();

	    // RIDUCE LA QUANTITÀ DEL PRODOTTO
	    int nuovaQuantita = carrelloProdotto.getQuantita() - quantitaDaRimuovere;

	    if (nuovaQuantita > 0) {
	        carrelloProdotto.setQuantita(nuovaQuantita);
	        carrelloProdottoRepository.save(carrelloProdotto);
	    } else {
	        // SE LA QUANTITÀ SCENDE A 0, RIMUOVE IL PRODOTTO DAL CARRELLO
	        carrello.getCarrelloProdotti().remove(carrelloProdotto); 
	        carrelloProdottoRepository.delete(carrelloProdotto);
	        carrelloProdottoRepository.flush();
	    }

	    // AGGIORNA AUTOMATICAMENTE I TOTALI DEL CARRELLO USANDO LE QUERY
	    Integer nuovaQuantitaTotale = carrelloRepository.getQuantitaTotale(carrello.getId());
	    Double nuovoPrezzoTotale = carrelloRepository.getPrezzoTotale(carrello.getId());

	    carrello.setQuantita(nuovaQuantitaTotale != null ? nuovaQuantitaTotale : 0);
	    carrello.setPrezzo(nuovoPrezzoTotale != null ? nuovoPrezzoTotale : 0.0);

	    return carrelloRepository.save(carrello);
	}




	@Override
	public List<CarrelloDTO> ottieniCarrello(Integer utenteId) {
	    // Filtra i carrelli in base all'utenteId
	    List<Carrello> lU = carrelloRepository.findByUtenteId(utenteId);  
	    return lU.stream()
	            .map(u -> new CarrelloDTO(u.getId(),
	                    u.getQuantita(),
	                    u.getPrezzo(),
	                    u.getUtente().getId(),
	                    buildCarrelloProdottoDTO(u.getCarrelloProdotti())))
	            .collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void eliminaCarrello(Integer carrelloId) throws Exception {
	    Optional<Carrello> carrelloOptional = carrelloRepository.findById(carrelloId);

	    if (carrelloOptional.isEmpty()) {
	        throw new Exception(msgS.getMessaggio("Carrello non trovato"));
	    }

	    // Svuota carrello
	    cpR.deleteByCarrelloId(carrelloId);

	    Carrello carrello = carrelloOptional.get();
	    carrello.setQuantita(0);
	    carrello.setPrezzo(0.0);
	    carrelloRepository.save(carrello);
	}



 
	
}
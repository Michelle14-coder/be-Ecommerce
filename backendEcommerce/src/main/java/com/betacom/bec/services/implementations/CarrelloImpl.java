package com.betacom.bec.services.implementations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.CarrelloProdotto;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.repositories.CarrelloProdottoRepository;
import com.betacom.bec.repositories.CarrelloRepository;
import com.betacom.bec.repositories.ProdottoRepository;
import com.betacom.bec.services.interfaces.CarrelloServices;

import static com.betacom.bec.utils.Utilities.buildCarrelloProdottoDTO;

@Service
public class CarrelloImpl implements CarrelloServices{

	@Autowired
    private CarrelloRepository carrelloRepository;
	
	@Autowired
    private ProdottoRepository prodottoRepository;
	
    
    @Autowired
    private CarrelloProdottoRepository carrelloProdottoRepository;
	
	@Autowired
	Logger log;
	
	@Override
	public Carrello aggiungiProdottoAlCarrello(Integer utenteId, Integer prodottoId, Integer quantita) {
	    Optional<Carrello> carrelloOptional = carrelloRepository.findByUtenteId(utenteId);
	    Optional<Prodotto> prodottoOptional = prodottoRepository.findById(prodottoId);

	    if (!carrelloOptional.isPresent()) {
	        throw new RuntimeException("Carrello non trovato per l'utente " + utenteId);
	    }
	    if (!prodottoOptional.isPresent()) {
	        throw new RuntimeException("Prodotto con ID " + prodottoId + " non trovato.");
	    }

	    Carrello carrello = carrelloOptional.get();
	    Prodotto prodotto = prodottoOptional.get();

	    if (carrello.getCarrelloProdotti() == null) {
	        carrello.setCarrelloProdotti(new ArrayList<>());
	    }

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

	    // AGGIORNAMENTO AUTOMATICO DEL CARRELLO
	    carrello.aggiornaTotali();
	    return carrelloRepository.save(carrello);
	}
    

	@Override
	public Carrello rimuoviProdotto(Integer utenteId, Integer prodottoId, Integer quantitaDaRimuovere) {
	    Optional<Carrello> carrelloOptional = carrelloRepository.findByUtenteId(utenteId);
	    Optional<Prodotto> prodottoOptional = prodottoRepository.findById(prodottoId);

	    if (!carrelloOptional.isPresent()) {
	        throw new RuntimeException("Carrello non trovato per l'utente " + utenteId);
	    }
	    if (!prodottoOptional.isPresent()) {
	        throw new RuntimeException("Prodotto con ID " + prodottoId + " non trovato.");
	    }

	    Carrello carrello = carrelloOptional.get();
	    Prodotto prodotto = prodottoOptional.get();

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
	        carrelloProdottoRepository.delete(carrelloProdotto);
	    }

	    // AGGIORNA AUTOMATICAMENTE I TOTALI DEL CARRELLO USANDO LE QUERY
	    Integer nuovaQuantitaTotale = carrelloRepository.getQuantitaTotale(carrello.getId());
	    Double nuovoPrezzoTotale = carrelloRepository.getPrezzoTotale(carrello.getId());

	    carrello.setQuantita(nuovaQuantitaTotale != null ? nuovaQuantitaTotale : 0);
	    carrello.setPrezzo(nuovoPrezzoTotale != null ? nuovoPrezzoTotale : 0.0);

	    return carrelloRepository.save(carrello);
	}


	@Override
	public List<CarrelloDTO> ottieniCarrello(int utenteId) {
	    // Recupero la lista di carrelli associati all'utente
	    List<Carrello> carrelli = carrelloRepository.getByUtenteId(utenteId);

	    if (carrelli.isEmpty()) {
	        System.out.println("Nessun carrello trovato per utenteId: " + utenteId);
	    } else {
	        carrelli.forEach(c -> {
	            System.out.println("Carrello trovato con id: " + c.getId());
	            System.out.println("Numero di prodotti nel carrello: " + c.getCarrelloProdotti().size());
	        });
	    }

	    // Trasformazione in una lista di DTO
	    return carrelli.stream()
	            .map(c -> new CarrelloDTO(
	                    c.getId(),
	                    c.getPrezzo(),
	                    c.getQuantita(),
	                    (c.getUtente() != null) ? new UtenteDTO(
	                            c.getUtente().getId(),
	                            c.getUtente().getNome(),
	                            c.getUtente().getCognome()) : null,
	                    buildCarrelloProdottoDTO(c.getCarrelloProdotti())
	            ))
	            .collect(Collectors.toList());
	}


    
	
}
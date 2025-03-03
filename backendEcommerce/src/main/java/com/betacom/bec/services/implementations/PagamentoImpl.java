package com.betacom.bec.services.implementations;

import static com.betacom.bec.utils.Utilities.buildCarrelloProdottoDTO;
import static com.betacom.bec.utils.Utilities.convertStringToDateCarta;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betacom.bec.dto.CarrelloDTO;
import com.betacom.bec.dto.PagamentoDTO;
import com.betacom.bec.dto.ProdottoDTO;
import com.betacom.bec.dto.RecensioneDTO;
import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.Pagamento;
import com.betacom.bec.models.Prodotto;
import com.betacom.bec.models.Utente;
import com.betacom.bec.repositories.PagamentoRepository;
import com.betacom.bec.repositories.UtenteRepository;
import com.betacom.bec.request.PagamentoReq;
import com.betacom.bec.services.interfaces.MessaggioServices;
import com.betacom.bec.services.interfaces.PagamentoServices;

@Service
public class PagamentoImpl implements PagamentoServices {
	
	@Autowired
	Logger log;
	
	@Autowired
	PagamentoRepository pagamentoR;
	
	@Autowired
	UtenteRepository utenteRepository;

	@Autowired
	private MessaggioServices msgS;
	
	// Metodo che crea il pagamento e lo associa all'utente
	
	@Override
	public void create(PagamentoReq req) throws Exception {


	    if (req.getMetodoDiPagamento() == null)
	        throw new Exception(msgS.getMessaggio("Metodo di pagamento obbligatorio!"));
	    if (req.getNumeroCarta() == null)
	        throw new Exception(msgS.getMessaggio("Numero carta obbligatorio!"));
	    if (!isNumeroCartaValido(req.getNumeroCarta()))
	        throw new Exception(msgS.getMessaggio("Numero carta non valido!"));
	    if (!isDataScadenzaValida(req.getDataScadenza()))
	        throw new Exception(msgS.getMessaggio("Data scadenza non valida!"));
	    if (req.getCvv() == null)
	        throw new Exception(msgS.getMessaggio("Cvv obbligatorio!"));
	    if (req.getUserId() == null)
	        throw new Exception(msgS.getMessaggio("Utente non trovato"));

	    // Recupera l'utente dal database
	    Optional<Utente> utenteOpt = utenteRepository.findById(req.getUserId());
	    if (utenteOpt.isEmpty()) {
	        throw new Exception(msgS.getMessaggio("Utente non trovato"));
	    }

	    Pagamento pg = new Pagamento();
	    pg.setMetodoDiPagamento(req.getMetodoDiPagamento());
	    pg.setNumeroCarta(req.getNumeroCarta());
	    pg.setDataScadenza(convertStringToDateCarta(req.getDataScadenza()));
	    pg.setCvv(req.getCvv());
	    pg.setIdUtente(req.getUserId());

	    pagamentoR.save(pg);
	}


	// Metodo che gestisce la data di scadenza, formato mm/yy
	
	public boolean isDataScadenzaValida(String dataScadenza) throws DateTimeParseException {
	    if (dataScadenza == null) return false;

	    // Verifica che la data rispetti il formato MM/YY
	    if (!dataScadenza.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
	        return false;
	    }

	    try {
	        // Converte la data in YearMonth per validare ulteriormente
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
	        YearMonth data = YearMonth.parse(dataScadenza, formatter);
	        
	        // La data di scadenza deve essere futura o il mese corrente
	        return !data.isBefore(YearMonth.now());
	    } catch (DateTimeParseException e) {
	        return false;
	    }
	}


	// Metodo che gestisce il numero della carta > minimo 13 a max 19 numeri

	public boolean isNumeroCartaValido(String numeroCarta) {
	    if (numeroCarta == null || !numeroCarta.matches("\\d{13,19}")) {
	        return false; 
	    }
	    return true;
	}
	
	@Override
	public List<Pagamento> getPagamentiByUserId(Integer userId) {
	    return pagamentoR.findByIdUtente(userId);
	}


	
	// Rimuovi pagamento

		@Override
		public void removePagamento(PagamentoReq req) throws Exception {
		    // Verifica che l'ID del pagamento sia fornito
		    if (req.getId() == null) {
		        throw new Exception(msgS.getMessaggio("Pagamento non trovato"));
		    }

		    // Recupera il pagamento dal database
		    Optional<Pagamento> pagamentoOpt = pagamentoR.findById(req.getId());
		    if (pagamentoOpt.isEmpty()) {
		        throw new Exception(msgS.getMessaggio("Pagamento non trovato"));
		    }

		    Pagamento pagamento = pagamentoOpt.get();

		    // Verifica che il pagamento sia associato all'utente
		    if (pagamento.getIdUtente() == null || !pagamento.getIdUtente().equals(req.getUserId())) {
		        throw new Exception(msgS.getMessaggio("Pagamento non associato ad utente"));
		    }

		    // Rimuovi il pagamento
		    pagamentoR.delete(pagamento);
		    log.debug("Pagamento con ID " + req.getId() + " rimosso per l'utente con ID " + req.getUserId());
		}
		
		@Override
		public void update(PagamentoReq req) throws Exception {
		    log.debug("Update Pagamento: " + req);

		    // Controlla se esiste un pagamento con lo stesso numero di carta per un altro utente
		    List<Pagamento> existingPagamenti = pagamentoR.findAll();
		    boolean numeroCartaExists = existingPagamenti.stream().anyMatch(p ->
		            p.getNumeroCarta().equals(req.getNumeroCarta()) &&
		            !p.getId().equals(req.getId()));

		    if (numeroCartaExists) {
		        throw new Exception(msgS.getMessaggio("Metodo di pagamento esistente"));
		    }

		    Optional<Pagamento> optPagamento = pagamentoR.findById(req.getId());
		    if (optPagamento.isEmpty()) {
		        throw new Exception(msgS.getMessaggio("Metodo di pagamento non trovato"));
		    }

		    Pagamento p = optPagamento.get();

		    if (req.getMetodoDiPagamento() != null) {
		        p.setMetodoDiPagamento(req.getMetodoDiPagamento());
		    }
		    if (req.getNumeroCarta() != null) {
		        p.setNumeroCarta(req.getNumeroCarta());
		    }
		    if (req.getCvv() != null) {
		        p.setCvv(req.getCvv());
		    }
		    if (req.getDataScadenza() != null) {
		        p.setDataScadenza(convertStringToDateCarta(req.getDataScadenza()));
		    }

		    pagamentoR.save(p);
		}


		@Override
		public List<Pagamento> getAllPagamenti() {
		    List<Pagamento> pagamenti = pagamentoR.findAll();
		    for (Pagamento pagamento : pagamenti) {
		        // Verifica che il campo utente non sia null
		        if (pagamento.getIdUtente() != null) {
		        	log.debug("Pagamento ID: " + pagamento.getId() + ", User ID: " + pagamento.getIdUtente());
		        } else {
		        	log.debug("Pagamento ID: " + pagamento.getId() + ", User ID: null");
		        }
		    }
		    return pagamenti;
		}


		@Override
		public List<PagamentoDTO> ottieniPagamenti(Integer idUtente) {
		    List<Pagamento> lU = pagamentoR.findByidUtente(idUtente);
		    
		    return lU.stream()
		            .map(u -> new PagamentoDTO(
		                    u.getId(), 
		                    u.getMetodoDiPagamento(), 
		                    u.getNumeroCarta(), 
		                    u.getDataScadenza(), 
		                    u.getIdUtente())) 
		            .collect(Collectors.toList());


}

}
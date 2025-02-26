package com.betacom.fe.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.betacom.fe.dto.CarrelloDTO;
import com.betacom.fe.dto.PagamentoDTO;
import com.betacom.fe.dto.UtenteDTO;
import com.betacom.fe.request.OrdineReq;
import com.betacom.fe.request.PagamentoReq;
import com.betacom.fe.response.ResponseBase;
import com.betacom.fe.response.ResponseList;
import com.betacom.fe.response.ResponseObject;

@Controller
public class PagamentoController {

    @Autowired
    RestTemplate rest;

    @Value("${jpa.backend}")
    String backend;

    @Autowired
    Logger log;
    


 // Lista di tutti i pagamenti
    @GetMapping("/listPagamenti")
    public ModelAndView listPagamenti() {
        ModelAndView mav = new ModelAndView("admin/listPagamenti");

        URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/listAll").build().toUri();
        ResponseEntity<ResponseList> responseEntity = rest.getForEntity(uri, ResponseList.class);
        ResponseList<PagamentoDTO> responseObject = responseEntity.getBody();

        if (responseObject != null && responseObject.getDati() != null) {
            mav.addObject("pagamenti", responseObject.getDati());
        } else {
            mav.addObject("pagamenti", null);
        }

        return mav;
    }
    
	@GetMapping("/listByUserId")
	public ModelAndView listByUserId(@RequestParam(required = false) String utenteId, Principal principal) {
	    ModelAndView pagamentoPage = new ModelAndView("inserisciPagamento"); 

	    try {
	        // Se l'ID non è passato come parametro, recuperalo dal nome utente
	        if (utenteId == null) {
	            utenteId = principal.getName();
	        }

	        // Recupera l'ID dell'utente usando il nome
	        Integer userId = getUserIdByUsername(utenteId);

	        if (userId == null) {
	        	pagamentoPage.addObject("error", "Impossibile recuperare l'ID dell'utente.");
	            return pagamentoPage;
	        }

	        URI uri = UriComponentsBuilder
	                .fromUriString(backend + "pagamento/listByUserId")
	                .queryParam("utenteId", userId)
	                .build().toUri();

	        log.debug("URI: " + uri);
	        

	        ResponseList<PagamentoDTO> responseList = rest.getForObject(uri, ResponseList.class);


	        if (responseList != null && responseList.getRc()) {
	        	pagamentoPage.addObject("listPagamenti", responseList.getDati());
	        } else {
	        	pagamentoPage.addObject("error", "Errore nel recupero dei pagamenti.");
	        }
	    } catch (Exception e) {
	        log.error("Errore nel recupero del carrello", e);
	        pagamentoPage.addObject("error", "Si è verificato un errore durante il recupero dei pagamenti.");
	    }

	    return pagamentoPage; 
	}



	private Integer getUserIdByUsername(String username) {
	    try {
	        URI uri = UriComponentsBuilder
	                .fromUriString(backend + "utente/carrelloByUsername")
	                .queryParam("userName", username)
	                .build().toUri();

	        log.debug("Recupero ID per utente: " + username);

	        HashMap<String, Object> userResponse = rest.getForObject(uri, HashMap.class);

	        // Verifica se i dati sono validi e restituisci l'ID utente
	        if (userResponse != null && userResponse.containsKey("dati")) {
	            HashMap<String, Object> dati = (HashMap<String, Object>) userResponse.get("dati");
	            HashMap<String, Object> utente = (HashMap<String, Object>) dati.get("utente");
	            return (Integer) utente.get("id");  // Ottieni l'ID utente
	        }
	        
	        return null;
	    } catch (Exception e) {
	        log.error("Errore nel recupero dell'ID utente", e);
	        return null;
	    }
	}
    
    
    @GetMapping("/createPagamentoDettagli")
	public ModelAndView createPagamento(@RequestParam Integer carrelloId, @RequestParam Double prezzo, @RequestParam Integer utenteId) {
	    ModelAndView mav = new ModelAndView("inserisciPagamento");
	    mav.addObject("pagamento", new PagamentoReq());
	    mav.addObject("ordine", new OrdineReq());    
	    mav.addObject("carrelloId", carrelloId);  
	    mav.addObject("prezzo", prezzo);  
	    mav.addObject("utenteId", utenteId);  
	    mav.addObject("errorMsg", null);
	    return mav;
	}
    
    
    @PostMapping("/savePagamentoDettagli")
    public ModelAndView savePagamentoDettagli(@ModelAttribute PagamentoReq req, @RequestParam Double prezzo, @RequestParam Integer carrelloId) {
        try {
            log.debug("savePagamentoDettagli: " + req);

            URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/create").build().toUri();
            ResponseBase response = rest.postForEntity(uri, req, ResponseBase.class).getBody();

            ModelAndView mav = new ModelAndView("inserisciPagamento");
            mav.addObject("ordine", new OrdineReq());
            mav.addObject("carrelloId", carrelloId);  
            mav.addObject("prezzo", prezzo); 
            mav.addObject("pagamento", new PagamentoReq()); 

            // Aggiungi messaggi solo se necessario
            if (!response.getRc()) {
                mav.addObject("errorMessage", response.getMsg());
            } else {
                mav.addObject("successMessage", "Il pagamento è stato salvato con successo!");
            }

            return mav;
        } catch (Exception e) {
            log.error("Errore durante il salvataggio dei dettagli del pagamento", e);
            ModelAndView mav = new ModelAndView("errorPage");
            mav.addObject("errorMessage", "Si è verificato un errore nel processare la richiesta.");
            return mav;
        }
    }

    // Salvataggio di un nuovo pagamento
    @PostMapping("/savePagamento")
    public Object savePagamento(@ModelAttribute PagamentoReq req) {
        log.debug("savePagamento: " + req);

        URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/create").build().toUri();
        ResponseBase response = rest.postForEntity(uri, req, ResponseBase.class).getBody();

        if (!response.getRc()) {
            ModelAndView mav = new ModelAndView("createPagamento");
            mav.addObject("req", new PagamentoReq());
            mav.addObject("errorMessage", response.getMsg());
            return mav;
        }

        return "redirect:/admin/listPagamenti";
    }

    // Form per aggiornare un pagamento
    @GetMapping("/updatePagamento")
    public ModelAndView updatePagamento(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("updatePagamento");

        // Verifica che l'id non sia nullo
        if (id == null) {
            mav.addObject("errorMsg", "ID del pagamento mancante.");
            return mav;
        }

        // Costruisce l'URI per recuperare il pagamento specifico
        URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/listByUser/{id}")
                                      .buildAndExpand(id).toUri();

        // Esegui la chiamata al backend per ottenere i dati
        ResponseObject<List<PagamentoDTO>> response = rest.getForEntity(uri, ResponseObject.class).getBody();

        PagamentoDTO pagamento = new PagamentoDTO();  // Oggetto di pagamento da caricare

        // Se la risposta contiene dati, mappa i dati nella DTO
        if (response != null && response.getDati() != null && !response.getDati().isEmpty()) {
            pagamento = response.getDati().get(0); // Supponiamo che l'ID corrisponda al primo pagamento
        }

        // Passa i dati al modello
        mav.addObject("pagamento", pagamento);
        return mav;
    }

    @PostMapping("/saveUpdatePagamento")
    public Object saveUpdatePagamento(@ModelAttribute PagamentoDTO pagamento) {
        log.debug("saveUpdatePagamento: " + pagamento);

        // Verifica che l'ID del pagamento sia presente
        if (pagamento.getId() == null) {
            ModelAndView mav = new ModelAndView("updatePagamento");
            mav.addObject("errorMsg", "Errore: ID del pagamento mancante.");
            mav.addObject("pagamento", pagamento);
            return mav;
        }

        // Costruisci l'URI per l'aggiornamento del pagamento
        URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/update").build().toUri();

        // Esegui la chiamata al backend per aggiornare il pagamento
        ResponseBase response = rest.postForEntity(uri, pagamento, ResponseBase.class).getBody();

        // Se la risposta segnala un errore, mostra un messaggio di errore
        if (!response.getRc()) {
            ModelAndView mav = new ModelAndView("updatePagamento");
            mav.addObject("pagamento", pagamento);
            mav.addObject("errorMsg", response.getMsg());
            return mav;
        }

        // Redirect alla lista dei pagamenti
        return "redirect:/admin/listPagamenti";
    }

    // Rimozione di un pagamento
    @PostMapping("/removePagamento")
    public String removePagamento(@RequestParam Integer id) {
        log.debug("removePagamento: " + id);

        URI uri = UriComponentsBuilder.fromUriString(backend + "pagamento/remove").build().toUri();
        PagamentoReq req = new PagamentoReq();
        req.setId(id);

        ResponseBase response = rest.postForEntity(uri, req, ResponseBase.class).getBody();

        if (!response.getRc()) {
            log.error("Errore nella rimozione del pagamento: " + response.getMsg());
        }

        return "redirect:/admin/listPagamenti";
    }
}
package com.betacom.bec.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.bec.models.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Integer> {

	//List<Ordine> findByUtenteId(Integer idUtente);
	
	List<Ordine> findByCarrelloId(Integer idCarrello);

}

package com.betacom.bec.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.bec.models.Recensione;

public interface RecensioneRepository extends JpaRepository<Recensione, Integer> {

	List<Recensione> findByProdottoId(Integer idProdotto);


}

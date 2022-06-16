package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Piatto;

public interface BuffetRepository extends CrudRepository<Buffet,Long>{

	public boolean existsByNomeAndChefAndDescrizione(String nome, Chef chef, String descrizione);

	public List<Buffet> findByPiatti(Piatto piatto);

	public List<Buffet> findByChef(Chef chef);
	
}

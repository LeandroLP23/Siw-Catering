package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;

@Service
public class IngredienteService implements IServices<Ingrediente> {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	@Transactional
	public void save(Ingrediente ingrediente) {
		this.ingredienteRepository.save(ingrediente);
	}
	
	@Override
	public boolean alreadyExists(Ingrediente ingrediente) {
		return this.ingredienteRepository.existsByNomeAndOrigineAndDescrizione(
				ingrediente.getNome(), ingrediente.getOrigine(), ingrediente.getDescrizione());
	}
	
	@Override
	public Ingrediente findById(Long id) {
		return this.ingredienteRepository.findById(id).get();
	}

	@Override
	public List<Ingrediente> findAll() {
		List<Ingrediente> ingredienti = new ArrayList<Ingrediente>();
		for (Ingrediente i : ingredienteRepository.findAll()) {
			ingredienti.add(i);
		}
		return ingredienti;
	}
	
	@Transactional
	public void deleteById(Long id) {
		this.ingredienteRepository.deleteById(id);
	}
}

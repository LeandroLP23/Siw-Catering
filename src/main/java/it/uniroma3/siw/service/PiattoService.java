package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.repository.PiattoRepository;

@Service
public class PiattoService {
	
	@Autowired
	private PiattoRepository piattoRepository;
	
	@Transactional
	public void save (Piatto piatto) {
		this.piattoRepository.save(piatto);
	}

	public Piatto findById(Long id) {
		return this.piattoRepository.findById(id).get();
	}

	public List<Piatto> findAll() {
		List<Piatto> piatti = new ArrayList<Piatto>();
		for (Piatto p : piattoRepository.findAll()) {
			piatti.add(p);
		}
		return piatti;
	}

	public boolean alreadyExists(Piatto piatto) {
		return this.piattoRepository.existsByNomeAndDescrizione(
				piatto.getNome(),piatto.getDescrizione());
	}
	
	@Transactional
	public void deleteById(Long id) {
		this.piattoRepository.deleteById(id);
	}

	public List<Piatto> findByIngrediente(Ingrediente ingrediente) {
		return this.piattoRepository.findByIngredienti(ingrediente);
	}

}

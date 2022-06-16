package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;

@Service
public class ChefService implements IServices<Chef> {
	@Autowired
	private ChefRepository chefRepository;
	
	@Transactional
	public void save (Chef chef) {
		this.chefRepository.save(chef);
	}

	@Override
	public Chef findById(Long id) {
		return this.chefRepository.findById(id).get();
	}

	@Override
	public List<Chef> findAll() {
		List<Chef> chefs= new ArrayList<Chef>();
		for (Chef c : chefRepository.findAll()) {
			chefs.add(c);
		}
		return chefs;
	}

	@Override
	public boolean alreadyExists(Chef chef) {
		return this.chefRepository.existsByNomeAndCognomeAndNazionalita(
				chef.getNome(), chef.getCognome(),chef.getNazionalita());

	}
	
	@Transactional
	public void deleteById(Long id) {
		this.chefRepository.deleteById(id);
	}
}

package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Piatto;
import it.uniroma3.siw.repository.BuffetRepository;

@Service
public class BuffetService {

	@Autowired
	private BuffetRepository buffetRepository;
	
	@Transactional
	public void save (Buffet buffet) {
		this.buffetRepository.save(buffet);
	}

	public Buffet findById(Long id) {
		return this.buffetRepository.findById(id).get();
	}

	public List<Buffet> findAll() {
		List<Buffet> buffets = new ArrayList<Buffet>();
		for (Buffet i : buffetRepository.findAll()) {
			buffets.add(i);
		}
		return buffets;
	}

	public boolean alreadyExists(Buffet buffet) {
		return this.buffetRepository.existsByNomeAndChefAndDescrizione(
				buffet.getNome(), buffet.getChef(), buffet.getDescrizione());
	}
	
	public List<Buffet> findRandomFour(){
		List<Buffet> buffets = new ArrayList<Buffet>();
		List<Buffet> result = new ArrayList<Buffet>();
		buffets = (List<Buffet>) this.buffetRepository.findAll();
		Collections.shuffle(buffets);
		int c = 0;
		for (Buffet i : buffets) {
			result.add(i);
			c++;
			if (c==4) break;
		}
		return result;
	}
	
	@Transactional
	public void deleteById(Long id) {
		this.buffetRepository.deleteById(id);
	}

	public List<Buffet> findByPiatti(Piatto piatto) {
		return this.buffetRepository.findByPiatti(piatto);
	}

	public List<Buffet> findByChef(Chef chef) {
		return this.buffetRepository.findByChef(chef);
	}
}

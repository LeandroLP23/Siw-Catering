package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;

@Controller
public class SiteController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetService buffetService;
	
	@GetMapping(path = {"", "/index","/"})
	public String getIndex(Model model) {

		List<Buffet>buffets = this.buffetService.findRandomFour();
		model.addAttribute("buffets",buffets);
		
		List<Chef> chefs = this.chefService.findAll();
		model.addAttribute("chefs",chefs);

		return "index";
	}
}

package it.uniroma3.siw.controller;

import java.util.List;


import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;

@Controller
public class ChefController {
	
	@Autowired
	private ChefValidator chefValidator;

	@Autowired
	private ChefService chefService;

	@Autowired
	private BuffetService buffetService;
	
	@GetMapping("/admin/addChef")
	public String getAddChef(Model model) {

		model.addAttribute("chef", new Chef());

		return "admin/addChef";
	}
	
	@PostMapping("/show/allBuffetChefPage")
	public String addChef (@ModelAttribute("chef") Chef chef,
			BindingResult bindingResult, Model model) {
		
		this.chefValidator.validate(chef, bindingResult);
		
		if (!bindingResult.hasErrors()){
			
			this.chefService.save(chef);
			
			model.addAttribute("chef", chefService.findById(chef.getId()));
			
			return "redirect:/";
		} else
			return "admin/addChef";
	}
	
	@GetMapping("/admin/editChef/{id}")
	public String getEditChef(@PathVariable("id") Long id, Model model) {

		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);

		return "admin/editChef";
	}
	
	@GetMapping("/admin/deleteChef/{id}")
	public String deleteChef(@PathVariable("id") Long id, Model model) {
		this.chefService.deleteById(id);
		return "redirect:/";
	}

	@PostMapping("/admin/updateChef/{id}")
	public String updateIngrediente(@ModelAttribute("chef") Chef chef,
									@PathVariable("id") Long id,
									BindingResult bindingResult, Model model) {

		this.chefValidator.validate(chef, bindingResult);

		if (!bindingResult.hasErrors()) {

			//Save
			chef.setId(id);
			this.chefService.save(chef);

			model.addAttribute("chef", chefService.findById(chef.getId()));
			return "redirect:/show/allChefPage";
		} else
			return "admin/editChef";
	}

	@GetMapping("/show/allBuffetChefPage/{id}")
	public String getAllChefBuffet(@PathVariable("id") Long id, Model model) {
		
		Chef chef = this.chefService.findById(id);
		List<Buffet>buffetsChef= this.buffetService.findByChef(chef);
		
		model.addAttribute("chef", chef );
		model.addAttribute("buffetschef",buffetsChef);
		
		return "allItemsPage";
	}

	@GetMapping("/show/allChefPage")
	public String getAllChef(Model model) {
		
		model.addAttribute("chefs", this.chefService.findAll() );
		
		return "allItemsPage";
	}
}

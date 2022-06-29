package it.uniroma3.siw.controller;


import java.util.List;


import it.uniroma3.siw.controller.validator.IngredienteValidator;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.PiattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Piatto;

@Controller
public class IngredienteController {
	
	@Autowired
	private IngredienteValidator ingredienteValidator;

	@Autowired
	private IngredienteService ingredienteService;
	
	@Autowired
	private PiattoService piattoService;
	
	@GetMapping("/admin/addIngrediente")
	public String getAddIngrediente(Model model) {

		model.addAttribute("ingrediente", new Ingrediente());

		return "admin/addIngrediente";
	}
	
	@PostMapping("/admin/ingredientePage")
	public String addIngrediente ( @ModelAttribute("ingrediente") Ingrediente ingrediente,
			BindingResult bindingResult, Model model) {
		
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		
		if (!bindingResult.hasErrors()){
			
			this.ingredienteService.save(ingrediente); 
			
			model.addAttribute("ingrediente", ingredienteService.findById(ingrediente.getId()));
			
			return "ingredientePage";
		} else
			return "admin/addIngrediente";
	}
	
	@GetMapping("/show/ingredientePage/{id}")
	public String getIngrediente(@PathVariable("id")Long id, Model model) {

		Ingrediente ingrediente = this.ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);

		return "ingredientePage";
	}
	
	@GetMapping("/admin/editIngrediente/{id}")
	public String getEditIngrediente(@PathVariable("id")Long id, Model model) {

		Ingrediente ingrediente = this.ingredienteService.findById(id);

		model.addAttribute("ingrediente", ingrediente);
		return "admin/editIngrediente";
	}
	
	@GetMapping("/admin/deleteIngrediente/{id}")
	public String deleteIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		List<Piatto> piatti = this.piattoService.findByIngrediente(ingrediente);
		for(Piatto piatto: piatti)
		{
			piatto.getIngredienti().remove(ingrediente);
		}
		this.ingredienteService.deleteById(id);
		return "redirect:/";
	}
	
	@PostMapping("/admin/updateIngrediente/{id}")
	public String updateIngrediente(@ModelAttribute("ingrediente") Ingrediente ingrediente,
			@PathVariable("id") Long id, 
			BindingResult bindingResult, Model model) {

		this.ingredienteValidator.validate(ingrediente, bindingResult);

		if (!bindingResult.hasErrors()) {

			//Save
			ingrediente.setId(id);
			this.ingredienteService.save(ingrediente);

			model.addAttribute("ingrediente", ingredienteService.findById(ingrediente.getId()));

			return "ingredientePage";
		} else
			return "admin/editIngrediente";
	}
	
	@GetMapping("/show/allIngredientiPage")
	public String getAllPiatti(Model model) {
		model.addAttribute("ingredienti",this.ingredienteService.findAll());
		
		return "allItemsPage";
	}
	
}

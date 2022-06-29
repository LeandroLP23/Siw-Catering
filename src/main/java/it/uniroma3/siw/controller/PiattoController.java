package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.siw.service.BuffetService;
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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.PiattoValidator;
import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Piatto;

@Controller
public class PiattoController {

	@Autowired
	private PiattoValidator piattoValidator;

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private BuffetService buffetService;

	@GetMapping("/admin/addPiatto")
	public String getAddPiatto(Model model) {

		model.addAttribute("piatto",new Piatto());
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		model.addAttribute("ingredientiSelected",new ArrayList<Ingrediente>());

		return "admin/addPiatto";
	}

	@PostMapping("/admin/piattoPage")
	public String addPiatto (@ModelAttribute("piatto") Piatto piatto,
							 @RequestParam(value = "idingredienti",required = false) List<Long> idIngredienti,
							 BindingResult bindingResult, Model model) {

		this.piattoValidator.validate(piatto, bindingResult);

		if(idIngredienti==null)
			bindingResult.reject("piatto.ingredienti");

		if (!bindingResult.hasErrors()){

			setIngredientiPiatto(piatto, idIngredienti);

			this.piattoService.save(piatto);

			model.addAttribute("piatto", this.piattoService.findById(piatto.getId()));

			return "piattoPage";
		}else {

			//Riempe nuovamente i parametri della Form
			model.addAttribute("piatto",piatto);
			model.addAttribute("ingredienti", this.ingredienteService.findAll());

			if(idIngredienti==null){
				model.addAttribute("ingredientiSelected", new ArrayList<Ingrediente>());
			}else{
				model.addAttribute("ingredientiSelected", getIngredientiSelezionati(idIngredienti));
			}

			return "admin/addPiatto";
		}
	}

	@GetMapping("/show/piattoPage/{id}")
	public String getPiatto(@PathVariable("id")Long id, Model model) {

		Piatto piatto = this.piattoService.findById(id);
		model.addAttribute("piatto", piatto);

		return "piattoPage";
	}

	@GetMapping("/admin/editPiatto/{id}")
	public String getEditPiatto(@PathVariable("id")Long id, Model model) {

		Piatto piatto = this.piattoService.findById(id);

		model.addAttribute("piatto", piatto);
		model.addAttribute("ingredientiSelected", piatto.getIngredienti());
		model.addAttribute("ingredienti", ingredienteService.findAll());

		return "admin/editPiatto";
	}


	@PostMapping("/admin/updatePiatto/{id}")
	public String updatePiatto(@ModelAttribute("piatto") Piatto piatto,
							   @PathVariable("id") Long id,
							   @RequestParam(value = "idingredienti",required = false) List<Long> idIngredienti,
							   BindingResult bindingResult, Model model) {

		if(idIngredienti==null)
			bindingResult.reject("piatto.ingredienti");

		this.piattoValidator.validate(piatto, bindingResult);

		if (!bindingResult.hasErrors()) {

			setIngredientiPiatto(piatto, idIngredienti);

			//Save
			piatto.setId(id);
			this.piattoService.save(piatto);

			model.addAttribute("piatto", this.piattoService.findById(piatto.getId()));

			return "piattoPage";
		} else{

			model.addAttribute("piatto",piatto);
			model.addAttribute("ingredienti", this.ingredienteService.findAll());

			if(idIngredienti==null){
				model.addAttribute("ingredientiSelected", new ArrayList<Ingrediente>());
			}else{
				model.addAttribute("ingredientiSelected", getIngredientiSelezionati(idIngredienti));
			}

			return "admin/editPiatto";
		}
	}

	@GetMapping("/admin/deletePiatto/{id}")
	public String deletePiatto(@PathVariable("id") Long id, Model model) {

		Piatto piatto = this.piattoService.findById(id);
		List<Buffet> buffets = this.buffetService.findByPiatti(piatto);

		for(Buffet buffet: buffets) {
			buffet.getPiatti().remove(piatto);
		}

		this.piattoService.deleteById(id);
		return "redirect:/";
	}

	@GetMapping("/show/allPiattiPage")
	public String getAllPiatti(Model model) {
		model.addAttribute("piatti",this.piattoService.findAll());

		return "allItemsPage";
	}

	private void setIngredientiPiatto(Piatto piatto, List<Long> idIngredienti) {
		List<Ingrediente> ingredienti = getIngredientiSelezionati(idIngredienti);
		piatto.setIngredienti(ingredienti);
	}

	private List<Ingrediente> getIngredientiSelezionati(List<Long> idIngredienti) {
		List<Ingrediente> ingredienti = new ArrayList<>();
		for (Long idIngrediente : idIngredienti) {
			ingredienti.add(this.ingredienteService.findById(idIngrediente));
		}
		return ingredienti;
	}
}

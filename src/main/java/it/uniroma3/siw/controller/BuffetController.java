package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;


import it.uniroma3.siw.service.BuffetService;
import it.uniroma3.siw.service.ChefService;
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

import it.uniroma3.siw.controller.validator.BuffetValidator;
import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Piatto;

@Controller
public class BuffetController {

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private BuffetValidator buffetValidator;

	@Autowired
	private PiattoService piattoService;

	@Autowired
	private ChefService chefService;

	@GetMapping("/admin/addBuffet")
	public String getAddBuffet(Model model) {

		model.addAttribute("chefs", this.chefService.findAll());
		model.addAttribute("piatti", this.piattoService.findAll());
		model.addAttribute("piattiSelected",new ArrayList<Piatto>());
		model.addAttribute("buffet", new Buffet());

		return "admin/addBuffet";
	}

	@PostMapping("/admin/buffetPage")
	public String addBuffet(@ModelAttribute("buffet") Buffet buffet,
							@RequestParam(value = "idpiatti",required = false) List<Long> idPiatti,
							@RequestParam(value = "idchef", required = false) Long idChef,
							BindingResult bindingResult, Model model) {

		checkPiattiChef(idPiatti, idChef, bindingResult);

		this.buffetValidator.validate(buffet, bindingResult);

		if (!bindingResult.hasErrors()) {// se i dati sono corretti

			this.setPiattiBuffet(idPiatti, buffet);

			this.setChefBuffet(idChef, buffet);

			this.buffetService.save(buffet);

			model.addAttribute("buffet", this.buffetService.findById(buffet.getId()));
			model.addAttribute("chef", (buffet.getChef()));

			return "buffetPage";
		} else{

			ripopolaBuffetForm(buffet, idPiatti, idChef, model);

			return "admin/addBuffet";
		}
	}

	@GetMapping("/show/buffetPage/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = buffetService.findById(id);

		model.addAttribute("buffet", buffet);
		model.addAttribute("chef", buffet.getChef());

		return "buffetPage";
	}

	@GetMapping("/admin/editBuffet/{id}")
	public String getEditBuffet(@PathVariable("id") Long id, Model model) {

		Buffet buffet = buffetService.findById(id);

		model.addAttribute("buffet", buffet);
		model.addAttribute("chefs", this.chefService.findAll());
		model.addAttribute("chefSelected", buffet.getChef());
		model.addAttribute("piatti", this.piattoService.findAll());
		model.addAttribute("piattiSelected", buffet.getPiatti());

		return "admin/editBuffet";
	}

	@GetMapping("/admin/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {

		Buffet buffet = this.buffetService.findById(id);
		Chef chef = buffet.getChef();

		chef.getBuffets().remove(buffet);
		buffet.setChef(null);
		this.chefService.save(chef);

		this.buffetService.deleteById(id);
		return "redirect:/";
	}

	@PostMapping("/admin/updateBuffet/{id}")
	public String updateBuffet(@ModelAttribute("buffet") Buffet buffet, @PathVariable("id") Long id,
							   @RequestParam(value = "idchef",required = false) Long idChef,
							   @RequestParam(value = "idpiatti",required = false) List<Long> idPiatti,
							   BindingResult bindingResult, Model model) {

		checkPiattiChef(idPiatti, idChef, bindingResult);

		this.buffetValidator.validate(buffet, bindingResult);

		if (!bindingResult.hasErrors()) {

			this.setPiattiBuffet(idPiatti, buffet);

			this.setChefBuffet(idChef, buffet);

			this.buffetService.save(buffet);

			model.addAttribute("buffet", buffetService.findById(buffet.getId()));

			model.addAttribute("chef", (buffet.getChef()));

			return "buffetPage";
		} else {

			ripopolaBuffetForm(buffet,idPiatti,idChef,model);

			return "admin/editBuffet";
		}
	}

	@GetMapping("/show/allBuffetPage")
	public String getAllBuffet(Model model) {

		model.addAttribute("buffets", this.buffetService.findAll());

		return "allItemsPage";
	}

	private void checkPiattiChef(List<Long> idPiatti, Long idChef, BindingResult bindingResult) {
		if (idPiatti == null)
			bindingResult.reject("buffet.piatti");

		if(idChef == 0)
			bindingResult.reject("buffet.chef");
	}

	/* Riempe nuovamente i parametri della Form */
	private void ripopolaBuffetForm(Buffet buffet, List<Long> idPiatti, Long idChef, Model model) {
		model.addAttribute("buffet", buffet);

		model.addAttribute("chefs", this.chefService.findAll());
		if(idChef != 0){
			model.addAttribute("chefSelected",this.chefService.findById(idChef));
		}

		model.addAttribute("piatti", this.piattoService.findAll());
		if(idPiatti != null) {
			model.addAttribute("piattiSelected", getPiattiSelezionati(idPiatti));
		}else{
			model.addAttribute("piattiSelected", new ArrayList<Piatto>());
		}
	}

	private void setPiattiBuffet(List<Long> idPiatti, Buffet buffet) {
		List<Piatto> piatti = getPiattiSelezionati(idPiatti);
		buffet.setPiatti(piatti);
	}

	private List<Piatto> getPiattiSelezionati(List<Long> idPiatti) {
		List<Piatto> piatti = new ArrayList<>();
		for (Long idIngrediente : idPiatti) {
			piatti.add(this.piattoService.findById(idIngrediente));
		}
		return piatti;
	}

	private void setChefBuffet(Long idChef, Buffet buffet) {
		buffet.setChef(this.chefService.findById(idChef));
	}
}

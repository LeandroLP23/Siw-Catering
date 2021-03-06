package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator {

	@Autowired
	private IngredienteService ingredienteService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Ingrediente.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "origine", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "required");

		if(this.ingredienteService.alreadyExists((Ingrediente)o)) {
			errors.reject("ingrediente.duplicato");
		}
	}
	
}

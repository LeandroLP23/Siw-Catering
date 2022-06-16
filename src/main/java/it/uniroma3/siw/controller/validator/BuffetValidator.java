package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Buffet;
import it.uniroma3.siw.service.BuffetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BuffetValidator implements Validator{

	@Autowired
	private BuffetService buffetService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Buffet.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "required");
		
		if(this.buffetService.alreadyExists((Buffet)o)) {
			errors.reject("buffet.duplicato");
		}
	}
	
}

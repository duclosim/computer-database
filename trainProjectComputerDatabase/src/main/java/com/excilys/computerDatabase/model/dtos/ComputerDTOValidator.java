package com.excilys.computerDatabase.model.dtos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.computerDatabase.utils.UserInputsValidator;

@Component
public class ComputerDTOValidator implements Validator {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTOValidator.class);
	
	@Autowired
	private UserInputsValidator userInputsValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		LOG.info(new StringBuilder("supports(" + clazz + ")").toString());
        return ComputerDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LOG.info(new StringBuilder("validate(" + target + ", "
				+ errors + ")").toString());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.computerDTO.name");
		ComputerDTO computer = (ComputerDTO) target;
		String intDate = computer.getIntroducedDate();
		if (intDate != null && !userInputsValidator.isValidDate(intDate)) {
			errors.rejectValue("introducedDate", "DateTimeFormat.computerDTO.introducedDate");
		}
		String disDate = computer.getDiscontinuedDate();
		if (disDate != null && !userInputsValidator.isValidDate(disDate)) {
			errors.rejectValue("discontinuedDate", "DateTimeFormat.computerDTO.discontinuedDate");
		}
	}

}

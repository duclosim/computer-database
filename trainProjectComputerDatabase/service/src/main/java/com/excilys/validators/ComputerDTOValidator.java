package com.excilys.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.binding.dtos.ComputerDTO;

@Component
public class ComputerDTOValidator implements Validator {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTOValidator.class);
	private static final String DATE_ERROR_MESSAGE_CODE = "DateTimeFormat.computerDTO";
	
	@Autowired
	private DateValidator dateValidator;

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
		if (!dateValidator.isValidDate(intDate)) {
			errors.rejectValue("introducedDate", DATE_ERROR_MESSAGE_CODE);
		}
		String disDate = computer.getDiscontinuedDate();
		if (!dateValidator.isValidDate(disDate)) {
			errors.rejectValue("discontinuedDate", DATE_ERROR_MESSAGE_CODE);
		}
	}

}

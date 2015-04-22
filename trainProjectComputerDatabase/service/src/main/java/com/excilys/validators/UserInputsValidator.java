package com.excilys.validators;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInputsValidator {
	private static final Logger LOG = LoggerFactory.getLogger(UserInputsValidator.class);
	private static final String NUMBER_REGEX = "[0-9]*";

	public static boolean isValidString(String string) {
		LOG.info("isValidString(" + string + ")");
		return ((string != null) && (!string.trim().isEmpty()));
	}

	/**
	 * Teste si un nombre est valide ou pas.
	 * @param number Le nombre à valider.
	 * @return <code>true</code> si le nombre est valide, <code>false</code> sinon.
	 */
	public static boolean isValidNumber(String number) {
		LOG.info("isValidNumber(" + number + ")");
		return isValidString(number) && Pattern.matches(NUMBER_REGEX, number);
	}
}

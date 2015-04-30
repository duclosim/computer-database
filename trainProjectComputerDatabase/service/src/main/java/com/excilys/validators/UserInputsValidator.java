package com.excilys.validators;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains methods to check the user inputs, 
 *   mainly for creating or editing Computer objects.
 * @author excilys
 *
 */
public class UserInputsValidator {
	private static final Logger LOG = LoggerFactory.getLogger(UserInputsValidator.class);
	private static final String NUMBER_REGEX = "[0-9]*";

	/**
	 * Checks if the String object is neither null or empty.
	 * @param string The String object to be checked.
	 * @return true if string is neither null or empty.
	 */
	public static boolean isValidString(String string) {
		LOG.trace("isValidString(" + string + ")");
		return ((string != null) && (!string.trim().isEmpty()));
	}

	/**
	 * Tests whether a string contains a valid positive number.
	 * @param number The String object to check.
	 * @return <code>true</code> if it is a real number, <code>false</code> otherwise.
	 */
	public static boolean isValidNumber(String number) {
		LOG.trace("isValidNumber(" + number + ")");
		return isValidString(number) && Pattern.matches(NUMBER_REGEX, number);
	}
}

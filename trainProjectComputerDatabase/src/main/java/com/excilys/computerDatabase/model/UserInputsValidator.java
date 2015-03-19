package com.excilys.computerDatabase.model;

import java.util.regex.Pattern;

public class UserInputsValidator {
	private static final String NUMBER_REGEX = "[1-9][0-9]*";
	
	/**
	 * Teste si une date est valide ou pas.
	 * @param date La date à valider.
	 * @return <code>true</code> si la date est valide, <code>false</code> sinon.
	 */
	public static boolean isValidDate(String date) {
		// TODO écrire le test
		return false;
	}

	/**
	 * Teste si un nombre est valide ou pas.
	 * @param date Le nombre à valider.
	 * @return <code>true</code> si le nombre est valide, <code>false</code> sinon.
	 */
	public static boolean isValidNumber(String number) {
		return Pattern.matches(NUMBER_REGEX, number);
	}
}

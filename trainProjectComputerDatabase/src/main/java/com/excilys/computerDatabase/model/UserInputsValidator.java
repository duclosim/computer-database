package com.excilys.computerDatabase.model;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class UserInputsValidator {
	private static final String NUMBER_REGEX = "[1-9][0-9]*";
	private static final String DATE_REGEX = "[1-2][0-9]{3}-[0-1][1-9]-[0-3][0-9]";
	
	/**
	 * Teste si une date est valide ou pas AAAA-MM-JJ.
	 * @param date La date à valider.
	 * @return <code>true</code> si la date est valide, <code>false</code> sinon.
	 */
	public static boolean isValidDate(String date) {
		boolean result = isValidString(date);
		result &= Pattern.matches(DATE_REGEX, date);
		int day = 0;
		int month = 0;
		int year = 0;
		StringTokenizer st = new StringTokenizer(date, "-");
		if (st.hasMoreTokens()) {
			year = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			month = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			day = Integer.parseInt(st.nextToken());
		}
		if ((day == 0) || (month == 0) || (year == 0)) {
			return false;
		}
		
		// TODO
		return result;
	}
	
	private static boolean isBissextileYear(int y) {
		return (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0));
	}

	/**
	 * Teste si un nombre est valide ou pas.
	 * @param date Le nombre à valider.
	 * @return <code>true</code> si le nombre est valide, <code>false</code> sinon.
	 */
	public static boolean isValidNumber(String number) {
		return isValidString(number) && Pattern.matches(NUMBER_REGEX, number);
	}
	
	public static boolean isValidString(String string) {
		return ((string == null) || (string.trim().isEmpty()));
	}
}

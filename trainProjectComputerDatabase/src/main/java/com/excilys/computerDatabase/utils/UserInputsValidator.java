package com.excilys.computerDatabase.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class UserInputsValidator {
	private static final String NUMBER_REGEX = "[0-9]*";
	private static final String DATE_REGEX = "[1-2][0-9]{3}-[0-1][1-9]-[0-3][0-9]";
	private static final Logger LOG = LoggerFactory.getLogger(UserInputsValidator.class);
	
	/**
	 * Teste si une date est valide ou pas AAAA-MM-JJ.
	 * @param date La date à valider.
	 * @return <code>true</code> si la date est valide, <code>false</code> sinon.
	 */
	public static boolean isValidDate(String date) {
		LOG.info("isValidDate(" + date + ")");
		if (!isValidString(date)) {
			return false;
		}
		boolean result = Pattern.matches(DATE_REGEX, date);
		int day = 0;
		int month = 0;
		int year = 0;
		StringTokenizer st = new StringTokenizer(date, "-");
		String token;
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			if (!isValidNumber(token)) {
				return false;
			}
			year = Integer.parseInt(token);
		}
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			if (!isValidNumber(token)) {
				return false;
			}
			month = Integer.parseInt(token);
		}
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			if (!isValidNumber(token)) {
				return false;
			}
			day = Integer.parseInt(token);
		}
		if ((day <= 0) || (month > 12) || (month <= 0) || (year <= 0)) {
			return false;
		}
		switch (month) {
		case 4: case 6:
        case 9: case 11 :
			result &= (day <= 30);
			break;
		case 2 :
			if (isBissextileYear(year)) {
				result &= (day <= 29);
			} else {
				result &= (day <= 28);
			}
			break;
        case 1: case 3: case 5:
        case 7: case 8: case 10:
        case 12:
			result &= (day <= 31);
			break;
		default :
			result = false;
			break;
		}
		return result;
	}
	
	public static String nullifyEmptyString(String str) {
		if ((str == null) || (str.trim().isEmpty())) {
			return null;
		} else {
			return str;
		}
	}
	
	public static boolean isValidOrNullDate(String date) {
		return ((date == null) || (isValidDate(date)));
	}
	
	private static boolean isBissextileYear(int y) {
		LOG.info("isBissextileYear(" + y + ")");
		return (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0));
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
	
	public static boolean isValidString(String string) {
		LOG.info("isValidString(" + string + ")");
		return ((string != null) && (!string.trim().isEmpty()));
	}
}

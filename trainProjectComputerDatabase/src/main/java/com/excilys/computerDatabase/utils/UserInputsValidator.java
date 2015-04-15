package com.excilys.computerDatabase.utils;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserInputsValidator {
	private static final Logger LOG = LoggerFactory.getLogger(UserInputsValidator.class);
	private static final String NUMBER_REGEX = "[0-9]*";
	private static final String DATE_REGEX_MESSAGE_CODE = "date.regex";

	private static Locale locale = LocaleContextHolder.getLocale();
//	private static ApplicationContext context = new ClassPathXmlApplicationContext("messageContext.xml");
//	private static String dateRegEx = context.getMessage("date.regex", 
//			null, locale);
//	private static String dateSep = context.getMessage("date.sep", 
//			null, locale);
	@Autowired
	private MessageSource messageSource;

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

	/**
	 * Teste si une date est valide ou pas AAAA-MM-JJ.
	 * @param date La date à valider.
	 * @return <code>true</code> si la date est valide, <code>false</code> sinon.
	 */
	public boolean isValidDate(String date) {
		LOG.info("isValidDate(" + date + ")");
		boolean result = isWellFormedDate(date);
		int day = 0;
		int month = 0;
		int year = 0;
		StringTokenizer st;
		String token;
		switch (locale.getLanguage()) {
		case "fr" :
			st = new StringTokenizer(date, "/");
			if (st.hasMoreTokens()) {
				token = st.nextToken();
				if (!isValidNumber(token)) {
					return false;
				}
				day = Integer.parseInt(token);
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
				year = Integer.parseInt(token);
			}
			break;
		case "en" :
			st = new StringTokenizer(date, "-");
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
			break;
		}
		return result && isExistingDate(year, month, day);
	}

	// OUTILS
	private boolean isWellFormedDate(String date) {
		LOG.info("isWellFormedDate(" + date + ")");
		String pattern = null;
		if (messageSource == null) {
			switch (locale.getLanguage()) {
			case "fr" :
				pattern = "[0-3][0-9]/[0-1][1-9]/[1-2][0-9]{3}";
				break;
			case "en" :
				pattern = "[1-2][0-9]{3}-[0-1][1-9]-[0-3][0-9]";
				break;
			}
		} else {
			pattern = messageSource.getMessage(DATE_REGEX_MESSAGE_CODE, null, locale);
		}
		return isValidString(date) && Pattern.matches(pattern, date);
	}

	private static boolean isExistingDate(int year, int month, int day) {
		boolean result = false;
		if ((day <= 0) || (month > 12) || (month <= 0) || (year <= 0)) {
			return false;
		}
		switch (month) {
		case 4: case 6:
		case 9: case 11 :
			result = (day <= 30);
			break;
		case 2 :
			if (isBissextileYear(year)) {
				result = (day <= 29);
			} else {
				result = (day <= 28);
			}
			break;
		case 1: case 3: case 5:
		case 7: case 8: case 10:
		case 12:
			result = (day <= 31);
			break;
		default :
			result = false;
			break;
		}
		return result;
	}

	private static boolean isBissextileYear(int y) {
		LOG.info("isBissextileYear(" + y + ")");
		return (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0));
	}
}

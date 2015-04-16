package com.excilys.computerDatabase.validators;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.util.Locale;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DateValidator {
	private static final Logger LOG = LoggerFactory.getLogger(DateValidator.class);
	private static final String DATE_REGEX_MESSAGE_CODE = "date.regex";
	private static final String DATE_FORMAT_MESSAGE_CODE = "date.format";
	private static Locale locale = LocaleContextHolder.getLocale();
//	private static ApplicationContext context = new ClassPathXmlApplicationContext("messageContext.xml");
//	private static String dateRegEx = context.getMessage("date.regex", 
//			null, locale);
//	private static String dateSep = context.getMessage("date.sep", 
//			null, locale);
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Teste si une date est valide ou pas selon le format local.
	 * @param date La date à valider.
	 * @return <code>true</code> si la date est valide, <code>false</code> sinon.
	 */
	public boolean isValidDate(String date) {
		LOG.info("isValidDate(" + date + ")");
		if (date == null) {
			return true;
		}
		if (!isWellFormedDate(date)) {
			return false;
		}
		boolean result = false;
		Locale locale = LocaleContextHolder.getLocale();
		String dateFormat = messageSource.getMessage(DATE_FORMAT_MESSAGE_CODE, null, locale);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
		// dirty checking pour parser une date à format variable et regarder si cette date existe.
		try {
			LocalDate.parse(date, dateFormatter);
		} catch (DateTimeParseException e) {
			result = false;
		}
		return result;
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
		return Pattern.matches(pattern, date);
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
			result = (isBissextileYear(year)) ? (day <= 29) : (day <= 28);
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

package com.excilys.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
/**
 * 
 * @author excilys
 * This class contains a method to validate dates, 
 *   basing upon the local format.
 */
public class DateValidator {
	private static final Logger LOG = LoggerFactory.getLogger(DateValidator.class);
	// The message.properties code for local date regex.
	private static final String DATE_REGEX_MESSAGE_CODE = "date.regex";
	// The local DateTimeFormatter pattern.
	private static final String DATE_FORMAT_MESSAGE_CODE = "date.format";
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Tests whether a date is valid or not, using local criteria.
	 * @param date A String containing the date to check.
	 * @return <code>true</code> if it is a valid date, <code>false</code> otherwise.
	 */
	public boolean isValidDate(String date) {
		LOG.trace("isValidDate(" + date + ")");
		if (date == null) {
			return true;
		}
		boolean result = true;
		Locale locale = LocaleContextHolder.getLocale();
		if (!isWellFormedDate(date, locale)) {
			return false;
		}
		String dateFormat = messageSource.getMessage(DATE_FORMAT_MESSAGE_CODE, null, locale);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
		// dirty checking pour parser une date à format variable
		try {
			LocalDate lDate = LocalDate.parse(date, dateFormatter);
			result = isExistingDate(lDate.getYear(), lDate.getMonthValue(), lDate.getDayOfMonth());
		} catch (DateTimeParseException e) {
			result = false;
		}
		return result;
	}

	// OUTILS
	/*
	 * Test if the String matches the local regex.
	 */
	private boolean isWellFormedDate(String date, Locale locale) {
		LOG.trace("isWellFormedDate(" + date + ")");
		String pattern = messageSource.getMessage(DATE_REGEX_MESSAGE_CODE, null, locale);
		return Pattern.matches(pattern, date);
	}

	/*
	 * Check if the date is an existing date, basing upon the leap years 
	 *   or the 30/31 months.
	 */
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

	/*
	 * Return true if the given year is a leapYear, false otherwise.
	 */
	private static boolean isBissextileYear(int y) {
		LOG.trace("isBissextileYear(" + y + ")");
		return (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0));
	}

}

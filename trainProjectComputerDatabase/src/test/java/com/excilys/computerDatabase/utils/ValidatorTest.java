package com.excilys.computerDatabase.utils;

import org.junit.Assert;
import org.junit.Test;

import com.excilys.computerDatabase.utils.UserInputsValidator;

public class ValidatorTest {
	@Test
	public void shouldValidateTheString() {
		// Given
		String test = "test";
		// When
		boolean result = UserInputsValidator.isValidString(test);
		// Then
		Assert.assertTrue("Erreur sur le valideur de string.", result);
	}
	
	@Test
	public void shouldNotValidateTheStringBecauseOfNullString() {
		// Given
		String test = null;
		// When
		boolean result = UserInputsValidator.isValidString(test);
		// Then
		Assert.assertFalse("Erreur sur le valideur de string.", result);
	}
	
	@Test
	public void shouldNotValidateTheStringBecauseOfEmptyString() {
		// Given
		String test = "";
		String test2 = "	 ";
		// When
		boolean result = UserInputsValidator.isValidString(test);
		boolean result2 = UserInputsValidator.isValidString(test2);
		// Then
		Assert.assertFalse("Erreur sur le valideur de string.", result);
		Assert.assertFalse("Erreur sur le valideur de string.", result2);
	}
	
	@Test
	public void shouldValidateTheNumber() {
		// Given
		String number = "15";
		// When
		boolean result = UserInputsValidator.isValidNumber(number);
		// Then
		Assert.assertTrue("Erreur sur le valideur de nombres.", result);
	}
	
	@Test
	public void shoudlNotValidateTheNumberBecauseOfWrongFormat() {
		// Given
		String number = "15a";
		// When
		boolean result = UserInputsValidator.isValidNumber(number);
		// Then
		Assert.assertFalse("Erreur sur le valideur de nombres.", result);
	}
	
	@Test
	public void shouldValidateTheDate() {
		// Given
		String date = "2015-03-22";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlValidateTheDateBecauseOfBissextileYear() {
		// Given
		String date = "2016-02-29";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongYearFormat() {
		// Given
		String date = "15-03-22";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonthFormat() {
		// Given
		String date = "2015-3-22";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayFormat() {
		// Given
		String date = "2015-03-2";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongYear() {
		// Given
		String date = "9999-03-22";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonth() {
		// Given
		String date = "2015-27-22";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDay() {
		// Given
		String date = "2015-27-37";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyDayMonth() {
		// Given
		String date = "2015-06-31";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyOneDayMonth() {
		// Given
		String date = "2015-07-32";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfBissextileYear() {
		// Given
		String date = "2015-02-29";
		// When
		boolean result = UserInputsValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
}

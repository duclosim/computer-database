package com.excilys.computerDatabase.validators;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.validators.UserInputsValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ValidatorsTest {
	@Autowired
	private DateValidator dateValidator;
	
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
		String date = "22/03/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlValidateTheDateBecauseOfBissextileYear() {
		// Given
		String date = "29/02/2016";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongYearFormat() {
		// Given
		String date = "22/03/15";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonthFormat() {
		// Given
		String date = "22/3/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayFormat() {
		// Given
		String date = "2/03/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongYear() {
		// Given
		String date = "22/03/9999";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonth() {
		// Given
		String date = "22/27/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDay() {
		// Given
		String date = "37/27/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyDayMonth() {
		// Given
		String date = "31/06/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyOneDayMonth() {
		// Given
		String date = "32/07/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfBissextileYear() {
		// Given
		String date = "29/02/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
}

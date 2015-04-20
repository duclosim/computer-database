package com.excilys.validators;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class ValidatorsTest {
	private static final Logger LOG = LoggerFactory.getLogger(ValidatorsTest.class);
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private DateValidator dateValidator;
	
	@Test
	public void shouldValidateTheString() {
		LOG.debug("shouldValidateTheString()");
		// Given
		String test = "test";
		// When
		boolean result = UserInputsValidator.isValidString(test);
		// Then
		Assert.assertTrue("Erreur sur le valideur de string.", result);
	}
	
	@Test
	public void shouldNotValidateTheStringBecauseOfNullString() {
		LOG.debug("shouldNotValidateTheStringBecauseOfNullString()");
		// Given
		String test = null;
		// When
		boolean result = UserInputsValidator.isValidString(test);
		// Then
		Assert.assertFalse("Erreur sur le valideur de string.", result);
	}
	
	@Test
	public void shouldNotValidateTheStringBecauseOfEmptyString() {
		LOG.debug("shouldNotValidateTheStringBecauseOfEmptyString()");
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
		LOG.debug("shouldValidateTheNumber()");
		// Given
		String number = "15";
		// When
		boolean result = UserInputsValidator.isValidNumber(number);
		// Then
		Assert.assertTrue("Erreur sur le valideur de nombres.", result);
	}
	
	@Test
	public void shoudlNotValidateTheNumberBecauseOfWrongFormat() {
		LOG.debug("shoudlNotValidateTheNumberBecauseOfWrongFormat()");
		// Given
		String number = "15a";
		// When
		boolean result = UserInputsValidator.isValidNumber(number);
		// Then
		Assert.assertFalse("Erreur sur le valideur de nombres.", result);
	}
	
	@Test
	public void shouldValidateTheDate() {
		LOG.debug("shouldValidateTheDate()");
		// Given
		String date = "22/03/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlValidateTheDateBecauseOfBissextileYear() {
		LOG.debug("shoudlValidateTheDateBecauseOfBissextileYear()");
		// Given
		String frDate = "29/02/2016";
		// When
		boolean result = dateValidator.isValidDate(frDate);
		// Then
		Assert.assertTrue("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongYearFormat() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongYearFormat()");
		// Given
		String date = "22/03/15";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonthFormat() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongMonthFormat()");
		// Given
		String date = "22/3/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayFormat() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongDayFormat()");
		// Given
		String date = "2/03/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongMonth() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongMonth()");
		// Given
		String date = "22/27/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDay() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongDay()");
		// Given
		String date = "37/12/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
	
	@Test
	public void shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyOneDayMonth() {
		LOG.debug("shoudlNotValidateTheDateBecauseOfWrongDayOfThirtyOneDayMonth()");
		// Given
		String date = "32/07/2015";
		// When
		boolean result = dateValidator.isValidDate(date);
		// Then
		Assert.assertFalse("Erreur sur le valideur de dates.", result);
	}
}

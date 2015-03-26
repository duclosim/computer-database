package com.excilys.computerDatabase.ui;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

// TODO
public class WebUserInterfaceTest {
	private WebDriver driver;
	private static final String HOME_PAGE = "http://localhost:8080/trainProjectComputerDatabase/dashboard";
	
	@Before
	public void initDriver() {
		// Create a new instance of the Firefox driver
		driver = new FirefoxDriver();
	    driver.get(HOME_PAGE);
	}
	
	@After
	public void closeDriver() {
	    //Close the browser
	    driver.quit();
	}

	@Test
	public void addAComputerShouldAddOneToCount() {
		// Given
		String addButtonId = "addComputer";
		String computerNameFieldId = "computerName";
		String itemsCount = "homeTitle";
	    WebElement element = driver.findElement(By.id(itemsCount));
		int expectedNbItems = Integer.parseInt(element.getText());
		++expectedNbItems;
	    // When
	    element = driver.findElement(By.id(addButtonId));
	    element.click();
	    // Wait for the page to load, timeout after 10 seconds
	    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver d) {
	            return d.getTitle().toLowerCase().startsWith("Computer Database - Add a computer");
	        }
	    });

	    element = driver.findElement(By.id(computerNameFieldId));
	    element.sendKeys("seleniumBean");
	    // Now submit the form. WebDriver will find the form for us from the element
	    element.submit();
	    driver.get(HOME_PAGE);
	    // Wait for the page to load, timeout after 10 seconds
	    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver d) {
	            return d.getTitle().toLowerCase().startsWith("Computer Database");
	        }
	    });
	    
	    int nbItems = Integer.parseInt(element.getText());
	    // Then
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", expectedNbItems, nbItems);
	}
	
	public void deleteAComputerShouldRemoveOneToCount() {
		
	}
	
	public void p() {
		
	}
}

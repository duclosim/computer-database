package com.excilys.computerDatabase.ui;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

// TODO finir d'écrire les tests selenium
public class WebUserInterfaceTest {
	private WebDriver driver;
	private static final String HOME_PAGE = "http://localhost:8080/trainProjectComputerDatabase/dashboard";
	private static final String beanName = "seleniumBean";
	
	@Before
	public void initDriver() {
		ComputerDTO c = new ComputerDTO();
		c.setName(beanName);
		ComputerServiceImpl.INSTANCE.create(c);
		// Create a new instance of the Firefox driver
		driver = new FirefoxDriver();
	    driver.get(HOME_PAGE);
	}
	
	@After
	public void closeDriver() {
	    //Close the browser
	    driver.quit();
	    List<ComputerDTO> list = ComputerServiceImpl.INSTANCE.getFiltered(beanName, 1, 0);
	    for (ComputerDTO computer : list) {
	    	ComputerServiceImpl.INSTANCE.delete(computer);
	    }
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

	    element = driver.findElement(By.name(computerNameFieldId));
	    element.sendKeys(beanName);
	    // Now submit the form. WebDriver will find the form for us from the element
	    element.submit();
	    driver.get(HOME_PAGE);
	    element = driver.findElement(By.id(itemsCount));
	    int nbItems = Integer.parseInt(element.getText());
	    // Then
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", expectedNbItems, nbItems);
	}

	@Ignore
	@Test
	public void deleteAComputerShouldRemoveOneToCount() {
		// Given
		String lastPageButtonId = "Last";
		String editModeButtonId = "editComputer";
		String rmBoxId = "computerRm575";
		String rmButtonId = "deleteSelected";
		String itemsCount = "homeTitle";
	    WebElement element = driver.findElement(By.id(itemsCount));
		int expectedNbItems = Integer.parseInt(element.getText());
		--expectedNbItems;
	    // When
		// Aller à la dernière page.
		element = driver.findElement(By.id(lastPageButtonId));
		element.click();
		// Se mettre en mode édition.
		element = driver.findElement(By.id(editModeButtonId));
	    element.click();
	    // Sélectionner l'ordinateur
	    element = driver.findElement(By.id(rmBoxId));
	    element.click();
	    // Supprimer les ordinateurs séléctionnés.
	    element = driver.findElement(By.id(rmButtonId));
	    element.click();
	    // TODO valider la fenêtre javascript
	    // Then
	    element = driver.findElement(By.id(itemsCount));
	    int nbItems = Integer.parseInt(element.getText());
	    // Then
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", expectedNbItems, nbItems);
	}
	
	@Test
	public void searchComputer() {
		// Given
		String searchFieldId = "searchbox";
		String searchedComputerName = "CM-2a";
	    WebElement element = driver.findElement(By.id(searchFieldId));
	    // When
	    element.sendKeys(searchedComputerName);
	    element.submit();
	    // Then
	    String foundName = element.findElement(By.linkText(searchedComputerName)).getText();
	    Assert.assertEquals("Erreur sur l'item trouvé.", searchedComputerName, foundName);
	}
	
	@Ignore
	@Test
	public void searchCompany()	 {
		// Given
		
		// When
		
		// Then
		
	}

	@Ignore
	@Test
	public void orderByComputerName() {
		// Given
		
		// When
		
		// Then
		
	}

	@Ignore
	@Test
	public void orderByIntroducedDate() {
		// Given
		
		// When
		
		// Then
		
	}

	@Ignore
	@Test
	public void orderByDiscontinuedDate() {
		// Given
		
		// When
		
		// Then
		
	}

	@Ignore
	@Test
	public void orderByCompanyName() {
		// Given
		
		// When
		
		// Then
		
	}
}

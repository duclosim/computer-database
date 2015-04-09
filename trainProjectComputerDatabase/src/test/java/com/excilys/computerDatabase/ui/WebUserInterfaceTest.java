package com.excilys.computerDatabase.ui;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

// TODO corriger les tests en erreur
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@ActiveProfiles("DEV")
public class WebUserInterfaceTest {
	private WebDriver driver;
	private static final String HOME_PAGE = "http://localhost:8080/trainProjectComputerDatabase/dashboard";
	private static final String beanName = "seleniumBean";
	
	@Autowired
	ComputerServiceImpl computerService;
	
	@Before
	public void initDriver() {
		ComputerDTO c = new ComputerDTO();
		c.setName(beanName);
		computerService.create(c);
		// Create a new instance of the Firefox driver
		driver = new FirefoxDriver();
	    driver.get(HOME_PAGE);
	}
	
	@After
	public void closeDriver() {
	    //Close the browser
	    driver.quit();
	    List<ComputerDTO> list = computerService.getFiltered(beanName, 1, 0);
	    for (ComputerDTO computer : list) {
	    	computerService.delete(computer);
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
	public void editComputerShouldUpdateTheComputer() {
		// TODO finir d'écrire le test
		// Given
		
		// When
		
		// Then
		
	}

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
	    driver.switchTo().alert().accept();
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
	
	@Test
	public void searchCompany()	 {
		// Given
		String searchFieldId = "searchbox";
		String itemsCount = "homeTitle";
		String companyName = "Sun Microsystems";
		int expectedComputerNb = 2;
	    WebElement element = driver.findElement(By.id(searchFieldId));
		// When
	    element.sendKeys(companyName);
	    element.submit();
		// Then
	    element = driver.findElement(By.id(itemsCount));
	    int nbItems = Integer.parseInt(element.getText());
		Assert.assertEquals("Erreur sur le nombre d'items trouvés.", expectedComputerNb, nbItems);
	}

	@Test
	public void orderByComputerNameAsc() {
		// Given
		String orderByCmptrAscField = "orderByComputerNameAsc";
		String computerName = "ACE";
		WebElement element = driver.findElement(By.id(orderByCmptrAscField));
		// When
		element.click();
		// Then
		String foundName = element.findElement(By.linkText(computerName)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}

	@Test
	public void orderByIntroducedDateDesc() {
		// Given
		String orderByIntroDescField = "orderByIntroducedDesc";
		String computerName = "iPhone 4S";
		WebElement element = driver.findElement(By.id(orderByIntroDescField));
		// When
		element.click();
		// Then
		String foundName = element.findElement(By.linkText(computerName)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}

	@Test
	public void orderByCompanyNameDesc() {
		// Given
		String orderByCompanyDescField = "orderByCompanyNameDesc";
		String computerName = "MSX";
		WebElement element = driver.findElement(By.id(orderByCompanyDescField));
		// When
		element.click();
		// Then
		String foundName = element.findElement(By.linkText(computerName)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}
	
	@Test
	public void filteredByCompanyNameAndOrderByComputerNameAsc() {
		// TODO finir d'écrire le test
		// Given
		String searchFieldId = "searchbox";
		String orderByCmptrAscField = "orderByComputerNameAsc";
		String companyName = "Nintendo";
		String computerName = "Game & Watch";
		// When
		// Filtering
	    WebElement element = driver.findElement(By.id(searchFieldId));
	    element.sendKeys(companyName);
	    element.submit();
		// Ordering
		element = driver.findElement(By.id(orderByCmptrAscField));
		element.click();
		// Then
		String foundName = element.findElement(By.linkText(computerName)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}
}

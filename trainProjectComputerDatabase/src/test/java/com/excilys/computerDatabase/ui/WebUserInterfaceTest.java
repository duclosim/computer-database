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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

// TODO enlever @Ignore
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
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
	    List<ComputerDTO> list = computerService.getFiltered(beanName, 10, 0);
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
	    // filling the form
	    element = driver.findElement(By.name(computerNameFieldId));
	    element.sendKeys(beanName);
	    element.submit();
	    driver.get(HOME_PAGE);
	    element = driver.findElement(By.id(itemsCount));
	    int nbItems = Integer.parseInt(element.getText());
	    // Then
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", expectedNbItems, nbItems);
	}
	
	@Test
	public void editComputerShouldUpdateTheComputer() {
		// Given
		String editedItemId = "computer3";
		String newIntroDate = "2015-02-22";
		String introDateFieldId = "introduced";
		WebElement element = driver.findElement(By.id(editedItemId));
		element.click();
		// When
		element = driver.findElement(By.name(introDateFieldId));
		element.sendKeys(newIntroDate);
		element.submit();
		// Then
		ComputerDTO editedBean = computerService.getById(new Long(3));
		Assert.assertTrue("Bean non modifié correctement.", editedBean.getIntroducedDate().contains(newIntroDate));
		// After
		element = driver.findElement(By.name(introDateFieldId));
		element.clear();
		element.sendKeys("");
		element.submit();
	}

	@Test
	public void deleteAComputerShouldRemoveOneToCount() {
		// Given
		ComputerDTO newBean = new ComputerDTO();
		newBean.setName(beanName);
		computerService.create(newBean);
		driver.get(HOME_PAGE);
		String lastPageButtonId = "Last";
		String editModeButtonId = "editComputer";
		String rmBoxId = "computerRm" + newBean.getId();
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
		String itemsCount = "homeTitle";
		String searchedComputerName = "CM-2a";
		int expectedComputerNb = 1;
	    WebElement element = driver.findElement(By.id(searchFieldId));
	    // When
	    element.sendKeys(searchedComputerName);
	    element.submit();
	    // Then
	    element = driver.findElement(By.id(itemsCount));
	    int nbItems = Integer.parseInt(element.getText());
		Assert.assertEquals("Erreur sur le nombre d'items trouvés.", expectedComputerNb, nbItems);
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
		String computerId = "computer381";
		WebElement element = driver.findElement(By.id(orderByCmptrAscField));
		// When
		element.click();
		// Then
		String foundName = driver.findElement(By.id(computerId)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}

	@Test
	public void orderByIntroducedDateDesc() {
		// Given
		String orderByIntroDescField = "orderByIntroducedDesc";
		String computerName = "HP TouchPad";
		String computerId = "computer569";
		WebElement element = driver.findElement(By.id(orderByIntroDescField));
		// When
		element.click();
		// Then
		String foundName = driver.findElement(By.id(computerId)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}

	@Test
	public void orderByCompanyNameDesc() {
		// Given
		String orderByCompanyDescField = "orderByCompanyNameDesc";
		String computerName = "MSX";
		String computerId = "computer248";
		WebElement element = driver.findElement(By.id(orderByCompanyDescField));
		// When
		element.click();
		// Then
		String foundName = driver.findElement(By.id(computerId)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}
	
	@Test
	public void filteredByCompanyNameAndOrderByComputerNameAsc() {
		// Given
		String searchFieldId = "searchbox";
		String orderByCmptrAscField = "orderByComputerNameAsc";
		String companyName = "Nintendo";
		String computerName = "Game & Watch";
		String computerId = "computer160";
		// When
		// Filtering
	    WebElement element = driver.findElement(By.id(searchFieldId));
	    element.sendKeys(companyName);
	    element.submit();
		// Ordering
		element = driver.findElement(By.id(orderByCmptrAscField));
		element.click();
		// Then
		String foundName = driver.findElement(By.id(computerId)).getText();
		Assert.assertEquals("Erreur sur l'item trouvé.", computerName, foundName);
	}
}

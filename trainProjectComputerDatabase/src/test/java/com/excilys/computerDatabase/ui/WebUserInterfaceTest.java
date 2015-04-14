package com.excilys.computerDatabase.ui;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.ComputerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class WebUserInterfaceTest {
	private WebDriver driver;
	private static final String HOME_PAGE = "http://localhost:8080/trainProjectComputerDatabase/dashboard";
	// Field ids/names
	private static final String BEAN_NAME_FIELD = "computerName";
	private static final String BEAN_INTRO_DATE_FIELD = "introduced";
	private static final String BEAN_DIS_DATE_FIELD = "discontinued";
	private static final String BEAN_COMPANY_FIELD = "companyId";
	private static final String COMPUTER_COUNT_FIELD = "homeTitle";
	// Bean attributes
	private static final String BEAN_NAME = "seleniumBean";
	private static final String BEAN_INTRO_DATE = "2015-02-15";
	private static final String BEAN_DIS_DATE = "2015-04-14";
	private static final String BEAN_COMPANY = "Commodore International";
	
	@Autowired
	ComputerService computerService;
	
	@Before
	public void initDriver() {
		ComputerDTO c = new ComputerDTO();
		c.setName(BEAN_NAME);
		computerService.create(c);
		// Create a new instance of the Firefox driver
		driver = new FirefoxDriver();
	    driver.get(HOME_PAGE);
	}
	
	@After
	public void closeDriver() {
	    //Close the browser
	    driver.quit();
	    List<ComputerDTO> list = computerService.getFiltered(BEAN_NAME, 10, 0);
	    for (ComputerDTO computer : list) {
	    	computerService.delete(computer);
	    }
	}

	@Test
	public void addAComputerWithNullDateAndCompanyShouldSetFields() {
		// Given
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		dto.setIntroducedDate(null);
		dto.setDiscontinuedDate(null);
		dto.setCompanyName(null);
		// When
		addBean(dto);
		// Then
	    ComputerDTO bean = computerService.getFilteredAndOrdered(1, 0, 
	    		BEAN_NAME, ComputerColumn.ID_COLUMN_LABEL, OrderingWay.DESC).get(0);
	    Assert.assertEquals("Nom mal attribué.", BEAN_NAME, bean.getName());
	    Assert.assertNull("Date d'introduction mal attribuée.", bean.getIntroducedDate());
	    Assert.assertNull("Date de retrait mal attribuée.", bean.getDiscontinuedDate());
	    Assert.assertNull("Nom d'entreprise mal attribué.", bean.getCompanyName());
	}
	
	@Test
	public void addAComputerShouldAddOneToCountAndSetFields() {
		// Given
		int expectedNbItems = computerService.countAllLines();
		++expectedNbItems;
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		dto.setIntroducedDate(BEAN_INTRO_DATE);
		dto.setDiscontinuedDate(BEAN_DIS_DATE);
		dto.setCompanyName(BEAN_COMPANY);
	    // When
		addBean(dto);
	    driver.get(HOME_PAGE);
	    int nbItems = computerService.countAllLines();
	    ComputerDTO bean = computerService.getFilteredAndOrdered(1, 0, 
	    		BEAN_NAME, ComputerColumn.ID_COLUMN_LABEL, OrderingWay.DESC).get(0);
	    // Then
	    Assert.assertEquals("Nom mal attribué.", bean.getName(),  BEAN_NAME);
	    Assert.assertEquals("Date d'introduction mal attribuée.", BEAN_INTRO_DATE, bean.getIntroducedDate());
	    Assert.assertEquals("Date de retrait mal attribuée.", BEAN_DIS_DATE, bean.getDiscontinuedDate());
	    Assert.assertEquals("Nom d'entreprise mal attribué.", BEAN_COMPANY, bean.getCompanyName());
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", expectedNbItems, nbItems);
	}
	
	@Test
	public void editComputerShouldUpdateTheComputer() {
		// Given
		String editedItemId = "computer3";
		WebElement element = driver.findElement(By.id(editedItemId));
		element.click();
		// When
		element = driver.findElement(By.id(BEAN_INTRO_DATE_FIELD));
		element.sendKeys(BEAN_INTRO_DATE);
		element.submit();
		// Then
		ComputerDTO editedBean = computerService.getById(new Long(3));
		String editedDate = editedBean.getIntroducedDate();
		editedBean.setIntroducedDate(null);
		computerService.update(editedBean);
		Assert.assertNotNull("introducedDate à null.", editedDate);
		Assert.assertTrue("Bean non modifié correctement.", editedDate.contains(BEAN_INTRO_DATE));
	}

	@Test
	public void deleteAComputerShouldRemoveOneToCount() {
		// Given
		ComputerDTO newBean = new ComputerDTO();
		newBean.setName(BEAN_NAME);
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
	
	/*
	 * Entre les infos du DTO dans le formulaire de création.
	 */
	private void addBean(ComputerDTO bean) {
		// Given
		String addButtonId = "addComputer";
	    WebElement element = driver.findElement(By.id(COMPUTER_COUNT_FIELD));
	    // When
	    element = driver.findElement(By.id(addButtonId));
	    element.click();
	    // filling the form
	    element = driver.findElement(By.id(BEAN_NAME_FIELD));
	    element.sendKeys(bean.getName());
	    if (bean.getIntroducedDate() != null) {
		    element = driver.findElement(By.id(BEAN_INTRO_DATE_FIELD));
		    element.sendKeys(bean.getIntroducedDate());
	    }
	    if (bean.getDiscontinuedDate() != null) {
		    element = driver.findElement(By.id(BEAN_DIS_DATE_FIELD));
		    element.sendKeys(bean.getDiscontinuedDate());
	    }
	    if (bean.getCompanyName() != null) {
		    element = driver.findElement(By.id(BEAN_COMPANY_FIELD));
		    Select dropDown = new Select(element);           
		    List<WebElement> options = dropDown.getOptions();
		    for(WebElement option : options){
			    if(option.getText().equals(bean.getCompanyName())) {
			    	option.click(); //select option here;       
			    }               
		    }
	    }
	    element.submit();
	}
}

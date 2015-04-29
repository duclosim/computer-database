package com.excilys.ui;

import java.awt.AWTException;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.model.beans.Computer;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.ComputerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:webapp-context.xml")
public class WebUserInterfaceTest {
	private static final Logger LOG = LoggerFactory.getLogger(WebUserInterfaceTest.class);
	private static final String HOME_PAGE = "http://localhost:8080/computer-database/dashboard";
	// Field ids/names
	private static final String BEAN_NAME_FIELD = "computerName";
	private static final String BEAN_INTRO_DATE_FIELD = "introduced";
	private static final String BEAN_DIS_DATE_FIELD = "discontinued";
	private static final String BEAN_COMPANY_FIELD = "companyId";
	private static final String COMPUTER_COUNT_FIELD = "homeTitle";
	private static final String UK_FLAG_ICON = "en_ic";
	private static final String FR_FLAG_ICON = "fr_ic";
	// Bean attributes
	private static final String BEAN_NAME = "seleniumBean";
	private static final String BEAN_FR_INTRO_DATE = "15/02/2015";
	private static final String BEAN_FR_DIS_DATE = "14/04/2015";
	private static final String BEAN_EN_INTRO_DATE = "2015-02-15";
	private static final String BEAN_EN_DIS_DATE = "2015-04-14";
	private static final String BEAN_COMPANY = "Commodore International";

	private static WebDriver driver;
	
	@Autowired
	ComputerService computerService;
	
	@BeforeClass
	public static void initDriver() throws AWTException {
		LOG.debug("initDriver()");
		// Create a new instance of the Firefox driver
		driver = new FirefoxDriver();
	    driver.get(HOME_PAGE);
	}
	
	@AfterClass
	public static void closeDriver() {
		LOG.debug("closeDriver()");
	    //Close the browser
	    driver.quit();
	}
	
	@Before
	public void createTestBean() {
		Computer c = new Computer();
		c.setName(BEAN_NAME);
		computerService.create(c);
	}
	
	@After
	public void goBackHome() {
		// delete created beans.
	    List<Computer> list = computerService.getFiltered(BEAN_NAME, 10, 0);
	    for (Computer computer : list) {
	    	computerService.delete(computer.getId());
	    }
		driver.get(HOME_PAGE);
	}
	
	@Test
	public void flagIconShouldChangeLanguage() {
		LOG.debug("flagIconShouldChangeLanguage()");
		// Given
		String expectedText = "Add Computer";
		setEnglish();
		// When
		String addButtonText = driver.findElement(By.id("addComputer")).getText();
		// Then
		driver.findElement(By.id(FR_FLAG_ICON)).click();
		Assert.assertEquals("Erreur sur le changement de langue.", 
				expectedText, addButtonText);
	}
	
	@Test
	public void addAComputerWithWrongDateFormatShouldNotBePossible() {
		LOG.debug("addAComputerWithNullDateAndCompanyShouldSetFields()");
		// Given
		Long id = 4L;
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		setFrench();
		dto.setIntroducedDate(BEAN_EN_INTRO_DATE);
		dto.setDiscontinuedDate(null);
		dto.setCompanyName(null);
		// When
		addBean(dto);
		// Then
		Computer bean = computerService.getById(id);
	    Assert.assertNull("Le bean ne devrait pas avoir de date.", bean.getIntroduced());
	}
	
	@Test
	public void addAComputerWithNullDateAndCompanyShouldSetFields() {
		LOG.debug("addAComputerWithNullDateAndCompanyShouldSetFields()");
		// Given
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		dto.setIntroducedDate(null);
		dto.setDiscontinuedDate(null);
		dto.setCompanyName(null);
		// When
		addBean(dto);
		// Then
		Computer bean = computerService.getFilteredAndOrdered(1, 0, 
	    		BEAN_NAME, ComputerColumn.ID_COLUMN_LABEL, OrderingWay.DESC).get(0);
	    Assert.assertEquals("Nom mal attribué.", BEAN_NAME, bean.getName());
	    Assert.assertNull("Date d'introduction mal attribuée.", bean.getIntroduced());
	    Assert.assertNull("Date de retrait mal attribuée.", bean.getDiscontinued());
	    Assert.assertNull("Nom d'entreprise mal attribué.", bean.getCompany());
	}
	
	@Test
	public void addAComputerInFrenchShouldAddOneToCountAndSetFields() {
		LOG.debug("addAComputerShouldAddOneToCountAndSetFields()");
		// Given
		int expectedNbItems = computerService.countAllLines();
		++expectedNbItems;
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		dto.setIntroducedDate(BEAN_FR_INTRO_DATE);
		dto.setDiscontinuedDate(BEAN_FR_DIS_DATE);
		dto.setCompanyName(BEAN_COMPANY);
		setFrench();
	    // When
		addBean(dto);
	    driver.get(HOME_PAGE);
	    int nbItems = computerService.countAllLines();
	    Computer bean = computerService.getFilteredAndOrdered(1, 0, 
	    		BEAN_NAME, ComputerColumn.ID_COLUMN_LABEL, OrderingWay.DESC).get(0);
	    // Then
	    Assert.assertEquals("Nom mal attribué.", bean.getName(),  BEAN_NAME);
	    Assert.assertEquals("Date d'introduction mal attribuée.", 
	    		BEAN_FR_INTRO_DATE, bean.getIntroduced().toString());
	    Assert.assertEquals("Date de retrait mal attribuée.", 
	    		BEAN_FR_DIS_DATE, bean.getDiscontinued().toString());
	    Assert.assertEquals("Nom d'entreprise mal attribué.", 
	    		BEAN_COMPANY, bean.getCompany().getName());
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", 
	    		expectedNbItems, nbItems);
	}
	
	@Test
	public void addAComputerInEnglishShouldAddOneToCountAndSetFields() {
		LOG.debug("addAComputerShouldAddOneToCountAndSetFields()");
		// Given
		int expectedNbItems = computerService.countAllLines();
		++expectedNbItems;
		ComputerDTO dto = new ComputerDTO();
		dto.setName(BEAN_NAME);
		dto.setIntroducedDate(BEAN_EN_INTRO_DATE);
		dto.setDiscontinuedDate(BEAN_EN_DIS_DATE);
		dto.setCompanyName(BEAN_COMPANY);
		setEnglish();
	    // When
		addBean(dto);
	    driver.get(HOME_PAGE);
	    int nbItems = computerService.countAllLines();
	    Locale locale = LocaleContextHolder.getLocale();
	    LocaleContextHolder.setLocale(Locale.ENGLISH);
	    Computer bean = computerService.getFilteredAndOrdered(1, 0, 
	    		BEAN_NAME, ComputerColumn.ID_COLUMN_LABEL, OrderingWay.DESC).get(0);
	    LocaleContextHolder.setLocale(locale);
	    // Then
	    Assert.assertEquals("Nom mal attribué.", bean.getName(),  BEAN_NAME);
	    Assert.assertEquals("Date d'introduction mal attribuée.", 
	    		BEAN_EN_INTRO_DATE, bean.getIntroduced().toString());
	    Assert.assertEquals("Date de retrait mal attribuée.", 
	    		BEAN_EN_DIS_DATE, bean.getDiscontinued().toString());
	    Assert.assertEquals("Nom d'entreprise mal attribué.", 
	    		BEAN_COMPANY, bean.getCompany().getName());
	    Assert.assertEquals("Erreur sur le nouveau nombre d'items", 
	    		expectedNbItems, nbItems);
	}
	
	@Test
	public void editAComputerWithWrongDateFormatShouldNotBePossible() {
		LOG.debug("addAComputerWithNullDateAndCompanyShouldSetFields()");
		// Given
		String editedItemId = "computer4";
		Long id = 4l;
		setFrench();
		WebElement element = driver.findElement(By.id(editedItemId));
		element.click();
		// When
		element = driver.findElement(By.id(BEAN_INTRO_DATE_FIELD));
		element.sendKeys(BEAN_EN_INTRO_DATE);
		element.submit();
		// Then
		Computer bean = computerService.getById(id);
	    Assert.assertNull("Le bean ne devrait pas avoir de date.", bean.getIntroduced());
	}
	
	@Test
	public void editComputerInFrenchShouldUpdateTheComputer() {
		LOG.debug("editComputerShouldUpdateTheComputer()");
		// Given
		String editedItemId = "computer3";
		setFrench();
		WebElement element = driver.findElement(By.id(editedItemId));
		element.click();
		// When
		element = driver.findElement(By.id(BEAN_INTRO_DATE_FIELD));
		element.sendKeys(BEAN_FR_INTRO_DATE);
		element.submit();
		// Then
		Computer editedBean = computerService.getById(3l);
		String editedDate = editedBean.getIntroduced().toString();
		editedBean.setIntroduced(null);
		computerService.update(editedBean);
		Assert.assertNotNull("introducedDate à null.", editedDate);
		Assert.assertTrue("Bean non modifié correctement.", editedDate.contains(BEAN_FR_INTRO_DATE));
	}

	@Test
	public void deleteAComputerShouldRemoveOneToCount() {
		LOG.debug("deleteAComputerShouldRemoveOneToCount()");
		// Given
		Computer newBean = new Computer();
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
		LOG.debug("searchComputer()");
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
		LOG.debug("searchCompany()");
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
		LOG.debug("orderByComputerNameAsc()");
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
		LOG.debug("orderByIntroducedDateDesc()");
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
		LOG.debug("orderByCompanyNameDesc()");
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
		LOG.debug("filteredByCompanyNameAndOrderByComputerNameAsc()");
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
		LOG.debug("addBean(" + bean + ")");
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
	
	private void setFrench() {
	    WebElement element = driver.findElement(By.id(FR_FLAG_ICON));
	    element.click();
	}
	
	private void setEnglish() {
		WebElement element = driver.findElement(By.id(UK_FLAG_ICON));
	    element.click();
	}
}

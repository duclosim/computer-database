package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.daos.CompanyDAO;
import com.excilys.computerDatabase.services.CompanyService;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class CompanyServiceTest {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private CompanyDAO companyDao;
	
	@Test
	public void getById() throws SQLException {
		// Given
		Long id = new Long(3);
		Company expectedCompany = companyDao.getById(id);
		// When
		Company result = companyService.getById(id);
		// Then
		Assert.assertEquals("Mauvais bean récupéré.", expectedCompany, result);
	}
	
	@Test
	public void getAll() throws SQLException {
		// Given
		List<Company> expectedCompanies = companyDao.getAll();
		// When
		List<Company> result = companyService.getAll();
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedCompanies, result);
	}
	
	@Test
	public void countAllLines() throws SQLException {
		// Given
		int expectedNbLines = companyDao.countLines();
		// When
		int result = companyService.countAllLines();
		// Then
		Assert.assertEquals("Mauvais bean récupéré", expectedNbLines, result);
	}
	
	@Ignore
	@Test
	public void delete() {
		// TODO
	}
	
}

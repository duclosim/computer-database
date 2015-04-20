package com.excilys.services;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.model.beans.Company;
import com.excilys.persistence.daos.CompanyDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class CompanyServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceTest.class);
	@Autowired
	private CompanyService companyService;
	@Autowired
	private CompanyDAO companyDao;
	
	@Test
	public void getById() throws SQLException {
		LOG.debug("getById()");
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
		LOG.debug("getAll()");
		// Given
		List<Company> expectedCompanies = companyDao.getAll();
		// When
		List<Company> result = companyService.getAll();
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedCompanies, result);
	}
	
	@Test
	public void countAllLines() throws SQLException {
		LOG.debug("countAllLines()");
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
		LOG.debug("delete()");
		// TODO
	}
	
}

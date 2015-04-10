package com.excilys.computerDatabase.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class CompanyDAOTest {
	@Autowired
	private CompanyDAOImpl companyDAO;
	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//    @BeforeClass
//    public static void setUpDB() {
//    	try {
//    		DBUtils.executeSqlFiles();
//    	} catch (SQLException | IOException e) {
//			e.printStackTrace();
//		}
//    }
//
//    @AfterClass
//    public static void tearDown() throws Exception {
//        DBUtils.databaseTester.onTearDown();
//    }
	
	@Test
	public void getByIdShouldReturnABean() throws SQLException {
		// Given
		Company bean;
		Long id = new Long(10);
		Company expectedBean;
		String query = "SELECT * FROM company WHERE id=?;";
		List<Company> results = jdbcTemplate.query(query, new Object[]{id}, mapper);
		// When
		bean = companyDAO.getById(id);
		// Then
		expectedBean = results.isEmpty() ? null : results.get(0);
		Assert.assertNotNull("Erreur sur le bean.", bean);
		Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
	}
	
	@Test
	public void getByNonFindableIdShouldReturnNullResult() throws SQLException {
		// Given
		Company bean;
		Long id = new Long(-10);
		// When
		bean = companyDAO.getById(id);
		// Then
		Assert.assertNull("Le computer devrait être à null.", bean);
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		// Given
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		List<Company> bean;
		List<Company> expectedBeans = jdbcTemplate.query(query, new Object[]{limit, offset}, mapper);
		// When
		bean = companyDAO.getAll(limit, offset);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() throws SQLException {
		// Given
		int nbLines;
		String query = "SELECT COUNT(*) FROM company;";
		int expectedSize = jdbcTemplate.queryForObject(query, Integer.class);
		// When
		nbLines = companyDAO.countLines();
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
	}
}

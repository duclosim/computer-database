package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = SQLException.class)
public class CompanyDAOTest {
	private Connection con;
	
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private ConnectionFactory connection;
	
	@Before
	public void prepareConnection() throws SQLException {
		con = connection.getConnection();
	}
	
	@After
	public void closeConnection() throws SQLException {
		con.close();
		throw new SQLException("Test");
	}
	
	@Test
	public void getByIdShouldReturnABean() throws SQLException {
		// Given
		Company bean;
		Long id = new Long(10);
		Company expectedBean;
		String query = "SELECT * FROM company WHERE id=?;";
		ResultSet results;
		
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, id);
		results = ps.executeQuery();
		if (results.next()) {
			expectedBean = mapper.mapCompany(results);
			ps.close();
			// When
			bean = companyDAO.getById(id);
			// Then
			Assert.assertNotNull("Erreur sur le bean.", bean);
			Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
		}
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		// Given
		List<Company> bean;
		List<Company> expectedBeans = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		ResultSet results;

		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeans.add(mapper.mapCompany(results));
		}
		ps.close();
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
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			int expectedSize = results.getInt(1);
			ps.close();
			// When
			nbLines = companyDAO.countLines();
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
	}
}

package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOTest {
	private Connection con;
	private CompanyDAO commpanyDAO;
	private CompanyMapper mapper;
	
	@Before
	public void initConnection() {
		con = ConnectionFactory.INSTANCE.getConnection();
		commpanyDAO = CompanyDAOImpl.INSTANCE;
		mapper = CompanyMapper.INSTANCE;
	}
	
	@After
	public void closeConnection() {
		ConnectionFactory.INSTANCE.closeConnection(con);
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
			// When
			bean = commpanyDAO.getById(id, con);
			// Then
			Assert.assertNotNull("Erreur sur le bean.", bean);
			Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
		}
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		// Given
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
		List<Company> bean;
		// When
		bean = commpanyDAO.getAll(limit, offset, con);
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
			// When
			nbLines = commpanyDAO.countLines(con);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
	}
	
	@Test
	public void deleteCompanyShouldDeleteCompanyAndRelatedComputers() {
		// TODO écrire test
	}
}

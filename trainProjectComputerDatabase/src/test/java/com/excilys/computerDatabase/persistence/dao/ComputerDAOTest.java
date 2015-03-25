package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAOTest {
	private Connection con;
	private ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
	
	@Before
	public void prepareConnection() {
		con = ConnectionFactory.INSTANCE.getConnection();
	}
	
	@After
	public void closeConnection() {
		ConnectionFactory.INSTANCE.closeConnection(con);
	}
	
	@Test
	public void getByIdShouldReturnABean() throws SQLException {
		// Given
		Computer bean;
		Long id = new Long(10);
		Computer expectedBean;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id=?;";
		ResultSet results;
		
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, id);
		results = ps.executeQuery();
		if (results.next()) {
			expectedBean = ComputerMapper.INSTANCE.mapComputer(results);
			// When
			bean = computerDAO.getById(id, con);
			// Then
			Assert.assertNotNull("Erreur sur le bean.", bean);
			Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
		}
	}
	
	@Test
	public void getByNameShouldReturnABean() throws SQLException {
		// Given
		String computerName = "CM-2a";
		String companyName = "Apple Inc.";
		List<Computer> beans;
		List<Computer> beansByCompany;
		List<Computer> expectedBeansByComputerName = new ArrayList<>();
		List<Computer> expectedBeansByCompanyName = new ArrayList<>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id"
				+ "WHERE computer.name LIKE %?% "
				+ "OR company.name LIKE %?%;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, computerName);
		ps.setString(2, computerName);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByComputerName.add(ComputerMapper.INSTANCE.mapComputer(results));
		}
		ps.close();
		ps = con.prepareStatement(query);
		ps.setString(1, companyName);
		ps.setString(2, companyName);
		while (results.next()) {
			expectedBeansByCompanyName.add(ComputerMapper.INSTANCE.mapComputer(results));
		}
		// When
		beans = computerDAO.getByNameOrCompanyName(computerName, con);
		beansByCompany = computerDAO.getByNameOrCompanyName(companyName, con);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans par nom de computer.", expectedBeansByComputerName, beans);
		Assert.assertEquals("Erreur sur la liste de beans par nom de company.", expectedBeansByCompanyName, beansByCompany);
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		// Given
		List<Computer> expectedBeans = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ? OFFSET ?;";
		ResultSet results;

		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeans.add(ComputerMapper.INSTANCE.mapComputer(results));
		}
		List<Computer> bean;
		// When
		bean = computerDAO.getAll(limit, offset, con);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
		
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() throws SQLException {
		// Given
		int nbLines;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "SELECT COUNT(*) FROM computer;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			int expectedSize = results.getInt(1);
			// When
			nbLines = computerDAO.countLines(con);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
		
	}
	
	@Test
	public void createShouldAddABeanToTheDatabase() throws SQLException {
		// Given
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, connection));
		// When
		computerDAO.create(bean, connection);
		Computer expectedBean = computerDAO.getById(bean.getId(), connection);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() throws SQLException {
		// Given
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Long id = new Long(10);
		Computer bean;
		bean = computerDAO.getById(id, connection);
		Computer expectedBean = computerDAO.getById(id, connection);
		String name = "new " + bean.getName();
		bean.setName(name);
		expectedBean.setName(name);
		// When
		computerDAO.update(bean, connection);
		bean = computerDAO.getById(id, connection);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void deleteShouldRemoveABeanFromTheDatabase() throws SQLException {
		// Given
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, connection));
		computerDAO.create(bean, connection);
		// When
		computerDAO.delete(bean, connection);
		bean = computerDAO.getById(bean.getId(), connection);
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
}

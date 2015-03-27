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
	public void prepareConnection() throws SQLException {
		con = ConnectionFactory.INSTANCE.getConnection();
		con.setAutoCommit(false);
	}
	
	@After
	public void closeConnection() throws SQLException {
		con.rollback();
		con.setAutoCommit(true);
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
			ps.close();
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
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, computerName);
		ps.setString(2, computerName);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByComputerName.add(ComputerMapper.INSTANCE.mapComputer(results));
		}
		ps.close();
		ps = con.prepareStatement(query);
		ps.setString(1, "%" + companyName + "%");
		ps.setString(2, "%" + companyName + "%");
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByCompanyName.add(ComputerMapper.INSTANCE.mapComputer(results));
		}
		ps.close();
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
		List<Computer> bean;
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
		ps.close();
		// When
		bean = computerDAO.getAll(limit, offset, con);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
		
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() throws SQLException {
		// Given
		int nbLines;
		String query = "SELECT COUNT(*) FROM computer;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			int expectedSize = results.getInt(1);
			ps.close();
			// When
			nbLines = computerDAO.countLines(con);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
		
	}
	
	@Test
	public void createShouldAddABeanToTheDatabase() throws SQLException {
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, con));
		// When
		computerDAO.create(bean, con);
		Computer expectedBean = computerDAO.getById(bean.getId(), con);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() throws SQLException {
		// Given
		Long id = new Long(10);
		Computer bean;
		bean = computerDAO.getById(id, con);
		Computer expectedBean = computerDAO.getById(id, con);
		String name = "new " + bean.getName();
		bean.setName(name);
		expectedBean.setName(name);
		// When
		computerDAO.update(bean, con);
		bean = computerDAO.getById(id, con);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void deleteShouldRemoveABeanFromTheDatabase() throws SQLException {
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, con));
		computerDAO.create(bean, con);
		// When
		computerDAO.delete(bean, con);
		bean = computerDAO.getById(bean.getId(), con);
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
	
	@Test
	public void deleteByCompanyIdShouldDeleteMultipleBeans() throws SQLException {
		// Given
		int nbLines;
		Long companyId = new Long(1);
		// When
		computerDAO.deleteByCompanyId(companyId, con);
		// Then
		String query = "SELECT COUNT(*) FROM computer WHERE company_id=?;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, companyId);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			nbLines = results.getInt(1);
			ps.close();
			Assert.assertEquals("Erreur sur le nombre de lignes", 0, nbLines);
		}
	}
}

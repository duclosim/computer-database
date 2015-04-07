package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAOTest {
	private Connection con;
	
	@Autowired
	private CompanyDAO companyDAO;
	
	@Autowired
	private ComputerDAO computerDAO;
	
	@Autowired
	private ComputerMapper computerMapper;
	
	@Before
	public void prepareConnection() throws SQLException {
		con = ConnectionFactory.INSTANCE.getConnection();
		ConnectionFactory.INSTANCE.startTransaction();
	}
	
	@After
	public void closeConnection() throws SQLException {
		ConnectionFactory.INSTANCE.rollback();
		ConnectionFactory.INSTANCE.closeConnection();
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
			expectedBean = computerMapper.mapComputer(results);
			ps.close();
			// When
			bean = computerDAO.getById(id);
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
		int limit = 15;
		int offset = 5;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ? "
				+ "LIMIT ? OFFSET ?;";
		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setString(++paramIndex, "%" + computerName + "%");
		ps.setString(++paramIndex, "%" + computerName + "%");
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByComputerName.add(computerMapper.mapComputer(results));
		}
		ps.close();
		ps = con.prepareStatement(query);
		paramIndex = 0;
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByCompanyName.add(computerMapper.mapComputer(results));
		}
		ps.close();
		// When
		beans = computerDAO.getFiltered(limit, offset, computerName);
		beansByCompany = computerDAO.getFiltered(limit, offset, companyName);
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
			expectedBeans.add(computerMapper.mapComputer(results));
		}
		ps.close();
		// When
		bean = computerDAO.getAll(limit, offset);
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
			nbLines = computerDAO.countLines();
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
		bean.setCompany(companyDAO.getById(companyId));
		// When
		computerDAO.create(bean);
		Computer expectedBean = computerDAO.getById(bean.getId());
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() throws SQLException {
		// Given
		Long id = new Long(10);
		Computer bean;
		bean = computerDAO.getById(id);
		Computer expectedBean = computerDAO.getById(id);
		String name = "new " + bean.getName();
		bean.setName(name);
		expectedBean.setName(name);
		// When
		computerDAO.update(bean);
		bean = computerDAO.getById(id);
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
		bean.setCompany(companyDAO.getById(companyId));
		computerDAO.create(bean);
		// When
		computerDAO.delete(bean);
		bean = computerDAO.getById(bean.getId());
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
	
	@Test
	public void deleteByCompanyIdShouldDeleteMultipleBeans() throws SQLException {
		// Given
		int nbLines;
		Long companyId = new Long(1);
		// When
		computerDAO.deleteByCompanyId(companyId);
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

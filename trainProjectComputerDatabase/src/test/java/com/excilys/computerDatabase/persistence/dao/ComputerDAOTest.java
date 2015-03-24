package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CRUDDao;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

import org.junit.Assert;
import org.junit.Test;

public class ComputerDAOTest {
	
	@Test
	public void getByIdShouldReturnABean() {
		// Given
		Computer bean;
		Long id = new Long(10);
		Computer expectedBean;
		String query = "SELECT * FROM computer WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				expectedBean = ComputerMapper.INSTANCE.mapComputer(results);
				// When
				bean = ComputerDAOImpl.INSTANCE.getById(id, con);
				// Then
				Assert.assertNotNull("Erreur sur le bean.", bean);
				Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() {
		// Given
		List<Computer> expectedBeans = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM computer LIMIT ? OFFSET ?;";
		ResultSet results;

		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
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
			bean = ComputerDAOImpl.INSTANCE.getAll(limit, offset, con);
			// Then
			Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
		
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() {
		// Given
		int nbLines;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "SELECT COUNT(*) FROM computer;";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				int expectedSize = results.getInt(1);
				// When
				nbLines = ComputerDAOImpl.INSTANCE.countLines(con);
				// Then
				Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
		
	}
	
	@Test
	public void createShouldAddABeanToTheDatabase() {
		// Given
		CRUDDao<Computer> dao = ComputerDAOImpl.INSTANCE;
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		try {
			bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, connection));
			// When
			dao.create(bean, connection);
			Computer expectedBean = dao.getById(bean.getId(), connection);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() {
		// Given
		CRUDDao<Computer> dao = ComputerDAOImpl.INSTANCE;
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Long id = new Long(10);
		Computer bean;
		try {
			bean = dao.getById(id, connection);
			Computer expectedBean = dao.getById(id, connection);
			String name = "new " + bean.getName();
			bean.setName(name);
			expectedBean.setName(name);
			// When
			dao.update(bean, connection);
			bean = dao.getById(id, connection);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
	}
	
	@Test
	public void deleteShouldRemoveABeanFromTheDatabase() {
		// Given
		CRUDDao<Computer> dao = ComputerDAOImpl.INSTANCE;
		Connection connection = ConnectionFactory.INSTANCE.getConnection();
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		try {
			bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId, connection));
			dao.create(bean, connection);
			// When
			dao.delete(bean, connection);
			bean = dao.getById(bean.getId(), connection);
			// Then
			Assert.assertNull("Erreur sur le bean", bean);
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		
	}

}

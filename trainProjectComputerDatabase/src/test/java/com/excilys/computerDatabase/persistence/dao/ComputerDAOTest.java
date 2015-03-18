package test.java.com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.dao.CRUDDao;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

import org.junit.Assert;
import org.junit.Test;

public class ComputerDAOTest {
	
	@Test
	public void getByIdShouldReturnABean() {
		// Given
		ComputerBean bean;
		Long id = new Long(10);
		ComputerBean expectedBean;
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
				bean = ComputerDAOImpl.INSTANCE.getById(id);
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
		List<ComputerBean> expectedBeans = new ArrayList<>();
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
			List<ComputerBean> bean;
			// When
			bean = ComputerDAOImpl.INSTANCE.getAll(limit, offset);
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
				nbLines = ComputerDAOImpl.INSTANCE.countLines();
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
		CRUDDao<ComputerBean> dao = ComputerDAOImpl.INSTANCE;
		ComputerBean bean = new ComputerBean();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId));
		// When
		dao.create(bean);
		ComputerBean expectedBean = dao.getById(bean.getId());
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() {
		// Given
		CRUDDao<ComputerBean> dao = ComputerDAOImpl.INSTANCE;
		Long id = new Long(10);
		ComputerBean bean = dao.getById(id);
		ComputerBean expectedBean = dao.getById(id);
		String name = "new " + bean.getName();
		bean.setName(name);
		expectedBean.setName(name);
		// When
		dao.update(bean);
		bean = dao.getById(id);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void deleteShouldRemoveABeanFromTheDatabase() {
		// Given
		CRUDDao<ComputerBean> dao = ComputerDAOImpl.INSTANCE;
		ComputerBean bean = new ComputerBean();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		bean.setCompany(CompanyDAOImpl.INSTANCE.getById(companyId));
		dao.create(bean);
		// When
		dao.delete(bean);
		bean = dao.getById(bean.getId());
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
		
	}

}

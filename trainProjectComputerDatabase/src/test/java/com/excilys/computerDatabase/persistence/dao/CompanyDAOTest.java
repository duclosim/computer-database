package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

import org.junit.Assert;
import org.junit.Test;

public class CompanyDAOTest {
	
	@Test
	public void getByIdShouldReturnABean() {
		// Given
		Company bean;
		Long id = new Long(10);
		Company expectedBean;
		String query = "SELECT * FROM company WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				expectedBean = CompanyMapper.INSTANCE.mapCompany(results);
				// When
				bean = CompanyDAOImpl.INSTANCE.getById(id, con);
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
		List<Company> expectedBeans = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		ResultSet results;

		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				expectedBeans.add(CompanyMapper.INSTANCE.mapCompany(results));
			}
			List<Company> bean;
			// When
			bean = CompanyDAOImpl.INSTANCE.getAll(limit, offset, con);
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
		String query = "SELECT COUNT(*) FROM company;";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				int expectedSize = results.getInt(1);
				// When
				nbLines = CompanyDAOImpl.INSTANCE.countLines(con);
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
}

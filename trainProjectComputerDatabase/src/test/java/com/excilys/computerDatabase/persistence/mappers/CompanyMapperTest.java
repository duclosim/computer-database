package com.excilys.computerDatabase.persistence.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@ActiveProfiles("DEV")
public class CompanyMapperTest {
	
	@Autowired
	CompanyMapper companyMapper;
	@Autowired
	ConnectionFactory connectionFactory;

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void mapCompanyShouldReturnNullObjectBecauseOfNullResultSet() {
		// Given
		ResultSet resultSet = null;
		// When
		Company res = companyMapper.mapRow(resultSet, 0);
		// Then
		Assert.assertNull("Bean n'est pas null.", res);
	}

	@Test
	public void mapCompanyShouldSetProperties() throws SQLException {
		// Given
		Company result = null;
		Long id = new Long(1);
		String query = "SELECT * FROM company WHERE id=" + id + ";";
		ResultSet results;
		Connection con = connectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			results = ps.executeQuery();
			if (results.next()) {
				Company expectedBean = new Company(results.getLong(CompanyDAOImpl.ID_COLUMN_LABEL),
						results.getString(CompanyDAOImpl.NAME_COLUMN_LABEL));
				// When
				result = companyMapper.mapRow(results, 0);
				results.close();
				ps.close();
				// Then
				Assert.assertEquals("Erreur sur le bean.", expectedBean, result);
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			con.close();
		}
	}
}

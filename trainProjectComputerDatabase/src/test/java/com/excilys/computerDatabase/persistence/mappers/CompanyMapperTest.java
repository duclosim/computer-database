package com.excilys.computerDatabase.persistence.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CompanyMapperTest {
	
	CompanyMapper companyMapper = CompanyMapper.INSTANCE;

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void mapCompanyShouldThrowAnIllegalArgumentExceptionBecauseOfNullResultSet() {
		// Given
		ResultSet resultSet = null;
		thrown.expect(IllegalArgumentException.class);
		// When
		companyMapper.mapCompany(resultSet);
		// Then
	}

	@Test
	public void mapCompanyShouldSetProperties() {
		// Given
		Company result = null;
		Long id = new Long(1);
		String query = "SELECT * FROM company WHERE id=" + id + ";";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			results = ps.executeQuery();
			if (results.next()) {
				Company expectedBean = new Company(results.getLong(CompanyDAOImpl.ID_COLUMN_LABEL),
						results.getString(CompanyDAOImpl.NAME_COLUMN_LABEL));
				// When
				result = companyMapper.mapCompany(results);
				// Then
				Assert.assertEquals("Erreur sur le bean.", expectedBean, result);
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(con);
		}
	}
}

package test.java.com.excilys.computerDatabase.persistence.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ComputerMapperTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void mapComputerShouldThrowAnIllegalArgumentExceptionBecauseOfNullResultSet() {
		// Given
		ResultSet resultSet = null;
		thrown.expect(IllegalArgumentException.class);
		// When
		ComputerMapper.INSTANCE.mapComputer(resultSet);
		// Then
	}

	@Test
	public void mapComputerShouldSetProperties() {
		// Given
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		ComputerBean result = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		CompanyBean company = null;
		
		Long id = new Long(400);
		String query = "SELECT * FROM computer WHERE id=" + id + ";";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			results = ps.executeQuery();
			if (results.next()) {
				if (results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL) != null) {
					introducedDate = results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL) != null) {
					discontinuedDate = results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL) != 0) {
					company = companyDAO.getById(results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL));
				}
				ComputerBean expectedBean = new ComputerBean(results.getLong(ComputerDAOImpl.ID_COLUMN_LABEL), 
						results.getString(ComputerDAOImpl.NAME_COLUMN_LABEL), 
						introducedDate,
						discontinuedDate,
						company);
				// When
				result = ComputerMapper.INSTANCE.mapComputer(results);
				// Then
				Assert.assertEquals("Erreur sur le bean",  expectedBean, result);
			}
		} catch (SQLException e) {
			System.err.println("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}

}

package com.excilys.computerDatabase.persistence.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.service.CompanyService;

public class ComputerMapperTest {
	@Autowired
	private ComputerMapper computerMapper;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ConnectionFactory connectionFactory;

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void mapComputerShouldThrowAnIllegalArgumentExceptionBecauseOfNullResultSet() {
		// Given
		ResultSet resultSet = null;
		thrown.expect(IllegalArgumentException.class);
		// When
		computerMapper.mapComputer(resultSet);
		// Then
	}

	@Test
	public void mapComputerShouldSetProperties() throws SQLException {
		// Given
		Computer result = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		Company company = null;
		
		Long id = new Long(400);
		String query = "SELECT * FROM computer WHERE id=" + id + ";";
		ResultSet results = null;
		PreparedStatement ps = null;
		Connection connection = connectionFactory.getConnection();
		try {
			ps = connection.prepareStatement(query);
			results = ps.executeQuery();
			if (results.next()) {
				Timestamp ts = results.getTimestamp(ComputerColumn.INTRODUCED_COLUMN_LABEL.getColumnName());
				if (ts != null) {
					introducedDate = ts.toLocalDateTime();
				}
				ts = results.getTimestamp(ComputerColumn.DISCONTINUED_COLUMN_LABEL.getColumnName());
				if (ts != null) {
					discontinuedDate = ts.toLocalDateTime();
				}
				Long l = results.getLong(ComputerColumn.COMPANY_ID_COLUMN_LABEL.getColumnName());
				if (l != 0) {
					company = companyService.getById(l);
				}
				Computer expectedBean = new Computer(results.getLong(ComputerColumn.ID_COLUMN_LABEL.getColumnName()), 
						results.getString(ComputerColumn.NAME_COLUMN_LABEL.getColumnName()), 
						introducedDate,
						discontinuedDate,
						company);
				// When
				result = computerMapper.mapComputer(results);
				ps.close();
				// Then
				Assert.assertEquals("Erreur sur le bean",  expectedBean, result);
			}
		} catch (SQLException e) {
			System.err.println("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		} finally {
			results.close();
			ps.close();
			connection.close();
		}
	}

}

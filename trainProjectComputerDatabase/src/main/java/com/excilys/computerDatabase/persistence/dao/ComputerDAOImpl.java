package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum ComputerDAOImpl implements ComputerDAO {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	public static final String ID_COLUMN_LABEL = "computer.id";
	public static final String NAME_COLUMN_LABEL = "computer.name";
	public static final String INTRODUCED_COLUMN_LABEL = "computer.introduced";
	public static final String DISCONTINUED_COLUMN_LABEL = "computer.discontinued";
	public static final String COMPANY_ID_COLUMN_LABEL = "computer.company_id";
	public static final String COMPANY_NAME_COLUMN_LABEL = "company.name";
	
	private ComputerMapper mapper;
	
	private ComputerDAOImpl() {
		mapper = ComputerMapper.INSTANCE;
	}
	
	public Computer getById(Long id, Connection con) throws SQLException {
		LOG.trace("getById(" + id + ")");
		Computer result = null;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id=?;";
		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, id);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			result = mapper.mapComputer(results);
		}
		ps.close();
		return result;
	}

	@Override
	public List<Computer> getByNameOrCompanyName(String name, Connection con)
			throws SQLException {
		LOG.trace("getByNameOrCompanyName(" + name + ")");
		List<Computer> result = new ArrayList<Computer>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE %?%"
				+ "OR company.name LIKE %?%;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, name);
		ps.setString(2, name);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapComputer(results));
		}
		ps.close();
		return result;
	}

	public List<Computer> getAll(int limit, int offset, Connection con) throws SQLException {
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul.");
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			LOG.error("offset est négatif.");
			throw new IllegalArgumentException("offset est négatif.");
		}
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		List<Computer> result = new ArrayList<Computer>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ? OFFSET ?;";
		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapComputer(results));
		}
		ps.close();
		return result;
	}

	@Override
	public int countLines(Connection con) throws SQLException {
		LOG.trace("countLine()");
		String query = "SELECT COUNT(*) FROM computer;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		}
		ps.close();
		return 0;
	}

	public void create(Computer computer, Connection con) throws SQLException {
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		LOG.trace("create(" + computer + ")");
		String query = new StringBuilder("INSERT INTO computer (")
		.append(NAME_COLUMN_LABEL)
		.append(", ")
		.append(INTRODUCED_COLUMN_LABEL)
		.append(", ")
		.append(DISCONTINUED_COLUMN_LABEL)
		.append(", ")
		.append(COMPANY_ID_COLUMN_LABEL)
		.append(") VALUES (?, ?, ?, ?);")
		.toString();
		PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int colNb = 0;
		Timestamp introduced = null;
		Timestamp discontinued = null;
		if (computer.getIntroducedDate() != null) {
			introduced = Timestamp.valueOf(computer.getIntroducedDate());
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinued = Timestamp.valueOf(computer.getDiscontinuedDate());
		}
		ps.setString(++colNb, computer.getName());
		ps.setTimestamp(++colNb, introduced);
		ps.setTimestamp(++colNb, discontinued);
		ps.setLong(++colNb, computer.getCompany().getId());
		ps.executeUpdate();
		ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
        	computer.setId(generatedKeys.getLong(1));
        } else {
        	throw new SQLException();
        }
        ps.close();
	}

	public void update(Computer computer, Connection con) throws SQLException {
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		if (computer.getName() == null) {
			LOG.error("computerName est à null.");
			throw new IllegalArgumentException("computerName est à null.");
		}
		LOG.trace("update(" + computer + ")");
		StringBuilder query = new StringBuilder("UPDATE computer SET ")
		.append(NAME_COLUMN_LABEL)
		.append("=?, ")
		.append(INTRODUCED_COLUMN_LABEL)
		.append("=?, ")
		.append(DISCONTINUED_COLUMN_LABEL)
		.append("=?");
		if (computer.getCompany() != null) {
			query.append(", ")
			.append(COMPANY_ID_COLUMN_LABEL)
			.append("=?");
		}
		query.append(" WHERE id = ?;");
		PreparedStatement ps = con.prepareStatement(query.toString());
		int colNb = 0;
		Timestamp introduced = null;
		Timestamp discontinued = null;
		if (computer.getIntroducedDate() != null) {
			introduced = Timestamp.valueOf(computer.getIntroducedDate());
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinued = Timestamp.valueOf(computer.getDiscontinuedDate());
		}
		// name
		ps.setString(++colNb, computer.getName());
		// introduced date
		ps.setTimestamp(++colNb, introduced);
		// discontinued date
		ps.setTimestamp(++colNb, discontinued);
		// company id
		if (computer.getCompany() != null) {
			ps.setLong(++colNb, computer.getCompany().getId());
		}
		// id
		ps.setLong(++colNb, computer.getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Computer computer, Connection con) throws SQLException {
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		LOG.trace("delete(" + computer + ")");
		String query = "DELETE FROM computer WHERE id=?";
		PreparedStatement ps = con.prepareStatement(query);
		if (computer.getId() != null) {
			ps.setLong(1, computer.getId());
			ps.executeUpdate();
		}
		ps.close();
	}

	@Override
	public void deleteByCompanyId(Long companyId, Connection con) throws SQLException {
		String deleteComputersQuery = "DELETE FROM computer WHERE company_id=?";
		PreparedStatement delComputersStatement = con.prepareStatement(deleteComputersQuery);
		if (companyId != null) {
			delComputersStatement.setLong(1, companyId);
			delComputersStatement.executeUpdate();
		}
		delComputersStatement.close();
	}
}

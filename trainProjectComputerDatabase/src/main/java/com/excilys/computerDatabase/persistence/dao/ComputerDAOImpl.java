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
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum ComputerDAOImpl implements ComputerDAO {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	private final ComputerMapper mapper;
	
	private ComputerDAOImpl() {
		mapper = ComputerMapper.INSTANCE;
	}
	
	@Override
	public Computer getById(Long id) throws SQLException {
		LOG.trace("getById(" + id + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
		releaseResources(con, ps);
		return result;
	}

	@Override
	public List<Computer> getAll(int limit, int offset) throws SQLException {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul.");
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			LOG.error("offset est négatif.");
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		List<Computer> result = new ArrayList<>();
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
		releaseResources(con, ps);
		return result;
	}

	@Override
	public List<Computer> getFiltered(String name)
			throws SQLException {
		LOG.trace("getByNameOrCompanyName(" + name + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		List<Computer> result = new ArrayList<>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, "%" + name + "%");
		ps.setString(2, "%" + name + "%");
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapComputer(results));
		}
		releaseResources(con, ps);
		return result;
	}
	
	@Override
	public List<Computer> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) throws SQLException {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul.");
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			LOG.error("offset est négatif.");
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		List<Computer> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ");
		if (column != null && way != null) {
			query.append("ORDER BY ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(" ");
		}
		query.append("LIMIT ? OFFSET ?;");
		PreparedStatement ps = con.prepareStatement(query.toString());
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapComputer(results));
		}
		releaseResources(con, ps);
		return result;
	}

	@Override
	public List<Computer> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way)
			throws SQLException {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul.");
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			LOG.error("offset est négatif.");
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		int paramIndex = 0;
		List<Computer> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ");
		if (name != null) {
			query.append("WHERE computer.name LIKE ? ")
				.append("OR company.name LIKE ? ");
		}
		if (column != null && way != null) {
			query.append("ORDER BY ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(" ");
		}
		query.append("LIMIT ? OFFSET ?;");
		PreparedStatement ps = con.prepareStatement(query.toString());
		if (column != null && way != null) {
			ps.setString(++paramIndex, "%" + column + "%");
			ps.setString(++paramIndex, "%" + column + "%");
		}
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapComputer(results));
		}
		releaseResources(con, ps);
		return result;
	}

	@Override
	public int countLines() throws SQLException {
		LOG.trace("countLine()");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "SELECT COUNT(*) FROM computer;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		}
		releaseResources(con, ps);
		return 0;
	}

	@Override
	public void create(Computer computer) throws SQLException {
		LOG.trace("create(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		StringBuilder query = new StringBuilder("INSERT INTO computer (")
			.append(ComputerColumn.NAME_COLUMN_LABEL.getColumnName())
			.append(", ")
			.append(ComputerColumn.INTRODUCED_COLUMN_LABEL.getColumnName())
			.append(", ")
			.append(ComputerColumn.DISCONTINUED_COLUMN_LABEL.getColumnName());
		if (computer.getCompany() != null) {
			query.append(", ")
				.append(ComputerColumn.COMPANY_ID_COLUMN_LABEL.getColumnName());
		}
		query.append(") VALUES (?, ?, ?");
		if (computer.getCompany() != null) {
			query.append(", ?");
		}
		query.append(");");
		PreparedStatement ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
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
		if (computer.getCompany() != null) {
			ps.setLong(++colNb, computer.getCompany().getId());
		}
		ps.executeUpdate();
		ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
        	computer.setId(generatedKeys.getLong(1));
        } else {
        	throw new SQLException();
        }
		releaseResources(con, ps);
	}

	@Override
	public void update(Computer computer) throws SQLException {
		LOG.trace("update(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		if (computer.getName() == null) {
			LOG.error("computerName est à null.");
			throw new IllegalArgumentException("computerName est à null.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		StringBuilder query = new StringBuilder("UPDATE computer SET ")
		.append(ComputerColumn.NAME_COLUMN_LABEL.getColumnName())
		.append("=?, ")
		.append(ComputerColumn.INTRODUCED_COLUMN_LABEL.getColumnName())
		.append("=?, ")
		.append(ComputerColumn.DISCONTINUED_COLUMN_LABEL.getColumnName())
		.append("=?");
		if (computer.getCompany() != null) {
			query.append(", ")
			.append(ComputerColumn.COMPANY_ID_COLUMN_LABEL.getColumnName())
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
		releaseResources(con, ps);
	}

	@Override
	public void delete(Computer computer) throws SQLException {
		LOG.trace("delete(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "DELETE FROM computer WHERE id=?";
		PreparedStatement ps = con.prepareStatement(query);
		if (computer.getId() != null) {
			ps.setLong(1, computer.getId());
			ps.executeUpdate();
		}
		releaseResources(con, ps);
	}

	@Override
	public void deleteByCompanyId(Long companyId) throws SQLException {
		LOG.trace("deleteByCompanyId(" + companyId + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String deleteComputersQuery = "DELETE FROM computer WHERE company_id=?";
		PreparedStatement delComputersStatement = con.prepareStatement(deleteComputersQuery);
		if (companyId != null) {
			delComputersStatement.setLong(1, companyId);
			delComputersStatement.executeUpdate();
		}
		releaseResources(con, delComputersStatement);
	}
	
	private void releaseResources(Connection connection, 
			PreparedStatement ps) throws SQLException {
		ps.close();
		if (connection.getAutoCommit()) {
			ConnectionFactory.INSTANCE.closeConnection();
		}
	}
}

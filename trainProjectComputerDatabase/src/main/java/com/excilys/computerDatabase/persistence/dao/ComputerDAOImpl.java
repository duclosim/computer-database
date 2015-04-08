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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
@Repository
public class ComputerDAOImpl implements ComputerDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	@Autowired
	private ComputerMapper mapper;
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Override
	public Computer getById(Long id) throws SQLException  {
		LOG.info("getById(" + id + ")");
		Connection con = connectionFactory.getConnection();
		Computer result = null;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id=?;";
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, id);
			results = ps.executeQuery();
			if (results.next()) {
				result = mapper.mapComputer(results);
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return result;
	}

	@Override
	public List<Computer> getAll(int limit, int offset) 
			throws SQLException  {
		LOG.info(new StringBuilder("getAll(")
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
		Connection con = connectionFactory.getConnection();
		List<Computer> result = new ArrayList<>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ? OFFSET ?;";
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(mapper.mapComputer(results));
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return result;
	}

	@Override
	public List<Computer> getFiltered(int limit, int offset, String name) 
			throws SQLException {
		LOG.info(new StringBuilder("getByNameOrCompanyName(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(")").toString());
		Connection con = connectionFactory.getConnection();
		List<Computer> result = new ArrayList<>();
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ? "
				+ "LIMIT ? OFFSET ?;";
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setString(++paramIndex, "%" + name + "%");
			ps.setString(++paramIndex, "%" + name + "%");
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(mapper.mapComputer(results));
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return result;
	}
	
	@Override
	public List<Computer> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) throws SQLException  {
		LOG.info(new StringBuilder("getAll(")
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
		Connection con = connectionFactory.getConnection();
		List<Computer> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ");
		if (column != null && way != null) {
			query.append("ORDER BY ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(" ");
		}
		query.append("LIMIT ? OFFSET ?;");
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query.toString());
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(mapper.mapComputer(results));
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return result;
	}

	@Override
	public List<Computer> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way) 
				throws SQLException {
		LOG.info(new StringBuilder("getAll(")
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
		Connection con = connectionFactory.getConnection();
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
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query.toString());
			if (name != null) {
				ps.setString(++paramIndex, "%" + name + "%");
				ps.setString(++paramIndex, "%" + name + "%");
			}
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(mapper.mapComputer(results));
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return result;
	}

	@Override
	public int countLines() throws SQLException {
		LOG.info("countLine()");
		Connection con = connectionFactory.getConnection();
		String query = "SELECT COUNT(*) FROM computer;";
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query);
			results = ps.executeQuery();
			if (results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return 0;
	}

	@Override
	public int countFilteredLines(String name) throws SQLException {
		LOG.info("countFilteredLines(" + name + ")");
		Connection con = connectionFactory.getConnection();
		String query = "SELECT COUNT(*) "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?;";
		PreparedStatement ps = null;
		ResultSet results = null;
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, "%" + name + "%");
			ps.setString(2, "%" + name + "%");
			results = ps.executeQuery();
			if (results.next()) {
				return results.getInt(1);
			}
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			releaseResources(ps, results);
		}
		return 0;
	}

	@Override
	public void create(Computer computer) throws SQLException {
		LOG.info("create(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection con = connectionFactory.getConnection();
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
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		try {
			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
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
			generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys.next()) {
	        	computer.setId(generatedKeys.getLong(1));
	        } else {
	        	throw new SQLException();
	        }
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			releaseResources(ps, generatedKeys);
		}
	}

	@Override
	public void update(Computer computer) throws SQLException {
		LOG.info("update(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		if (computer.getName() == null) {
			LOG.error("computerName est à null.");
			throw new IllegalArgumentException("computerName est à null.");
		}
		Connection con = connectionFactory.getConnection();
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
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query.toString());
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
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			releaseResources(ps, null);
		}
	}

	@Override
	public void delete(Computer computer) throws SQLException {
		LOG.info("delete(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection con = connectionFactory.getConnection();
		String query = "DELETE FROM computer WHERE id=?";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query);
			if (computer.getId() != null) {
				ps.setLong(1, computer.getId());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			releaseResources(ps, null);
		}
	}

	@Override
	public void deleteByCompanyId(Long companyId) throws SQLException {
		LOG.info("deleteByCompanyId(" + companyId + ")");
		Connection con = connectionFactory.getConnection();
		String deleteComputersQuery = "DELETE FROM computer WHERE company_id=?";
		PreparedStatement delComputersStatement = null;
		try {
			delComputersStatement = con.prepareStatement(deleteComputersQuery);
			if (companyId != null) {
				delComputersStatement.setLong(1, companyId);
				delComputersStatement.executeUpdate();
			}
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			releaseResources(delComputersStatement, null);
		}
	}
	
	private void releaseResources(PreparedStatement ps,
			ResultSet results) throws SQLException {
		if (results != null) {
			results.close();
		}
		if (ps != null) {
			ps.close();
		}
		Connection con = connectionFactory.getConnection();
		if (con != null && con.getAutoCommit()) {
			con.close();
		}
	}
}

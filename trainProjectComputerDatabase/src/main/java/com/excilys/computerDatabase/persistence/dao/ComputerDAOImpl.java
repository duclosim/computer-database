package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
@Repository
public class ComputerDAOImpl implements ComputerDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	private static final String GET_BY_ID = "SELECT * FROM computer "
			+ "LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.id=?;";
	private static final String GET_ALL = "SELECT * "
			+ "FROM computer "
			+ "LEFT JOIN company ON computer.company_id = company.id "
			+ "LIMIT ? OFFSET ?;";
	private static final String GET_FILTERED = "SELECT * "
			+ "FROM computer "
			+ "LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? "
			+ "OR company.name LIKE ? "
			+ "LIMIT ? OFFSET ?;";
	private static final String COUNT = "SELECT COUNT(*) FROM computer;";
	private static final String COUNT_FILTERED = "SELECT COUNT(*) "
			+ "FROM computer "
			+ "LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? "
			+ "OR company.name LIKE ?;";
	private static final String DELETE  = "DELETE FROM computer WHERE id=?";
	private static final String DELETE_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id=?";
	
	@Autowired
	private ComputerMapper mapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Computer getById(Long id) {
		LOG.info("getById(" + id + ")");
		List<Computer> result = jdbcTemplate.query(GET_BY_ID, new Object[]{id}, mapper);
		if (result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
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
		return jdbcTemplate.query(GET_ALL, new Object[]{limit, offset}, mapper);
	}

	@Override
	public List<Computer> getFiltered(int limit, int offset, String name) {
		LOG.info(new StringBuilder("getByNameOrCompanyName(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(")").toString());
		return jdbcTemplate.query(GET_FILTERED, new Object[]{
			new StringBuilder("%").append(name).append("%").toString(), 
			new StringBuilder("%").append(name).append("%").toString(),
			limit, offset},
			mapper);
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
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ");
		if (column != null && way != null) {
			query.append("ORDER BY ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(", ")
				.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" ASC ");
		}
		query.append("LIMIT ? OFFSET ?;");
		return jdbcTemplate.query(query.toString(), new Object[]{limit, offset}, mapper);
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
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ");
		if (name != null) {
			query.append("WHERE computer.name LIKE ? ")
				.append("OR company.name LIKE ? ");
		}
		if (column != null && way != null) {
			query.append("ORDER BY ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(", ")
				.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" ASC ");
		}
		query.append("LIMIT ? OFFSET ?;");
		return jdbcTemplate.query(query.toString(), new Object[]{
			new StringBuilder("%").append(name).append("%").toString(), 
			new StringBuilder("%").append(name).append("%").toString(),
			limit, offset}, mapper);
	}

	@Override
	public int countLines() {
		LOG.info("countLine()");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public int countFilteredLines(String name) {
		LOG.info("countFilteredLines(" + name + ")");
		String usableName = new StringBuilder("%").append(name).append("%").toString();
		return jdbcTemplate.queryForObject(COUNT_FILTERED, new Object[]{usableName, usableName}, Integer.class);
	}

	@Override
	public void create(Computer computer) {
		LOG.info("create(" + computer + ")");         
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
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
		final Timestamp introduced;
		final Timestamp discontinued;
		if (computer.getIntroducedDate() != null) {
			introduced = Timestamp.valueOf(computer.getIntroducedDate());
		} else {
			introduced = null;
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinued = Timestamp.valueOf(computer.getDiscontinuedDate());
		} else {
			discontinued = null;
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps =
							connection.prepareStatement(query.toString(), 
									Statement.RETURN_GENERATED_KEYS);
					int paramIndex = 0;
					ps.setString(++paramIndex, computer.getName());
					ps.setTimestamp(++paramIndex, introduced);
					ps.setTimestamp(++paramIndex, discontinued);
					if (computer.getCompany() != null) {
						ps.setLong(++paramIndex, computer.getCompany().getId());
					}
					return ps;
				}
			},
			keyHolder);
		computer.setId(keyHolder.getKey().longValue());
	}

	@Override
	public void update(Computer computer) {
		LOG.info("update(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		if (computer.getName() == null) {
			LOG.error("computerName est à null.");
			throw new IllegalArgumentException("computerName est à null.");
		}
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
		Timestamp introduced = null;
		Timestamp discontinued = null;
		if (computer.getIntroducedDate() != null) {
			introduced = Timestamp.valueOf(computer.getIntroducedDate());
		}
		if (computer.getDiscontinuedDate() != null) {
			discontinued = Timestamp.valueOf(computer.getDiscontinuedDate());
		}
		List<Object> params = new ArrayList<>();
		// name
		params.add(computer.getName());
		// introduced date
		params.add(introduced);
		// discontinued date
		params.add(discontinued);
		// company id
		if (computer.getCompany() != null) {
			params.add(computer.getCompany().getId());
		}
		// id
		params.add(computer.getId());
		jdbcTemplate.update(query.toString(), params.toArray());
	}

	@Override
	public void delete(Computer computer) throws SQLException {
		LOG.info("delete(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		jdbcTemplate.update(DELETE, computer.getId());
	}

	@Override
	public void deleteByCompanyId(Long companyId) throws SQLException {
		LOG.info("deleteByCompanyId(" + companyId + ")");
		jdbcTemplate.update(DELETE_BY_COMPANY_ID, companyId);
	}
}

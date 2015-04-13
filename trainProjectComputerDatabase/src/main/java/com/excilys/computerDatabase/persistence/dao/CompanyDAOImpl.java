package com.excilys.computerDatabase.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAOImpl.class);
	
	private static final String GET_BY_ID = "SELECT * FROM company WHERE id=?;";
	private static final String GET_ALL = "SELECT * FROM company;";
	private static final String GET_ALL_WITH_OFFSET = "SELECT * FROM company LIMIT ? OFFSET ?;";
	private static final String COUNT = "SELECT COUNT(*) FROM company;";
	private static final String DELETE  = "DELETE FROM company WHERE id=?";
	
	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Company getById(Long id) {
		LOG.info("getById(" + id + ")");
		List<Company> result = jdbcTemplate.query(GET_BY_ID, new Object[]{id}, mapper);
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	public List<Company> getAll(int limit, int offset) throws SQLException  {
		LOG.info("getAll(" + limit + ", " + offset + ")");
		return jdbcTemplate.query(GET_ALL_WITH_OFFSET, new Object[]{limit, offset}, mapper);
	}
	
	@Override
	public List<Company> getAll() throws SQLException  {
		LOG.info("getAll()");
		return jdbcTemplate.query(GET_ALL, mapper);
	}

	@Override
	public List<Company> getFiltered(int limit, int offset, String name)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> getOrdered(int limit, int offset,
			ComputerColumn column, OrderingWay way) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int countLines() throws SQLException  {
		LOG.info("countLine()");
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	@Override
	public void create(Company company) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(Company company) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void delete(Company company) throws SQLException {
		LOG.info("delete(" + company + ")");
		if (company == null) {
			LOG.error("company est à null.");
			throw new IllegalArgumentException("company est à null.");
		}
		jdbcTemplate.update(DELETE, company.getId());
	}
}

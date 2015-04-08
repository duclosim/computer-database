package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAOImpl.class);
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";

	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Override
	public Company getById(Long id) throws SQLException {
		LOG.info("getById(" + id + ")");
		Connection con = connectionFactory.getConnection();
		Company result = null;
		String query = "SELECT * FROM company WHERE id=?;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, id);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			result = mapper.mapCompany(results);
		}
		releaseResources(con, ps);
		return result;
	}
	
	@Override
	public List<Company> getAll(int limit, int offset) throws SQLException {
		LOG.info("getAll(" + limit + ", " + offset + ")");
		Connection con = connectionFactory.getConnection();
		List<Company> result = new ArrayList<>();
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapCompany(results));
		}
		releaseResources(con, ps);
		return result;
	}
	
	@Override
	public List<Company> getAll() throws SQLException {
		LOG.info("getAll()");
		Connection con = connectionFactory.getConnection();
		List<Company> result = new ArrayList<>();
		String query = "SELECT * FROM company;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(mapper.mapCompany(results));
		}
		releaseResources(con, ps);
		return result;
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
	public int countLines() throws SQLException {
		LOG.info("countLine()");
		Connection con = connectionFactory.getConnection();
		String query = "SELECT COUNT(*) FROM company;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		}
		releaseResources(con, ps);
		return 0;
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
		Connection con = connectionFactory.getConnection();
		String deleteCompanyQuery = "DELETE FROM company WHERE id=?";
		PreparedStatement delCompaniesStatement = con.prepareStatement(deleteCompanyQuery);
		delCompaniesStatement.setLong(1, company.getId());
		delCompaniesStatement.executeUpdate();
		releaseResources(con, delCompaniesStatement);
	}
	
	private void releaseResources(Connection connection, 
			PreparedStatement ps) throws SQLException {
		ps.close();
		if (connection.getAutoCommit()) {
			connection.close();
		}
	}
}

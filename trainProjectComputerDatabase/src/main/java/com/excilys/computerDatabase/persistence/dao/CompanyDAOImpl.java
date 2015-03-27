package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum CompanyDAOImpl implements CompanyDAO {
	INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAOImpl.class);
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";

	private final CompanyMapper mapper;
	
	private CompanyDAOImpl() {
		mapper = CompanyMapper.INSTANCE;
	}
	
	@Override
	public Company getById(Long id) throws SQLException {
		LOG.trace("getById(" + id + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
		LOG.trace("getAll()");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
	public List<Company> getFiltered(String name) throws SQLException {
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
		LOG.trace("countLine()");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
		LOG.trace("delete(" + company + ")");
		if (company == null) {
			LOG.error("company est à null.");
			throw new IllegalArgumentException("company est à null.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
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
			ConnectionFactory.INSTANCE.closeConnection();
		}
	}
}

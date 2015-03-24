package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum CompanyDAOImpl implements CompanyDAO {
	INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";

	@Override
	public Company getById(Long id, Connection con) throws SQLException {
		LOG.trace("getById(" + id + ")");
		Company result = null;
		String query = "SELECT * FROM company WHERE id=?;";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setLong(1, id);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			result = CompanyMapper.INSTANCE.mapCompany(results);
		}
		ps.close();
		return result;
	}
	
	@Override
	public List<Company> getAll(int limit, int offset, Connection con) throws SQLException {
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		List<Company> result = new ArrayList<Company>();
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		PreparedStatement ps = con.prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(CompanyMapper.INSTANCE.mapCompany(results));
		}
		ps.close();
		return result;
	}
	
	@Override
	public List<Company> getAll(Connection con) throws SQLException {
		LOG.trace("getAll()");
		List<Company> result = new ArrayList<Company>();
		String query = "SELECT * FROM company;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		while (results.next()) {
			result.add(CompanyMapper.INSTANCE.mapCompany(results));
		}
		ps.close();
		return result;
	}

	@Override
	public int countLines(Connection con) throws SQLException {
		LOG.trace("countLine()");
		String query = "SELECT COUNT(*) FROM company;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet results = ps.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		}
		ps.close();
		return 0;
	}

	@Override
	public void create(Company entity, Connection con) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(Company entity, Connection con) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void delete(Company entity, Connection con) throws SQLException {
		LOG.trace("delete(" + entity + ")");
		String deleteCompanyQuery = "DELETE FROM company WHERE id=?";
		PreparedStatement delCompaniesStatement = con.prepareStatement(deleteCompanyQuery);
		delCompaniesStatement.setLong(1, entity.getId());
		delCompaniesStatement.executeUpdate();
		delCompaniesStatement.close();
	}
}

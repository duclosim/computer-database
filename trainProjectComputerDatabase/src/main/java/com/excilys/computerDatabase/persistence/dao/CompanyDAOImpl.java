package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.CompanyBean;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
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

	public CompanyBean getById(Long id) {
		LOG.trace("getById(" + id + ")");
		CompanyBean result = null;
		String query = "SELECT * FROM company WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				result = CompanyMapper.INSTANCE.mapCompany(results);
			}
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
	
	public List<CompanyBean> getAll(int limit, int offset) {
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		List<CompanyBean> result = new ArrayList<CompanyBean>();
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		ResultSet results;
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(CompanyMapper.INSTANCE.mapCompany(results));
			}
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public int countLines() {
		LOG.trace("countLine()");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "SELECT COUNT(*) FROM company;";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return results.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	public void create(CompanyBean entity) {
		throw new UnsupportedOperationException();
		
	}

	public void update(CompanyBean entity) {
		throw new UnsupportedOperationException();
		
	}

	public void delete(CompanyBean entity) {
		throw new UnsupportedOperationException();
		
	}
}

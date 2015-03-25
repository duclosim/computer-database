package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author excilys
 *
 */
public enum CompanyServiceImpl implements CompanyService {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	private CompanyDAO dao;
	
	private CompanyServiceImpl() {
		dao = CompanyDAOImpl.INSTANCE;
	}

	@Override
	public Company getById(Long id) {
		LOG.trace("getById(" + id + ")");
		if (id == null) {
			LOG.error("id est à null.");
			throw new IllegalArgumentException("id est à null.");
		}
		Connection connection = null;
		Company result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getById(id, connection);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<Company> getByNameOrCompanyName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> getAll(int limit, int offset) {
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul.");
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			LOG.error("offset est négatif.");
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection connection = null;
		List<Company> result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getAll(limit, offset, connection);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}
	
	@Override
	public List<Company> getAll() {
		LOG.trace("getAll()");
		Connection connection = null;
		List<Company> result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getAll(connection);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}
	
	@Override
	public int countLines() {
		LOG.trace("countLines()");
		Connection connection = null;
		int result = 0;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.countLines(connection);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public void delete(Company company) {
		LOG.trace("delete(" + company + ")");
		if (company == null) {
			LOG.error("company est à null.");
			throw new IllegalArgumentException("company est à null.");
		}
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			connection.setAutoCommit(false);
			ComputerDAOImpl.INSTANCE.deleteByCompanyId(company.getId(), connection);
			dao.delete(company, connection);
			connection.commit();
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOG.error("Impossible de rollback les changements.");
				e1.printStackTrace();
				throw new PersistenceException("Impossible de rollback les changements.");
			}
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
	}

	
}

package com.excilys.computerDatabase.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

/**
 * 
 * @author excilys
 *
 */
public enum CompanyServiceImpl implements CompanyService {
	INSTANCE;
	
	private CompanyDAO dao;
	
	private CompanyServiceImpl() {
		dao = CompanyDAOImpl.INSTANCE;
	}

	@Override
	public Company getById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("id est à null.");
		}
		Connection connection = null;
		Company result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getById(id, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<Company> getAll(int limit, int offset) {
		if (limit <= 0) {
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection connection = null;
		List<Company> result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getAll(limit, offset, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return result;
	}
	
	@Override
	public List<Company> getAll() {
		Connection connection = null;
		List<Company> result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.getAll(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return result;
	}
	
	@Override
	public int countLines() {
		Connection connection = null;
		int result = 0;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.countLines(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
		return result;
	}

	@Override
	public void delete(Company company) {
		if (company == null) {
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
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new PersistenceException("Impossible de rollback les changements.");
			}
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
	}

}

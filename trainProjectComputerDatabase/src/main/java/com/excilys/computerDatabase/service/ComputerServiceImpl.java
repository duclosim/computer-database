package com.excilys.computerDatabase.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

/**
 * 
 * @author excilys
 *
 */
public enum ComputerServiceImpl implements ComputerService {
	INSTANCE;
	
	private ComputerDAO dao;
	
	private ComputerServiceImpl() {
		dao = ComputerDAOImpl.INSTANCE;
	}
	
	@Override
	public Computer getById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("id est à null.");
		}
		Connection connection = null;
		Computer result = null;
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
	public List<Computer> getAll(int limit, int offset) {
		if (limit <= 0) {
			throw new IllegalArgumentException("limit est négatif ou nul.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("offset est négatif.");
		}
		Connection connection = null;
		List<Computer> result = null;
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
	public void create(Computer computer) {
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.create(computer, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}

	}

	@Override
	public void update(Computer computer) {
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.update(computer, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
	}

	@Override
	public void delete(Computer computer) {
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.delete(computer, connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(connection);
		}
	}
}

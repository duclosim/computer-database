package com.excilys.computerDatabase.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;

/**
 * 
 * @author excilys
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	@Autowired
	private CompanyDAO dao;
	@Autowired
	private ComputerDAO computerDAO;
	
	@Override
	public Company getById(Long id) {
		LOG.trace("getById(" + id + ")");
		if (id == null) {
			LOG.error("id est à null.");
			throw new IllegalArgumentException("id est à null.");
		}
		Company result = null;
		try {
			result = dao.getById(id);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public List<Company> getFiltered(String name, int limit, int offset) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<Company> getAll() {
		LOG.trace("getAll()");
		List<Company> result = null;
		try {
			result = dao.getAll();
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
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
		List<Company> result = null;
		try {
			result = dao.getAll(limit, offset);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public List<Company> getOrdered(int limit, int offset,
			ComputerColumn column, OrderingWay way) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int countAllLines() {
		LOG.trace("countLines()");
		int result = 0;
		try {
			result = dao.countLines();
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public int countFilteredLines(String name) {
		throw new UnsupportedOperationException();
	}

	@Transactional
	@Override
	public void delete(Company company) {
		LOG.trace("delete(" + company + ")");
		if (company == null) {
			LOG.error("company est à null.");
			throw new IllegalArgumentException("company est à null.");
		}
		try {
			computerDAO.deleteByCompanyId(company.getId());
			dao.delete(company);
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		}
	}
}

package com.excilys.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.beans.Company;
import com.excilys.persistence.daos.CompanyDAO;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.ComputerDAO;
import com.excilys.persistence.daos.OrderingWay;

/**
 * 
 * @author excilys
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {
	private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	@Autowired
	private CompanyDAO companyDao;
	@Autowired
	private ComputerDAO computerDAO;
	
	@Override
	public Company getById(Long id) {
		LOG.trace("getById(" + id + ")");
		if (id == null) {
			LOG.error("id est à null.");
			throw new IllegalArgumentException("id est à null.");
		}
		Company result;
		result = companyDao.getById(id);
		return result;
	}
	
	@Override
	public List<Company> getAll() {
		LOG.trace("getAll()");
		List<Company> result;
		result = companyDao.getAll();
		return result;
	}

	@Override
	public List<Company> getAll(int limit, int offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> getFiltered(String name, int limit, int offset) {
		throw new UnsupportedOperationException();
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
		int result;
		result = companyDao.countLines();
		return result;
	}

	@Override
	public int countFilteredLines(String name) {
		throw new UnsupportedOperationException();
	}

	@Transactional
	@Override
	public void delete(Long id) {
		LOG.trace("delete(" + id + ")");
		if (id == null) {
			LOG.error("id est à null.");
			throw new IllegalArgumentException("id est à null.");
		}
		computerDAO.deleteByCompanyId(id);
		companyDao.delete(id);
	}
}

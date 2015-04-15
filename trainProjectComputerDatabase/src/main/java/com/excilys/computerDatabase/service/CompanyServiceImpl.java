package com.excilys.computerDatabase.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerDatabase.model.beans.Company;
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
	private CompanyDAO companyDao;
	@Autowired
	private ComputerDAO computerDAO;
	
	@Override
	public Company getById(Long id) {
		LOG.info("getById(" + id + ")");
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
		LOG.info("getAll()");
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
		LOG.info("countLines()");
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
	public void delete(Company company) {
		LOG.info("delete(" + company + ")");
		if (company == null) {
			LOG.error("company est à null.");
			throw new IllegalArgumentException("company est à null.");
		}
		computerDAO.deleteByCompanyId(company.getId());
		companyDao.delete(company);
	}
}

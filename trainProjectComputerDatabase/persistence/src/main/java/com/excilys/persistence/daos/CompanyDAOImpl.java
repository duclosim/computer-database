package com.excilys.persistence.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.beans.Company;
import com.excilys.persistence.mappers.CompanyMapper;

/**
 * 
 * @author excilys
 *
 */
@Repository
@Transactional
public class CompanyDAOImpl implements CompanyDAO {
	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAOImpl.class);
	
	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;

	private static final String HQL_GET = "select company from Company company where id= :id";
	@Override
	public Company getById(Long id) {
		LOG.trace("getById(" + id + ")");
		return (Company) getSession().createQuery(HQL_GET)
               .setLong("id", id)
               .uniqueResult();
	}

	private static final String HQL_GET_ALL = "select company from Company company";
	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getAll(int limit, int offset) {
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		return (List<Company>) getSession().createQuery(HQL_GET_ALL)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getAll() {
		LOG.trace("getAll()");
		return (List<Company>) getSession().createQuery(HQL_GET_ALL)
				.list();
	}

	@Override
	public List<Company> getFiltered(int limit, int offset, String name) {
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

	private static final String HQL_COUNT = "select count(*) from Company";
	@Override
	public int countLines() {
		LOG.trace("countLine()");
		Long result = (Long) getSession().createQuery(HQL_COUNT).uniqueResult();
		return result.intValue();
	}

	@Override
	public void create(Company company) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(Company company) {
		throw new UnsupportedOperationException();
		
	}

	private static final String HQL_DELETE = "delete from Company where id= :id";
	@Override
	public void delete(Long id) {
		LOG.trace("delete(" + id + ")");
		if (id == null) {
			LOG.error("id est à null.");
			throw new IllegalArgumentException("id est à null.");
		}
		getSession().createQuery(HQL_DELETE)
				.setLong("id", id)
				.executeUpdate();
	}
	
	/*
	 * Returns the current Hibernate session.
	 */
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}

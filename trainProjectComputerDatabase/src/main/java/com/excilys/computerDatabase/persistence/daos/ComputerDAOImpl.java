package com.excilys.computerDatabase.persistence.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 */
@Repository
@Transactional
public class ComputerDAOImpl implements ComputerDAO {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	@Autowired
	private ComputerMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;

	private static final String HQL_GET = "select computer from Computer computer "
			+ "left join computer.company as company "
			+ "where computer.id= :id";
	@Override
	public Computer getById(Long id) {
		LOG.info("getById(" + id + ")");
		Computer result = (Computer) getSession().createQuery(HQL_GET)
				.setLong("id", id)
				.uniqueResult();
		return result;
	}

	private static final String HQL_GET_ALL = "select computer from Computer computer "
			+ "left join computer.company as company";
	@SuppressWarnings("unchecked")
	@Override
	public List<Computer> getAll(int limit, int offset) {
		LOG.info(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<Computer> result = (List<Computer>) getSession().createQuery(HQL_GET_ALL)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.list();
		return result;
	}

	private static final String HQL_GET_FILTERED = "select computer from Computer computer "
			+ "left join computer.company as company "
			+ "where computer.name like :name "
			+ "or company.name like :name";
	@SuppressWarnings("unchecked")
	@Override
	public List<Computer> getFiltered(int limit, int offset, String name) {
		LOG.info(new StringBuilder("getFiltered(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(")").toString());
		List<Computer> result = (List<Computer>) getSession().createQuery(HQL_GET_FILTERED)
				.setString("name", '%' + name + '%')
				.setFirstResult(offset)
				.setMaxResults(limit)
				.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Computer> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) {
		LOG.info(new StringBuilder("getOrdered(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		StringBuilder query = new StringBuilder("select computer from Computer computer ")
			.append("left join computer.company as company ");
		if (column != null && way != null) {
			query.append("order by ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(", ")
				.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" asc ");
		}
		List<Computer> result = (List<Computer>) getSession().createQuery(query.toString())
				.setFirstResult(offset)
				.setMaxResults(limit)
				.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Computer> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way) {
		LOG.info(new StringBuilder("getFilteredAndOrdered(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		StringBuilder query = new StringBuilder("select computer from Computer computer ")
			.append("left join computer.company as company ");
		if (name != null) {
			query.append("where computer.name like :name ")
				.append("or company.name like :name ");
		}
		if (column != null && way != null) {
			query.append("order by ")
				.append(column.getColumnName()).append(" ")
				.append(way.getWay()).append(", ")
				.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" asc ");
		}
		List<Computer> result = (List<Computer>) getSession().createQuery(query.toString())
				.setString("name", '%' + name + '%')
				.setFirstResult(offset)
				.setMaxResults(limit)
				.list();
		return result;
	}

	private static final String HQL_COUNT = "select count(*) from Computer";
	@Override
	public int countLines() {
		LOG.info("countLine()");
		Long result = (Long) getSession().createQuery(HQL_COUNT).uniqueResult();
		return result.intValue();
	}

	private static final String HQL_COUNT_FILTERED = "select count(*) from Computer as computer "
			+ "left join computer.company as company "
			+ "where computer.name like :name "
			+ "or company.name like :name ";
	@Override
	public int countFilteredLines(String name) {
		LOG.info("countFilteredLines(" + name + ")");
		Long result = (Long) getSession().createQuery(HQL_COUNT_FILTERED)
				.setString("name", '%' + name + '%')
				.uniqueResult();
		return result.intValue();
	}

	@Override
	public void create(Computer computer) {
		LOG.info("create(" + computer + ")");         
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		getSession().saveOrUpdate(computer);
	}

	@Override
	public void update(Computer computer) {
		LOG.info("update(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		if (computer.getName() == null) {
			LOG.error("computerName est à null.");
			throw new IllegalArgumentException("computerName est à null.");
		}
		getSession().update(computer);
	}

	private static final String HQL_DELETE = "delete from Computer where id= :id";
	@Override
	public void delete(Computer computer) {
		LOG.info("delete(" + computer + ")");
		if (computer == null) {
			LOG.error("computer est à null.");
			throw new IllegalArgumentException("computer est à null.");
		}
		getSession().createQuery(HQL_DELETE)
				.setLong("id", computer.getId())
				.executeUpdate();
	}

	private static final String HQL_DELETE_BY_COMPANY_ID = "delete from Computer where company_id= :id";
	@Override
	public void deleteByCompanyId(Long companyId) {
		LOG.info("deleteByCompanyId(" + companyId + ")");
		getSession().createQuery(HQL_DELETE_BY_COMPANY_ID)
				.setLong("id", companyId)
				.executeUpdate();
	}
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}

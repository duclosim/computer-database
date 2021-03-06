package com.excilys.persistence.daos;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.model.beans.Computer;
import com.excilys.persistence.mappers.ComputerMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:persistence-context.xml")
public class ComputerDAOTest {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOTest.class);
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private ComputerDAO computerDAO;
	@Autowired
	private ComputerMapper mapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void getByIdShouldReturnABean() throws SQLException {
		LOG.debug("getByIdShouldReturnABean()");
		// Given
		Long id = new Long(10);
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id=?;";
		Computer bean;
		Computer expectedBean;
		List<Computer> list = jdbcTemplate.query(query, new Object[]{id}, mapper);
		expectedBean = list.isEmpty() ? null : list.get(0);
		// When
		bean = computerDAO.getById(id);
		// Then
		Assert.assertNotNull("Erreur sur le bean.", bean);
		Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
	}
	
	@Test
	public void getByNonFindableIdShouldReturnNullResult() {
		LOG.debug("getByNonFindableIdShouldReturnNullResult()");
		// Given
		Computer bean;
		Long id = new Long(-10);
		// When
		bean = computerDAO.getById(id);
		// Then
		Assert.assertNull("Le computer devrait être à null.", bean);
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		LOG.debug("getAllShouldReturnMultipleBeans()");
		// Given
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ? OFFSET ?;";
		List<Computer> bean;
		List<Computer> expectedBeans = jdbcTemplate.query(query, new Object[]{limit, offset}, mapper);
		// When
		bean = computerDAO.getAll(limit, offset);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
	}
	
	@Test
	public void getByNameShouldReturnABean() throws SQLException {
		LOG.debug("getByNameShouldReturnABean()");
		// Given
		String computerName = "CM-2a";
		String companyName = "Apple Inc.";
		int limit = 15;
		int offset = 5;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ? "
				+ "LIMIT ? OFFSET ?;";
		List<Computer> beans;
		List<Computer> beansByCompany;
		List<Computer> expectedBeansByComputerName = jdbcTemplate.query(query, new Object[]{
				new StringBuilder("%").append(computerName).append("%").toString(), 
				new StringBuilder("%").append(computerName).append("%").toString(),
				limit, offset},
				mapper);
		List<Computer> expectedBeansByCompanyName = jdbcTemplate.query(query, new Object[]{
				new StringBuilder("%").append(companyName).append("%").toString(), 
				new StringBuilder("%").append(companyName).append("%").toString(),
				limit, offset},
				mapper);
		// When
		beans = computerDAO.getFiltered(limit, offset, computerName);
		beansByCompany = computerDAO.getFiltered(limit, offset, companyName);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans par nom de computer.", expectedBeansByComputerName, beans);
		Assert.assertEquals("Erreur sur la liste de beans par nom de company.", expectedBeansByCompanyName, beansByCompany);
	}
	
	@Test
	public void getOrderedShouldReturnOrderedList() throws SQLException {
		LOG.debug("getOrderedShouldReturnOrderedList()");
		// Given
		int limit = 4;
		int offset = 0;
		ComputerColumn column = ComputerColumn.COMPANY_NAME_COLUMN_LABEL;
		OrderingWay way = OrderingWay.DESC;
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ")
			.append("ORDER BY ")
			.append(column.getColumnName()).append(" ")
			.append(way.getWay()).append(", ")
			.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" ASC ")
			.append("LIMIT ? OFFSET ?;");
		List<Computer> beans;
		List<Computer> expectedBeans = jdbcTemplate.query(query.toString(), new Object[]{limit, offset}, mapper);
		// When
		beans = computerDAO.getOrdered(limit, offset, column, way);
		// Then
		Assert.assertEquals("Erreur sur la taille de la liste.", expectedBeans.size(), beans.size());
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, beans);
	}

	@Test
	public void getFilteredAndOrderedShouldReturnOrderedList() throws SQLException {
		LOG.debug("getFilteredAndOrderedShouldReturnOrderedList()");
		// Given
		String companyName = "Nintendo";
		int limit = 15;
		int offset = 5;
		ComputerColumn col = ComputerColumn.NAME_COLUMN_LABEL;
		OrderingWay way = OrderingWay.ASC;
		StringBuilder query = new StringBuilder("SELECT * FROM computer ")
			.append("LEFT JOIN company ON computer.company_id = company.id ")
			.append("WHERE computer.name LIKE ? ")
			.append("OR company.name LIKE ? ")
			.append("ORDER BY ")
			.append(col.getColumnName()).append(" ")
			.append(way.getWay()).append(", ")
			.append(ComputerColumn.ID_COLUMN_LABEL.getColumnName()).append(" ASC ")
			.append("LIMIT ? OFFSET ?;");
		List<Computer> bean;
		List<Computer> expectedBeans = jdbcTemplate.query(query.toString(), new Object[]{
			new StringBuilder("%").append(companyName).append("%").toString(), 
			new StringBuilder("%").append(companyName).append("%").toString(),
			limit, offset}, mapper);
		// When
		bean = computerDAO.getFilteredAndOrdered(limit, offset, companyName, col, way);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() throws SQLException {
		LOG.debug("countLinesShouldReturnTheNumberOfRowsInTheDatabase()");
		// Given
		int nbLines;
		String query = "SELECT COUNT(*) FROM computer;";
		int expectedNbLines = jdbcTemplate.queryForObject(query, Integer.class);
		// When
		nbLines = computerDAO.countLines();
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedNbLines, nbLines);
	}
	
	@Test
	public void countFilteredLines() throws SQLException {
		LOG.debug("countFilteredLines()");
		// Given
		int nbLines;
		String name = "Nintendo";
		final String query = "SELECT COUNT(*) "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?;";
		String usableName = new StringBuilder("%").append(name).append("%").toString();
		int expectedSize = jdbcTemplate.queryForObject(query, new Object[]{usableName, usableName}, Integer.class);
		// When
		nbLines = computerDAO.countFilteredLines(name);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
	}
	
	@Test
	public void createShouldAddABeanToTheDatabase() throws SQLException {
		LOG.debug("createShouldAddABeanToTheDatabase()");
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		bean.setCompany(companyDAO.getById(companyId));
		// When
		computerDAO.create(bean);
		Computer expectedBean = computerDAO.getById(bean.getId());
		computerDAO.delete(bean.getId());
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() throws SQLException {
		LOG.debug("updateShouldAlterABeanFromTheDatabase()");
		// Given
		Long id = new Long(10);
		Computer bean = computerDAO.getById(id);
		Computer expectedBean = computerDAO.getById(id);
		String name = bean.getName();
		String newName = "new" + name;
		bean.setName(newName);
		expectedBean.setName(newName);
		// When
		computerDAO.update(bean);
		bean = computerDAO.getById(id);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
		// After
		bean.setName(name);
		computerDAO.update(bean);
	}
	
	@Test
	@Ignore
	public void deleteShouldRemoveABeanFromTheDatabase() throws SQLException {
		LOG.debug("deleteShouldRemoveABeanFromTheDatabase()");
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroduced(time);
		bean.setDiscontinued(time);
		bean.setCompany(companyDAO.getById(companyId));
		computerDAO.create(bean);
		// When
		computerDAO.delete(bean.getId());
		bean = computerDAO.getById(bean.getId());
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
	
	@Test
	@Ignore
	public void deleteByCompanyIdShouldDeleteMultipleBeans() throws SQLException {
		LOG.debug("deleteByCompanyIdShouldDeleteMultipleBeans()");
		// Given
		int nbLines;
		Long companyId = new Long(1);
		// When
		computerDAO.deleteByCompanyId(companyId);
		// Then
		String query = "SELECT COUNT(*) FROM computer WHERE company_id=?;";
		nbLines = jdbcTemplate.queryForObject(query, new Object[]{companyId}, Integer.class);
		Assert.assertEquals("Erreur sur le nombre de lignes", 0, nbLines);
	}
}

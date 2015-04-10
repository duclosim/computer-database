package com.excilys.computerDatabase.persistence.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@ActiveProfiles("DEV")
public class ComputerDAOTest {
	private ResultSet results;
	private PreparedStatement ps;
	
	@Autowired
	private CompanyDAOImpl companyDAO;
	@Autowired
	private ComputerDAOImpl computerDAO;
	@Autowired
	private ComputerMapper computerMapper;
	@Autowired
	private ConnectionFactory connection;
	
//    @BeforeClass
//    public static void setUpDB() {
//    	try {
//    		DBUtils.executeSqlFiles();
//    	} catch (SQLException | IOException e) {
//			e.printStackTrace();
//		}
//    }
//
//    @AfterClass
//    public static void tearDown() throws Exception {
//        DBUtils.databaseTester.onTearDown();
//    }
	
	@After
	public void closeConnection() throws SQLException {
		closeResources(results, ps);
		connection.closeConnection();
	}
	
	@Test
	public void getByIdShouldReturnABean() throws SQLException {
		// Given
		Computer bean;
		Long id = new Long(10);
		Computer expectedBean;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.id=?;";
		
		ps = connection.getConnection().prepareStatement(query);
		ps.setLong(1, id);
		results = ps.executeQuery();
		if (results.next()) {
			expectedBean = computerMapper.mapRow(results, 0);
			// When
			bean = computerDAO.getById(id);
			// Then
			Assert.assertNotNull("Erreur sur le bean.", bean);
			Assert.assertEquals("Erreur sur le bean.", expectedBean, bean);
		}
	}
	
	@Test
	public void getByNonFindableIdShouldReturnNullResult() {
		// Given
		Computer bean;
		Long id = new Long(-10);
		// When
		bean = computerDAO.getById(id);
		// Then
		Assert.assertNull("Le computer devrait être à null.", bean);
	}
	
	@Test
	public void getByNameShouldReturnABean() throws SQLException {
		// Given
		String computerName = "CM-2a";
		String companyName = "Apple Inc.";
		List<Computer> beans;
		List<Computer> beansByCompany;
		List<Computer> expectedBeansByComputerName = new ArrayList<>();
		List<Computer> expectedBeansByCompanyName = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ? "
				+ "LIMIT ? OFFSET ?;";
		ps = connection.getConnection().prepareStatement(query);
		int paramIndex = 0;
		ps.setString(++paramIndex, "%" + computerName + "%");
		ps.setString(++paramIndex, "%" + computerName + "%");
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByComputerName.add(computerMapper.mapRow(results, 0));
		}
		closeResources(results, ps);
		ps = connection.getConnection().prepareStatement(query);
		paramIndex = 0;
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeansByCompanyName.add(computerMapper.mapRow(results, 0));
		}
		// When
		beans = computerDAO.getFiltered(limit, offset, computerName);
		beansByCompany = computerDAO.getFiltered(limit, offset, companyName);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans par nom de computer.", expectedBeansByComputerName, beans);
		Assert.assertEquals("Erreur sur la liste de beans par nom de company.", expectedBeansByCompanyName, beansByCompany);
	}
	
	@Test
	public void getAllShouldReturnMultipleBeans() throws SQLException {
		// Given
		List<Computer> bean;
		List<Computer> expectedBeans = new ArrayList<>();
		int limit = 15;
		int offset = 5;
		String query = "SELECT * FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "LIMIT ? OFFSET ?;";
		ps = connection.getConnection().prepareStatement(query);
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeans.add(computerMapper.mapRow(results, 0));
		}
		// When
		bean = computerDAO.getAll(limit, offset);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
	}
	
	@Test
	public void getOrderedShouldReturnOrderedList() throws SQLException {
		// Given
		List<Computer> beans;
		List<Computer> expectedBeans = new ArrayList<>();
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
		ps = connection.getConnection().prepareStatement(query.toString());
		int paramIndex = 0;
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeans.add(computerMapper.mapRow(results, 0));
		}
		// When
		beans = computerDAO.getOrdered(limit, offset, column, way);
		// Then
		Assert.assertEquals("Erreur sur la taille de la liste.", expectedBeans.size(), beans.size());
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, beans);
	}

	@Test
	public void getFilteredAndOrderedShouldReturnOrderedList() throws SQLException {
		// Given
		List<Computer> bean;
		List<Computer> expectedBeans = new ArrayList<>();
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
		ps = connection.getConnection().prepareStatement(query.toString());
		int paramIndex = 0;
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setString(++paramIndex, "%" + companyName + "%");
		ps.setLong(++paramIndex, limit);
		ps.setLong(++paramIndex, offset);
		results = ps.executeQuery();
		while (results.next()) {
			expectedBeans.add(computerMapper.mapRow(results, 0));
		}
		// When
		bean = computerDAO.getFilteredAndOrdered(limit, offset, companyName, col, way);
		// Then
		Assert.assertEquals("Erreur sur la liste de beans.", expectedBeans, bean);
	}
	
	@Test
	public void countLinesShouldReturnTheNumberOfRowsInTheDatabase() throws SQLException {
		// Given
		int nbLines;
		String query = "SELECT COUNT(*) FROM computer;";
		ps = connection.getConnection().prepareStatement(query);
		results = ps.executeQuery();
		if (results.next()) {
			int expectedSize = results.getInt(1);
			// When
			nbLines = computerDAO.countLines();
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
	}
	
	@Test
	public void countFilteredLines() throws SQLException {
		// Given
		int nbLines;
		String name = "Nintendo";
		final String query = "SELECT COUNT(*) "
				+ "FROM computer "
				+ "LEFT JOIN company ON computer.company_id = company.id "
				+ "WHERE computer.name LIKE ? "
				+ "OR company.name LIKE ?;";
		ps = connection.getConnection().prepareStatement(query);
		ps.setString(1, new StringBuilder("%").append(name).append("%").toString());
		ps.setString(2, new StringBuilder("%").append(name).append("%").toString());
		results = ps.executeQuery();
		if (results.next()) {
			int expectedSize = results.getInt(1);
			// When
			nbLines = computerDAO.countFilteredLines(name);
			// Then
			Assert.assertEquals("Erreur sur le bean", expectedSize, nbLines);
		}
	}
	
	@Test
	public void createShouldAddABeanToTheDatabase() throws SQLException {
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(companyDAO.getById(companyId));
		// When
		computerDAO.create(bean);
		Computer expectedBean = computerDAO.getById(bean.getId());
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void updateShouldAlterABeanFromTheDatabase() throws SQLException {
		// Given
		Long id = new Long(10);
		Computer bean;
		bean = computerDAO.getById(id);
		Computer expectedBean = computerDAO.getById(id);
		String name = "new " + bean.getName();
		bean.setName(name);
		expectedBean.setName(name);
		// When
		computerDAO.update(bean);
		bean = computerDAO.getById(id);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void deleteShouldRemoveABeanFromTheDatabase() throws SQLException {
		// Given
		Computer bean = new Computer();
		Long companyId = new Long(10);
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		LocalDateTime time = null;
		bean.setIntroducedDate(time);
		bean.setDiscontinuedDate(time);
		bean.setCompany(companyDAO.getById(companyId));
		computerDAO.create(bean);
		// When
		computerDAO.delete(bean);
		bean = computerDAO.getById(bean.getId());
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
	
	@Test
	public void deleteByCompanyIdShouldDeleteMultipleBeans() throws SQLException {
		// Given
		int nbLines;
		Long companyId = new Long(1);
		// When
		computerDAO.deleteByCompanyId(companyId);
		// Then
		String query = "SELECT COUNT(*) FROM computer WHERE company_id=?;";
		ps = connection.getConnection().prepareStatement(query);
		ps.setLong(1, companyId);
		results = ps.executeQuery();
		if (results.next()) {
			nbLines = results.getInt(1);
			Assert.assertEquals("Erreur sur le nombre de lignes", 0, nbLines);
		}
	}
	
	private void closeResources(ResultSet results, PreparedStatement ps) throws SQLException {
		if (results != null) {
			results.close();
		}
		if (ps != null) {
			ps.close();
		}
	}
}

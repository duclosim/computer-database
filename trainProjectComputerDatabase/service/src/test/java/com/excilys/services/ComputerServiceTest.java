package com.excilys.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.binding.dtos.ComputerDTOMapper;
import com.excilys.model.beans.Company;
import com.excilys.model.beans.Computer;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.ComputerDAO;
import com.excilys.persistence.daos.OrderingWay;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class ComputerServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerServiceTest.class);
	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerDAO computerDao;
	@Autowired
	private ComputerDTOMapper computerMapper;
	
	@Test
	public void getById() {
		LOG.debug("getById()");
		// Given
		Long id = new Long(3);
		Computer expectedComputer = computerDao.getById(id);
		// When
		Computer result = computerMapper.DTOToBean(computerService.getById(id));
		// Then
		Assert.assertEquals("Mauvais bean récupéré.", expectedComputer, result);
	}
	
	@Test
	public void getAll() {
		LOG.debug("getAll()");
		// Given
		int limit = 3;
		List<ComputerDTO> expectedComputers = computerMapper.BeansToDTOs(computerDao.getAll(limit, 0));
		// When
		List<ComputerDTO> result =  computerService.getAll(limit, 0);
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedComputers, result);
	}
	
	@Test
	public void getFiltered() {
		LOG.debug("getFiltered()");
		// Given
		int limit = 3;
		String searchedName = "ass";
		List<ComputerDTO> expectedComputers = computerMapper.BeansToDTOs(computerDao
				.getFiltered(limit, 0, searchedName));
		// When
		List<ComputerDTO> result =  computerService.getFiltered(searchedName, limit, 0);
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedComputers, result);
	}
	
	@Test
	public void getOrdered() {
		LOG.debug("getOrdered()");
		// Given
		int limit = 3;
		ComputerColumn column = ComputerColumn.COMPANY_NAME_COLUMN_LABEL;
		OrderingWay way = OrderingWay.ASC;
		List<ComputerDTO> expectedComputers = computerMapper.BeansToDTOs(computerDao
				.getOrdered(limit, 0, column, way));
		// When
		List<ComputerDTO> result =  computerService.getOrdered(limit, 0, column, way);
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedComputers, result);
	}
	
	@Test
	public void getFilteredAndOrdered() {
		LOG.debug("getFilteredAndOrdered()");
		// Given
		int limit = 3;
		String searchedName = "ass";
		ComputerColumn column = ComputerColumn.COMPANY_NAME_COLUMN_LABEL;
		OrderingWay way = OrderingWay.ASC;
		List<ComputerDTO> expectedComputers = computerMapper.BeansToDTOs(computerDao
				.getFilteredAndOrdered(limit, 0, searchedName, column, way));
		// When
		List<ComputerDTO> result =  computerService
				.getFilteredAndOrdered(limit, 0, searchedName, column, way);
		// Then
		Assert.assertEquals("Mauvais beans récupérés.", expectedComputers, result);
	}
	
	@Test
	public void countAllLines() {
		LOG.debug("countAllLines()");
		// Given
		int expectedNbLines = computerDao.countLines();
		// When
		int result = computerService.countAllLines();
		// Then
		Assert.assertEquals("Mauvais bean récupéré", expectedNbLines, result);
	}
	
	@Test
	public void countFilteredLines() {
		LOG.debug("countFilteredLines()");
		// Given
		String searchedName = "ass";
		int expectedNbLines = computerDao.countFilteredLines(searchedName);
		// When
		int result = computerService.countFilteredLines(searchedName);
		// Then
		Assert.assertEquals("Mauvais bean récupéré", expectedNbLines, result);
	}
	
	@Test
	public void create() {
		LOG.debug("create()");
		// Given
		ComputerDTO bean = new ComputerDTO();
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		bean.setIntroducedDate(null);
		bean.setDiscontinuedDate(null);
		Company company = companyService.getById(new Long(7));
		bean.setCompanyId(company.getId().toString());
		bean.setCompanyName(company.getName().toString());
		// When
		computerService.create(bean);
		ComputerDTO expectedBean = computerService.getById(Long.parseLong(bean.getId()));
		computerService.delete(bean);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
	}
	
	@Test
	public void update() {
		LOG.debug("update()");
		// Given
		Long id = new Long(7);
		ComputerDTO bean = computerService.getById(id);
		ComputerDTO expectedBean = computerService.getById(id);
		String name = bean.getName();
		String newName = "new" + name;
		bean.setName(newName);
		expectedBean.setName(newName);
		// When
		computerService.update(bean);
		bean = computerService.getById(id);
		// Then
		Assert.assertEquals("Erreur sur le bean", expectedBean, bean);
		// After
		bean.setName(name);
		computerService.update(bean);
	}
	
	@Test
	public void delete() {
		LOG.debug("delete()");
		// Given
		ComputerDTO bean = new ComputerDTO();
		String newBeanName = "newBean";
		bean.setName(newBeanName);
		bean.setIntroducedDate(null);
		bean.setDiscontinuedDate(null);
		Company company = companyService.getById(new Long(7));
		bean.setCompanyId(company.getId().toString());
		bean.setCompanyName(company.getName().toString());
		computerService.create(bean);
		Long id = Long.parseLong(bean.getId());
		// When
		computerService.delete(bean);
		bean = computerService.getById(id);
		// Then
		Assert.assertNull("Erreur sur le bean", bean);
	}
}

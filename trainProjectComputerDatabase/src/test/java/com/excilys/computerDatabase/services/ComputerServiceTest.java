package com.excilys.computerDatabase.services;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ComputerServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerServiceTest.class);
	@Autowired
	private ComputerService computerService;
	// TODO écrire tests
	
	@Test
	public void getById() {
		
	}
	
	@Test
	public void getAll() {
		
	}
	
	@Test
	public void getFiltered() {
		
	}
	
	@Test
	public void getOrdered() {
		
	}
	
	@Test
	public void getFilteredAndOrdered() {
		
	}
	
	@Test
	public void countAllLines() {
		
	}
	
	@Test
	public void countFilteredLines() {
		
	}
	
	@Test
	public void create() {
		
	}
	
	@Test
	public void update() {
		
	}
	
	@Test
	public void delete() {
		
	}
}

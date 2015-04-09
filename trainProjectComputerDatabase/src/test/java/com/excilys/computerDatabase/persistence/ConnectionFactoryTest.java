package com.excilys.computerDatabase.persistence;

import com.excilys.computerDatabase.persistence.ConnectionFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@ActiveProfiles("DEV")
public class ConnectionFactoryTest {
	@Autowired
	private ConnectionFactory connection;
	
	@Test
	public void shouldGetTheUniqueInstance() {
		// GIVEN
		// WHEN
		connection.getConnection();
		// THEN
	}
}

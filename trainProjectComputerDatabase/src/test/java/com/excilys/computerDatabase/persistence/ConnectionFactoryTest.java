package com.excilys.computerDatabase.persistence;

import com.excilys.computerDatabase.persistence.ConnectionFactory;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

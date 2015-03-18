package test.java.com.excilys.computerDatabase.persistence;

import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;

import org.junit.Test;

public class ConnectionFactoryTest {
	
	@Test
	public void shouldGetTheUniqueInstance() {
		// GIVEN
		// WHEN
		ConnectionFactory.INSTANCE.getConnection();
		// THEN
	}
	
	@Test
	public void shouldCloseTheUniqueInstance() {
		// GIVEN
		// WHEN
		ConnectionFactory.closeConnection(ConnectionFactory.INSTANCE.getConnection());
		// THEN
	}
}

package test.java.com.excilys.computerDatabase.persistence;

import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;

import org.junit.Test;

public class ConnectionFactoryTest {
	
	@SuppressWarnings("static-access")
	@Test
	public void shouldGetTheUniqueInstance() {
		// GIVEN
		// WHEN
		ConnectionFactory.INSTANCE.getConnection();
		// THEN
	}
}

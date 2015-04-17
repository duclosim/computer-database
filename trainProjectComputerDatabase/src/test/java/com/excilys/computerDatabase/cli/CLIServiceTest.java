package com.excilys.computerDatabase.cli;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerDatabase.validators.DateValidator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:mainApplicationContext.xml")
public class CLIServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(DateValidator.class);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		LOG.debug("setUpStreams()");
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		LOG.debug("cleanUpStreams()");
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Rule
    public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void commandInterpreterShouldRun() {
		LOG.debug("commandInterpreterShouldRun()");
		// Given
		CLIService cliService = new CLIService();
		String command = "exit";
		Scanner sc = new Scanner(System.in);
		// When
		boolean result = cliService.interpretCommand(command, sc);
		// Then
		Assert.assertTrue("Erreur sur la valeur de sortie.", result);
	}

	@Test
	public void commandInterpreterShouldNotWorkWithUnknownCommand() {
		LOG.debug("commandInterpreterShouldNotWorkWithUnknownCommand()");
		// Given
		CLIService cliService = new CLIService();
		String command = "toto";
		Scanner sc = new Scanner(System.in);
		String expectedMessage = "Commande non reconnue.\n";
		// When
		boolean result = cliService.interpretCommand(command, sc);
		// Then
		Assert.assertFalse("Erreur sur le résultat.", result);
		Assert.assertTrue("Erreur sur le message de sortie.", outContent.toString().contains(expectedMessage));
	}

	@Test
	public void commandInterpreterShouldThrowAnIllegalArgumentExceptionForNullCommand() {
		LOG.debug("commandInterpreterShouldThrowAnIllegalArgumentExceptionForNullCommand()");
		// Given
		CLIService cliService = new CLIService();
		String command = null;
		Scanner sc = new Scanner(System.in);
		thrown.expect(IllegalArgumentException.class);
		// When
		cliService.interpretCommand(command, sc);
		// Then
	}

	@Test
	public void commandInterpreterShouldThrowAnIllegalArgumentExceptionForNullScanner() {
		LOG.debug("commandInterpreterShouldThrowAnIllegalArgumentExceptionForNullScanner()");
		// Given
		CLIService cliService = new CLIService();
		String command = "";
		Scanner sc = null;
		thrown.expect(IllegalArgumentException.class);
		// When
		cliService.interpretCommand(command, sc);
		// Then
	}
}

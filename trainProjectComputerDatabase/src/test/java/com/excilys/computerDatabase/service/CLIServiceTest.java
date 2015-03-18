package test.java.com.excilys.computerDatabase.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import main.java.com.excilys.computerDatabase.service.CLIService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CLIServiceTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test
	public void commandInterpreterShouldRun() {
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
		// Given
		CLIService cliService = new CLIService();
		String command = "toto";
		Scanner sc = new Scanner(System.in);
		String expectedMessage = "Commande non reconnue.\n";
		// When
		boolean result = cliService.interpretCommand(command, sc);
		// Then
		Assert.assertFalse("Erreur sur le résultat.", result);
		Assert.assertEquals("Erreur sur le message de sortie.", expectedMessage, outContent.toString());
	}

	@Test
	public void commandInterpreterShouldThrowAnIllegalArgumentExceptionForNullCommand() {
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

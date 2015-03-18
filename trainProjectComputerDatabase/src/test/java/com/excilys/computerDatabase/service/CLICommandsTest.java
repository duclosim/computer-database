package test.java.com.excilys.computerDatabase.service;

import java.util.Scanner;

import main.java.com.excilys.computerDatabase.service.CLICommand;
import main.java.com.excilys.computerDatabase.service.runners.CommandRunner;
import main.java.com.excilys.computerDatabase.service.runners.GetCompanies;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class CLICommandsTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void constructorShouldSetParam() {
		// Given
		String expectedCommand = "get_companies";
		boolean expectedEndService = false;
		CommandRunner expectedCommandRunner = new GetCompanies();
		// When
		CLICommand cliCommand = CLICommand.GET_COMPANIES;
		// Then
		Assert.assertEquals("Erreur sur la commande.", expectedCommand, cliCommand.getCommand());
		Assert.assertEquals("Erreur sur la fin de service.", expectedEndService, cliCommand.getEndService());
		Assert.assertEquals("Erreur sur le lanceur de commande.", expectedCommandRunner.getClass(), cliCommand.getCommandRunner().getClass());
	}
	
	@Test
	public void commandRunShouldThrowAnIllegalArgumentExceptionBecauseOfNullScanner() {
		// Given
		Scanner sc = null;
		CLICommand cliCommand = CLICommand.DETAIL_COMPUTER;
		// When
		thrown.expect(IllegalArgumentException.class);
		cliCommand.runCommand(sc);
		// Then
	}
}

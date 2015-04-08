package com.excilys.computerDatabase.userInterface;

import com.excilys.computerDatabase.service.cli.CLIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Cette classe lit les commandes entrées en ligne de commande et les exécute.
 * @author excilys
 *
 */
public class ComputerDatabaseCLI {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDatabaseCLI.class);
	
	@Autowired
	private static CLIService cliService;
	
	public static void main(String args[]) {
		LOG.info("main(" + Arrays.toString(args) + ")");
		Scanner sc = new Scanner(System.in);
		boolean terminated = false;
		System.out.println("Bienvenue sur le CLI de Computer Database.");
        while(sc.hasNextLine() && !terminated) {
        	terminated = cliService.interpretCommand(sc.next(), sc);
        }
        sc.close();
	}
}

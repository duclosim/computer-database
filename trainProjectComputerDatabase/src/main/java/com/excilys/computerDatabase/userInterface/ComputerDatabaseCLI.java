package com.excilys.computerDatabase.userInterface;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerDatabase.service.cli.CLIService;

/**
 * Cette classe lit les commandes entrées en ligne de commande et les exécute.
 * @author excilys
 *
 */
public class ComputerDatabaseCLI {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDatabaseCLI.class);
	
	public static void main(String args[]) {
		LOG.info("main(" + Arrays.toString(args) + ")");
		Scanner sc = new Scanner(System.in);
		boolean terminated = false;
		System.out.println("Bienvenue sur le CLI de Computer Database.");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("./applicationContext.xml");
		CLIService cliService = (CLIService) ctx.getBean(CLIService.class);
        while(sc.hasNextLine() && !terminated) {
        	terminated = cliService.interpretCommand(sc.next(), sc);
        }
        sc.close();
	}
}

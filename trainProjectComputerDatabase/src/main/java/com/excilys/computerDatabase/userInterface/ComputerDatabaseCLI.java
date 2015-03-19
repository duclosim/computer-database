package com.excilys.computerDatabase.userInterface;

import java.util.Scanner;

import com.excilys.computerDatabase.service.CLIService;

/**
 * Cette classe lit les commandes entrées en ligne de commande et les exécute.
 * @author excilys
 *
 */
public class ComputerDatabaseCLI {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		boolean terminated = false;
		CLIService cliService = new CLIService();
		System.out.println("Bienvenue sur le CLI de Computer Database.");
        while(sc.hasNextLine() && !terminated) {
        	terminated = cliService.interpretCommand(sc.next(), sc);
        }
        sc.close();
	}
}

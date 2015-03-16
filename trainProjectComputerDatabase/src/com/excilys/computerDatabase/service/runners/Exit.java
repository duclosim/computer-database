package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

/**
 * Cette classe peut lancer la commande de sortie du programme.
 * @author excilys
 *
 */
public class Exit implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		System.out.println("Fin du programme");
	}

}

package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

/**
 * Cette interface représente les exécuteurs de commande de la CLI.
 * @author excilys
 *
 */
public interface CommandRunner {
	/**
	 * Cette méthode lance la commande implémentée.
	 * @param sc Le Scanner qui permet de lire les entrées utilisateur.
	 */
	void runCommand(Scanner sc);
}

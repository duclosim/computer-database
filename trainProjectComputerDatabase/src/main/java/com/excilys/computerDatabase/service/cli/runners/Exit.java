package com.excilys.computerDatabase.service.cli.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Cette classe peut lancer la commande de sortie du programme.
 * @author excilys
 *
 */
public class Exit implements CommandRunner {
	private static final Logger LOG = LoggerFactory.getLogger(Exit.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		System.out.println("Fin du programme");
	}

}

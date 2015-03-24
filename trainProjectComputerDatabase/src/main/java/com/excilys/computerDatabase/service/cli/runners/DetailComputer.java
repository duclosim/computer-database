package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

/**
 * Cette classe peut lancer la commande d'affichage des détails d'un ordinateur.
 * @author excilys
 *
 */
public class DetailComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		try {
			System.out.println("Entrez l'id de l'ordinateur recherché : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			Computer computerBean = computerService.getById(computerId);
			System.out.println(computerBean);
		} catch (NumberFormatException e) {
			System.err.println("L'id passé n'est pas un nombre.");
		}
	}

}

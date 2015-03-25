package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de suppression d'ordinateurs.
 * @author excilys
 *
 */
public class DeleteComputer implements CommandRunner {
	private static final Logger LOG = LoggerFactory.getLogger(DeleteComputer.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		try {
			System.out.println("Entrez l'id de l'ordinateur à supprimer : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			computerService.delete(computer);
			System.out.println(computer + " a été supprimé avec succès.");
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}

}

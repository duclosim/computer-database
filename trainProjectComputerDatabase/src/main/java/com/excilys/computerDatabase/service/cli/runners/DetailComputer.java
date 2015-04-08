package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande d'affichage des détails d'un ordinateur.
 * @author excilys
 *
 */
@Component
public class DetailComputer implements CommandRunner {
	@Autowired
	private ComputerService computerService;
	
	private static final Logger LOG = LoggerFactory.getLogger(DetailComputer.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		try {
			System.out.println("Entrez l'id de l'ordinateur recherché : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			System.out.println(computer);
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}

}

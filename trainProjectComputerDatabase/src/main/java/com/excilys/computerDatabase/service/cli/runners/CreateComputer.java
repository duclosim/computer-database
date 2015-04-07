package com.excilys.computerDatabase.service.cli.runners;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de création de nouveaux ordinateurs.
 * @author excilys
 *
 */
public class CreateComputer implements CommandRunner {
	@Autowired
	private ComputerService computerService;
	
	private static final Logger LOG = LoggerFactory.getLogger(CreateComputer.class);
	
	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		ComputerDTO computer = new ComputerDTO();
		System.out.println("Nom du nouvel ordinateur : ");
		String args = sc.next();
		if (!("").equals(args)) {
			computer.setName(args);
		}
		System.out.println("Date d'introduction du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computer.setIntroducedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println("Date de sortie du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computer.setDiscontinuedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println("Id d'entreprise du nouvel ordinateur : ");
		args = sc.nextLine();
		try {
			computer.setCompanyId(args);
		} catch (NumberFormatException e) {
			LOG.error("Nombre impossible à reconnaître.");
		}
		computerService.create(computer);
	}

}

package com.excilys.computerDatabase.service.cli.runners;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de création de nouveaux ordinateurs.
 * @author excilys
 *
 */
public class CreateComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
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
			System.err.println("Date impossible à reconnaître.");
		}
		System.out.println("Date de sortie du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computer.setDiscontinuedDate(args);
		} catch (DateTimeParseException e) {
			System.err.println("Date impossible à reconnaître.");
		}
		System.out.println("Id d'entreprise du nouvel ordinateur : ");
		args = sc.nextLine();
		try {
			computer.setCompanyId(args);
		} catch (NumberFormatException e) {
			System.err.println("Nombre impossible à reconnaître.");
		}
		computerService.create(computer);
	}

}

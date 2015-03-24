package com.excilys.computerDatabase.service.cli.runners;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

/**
 * Cette classe peut lancer la commande de création de nouveaux ordinateurs.
 * @author excilys
 *
 */
public class CreateComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		Computer computerBean = new Computer();
		System.out.println("Nom du nouvel ordinateur : ");
		String args = sc.next();
		if (!("").equals(args)) {
			computerBean.setName(args);
		}
		System.out.println("Date d'introduction du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computerBean.setIntroduced(LocalDateTime.parse(args));
		} catch (DateTimeParseException e) {
			System.err.println("Date impossible à reconnaître.");
		}
		System.out.println("Date de sortie du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computerBean.setDiscontinued(LocalDateTime.parse(args));
		} catch (DateTimeParseException e) {
			System.err.println("Date impossible à reconnaître.");
		}
		System.out.println("Id d'entreprise du nouvel ordinateur : ");
		args = sc.nextLine();
		try {
			computerBean.setCompany(CompanyServiceImpl.INSTANCE.getById(Long.parseLong(args)));
		} catch (NumberFormatException e) {
			System.err.println("Nombre impossible à reconnaître.");
		}
		computerService.create(computerBean);
	}

}

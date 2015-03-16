package com.excilys.computerDatabase.service.runners;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;


public class CreateComputer implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		ComputerBean computerBean = new ComputerBean();
		System.out.println("Nom du nouvel ordinateur : ");
		//Scanner sc = new Scanner(System.in);
		String args = sc.next();
		if (!("").equals(args)) {
			computerBean.setName(args);
		}
		System.out.println("Date d'introduction du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computerBean.setIntroduced(LocalDateTime.parse(args));
		} catch (DateTimeParseException e) {
			System.out.println("Date impossible à reconnaître.");
		}
		System.out.println("Date de sortie du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computerBean.setDiscontinued(LocalDateTime.parse(args));
		} catch (DateTimeParseException e) {
			System.out.println("Date impossible à reconnaître.");
		}
		System.out.println("Id d'entreprise du nouvel ordinateur : ");
		args = sc.nextLine();
		try {
			computerBean.setCompanyId(Long.parseLong(args));
		} catch (NumberFormatException e) {
			System.out.println("Nombre impossible à reconnaître.");
		}
		computerDAO.updateComputer(computerBean);
		//sc.close();
	}

}

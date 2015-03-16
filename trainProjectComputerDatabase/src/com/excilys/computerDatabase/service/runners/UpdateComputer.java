package com.excilys.computerDatabase.service.runners;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;


public class UpdateComputer implements CommandRunner {

	@Override
	public void runCommand() {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		System.out.println("Entrez l'id de l'ordinateur à modifier : ");
		Scanner sc = new Scanner(System.in);
		String args = sc.next();
		try {
			Long id = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(id);
			System.out.println("L'ordinateur à modifier est le suivant : ");
			System.out.println(computerBean);
			System.out.println("Nouveau nom : ");
			args = sc.next();
			if (!("").equals(args)) {
				computerBean.setName(args);
			}
			System.out.println("Nouvelle date d'introduction : ");
			args = sc.next();
			if (!("").equals(args)) {
				try {
					computerBean.setIntroduced(LocalDateTime.parse(args));
				} catch (DateTimeParseException e) {
					System.out.println("Date impossible à reconnaître.");
				}
			}
			System.out.println("Nouvelle date de sortie : ");
			args = sc.next();
			if (!("").equals(args)) {
				try {
					computerBean.setDiscontinued(LocalDateTime.parse(args));
				} catch (DateTimeParseException e) {
					System.out.println("Date impossible à reconnaître.");
				}
			}
			System.out.println("Nouvel id d'entreprise : ");
			args = sc.next();
			if (!("").equals(args)) {
				try {
					computerBean.setCompanyId(Long.parseLong(args));
				} catch (NumberFormatException e) {
					System.out.println("Nombre impossible à reconnaître.");
				}
			}
			computerDAO.updateComputer(computerBean);
		} catch (NumberFormatException e) {
			System.out.println("Nombre impossible à reconnaître");
		} finally {
			sc.close();
		}
	}

}

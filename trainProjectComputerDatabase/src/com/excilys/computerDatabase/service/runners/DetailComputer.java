package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;


public class DetailComputer implements CommandRunner {

	@Override
	public void runCommand() {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		try {
			System.out.println("Entrez l'id de l'ordinateur recherché : ");
			Scanner sc = new Scanner(System.in);
			String args = sc.next();
			sc.close();
			Long computerId = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(computerId);
			System.out.println(computerBean);
		} catch (NumberFormatException e) {
			System.out.println("L'id passé n'est pas un nombre.");
		}
	}

}

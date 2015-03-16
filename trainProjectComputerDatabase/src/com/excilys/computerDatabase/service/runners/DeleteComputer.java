package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;


public class DeleteComputer implements CommandRunner {

	@Override
	public void runCommand() {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		try {
			System.out.println("Entrez l'id de l'ordinateur à supprimer : ");
			Scanner sc = new Scanner(System.in);
			String args = sc.next();
			sc.close();
			Long computerId = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(computerId);
			computerDAO.deleteComputer(computerBean);
			System.out.println(computerBean + " a été supprimé avec succès.");
		} catch (NumberFormatException e) {
			System.out.println("L'id passé n'est pas un nombre.");
		}
	}

}

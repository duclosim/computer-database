package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande de suppression d'ordinateurs.
 * @author excilys
 *
 */
public class DeleteComputer implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		try {
			System.out.println("Entrez l'id de l'ordinateur à supprimer : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(computerId);
			computerDAO.deleteComputer(computerBean);
			System.out.println(computerBean + " a été supprimé avec succès.");
		} catch (NumberFormatException e) {
			System.out.println("L'id passé n'est pas un nombre.");
		}
	}

}

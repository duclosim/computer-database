package main.java.com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import main.java.com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande de suppression d'ordinateurs.
 * @author excilys
 *
 */
public class DeleteComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		try {
			System.out.println("Entrez l'id de l'ordinateur à supprimer : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(computerId);
			computerDAO.delete(computerBean);
			System.out.println(computerBean + " a été supprimé avec succès.");
		} catch (NumberFormatException e) {
			System.err.println("L'id passé n'est pas un nombre.");
		}
	}

}

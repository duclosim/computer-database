package main.java.com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import main.java.com.excilys.computerDatabase.model.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAO;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande d'affichage des détails d'un ordinateur.
 * @author excilys
 *
 */
public class DetailComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		try {
			System.out.println("Entrez l'id de l'ordinateur recherché : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(computerId);
			System.out.println(computerBean);
		} catch (NumberFormatException e) {
			System.err.println("L'id passé n'est pas un nombre.");
		}
	}

}

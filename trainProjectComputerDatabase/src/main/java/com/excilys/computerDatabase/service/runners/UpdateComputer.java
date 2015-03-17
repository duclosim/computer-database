package main.java.com.excilys.computerDatabase.service.runners;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import main.java.com.excilys.computerDatabase.model.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.CompanyDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAO;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande de mise à jour des 
 *   informations d'un ordinateur.
 * @author excilys
 *
 */
public class UpdateComputer implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		System.out.println("Entrez l'id de l'ordinateur à modifier : ");
		String args = sc.next();
		try {
			Long id = Long.parseLong(args);
			ComputerBean computerBean = computerDAO.getById(id);
			System.out.println("L'ordinateur à modifier est le suivant : ");
			System.out.println(computerBean);
			System.out.println("Nouveau nom : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				computerBean.setName(args);
			}
			System.out.println("Nouvelle date d'introduction (format 2007-12-03T10:15:30) : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computerBean.setIntroduced(LocalDateTime.parse(args));
				} catch (DateTimeParseException e) {
					System.err.println("Date impossible à reconnaître.");
				}
			} else {
				computerBean.setIntroduced(null);
			}
			System.out.println("Nouvelle date de sortie (format 2007-12-03T10:15:30) : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computerBean.setDiscontinued(LocalDateTime.parse(args));
				} catch (DateTimeParseException e) {
					System.err.println("Date impossible à reconnaître.");
				}
			} else {
				computerBean.setDiscontinued(null);
			}
			System.out.println("Nouvel id d'entreprise : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computerBean.setCompany(CompanyDAOImpl.INSTANCE.getById(Long.parseLong(args)));
				} catch (NumberFormatException e) {
					System.err.println("Nombre impossible à reconnaître.");
				}
			} else {
				computerBean.setCompany(null);
			}
			computerDAO.updateComputer(computerBean);
		} catch (NumberFormatException e) {
			System.err.println("Nombre impossible à reconnaître");
		} finally {
		}
	}

}

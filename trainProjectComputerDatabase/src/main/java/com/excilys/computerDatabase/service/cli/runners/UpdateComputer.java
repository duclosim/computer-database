package com.excilys.computerDatabase.service.cli.runners;

import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Cette classe peut lancer la commande de mise à jour des 
 *   informations d'un ordinateur.
 * @author excilys
 *
 */
public class UpdateComputer implements CommandRunner {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateComputer.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		System.out.println("Entrez l'id de l'ordinateur à modifier : ");
		String args = sc.next();
		try {
			Long id = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(id);
			System.out.println("L'ordinateur à modifier est le suivant : ");
			System.out.println(computer);
			System.out.println("Nouveau nom : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				computer.setName(args);
			}
			System.out.println("Nouvelle date d'introduction (format 2007-12-03T10:15:30) : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computer.setIntroducedDate(args);
				} catch (DateTimeParseException e) {
					LOG.error("Date impossible à reconnaître.");
				}
			} else {
				computer.setIntroducedDate(null);
			}
			System.out.println("Nouvelle date de sortie (format 2007-12-03T10:15:30) : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computer.setDiscontinuedDate(args);
				} catch (DateTimeParseException e) {
					LOG.error("Date impossible à reconnaître.");
				}
			} else {
				computer.setDiscontinuedDate(null);
			}
			System.out.println("Nouvel id d'entreprise : ");
			args = sc.nextLine();
			if (!("").equals(args)) {
				try {
					computer.setCompanyId(args);
				} catch (NumberFormatException e) {
					LOG.error("Nombre impossible à reconnaître.");
				}
			} else {
				computer.setCompanyId(null);
			}
			computerService.update(computer);
		} catch (NumberFormatException e) {
			LOG.error("Nombre impossible à reconnaître");
		} finally {
		}
	}

}

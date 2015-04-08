package com.excilys.computerDatabase.service.cli;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe représente l'interpréteur et invocateur de commandes.
 * @author excilys
 *
 */
@Component
public class CLIService {
	private static final String UNKNOWN_COMMAND = "Commande non reconnue.";
	private static final Logger LOG = LoggerFactory.getLogger(CLIService.class);
	
	@Autowired
	private Page<ComputerDTO> page;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	/**
	 * Cette méthode interpète la chaîne écrite sur l'entrée standard 
	 * 		et tente d'exécuter l'instruction correspondante.
	 * @param command
	 * @return true if the program is over.
	 */
	public boolean interpretCommand(String command, Scanner sc) {
		LOG.info(new StringBuilder("new interpretCommand(")
			.append(command).append(", ")
			.append(sc).append(")").toString());
		if (sc == null) {
			LOG.error("sc est à null.");
			throw new IllegalArgumentException("sc est à null.");
		}
		boolean isExit = false;
		switch (command) {
		case ("get_companies") :
			getCompanies();
			break;
		case ("get_computers") :
			getComputers(sc);
			break;
		case ("detail_computer") :
			detailComputer(sc);
			break;
		case ("create_computer") :
			createComputer(sc);
			break;
		case ("update_computer") :
			updateComputer(sc);
			break;
		case ("delete_company") :
			deleteCompany(sc);
			break;
		case ("delete_computer") :
			deleteComputer(sc);
			break;
		case ("exit") :
			exit();
			isExit = true;
			break;
		default :
			System.out.println(UNKNOWN_COMMAND);
			break;
		}
		return isExit;
	}
	
	public void getCompanies() {
		LOG.trace("getCompanies()");
		List<Company> page = companyService.getAll();
		System.out.println(page);
	}
	
	public void getComputers(Scanner sc) {
		LOG.trace("getComputers(" + sc + ")");
		System.out.println(page);
		for (int k = 2; k < page.getLastPageNb(); ++k) {
			page.setPageNum(k);
			System.out.println(page);
			sc.nextLine();
		}
	}
	
	public void createComputer(Scanner sc) {
		LOG.trace("createComputer(" + sc + ")");
		ComputerDTO computer = new ComputerDTO();
		System.out.println("Nom du nouvel ordinateur : ");
		String args = sc.next();
		if (!("").equals(args)) {
			computer.setName(args);
		}
		System.out.println("Date d'introduction du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computer.setIntroducedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println("Date de sortie du nouvel ordinateur (format 2007-12-03T10:15:30) : ");
		args = sc.nextLine();
		try {
			computer.setDiscontinuedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println("Id d'entreprise du nouvel ordinateur : ");
		args = sc.nextLine();
		try {
			computer.setCompanyId(args);
		} catch (NumberFormatException e) {
			LOG.error("Nombre impossible à reconnaître.");
		}
		computerService.create(computer);
	}
	
	public void deleteCompany(Scanner sc) {
		LOG.trace("deleteCompany(" + sc + ")");
		try {
			System.out.println("Entrez l'id de la companie à supprimer : ");
			String args = sc.next();
			Long companyId = Long.parseLong(args);
			Company company = companyService.getById(companyId);
			companyService.delete(company);
			System.out.println(company + " a été supprimée avec succès.");
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	public void deleteComputer(Scanner sc) {
		LOG.trace("deleteComputer(" + sc + ")");
		try {
			System.out.println("Entrez l'id de l'ordinateur à supprimer : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			computerService.delete(computer);
			System.out.println(computer + " a été supprimé avec succès.");
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	public void detailComputer(Scanner sc) {
		LOG.trace("detailComputer(" + sc + ")");
		try {
			System.out.println("Entrez l'id de l'ordinateur recherché : ");
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			System.out.println(computer);
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	public void exit() {
		LOG.trace("exit()");
		System.out.println("Fin du programme");
	}
	
	public void updateComputer(Scanner sc) {
		LOG.trace("updateComputer(" + sc + ")");
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
		}
    }
}

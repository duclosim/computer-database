package com.excilys.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.model.beans.Company;
import com.excilys.page.Page;
import com.excilys.services.CompanyService;
import com.excilys.services.ComputerService;
import com.excilys.ws.ComputerDatabaseWS;

/**
 * Cette classe représente l'interpréteur et invocateur de commandes.
 * @author excilys
 *
 */
@Component
class CLIWSClient {
	private static final Logger LOG = LoggerFactory.getLogger(CLIWSClient.class);
	private final URL url;
    private final QName qname;
    private final Service service;
    private final ComputerDatabaseWS ws;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Page page;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	public CLIWSClient() throws MalformedURLException {
		url = new URL("http://localhost:9999/computer-database-ws/computers?wsdl");
		qname = new QName("http://ws.excilys.com/", "ComputerDatabaseWSImpl");
        service = Service.create(url, qname);
        ws = service.getPort(ComputerDatabaseWS.class);
	}
	
	/**
	 * Cette méthode interpète la chaîne écrite sur l'entrée standard 
	 * 		et tente d'exécuter l'instruction correspondante.
	 * @param command
	 * @return true if the program is over.
	 */
	public boolean interpretCommand(String command, Scanner sc) {
		LOG.info(new StringBuilder("interpretCommand(")
			.append(command).append(", ")
			.append(sc).append(")").toString());
		if (command == null) {
			LOG.error("command est à null.");
			throw new IllegalArgumentException("command est à null.");
		}
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
			System.out.println();
			break;
		}
		return isExit;
	}
	
	private void getCompanies() {
		LOG.info("getCompanies()");
		for (Company cmpny : companyService.getAll()) {
			System.out.println(cmpny);
		}
	}
	
	private void getComputers(Scanner sc) {
		LOG.info("getComputers(" + sc + ")");
		for (int k = 1; k <= page.getLastPageNb(); ++k) {
			page.setPageNum(k);
			System.out.println(page);
		}
	}
	
	private void createComputer(Scanner sc) {
		LOG.info("createComputer(" + sc + ")");
		ComputerDTO computer = new ComputerDTO();
		System.out.println(getMessage("console.newComputer.name"));
		String args = sc.next();
		if (!("").equals(args)) {
			computer.setName(args);
		}
		System.out.println(getMessage("console.newComputer.intro"));
		args = sc.nextLine();
		try {
			computer.setIntroducedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println(getMessage("console.newComputer.dis"));
		args = sc.nextLine();
		try {
			computer.setDiscontinuedDate(args);
		} catch (DateTimeParseException e) {
			LOG.error("Date impossible à reconnaître.");
		}
		System.out.println(getMessage("console.newComputer.companyId"));
		args = sc.nextLine();
		try {
			computer.setCompanyId(args);
		} catch (NumberFormatException e) {
			LOG.error("Nombre impossible à reconnaître.");
		}
		computerService.create(computer);
	}
	
	private void deleteCompany(Scanner sc) {
		LOG.info("deleteCompany(" + sc + ")");
		try {
			System.out.println(getMessage("console.delete.companyId"));
			String args = sc.next();
			Long companyId = Long.parseLong(args);
			Company company = companyService.getById(companyId);
			companyService.delete(company);
			System.out.println(company + " " + getMessage("console.delete.companySucces"));
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void deleteComputer(Scanner sc) {
		LOG.info("deleteComputer(" + sc + ")");
		try {
			System.out.println(getMessage("console.delete.computerId"));
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			computerService.delete(computer);
			System.out.println(computer + " " + getMessage("console.delete.computerSucces"));
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void detailComputer(Scanner sc) {
		LOG.info("detailComputer(" + sc + ")");
		try {
			System.out.println(getMessage("console.detail.computerId"));
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(computerId);
			System.out.println(computer);
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void updateComputer(Scanner sc) {
		LOG.info("updateComputer(" + sc + ")");
		System.out.println(getMessage("console.edit.computerId"));
		String args = sc.next();
		try {
			Long id = Long.parseLong(args);
			ComputerDTO computer = computerService.getById(id);
			System.out.println(getMessage("console.edit.computerDetail"));
			System.out.println(computer);
			System.out.println(getMessage("console.edit.name"));
			args = sc.nextLine();
			if (!("").equals(args)) {
				computer.setName(args);
			}
			System.out.println(getMessage("console.edit.intro"));
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
			System.out.println(getMessage("console.edit.dis"));
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
			System.out.println(getMessage("console.edit.companyId"));
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
	
	private void exit() {
		LOG.info("exit()");
		System.out.println(getMessage("console.exit"));
	}
	
	private String getMessage(String code) {
		return messageSource
				.getMessage(code, null, LocaleContextHolder.getLocale());
	}
}

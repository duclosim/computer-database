package com.excilys.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
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
	
	public CLIWSClient() throws MalformedURLException {
		url = new URL(ComputerDatabaseWS.ENDPOINT + "?wsdl");
		qname = new QName("http://ws.excilys.com/", "ComputerDatabaseWSImplService");
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
		LOG.trace(new StringBuilder("interpretCommand(")
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
		LOG.trace("getCompanies()");
		System.out.println(ws.getCompanies());
	}
	
	private void getComputers(Scanner sc) {
		LOG.trace("getComputers(" + sc + ")");
		System.out.println(getMessage("console.detailComputer.limit"));
		String args = sc.next();
		int limit = 10;
		try {
			limit = Integer.parseInt(args);
		} catch (NumberFormatException e) {
			LOG.error("La limite passée n'est pas un nombre.");
		}
		System.out.println(getMessage("console.detailComputer.pageNum"));
		args = sc.next();
		int offset = 0;
		try {
			offset = Integer.parseInt(args);
		} catch (NumberFormatException e) {
			LOG.error("Le numéro de page passé n'est pas un nombre.");
		}
		System.out.println("Entrez 'y' pour indiquer un nom à rechercher : ");
		String searchedName = null;
		if (sc.next().equals("y")) {
			System.out.println(getMessage("console.detailComputer.searched"));
			args = sc.next();
			searchedName = args;
		}
		ComputerColumn column = null;
		OrderingWay way = null;
		System.out.println("Entrez 'y' pour indiquer une colonne et un sens de tri : ");
		if (sc.next().equals("y")) {
			System.out.println(getMessage("console.detailComputer.column"));
			args = sc.next();
			for (ComputerColumn col : ComputerColumn.values()) {
				if (col.getColumnName().equals(args)) {
					column = col;
				}
			}
			System.out.println(getMessage("console.detailComputer.way"));
			args = sc.next();
			for (OrderingWay w : OrderingWay.values()) {
				if (w.getWay().equals(args)) {
					way = w;
				}
			}
		}
		System.out.println(ws.getComputers(limit, offset, searchedName, column, way));
	}
	
	private void createComputer(Scanner sc) {
		LOG.trace("createComputer(" + sc + ")");
		ComputerDTO computer = new ComputerDTO();
		computer.setName(null);
		System.out.println(getMessage("console.newComputer.name"));
		String args = sc.next();
		if (!("").equals(args)) {
			computer.setName(args);
		}
		System.out.println("Entrez 'y' pour indiquer une date d'introduction : ");
		if (sc.next().equals("y")) {
			System.out.println(getMessage("console.newComputer.intro"));
			args = sc.next();
			try {
				LocalDate.parse(args, getFormatter());
				computer.setIntroducedDate(args);
			} catch (DateTimeParseException e) {
				LOG.error("Date impossible à reconnaître.");
			}
		}
		System.out.println("Entrez 'y' pour indiquer une date de retrait : ");
		if (sc.next().equals("y")) {
			System.out.println(getMessage("console.newComputer.dis"));
			args = sc.next();
			try {
				LocalDate.parse(args, getFormatter());
				computer.setDiscontinuedDate(args);
			} catch (DateTimeParseException e) {
				LOG.error("Date impossible à reconnaître.");
			}
		}
		System.out.println("Entrez 'y' pour indiquer un numéro d'entreprise : ");
		if (sc.next().equals("y")) {
			System.out.println(getMessage("console.newComputer.companyId"));
			args = sc.next();
			try {
				computer.setCompanyId(args);
			} catch (NumberFormatException e) {
				LOG.error("Nombre impossible à reconnaître.");
			}
		}
		System.out.println(ws.createComputer(computer));
	}
	
	private void deleteCompany(Scanner sc) {
		LOG.trace("deleteCompany(" + sc + ")");
		try {
			System.out.println(getMessage("console.delete.companyId"));
			String args = sc.next();
			Long companyId = Long.parseLong(args);
			ws.deleteCompany(companyId);
			System.out.println(companyId + " " + getMessage("console.delete.companySucces"));
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void deleteComputer(Scanner sc) {
		LOG.trace("deleteComputer(" + sc + ")");
		try {
			System.out.println(getMessage("console.delete.computerId"));
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			ws.deleteComputer(computerId);
			System.out.println(computerId + " " + getMessage("console.delete.computerSucces"));
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void detailComputer(Scanner sc) {
		LOG.trace("detailComputer(" + sc + ")");
		try {
			System.out.println(getMessage("console.detail.computerId"));
			String args = sc.next();
			Long computerId = Long.parseLong(args);
			System.out.println(ws.detailComputer(computerId));
		} catch (NumberFormatException e) {
			LOG.error("L'id passé n'est pas un nombre.");
		}
	}
	
	private void updateComputer(Scanner sc) {
		LOG.trace("updateComputer(" + sc + ")");
		System.out.println(getMessage("console.edit.computerId"));
		String args = sc.next();
		try {
			ComputerDTO computer = new ComputerDTO();
			computer.setId(args);
			System.out.println(getMessage("console.edit.computerDetail"));
			System.out.println(ws.detailComputer(Long.parseLong(args)));
			System.out.println("Entrez 'y' pour indiquer un nouveau nom : ");
			if (sc.next().equals("y")) {
				System.out.println(getMessage("console.edit.name"));
				args = sc.next();
				if (!("").equals(args)) {
					computer.setName(args);
				}
			}
			System.out.println("Entrez 'y' pour indiquer une nouvelle date d'introduction : ");
			if (sc.next().equals("y")) {
				try {
					System.out.println(getMessage("console.edit.intro"));
					args = sc.next();
					LocalDate.parse(args, getFormatter());
					computer.setIntroducedDate(args);
				} catch (DateTimeParseException e) {
					LOG.error("Date impossible à reconnaître.");
				}
			}
			System.out.println("Entrez 'y' pour indiquer une nouvelle date de retrait : ");
			if (sc.next().equals("y")) {
				try {
					System.out.println(getMessage("console.edit.dis"));
					args = sc.next();
					LocalDate.parse(args, getFormatter());
					computer.setDiscontinuedDate(args);
				} catch (DateTimeParseException e) {
					LOG.error("Date impossible à reconnaître.");
				}
			}
			System.out.println("Entrez 'y' pour indiquer un nouveau numéro d'entreprise : ");
			if (sc.next().equals("y")) {
				System.out.println(getMessage("console.edit.companyId"));
				args = sc.next();
				try {
					computer.setCompanyId(args);
				} catch (NumberFormatException e) {
					LOG.error("Nombre impossible à reconnaître.");
				}
			}
			ws.updateComputer(computer);
		} catch (NumberFormatException e) {
			LOG.error("Nombre impossible à reconnaître");
		}
    }
	
	private void exit() {
		LOG.trace("exit()");
		System.out.println(getMessage("console.exit"));
	}
	
	private String getMessage(String code) {
		return messageSource
				.getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
	private DateTimeFormatter getFormatter() {
		String dateFormat = getMessage("date.format");
		return DateTimeFormatter.ofPattern(dateFormat);
	}
}

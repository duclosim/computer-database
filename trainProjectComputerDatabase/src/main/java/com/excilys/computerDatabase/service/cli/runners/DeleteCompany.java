package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.service.CompanyService;

public class DeleteCompany implements CommandRunner {
	@Autowired
	private CompanyService companyService;
	
	private static final Logger LOG = LoggerFactory.getLogger(DeleteCompany.class);

	@Override
	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
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

}

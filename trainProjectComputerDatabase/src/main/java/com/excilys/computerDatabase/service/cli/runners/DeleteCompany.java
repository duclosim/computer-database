package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;

public class DeleteCompany implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		CompanyService companyService = CompanyServiceImpl.INSTANCE;
		try {
			System.out.println("Entrez l'id de la companie à supprimer : ");
			String args = sc.next();
			Long companyId = Long.parseLong(args);
			Company company = companyService.getById(companyId);
			companyService.delete(company);
			System.out.println(company + " a été supprimée avec succès.");
		} catch (NumberFormatException e) {
			System.err.println("L'id passé n'est pas un nombre.");
		}
	}

}

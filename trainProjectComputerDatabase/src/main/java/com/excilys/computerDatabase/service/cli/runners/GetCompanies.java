package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.service.CompanyService;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {
	@Autowired
	private CompanyService companyService;
	
	private static final Logger LOG = LoggerFactory.getLogger(GetCompanies.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		Page<Company> page = new Page<>(companyService, MAX_ITEMS_BY_PAGE, 0);
		System.out.println(page);
		for (int k = 2; k < page.getLastPageNb(); ++k) {
			page = new Page<>(companyService);
			System.out.println(page);
			sc.nextLine();
		}
	}
}

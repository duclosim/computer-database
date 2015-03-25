package com.excilys.computerDatabase.service.cli.runners;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.page.NavigationPage;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {
	private static final Logger LOG = LoggerFactory.getLogger(GetCompanies.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		CompanyService companyService = CompanyServiceImpl.INSTANCE;
		NavigationPage<Company> page = new NavigationPage<>(companyService, MAX_ITEMS_BY_PAGE, 0);
		System.out.println(page);
		for (int k = 2; k < page.getLastPageNb(); ++k) {
			page = new NavigationPage<>(companyService);
			System.out.println(page);
			sc.nextLine();
		}
	}
}

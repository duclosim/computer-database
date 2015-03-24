package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {

	public void runCommand(Scanner sc) {
		CompanyService companyService = CompanyServiceImpl.INSTANCE;
		int offset = 0;
		int limit = MAX_ITEMS_BY_PAGE;
		Page<Company> page = new Page<Company>(companyService, limit, offset);
		while (page.getPageNum() <= page.getLastPageNb()) {
			System.out.println(page);
			page = new Page<Company>(companyService, limit, offset);
			offset += limit;
			sc.nextLine();
		}
	}
}

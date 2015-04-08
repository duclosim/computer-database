package com.excilys.computerDatabase.service.cli.runners;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.service.CompanyService;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
@Component
public class GetCompanies implements CommandRunner {
	@Autowired
	private CompanyService companyService;
	
	private static final Logger LOG = LoggerFactory.getLogger(GetCompanies.class);

	public void runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		List<Company> page = companyService.getAll();
		System.out.println(page);
	}
}

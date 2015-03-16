package com.excilys.computerDatabase.service.runners;

import java.util.List;
import java.util.Scanner;

import com.excilys.computerDatabase.model.CompanyBean;
import com.excilys.computerDatabase.persistence.CompanyDAO;
import com.excilys.computerDatabase.persistence.CompanyDAOImpl;
import com.excilys.computerDatabase.service.pages.Page;
import com.excilys.computerDatabase.service.pages.PageContainer;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		CompanyDAO companyDAO = CompanyDAOImpl.getInstance();
		List<CompanyBean> companies = companyDAO.getAll();
		int startIndex = 0;
		int endIndex = Integer.min(companies.size(), startIndex + PageContainer.NB_ITEM_BY_PAGE);
		while (startIndex < companies.size()) {
			Page<CompanyBean> page = new Page<CompanyBean>(companies.subList(startIndex, endIndex));
			System.out.println(page);
			startIndex = endIndex;
			endIndex = Integer.min(companies.size(), endIndex + PageContainer.NB_ITEM_BY_PAGE);
			sc.nextLine();
		}
	}
}

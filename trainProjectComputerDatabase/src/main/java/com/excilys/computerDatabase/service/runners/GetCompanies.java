package main.java.com.excilys.computerDatabase.service.runners;

import java.util.List;
import java.util.Scanner;

import main.java.com.excilys.computerDatabase.model.CompanyBean;
import main.java.com.excilys.computerDatabase.model.pages.Page;
import main.java.com.excilys.computerDatabase.model.pages.PageContainer;
import main.java.com.excilys.computerDatabase.persistence.CompanyDAO;
import main.java.com.excilys.computerDatabase.persistence.CompanyDAOImpl;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {

	public void runCommand(Scanner sc) {
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
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

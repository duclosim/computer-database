package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.CompanyBean;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

/**
 * Cette classe peut lancer la commande de listage des company.
 * @author excilys
 *
 */
public class GetCompanies implements CommandRunner {

	public void runCommand(Scanner sc) {
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		int offset = 0;
		int limit = MAX_ITEMS_BY_PAGE;
		Page<CompanyBean> page = new Page<CompanyBean>(companyDAO, limit, offset);
		while (page.getPageNum() <= page.getLastPageNb()) {
			System.out.println(page);
			page = new Page<CompanyBean>(companyDAO, limit, offset);
			offset += limit;
			sc.nextLine();
		}
	}
}

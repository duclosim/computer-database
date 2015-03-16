package com.excilys.computerDatabase.service.runners;

import com.excilys.computerDatabase.model.CompanyBean;
import com.excilys.computerDatabase.persistence.CompanyDAO;
import com.excilys.computerDatabase.persistence.CompanyDAOImpl;


public class GetCompanies implements CommandRunner {

	@Override
	public void runCommand() {
		CompanyDAO companyDAO = CompanyDAOImpl.getInstance();
		for (CompanyBean bean : companyDAO.getAll()) {
			System.out.println(bean);
		}
	}

}

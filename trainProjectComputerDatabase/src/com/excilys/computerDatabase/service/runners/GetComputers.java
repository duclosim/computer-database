package com.excilys.computerDatabase.service.runners;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;


public class GetComputers implements CommandRunner {

	@Override
	public void runCommand() {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
	}

}

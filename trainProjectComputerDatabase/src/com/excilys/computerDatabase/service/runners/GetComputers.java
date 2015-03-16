package com.excilys.computerDatabase.service.runners;

import java.util.List;
import java.util.Scanner;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;
import com.excilys.computerDatabase.service.Page;


public class GetComputers implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		List<ComputerBean> computers = computerDAO.getAll();
		int startIndex = 0;
		int endIndex = Integer.min(computers.size(), startIndex + NB_ITEM_BY_PAGE);
		while (startIndex < computers.size()) {
			Page<ComputerBean> page = new Page<ComputerBean>(computers.subList(startIndex, endIndex));
			System.out.println(page);
			startIndex = endIndex;
			endIndex = Integer.min(computers.size(), endIndex + NB_ITEM_BY_PAGE);
			sc.nextLine();
		}
	}

}

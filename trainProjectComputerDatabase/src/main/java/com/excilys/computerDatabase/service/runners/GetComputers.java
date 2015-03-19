package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.ComputerBean;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		int offset = 0;
		int limit = MAX_ITEMS_BY_PAGE;
		Page<ComputerBean> page = new Page<ComputerBean>(computerDAO, limit, offset);
		while (page.getPageNum() <= page.getLastPageNb()) {
			System.out.println(page);
			page = new Page<ComputerBean>(computerDAO, limit, offset);
			offset += limit;
			sc.nextLine();
		}
	}

}

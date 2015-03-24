package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		int offset = 0;
		int limit = MAX_ITEMS_BY_PAGE;
		Page<Computer> page = new Page<Computer>(computerService, limit, offset);
		while (page.getPageNum() <= page.getLastPageNb()) {
			System.out.println(page);
			page = new Page<Computer>(computerService, limit, offset);
			offset += limit;
			sc.nextLine();
		}
	}

}

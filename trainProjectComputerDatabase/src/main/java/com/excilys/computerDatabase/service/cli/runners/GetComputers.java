package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.PageableService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {

	public void runCommand(Scanner sc) {
		PageableService<ComputerDTO> computerService = ComputerServiceImpl.INSTANCE;
		int offset = 0;
		int limit = MAX_ITEMS_BY_PAGE;
		Page<ComputerDTO> page = new Page<>(computerService, limit, offset);
		while (page.getPageNum() <= page.getLastPageNb()) {
			System.out.println(page);
			page = new Page<>(computerService, limit, offset);
			offset += limit;
			sc.nextLine();
		}
	}

}

package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.page.NavigationPage;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.PageableService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {
	private static final Logger LOG = LoggerFactory.getLogger(GetComputers.class);

	public void runCommand(Scanner sc) {
	LOG.trace("runCommand(" + sc + ")");
		PageableService<ComputerDTO> computerService = ComputerServiceImpl.INSTANCE;
		NavigationPage<ComputerDTO> page = new NavigationPage<>(computerService, MAX_ITEMS_BY_PAGE, 0);
		System.out.println(page);
		for (int k = 2; k < page.getLastPageNb(); ++k) {
			page.setPageNum(k);
			System.out.println(page);
			sc.nextLine();
		}
	}

}

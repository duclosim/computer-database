package com.excilys.computerDatabase.service.cli.runners;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {
	@Autowired
	private ComputerService computerService;
	
	private static final Logger LOG = LoggerFactory.getLogger(GetComputers.class);

	public void runCommand(Scanner sc) {
	LOG.trace("runCommand(" + sc + ")");
		Page<ComputerDTO> page = new Page<>(computerService, MAX_ITEMS_BY_PAGE, 0);
		System.out.println(page);
		for (int k = 2; k < page.getLastPageNb(); ++k) {
			page.setPageNum(k);
			System.out.println(page);
			sc.nextLine();
		}
	}

}

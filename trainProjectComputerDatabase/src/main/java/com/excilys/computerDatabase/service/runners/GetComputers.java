package main.java.com.excilys.computerDatabase.service.runners;

import java.util.List;
import java.util.Scanner;

import main.java.com.excilys.computerDatabase.model.ComputerBean;
import main.java.com.excilys.computerDatabase.model.pages.Page;
import main.java.com.excilys.computerDatabase.model.pages.PageContainer;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAO;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAOImpl;

/**
 * Cette classe peut lancer la commande de listage des ordinateurs.
 * @author excilys
 *
 */
public class GetComputers implements CommandRunner {

	public void runCommand(Scanner sc) {
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		List<ComputerBean> computers = computerDAO.getAll();
		int startIndex = 0;
		int endIndex = Integer.min(computers.size(), startIndex + PageContainer.NB_ITEM_BY_PAGE);
		while (startIndex < computers.size()) {
			Page<ComputerBean> page = new Page<ComputerBean>(computers.subList(startIndex, endIndex));
			System.out.println(page);
			startIndex = endIndex;
			endIndex = Integer.min(computers.size(), endIndex + PageContainer.NB_ITEM_BY_PAGE);
			sc.nextLine();
		}
	}

}

package main.java.com.excilys.computerDatabase.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Cette classe représente l'interpréteur et invocateur de commandes.
 * @author excilys
 *
 */
public class CLIService {
	private static Map<String, CLICommands> map;
	
	public CLIService() {
		super();
		map = new HashMap<String, CLICommands>();
		map.put("get_companies", CLICommands.GET_COMPANIES);
		map.put("get_computers", CLICommands.GET_COMPUTERS);
		map.put("detail_computer", CLICommands.DETAIL_COMPUTER);
		map.put("create_computer", CLICommands.CREATE_COMPUTER);
		map.put("update_computer", CLICommands.UPDATE_COMPUTER);
		map.put("delete_computer", CLICommands.DELETE_COMPUTER);
		map.put("exit", CLICommands.EXIT);
	}

	/**
	 * Cette méthode interpète la chaîne écrite sur l'entrée standard 
	 * 		et tente d'exécuter l'instruction correspondante.
	 * @param command
	 * @return true if the program is over.
	 */
	public boolean interpretCommand(String command, Scanner sc) {
		if (command == null) {
			throw new IllegalArgumentException("command est à null.");
		}
		CLICommands cliCommands = map.get(command);
		if (cliCommands != null) {
			return cliCommands.runCommand(sc);
		}
		System.out.println("Commande non reconnue.");
		return false;
	}
}

package com.excilys.computerDatabase.service.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Cette classe représente l'interpréteur et invocateur de commandes.
 * @author excilys
 *
 */
public class CLIService {
	private static Map<String, CLICommand> map;
	private static final String UNKNOWN_COMMAND = "Commande non reconnue.";
	private static final Logger LOG = LoggerFactory.getLogger(CLIService.class);
	
	public CLIService() {
		LOG.trace("new CLIService()");
		map = new HashMap<>();
		map.put("get_companies", CLICommand.GET_COMPANIES);
		map.put("get_computers", CLICommand.GET_COMPUTERS);
		map.put("detail_computer", CLICommand.DETAIL_COMPUTER);
		map.put("create_computer", CLICommand.CREATE_COMPUTER);
		map.put("update_computer", CLICommand.UPDATE_COMPUTER);
		map.put("delete_companies", CLICommand.DELETE_COMPANIES);
		map.put("delete_computer", CLICommand.DELETE_COMPUTER);
		map.put("exit", CLICommand.EXIT);
	}

	/**
	 * Cette méthode interpète la chaîne écrite sur l'entrée standard 
	 * 		et tente d'exécuter l'instruction correspondante.
	 * @param command
	 * @return true if the program is over.
	 */
	public boolean interpretCommand(String command, Scanner sc) {
		LOG.trace(new StringBuilder("new interpretCommand(")
			.append(command).append(", ")
			.append(sc).append(")").toString());
		if (command == null) {
			LOG.error("command est à null.");
			throw new IllegalArgumentException("command est à null.");
		}
		if (sc == null) {
			LOG.error("sc est à null.");
			throw new IllegalArgumentException("sc est à null.");
		}
		CLICommand cliCommands = map.get(command);
		if (cliCommands != null) {
			return cliCommands.runCommand(sc);
		}
		System.out.println(UNKNOWN_COMMAND);
		return false;
	}
}

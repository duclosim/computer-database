package com.excilys.computerDatabase.service;

import java.util.HashMap;
import java.util.Map;

public class CLIService {
	private static Map<String, CLICommands> map;
	
	private static volatile CLIService cliService = null;
	
	private CLIService() {
		super();
		map = new HashMap<String, CLICommands>();
		map.put("get companies", CLICommands.GET_COMPANIES);
		map.put("get computers", CLICommands.GET_COMPUTERS);
		map.put("detail computer", CLICommands.DETAIL_COMPUTER);
		map.put("create computer", CLICommands.CREATE_COMPUTER);
		map.put("update computer", CLICommands.UPDATE_COMPUTER);
		map.put("delete computer", CLICommands.DELETE_COMPUTER);
	}

	/**
	 * Cette méthode retourne l'unique instance de cette classe.
	 * @return Une instance de CLIService.
	 */
	public static final CLIService getInstance() {
		if (CLIService.cliService == null) {
			synchronized (CLIService.class) {
				if (CLIService.cliService == null) {
					CLIService.cliService = new CLIService();
				}
			}
		}
		return CLIService.cliService;
	}
	
	/**
	 * 
	 * @param command
	 * @return true if the program is over.
	 */
	public static boolean interpretCommand(String command) {
		if (command == null) {
			throw new IllegalArgumentException("command est à null.");
		}
		CLICommands cliCommands = map.get(command);
		if (cliCommands != null) {
			if (cliCommands == CLICommands.EXIT) {
				return true;
			}
			// TODO
		}
		return false;
	}
}

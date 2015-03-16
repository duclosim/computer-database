package com.excilys.computerDatabase.service;

import java.util.Scanner;

import com.excilys.computerDatabase.service.runners.*;

/**
 * Cette énumération représente les différentes commandes invoquables depuis
 *   l'interface utilisateur.
 * @author excilys
 *
 */
public enum CLICommands {
	GET_COMPANIES("get_companies", false, new GetCompanies()),
	GET_COMPUTERS("get_computers", false, new GetComputers()),
	DETAIL_COMPUTER("detail_computer", false, new DetailComputer()),
	CREATE_COMPUTER("create_computer", false, new CreateComputer()),
	UPDATE_COMPUTER("update_computer", false, new UpdateComputer()),
	DELETE_COMPUTER("delete_computer", false, new DeleteComputer()),
	EXIT("exit", true, new Exit());
	
	private String command;
	private boolean endService;
	private CommandRunner commandRunner;
	
	CLICommands(String command, boolean endService, CommandRunner commandRunner) {
		this.command = command;
		this.endService = endService;
		this.commandRunner = commandRunner;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public boolean getEndService() {
		return this.endService;
	}
	
	public CommandRunner getCommandRunner() {
		return this.commandRunner;
	}
	
	/**
	 * Lance la commande possédée par cette instance de l'énumération.
	 * @param sc Un scanner sur l'entrée lue, 
	 *   qui sera utilisé par certaines commandes.
	 * @return Un booléen indiquant si la commande appelée met fin au programme.
	 */
	public boolean runCommand(Scanner sc) {
		getCommandRunner().runCommand(sc);
		return getEndService();
	}
}

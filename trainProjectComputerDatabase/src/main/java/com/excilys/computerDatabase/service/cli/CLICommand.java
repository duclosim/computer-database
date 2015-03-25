package com.excilys.computerDatabase.service.cli;

import com.excilys.computerDatabase.service.cli.runners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Cette énumération représente les différentes commandes invoquables depuis
 *   l'interface utilisateur.
 * @author excilys
 *
 */
public enum CLICommand {
	GET_COMPANIES("get_companies", false, new GetCompanies()),
	GET_COMPUTERS("get_computers", false, new GetComputers()),
	DETAIL_COMPUTER("detail_computer", false, new DetailComputer()),
	CREATE_COMPUTER("create_computer", false, new CreateComputer()),
	UPDATE_COMPUTER("update_computer", false, new UpdateComputer()),
	DELETE_COMPANIES("delete_company", false, new DeleteCompany()),
	DELETE_COMPUTER("delete_computer", false, new DeleteComputer()),
	EXIT("exit", true, new Exit());
	
	private static final Logger LOG = LoggerFactory.getLogger(CLICommand.class);
	
	private String command;
	private boolean endService;
	private CommandRunner commandRunner;
	
	CLICommand(String command, boolean endService, CommandRunner commandRunner) {
		this.command = command;
		this.endService = endService;
		this.commandRunner = commandRunner;
	}
	
	public String getCommand() {
		LOG.trace("getCommand()");
		return this.command;
	}
	
	public boolean getEndService() {
		LOG.trace("getEndService()");
		return this.endService;
	}
	
	public CommandRunner getCommandRunner() {
		LOG.trace("getCommandRunner()");
		return this.commandRunner;
	}
	
	/**
	 * Lance la commande possédée par cette instance de l'énumération.
	 * @param sc Un scanner sur l'entrée lue, 
	 *   qui sera utilisé par certaines commandes.
	 * @return Un booléen indiquant si la commande appelée met fin au programme.
	 */
	public boolean runCommand(Scanner sc) {
		LOG.trace("runCommand(" + sc + ")");
		if (sc == null) {
			LOG.error("sc est à null.");
			throw new IllegalArgumentException("sc est à null.");
		}
		getCommandRunner().runCommand(sc);
		return getEndService();
	}
}

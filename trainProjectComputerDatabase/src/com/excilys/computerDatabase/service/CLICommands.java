package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.service.runners.*;

public enum CLICommands {
	GET_COMPANIES("get companies", false, new GetCompanies()),
	GET_COMPUTERS("get computers", false, new GetComputers()),
	DETAIL_COMPUTER("detail computer", false, new DetailComputer()),
	CREATE_COMPUTER("create computer", false, new CreateComputer()),
	UPDATE_COMPUTER("update computer", false, new UpdateComputer()),
	DELETE_COMPUTER("delete computer", false, new DeleteComputer()),
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
	
	public boolean runCommand() {
		getCommandRunner().runCommand();
		return getEndService();
	}
}

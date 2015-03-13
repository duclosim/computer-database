package com.excilys.computerDatabase.service;

public enum CLICommands {
	GET_COMPANIES("get companies"),
	GET_COMPUTERS("get computers"),
	DETAIL_COMPUTER("detail computer"),
	CREATE_COMPUTER("create computer"),
	UPDATE_COMPUTER("update computer"),
	DELETE_COMPUTER("delete computer"),
	EXIT("exit");
	
	private String command;
	
	CLICommands(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public void runCommand() {
		// TODO
	}
}

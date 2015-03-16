package com.excilys.computerDatabase.service.runners;

public class Exit implements CommandRunner {

	@Override
	public void runCommand() {
		System.out.println("Fin du programme");
	}

}

package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

public class Exit implements CommandRunner {

	@Override
	public void runCommand(Scanner sc) {
		System.out.println("Fin du programme");
	}

}

package com.excilys.computerDatabase.userInterface;

import java.util.Scanner;

import com.excilys.computerDatabase.service.CLIService;

public class ComputerDatabaseCLI {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		boolean terminated = false;
        while(sc.hasNextLine() && !terminated) {
        	terminated = CLIService.interpretCommand(sc.next());
        }
        sc.close();
	}
}

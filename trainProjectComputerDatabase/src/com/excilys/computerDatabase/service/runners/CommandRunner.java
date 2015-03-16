package com.excilys.computerDatabase.service.runners;

import java.util.Scanner;

public interface CommandRunner {
	public static final int NB_ITEM_BY_PAGE = 10;
	void runCommand(Scanner sc);
}

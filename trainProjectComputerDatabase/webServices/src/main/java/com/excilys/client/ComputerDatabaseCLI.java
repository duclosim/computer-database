package com.excilys.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * Cette classe lit les commandes entrées en ligne de commande et les exécute.
 * @author excilys
 *
 */
class ComputerDatabaseCLI {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDatabaseCLI.class);
	@Autowired
	private MessageSource messageSource;
	
	public static void main(String args[]) {
		LOG.info("main(" + Arrays.toString(args) + ")");
		Scanner sc = new Scanner(System.in);
		boolean terminated = false;
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:console-context.xml");
		CLIWSClient cliService = ctx.getBean(CLIWSClient.class);
		MessageSource messageSource = ctx.getBean(MessageSource.class);
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println(messageSource.getMessage("console.welcome", null, locale));
        while(sc.hasNextLine() && !terminated) {
        	terminated = cliService.interpretCommand(sc.next(), sc);
        }
        sc.close();
	}
}

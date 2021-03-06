package com.excilys.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * This method launches the Computer Database CLI which uses 
 *   a Jax-WS to get data.
 * @author excilys
 *
 */
class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String args[]) {
		LOG.trace("main(" + Arrays.toString(args) + ")");
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

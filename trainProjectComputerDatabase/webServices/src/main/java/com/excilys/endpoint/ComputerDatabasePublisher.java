package com.excilys.endpoint;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.ws.ComputerDatabaseWS;

// Endpoint publisher
public class ComputerDatabasePublisher {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDatabasePublisher.class);
	
	public static void main(String[] args) {
		LOG.trace("main");
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ws-context.xml");
		ComputerDatabaseWS ws = ctx.getBean(ComputerDatabaseWS.class);
		Endpoint.publish(ComputerDatabaseWS.ENDPOINT, ws);
	}
}

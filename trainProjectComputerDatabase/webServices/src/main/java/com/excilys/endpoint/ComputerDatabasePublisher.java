package com.excilys.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.ws.ComputerDatabaseWS;

// Endpoint publisher
public class ComputerDatabasePublisher {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ws-context.xml");
		ComputerDatabaseWS ws = ctx.getBean(ComputerDatabaseWS.class);
		Endpoint.publish("http://localhost:9999/computer-database-ws/computers", ws);
	}
}

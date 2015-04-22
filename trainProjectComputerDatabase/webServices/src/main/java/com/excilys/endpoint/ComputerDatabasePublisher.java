package com.excilys.endpoint;

import javax.xml.ws.Endpoint;

import com.excilys.ws.ComputerDatabaseWSImpl;

// Endpoint publisher
public class ComputerDatabasePublisher {
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9999/computer-database-ws/computers", 
				new ComputerDatabaseWSImpl());
	}
}

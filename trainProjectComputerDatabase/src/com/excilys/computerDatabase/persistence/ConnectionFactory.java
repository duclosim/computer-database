package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Cette classe donne des instances de Connection.
 * @author excilys
 *
 */
public class ConnectionFactory {
	// URL de la source de données
	private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db";
	// login et mot de passe pour accéder à la base de données.
	private static final String LOGIN = "admincdb";
	private static final String PASS = "qwerty1234";
	
	static {
		// Chargement du Driver et enregistrement auprès du DriverManager
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ConnectionFactory() {}
	
	/**
	 * Retourne une connection à la base de données.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static final Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, LOGIN, PASS);
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("Erreur : connection à la base de "
					+ "données impossible.");
		}
		return connection;
	}
	
	public static final void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erreur : impossible de fermer la "
					+ "connection à la base de données.");
			e.printStackTrace();
		}
	}
}

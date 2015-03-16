package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
public class ConnectionFactory {
	// URL de la source de données
	private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
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
	 * @return L'instance de connexion à la base de données.
	 * @throws ClassNotFoundException si on ne trouve pas le Driver 
	 *   de la base de données.
	 * @throws SQLException si on ne parvient pas à se connecter 
	 *   à la base de données.
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
	
	/**
	 * Ferme la connexion à la base de données.
	 * @param connection La connexion à refermer.
	 */
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

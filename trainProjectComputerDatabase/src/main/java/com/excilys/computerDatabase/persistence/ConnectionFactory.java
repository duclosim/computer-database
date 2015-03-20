package com.excilys.computerDatabase.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Driver;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
public enum ConnectionFactory {
	INSTANCE;
	
	private static final String PROPERTIES_FILE = "./db.properties";
	private String url;
	private String user;
	private String password;

	private ConnectionFactory() {
		// Chargement du Driver et enregistrement auprès du DriverManager
		try {
			Driver driver = new com.mysql.jdbc.Driver();
			DriverManager.registerDriver(driver);
			Properties configProp = new Properties();
			ClassLoader cls = Thread.currentThread().getContextClassLoader();
			InputStream ips = cls.getResourceAsStream(PROPERTIES_FILE);
			configProp.load(ips);
			url = configProp.getProperty("db.url");
			user = configProp.getProperty("db.username");
			password = configProp.getProperty("db.password");
		} catch (SQLException e) {
			System.err.println("Pas possible de se connecter à la bdd.");
			e.printStackTrace();
			throw new IllegalStateException("Problème de connexion.");
		} catch (IOException e) {
			System.err.println("Fichier de propriétés non trouvé.");
			e.printStackTrace();
			throw new IllegalStateException("Pas de fichier de propriété.");
		}
	}
	
	/**
	 * Retourne une connection à la base de données.
	 * @return L'instance de connexion à la base de données.
	 * @throws ClassNotFoundException si on ne trouve pas le Driver 
	 *   de la base de données.
	 * @throws SQLException si on ne parvient pas à se connecter 
	 *   à la base de données.
	 */
	public final Connection getConnection() {
		try {
			return (Connection) DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println("Pas possible de se connecter à la bdd.");
			e.printStackTrace();
			throw new IllegalStateException("Problème de connexion.");
		}
	}
	
	/**
	 * Ferme la connexion à la base de données.
	 * @param connection La connexion à refermer.
	 */
	public static final void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("Erreur : impossible de fermer la "
					+ "connection à la base de données.");
			e.printStackTrace();
		}
	}
}

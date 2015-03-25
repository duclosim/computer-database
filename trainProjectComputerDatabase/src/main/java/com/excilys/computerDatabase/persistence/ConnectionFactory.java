package com.excilys.computerDatabase.persistence;

import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
public enum ConnectionFactory {
	INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
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
		LOG.trace("getConnection()");
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			LOG.error("Pas possible de se connecter à la bdd.");
			e.printStackTrace();
			throw new IllegalStateException("Problème de connexion.");
		}
	}
	
	/**
	 * Ferme la connexion à la base de données.
	 * @param connection La connexion à refermer.
	 */
	public final void closeConnection(Connection connection) {
		LOG.trace("closeConnection(" + connection + ")");
		try {
			connection.close();
		} catch (SQLException e) {
			LOG.error("Erreur : impossible de fermer la "
					+ "connection à la base de données.");
			e.printStackTrace();
		}
	}

    public final void closeConnectionAndStatementAndResults(Connection connection,
			Statement statement, ResultSet results) {
		LOG.trace("closeConnection(" 
			+ connection + ", " 
			+ statement + ", "
			+ results + ")");
		try {
			connection.close();
			statement.close();
			results.close();
		} catch (SQLException e) {
			LOG.error("Erreur : impossible de fermer la "
					+ "connection à la base de données.");
			e.printStackTrace();
		}
	}
}

package main.java.com.excilys.computerDatabase.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
public enum ConnectionFactory {
	INSTANCE;
	
	private static final String PROPERTIES_FILE = "src/main/resources/db.properties";

	private ConnectionFactory() {
		// Chargement du Driver et enregistrement auprès du DriverManager
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Pas possible de charger le driver jdbc.");
			e.printStackTrace();
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
			Properties configProp = new Properties();
			InputStream ips = new FileInputStream(PROPERTIES_FILE);
			configProp.load(ips);
			String url = configProp.getProperty("db.url");
			String user = configProp.getProperty("db.username");
			String password = configProp.getProperty("db.password");
			return (Connection) DriverManager.getConnection(url, user, password);
		} catch (FileNotFoundException e) {
			System.err.println("Fichier de propriétés non trouvé.");
			e.printStackTrace();
			throw new IllegalStateException("Pas de fichier de propriété.");
		} catch (IOException e) {
			System.err.println("Fichier de propriétés non lisible.");
			e.printStackTrace();
			throw new IllegalStateException("Problème de connexion.");
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

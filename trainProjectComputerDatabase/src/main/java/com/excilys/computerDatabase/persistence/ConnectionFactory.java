package com.excilys.computerDatabase.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
public enum ConnectionFactory {
	INSTANCE;

	private final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
	private static final String PROPERTIES_FILE = "./db.properties";

    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "username";
    private static final String PROPERTY_MOT_DE_PASSE    = "password";
	
	private BoneCP connectionPool;
	
	private ConnectionFactory() {
		LOG.trace("new ConnectionFactory()");
		// Chargement du Driver et enregistrement auprès du DriverManager
		try {
			Properties configProp = new Properties();
			ClassLoader cls = Thread.currentThread().getContextClassLoader();
			InputStream ips = cls.getResourceAsStream(PROPERTIES_FILE);
			configProp.load(ips);
			String url = configProp.getProperty(PROPERTY_URL);
			String user = configProp.getProperty(PROPERTY_NOM_UTILISATEUR);
			String password = configProp.getProperty(PROPERTY_MOT_DE_PASSE);
			Class.forName(configProp.getProperty(PROPERTY_DRIVER));
			// Création du pool de connection
			BoneCPConfig config = new BoneCPConfig();
            /* Mise en place de l'URL, du nom et du mot de passe */
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            /* Paramétrage de la taille du pool */
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(2);
            /* Création du pool à partir de la configuration, via l'objet BoneCP */
            connectionPool = new BoneCP(config);
		} catch (IOException e) {
			LOG.error("Fichier de propriétés non trouvé.");
			e.printStackTrace();
			throw new IllegalStateException("Pas de fichier de propriété.");
		} catch (ClassNotFoundException e) {
			LOG.error("Impossible de charger le driver.");
			e.printStackTrace();
			throw new IllegalStateException("Impossible de charger le driver.");
		} catch (SQLException e) {
			LOG.error("Pas possible de créer le pool de connections.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de créer le pool de connections.");
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
			return connectionPool.getConnection();
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

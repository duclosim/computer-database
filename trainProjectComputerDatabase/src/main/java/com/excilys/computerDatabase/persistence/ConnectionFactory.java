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
	private final String PROPERTIES_FILE = "./db.properties";

    private final String PROPERTY_URL		= "url";
    private final String PROPERTY_DRIVER	= "driver";
    private final String PROPERTY_USER_NAME	= "username";
    private final String PROPERTY_PASSWORD	= "password";
	private final ThreadLocal<Connection> thread = new ThreadLocal<>(); 

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
			String user = configProp.getProperty(PROPERTY_USER_NAME);
			String password = configProp.getProperty(PROPERTY_PASSWORD);
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
			Connection res = thread.get();
			if (res == null) {
				res = connectionPool.getConnection();
				thread.set(res);
			}
		} catch (SQLException e) {
			LOG.error("Pas possible de prendre une connection dans le pool de connections.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de prendre une connection dans le pool de connections.");
		}
		return thread.get();
	}
	
	public final void startTransaction() {
		LOG.trace("startTransaction()");
		try {
			thread.get().setAutoCommit(false);
		} catch (SQLException e) {
			LOG.error("Problème interne à la bdd.");
			e.printStackTrace();
			throw new IllegalStateException("Problème interne à la bdd.");
		}
	}
	
	public final void commit() throws SQLException {
		LOG.trace("commit()");
		thread.get().commit();
		thread.get().setAutoCommit(true);
	}
	
	public final void rollback() {
		LOG.trace("rollback()");
		try {
			thread.get().rollback();
		} catch (SQLException e) {
			LOG.error("Impossible de rollback.");
			e.printStackTrace();
			throw new IllegalStateException("Impossible de rollback.");
		}
	}
	
	/**
	 * Ferme la connexion à la base de données.
	 * @param connection La connexion à refermer.
	 */
	public final void closeConnection() {
		LOG.trace("closeConnection()");
		try {
			thread.get().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public final void closeConnectionAndStatementAndResults(Statement statement, 
    		ResultSet results) {
		LOG.trace("closeConnection(" 
			+ statement + ", "
			+ results + ")");
		try {
			closeConnection();
			statement.close();
			results.close();
		} catch (SQLException e) {
			LOG.error("Erreur : impossible de fermer la "
					+ "connection à la base de données.");
			e.printStackTrace();
		}
	}
}

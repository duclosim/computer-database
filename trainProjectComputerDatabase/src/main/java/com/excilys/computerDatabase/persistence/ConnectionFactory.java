package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cette classe donne des instances de Connection afin de se 
 *   connecter à la base de données.
 * @author excilys
 *
 */
@Component
public class ConnectionFactory {
	private final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
	private final ThreadLocal<Connection> thread = new ThreadLocal<>();
	
	@Autowired
	private BasicDataSource dataSource;
	
	/**
	 * Retourne une connection à la base de données.
	 * @return L'instance de connexion à la base de données.
	 * @throws ClassNotFoundException si on ne trouve pas le Driver 
	 *   de la base de données.
	 * @throws SQLException si on ne parvient pas à se connecter 
	 *   à la base de données.
	 */
	public final Connection getConnection() {
		LOG.info("getConnection()");
		Connection res = null;
		try {
			res = thread.get();
			if ((res == null) || (res.isClosed())) {
				res = dataSource.getConnection();
				thread.set(res);
			}
		} catch (SQLException e) {
			LOG.error("Pas possible de prendre une connection dans le pool de connections.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de prendre une connection dans le pool de connections.");
		}
		return res;
	}
	
	public final void closeConnection() {
		try {
			thread.get().close();
		} catch (SQLException e) {
			LOG.error("Pas possible de rendre la connection au pool de connections.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de rendre la connection au pool de connections.");
		}
	}
	
	public final void startTransaction() {
		try {
			thread.get().setAutoCommit(false);
		} catch (SQLException e) {
			LOG.error("Pas possible de commencer la transaction.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de commencer la transaction.");
		}
	}
	
	public final void commit() {
		try {
			thread.get().commit();
			thread.get().setAutoCommit(true);
		} catch (SQLException e) {
			LOG.error("Pas possible de terminer la transaction.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de terminer la transaction.");
		}
	}
	
	public final void rollback() {
		try {
			thread.get().rollback();
		} catch (SQLException e) {
			LOG.error("Pas possible de rollback la transaction.");
			e.printStackTrace();
			throw new IllegalStateException("Pas possible de rollback la transaction.");
		}
	}
}

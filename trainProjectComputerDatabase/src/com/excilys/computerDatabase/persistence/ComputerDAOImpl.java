package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum ComputerDAOImpl implements ComputerDAO {
	INSTANCE;
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";
	public static final String INTRODUCED_COLUMN_LABEL = "introduced";
	public static final String DISCONTINUED_COLUMN_LABEL = "discontinued";
	public static final String COMPANY_ID_COLUMN_LABEL = "company_id";
	
	@Override
	public ComputerBean getById(Long id) {
		ComputerBean result = null;
		String query = "SELECT * FROM computer WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				result = ComputerMapper.mapComputer(results);
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
		return result;
	}

	@Override
	public List<ComputerBean> getAll() {
		Connection con = ConnectionFactory.getConnection();
		List<ComputerBean> result = new ArrayList<ComputerBean>();
		String query = "SELECT * FROM computer;";
		ResultSet results;
		
		Statement statement;
		try {
			statement = con.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				result.add(ComputerMapper.mapComputer(results));
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
		return result;
	}

	@Override
	public void createComputer(ComputerBean computerBean) {
		if (computerBean.getName() == null) {
			throw new IllegalArgumentException("computer.name est à null.");
		}
		String query = new StringBuilder("INSERT INTO computer (")
		.append(NAME_COLUMN_LABEL)
		.append(", ")
		.append(INTRODUCED_COLUMN_LABEL)
		.append(", ")
		.append(DISCONTINUED_COLUMN_LABEL)
		.append(", ")
		.append(COMPANY_ID_COLUMN_LABEL)
		.append(") VALUES (?, ?, ?, ?);")
		.toString();
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int colNb = 0;
			Timestamp introduced = null;
			Timestamp discontinued = null;
			if (computerBean.getIntroduced() != null) {
				introduced = Timestamp.valueOf(computerBean.getIntroduced());
			}
			if (computerBean.getDiscontinued() != null) {
				discontinued = Timestamp.valueOf(computerBean.getDiscontinued());
			}
			ps.setString(++colNb, computerBean.getName());
			ps.setTimestamp(++colNb, introduced);
			ps.setTimestamp(++colNb, discontinued);
			ps.setLong(++colNb, computerBean.getCompany().getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public void updateComputer(ComputerBean computerBean) {
		if (computerBean.getName() == null) {
			throw new IllegalArgumentException("computer.name est à null.");
		}
		String query = new StringBuilder("UPDATE computer SET ")
			.append(NAME_COLUMN_LABEL)
			.append("=?, ")
			.append(INTRODUCED_COLUMN_LABEL)
			.append("=?, ")
			.append(DISCONTINUED_COLUMN_LABEL)
			.append("=?, ")
			.append(COMPANY_ID_COLUMN_LABEL)
			.append("=? WHERE id = ?;")
			.toString();
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int colNb = 0;
			Timestamp introduced = null;
			Timestamp discontinued = null;
			if (computerBean.getIntroduced() != null) {
				introduced = Timestamp.valueOf(computerBean.getIntroduced());
			}
			if (computerBean.getDiscontinued() != null) {
				discontinued = Timestamp.valueOf(computerBean.getDiscontinued());
			}
			// name
			ps.setString(++colNb, computerBean.getName());
			// introduced date
			ps.setTimestamp(++colNb, introduced);
			// discontinued date
			ps.setTimestamp(++colNb, discontinued);
			// company id
			ps.setLong(++colNb, computerBean.getCompany().getId());
			// id
			ps.setLong(++colNb, computerBean.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public void deleteComputer(ComputerBean computerBean) {
		if (computerBean == null) {
			throw new IllegalArgumentException("computerBean est à null.");
		}
		String query = "DELETE FROM computer WHERE id=?";
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			if (computerBean.getId() != null) {
				ps.setLong(1, computerBean.getId());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de suppression bdd");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
}

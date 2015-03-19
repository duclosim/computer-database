package main.java.com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.mappers.ComputerMapper;

/**
 * Cette classe implémente ComputerDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum ComputerDAOImpl implements ComputerDAO {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";
	public static final String INTRODUCED_COLUMN_LABEL = "introduced";
	public static final String DISCONTINUED_COLUMN_LABEL = "discontinued";
	public static final String COMPANY_ID_COLUMN_LABEL = "company_id";
	
	public ComputerBean getById(Long id) {
		LOG.trace("getById(" + id + ")");
		ComputerBean result = null;
		String query = "SELECT * FROM computer WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, id);
			results = ps.executeQuery();
			if (results.next()) {
				result = ComputerMapper.INSTANCE.mapComputer(results);
			}
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	public List<ComputerBean> getAll(int limit, int offset) {
		LOG.trace("getAll(" + limit + ", " + offset + ")");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		List<ComputerBean> result = new ArrayList<ComputerBean>();
		String query = "SELECT * FROM computer LIMIT ? OFFSET ?;";
		ResultSet results;
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(ComputerMapper.INSTANCE.mapComputer(results));
			}
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public int countLines() {
		LOG.trace("countLine()");
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		String query = "SELECT COUNT(*) FROM computer;";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return results.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	public void create(ComputerBean entity) {
		LOG.trace("create(" + entity + ")");
		if (entity.getName() == null) {
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
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int colNb = 0;
			Timestamp introduced = null;
			Timestamp discontinued = null;
			if (entity.getIntroduced() != null) {
				introduced = Timestamp.valueOf(entity.getIntroduced());
			}
			if (entity.getDiscontinued() != null) {
				discontinued = Timestamp.valueOf(entity.getDiscontinued());
			}
			ps.setString(++colNb, entity.getName());
			ps.setTimestamp(++colNb, introduced);
			ps.setTimestamp(++colNb, discontinued);
			ps.setLong(++colNb, entity.getCompany().getId());
			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            } else {
            	throw new SQLException();
            }
		} catch (SQLException e) {
			System.err.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}		
	}

	public void update(ComputerBean entity) {
		LOG.trace("update(" + entity + ")");
		if (entity.getName() == null) {
			throw new IllegalArgumentException("computer.name est à null.");
		}
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			StringBuilder query = new StringBuilder("UPDATE computer SET ")
			.append(NAME_COLUMN_LABEL)
			.append("=?, ")
			.append(INTRODUCED_COLUMN_LABEL)
			.append("=?, ")
			.append(DISCONTINUED_COLUMN_LABEL)
			.append("=?");
			if (entity.getCompany() != null) {
				query.append(", ")
				.append(COMPANY_ID_COLUMN_LABEL)
				.append("=?");
			}
			query.append(" WHERE id = ?;");
			PreparedStatement ps = con.prepareStatement(query.toString());
			int colNb = 0;
			Timestamp introduced = null;
			Timestamp discontinued = null;
			if (entity.getIntroduced() != null) {
				introduced = Timestamp.valueOf(entity.getIntroduced());
			}
			if (entity.getDiscontinued() != null) {
				discontinued = Timestamp.valueOf(entity.getDiscontinued());
			}
			// name
			ps.setString(++colNb, entity.getName());
			// introduced date
			ps.setTimestamp(++colNb, introduced);
			// discontinued date
			ps.setTimestamp(++colNb, discontinued);
			// company id
			if (entity.getCompany() != null) {
				ps.setLong(++colNb, entity.getCompany().getId());
			}
			// id
			ps.setLong(++colNb, entity.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	public void delete(ComputerBean entity) {
		LOG.trace("delete(" + entity + ")");
		if (entity == null) {
			throw new IllegalArgumentException("computerBean est à null.");
		}
		String query = "DELETE FROM computer WHERE id=?";
		Connection con = ConnectionFactory.INSTANCE.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			if (entity.getId() != null) {
				ps.setLong(1, entity.getId());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println("Erreur : problème de suppression bdd");
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
}

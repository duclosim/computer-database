package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.model.ComputerBean;

public class ComputerDAOImpl implements ComputerDAO {
	private static volatile ComputerDAOImpl computerDAOImpl = null;
	
	private static final String ID_COLUMN_LABEL = "id";
	private static final String NAME_COLUMN_LABEL = "name";
	private static final String INTRODUCED_COLUMN_LABEL = "introduced";
	private static final String DISCONTINUED_COLUMN_LABEL = "discontinued";
	private static final String COMPANY_ID_COLUMN_LABEL = "company_id";
	
	private ComputerDAOImpl() {
		super();
	}
	
	public static final ComputerDAO getInstance() {
		if (ComputerDAOImpl.computerDAOImpl == null) {
			synchronized (ComputerDAOImpl.class) {
				if (ComputerDAOImpl.computerDAOImpl == null) {
					ComputerDAOImpl.computerDAOImpl = new ComputerDAOImpl();
				}
			}
		}
		return ComputerDAOImpl.computerDAOImpl;
	}

	@Override
	public ComputerBean getById(long id) {
		ComputerBean result = null;
		String query = "SELECT * FROM computer WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			LocalDateTime introducedDate = null;
			LocalDateTime discontinuedDate = null;
			if (results.getTimestamp(INTRODUCED_COLUMN_LABEL) != null) {
				introducedDate = results.getTimestamp(INTRODUCED_COLUMN_LABEL).toLocalDateTime();
			}
			if (results.getTimestamp(DISCONTINUED_COLUMN_LABEL) != null) {
				discontinuedDate = results.getTimestamp(DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
			}
			result = new ComputerBean(results.getLong(ID_COLUMN_LABEL), 
					results.getString(NAME_COLUMN_LABEL), 
					introducedDate,
					discontinuedDate,
					results.getLong(COMPANY_ID_COLUMN_LABEL));
		} catch (SQLException e) {
			System.out.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
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
				LocalDateTime introducedDate = null;
				LocalDateTime discontinuedDate = null;
				if (results.getTimestamp(INTRODUCED_COLUMN_LABEL) != null) {
					introducedDate = results.getTimestamp(INTRODUCED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getTimestamp(DISCONTINUED_COLUMN_LABEL) != null) {
					discontinuedDate = results.getTimestamp(DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
				}
				result.add(new ComputerBean(results.getLong(ID_COLUMN_LABEL), 
						results.getString(NAME_COLUMN_LABEL), 
						introducedDate,
						discontinuedDate,
						results.getLong(COMPANY_ID_COLUMN_LABEL)));
			}
		} catch (SQLException e) {
			System.out.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
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
		String query = "INSERT INTO computer (?, ?, ?, ?, ?) VALUES (?, ?, ?, ?, ?);";
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			// Column names
			int colNb = 0;
			ps.setString(++colNb, NAME_COLUMN_LABEL);
			ps.setString(++colNb, INTRODUCED_COLUMN_LABEL);
			ps.setString(++colNb, DISCONTINUED_COLUMN_LABEL);
			ps.setString(++colNb, COMPANY_ID_COLUMN_LABEL);
			// Values
			ps.setString(++colNb, computerBean.getName());
			ps.setTimestamp(++colNb, Timestamp.valueOf(computerBean.getIntroduced()));
			ps.setTimestamp(++colNb, Timestamp.valueOf(computerBean.getDiscontinued()));
			ps.setLong(++colNb, computerBean.getCompanyId());
			ps.execute();
		} catch (SQLException e) {
			System.out.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public void updateComputer(ComputerBean computerBean) {
		if (computerBean.getName() == null) {
			throw new IllegalArgumentException("computer.name est à null.");
		}
		String query = "UPDATE computer SET ? = ?, ? = ?, ? = ?, ? = ?, ? = ?) WHERE id=?;";
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int colNb = 0;
			// name
			ps.setString(++colNb, NAME_COLUMN_LABEL);
			ps.setString(++colNb, computerBean.getName());
			// introduced date
			ps.setString(++colNb, INTRODUCED_COLUMN_LABEL);
			ps.setTimestamp(++colNb, Timestamp.valueOf(computerBean.getIntroduced()));
			// discontinued date
			ps.setString(++colNb, DISCONTINUED_COLUMN_LABEL);
			ps.setTimestamp(++colNb, Timestamp.valueOf(computerBean.getDiscontinued()));
			// company id
			ps.setString(++colNb, COMPANY_ID_COLUMN_LABEL);
			ps.setLong(++colNb, computerBean.getCompanyId());
			// id
			ps.setLong(++colNb, computerBean.getId());
			ps.execute();
		} catch (SQLException e) {
			System.out.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public void deleteComputer(ComputerBean computerBean) {
		String query = "DELETE FROM computer WHERE id=?";
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, computerBean.getId());
		} catch (SQLException e) {
			System.out.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
}

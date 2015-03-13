package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
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
	public ComputerBean getById(Long id) {
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
			Long companyId = null;
			if (results.next()) {
				if (results.getTimestamp(INTRODUCED_COLUMN_LABEL) != null) {
					introducedDate = results.getTimestamp(INTRODUCED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getTimestamp(DISCONTINUED_COLUMN_LABEL) != null) {
					discontinuedDate = results.getTimestamp(DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getLong(COMPANY_ID_COLUMN_LABEL) != 0) {
					companyId = results.getLong(COMPANY_ID_COLUMN_LABEL);
				}
				result = new ComputerBean(results.getLong(ID_COLUMN_LABEL), 
						results.getString(NAME_COLUMN_LABEL), 
						introducedDate,
						discontinuedDate,
						companyId);
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
				Long companyId = null;
				if (results.getTimestamp(INTRODUCED_COLUMN_LABEL) != null) {
					introducedDate = results.getTimestamp(INTRODUCED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getTimestamp(DISCONTINUED_COLUMN_LABEL) != null) {
					discontinuedDate = results.getTimestamp(DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
				}
				if (results.getLong(COMPANY_ID_COLUMN_LABEL) != 0) {
					companyId = results.getLong(COMPANY_ID_COLUMN_LABEL);
				}
				result.add(new ComputerBean(results.getLong(ID_COLUMN_LABEL), 
						results.getString(NAME_COLUMN_LABEL), 
						introducedDate,
						discontinuedDate,
						companyId));
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
			ps.setLong(++colNb, computerBean.getCompanyId());
			ps.executeUpdate();
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
			ps.setLong(++colNb, computerBean.getCompanyId());
			// id
			ps.setLong(++colNb, computerBean.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
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
			System.out.println("Erreur : problème d'écriture bdd");
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
}

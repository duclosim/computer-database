package main.java.com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.model.CompanyBean;
import main.java.com.excilys.computerDatabase.persistence.mappers.CompanyMapper;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public enum CompanyDAOImpl implements CompanyDAO {
	INSTANCE;
	
	public static final String ID_COLUMN_LABEL = "id";
	public static final String NAME_COLUMN_LABEL = "name";

	public CompanyBean getById(Long id) {
		CompanyBean result = null;
		String query = "SELECT * FROM company WHERE id=?;";
		ResultSet results;
		Connection con = ConnectionFactory.getConnection();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, id);
			results = ps.executeQuery();
			if (results.next()) {
				result = CompanyMapper.mapCompany(results);
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
	
	public List<CompanyBean> getAll() {
		Connection con = ConnectionFactory.getConnection();
		List<CompanyBean> result = new ArrayList<CompanyBean>();
		String query = "SELECT * FROM company;";
		ResultSet results;
		
		Statement statement;
		try {
			statement = con.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				result.add(new CompanyBean(results.getLong(ID_COLUMN_LABEL), 
						results.getString(NAME_COLUMN_LABEL)));
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
}

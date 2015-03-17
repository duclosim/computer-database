package main.java.com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.persistence.ConnectionFactory;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.mappers.CompanyMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}
	
	public List<CompanyBean> getAll(int limit, int offset) {
		Connection con = ConnectionFactory.getConnection();
		List<CompanyBean> result = new ArrayList<CompanyBean>();
		String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
		ResultSet results;
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			int paramIndex = 0;
			ps.setLong(++paramIndex, limit);
			ps.setLong(++paramIndex, offset);
			results = ps.executeQuery();
			while (results.next()) {
				result.add(new CompanyBean(results.getLong(ID_COLUMN_LABEL), 
						results.getString(NAME_COLUMN_LABEL)));
			}
			return result;
		} catch (SQLException e) {
			System.err.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	@Override
	public int countLines() {
		Connection con = ConnectionFactory.getConnection();
		String query = "SELECT COUNT(*) FROM company;";
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
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.closeConnection(con);
		}
	}

	public void create(CompanyBean entity) {
		throw new NotImplementedException();
		
	}

	public void update(CompanyBean entity) {
		throw new NotImplementedException();
		
	}

	public void delete(CompanyBean entity) {
		throw new NotImplementedException();
		
	}
}

package com.excilys.computerDatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.model.CompanyBean;

/**
 * Cette classe implémente CompanyDAO et utilise le design pattern Singleton.
 * @author excilys
 *
 */
public final class CompanyDAOImpl implements CompanyDAO {
	private static volatile CompanyDAOImpl companyDAOImpl = null;
	
	private static final String ID_COLUMN_LABEL = "id";
	private static final String NAME_COLUMN_LABEL = "name";
	
	private CompanyDAOImpl() {
		super();
	}

	/**
	 * Cette méthode retourne l'unique instance de cette classe.
	 * @return Une instance de CompanyDAOImpl.
	 */
	public static final CompanyDAO getInstance() {
		if (CompanyDAOImpl.companyDAOImpl == null) {
			synchronized (CompanyDAOImpl.class) {
				if (CompanyDAOImpl.companyDAOImpl == null) {
					CompanyDAOImpl.companyDAOImpl = new CompanyDAOImpl();
				}
			}
		}
		return CompanyDAOImpl.companyDAOImpl;
	}

	@Override
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
				result = new CompanyBean(results.getLong(ID_COLUMN_LABEL),
						results.getString(NAME_COLUMN_LABEL));
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
			System.out.println("Erreur : problème de lecture bdd");
			e.printStackTrace();
		} finally {
			ConnectionFactory.closeConnection(con);
		}
		return result;
	}
}

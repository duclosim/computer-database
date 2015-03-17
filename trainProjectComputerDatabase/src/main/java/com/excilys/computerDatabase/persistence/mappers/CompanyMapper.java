package main.java.com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

public enum CompanyMapper {
	INSTANCE;

	public static CompanyBean mapCompany(ResultSet results) {
		CompanyBean companyBean = null;
		try {
			companyBean = new CompanyBean(results.getLong(CompanyDAOImpl.ID_COLUMN_LABEL),
					results.getString(CompanyDAOImpl.NAME_COLUMN_LABEL));
			return companyBean;
		} catch (SQLException e) {
			System.err.println("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}
}

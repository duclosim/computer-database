package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerDatabase.model.CompanyBean;
import com.excilys.computerDatabase.persistence.CompanyDAOImpl;

public enum CompanyMapper {
	INSTANCE;

	public static CompanyBean mapCompany(ResultSet results) throws SQLException {
		CompanyBean companyBean = null;
		try {
			companyBean = new CompanyBean(results.getLong(CompanyDAOImpl.ID_COLUMN_LABEL),
					results.getString(CompanyDAOImpl.NAME_COLUMN_LABEL));
			return companyBean;
		} catch (SQLException e) {
			System.err.println("erreur de lecture d'une colonne");
			e.printStackTrace();
			throw e;
		}
	}
}

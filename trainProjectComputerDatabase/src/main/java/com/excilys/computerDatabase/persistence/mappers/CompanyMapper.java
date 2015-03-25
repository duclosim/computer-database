package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

public enum CompanyMapper {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(CompanyMapper.class);

	public Company mapCompany(ResultSet results) {
		LOG.trace("mapCompany(" + results + ")");
		if (results == null) {
			LOG.error("results est à null.");
			throw new IllegalArgumentException("results est à null.");
		}
		try {
			Company companyBean = new Company(results.getLong(CompanyDAOImpl.ID_COLUMN_LABEL),
					results.getString(CompanyDAOImpl.NAME_COLUMN_LABEL));
			return companyBean;
		} catch (SQLException e) {
			LOG.error("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}
}

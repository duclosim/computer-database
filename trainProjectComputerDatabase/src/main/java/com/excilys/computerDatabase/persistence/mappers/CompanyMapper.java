package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;

@Component
public class CompanyMapper implements RowMapper<Company> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompanyMapper.class);

	@Override
	public Company mapRow(ResultSet rs, int rowNum) {
		LOG.info("mapRow(" + rs + ", " + rowNum + ")");
		if (rs == null) {
			return null;
		}
		try {
            return new Company(rs.getLong(CompanyDAO.ID_COLUMN_LABEL),
            		rs.getString(CompanyDAO.NAME_COLUMN_LABEL));
		} catch (SQLException e) {
			LOG.error("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}

}

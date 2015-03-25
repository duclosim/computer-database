package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;

public enum ComputerMapper {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);
	
	public Computer mapComputer(ResultSet results) {
		LOG.trace("mapComputer(" + results + ")");
		if (results == null) {
			LOG.error("results est à null.");
			throw new IllegalArgumentException("results est à null.");
		}
		CompanyService companyService = CompanyServiceImpl.INSTANCE;
		Computer computerBean = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		Company company = null;
		try {
			if (results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL) != null) {
				introducedDate = results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL).toLocalDateTime();
			}
			if (results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL) != null) {
				discontinuedDate = results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
			}
			if (results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL) != 0) {
				company = companyService.getById(results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL));
			}
			computerBean = new Computer(results.getLong(ComputerDAOImpl.ID_COLUMN_LABEL), 
					results.getString(ComputerDAOImpl.NAME_COLUMN_LABEL), 
					introducedDate,
					discontinuedDate,
					company);
			return computerBean;
		} catch (SQLException e) {
			LOG.error("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}
}

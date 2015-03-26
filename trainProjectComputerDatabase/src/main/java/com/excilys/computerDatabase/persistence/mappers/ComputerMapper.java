package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;

public enum ComputerMapper {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);
	
	public Computer mapComputer(ResultSet results) {
		LOG.trace("mapComputer(" + results + ")");
		if (results == null) {
			LOG.error("results est à null.");
			throw new IllegalArgumentException("results est à null.");
		}
		Computer computerBean = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		Company company = null;
		try {
			Timestamp ts = results.getTimestamp(ComputerColumn.INTRODUCED_COLUMN_LABEL.getColumnName());
			if (ts != null) {
				introducedDate = ts.toLocalDateTime();
			}
			ts = results.getTimestamp(ComputerColumn.DISCONTINUED_COLUMN_LABEL.getColumnName());
			if (ts != null) {
				discontinuedDate = ts.toLocalDateTime();
			}
			Long l = results.getLong(ComputerColumn.COMPANY_ID_COLUMN_LABEL.getColumnName());
			if (l != 0) {
				company = new Company();
				company.setId(l);
				company.setName(results.getString(ComputerColumn.COMPANY_NAME_COLUMN_LABEL.getColumnName()));
			}
			computerBean = new Computer(results.getLong(ComputerColumn.ID_COLUMN_LABEL.getColumnName()), 
					results.getString(ComputerColumn.NAME_COLUMN_LABEL.getColumnName()), 
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

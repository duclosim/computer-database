package com.excilys.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.model.beans.Company;
import com.excilys.model.beans.Computer;
import com.excilys.persistence.PersistenceException;
import com.excilys.persistence.daos.ComputerColumn;

@Component
public class ComputerMapper implements RowMapper<Computer> {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) {
		LOG.trace("mapRow(" + rs + ", " + rowNum + ")");
		if (rs == null) {
			return null;
		}
		Computer computerBean;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		Company company = null;
		try {
			Timestamp ts = rs.getTimestamp(ComputerColumn.INTRODUCED_COLUMN_LABEL.getColumnName());
			if (ts != null) {
				introducedDate = ts.toLocalDateTime();
			}
			ts = rs.getTimestamp(ComputerColumn.DISCONTINUED_COLUMN_LABEL.getColumnName());
			if (ts != null) {
				discontinuedDate = ts.toLocalDateTime();
			}
			Long l = rs.getLong(ComputerColumn.COMPANY_ID_COLUMN_LABEL.getColumnName());
			if (l != 0) {
				company = new Company();
				company.setId(l);
				company.setName(rs.getString(ComputerColumn.COMPANY_NAME_COLUMN_LABEL.getColumnName()));
			}
			computerBean = new Computer(rs.getLong(ComputerColumn.ID_COLUMN_LABEL.getColumnName()), 
					rs.getString(ComputerColumn.NAME_COLUMN_LABEL.getColumnName()), 
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

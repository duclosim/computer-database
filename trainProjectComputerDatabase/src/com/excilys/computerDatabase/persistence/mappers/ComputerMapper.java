package com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.excilys.computerDatabase.model.CompanyBean;
import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.CompanyDAO;
import com.excilys.computerDatabase.persistence.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;

public enum ComputerMapper {
	INSTANCE;
	
	public static ComputerBean mapComputer(ResultSet results) throws SQLException {
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		ComputerBean computerBean = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		CompanyBean company = null;
		if (results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL) != null) {
			introducedDate = results.getTimestamp(ComputerDAOImpl.INTRODUCED_COLUMN_LABEL).toLocalDateTime();
		}
		if (results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL) != null) {
			discontinuedDate = results.getTimestamp(ComputerDAOImpl.DISCONTINUED_COLUMN_LABEL).toLocalDateTime();
		}
		if (results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL) != 0) {
			company = companyDAO.getById(results.getLong(ComputerDAOImpl.COMPANY_ID_COLUMN_LABEL));
		}
		computerBean = new ComputerBean(results.getLong(ComputerDAOImpl.ID_COLUMN_LABEL), 
				results.getString(ComputerDAOImpl.NAME_COLUMN_LABEL), 
				introducedDate,
				discontinuedDate,
				company);
		return computerBean;
	}
}

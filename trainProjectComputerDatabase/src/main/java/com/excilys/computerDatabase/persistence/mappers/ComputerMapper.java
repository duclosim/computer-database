package main.java.com.excilys.computerDatabase.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.PersistenceException;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

public enum ComputerMapper {
	INSTANCE;
	
	public static ComputerBean mapComputer(ResultSet results) {
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		ComputerBean computerBean = null;
		LocalDateTime introducedDate = null;
		LocalDateTime discontinuedDate = null;
		CompanyBean company = null;
		try {
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
		} catch (SQLException e) {
			System.err.println("Erreur de lecture d'une colonne");
			e.printStackTrace();
			throw new PersistenceException("Problème de lecture colonne");
		}
	}
}

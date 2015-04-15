package com.excilys.computerDatabase.persistence.daos;

import com.excilys.computerDatabase.model.beans.Company;

import java.sql.SQLException;
import java.util.List;

/**
 * Cette interface rassemble des méthodes d'accès à la table company.
 * @author excilys
 *
 */
public interface CompanyDAO extends CRUDDao<Company> {
	
	String ID_COLUMN_LABEL = "id";
	String NAME_COLUMN_LABEL = "name";
	/**
	 * 
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	List<Company> getAll();
}

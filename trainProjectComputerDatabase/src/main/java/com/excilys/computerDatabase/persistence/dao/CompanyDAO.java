package com.excilys.computerDatabase.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerDatabase.model.beans.Company;

/**
 * Cette interface rassemble des méthodes d'accès à la table company.
 * @author excilys
 *
 */
public interface CompanyDAO extends CRUDDao<Company> {
	/**
	 * 
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	List<Company> getAll() throws SQLException;
}

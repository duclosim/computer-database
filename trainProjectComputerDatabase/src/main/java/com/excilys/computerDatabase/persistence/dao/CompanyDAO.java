package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Company;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
	List<Company> getAll(Connection con) throws SQLException;
}

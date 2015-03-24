package com.excilys.computerDatabase.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.computerDatabase.model.beans.Computer;

/**
 * Cette interface rassemble des méthodes d'accès à la table computer.
 * @author excilys
 *
 */
public interface ComputerDAO extends CRUDDao<Computer> {

	/**
	 * Cette méthode supprime tous les computer avec un certain company_id.
	 * @param companyId L'id de company dont les computers qui l'ont 
	 *   comme company seront supprimés.
	 * @throws SQLException 
	 */
	void deleteByCompanyId(Long companyId, Connection con) throws SQLException;
}

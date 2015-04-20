package com.excilys.persistence.daos;

import java.sql.SQLException;

import com.excilys.model.beans.Computer;

/**
 * Cette interface rassemble des méthodes d'accès à la table computer.
 * @author excilys
 *
 */
public interface ComputerDAO extends CRUDDao<Computer> {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws SQLException 
	 */
	int countFilteredLines(String name);
	
	/**
	 * Cette méthode supprime tous les computer avec un certain company_id.
	 * @param companyId L'id de company dont les computers qui l'ont 
	 *   comme company seront supprimés.
	 * @throws SQLException 
	 */
	void deleteByCompanyId(Long companyId);
}
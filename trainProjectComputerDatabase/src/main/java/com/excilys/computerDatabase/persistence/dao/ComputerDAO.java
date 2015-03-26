package com.excilys.computerDatabase.persistence.dao;

import com.excilys.computerDatabase.model.beans.Computer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

	/**
	 * 
	 * @param name
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	List<Computer> getByNameOrCompanyName(String name, Connection con) throws SQLException;

	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param column
	 * @param way
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	List<Computer> getAll(int limit, int offset, ComputerColumn column,
			OrderingWay way, Connection con) throws SQLException;
}

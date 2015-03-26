package com.excilys.computerDatabase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;

public interface PageableService<T> {

	/**
	 * 
	 * @param id
	 * @return
	 */
	T getById(Long id);

	/**
	 * 
	 * @param name
	 * @return
	 */
	List<T> getByNameOrCompanyName(String name);
	
	/**
	 * Cette méthode retourne tout le contenu de la table 
	 *   sous forme de liste de bean.
	 * @param limit Le nombre de beans à récupérer.
	 * @param offset Le numéro du bean à partir duquel commencer à lire.
	 * @return La List de tous les beans.
	 * @throws SQLException 
	 */
	List<T> getAll(int limit, int offset);

	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param column
	 * @param way
	 * @return
	 */
	List<T> getAll(int limit, int offset, 
			ComputerColumn column, OrderingWay way);
	
	/**
	 * Retourne le nombre de lignes dans la table.
	 * @return Le nombre total de lignes dans la table.
	 * @throws SQLException 
	 */
	int countLines();
	
	/**
	 * 
	 * @param computer
	 */
	void delete(T t);

}

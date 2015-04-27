package com.excilys.services;

import java.sql.SQLException;
import java.util.List;

import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;

public interface CRUDService<T> {

	/**
	 * 
	 * @param id
	 * @return
	 */
	T getById(Long id);
	
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
	 * @param name
	 * @return
	 */
	List<T> getFiltered(String name, int limit, int offset);
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param column
	 * @param way
	 * @return
	 */
	List<T> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way);

	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param name
	 * @param column
	 * @param way
	 * @return
	 */
	List<T> getFilteredAndOrdered(int limit, int offset, String name,
			ComputerColumn column, OrderingWay way);
	
	/**
	 * Retourne le nombre de lignes dans la table.
	 * @return Le nombre total de lignes dans la table.
	 * @throws SQLException 
	 */
	int countAllLines();
	
	/**
	 * Retourne le nombre de lignes dans la table, filtrée sur le nom.
	 * @return
	 */
	int countFilteredLines(String name);
	
	/**
	 * 
	 * @param computer
	 */
	void delete(Long id);

}

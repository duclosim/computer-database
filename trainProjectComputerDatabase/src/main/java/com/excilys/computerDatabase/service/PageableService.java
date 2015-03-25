package com.excilys.computerDatabase.service;

import java.sql.SQLException;
import java.util.List;

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
	List<T> getByName(String name);
	
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

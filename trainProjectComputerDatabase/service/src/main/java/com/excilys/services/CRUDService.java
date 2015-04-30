package com.excilys.services;

import java.util.List;

import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;

/**
 * @author excilys
 * 
 * This interface contains the methods used by all the services of this application.
 * @param <T> The type of object to be retrieved from the database.
 */
public interface CRUDService<T> {

	/**
	 * This method gets an object basing upon its id from the database.
	 * @param id The id of the object.
	 * @return An object with matching id, null otherwise.
	 */
	T getById(Long id);
	
	/**
	 * This method gets several objects from the database.
	 * @param limit The number of objects to get.
	 * @param offset The number of beans ignored when reading the database.
	 * @return The list of the first limit objects, starting form offset.
	 */
	List<T> getAll(int limit, int offset);

	/**
	 * This method gets several objects from the database, filtered by name.
	 * @param limit The number of objects to get.
	 * @param offset The number of beans ignored when reading the database.
	 * @param name The name of the beans searched.
	 * @return A list of objects with matching names.
	 */
	List<T> getFiltered(String name, int limit, int offset);
	
	/**
	 * This method gets several objects from the database, filtered by name.
	 * @param limit The number of objects to get.
	 * @param offset The number of beans ignored when reading the database.
	 * @param column The column to order by.
	 * @param way The way to order by.
	 * @return A list of objects ordered by column ascending or descending.
	 */
	List<T> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way);

	/**
	 * This method gets several objects from the database, filtered by name and ordered.
	 * @param limit The number of objects to get.
	 * @param offset The number of beans ignored when reading the database.
	 * @param name The name of the beans searched.
	 * @param column The column to order by.
	 * @param way The way to order by.
	 * @return A list of objects filtered by name and ordered by column ascending or descending.
	 */
	List<T> getFilteredAndOrdered(int limit, int offset, String name,
			ComputerColumn column, OrderingWay way);
	
	/**
	 * Returns the total number of the rows in the database.
	 * @return An int representing the total number of rows of the database.
	 */
	int countAllLines();
	
	/**
	 * Return the number of rows in the database with matching name.
	 * @param name The name of the beans searched.
	 * @return An int representing the number of rows with matching names in the database.
	 */
	int countFilteredLines(String name);
	
	/**
	 * Delete one or zero object from the database with the mathing id.
	 * @param id The id of the object to be removed from the database.
	 */
	void delete(Long id);
}

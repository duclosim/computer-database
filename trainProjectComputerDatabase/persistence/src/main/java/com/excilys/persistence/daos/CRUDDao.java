package com.excilys.persistence.daos;

import java.util.List;

/**
 * This interface contains shared database management methods.
 * @author excilys
 *
 * @param <T> The type of the objects to be managed by these dao.
 */
public interface CRUDDao<T> {
	/**
	 * This method add a new object in the database.
	 * @param entity The new object to add in the database.
	 */
	void create(T entity);
	
	/**
	 * This method return an object with matching id, null 
	 *   if no objects have been found.
	 * @param id The id of the object searched.
	 * @return A object filled with the content of the row of the database, 
	 *   null if there was no matching row.
	 */
	T getById(Long id);
	
	/**
	 * This method returns a part of the objects contained in the database.
	 * @param limit The number of objects to retrieve.
	 * @param offset The number of the row to start retrieving with.
	 * @return A list containing the objects.
	 */
	List<T> getAll(int limit, int offset);
	
	/**
	 * This method works the same way getAll(int, int) works, except it 
	 *   filters the objects basing upon its names.
	 * @param limit The number of objects to retrieve.
	 * @param offset The number of the row to start retrieving with.
	 * @param name The name to filter the objects.
	 * @return A filtered list containing the objects.
	 */
	List<T> getFiltered(int limit, int offset, String name);

	/**
	 * This method works the same way getAll(int, int) works, except it 
	 *   orders the objects basing upon a column name and an ordering way.
	 * @param limit The number of objects to retrieve.
	 * @param offset The number of the row to start retrieving with.
	 * @param column The column to order by.
	 * @param way The way to order (ASC or DESC).
	 * @return An ordered list containing the objects.
	 */
	List<T> getOrdered(int limit, int offset, ComputerColumn column,
			OrderingWay way);
	
	/**
	 * This method works the same way getAll(int, int) works, except it 
	 *   filters the objects basing upon its names and it orders these objects, 
	 *   basing upon a column name and an ordering way.
	 * @param limit The number of objects to retrieve.
	 * @param offset The number of the row to start retrieving with.
	 * @param name The name to filter the objects.
	 * @param column The column to order by.
	 * @param way The way to order (ASC or DESC).
	 * @return
	 */
	List<T> getFilteredAndOrdered(int limit, int offset, String name,
			ComputerColumn column, OrderingWay way);
	/**
	 * Returns the total number of rows in the table.
	 * @return An int representing the total number of rows in the table.
	 */
	int countLines();
	
	/**
	 * This method updates an object in the database.
	 * @param entity The object to be updated.
	 */
	void update(T entity);
	
	/**
	 * This method delete an object from the database.
	 * @param id The id of the object to be deleted.
	 */
	void delete(Long id);
}

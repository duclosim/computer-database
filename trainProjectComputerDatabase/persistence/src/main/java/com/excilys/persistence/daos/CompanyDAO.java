package com.excilys.persistence.daos;

import java.util.List;

import com.excilys.model.beans.Company;

/**
 * This interface represents a dao accessing to the Company table 
 *   in the database.
 * @author excilys
 *
 */
public interface CompanyDAO extends CRUDDao<Company> {
	
	/**
	 * The name of the column containing the id.
	 */
	String ID_COLUMN_LABEL = "id";
	/**
	 * The name of the column containing the name.
	 */
	String NAME_COLUMN_LABEL = "name";

	/**
	 * Return a list containing all of the Company objects 
	 *   stored in the database.
	 * @return A List of all the Company objects stored in the database.
	 */
	List<Company> getAll();
}

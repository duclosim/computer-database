package com.excilys.persistence.daos;

import com.excilys.model.beans.Computer;

/**
 * This interface represents a dao accessing to the Computer table 
 *   in the database.
 * @author excilys
 */
public interface ComputerDAO extends CRUDDao<Computer> {

	/**
	 * This method return the number of Computer object with matching name.
	 * @param name The name searched in Computer objects 
	 *   stored in the database.
	 * @return An int representing the count of all Computer objects 
	 *   with matching names.
	 */
	int countFilteredLines(String name);
	
	/**
	 * This methods deletes all the Computer objects 
	 *   with company_id matching.
	 * @param companyId The id of the company the related computers 
	 *   are to be deleted.
	 */
	void deleteByCompanyId(Long companyId);
}

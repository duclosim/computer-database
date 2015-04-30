package com.excilys.services;

import com.excilys.model.beans.Computer;
import com.excilys.persistence.daos.ComputerDAO;

/**
 * 
 * @author excilys
 * A Service to get Computer from the database.
 */
public interface ComputerService extends CRUDService<Computer> {
	
	/**
	 * Add a new computer to the database.
	 * @param computer The Computer to be created.
	 */
	void create(Computer computer);
	
	/**
	 * Update a computer in the database.
	 * @param computer The computer to be updated.
	 */
	void update(Computer computer);
	
	/**
	 * Set the computer dao of this service to dao.
	 * @param dao The new not null ComputerDao to be used by this service.
	 */
	void setComputerDao(ComputerDAO dao);
}

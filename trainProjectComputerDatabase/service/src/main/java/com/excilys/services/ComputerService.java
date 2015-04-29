package com.excilys.services;

import com.excilys.model.beans.Computer;

/**
 * 
 * @author excilys
 *
 */
public interface ComputerService extends CRUDService<Computer> {
	
	/**
	 * 
	 * @param computer
	 */
	void create(Computer computer);
	
	/**
	 * 
	 * @param computer
	 */
	void update(Computer computer);
}

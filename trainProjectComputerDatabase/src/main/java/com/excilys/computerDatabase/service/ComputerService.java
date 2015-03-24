package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.beans.Computer;

/**
 * 
 * @author excilys
 *
 */
public interface ComputerService extends PageableService<Computer> {
	/**
	 * 
	 * @param id
	 * @return
	 */
	Computer getById(Long id);
	
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
	
	/**
	 * 
	 * @param computer
	 */
	void delete(Computer computer);
}

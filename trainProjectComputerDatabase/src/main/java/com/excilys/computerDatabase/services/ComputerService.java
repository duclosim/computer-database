package com.excilys.computerDatabase.services;

import com.excilys.computerDatabase.model.dtos.ComputerDTO;

/**
 * 
 * @author excilys
 *
 */
public interface ComputerService extends CRUDService<ComputerDTO> {
	
	/**
	 * 
	 * @param computer
	 */
	void create(ComputerDTO computer);
	
	/**
	 * 
	 * @param computer
	 */
	void update(ComputerDTO computer);
}

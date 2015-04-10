package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.dto.ComputerDTO;

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

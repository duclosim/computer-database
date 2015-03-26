package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * 
 * @author excilys
 *
 */
public interface ComputerService extends PageableService<ComputerDTO> {
	
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

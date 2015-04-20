package com.excilys.services;

import com.excilys.binding.dtos.ComputerDTO;

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

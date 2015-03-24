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
	 * @param id
	 * @return
	 */
	ComputerDTO getById(Long id);
	
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
	
	/**
	 * 
	 * @param computer
	 */
	void delete(ComputerDTO computer);
}

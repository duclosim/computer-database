package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.service.dto.ComputerDTO;

/**
 * 
 * @author excilys
 *
 */
public interface ComputerService extends PageableService<ComputerDTO> {
	// TODO ajouter tri par nom, date d'introduction, date de sortie et nom d'entreprise.
	
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

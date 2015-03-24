package com.excilys.computerDatabase.service;

import java.util.List;

import com.excilys.computerDatabase.model.beans.Company;

/**
 * 
 * @author excilys
 *
 */
public interface CompanyService extends PageableService<Company> {
	/**
	 * 
	 * @param id
	 * @return
	 */
	Company getById(Long id);
	
	/**
	 * 
	 * @param company
	 */
	void delete(Company company);

	/**
	 * 
	 * @return
	 */
	List<Company> getAll();
}

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
	 * @return
	 */
	List<Company> getAll();
}

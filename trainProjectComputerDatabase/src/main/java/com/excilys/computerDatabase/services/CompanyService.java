package com.excilys.computerDatabase.services;

import com.excilys.computerDatabase.model.beans.Company;

import java.util.List;

/**
 * 
 * @author excilys
 *
 */
public interface CompanyService extends CRUDService<Company> {
	
	/**
	 * 
	 * @return
	 */
	List<Company> getAll();
}

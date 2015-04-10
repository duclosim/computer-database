package com.excilys.computerDatabase.service;

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

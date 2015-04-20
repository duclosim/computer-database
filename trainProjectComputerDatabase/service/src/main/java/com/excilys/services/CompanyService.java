package com.excilys.services;

import java.util.List;

import com.excilys.model.beans.Company;

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

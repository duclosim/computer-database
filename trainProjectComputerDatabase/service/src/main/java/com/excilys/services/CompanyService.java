package com.excilys.services;

import java.util.List;

import com.excilys.model.beans.Company;
import com.excilys.persistence.daos.CompanyDAO;
import com.excilys.persistence.daos.ComputerDAO;

/**
 * @author excilys
 * A Service to get Company from the database.
 */
public interface CompanyService extends CRUDService<Company> {
	
	/**
	 * Return all the companies from the database.
	 * @return A List containing all of the Company in the database.
	 */
	List<Company> getAll();
	
	/**
	 * Set the company dao of this service to dao.
	 * @param dao The new not null CompanyDao to be used by this service.
	 */
	void setCompanyDao(CompanyDAO dao);
	
	/**
	 * Set the computer dao of this service to dao.
	 * @param dao The new not null ComputerDao to be used by this service.
	 */
	void setComputerDao(ComputerDAO dao);
}

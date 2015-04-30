package com.excilys.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;

// Service endpoint interface
@WebService
@SOAPBinding
public interface ComputerDatabaseWS {
	String ENDPOINT = "http://localhost:9999/computer-database-ws/computers";

	/**
	 * Returns a String containing all of the Company objects in the database.
	 * @return A String containing all of the Company objects in the database.
	 */
	@WebMethod
	String getCompanies();

	/**
	 * This method return the Computer matching given id, 
	 *   null if no objects are found.
	 * @param id The id owned by the searched Company.
	 * @return A Company with matching id, 
	 *   null if no matching Company are found.
	 */
	@WebMethod
	String getComputers(int limit, int offset, String searchedName, 
			ComputerColumn column, OrderingWay way);

	/**
	 * Add a new Computer to the database.
	 * @param computer The computer to add in the database.
	 */
	@WebMethod
	String createComputer(ComputerDTO computer);
	
	/**
	 * Deletes the company and related computers from the database.
	 * @param id The id of the company to be deleted.
	 */
	@WebMethod
	void deleteCompany(Long id);
	
	/**
	 * Deletes the computer from the database.
	 * @param id The id of the computer to be deleted.
	 */
	@WebMethod
	void deleteComputer(Long id);
	
	/**
	 * Details the computer data from the database.
	 * @param id The id of the computer to detail.
	 * @return A String containing all the computer details.
	 */
	@WebMethod
	String detailComputer(Long id);

    /**
     * This method updates a Computer in the database.
     * @param computer The Computer to be updated in the database.
     */
	@WebMethod
	String updateComputer(ComputerDTO computer);
}

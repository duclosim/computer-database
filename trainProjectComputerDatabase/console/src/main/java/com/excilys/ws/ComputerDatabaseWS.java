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
	@WebMethod
	String getCompanies();
	
	@WebMethod
	String getComputers(int limit, int offset, String searchedName, 
			ComputerColumn column, OrderingWay way);
	
	@WebMethod
	String createComputer(ComputerDTO computer);
	
	@WebMethod
	void deleteCompany(Long id);
	
	@WebMethod
	void deleteComputer(Long id);
	
	@WebMethod
	String detailComputer(Long id);
	
	@WebMethod
	String updateComputer(ComputerDTO computer);
}

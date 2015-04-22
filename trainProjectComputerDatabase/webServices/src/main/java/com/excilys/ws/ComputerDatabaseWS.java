package com.excilys.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.model.beans.Company;
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
	void deleteCompany(Company company);
	
	@WebMethod
	void deleteComputer(ComputerDTO computer);
	
	@WebMethod
	String detailComputer(String str);
	
	@WebMethod
	String updateComputer(ComputerDTO computer);
}

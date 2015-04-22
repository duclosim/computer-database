package com.excilys.ws;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.model.beans.Company;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.CompanyService;
import com.excilys.services.ComputerService;

// Service Implementation
@WebService(endpointInterface = "com.excilys.ws.ComputerDatabaseWS")
public class ComputerDatabaseWSImpl implements ComputerDatabaseWS {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDatabaseWSImpl.class);

	@Autowired
	private Page page;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	@Override
	public String getCompanies() {
		LOG.info("getCompanies()");
		StringBuilder sb = new StringBuilder();
		for (Company cmpny : companyService.getAll()) {
			sb.append(cmpny);
		}
		return sb.toString();
	}
	
	@Override
	public String getComputers(int limit, int pageNum, String searchedName, 
			ComputerColumn column, OrderingWay way) {
		LOG.info(new StringBuilder("getComputers(")
			.append(limit).append(", ")
			.append(pageNum).append(", ")
			.append(searchedName).append(", ")
			.append(column).append(", ")
			.append(way).append(")").toString());
		// Numéro de page.
		if (pageNum < 0) {
			page.setPageNum(1);
		} else {
			page.setPageNum(pageNum);
		}
		// Nombre d'objets par page.
		if (limit < 0) {
			page.setMaxNbItemsByPage(1);
		} else {
			page.setMaxNbItemsByPage(limit);
		}
		// Tri
		if (column == null && way == null) {
			page.setColumn(null);
			page.setWay(null);
		} else {
			page.setColumn(column);
			page.setWay(way);
		}
		// Recherche sur le nom.
		if (searchedName == null || searchedName.trim().isEmpty()) {
			page.setSearchedName(null);
		} else {
			page.setSearchedName(searchedName);
		}
		return page.toString();
	}
	
	@Override
	public String createComputer(ComputerDTO computer) {
		LOG.info("createComputer(" + computer + ")");
		computerService.create(computer);
		return computer.toString();
	}
	
	@Override
	public void deleteCompany(Company company) {
		LOG.info("deleteCompany(" + company + ")");
		companyService.delete(company);
	}
	
	@Override
	public void deleteComputer(ComputerDTO computer) {
		LOG.info("deleteComputer(" + computer + ")");
		computerService.delete(computer);
	}
	
	@Override
	public String detailComputer(String str) {
		LOG.info("detailComputer(" + str + ")");
		return computerService.getById(Long.parseLong(str)).toString();
	}
	
	@Override
	public String updateComputer(ComputerDTO computer) {
		LOG.info("updateComputer(" + computer + ")");
		computerService.update(computer);
		return computerService.getById(
				Long.parseLong(computer.getId()))
				.toString();
    }
}

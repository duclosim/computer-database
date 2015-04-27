package com.excilys.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.model.beans.Company;
import com.excilys.services.CompanyService;

@RestController
@RequestMapping("/rest-api/companies")
public class CompanyRESTController {
	private static final Logger LOG = LoggerFactory.getLogger(CompanyRESTController.class);

	@Autowired
	private CompanyService service;
	
	@RequestMapping(value = "getById", method = RequestMethod.GET)
	public Company getById(@RequestParam(value = "id") Long id) {
		LOG.trace("getById(" + id + ")");
		return service.getById(id);
	}
	
    @RequestMapping(value = "list",  method = RequestMethod.GET)
    public List<Company> getList() {
		LOG.trace("list()");
		return service.getAll();
    }
}

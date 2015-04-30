package com.excilys.controllers;

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

/**
 * This class exposes listing functions for Company objects from the database.
 * @author excilys
 *
 */
@RestController
@RequestMapping("/companies")
public class CompanyRESTController {
	private static final Logger LOG = LoggerFactory.getLogger(CompanyRESTController.class);

	@Autowired
	private CompanyService service;
	
	/**
	 * This method return the company matching given id, 
	 *   null if no objects are found.
	 * @param id The id owned by the searched Company.
	 * @return A Company with matching id, 
	 *   null if no matching Company are found.
	 */
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	public Company getById(@RequestParam(value = "id") Long id) {
		LOG.trace("getById(" + id + ")");
		return service.getById(id);
	}
	
	/**
	 * This method returns all of the Company objects from the database.
	 * @return A list of all the Company objects from the database..
	 */
    @RequestMapping(value = "/list",  method = RequestMethod.GET)
    public List<Company> getList() {
		LOG.trace("list()");
		return service.getAll();
    }
}

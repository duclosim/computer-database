package com.excilys.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.model.beans.Computer;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.ComputerService;

/**
 * This class exposes listing functions for Computer objects from the database.
 * @author excilys
 *
 */
@RestController
@RequestMapping("/computers")
public class ComputerRESTController {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerRESTController.class);
	
	@Autowired
	private ComputerService service;
	@Autowired
	private Page page;
    
	/**
	 * Add a new Computer to the database.
	 * @param computer The computer to add in the database.
	 */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestBody Computer computer) {
    	LOG.trace("create(" + computer + ")");
    	service.create(computer);
    }

	/**
	 * This method return the Computer matching given id, 
	 *   null if no objects are found.
	 * @param id The id owned by the searched Company.
	 * @return A Company with matching id, 
	 *   null if no matching Company are found.
	 */
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	public Computer getById(@RequestParam(value = "id") Long id) {
		LOG.trace("getById(" + id + ")");
		return service.getById(id);
	}
	
	/**
	 * This method get a list formed by a part of the Computer table.
	 *   It can take several arguments, namely for ordering 
	 *   and name-based filtering.
	 * @param nbItem The maximum number of objects to be retrieved, 
	 *   10 by default.
	 * @param name The String for name-based filtering of Computer objects, 
	 *   empty by default.
	 * @param col The column to perform ordering, empty by default, 
	 *   null by default.
	 * @param way The way to order Computer objects, null by default.
	 * @param pageNum The number of the page to get the objects from, 
	 *   1 by default.
	 * @return A List of the Computer objects contained by the Page object
	 *   which parameters have been set.
	 */
    @RequestMapping(value = "/list",  method = RequestMethod.GET)
    public List<Computer> getList(@RequestParam(value = "nbItem", defaultValue = "10") int nbItem,
    		@RequestParam(value = "name", defaultValue = "") String name,
    		@RequestParam(value = "col", defaultValue = "computer.id") String col,
    		@RequestParam(value = "way", defaultValue = "ASC") String way,
    		@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
		LOG.trace(new StringBuilder("list(")
			.append(nbItem).append(", ")
			.append(name).append(", ")
			.append(col).append(", ")
			.append(way).append(", ")
			.append(pageNum).append(")").toString());
    	page.setMaxNbItemsByPage(nbItem);
    	page.setSearchedName(name);
		page.setColumn(null);
    	for (ComputerColumn column : ComputerColumn.values()) {
    		if (column.getColumnName().equals(col)) {
    			page.setColumn(column);
    		}
    	}
    	page.setWay(null);
    	for (OrderingWay w : OrderingWay.values()) {
    		if (w.getWay().equals(way)) {
    			page.setWay(w);
    		}
    	}
    	page.setPageNum(pageNum);
    	return page.getEntities();
    }

    /**
     * This method updates a Computer in the database.
     * @param computer The Computer to be updated in the database.
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(@RequestBody Computer computer) {
		LOG.trace("update(" + computer + ")");
		service.update(computer);
    }
}

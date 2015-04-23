package com.excilys.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;

@RestController
@RequestMapping("/rest-api/")
public class ComputerRESTController {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerRESTController.class);
	
	@Autowired
	private Page page;
	
    @RequestMapping(value = "listComputers",  method = RequestMethod.GET)
    public List<ComputerDTO> getComputers(@RequestParam(value = "nbItem", defaultValue = "10") int nbItem,
    		@RequestParam(value = "name", defaultValue = "") String name,
    		@RequestParam(value = "col", defaultValue = "computer.id") String col,
    		@RequestParam(value = "way", defaultValue = "ASC") String way,
    		@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
		LOG.trace(new StringBuilder("getDashboard(")
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
}

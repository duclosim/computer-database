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

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.binding.dtos.ComputerDTOMapper;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.ComputerService;

@RestController
@RequestMapping("/rest-api/computers")
public class ComputerRESTController {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerRESTController.class);
	
	@Autowired
	private ComputerDTOMapper mapper;
	@Autowired
	private ComputerService service;
	@Autowired
	private Page page;
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void create(@RequestBody ComputerDTO computer) {
    	LOG.trace("create(" + computer + ")");
    	service.create(mapper.DTOToBean(computer));
    }
	
	@RequestMapping(value = "getById", method = RequestMethod.GET)
	public ComputerDTO getById(@RequestParam(value = "id") Long id) {
		LOG.trace("getById(" + id + ")");
		return mapper.BeanToDTO(service.getById(id));
	}
	
    @RequestMapping(value = "list",  method = RequestMethod.GET)
    public List<ComputerDTO> getList(@RequestParam(value = "nbItem", defaultValue = "10") int nbItem,
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
    	return mapper.BeansToDTOs(page.getEntities());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void update(@RequestBody ComputerDTO computer) {
		LOG.trace("update(" + computer + ")");
		service.update(mapper.DTOToBean(computer));
    }
}

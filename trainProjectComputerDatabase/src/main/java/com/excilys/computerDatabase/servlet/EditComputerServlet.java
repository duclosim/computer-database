package com.excilys.computerDatabase.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;

@Controller
@RequestMapping("/editComputer")
public class EditComputerServlet {
	private static final Logger LOG = LoggerFactory.getLogger(EditComputerServlet.class);
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private MessageSource messageSource;
	
	private ComputerDTO computer;
	private final String VALID_MESSAGE = "";//messageSource.getMessage("editComputer.validMessage", new Object[]{}, null);
	
	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam("beanId") Long id, Model model) {
		LOG.info(new StringBuilder("get(")
			.append(id).append(", ")
			.append(model).append(")").toString());
		computer = computerService.getById(id);
		
		model.addAttribute("companies", companyService.getAll());
		model.addAttribute("computerBean", computer);
		return "addComputer";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void editComputer(@RequestParam(value = "computerName", required = true) String name, 
			@RequestParam(value = "introduced", required = false) String introducedDate, 
			@RequestParam(value = "discontinued", required = false) String discontinuedDate, 
			@RequestParam(value = "companyId", required = false) String companyId, 
			Model model) {
		LOG.info(new StringBuilder("editComputer(")
			.append(name).append(", ")
			.append(introducedDate).append(", ")
			.append(discontinuedDate).append(", ")
			.append(companyId).append(", ")
			.append(model).append(")").toString());
		if (introducedDate != null && introducedDate.trim().isEmpty()) {
			introducedDate = null;
		}
		if (discontinuedDate != null && discontinuedDate.trim().isEmpty()) {
			discontinuedDate = null;
		}
		if (companyId != null && companyId.trim().isEmpty()) {
			companyId = null;
		}
		computer.setName(name);
		computer.setIntroducedDate(introducedDate);
		computer.setDiscontinuedDate(discontinuedDate);
		computer.setCompanyId(companyId);
		computerService.update(computer);
		
		model.addAttribute("validMessage", VALID_MESSAGE);
		model.addAttribute("computerBean", computer);
	}
}

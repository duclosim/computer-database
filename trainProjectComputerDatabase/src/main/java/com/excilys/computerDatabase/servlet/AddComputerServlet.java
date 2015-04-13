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
@RequestMapping("/addComputer")
public class AddComputerServlet {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private MessageSource messageSource;
	
	private static final Logger LOG = LoggerFactory.getLogger(AddComputerServlet.class);
	private final String VALID_MESSAGE = "";//messageSource.getMessage("addComputer.validMessage", new Object[]{}, null);

	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model) {
		LOG.trace(new StringBuilder("get()").toString());
		model.addAttribute("companies", companyService.getAll());
		return "addComputer";
	}

	@RequestMapping(method = RequestMethod.POST)
	public void createComputer(@RequestParam(value = "computerName", required = true) String name, 
			@RequestParam(value = "introduced", required = false) String introducedDate, 
			@RequestParam(value = "discontinued", required = false) String discontinuedDate, 
			@RequestParam(value = "companyId", required = false) String companyId, 
			Model model) {
		LOG.trace(new StringBuilder("createComputer(")
			.append(name).append(", ")
			.append(introducedDate).append(", ")
			.append(discontinuedDate).append(", ")
			.append(companyId).append(", ")
			.append(model).append(")").toString());
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(name);
		if (introducedDate != null && introducedDate.trim().isEmpty()) {
			introducedDate = null;
		}
		if (discontinuedDate != null && discontinuedDate.trim().isEmpty()) {
			discontinuedDate = null;
		}
		if (companyId != null && companyId.trim().isEmpty()) {
			companyId = null;
		}
		computerDTO.setIntroducedDate(introducedDate);
		computerDTO.setDiscontinuedDate(discontinuedDate);
		computerDTO.setCompanyId(companyId);
		computerService.create(computerDTO);
		
		model.addAttribute("validMessage", VALID_MESSAGE + computerDTO.toString());
	}
}

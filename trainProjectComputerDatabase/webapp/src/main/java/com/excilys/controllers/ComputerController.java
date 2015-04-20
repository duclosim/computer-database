package com.excilys.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.model.beans.Company;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.CompanyService;
import com.excilys.services.ComputerService;
import com.excilys.validators.UserInputsValidator;

@Controller
@RequestMapping("/")
public class ComputerController {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerController.class);
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private Page page; 
    @Autowired
    private Validator computerDTOValidator;
 
	// --- DASHBOARD ---
	@RequestMapping(value = "dashboard", method = RequestMethod.GET)
	public ModelAndView getDashboard(@RequestParam(value = "pageNum", required = false) Integer numParam,
			@RequestParam(value = "itemByPage", required = false) Integer maxItemPageParam,
			@RequestParam(value = "column", required = false) String computerColumnStr,
			@RequestParam(value = "orderWay", required = false) String orderWayStr,
			@RequestParam(value = "search", required = false) String searchedName) {
		LOG.info(new StringBuilder("getDashboard(")
			.append(numParam).append(", ")
			.append(maxItemPageParam).append(", ")
			.append(computerColumnStr).append(", ")
			.append(orderWayStr).append(", ")
			.append(searchedName).append(")").toString());
		ModelAndView model = new ModelAndView("dashboard");
		// Numéro de page.
		int newPageNum = Page.DEFAULT_PAGE_NUM;
		if (numParam != null) {
			newPageNum = numParam;
		}
		if (page.getPageNum() != newPageNum) {
			page.setPageNum(newPageNum);
		}
		// Nombre d'objets par page.
		int newItemByPage = Page.DEFAULT_LIMIT;
		if (maxItemPageParam != null) {
			newItemByPage = maxItemPageParam;
		}
		if (page.getMaxNbItemsByPage() != newItemByPage) {
			page.setMaxNbItemsByPage(newItemByPage);
		}
		// Tri
		ComputerColumn column = null;
		OrderingWay way = null;
		if (UserInputsValidator.isValidString(computerColumnStr)
				&& UserInputsValidator.isValidString(orderWayStr)) {
			int k = 0;
			ComputerColumn c = ComputerColumn.values()[k];
			while (!c.getColumnName().equals(computerColumnStr)
					&& k < ComputerColumn.values().length) {
				++k;
				c = ComputerColumn.values()[k];
			}
			if (c.getColumnName().equals(computerColumnStr)) {
				column = c;
			}
			k = 0;
			OrderingWay ow = OrderingWay.values()[k];
			while (!ow.getWay().equals(orderWayStr)
					&& k < OrderingWay.values().length) {
				++k;
				ow = OrderingWay.values()[k];
			}
			if (ow.getWay().equals(orderWayStr)) {
				way = ow;
			}
		}
		page.setColumn(column);
		page.setWay(way);
		// Recherche sur le nom.
		if (UserInputsValidator.isValidString(searchedName)) {
			page.setSearchedName(searchedName);
		} else {
			page.setSearchedName(null);
		}
		// Passage des paramètres de la page dans la requête.
		model.addObject("page", page);
		return model;
	}

	@RequestMapping(value = "dashboard", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("selection") String selectedComputersId) {
		LOG.info(new StringBuilder("delete(")
			.append(selectedComputersId).append(")").toString());
		ModelAndView model = new ModelAndView("dashboard");
		if (selectedComputersId != null) {
			StringTokenizer st = new StringTokenizer(selectedComputersId, ",");
			while (st.hasMoreTokens()) {
				ComputerDTO deleteDTO = computerService.getById(Long.parseLong(st.nextToken()));
				computerService.delete(deleteDTO);
			}
		}
		model.addObject("page", page);
		return model;
	}
	// --- ADD COMPUTER ---
	@RequestMapping(value = "addComputer", method = RequestMethod.GET)
	public ModelAndView getAddComputer() {
		LOG.info("getAddComputer()");
		ModelAndView model = new ModelAndView("addComputer");
		model.addObject("companies", mapCompanies());
		model.addObject("computerForm", new ComputerDTO());
		return model;
	}

	@RequestMapping(value = "/addComputer", method = RequestMethod.POST)
	public ModelAndView createComputer(@ModelAttribute("computerForm") ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.info(new StringBuilder("createComputer(")
			.append(computer).append(", ")
			.append(bindingResult).append(")").toString());
		ModelAndView model = new ModelAndView();
		model.addObject("companies", mapCompanies());
		return saveOrUpdateComputer(SaveOrUpdate.SAVE, computer, bindingResult);
	}
	// --- EDIT COMPUTER ---
	@RequestMapping(value = "editComputer", method = RequestMethod.GET)
	public ModelAndView getEditComputer(@RequestParam("beanId") Long id) {
		LOG.info(new StringBuilder("getEditComputer(")
			.append(id).append(")").toString());
		ModelAndView model = new ModelAndView("editComputer");
		model.addObject("companies", mapCompanies());
		model.addObject("computerForm", computerService.getById(id));
		return model;
	}
	
	@RequestMapping(value = "editComputer", method = RequestMethod.POST)
	public ModelAndView editComputer(@ModelAttribute("computerForm") ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.info(new StringBuilder("editComputer(")
			.append(computer).append(", ")
			.append(bindingResult).append(")").toString());
		return saveOrUpdateComputer(SaveOrUpdate.UPDATE, computer, bindingResult)
				.addObject("companies", mapCompanies());
	}
	// --- OUTILS ---
	/*
	 * Fill up a Map<Long, String> with companies ids and names.
	 * @return
	 */
	private Map<Long, String> mapCompanies() {
		LOG.info("mapCompanies()");
		Map<Long, String> companies = new HashMap<>();
		for (Company cmpny : companyService.getAll()) {
			companies.put(cmpny.getId(), cmpny.getName());
		}
		return companies;
	}
	
	private enum SaveOrUpdate {SAVE, UPDATE};
	
	private ModelAndView saveOrUpdateComputer(SaveOrUpdate method, 
			ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.info(new StringBuilder("saveOrUpdateComputer(")
			.append(method).append(", ")
			.append(computer).append(", ")
			.append(bindingResult).append(")").toString());
		nullifyEmptyStrings(computer);
		computerDTOValidator.validate(computer, bindingResult);
		if (!bindingResult.hasErrors()) {
			// Company id
			if (computer.getCompanyId() != null) {
				Long companyId = Long.parseLong(computer.getCompanyId());
				Company cmpny = companyService.getById(companyId);
				computer.setCompanyName(cmpny.getName());
			}
			switch (method) {
			case SAVE :
				computerService.create(computer);
				return new ModelAndView("redirect:dashboard");
			case UPDATE :
				computerService.update(computer);
				return new ModelAndView("redirect:dashboard");
			}
		}
		switch (method) {
		case SAVE :
			return new ModelAndView("addComputer");
		case UPDATE :
			return new ModelAndView("editComputer");
		default :
			return new ModelAndView("redirect:dashboard");
		}
	}
	
	private static void nullifyEmptyStrings(ComputerDTO computer) {
		LOG.info("nullifyEmptyStrings(" + computer + ")");
		computer.setId(nullifyEmptyString(computer.getId()));
		computer.setIntroducedDate(nullifyEmptyString(computer.getIntroducedDate()));
		computer.setDiscontinuedDate(nullifyEmptyString(computer.getDiscontinuedDate()));
		computer.setCompanyId(nullifyEmptyString(computer.getCompanyId()));
	}

	private static String nullifyEmptyString(String str) {
		LOG.info("nullifyEmptyStrings(" + str + ")");
		if ((str == null) || (str.trim().isEmpty())) {
			return null;
		} else {
			return str;
		}
	}
}

package com.excilys.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.binding.dtos.ComputerDTOMapper;
import com.excilys.binding.dtos.PageDTO;
import com.excilys.model.beans.Company;
import com.excilys.model.beans.Computer;
import com.excilys.page.Page;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.CompanyService;
import com.excilys.services.ComputerService;
import com.excilys.validators.UserInputsValidator;

/**
 * This controller manage the access to the Computer manager pages.
 * @author excilys
 *
 */
@Controller
@RequestMapping("/")
public class ComputerController {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerController.class);
	
	@Autowired
	private ComputerDTOMapper mapper;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private PageDTO page; 
    @Autowired
    @Qualifier(value = "computerValidator")
    private Validator computerDTOValidator;
    
    // --- REDIRECT ---
    /**
     * This method redirect the default URL to dashboard.
     * @return The ModelAndView to redirect to dashboard jsp.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView redirect() {
    	return new ModelAndView("redirect:dashboard");
    }
 
	// --- DASHBOARD ---
    /**
     * This method load the parameters for PageDTO object, 
     *   performs requests and finally put it into the model.
     * @param numParam The number of the page to load.
     * @param maxItemPageParam The maximum of objects to load.
     * @param computerColumnStr The column of the computer table 
     *   to order Computer objects by.
     * @param orderWayStr The way to order Computer objects by.
     * @param searchedName The name to filter Computer objects.
     * @return The ModelAndView containing the PageDTO object 
     *   and reloading dashboard jsp.
     */
	@RequestMapping(value = "dashboard", method = RequestMethod.GET)
	public ModelAndView getDashboard(@RequestParam(value = "pageNum", required = false) Integer numParam,
			@RequestParam(value = "itemByPage", required = false) Integer maxItemPageParam,
			@RequestParam(value = "column", required = false) String computerColumnStr,
			@RequestParam(value = "orderWay", required = false) String orderWayStr,
			@RequestParam(value = "search", required = false) String searchedName) {
		LOG.trace(new StringBuilder("getDashboard(")
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
	
	/**
	 * This method performs the deletion of selected computers, 
	 *   only if the user has the role admin.
	 * @param selectedComputersId A String containing the 
	 *   comma separated ids of the Computer objects to be deleted 
	 * @return A ModelAndView which reload dashboard jsp.
	 */
	@RequestMapping(value = "dashboard", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("selection") String selectedComputersId) {
		LOG.trace(new StringBuilder("delete(")
			.append(selectedComputersId).append(")").toString());
		ModelAndView model = new ModelAndView("dashboard");
		if (selectedComputersId != null) {
			StringTokenizer st = new StringTokenizer(selectedComputersId, ",");
			while (st.hasMoreTokens()) {
				Computer deleteDTO = computerService.getById(Long.parseLong(st.nextToken()));
				computerService.delete(deleteDTO.getId());
			}
		}
		model.addObject("page", page);
		return model;
	}
	// --- ADD COMPUTER ---
	/**
	 * This method goes on the addComputer jsp.
	 * @return A ModelAndView to redirect to addComputer.
	 */
	@RequestMapping(value = "addComputer", method = RequestMethod.GET)
	public ModelAndView getAddComputer() {
		LOG.trace("getAddComputer()");
		ModelAndView model = new ModelAndView("addComputer");
		model.addObject("companies", mapCompanies());
		model.addObject("computerForm", new ComputerDTO());
		return model;
	}

	/**
	 * This method performs the creation of a Computer object in the 
	 *   database and redirect to dashboard.
	 * @param computer The computer to be created.
	 * @param bindingResult It contains the results of the validation 
	 *   of the computer via ComputerDTOValidator.
	 * @return A ModelAndView to redirect to dashboard.
	 */
	@RequestMapping(value = "/addComputer", method = RequestMethod.POST)
	public ModelAndView createComputer(@ModelAttribute("computerForm") ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.trace(new StringBuilder("createComputer(")
			.append(computer).append(", ")
			.append(bindingResult).append(")").toString());
		ModelAndView model = new ModelAndView();
		model.addObject("companies", mapCompanies());
		return saveOrUpdateComputer(SaveOrUpdate.SAVE, computer, bindingResult);
	}
	
	// --- EDIT COMPUTER ---
	/**
	 * This method goes on the editComputer with data of 
	 *   a Computer object jsp.
	 * @param id The id of the edited Computer.
	 * @return A ModelAndView to redirect to addComputer.
	 */
	@RequestMapping(value = "editComputer", method = RequestMethod.GET)
	public ModelAndView getEditComputer(@RequestParam("beanId") Long id) {
		LOG.trace(new StringBuilder("getEditComputer(")
			.append(id).append(")").toString());
		ModelAndView model = new ModelAndView("editComputer");
		model.addObject("companies", mapCompanies());
		model.addObject("computerForm", mapper.BeanToDTO(computerService.getById(id)));
		return model;
	}
	
	/**
	 * This method performs the updating of a Computer object in the 
	 *   database and redirect to dashboard.
	 * @param computer The computer to be updated.
	 * @param bindingResult It contains the results of the validation 
	 *   of the computer via ComputerDTOValidator.
	 * @return A ModelAndView to redirect to dashboard.
	 */
	@RequestMapping(value = "editComputer", method = RequestMethod.POST)
	public ModelAndView editComputer(@ModelAttribute("computerForm") ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.trace(new StringBuilder("editComputer(")
			.append(computer).append(", ")
			.append(bindingResult).append(")").toString());
		return saveOrUpdateComputer(SaveOrUpdate.UPDATE, computer, bindingResult)
				.addObject("companies", mapCompanies());
	}
	
	// --- TOOLS ---
	/*
	 * Fill up a Map<Long, String> with companies ids and names.
	 */
	private Map<Long, String> mapCompanies() {
		LOG.trace("mapCompanies()");
		Map<Long, String> companies = new HashMap<>();
		for (Company cmpny : companyService.getAll()) {
			companies.put(cmpny.getId(), cmpny.getName());
		}
		return companies;
	}
	
	/*
	 * This enum is used to indicates if the method run is create 
	 *   or update a Computer object.
	 */
	private enum SaveOrUpdate {SAVE, UPDATE};
	
	/*
	 * This method contains shared code between add and edit methods.
	 */
	private ModelAndView saveOrUpdateComputer(SaveOrUpdate method, 
			ComputerDTO computer, 
			BindingResult bindingResult) {
		LOG.trace(new StringBuilder("saveOrUpdateComputer(")
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
				computerService.create(mapper.DTOToBean(computer));
				break;
			case UPDATE :
				computerService.update(mapper.DTOToBean(computer));
				break;
			}
			return new ModelAndView("redirect:dashboard");
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
	
	/*
	 * This method applies nullifyEmptyStrings on all the fields of computer.
	 */
	private static void nullifyEmptyStrings(ComputerDTO computer) {
		LOG.trace("nullifyEmptyStrings(" + computer + ")");
		computer.setId(nullifyEmptyString(computer.getId()));
		computer.setIntroducedDate(nullifyEmptyString(computer.getIntroducedDate()));
		computer.setDiscontinuedDate(nullifyEmptyString(computer.getDiscontinuedDate()));
		computer.setCompanyId(nullifyEmptyString(computer.getCompanyId()));
	}

	/*
	 * This method returns null if str is empty or null.
	 */
	private static String nullifyEmptyString(String str) {
		LOG.trace("nullifyEmptyStrings(" + str + ")");
		if ((str == null) || (str.trim().isEmpty())) {
			return null;
		} else {
			return str;
		}
	}
}

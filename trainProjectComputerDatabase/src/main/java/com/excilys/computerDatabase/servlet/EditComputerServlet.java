package com.excilys.computerDatabase.servlet;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.utils.UserInputsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
	
	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam("beanId") Long id, Model model) {
		LOG.info(new StringBuilder("get(")
			.append(id).append(", ")
			.append(model).append(")").toString());
		Map<Long, String> companies = new HashMap<>();
		for (Company cmpny : companyService.getAll()) {
			companies.put(cmpny.getId(), cmpny.getName());
		}
		model.addAttribute("companies", companies);
		model.addAttribute("computerForm", computerService.getById(id));
		return "editComputer";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String editComputer(@Valid @ModelAttribute("computerForm") ComputerDTO computerDTO, 
			BindingResult bindingResult,
			Model model) {
		LOG.info(new StringBuilder("editComputer(")
			.append(computerDTO).append(", ")
			.append(bindingResult).append(", ")
			.append(model).append(")").toString());
		if (!bindingResult.hasErrors()) {
			// Introduction date
			String introducedDate = UserInputsValidator.nullifyEmptyString(computerDTO.getIntroducedDate());
			if (UserInputsValidator.isValidOrNullDate(introducedDate)) {
				computerDTO.setIntroducedDate(introducedDate);
			} else {
				computerDTO.setIntroducedDate(null);
			}
			// Discontinued date
			String discontinuedDate = UserInputsValidator.nullifyEmptyString(computerDTO.getDiscontinuedDate());
			if (UserInputsValidator.isValidOrNullDate(discontinuedDate)) {
				computerDTO.setDiscontinuedDate(discontinuedDate);
			} else {
				computerDTO.setDiscontinuedDate(null);
			}
			// Company id
			String companyIdStr = UserInputsValidator.nullifyEmptyString(computerDTO.getCompanyId());
			computerDTO.setCompanyId(companyIdStr);
			if (companyIdStr != null) {
				Long companyId = Long.parseLong(companyIdStr);
				Company cmpny = companyService.getById(companyId);
				computerDTO.setCompanyName(cmpny.getName());
			}
			computerService.update(computerDTO);
			return "redirect:/dashboard";
		}
		return "editComputer";
	}
}

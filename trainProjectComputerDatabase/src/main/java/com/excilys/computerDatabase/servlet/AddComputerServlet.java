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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model) {
		LOG.info("get()");
		Map<Long, String> companies = new HashMap<>();
		for (Company cmpny : companyService.getAll()) {
			companies.put(cmpny.getId(), cmpny.getName());
		}
		model.addAttribute("companies", companies);
		model.addAttribute("computerForm", new ComputerDTO());
		return "addComputer";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createComputer(@Valid @ModelAttribute("computerForm") ComputerDTO computerDTO, 
			BindingResult bindingResult,
			Model model) {
		LOG.info(new StringBuilder("createComputer(")
			.append(computerDTO).append(", ")
			.append(bindingResult).append(", ")
			.append(model).append(")").toString());
		if (!bindingResult.hasErrors()) {
			String introducedDate = UserInputsValidator.nullifyEmptyString(computerDTO.getIntroducedDate());
			if (UserInputsValidator.isValidOrNullDate(introducedDate)) {
				computerDTO.setIntroducedDate(introducedDate);
			} else {
				computerDTO.setIntroducedDate(null);
			}
			String discontinuedDate = UserInputsValidator.nullifyEmptyString(computerDTO.getDiscontinuedDate());
			if (UserInputsValidator.isValidOrNullDate(discontinuedDate)) {
				computerDTO.setDiscontinuedDate(discontinuedDate);
			} else {
				computerDTO.setDiscontinuedDate(null);
			}
			computerDTO.setCompanyId(UserInputsValidator.nullifyEmptyString(computerDTO.getCompanyId()));
			if (computerDTO.getCompanyId() != null) {
				Long companyId = Long.parseLong(computerDTO.getCompanyId());
				Company cmpny = companyService.getById(companyId);
				computerDTO.setCompanyName(cmpny.getName());
			}
			computerService.create(computerDTO);
			return "redirect:/dashboard";
		}
		return "addComputer";
	}
}

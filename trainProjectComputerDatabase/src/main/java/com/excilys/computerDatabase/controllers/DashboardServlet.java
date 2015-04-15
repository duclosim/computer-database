package com.excilys.computerDatabase.controllers;

import com.excilys.computerDatabase.model.dtos.ComputerDTO;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.persistence.daos.ComputerColumn;
import com.excilys.computerDatabase.persistence.daos.OrderingWay;
import com.excilys.computerDatabase.services.ComputerService;
import com.excilys.computerDatabase.utils.UserInputsValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.StringTokenizer;

@Controller
@RequestMapping("/dashboard")
public class DashboardServlet {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardServlet.class);

	@Autowired
	private ComputerService service;
	@Autowired
	private Page page;

	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam(value = "pageNum", required = false) Integer numParam,
			@RequestParam(value = "itemByPage", required = false) Integer maxItemPageParam,
			@RequestParam(value = "column", required = false) String computerColumnStr,
			@RequestParam(value = "orderWay", required = false) String orderWayStr,
			@RequestParam(value = "search", required = false) String searchedName,
			Model model) {
		LOG.info(new StringBuilder("doGet(")
			.append(numParam).append(", ")
			.append(maxItemPageParam).append(", ")
			.append(computerColumnStr).append(", ")
			.append(orderWayStr).append(", ")
			.append(searchedName).append(", ")
			.append(model).append(")").toString());
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
		model.addAttribute("page", page);
		return "dashboard";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String delete(@RequestParam("selection") String selectedComputersId, 
			Model model) {
		LOG.info(new StringBuilder("doPost(")
			.append(selectedComputersId).append(", ")
			.append(model).append(")").toString());
		if (selectedComputersId != null) {
			StringTokenizer st = new StringTokenizer(selectedComputersId, ",");
			while (st.hasMoreTokens()) {
				ComputerDTO deleteDTO = service.getById(Long.parseLong(st.nextToken()));
				service.delete(deleteDTO);
			}
		}
		model.addAttribute("page", page);
		return "dashboard";
	}
}

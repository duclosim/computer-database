package com.excilys.computerDatabase.servlet;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardServlet.class);
	private static final long serialVersionUID = -5526661127455358108L;
	
	@Autowired
	private ComputerService service;
	@Autowired
	private Page<ComputerDTO> page;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		LOG.trace("init(" + config + ")");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		page = new Page<>(service);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doGet(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		if (req.getParameterMap().values().isEmpty()) {
			page.setColumn(null);
			page.setSearchedName(null);
			page.setWay(null);
		}
		// Numéro de page.
		String numParam = req.getParameter("pageNum");
		int newPageNum = Page.DEFAULT_PAGE_NUM;
		if (numParam != null) {
			if (!UserInputsValidator.isValidNumber(numParam)) {
				numParam = null;
			} else {
				newPageNum = Integer.parseInt(numParam);
			}
		}
		if (page.getPageNum() != newPageNum) {
			page.setPageNum(newPageNum);
		}
		// Nombre d'objets par page.
		String maxItemPageParam = req.getParameter("itemByPage");
		int newItemByPage = Page.DEFAULT_LIMIT;
		if (maxItemPageParam != null) {
			if (!UserInputsValidator.isValidNumber(maxItemPageParam)) {
				maxItemPageParam = null;
			} else {
				newItemByPage = Integer.parseInt(maxItemPageParam);
			}
		}
		if (page.getMaxNbItemsByPage() != newItemByPage) {
			page.setMaxNbItemsByPage(newItemByPage);
		}
		// Tri
		String computerColumnStr = req.getParameter("column");
		String orderWayStr = req.getParameter("orderWay");
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
			page.setColumn(column);
			page.setWay(way);
		}
		// Recherche sur le nom.
		String searchedName = req.getParameter("search");
		if (UserInputsValidator.isValidString(searchedName)) {
			page.setSearchedName(searchedName);
		}
		// Passage des paramètres de la page dans la requête.
		req.setAttribute("page", page);
		getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doPost(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		String selectedComputersId = req.getParameter("selection");
		if (selectedComputersId == null) {
			req.setAttribute("page", page);
			getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
			return;
		}
		StringTokenizer st = new StringTokenizer(selectedComputersId, ",");
		while (st.hasMoreTokens()) {
			ComputerDTO deleteDTO = new ComputerDTO();
			deleteDTO.setId(st.nextToken());
			service.delete(deleteDTO);
		}
		req.setAttribute("page", page);
		getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
	}
}

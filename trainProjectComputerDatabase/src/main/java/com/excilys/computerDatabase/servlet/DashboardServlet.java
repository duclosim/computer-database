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

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.model.page.NameFilteredPage;
import com.excilys.computerDatabase.model.page.NavigationPage;
import com.excilys.computerDatabase.model.page.OrderedPage;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(DashboardServlet.class);
	private static final long serialVersionUID = -5526661127455358108L;
	private ComputerService service;
	private Page<ComputerDTO> page;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		LOG.trace("init(" + config + ")");
		service = ComputerServiceImpl.INSTANCE;
		page = new NavigationPage<>(service);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doGet(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		try {
			// Recherche sur le nom.
			String searchedName = req.getParameter("search");
			if (searchedName != null && !searchedName.trim().isEmpty()) {
				page = new NameFilteredPage<ComputerDTO>(service, searchedName);
			} else {
				// Page standard.
				String numParam = req.getParameter("pageNum");
				String maxItemPageParam = req.getParameter("itemByPage");
				int newPageNum = NavigationPage.DEFAULT_PAGE_NUM;
				int newItemByPage = NavigationPage.DEFAULT_LIMIT;
				if ((numParam != null) 
						&& (!UserInputsValidator.isValidNumber(numParam))) {
					numParam = null;
				}
				if ((maxItemPageParam != null) 
						&& (!UserInputsValidator.isValidNumber(maxItemPageParam))) {
					maxItemPageParam = null;
				}
				if (numParam != null) {
					newPageNum = Integer.parseInt(numParam);
				}
				if (maxItemPageParam != null) {
					newItemByPage = Integer.parseInt(maxItemPageParam);
				}
				// TODO mettre noms des paramètres
				String computerColumnStr = req.getParameter("column");
				String orderWayStr = req.getParameter("orderWay");
				ComputerColumn column = null;
				OrderingWay way = null;
				if (computerColumnStr != null 
						&& orderWayStr != null
						&& !computerColumnStr.trim().isEmpty()
						&& !orderWayStr.trim().isEmpty()) {
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
					page = new OrderedPage<ComputerDTO>(service, column, way);
				} else {
					page = new NavigationPage<ComputerDTO>(service);
				}
				if (page.getPageNum() != newPageNum) {
					page.setPageNum(newPageNum);
				}
				if (page.getMaxNbItemsByPage() != newItemByPage) {
					page.setMaxNbItemsByPage(newItemByPage);
				}
			}
			page.refresh();
			// Passage des paramètres de la page dans la requête.
			req.setAttribute("page", page);
			getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
		} catch (NumberFormatException e) {
			LOG.error("Erreur de parsing du numéro de page.");
			e.printStackTrace();
			throw new IllegalArgumentException("Pas de numéro de page valide");
		}
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
		page.refresh();
		req.setAttribute("page", page);
		getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
	}
}

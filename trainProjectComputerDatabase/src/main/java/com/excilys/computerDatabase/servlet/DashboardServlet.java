package com.excilys.computerDatabase.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.UserInputsValidator;
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
		LOG.trace("init(" + config + ")");
		service = ComputerServiceImpl.INSTANCE;
		page = new Page<>(service);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doGet(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		try {
			String numParam = req.getParameter("pageNum");
			String maxItemPageParam = req.getParameter("itemByPage");
			int newPageNum = Page.DEFAULT_PAGE_NUM;
			int newItemByPage = Page.DEFAULT_LIMIT;
			if ((numParam != null) && (!UserInputsValidator.isValidNumber(numParam))) {
				numParam = null;
			}
			if ((maxItemPageParam != null) && (!UserInputsValidator.isValidNumber(maxItemPageParam))) {
				maxItemPageParam = null;
			}
			if (numParam != null) {
				newPageNum = Integer.parseInt(numParam);
			}
			if (maxItemPageParam != null) {
				newItemByPage = Integer.parseInt(maxItemPageParam);
			}
			page.setPageNum(newPageNum);
			page.setMaxNbItemsByPage(newItemByPage);
			// Passage des paramètres de la page dans la requête.
			req.setAttribute("page", page);
			getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
		} catch (NumberFormatException e) {
			LOG.error("Erreur de parsing du numéro de page.");
			e.printStackTrace();
			throw new IllegalArgumentException("Pas de numéro de page valide");
		}
	}
}

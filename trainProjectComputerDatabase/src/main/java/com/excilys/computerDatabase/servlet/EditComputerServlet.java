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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(EditComputerServlet.class);
	private static final long serialVersionUID = 423648038487626720L;
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	private ComputerDTO computer;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		LOG.trace("init(" + config + ")");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doGet(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		req.setAttribute("companies", companyService.getAll());
		String beanIdStr = req.getParameter("beanId");
		Long id = Long.parseLong(beanIdStr);
		computer = computerService.getById(id);
		
		req.setAttribute("computerBean", computer);
		getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doPost(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		computer.setName(req.getParameter("computerName"));
		String introducedDate = req.getParameter("introduced");
		String discontinuedDate = req.getParameter("discontinued");
		String companyId = req.getParameter("companyId");
		if (introducedDate != null && introducedDate.trim().isEmpty()) {
			introducedDate = null;
		}
		if (discontinuedDate != null && discontinuedDate.trim().isEmpty()) {
			discontinuedDate = null;
		}
		if (companyId != null && companyId.trim().isEmpty()) {
			companyId = null;
		}
		computer.setIntroducedDate(introducedDate);
		computer.setDiscontinuedDate(discontinuedDate);
		computer.setCompanyId(companyId);
		computerService.update(computer);
		
		req.setAttribute("validMessage", "Computer successfully updated.");
		req.setAttribute("computerBean", computer);
		getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(req, resp);
	}
}

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

import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(AddComputerServlet.class);
	private static final long serialVersionUID = 6902766188799864148L;
	
	@Autowired
	private CompanyServiceImpl companyService;
	@Autowired
	private ComputerServiceImpl computerService;
	
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
		getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doPost(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		ComputerDTO computerDTO = new ComputerDTO();
		computerDTO.setName(req.getParameter("computerName"));
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
		computerDTO.setIntroducedDate(introducedDate);
		computerDTO.setDiscontinuedDate(discontinuedDate);
		computerDTO.setCompanyId(companyId);
		computerService.create(computerDTO);
		
		req.setAttribute("validMessage", "Computer successfully added : " + computerDTO.toString());
		getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(req, resp);
	}
}

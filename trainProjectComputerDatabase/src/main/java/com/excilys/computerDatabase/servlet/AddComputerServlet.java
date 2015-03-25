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

import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.cli.runners.DeleteCompany;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(DeleteCompany.class);
	private static final long serialVersionUID = 6902766188799864148L;
	
	private CompanyService companyService = CompanyServiceImpl.INSTANCE;
	private ComputerService computerService = ComputerServiceImpl.INSTANCE;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		LOG.trace("init(" + config + ")");
		companyService = CompanyServiceImpl.INSTANCE;
		computerService = ComputerServiceImpl.INSTANCE;
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
		computerDTO.setIntroducedDate(req.getParameter("introduced"));
		computerDTO.setDiscontinuedDate(req.getParameter("discontinued"));
		computerDTO.setCompanyId(req.getParameter("companyId"));
		computerService.create(computerDTO);
		
		req.setAttribute("validMessage", "Computer successfully updated : " + computerDTO.toString());
		getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(req, resp);
	}
}

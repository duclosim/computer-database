package com.excilys.computerDatabase.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 423648038487626720L;
	
	CompanyService companyService = CompanyServiceImpl.INSTANCE;
	ComputerService computerService = ComputerServiceImpl.INSTANCE;
	private Computer bean;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("companies", companyService.getAll());
		String beanIdStr = req.getParameter("beanId");
		if ((beanIdStr != null) && (!beanIdStr.trim().isEmpty())) {
			try {
				Long id = Long.parseLong(beanIdStr);
				bean = computerService.getById(id);
				req.setAttribute("computerBean", bean);
			} catch (NumberFormatException e) {
				System.err.println("Nombre impossible à parser en Long.");
				e.printStackTrace();
				throw new IllegalArgumentException("Erreur parsing.");
			}
		}
		getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuilder errorMessage = new StringBuilder("");
		Company companyBean = null;
		String name = req.getParameter("computerName");
		String introducedDate = req.getParameter("introduced");
		String discontinuedDate = req.getParameter("discontinued");
		String companyIdStr = req.getParameter("companyId");
		
		if ((name == null) || (name.trim().isEmpty())) {
			errorMessage.append("Nom non valide.\n");
			name = null;
		}
		if ((introducedDate != null) && (!UserInputsValidator.isValidDate(introducedDate))) {
			errorMessage.append("Date d'introduction non valide.\n");
			introducedDate = null;
		}
		if ((discontinuedDate != null) && (!UserInputsValidator.isValidDate(discontinuedDate))) {
			errorMessage.append("Date de sortie non valide.\n");
			discontinuedDate = null;
		}
		if ((companyIdStr != null) && (!UserInputsValidator.isValidNumber(companyIdStr))) {
			errorMessage.append("Numéro de companie non valide.\n");
			companyIdStr = null;
		} else {
			try {
				if (companyIdStr != null) {
					Long companyIdLg = Long.parseLong(companyIdStr);
					companyBean = companyService.getById(companyIdLg);
				}
			} catch (NumberFormatException e) {
				System.err.println("Nombre impossible à parser en Long.");
				e.printStackTrace();
				throw new IllegalArgumentException("Erreur parsing.");
			}
			if (companyBean == null) {
				errorMessage.append("Le numéro de companie est correct mais ne correspond à aucune entrée.\n");
			}
		}
		if (name == null) {
			req.setAttribute("errorMessage", errorMessage);
			getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(req, resp);
		}
		bean.setName(name);
		try {
			if (introducedDate != null) {
				bean.setIntroduced(LocalDateTime.parse(introducedDate));
			}
			if (discontinuedDate != null) {
				bean.setDiscontinued(LocalDateTime.parse(discontinuedDate));
			}
		} catch (DateTimeParseException e) {
			System.err.println("Dates impossible à parser.");
			e.printStackTrace();
			throw new IllegalArgumentException("Erreur parsing.");
		}
		bean.setCompany(companyBean);
		
		computerService.update(bean);
		req.setAttribute("validMessage", "Computer successfully updated.");
		req.setAttribute("computerBean", bean);
		getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(req, resp);
	}
}

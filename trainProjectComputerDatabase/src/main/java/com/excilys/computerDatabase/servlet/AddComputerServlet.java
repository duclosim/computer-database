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
import com.excilys.computerDatabase.model.beans.CompanyBean;
import com.excilys.computerDatabase.model.beans.ComputerBean;
import com.excilys.computerDatabase.persistence.dao.CRUDDao;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 6902766188799864148L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuilder errorMessage = new StringBuilder("");
		CRUDDao<CompanyBean> companyDao = CompanyDAOImpl.INSTANCE;
		CRUDDao<ComputerBean> computerDao = ComputerDAOImpl.INSTANCE;
		ComputerBean computerBean = new ComputerBean();
		CompanyBean companyBean = null;
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
				Long companyIdLg = Long.parseLong(companyIdStr);
				companyBean = companyDao.getById(companyIdLg);
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
			getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(req, resp);
		}
		computerBean.setName(name);
		try {
			computerBean.setIntroduced(LocalDateTime.parse(introducedDate));
			computerBean.setDiscontinued(LocalDateTime.parse(discontinuedDate));
		} catch (DateTimeParseException e) {
			System.err.println("Dates impossible à parser.");
			e.printStackTrace();
			throw new IllegalArgumentException("Erreur parsing.");
		}
		computerBean.setCompany(companyBean);
		
		computerDao.create(computerBean);
		getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
	}
}

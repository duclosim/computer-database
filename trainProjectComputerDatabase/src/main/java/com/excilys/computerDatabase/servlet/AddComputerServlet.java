package main.java.com.excilys.computerDatabase.servlet;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.excilys.computerDatabase.model.UserInputsValidator;
import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.model.beans.ComputerBean;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

public class AddComputerServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 6902766188799864148L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ComputerBean computerBean = new ComputerBean();
		String name = req.getParameter("computerName");
		String introducedDate = req.getParameter("introduced");
		String discontinuedDate = req.getParameter("discontinued");
		String companyIdStr = req.getParameter("companyId");
		CompanyBean companyBean = null;
		
		if ((name == null) || (name.trim().isEmpty())) {
			System.out.println("Nom non valide.");
			name = null;
		}
		if ((introducedDate != null) && (!UserInputsValidator.isValidDate(introducedDate))) {
			System.out.println("Date d'introduction non valide.");
			introducedDate = null;
		}
		if ((discontinuedDate != null) && (!UserInputsValidator.isValidDate(discontinuedDate))) {
			System.out.println("Date de sortie non valide.");
			discontinuedDate = null;
		}
		if ((companyIdStr != null) && (!UserInputsValidator.isValidNumber(companyIdStr))) {
			System.out.println("Numéro de companie non valide.");
			companyIdStr = null;
		} else {
			Long companyIdLg = Long.parseLong(companyIdStr);
			companyBean = CompanyDAOImpl.INSTANCE.getById(companyIdLg);
			if (companyBean == null) {
				System.out.println("Le numéro de companie est correct mais ne correspond à aucune entrée.");
			}
		}
		
		computerBean.setName(name);
		computerBean.setIntroduced(LocalDateTime.parse(introducedDate));
		computerBean.setDiscontinued(LocalDateTime.parse(discontinuedDate));
		computerBean.setCompany(companyBean);
		

	}
}

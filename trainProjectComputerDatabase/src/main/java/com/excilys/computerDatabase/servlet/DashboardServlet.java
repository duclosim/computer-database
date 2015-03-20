package com.excilys.computerDatabase.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.model.beans.ComputerBean;
import com.excilys.computerDatabase.persistence.dao.CRUDDao;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = -5526661127455358108L;
	private CRUDDao<ComputerBean> dao= ComputerDAOImpl.INSTANCE;
	private Page<ComputerBean> page;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String numParam = req.getParameter("pageNum");
			int newPageNum = 1;
			if ((numParam != null) && (!UserInputsValidator.isValidNumber(numParam))) {
				req.setAttribute("errorMessage", new String("Numéro de page invalide."));
				this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
			}
			if (numParam != null) {
				newPageNum = Integer.parseInt(numParam);
			}
			page = new Page<ComputerBean>(dao);
			page.setPageNum(newPageNum);
			req.setAttribute("curPage", page.getPageNum());
			req.setAttribute("maxPage", page.getLastPageNb());
			req.setAttribute("entities", page.getEntities());
			req.setAttribute("nbLines", page.getTotalNbEntities());
			getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
		} catch (NumberFormatException e) {
			System.err.println("Erreur de parse du numéro de page.");
			e.printStackTrace();
			throw new IllegalArgumentException("Pas de numéro de page valide");
		}
	}
}

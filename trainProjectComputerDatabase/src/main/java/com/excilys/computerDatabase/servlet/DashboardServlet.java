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
			String maxItemPageParam = req.getParameter("itemByPage");
			StringBuilder sb = new StringBuilder("");
			int newPageNum = Page.DEFAULT_PAGE_NUM;
			int newItemByPage = Page.DEFAULT_LIMIT;
			// Construction de la Page.
			if (page == null) {
				page = new Page<ComputerBean>(dao);
			}
			if ((numParam != null) && (!UserInputsValidator.isValidNumber(numParam))) {
				sb.append("Numéro de page invalide.\n");
			}
			if ((maxItemPageParam != null) && (!UserInputsValidator.isValidNumber(maxItemPageParam))) {
				sb.append("Nombre d'objets par page invalide.\n");
			}
			if (!sb.toString().isEmpty()) {
				req.setAttribute("errorMessage", sb.toString());
				this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
				return;
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
			req.setAttribute("startPage", page.getStartingPage());
			req.setAttribute("endPage", page.getFinishingPage());
			getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
		} catch (NumberFormatException e) {
			System.err.println("Erreur de parsing du numéro de page.");
			e.printStackTrace();
			throw new IllegalArgumentException("Pas de numéro de page valide");
		}
	}
}

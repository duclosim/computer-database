package com.excilys.computerDatabase.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/500")
public class ErrorServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = -2760351849206909874L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp").forward(req, resp);
	}
}
package com.excilys.computerDatabase.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/500")
public class ErrorServlet extends HttpServlet implements Servlet {
	private static final Logger LOG = LoggerFactory.getLogger(ErrorServlet.class);

	private static final long serialVersionUID = -2760351849206909874L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.trace(new StringBuilder("doGet(")
			.append(req).append(", ")
			.append(resp).append(")").toString());
		getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp").forward(req, resp);
	}
}
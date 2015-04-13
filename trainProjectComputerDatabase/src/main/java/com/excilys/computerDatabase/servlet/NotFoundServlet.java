package com.excilys.computerDatabase.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/404")
public class NotFoundServlet {
	private static final Logger LOG = LoggerFactory.getLogger(NotFoundServlet.class);

	public String get() {
		LOG.info("get()");
		return "404";
	}
}
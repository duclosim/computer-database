package com.excilys.computerDatabase.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/403")
public class UnauthorizedServlet {
	private static final Logger LOG = LoggerFactory.getLogger(UnauthorizedServlet.class);

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		LOG.info("get()");
		return "403";
	}
}

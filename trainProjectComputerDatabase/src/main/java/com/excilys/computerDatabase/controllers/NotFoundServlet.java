package com.excilys.computerDatabase.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/404")
public class NotFoundServlet {
	private static final Logger LOG = LoggerFactory.getLogger(NotFoundServlet.class);

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		LOG.info("get()");
		return "404";
	}
}
package com.excilys.computerDatabase.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/500")
public class ErrorServlet {
	private static final Logger LOG = LoggerFactory.getLogger(ErrorServlet.class);

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		LOG.info("get()");
		return "500";
	}
}
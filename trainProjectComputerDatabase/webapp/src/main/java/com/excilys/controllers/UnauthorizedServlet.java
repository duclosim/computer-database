package com.excilys.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * This servlet manages the access to Http error code 403 custom page.
 * @author excilys
 *
 */
@Controller
@RequestMapping("/403")
public class UnauthorizedServlet {
	private static final Logger LOG = LoggerFactory.getLogger(UnauthorizedServlet.class);

	/**
	 * Redirect the page to the 403 custom error jsp.
	 * @return A ModelAndView redirecting to the matching jsp.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView get() {
		LOG.trace("get()");
		return new ModelAndView("403");
	}
}

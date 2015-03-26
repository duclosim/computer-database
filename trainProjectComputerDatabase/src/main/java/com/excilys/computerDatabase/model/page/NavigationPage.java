package com.excilys.computerDatabase.model.page;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.service.PageableService;

/**
 * Cette classe représente une page contenant des objets pour les 
 *   présenter sous une forme plus lisible.
 * @author excilys
 *
 * @param <T> La classe des objets que doivent contenir les pages.
 */
public class NavigationPage<T> extends Page<T> {

	private static final Logger LOG = LoggerFactory.getLogger(NavigationPage.class);
	
	public NavigationPage(PageableService<T> service) {
		super(service);
		LOG.trace(new StringBuilder("new NavigationPage(")
			.append(service).append(")")
			.toString());
	}
	
	public NavigationPage(PageableService<T> service, int limit, int offset) {
		super(service, limit, offset);
		LOG.trace(new StringBuilder("new NavigationPage(")
			.append(service).append(", ")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
	}

	@Override
	public void refresh() {
		LOG.trace("refresh()");
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		setEntities(new ArrayList<>(getService().getAll(getMaxNbItemsByPage(), offset)));
		setLastPageNb(getService().countLines() / getMaxNbItemsByPage() + 1);
		if (getPageNum() > getLastPageNb()) {
			setPageNum(getLastPageNb());
		}
	}
}

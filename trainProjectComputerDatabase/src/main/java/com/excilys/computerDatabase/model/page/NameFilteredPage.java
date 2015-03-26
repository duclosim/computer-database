package com.excilys.computerDatabase.model.page;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.service.PageableService;

public class NameFilteredPage<T> extends Page<T> {
	private static final Logger LOG = LoggerFactory.getLogger(NameFilteredPage.class);
	
	private String searchedName;
	
	public NameFilteredPage(PageableService<T> service, int limit, 
			int offset, String searchedName) {
		super(service, limit, offset);
		LOG.trace(new StringBuilder("new NameFilteredPage(")
			.append(service).append(", ")
			.append(limit).append(", ")
			.append(offset).append(", ")
			.append(searchedName).append(")")
			.toString());
		this.searchedName = searchedName;
		setMaxNbItemsByPage(getTotalNbEntities());
	}

	public NameFilteredPage(PageableService<T> service, 
			String searchedName) {
		super(service);
		LOG.trace(new StringBuilder("new NameFilteredPage(")
			.append(service).append(", ")
			.append(searchedName).append(")")
			.toString());
		this.searchedName = searchedName;
		setMaxNbItemsByPage(getTotalNbEntities());
	}

	public String getSearchedName() {
		return searchedName;
	}
	public void setSearchedName(String searchedName) {
		this.searchedName = searchedName;
	}

	@Override
	public void reloadEntities() {
		LOG.trace("reloadEntities()");
		setEntities(new ArrayList<>(getService()
				.getByNameOrCompanyName(searchedName)));
	}

}

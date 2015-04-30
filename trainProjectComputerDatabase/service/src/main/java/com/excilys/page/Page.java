package com.excilys.page;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.model.beans.Computer;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.services.ComputerService;
import com.excilys.validators.UserInputsValidator;

@Component
/**
 *
 * @author excilys
 * This class represents an Computer wrapper, which allows to access to
 *   a ComputerService and presents objects with ways to navigate between
 *   part of the entire database, and ordering and filtering methods.
 */
public class Page {
	private static final Logger LOG = LoggerFactory.getLogger(Page.class);
	
	/**
	 * The number of the pages reachable backward or forward for navigation purposes.
	 */
	public static final int WIDTH = 3;
	
	/**
	 * The default maximum number of objects presented by the Page.
	 */
	public static final int DEFAULT_LIMIT = 10;
	
	/**
	 * The default number of ignored rows presented by the Page.
	 */
	public static final int DEFAULT_OFFSET = 0;
	
	/**
	 * The default starting page of Page objects.
	 */
	public static final int DEFAULT_PAGE_NUM = 1;
	
	@Autowired
	private ComputerService service;
	private int maxNbItemsByPage;
	private int pageNum;
    
	private String searchedName;

	private ComputerColumn column;
	private OrderingWay way;

	// Constructeurs
	/*
	 * Creates a new Page with a limited number of objects loaded,
	 *   and objects loaded only from a certain row number.
	 * @param offset The number of the first row to get from the service.
	 * @param limit The maximum number of objects to be retrieved from the service.
	 */
	private Page(int limit, int offset) {
		LOG.trace(new StringBuilder("new Page(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul");
			throw new IllegalArgumentException("limit est négatif ou nul");
		}
		if (offset < 0) {
			LOG.error("offset est négatif");
			throw new IllegalArgumentException("offset est négatif");
		}
		this.maxNbItemsByPage = limit;
		pageNum = offset / getMaxNbItemsByPage() + 1;
	}
	
	/**
	 * Creates a new Page object, with default limit and offset.
	 */
	public Page() {
		this(DEFAULT_LIMIT, DEFAULT_OFFSET);
		LOG.trace("new Page()");
	}

	// Requetes
	/**
	 * Returns the current service in use.
	 * @return A ComputerService.
	 */
	public ComputerService getService() {
		return service;
	}
	/**
	 * Returns the entities wrapped by this Page.
	 * @return A List of Computer.
	 */
	public List<Computer> getEntities() {
		return reloadEntities();
	}
	/**
	 * Returns the name the wrapped Computer are filtered by.
	 * @return A String containing searchedName.
	 */
	public String getSearchedName() {
		return searchedName;
	}
	/**
	 * Returns the maximum number of objects wrapped by this Page object.
	 * @return An int representing the maximum number of objects wrapped.
	 */
	public int getMaxNbItemsByPage() {
		return maxNbItemsByPage;
	}
	/**
	 * Returns the current page number.
	 * @return An int representing the current page number.
	 */
	public int getPageNum() {
		return pageNum;
	}
	
	/**
	 * Returns the total number of objects wrapped by this Page object.
	 * @return An int representing the number of objects wrapped by 
	 *   this Page object.
	 */
	public int getTotalNbEntities() {
		int result;
		if (UserInputsValidator.isValidString(searchedName)) {
			result = service.countFilteredLines(searchedName);
		} else {
			result = service.countAllLines();
		}
		return result;
	}
	
	/**
	 * Returns the last accessible page number reachable by this Page object.
	 * @return An int representing the last accessible page number 
	 *   reachable by this Page object.
	 */
	public int getLastPageNb() {
		int lastPageNb = getTotalNbEntities() / 
				maxNbItemsByPage;
		if (getTotalNbEntities() % maxNbItemsByPage != 0) {
			++lastPageNb;
		}
		return lastPageNb;
	}
	
	/**
	 * Returns the column name the objects are ordered by.
	 * @return A String containing the column name the objects are ordered by.
	 */
	public String getColumn() {
		if (column == null) {
			return null;
		} else {
			return column.getColumnName();
		}
	}
	
	/**
	 * Returns the way the objects are ordered by, in addition with 
	 *   the column name.
	 * @return A String containing either null, ASC or DESC.
	 */
	public String getWay() {
		if (column == null) {
			return null;
		} else {
			return way.getWay();
		}
	}

	/**
	 * Returns the number of the page reachable backward 
	 *   for navigation purpose.
	 * @return Integer.max(getCurrentPage - WIDTH, 1)
	 */
    public int getStartingPage() {
		LOG.trace("getStartingPage()");
		return Integer.max(1, getPageNum() - WIDTH);
	}

	/**
	 * Returns the number of the page reachable forward 
	 *   for navigation purpose.
	 * @return Integer.min(getCurrentPage + WIDTH, getLastPageNb)
	 */
	public int getFinishingPage() {
		LOG.trace("getFinishingPage()");
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}
	
	// Commandes
	/**
	 * Set the service.
	 * @param service The new service.
	 */
	public void setService(ComputerService service) {
		this.service = service;
	}
	/**
	 * Set the searched name.
	 * @param searchedName The new searched name.
	 */
	public void setSearchedName(String searchedName) {
		this.searchedName = searchedName;
	}
	/**
	 * Set the ordering column.
	 * @param column The new ordering column.
	 */
	public void setColumn(ComputerColumn column) {
		this.column = column;
	}
	/**
	 * Set the ordering way.
	 * @param way ASC or DESC.
	 */
	public void setWay(OrderingWay way) {
		this.way = way;
	}

	/**
	 * Modify the maximum number of objects wrapped in this Page object.
	 * @param maxNbItemsByPage The new maximum number of items 
	 *   wrapped by this Page object.
	 */
	public void setMaxNbItemsByPage(int maxNbItemsByPage) {
		LOG.trace("setMaxNbItemsByPage(" + maxNbItemsByPage + ")");
		if (maxNbItemsByPage < 0) {
			LOG.error("maxNbItemsByPage est négatif.");
			throw new IllegalArgumentException("maxNbItemsByPage est négatif.");
		}
		this.maxNbItemsByPage = maxNbItemsByPage;
		if (getPageNum() > getLastPageNb()) {
			setPageNum(getLastPageNb());
		}
	}
	/**
	 * Move to another page.
	 * @param pageNum The new current page number, 
	 *   1 <= pageNum && pageNum <= getLastPageNb
	 */
	public void setPageNum(int pageNum) {
		LOG.trace("setPageNum(" + pageNum + ")");
		if ((0 >= pageNum) || (pageNum > getLastPageNb())) {
			LOG.error("pageNum est hors-limites.");
			throw new IllegalArgumentException("pageNum est hors-limites.");
		}
		this.pageNum = pageNum;
	}

    // OUTILS
	/*
	 * This method reload the Computer wrapped by this Page object.
	 */
	private List<Computer> reloadEntities() {
		LOG.trace("reloadEntities()");
		List<Computer> entities;
		boolean research = UserInputsValidator.isValidString(searchedName);
		boolean ordering = ((column != null) && (way != null));
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		if (research) {
			if (ordering) {
				// filtré et ordonné
				entities = service.getFilteredAndOrdered(
						getMaxNbItemsByPage(), offset, searchedName, 
						column, way);
			} else {
				// filtré mais pas ordonné
				entities = service.getFiltered(
						searchedName, getMaxNbItemsByPage(), offset);
			}
		} else {
			if (ordering) {
				// ordonné mais non filtré
				entities = service.getOrdered(
						getMaxNbItemsByPage(), offset, column, way);
			} else {
				// ni filtré ni ordonné
				entities = service.getAll(getMaxNbItemsByPage(), offset);
			}
		}
		return entities;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (Computer entity : getEntities()) {
			res.append(entity).append('\n');
		}
		return res.toString();
	}

}

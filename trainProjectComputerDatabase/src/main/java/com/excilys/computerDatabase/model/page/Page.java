package com.excilys.computerDatabase.model.page;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.PageableService;

public class Page<T> {
	private static final Logger LOG = LoggerFactory.getLogger(Page.class);
	private static final int WIDTH = 3;
	
	public static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 0;
	public static final int DEFAULT_PAGE_NUM = 1;
	
	private PageableService<T> service;
	private List<T> entities;
	private int maxNbItemsByPage;
	private int pageNum;
    
	private String searchedName;

	private ComputerColumn column;
	private OrderingWay way;

	// Constructeurs
	/**
	 * 
	 * @param service
	 */
	public Page(PageableService<T> service) {
		this(service, DEFAULT_LIMIT, DEFAULT_OFFSET);
		LOG.trace("new Page(" + service + ")");
	}
	
	/**
	 * 
	 * @param service
	 * @param offset
	 * @param limit
	 */
	public Page(PageableService<T> service, int limit, int offset) {
		LOG.trace(new StringBuilder("new Page(")
			.append(service).append(", ")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		if (service == null) {
			LOG.error("service est à null.");
			throw new IllegalArgumentException("service est à null.");
		}
		if (limit <= 0) {
			LOG.error("limit est négatif ou nul");
			throw new IllegalArgumentException("limit est négatif ou nul");
		}
		if (offset < 0) {
			LOG.error("offset est négatif");
			throw new IllegalArgumentException("offset est négatif");
		}
		this.service = service;
		this.maxNbItemsByPage = limit;
		pageNum = offset / getMaxNbItemsByPage() + 1;
		refresh();
	}
	
	// Requetes
	public PageableService<T> getService() {
		return service;
	}
	public List<T> getEntities() {
		return entities;
	}
	public String getSearchedName() {
		return searchedName;
	}
	public int getMaxNbItemsByPage() {
		return maxNbItemsByPage;
	}
	public int getPageNum() {
		return pageNum;
	}
	public int getTotalNbEntities() {
		return service.countLines();
	}
	public int getLastPageNb() {
		int lastPageNb = service.countLines() / 
				maxNbItemsByPage;
		if (service.countLines() % maxNbItemsByPage != 0) {
			++lastPageNb;
		}
		return lastPageNb;
	}
	public String getColumn() {
		return column.getColumnName();
	}
	public String getWay() {
		return way.getWay();
	}
    public int getStartingPage() {
		LOG.trace("getStartingPage()");
		return Integer.max(1, getPageNum() - WIDTH);
	}
	public int getFinishingPage() {
		LOG.trace("getFinishingPage()");
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}
	
	// Commandes
	public void setService(PageableService<T> service) {
		this.service = service;
		refresh();
	}
	public void setSearchedName(String searchedName) {
		this.searchedName = searchedName;
		refresh();
	}
	public void setColumn(ComputerColumn column) {
		this.column = column;
		refresh();
	}
	public void setWay(OrderingWay way) {
		this.way = way;
		refresh();
	}

	/**
	 * Modifie le nombre d'entités contenues dans une seule page.
	 * @param maxNbItemsByPage
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
		refresh();
	}
	/**
	 * Se déplace de la page en cours vers une nouvelle page.
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		LOG.trace("setPageNum(" + pageNum + ")");
		if ((0 >= pageNum) || (pageNum > getLastPageNb())) {
			LOG.error("pageNum est hors-limites.");
			throw new IllegalArgumentException("pageNum est hors-limites.");
		}
		this.pageNum = pageNum;
		refresh();
	}

    // OUTILS
	/**
	 * Cette méthode recharge les entités en raison de changements 
	 *   de page ou du nombre d'objets par page.
	 */
	public void refresh() {
		LOG.trace("setPageNum(" + pageNum + ")");
		reloadEntities();
	}
	
	public void reloadEntities() {
		// TODO écrire code des getAll()
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
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (T entity : entities) {
			res.append(entity).append('\n');
		}
		return res.toString();
	}

}

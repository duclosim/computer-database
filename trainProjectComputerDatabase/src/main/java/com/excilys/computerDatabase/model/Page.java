package com.excilys.computerDatabase.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.ComputerServiceImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;

@Component
public class Page {
	private static final Logger LOG = LoggerFactory.getLogger(Page.class);
	private static final int WIDTH = 3;
	
	public static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 0;
	public static final int DEFAULT_PAGE_NUM = 1;
	
	@Autowired
	private ComputerServiceImpl service;
	private int maxNbItemsByPage;
	private int pageNum;
    
	private String searchedName;

	private ComputerColumn column;
	private OrderingWay way;

	// Constructeurs
	/**
	 * 
	 * @param service
	 * @param offset
	 * @param limit
	 */
	public Page(int limit, int offset) {
		LOG.info(new StringBuilder("new Page(")
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
	 * 
	 * @param service
	 */
	public Page() {
		this(DEFAULT_LIMIT, DEFAULT_OFFSET);
		LOG.info("new Page()");
	}
	
	// Requetes
	public ComputerServiceImpl getService() {
		return service;
	}
	public List<ComputerDTO> getEntities() {
		return reloadEntities();
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
		int result = 0;
		if (UserInputsValidator.isValidString(searchedName)) {
			result = service.countFilteredLines(searchedName);
		} else {
			result = service.countAllLines();
		}
		return result;
	}
	public int getLastPageNb() {
		int lastPageNb = getTotalNbEntities() / 
				maxNbItemsByPage;
		if (getTotalNbEntities() % maxNbItemsByPage != 0) {
			++lastPageNb;
		}
		return lastPageNb;
	}
	public String getColumn() {
		if (column == null) {
			return null;
		} else {
			return column.getColumnName();
		}
	}
	public String getWay() {
		if (column == null) {
			return null;
		} else {
			return way.getWay();
		}
	}
    public int getStartingPage() {
		LOG.info("getStartingPage()");
		return Integer.max(1, getPageNum() - WIDTH);
	}
	public int getFinishingPage() {
		LOG.info("getFinishingPage()");
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}
	
	// Commandes
	public void setService(ComputerServiceImpl service) {
		this.service = service;
	}
	public void setSearchedName(String searchedName) {
		this.searchedName = searchedName;
	}
	public void setColumn(ComputerColumn column) {
		this.column = column;
	}
	public void setWay(OrderingWay way) {
		this.way = way;
	}

	/**
	 * Modifie le nombre d'entités contenues dans une seule page.
	 * @param maxNbItemsByPage
	 */
	public void setMaxNbItemsByPage(int maxNbItemsByPage) {
		LOG.info("setMaxNbItemsByPage(" + maxNbItemsByPage + ")");
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
	 * Se déplace de la page en cours vers une nouvelle page.
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		LOG.info("setPageNum(" + pageNum + ")");
		if ((0 >= pageNum) || (pageNum > getLastPageNb())) {
			LOG.error("pageNum est hors-limites.");
			throw new IllegalArgumentException("pageNum est hors-limites.");
		}
		this.pageNum = pageNum;
	}

    // OUTILS
	/**
	 * Cette méthode recharge les entités en raison de changements 
	 *   de page ou du nombre d'objets par page.
	 */
	private List<ComputerDTO> reloadEntities() {
		LOG.info("reloadEntities()");
		List<ComputerDTO> entities = null;
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
		for (ComputerDTO entity : getEntities()) {
			res.append(entity).append('\n');
		}
		return res.toString();
	}

}

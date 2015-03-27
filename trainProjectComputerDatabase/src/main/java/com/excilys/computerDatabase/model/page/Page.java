package com.excilys.computerDatabase.model.page;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.service.PageableService;

public abstract class Page<T> {
	private static final Logger LOG = LoggerFactory.getLogger(Page.class);
	private static final int WIDTH = 3;
	
	public static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 0;
	public static final int DEFAULT_PAGE_NUM = 1;
	
	private PageableService<T> service;
	private List<T> entities;
	private int maxNbItemsByPage;
	private int pageNum;
	private int lastPageNb;
    private int totalNbEntities;

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

	// REQUETES
	
	
	public List<T> getEntities() {
		LOG.trace("getEntities()");
		return entities;
	}
	public PageableService<T> getService() {
		return service;
	}

	public int getMaxNbItemsByPage() {
		LOG.trace("getMaxNbItemsByPage");
		return maxNbItemsByPage;
	}
	/**
	 * Retourne le numéro de la dernière page accessible avec setPageNum().
	 * @return
	 */
	public int getLastPageNb() {
		LOG.trace("getLastPageNb()");
		return lastPageNb;
	}
    /**
     *
     * @return
     */
    public int getTotalNbEntities() {
        return totalNbEntities;
    }
	/**
	 * Retourne le numéro de la page courante.
	 * @return
	 */
	public int getPageNum() {
		LOG.trace("getPageNum()");
		return pageNum;
	}
	/**
	 * 
	 * @return
	 */
    public int getStartingPage() {
		LOG.trace("getStartingPage()");
		return Integer.max(1, getPageNum() - WIDTH);
	}
    /**
     * 
     * @return
     */
	public int getFinishingPage() {
		LOG.trace("getFinishingPage()");
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}

	// COMMANDES
	/**
	 * 
	 * @param entities
	 */
	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	public void setService(PageableService<T> service) {
		this.service = service;
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
		refresh();
	}
	/**
	 * Se déplace de la page en cours vers une nouvelle page.
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		LOG.trace("setPageNum(" + pageNum + ")");
		if ((0 >= pageNum) || (pageNum > lastPageNb)) {
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
		totalNbEntities = service.countLines();
		lastPageNb = service.countLines() / 
				maxNbItemsByPage;
		if (service.countLines() % maxNbItemsByPage != 0) {
			++lastPageNb;
		}
		if (getPageNum() > getLastPageNb()) {
			setPageNum(getLastPageNb());
		}
	}
	
	public abstract void reloadEntities();
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (T entity : entities) {
			res.append(entity).append('\n');
		}
		return res.toString();
	}

}

package com.excilys.computerDatabase.model.page;

import java.util.ArrayList;
import java.util.List;

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
public class NavigationPage<T> {
	private static final Logger LOG = LoggerFactory.getLogger(NavigationPage.class);
	private static final int WIDTH = 3;
	
	public static final int DEFAULT_LIMIT = 10;
	public static final int DEFAULT_OFFSET = 0;
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
	public NavigationPage(PageableService<T> service) {
		this(service, DEFAULT_LIMIT, DEFAULT_OFFSET);
		LOG.trace("new Page(" + service + ")");
	}
	
	/**
	 * 
	 * @param service
	 * @param offset
	 * @param limit
	 */
	public NavigationPage(PageableService<T> service, int limit, int offset) {
		LOG.trace(new StringBuilder("new Page(")
			.append(service).append(", ")
			.append(limit).append(",')")
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
		totalNbEntities = service.countLines();
		refresh();
	}

	// REQUETES
	public List<T> getEntities() {
		LOG.trace("getEntities()");
		return entities;
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
	 * Retourne le numéro de la page courante.
	 * @return
	 */
	public int getPageNum() {
		LOG.trace("getPageNum()");
		return pageNum;
	}
	
	public int getTotalNbEntities() {
		LOG.trace("getTotalNbENtities()");
		return totalNbEntities;
	}
	public int getStartingPage() {
		LOG.trace("getStartingPage()");
		return Integer.max(1, getPageNum() - WIDTH);
	}
	public int getFinishingPage() {
		LOG.trace("getFinishingPage()");
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}

	// COMMANDES
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
	/**
	 * Va à la prochaine page si on y est pas déjà.
	 */
	public void goToNextPage() {
		LOG.trace("goToNextPage()");
		if (getPageNum() != getLastPageNb()) {
			setPageNum(pageNum + 1);
		}
	}
	/**
	 * Va à la page précédente si on y est pas déjà.
	 */
	public void goToPreviousPage() {
		LOG.trace("goToPreviousPage()");
		if (getPageNum() != 0) {
			setPageNum(pageNum - 1);
		}
	}
	
	// OUTILS
	/**
	 * Cette méthode recharge les entités en raison de changements 
	 *   de page ou du nombre d'objets par page.
	 */
	public void refresh() {
		LOG.trace("refresh()");
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		entities = new ArrayList<>(service.getAll(getMaxNbItemsByPage(), offset));
		lastPageNb = service.countLines() / getMaxNbItemsByPage() + 1;
		if (pageNum > lastPageNb) {
			setPageNum(lastPageNb);
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

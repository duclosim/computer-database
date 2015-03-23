package com.excilys.computerDatabase.model;

import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDatabase.persistence.dao.CRUDDao;

/**
 * Cette classe représente une page contenant des objets pour les 
 *   présenter sous une forme plus lisible.
 * @author excilys
 *
 * @param <T> La classe des objets que doivent contenir les pages.
 */
public class Page<T> {
	public static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 0;
	private static final int WIDTH = 3;
	public static final int DEFAULT_PAGE_NUM = 1;
	
	private CRUDDao<T> dao;
	private List<T> entities;
	private int maxNbItemsByPage;
	private int pageNum;
	private int lastPageNb;
	private int totalNbEntities;

	/**
	 * 
	 * @param dao
	 */
	public Page(CRUDDao<T> dao) {
		this(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	/**
	 * 
	 * @param dao
	 * @param offset
	 * @param limit
	 */
	public Page(CRUDDao<T> dao, int limit, int offset) {
		if (dao == null) {
			throw new IllegalArgumentException("dao est à null.");
		}
		if (limit <= 0) {
			throw new IllegalArgumentException("limit est négatif ou nul");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("offset est négatif");
		}
		this.dao = dao;
		this.maxNbItemsByPage = limit;
		pageNum = offset / getMaxNbItemsByPage() + 1;
		totalNbEntities = dao.countLines();
		refresh();
	}

	// REQUETES
	public CRUDDao<T> getDao() {
		return dao;
	}
	public List<T> getEntities() {
		return entities;
	}
	public int getMaxNbItemsByPage() {
		return maxNbItemsByPage;
	}
	/**
	 * Retourne le numéro de la dernière page accessible avec setPageNum().
	 * @return
	 */
	public int getLastPageNb() {
		return lastPageNb;
	}
	/**
	 * Retourne le numéro de la page courante.
	 * @return
	 */
	public int getPageNum() {
		return pageNum;
	}
	
	public int getTotalNbEntities() {
		return totalNbEntities;
	}
	public int getStartingPage() {
		return Integer.max(1, getPageNum() - WIDTH);
	}
	public int getFinishingPage() {
		return Integer.min(getLastPageNb(), getPageNum() + WIDTH);
	}

	// COMMANDES
	/**
	 * Modifie le nombre d'entités contenues dans une seule page.
	 * @param maxNbItemsByPage
	 */
	public void setMaxNbItemsByPage(int maxNbItemsByPage) {
		if (maxNbItemsByPage < 0) {
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
		if ((0 >= pageNum) || (pageNum > lastPageNb)) {
			throw new IllegalArgumentException("pageNum est hors-limites.");
		}
		this.pageNum = pageNum;
		refresh();
	}
	/**
	 * Va à la prochaine page si on y est pas déjà.
	 */
	public void goToNextPage() {
		if (getPageNum() != getLastPageNb()) {
			setPageNum(pageNum + 1);
		}
	}
	/**
	 * Va à la page précédente si on y est pas déjà.
	 */
	public void goToPreviousPage() {
		if (getPageNum() != 0) {
			setPageNum(pageNum - 1);
		}
	}
	
	// OUTILS
	/*
	 * Cette méthode recharge les entités en raison de changements 
	 *   de page ou du nombre d'objets par page.
	 */
	private void refresh() {
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		entities = new ArrayList<>(dao.getAll(getMaxNbItemsByPage(), offset));
		lastPageNb = dao.countLines() / getMaxNbItemsByPage() + 1;
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

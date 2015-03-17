package main.java.com.excilys.computerDatabase.model.pages;

import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.persistence.dao.CRUDDao;

/**
 * Cette classe représente une page contenant des objets pour les 
 *   présenter sous une forme plus lisible.
 * @author excilys
 *
 * @param <T> La classe des objets que doivent contenir les pages.
 */
public class Page<T> {
	private CRUDDao<T> dao;
	private List<T> entities;
	private int maxNbItemsByPage;
	private int pageNum;
	private int lastPageNb;
	
	/**
	 * 
	 * @param dao
	 * @param offset
	 * @param limit
	 */
	public Page(CRUDDao<T> dao, int limit, int offset) {
		this.dao = dao;
		this.maxNbItemsByPage = limit;
		pageNum = offset / getMaxNbItemsByPage() + 1;
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
			++pageNum;
		}
		refresh();
	}
	/**
	 * Va à la page précédente si on y est pas déjà.
	 */
	public void goToPreviousPage() {
		if (getPageNum() != 0) {
			--pageNum;
		}
		refresh();
	}
	
	// OUTILS
	private void refresh() {
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		entities = new ArrayList<>(dao.getAll(getMaxNbItemsByPage(), offset));
		lastPageNb = (dao.countLines() - offset) / getMaxNbItemsByPage() + 1;
		if (entities.size() % getMaxNbItemsByPage() != 0) {
			++lastPageNb;
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

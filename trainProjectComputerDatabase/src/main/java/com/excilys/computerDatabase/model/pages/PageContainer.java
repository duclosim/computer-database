package main.java.com.excilys.computerDatabase.model.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un conteneur de Page, afin d'instancier des 
 *   objets Page selon une liste d'objets génériques.
 * @author excilys
 *
 * @param <T> La classe des objets que doivent contenir les pages.
 */
public class PageContainer<T> {
	public static final int NB_ITEM_BY_PAGE = 10;
	
	private List<Page<T> > pages;
	private int currentPage;
	
	public PageContainer(List<T> entities) {
		pages = new ArrayList<Page<T> >();
		int pageNb = entities.size() / NB_ITEM_BY_PAGE;
		if (entities.size() % NB_ITEM_BY_PAGE != 0) {
			++pageNb;
		}
		for(int k = 0; k < pageNb; k += NB_ITEM_BY_PAGE) {
			int endIndex = Integer.min(k + NB_ITEM_BY_PAGE, entities.size());
			pages.add(new Page<T>(entities.subList(k, endIndex)));
		}
	}
	
	public PageContainer() {
		pages = new ArrayList<Page<T> >();
	}

	/**
	 * Cette méthode retourne la Page courante.
	 * @return La Page courante de ce conteneur.
	 */
	public Page<T> getCurrentPage() {
		return pages.get(currentPage);
	}
	
	/**
	 * Cette méthode retourne l'indice de la Page courante de ce conteneur.
	 * @return Un entier représentant l'indice de la Page courante.
	 */
	public int getCurrentPageNum() {
		return currentPage;
	}
	
	/**
	 * Cette méthode retourne l'indice de la dernière Page de ce conteneur.
	 * @return Un entier représentant l'indice de la dernière Page.
	 */
	public int getMaxPageNum() {
		return pages.size() - 1;
	}

	public void setPages(List<Page<T> > pages) {
		if (pages == null) {
			throw new IllegalArgumentException("pages est à null");
		}
		this.pages = new ArrayList<Page<T> >(pages);
		currentPage = 0;
	}

	/**
	 * Cette méthode change la page courante.
	 * @param pageNum Le numéro de la nouvelle page courante.
	 */
	public void goToPage(int pageNum) {
		if ((0 > pageNum) || (pageNum >= pages.size())) {
			throw new IllegalArgumentException("pageNum est hors-limites");
		}
		this.currentPage = pageNum;
	}

	/**
	 * Cette méthode passe à la page suivante.
	 */
	public void goToNextPage() {
		if (currentPage < (pages.size() - 1)) {
			++currentPage;
		}
	}
	
	/**
	 * Cette méthode passe à la page précédente.
	 */
	public void goToPreviousPage() {
		if (currentPage > 0) {
			--currentPage;
		}
	}
}

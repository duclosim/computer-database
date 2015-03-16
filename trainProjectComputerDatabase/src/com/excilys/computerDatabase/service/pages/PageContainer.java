package com.excilys.computerDatabase.service.pages;

import java.util.ArrayList;
import java.util.List;

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
			pages.add(
					new Page<T>(entities.subList(k, 
							Integer.min(k + NB_ITEM_BY_PAGE, entities.size())))
					);
		}
	}
	
	public PageContainer() {
		pages = new ArrayList<Page<T> >();
	}
		
	public Page<T> getCurrentPage() {
		return pages.get(currentPage);
	}
	
	public int getCurrentPageNum() {
		return currentPage;
	}
	
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
	
	public void goToPage(int pageNum) {
		if ((0 > pageNum) || (pageNum >= pages.size())) {
			throw new IllegalArgumentException("pageNum est hors-limites");
		}
		this.currentPage = pageNum;
	}
	
	public void goToNextPage() {
		if (currentPage < (pages.size() - 1)) {
			++currentPage;
		}
	}
	
	public void goToPreviousPage() {
		if (currentPage > 0) {
			--currentPage;
		}
	}
}

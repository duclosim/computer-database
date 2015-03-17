package main.java.com.excilys.computerDatabase.model.pages;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente une page contenant des objets pour les 
 *   présenter sous une forme plus lisible.
 * @author excilys
 *
 * @param <T> La classe des objets que doivent contenir les pages.
 */
public class Page<T> {
	private List<T> entities;
	
	public Page(List<T> entities) {
		this.entities = new ArrayList<T>(entities);
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

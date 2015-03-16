package com.excilys.computerDatabase.service.pages;

import java.util.ArrayList;
import java.util.List;

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

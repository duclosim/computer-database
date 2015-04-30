package com.excilys.persistence.daos;

/**
 * This class contains the two different way to order by a Computer list.
 * @author excilys
 *
 */
public enum OrderingWay {
	ASC("ASC"), 
	DESC("DESC");
	
	private final String way;
	
	OrderingWay(String way) {
		this.way = way;
	}
	
	public String getWay() {
		return way;
	}
}

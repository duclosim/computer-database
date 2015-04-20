package com.excilys.persistence.daos;

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

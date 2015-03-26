package com.excilys.computerDatabase.persistence.dao;

public enum OrderingWay {
	ASC("ASC"), 
	DESC("DESC");
	
	private String way;
	
	private OrderingWay(String way) {
		this.way = way;
	}
	
	public String getWay() {
		return way;
	}
}

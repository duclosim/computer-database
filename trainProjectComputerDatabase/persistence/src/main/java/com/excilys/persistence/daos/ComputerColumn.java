package com.excilys.persistence.daos;

/**
 * This class contains a list of all the columns of the Computer table.
 * @author excilys
 *
 */
public enum ComputerColumn {
	ID_COLUMN_LABEL("computer.id"),
	NAME_COLUMN_LABEL("computer.name"),
	INTRODUCED_COLUMN_LABEL("computer.introduced"),
	DISCONTINUED_COLUMN_LABEL("computer.discontinued"),
	COMPANY_ID_COLUMN_LABEL("computer.company_id"),
	COMPANY_NAME_COLUMN_LABEL("company.name");

	private final String columnName;
	
	ComputerColumn(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Returns a String containing the column name.
	 * @param columnName A String containig the column name.
	 */
	public String getColumnName() {
		return columnName;
	}
}

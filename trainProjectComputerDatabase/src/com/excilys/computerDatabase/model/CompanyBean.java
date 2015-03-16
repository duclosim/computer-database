package com.excilys.computerDatabase.model;

/**
 * Cette classe contient les informations contenue dans une ligne de 
 *   la table company.
 * @author excilys
 *
 */
public class CompanyBean {
	private Long id;
	private String name;
	
	public CompanyBean() {
		
	}
	
	public CompanyBean(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CompanyBean [id=" + id + ", name=" + name + "]";
	}
}

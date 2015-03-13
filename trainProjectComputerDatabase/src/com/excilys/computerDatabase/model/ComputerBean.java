package com.excilys.computerDatabase.model;

import java.time.LocalDateTime;

public class ComputerBean {
	private Long id;
	private String name;
	private LocalDateTime introduced;
	private LocalDateTime discontinued;
	private Long companyId;
	
	public ComputerBean() {
		
	}
	
	public ComputerBean(Long id, String name, LocalDateTime introduced,
			LocalDateTime discontinued, Long companyId) {
		super();
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
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
	public LocalDateTime getIntroduced() {
		return introduced;
	}
	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}
	public LocalDateTime getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "ComputerBean [id=" + id + ", name=" + name + ", introduced="
				+ introduced + ", discontinued=" + discontinued
				+ ", companyId=" + companyId + "]";
	}
}

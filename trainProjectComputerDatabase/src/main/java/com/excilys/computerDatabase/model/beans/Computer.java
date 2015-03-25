package com.excilys.computerDatabase.model.beans;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cette classe contient les informations contenue dans une ligne de 
 *   la table computer.
 * @author excilys
 *
 */
public class Computer {
	private static final Logger LOG = LoggerFactory.getLogger(Computer.class);
	
	private Long id;
	private String name;
	private LocalDateTime introduced;
	private LocalDateTime discontinued;
	private Company company;

	public Computer(Long id, String name, LocalDateTime introduced,
			LocalDateTime discontinued, Company company) {
		LOG.trace(new StringBuilder("new Computer(")
			.append(id).append(",")
			.append(name).append(",")
			.append(introduced).append(",")
			.append(discontinued).append(",")
			.append(company).append(")")
			.toString());
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	public Computer() {
		LOG.trace("new Computer()");
	}
	
	public Long getId() {
		LOG.trace("getId()");
		return id;
	}
	public void setId(Long id) {
		LOG.trace("setId(" + id + ")");
		this.id = id;
	}
	public String getName() {
		LOG.trace("getName()");
		return name;
	}
	public void setName(String name) {
		LOG.trace("setName(" + name + ")");
		this.name = name;
	}
	public LocalDateTime getIntroduced() {
		LOG.trace("getIntroduced()");
		return introduced;
	}
	public void setIntroduced(LocalDateTime introduced) {
		LOG.trace("setIntroduced(" + introduced + ")");
		this.introduced = introduced;
	}
	public LocalDateTime getDiscontinued() {
		LOG.trace("getDiscontinued()");
		return discontinued;
	}
	public void setDiscontinued(LocalDateTime discontinued) {
		LOG.trace("setDiscontinued(" + discontinued + ")");
		this.discontinued = discontinued;
	}
	public Company getCompany() {
		LOG.trace("getCompany()");
		return company;
	}
	public void setCompany(Company company) {
		LOG.trace("setCompany(" + company + ")");
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((company == null) ? 0 : company.hashCode());
		result = prime * result
				+ ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComputerBean [id=" + id + ", name=" + name + ", introduced="
				+ introduced + ", discontinued=" + discontinued
				+ ", company=" + company + "]";
	}
}

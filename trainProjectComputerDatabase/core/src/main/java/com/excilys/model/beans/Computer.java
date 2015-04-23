package com.excilys.model.beans;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cette classe contient les informations contenue dans une ligne de 
 *   la table computer.
 * @author excilys
 *
 */
@Entity
@Table(name = "computer")
public class Computer implements Serializable {
	private static final long serialVersionUID = -683912164717549922L;
	private static final Logger LOG = LoggerFactory.getLogger(Computer.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@Convert(converter = LocalDateTimeToTimestampConverter.class)
	private LocalDateTime introduced;
	@Convert(converter = LocalDateTimeToTimestampConverter.class)
	private LocalDateTime discontinued;
	@OneToOne
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
		LOG.trace("getIntroducedDate()");
		return introduced;
	}
	public void setIntroduced(LocalDateTime introducedDate) {
		LOG.trace("setIntroducedDate(" + introducedDate + ")");
		this.introduced = introducedDate;
	}
	public LocalDateTime getDiscontinued() {
		LOG.trace("getDiscontinuedDate()");
		return discontinued;
	}
	public void setDiscontinued(LocalDateTime discontinuedDate) {
		LOG.trace("setDiscontinuedDate(" + discontinuedDate + ")");
		this.discontinued = discontinuedDate;
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

package com.excilys.computerDatabase.model.beans;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "company")
public class Computer {
	private static final Logger LOG = LoggerFactory.getLogger(Computer.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@Column(name = "introduced")
	private LocalDateTime introducedDate;
	@Column(name = "discontinued")
	private LocalDateTime discontinuedDate;
	@ManyToOne
	private Company company;

	public Computer(Long id, String name, LocalDateTime introduced,
			LocalDateTime discontinued, Company company) {
		LOG.info(new StringBuilder("new Computer(")
			.append(id).append(",")
			.append(name).append(",")
			.append(introduced).append(",")
			.append(discontinued).append(",")
			.append(company).append(")")
			.toString());
		this.id = id;
		this.name = name;
		this.introducedDate = introduced;
		this.discontinuedDate = discontinued;
		this.company = company;
	}
	
	public Computer() {
		LOG.info("new Computer()");
	}
	
	public Long getId() {
		LOG.info("getId()");
		return id;
	}
	public void setId(Long id) {
		LOG.info("setId(" + id + ")");
		this.id = id;
	}
	public String getName() {
		LOG.info("getName()");
		return name;
	}
	public void setName(String name) {
		LOG.info("setName(" + name + ")");
		this.name = name;
	}
	public LocalDateTime getIntroducedDate() {
		LOG.info("getIntroducedDate()");
		return introducedDate;
	}
	public void setIntroducedDate(LocalDateTime introducedDate) {
		LOG.info("setIntroducedDate(" + introducedDate + ")");
		this.introducedDate = introducedDate;
	}
	public LocalDateTime getDiscontinuedDate() {
		LOG.info("getDiscontinuedDate()");
		return discontinuedDate;
	}
	public void setDiscontinuedDate(LocalDateTime discontinuedDate) {
		LOG.info("setDiscontinuedDate(" + discontinuedDate + ")");
		this.discontinuedDate = discontinuedDate;
	}
	public Company getCompany() {
		LOG.info("getCompany()");
		return company;
	}
	public void setCompany(Company company) {
		LOG.info("setCompany(" + company + ")");
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((company == null) ? 0 : company.hashCode());
		result = prime * result
				+ ((discontinuedDate == null) ? 0 : discontinuedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((introducedDate == null) ? 0 : introducedDate.hashCode());
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
		if (discontinuedDate == null) {
			if (other.discontinuedDate != null)
				return false;
		} else if (!discontinuedDate.equals(other.discontinuedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introducedDate == null) {
			if (other.introducedDate != null)
				return false;
		} else if (!introducedDate.equals(other.introducedDate))
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
				+ introducedDate + ", discontinued=" + discontinuedDate
				+ ", company=" + company + "]";
	}
}

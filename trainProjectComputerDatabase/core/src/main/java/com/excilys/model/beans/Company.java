package com.excilys.model.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author excilys
 * This class represents a Company object, as it is stored in the database.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {
	private static final long serialVersionUID = 4212246981996257432L;
	private static final Logger LOG = LoggerFactory.getLogger(Company.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	
	public Company() {
		LOG.trace("new Company()");
	}
	
	public Company(Long id, String name) {
		LOG.trace(new StringBuilder("new Company(")
		.append(id).append(",")
		.append(name).append(")")
		.toString());
		this.id = id;
		this.name = name;
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
		LOG.trace("setName(" + id + ")");
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "CompanyBean [id=" + id + ", name=" + name + "]";
	}
}

package com.excilys.computerDatabase.service.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ComputerDTO {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTO.class);
	
	private String id;
	private String name;
	private String introducedDate;
	private String discontinuedDate;
	private String companyId;
	private String companyName;
	
	public ComputerDTO() {
		LOG.trace("new ComputerDTO()");
	}
	
	public String getId() {
		LOG.trace("getId()");
		return id;
	}
	public void setId(String id) {
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
	public String getIntroducedDate() {
		LOG.trace("getIntroducedDate()");
		return introducedDate;
	}
	public void setIntroducedDate(String introducedDate) {
		LOG.trace("setIntroducedDate(" + introducedDate + ")");
		this.introducedDate = introducedDate;
	}
	public String getDiscontinuedDate() {
		LOG.trace("getDiscontinuedDate()");
		return discontinuedDate;
	}
	public void setDiscontinuedDate(String discontinuedDate) {
		LOG.trace("setDiscontinuedDate(" + discontinuedDate + ")");
		this.discontinuedDate = discontinuedDate;
	}
	public String getCompanyId() {
		LOG.trace("getCompanyId()");
		return companyId;
	}
	public void setCompanyId(String companyId) {
		LOG.trace("setCompanyId(" + companyId + ")");
		this.companyId = companyId;
	}
	public String getCompanyName() {
		LOG.trace("getCompanyName()");
		return companyName;
	}
	public void setCompanyName(String companyName) {
		LOG.trace("setCompanyName(" + companyName + ")");
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "ComputerDTO [id=" + id + ", name=" + name + ", introducedDate="
				+ introducedDate + ", discontinuedDate=" + discontinuedDate
				+ ", companyId=" + companyId + ", companyName=" + companyName
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime
				* result
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
		ComputerDTO other = (ComputerDTO) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
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
}

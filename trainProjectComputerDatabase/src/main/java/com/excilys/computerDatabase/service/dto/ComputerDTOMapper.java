package com.excilys.computerDatabase.service.dto;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;

/**
 * 
 * @author excilys
 *
 */
public enum ComputerDTOMapper {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTOMapper.class);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public Computer DTOToBean(ComputerDTO dto) {
		LOG.trace("DTOToBean(" + dto + ")");
		Computer bean = new Computer();
		bean.setId(Long.parseLong(dto.getId()));
		bean.setName(dto.getName());
		bean.setIntroducedDate(null);
		bean.setDiscontinuedDate(null);
		bean.setCompany(null);
		if (dto.getIntroducedDate() != null) {
			bean.setIntroducedDate(LocalDateTime.parse(dto.getIntroducedDate()));
		}
		if (dto.getDiscontinuedDate() != null) {
			bean.setDiscontinuedDate(LocalDateTime.parse(dto.getDiscontinuedDate()));
		}
		if ((dto.getCompanyId() != null) && (dto.getCompanyName() != null)) {
			bean.setCompany(new Company(Long.parseLong(dto.getCompanyId()), dto.getCompanyName()));
		}
		return bean;
	}
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public ComputerDTO BeanToDTO(Computer bean) {
		LOG.trace("BeanToDTO(" + bean + ")");
		ComputerDTO dto = new ComputerDTO();
		dto.setId(bean.getId().toString());
		dto.setName(bean.getName());
		dto.setIntroducedDate(null);
		dto.setDiscontinuedDate(null);
		dto.setCompanyId(null);
		dto.setCompanyName(null);
		if (bean.getIntroducedDate() != null) {
			dto.setIntroducedDate(bean.getIntroducedDate().toLocalDate().toString());
		}
		if (bean.getDiscontinuedDate() != null) {
			dto.setDiscontinuedDate(bean.getDiscontinuedDate().toLocalDate().toString());
		}
		if (bean.getCompany() != null) {
			dto.setCompanyId(bean.getCompany().getId().toString());
			dto.setCompanyName(bean.getCompany().getName());
		}
		return dto;
	}
}

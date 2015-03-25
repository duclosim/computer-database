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
		bean.setIntroduced(LocalDateTime.parse(dto.getIntroducedDate()));
		bean.setDiscontinued(LocalDateTime.parse(dto.getDiscontinuedDate()));
		bean.setCompany(new Company(Long.parseLong(dto.getCompanyId()), dto.getCompanyName()));
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
		dto.setIntroducedDate(bean.getIntroduced().toLocalDate().toString());
		dto.setDiscontinuedDate(bean.getDiscontinued().toLocalDate().toString());
		dto.setCompanyId(bean.getCompany().getId().toString());
		dto.setCompanyName(bean.getCompany().getName());
		return dto;
	}
}

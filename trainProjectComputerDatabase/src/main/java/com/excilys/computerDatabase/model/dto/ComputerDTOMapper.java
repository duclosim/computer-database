package com.excilys.computerDatabase.model.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;

/**
 * 
 * @author excilys
 *
 */
@Component
public class ComputerDTOMapper {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTOMapper.class);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public Computer DTOToBean(ComputerDTO dto) {
		LOG.info("DTOToBean(" + dto + ")");
		Computer bean = new Computer();
		bean.setId(null);
		bean.setName(dto.getName());
		bean.setIntroducedDate(null);
		bean.setDiscontinuedDate(null);
		bean.setCompany(null);
		if (dto.getId() != null) {
			bean.setId(Long.parseLong(dto.getId()));
		}
		LocalDate lDate;
		if (dto.getIntroducedDate() != null) {
			lDate = LocalDate.parse(dto.getIntroducedDate());
			bean.setIntroducedDate(lDate.atStartOfDay());
		}
		if (dto.getDiscontinuedDate() != null) {
			lDate = LocalDate.parse(dto.getDiscontinuedDate());
			bean.setDiscontinuedDate(lDate.atStartOfDay());
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
		LOG.info("BeanToDTO(" + bean + ")");
		ComputerDTO dto = new ComputerDTO();
		dto.setId(bean.getId().toString());
		dto.setName(bean.getName());
		dto.setIntroducedDate(null);
		dto.setDiscontinuedDate(null);
		dto.setCompanyId(null);
		dto.setCompanyName(null);
		if (bean.getIntroducedDate() != null) {
			dto.setIntroducedDate(bean.getIntroducedDate().toString());
		}
		if (bean.getDiscontinuedDate() != null) {
			dto.setDiscontinuedDate(bean.getDiscontinuedDate().toString());
		}
		if (bean.getCompany() != null) {
			dto.setCompanyId(bean.getCompany().getId().toString());
			dto.setCompanyName(bean.getCompany().getName());
		}
		return dto;
	}
	
	/**
	 * 
	 * @param beans
	 * @return
	 */
	public List<ComputerDTO> BeansToDTOs(List<Computer> beans) {
        return beans.stream().map(this::BeanToDTO).collect(Collectors.toList());
	}
}

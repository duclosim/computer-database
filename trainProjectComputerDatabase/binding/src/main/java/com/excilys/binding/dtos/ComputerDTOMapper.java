package com.excilys.binding.dtos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.excilys.model.beans.Company;
import com.excilys.model.beans.Computer;

/**
 * 
 * @author excilys
 *
 */
@Component
public class ComputerDTOMapper {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDTOMapper.class);
	private static final String DATE_FORMAT_MESSAGE_CODE = "date.format";
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Transforme un DTO en bean.
	 * @param dto Le dto à transformer.
	 * @return Le bean tel qu'utilisable par la couche dao.
	 */
	public Computer DTOToBean(ComputerDTO dto) {
		LOG.info("DTOToBean(" + dto + ")");
		Computer bean = new Computer();
		bean.setId(null);
		bean.setName(dto.getName());
		bean.setIntroduced(null);
		bean.setDiscontinued(null);
		bean.setCompany(null);
		if (dto.getId() != null) {
			bean.setId(Long.parseLong(dto.getId()));
		}
		LocalDate lDate;
		if (dto.getIntroducedDate() != null) {
			lDate = LocalDate.parse(dto.getIntroducedDate(), getFormatter());
			bean.setIntroduced(lDate.atStartOfDay());
		}
		if (dto.getDiscontinuedDate() != null) {
			lDate = LocalDate.parse(dto.getDiscontinuedDate(), getFormatter());
			bean.setDiscontinued(lDate.atStartOfDay());
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
		if (bean == null) {
			return null;
		}
		ComputerDTO dto = new ComputerDTO();
		dto.setId(bean.getId().toString());
		dto.setName(bean.getName());
		dto.setIntroducedDate(null);
		dto.setDiscontinuedDate(null);
		dto.setCompanyId(null);
		dto.setCompanyName(null);
		if (bean.getIntroduced() != null) {
			dto.setIntroducedDate(bean.getIntroduced().format(getFormatter()));
		}
		if (bean.getDiscontinued() != null) {
			dto.setDiscontinuedDate(bean.getDiscontinued().format(getFormatter()));
		}
		if (bean.getCompany() != null) {
			dto.setCompanyId(bean.getCompany().getId().toString());
			dto.setCompanyName(bean.getCompany().getName());
		}
		return dto;
	}
	
	public List<ComputerDTO> BeansToDTOs(List<Computer> beans) {
        return beans.stream().map(this::BeanToDTO).collect(Collectors.toList());
	}
	
	private DateTimeFormatter getFormatter() {
		Locale locale = LocaleContextHolder.getLocale();
		String dateFormat = messageSource.getMessage(DATE_FORMAT_MESSAGE_CODE, null, locale);
		return DateTimeFormatter.ofPattern(dateFormat);
	}
}

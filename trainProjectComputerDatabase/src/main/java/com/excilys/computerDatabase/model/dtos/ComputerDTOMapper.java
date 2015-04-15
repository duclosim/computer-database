package com.excilys.computerDatabase.model.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private static final String DATE_FORMAT_MESSAGE_CODE = "date.format";
	
	@Autowired
	private MessageSource messageSource;
	
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
		// TODO assigner le format à partir du fichier de properties
		String dateFormat = null;
		Locale locale = LocaleContextHolder.getLocale();
		if (messageSource == null) {
			switch(locale.getLanguage()) {
			case "fr" :
				dateFormat = "dd/MM/uuuu";
				break;
			case "en" :
				dateFormat = "uuuu-MM-dd";
				break;
			}
		} else {
			dateFormat = messageSource.getMessage(DATE_FORMAT_MESSAGE_CODE, null, locale);
		}
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
		if (dto.getIntroducedDate() != null) {
			lDate = LocalDate.parse(dto.getIntroducedDate(), dateFormatter);
			bean.setIntroducedDate(lDate.atStartOfDay());
		}
		if (dto.getDiscontinuedDate() != null) {
			lDate = LocalDate.parse(dto.getDiscontinuedDate(), dateFormatter);
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
		Locale locale = LocaleContextHolder.getLocale();
		if (bean.getIntroducedDate() != null) {
			dto.setIntroducedDate(localDateToLocalizedString(bean.getIntroducedDate(), locale));
		}
		if (bean.getDiscontinuedDate() != null) {
			dto.setDiscontinuedDate(localDateToLocalizedString(bean.getDiscontinuedDate(), locale));
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
	
	private String localDateToLocalizedString(LocalDateTime date, Locale locale) {
		String strDate = null;
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();
		int year = date.getYear();
		switch (locale.getLanguage()) {
		case "fr" :
			strDate = new StringBuilder(day).append("/")
				.append(month).append("/")
				.append(year).toString();
			break;
		case "en" :
			strDate = new StringBuilder(year).append("-")
			.append(month).append("-")
			.append(day).toString();
			break;
		}
		return strDate;
	}
}

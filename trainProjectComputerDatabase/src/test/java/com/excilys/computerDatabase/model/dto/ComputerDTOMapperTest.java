package com.excilys.computerDatabase.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Assert;

import org.junit.Test;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;

// TODO
public class ComputerDTOMapperTest {

	private static final String STR_ID = "16";
	private static final String NAME = "Apple II";
	private static final String STR_INTRO_DATE = "1977-04-01";
	private static final String STR_DIS_DATE = "1993-10-01";
	private static final String STR_COMPANY_ID = "1";
	private static final String COMPANY_NAME = "Apple Inc.";
	
	private static final Long ID = Long.parseLong(STR_ID);
	private static final LocalDateTime INTRO_DATE = LocalDate.parse(STR_INTRO_DATE).atStartOfDay();
	private static final LocalDateTime DIS_DATE = LocalDate.parse(STR_DIS_DATE).atStartOfDay();
	private static final Long COMPANY_ID = Long.parseLong(STR_COMPANY_ID);
	
	@Test
	public void beanToDTOShouldMapWithNoNullAttributes() {
		// Given
		Computer bean = new Computer();
		bean.setId(ID);
		bean.setName(NAME);
		bean.setIntroducedDate(INTRO_DATE);
		bean.setDiscontinuedDate(DIS_DATE);
		bean.setCompany(new Company(COMPANY_ID, COMPANY_NAME));
		ComputerDTO expectedDTO = new ComputerDTO();
		expectedDTO.setId(STR_ID);
		expectedDTO.setName(NAME);
		expectedDTO.setIntroducedDate(STR_INTRO_DATE);
		expectedDTO.setDiscontinuedDate(STR_DIS_DATE);
		expectedDTO.setCompanyId(STR_COMPANY_ID);
		expectedDTO.setCompanyName(COMPANY_NAME);
		ComputerDTO dto = null;
		// When
		dto = ComputerDTOMapper.BeanToDTO(bean);
		// Then
		Assert.assertEquals("Erreur de mapping de bean.", expectedDTO, dto);
	}
	
	@Test
	public void dtoToBeanShouldMapWithNoNullAttributes() {
		// Given
		Computer expectedBean = new Computer();
		expectedBean.setId(ID);
		expectedBean.setName(NAME);
		expectedBean.setIntroducedDate(INTRO_DATE);
		expectedBean.setDiscontinuedDate(DIS_DATE);
		expectedBean.setCompany(new Company(COMPANY_ID, COMPANY_NAME));
		ComputerDTO dto = new ComputerDTO();
		dto.setId(STR_ID);
		dto.setName(NAME);
		dto.setIntroducedDate(STR_INTRO_DATE);
		dto.setDiscontinuedDate(STR_DIS_DATE);
		dto.setCompanyId(STR_COMPANY_ID);
		dto.setCompanyName(COMPANY_NAME);
		Computer bean = null;
		// When
		bean = ComputerDTOMapper.DTOToBean(dto);
		// Then
		Assert.assertEquals("Erreur de mapping de dto.", expectedBean, bean);
	}
}

package com.excilys.binding.dtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.page.Page;

/**
 * This page extends the Page class but which transforms the entities 
 *   into Dtos before returning them.
 * @author excilys
 *
 */
@Component
public class PageDTO extends Page {
	@Autowired
	private ComputerDTOMapper mapper;

	/**
	 * This methods returns the Computer wrapped in this object 
	 *   but into ComputerDTO forms.
	 * @return A list of ComputerDTO.
	 */
	public List<ComputerDTO> getDtos() {
		return mapper.BeansToDTOs(super.getEntities());
	}
}

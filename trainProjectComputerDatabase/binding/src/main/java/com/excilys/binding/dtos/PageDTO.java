package com.excilys.binding.dtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.page.Page;

@Component
public class PageDTO extends Page {
	@Autowired
	private ComputerDTOMapper mapper;

	public List<ComputerDTO> getDtos() {
		return mapper.BeansToDTOs(super.getEntities());
	}
}

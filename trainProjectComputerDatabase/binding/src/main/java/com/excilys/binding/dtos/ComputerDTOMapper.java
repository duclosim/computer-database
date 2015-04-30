package com.excilys.binding.dtos;

import java.util.List;

import com.excilys.model.beans.Computer;

/**
 * This class is a way of presenting the data contained in a Computer object
 *   in order to make it easy to read from a web application.
 * @author excilys
 *
 */
public interface ComputerDTOMapper {

	/**
	 * Takes a ComputerDTO object and make a Computer object.
	 * @param dto The ComputerDTO object to base upon to make a Computer object.
	 * @return A Computer containing the data of dto.
	 */
	Computer DTOToBean(ComputerDTO dto);

    /**
	 * Takes a Computer object and make a ComputerDTO object.
	 * @param bean The Computer object to base upon to make a ComputerDTO object.
	 * @return A ComputerDTO containing the data of bean.
	 */
	ComputerDTO BeanToDTO(Computer bean);
	
	/**
	 * This methods applies the BeanToDto method on a List of Computer objects.
	 * @param beans A List of Computer.
	 * @return A List of ComputerDTO containing the data of beans.
	 */
	List<ComputerDTO> BeansToDTOs(List<Computer> beans);
}

package com.excilys.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.binding.dtos.ComputerDTO;
import com.excilys.binding.dtos.ComputerDTOMapper;
import com.excilys.model.beans.Computer;
import com.excilys.persistence.daos.ComputerColumn;
import com.excilys.persistence.daos.ComputerDAO;
import com.excilys.persistence.daos.OrderingWay;
import com.excilys.validators.DateValidator;
import com.excilys.validators.UserInputsValidator;

/**
 * 
 * @author excilys
 *
 */
@Service
public class ComputerServiceImpl implements ComputerService {
	private static final Logger LOG = LoggerFactory.getLogger(ComputerServiceImpl.class);
	
	@Autowired
	private ComputerDAO computerDao;
	@Autowired
	private ComputerDTOMapper computerDTOMapper;
	@Autowired
	private DateValidator dateValidator;
	
	@Override
	public ComputerDTO getById(Long id) {
		LOG.trace("getById(" + id + ")");
		ComputerDTO result;
		result = computerDTOMapper.BeanToDTO(computerDao.getById(id));
		return result;
	}

	@Override
	public List<ComputerDTO> getAll(int limit, int offset) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs((computerDao.getAll(limit, offset)));
		return result;
	}

	@Override
	public List<ComputerDTO> getFiltered(String name, int limit, int offset) {
		LOG.trace("getByNameOrCompanyName(" + name + ")");
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs(computerDao.getFiltered(limit, offset, name));
		return result;
	}
	
	@Override
	public List<ComputerDTO> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs((computerDao.getOrdered(limit, offset,
				column, way)));
		return result;
	}

	@Override
	public List<ComputerDTO> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(", ")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs((computerDao.getFilteredAndOrdered(
				limit, offset, name, column, way)));
		return result;
	}
	
	@Override
	public int countAllLines() {
		LOG.trace("countLines()");
		int result;
		result = computerDao.countLines();
		return result;
	}

	@Override
	public int countFilteredLines(String name) {
		LOG.trace("countFilteredLines(" + name + ")");
		int result;
		result = computerDao.countFilteredLines(name);
		return result;
	}

	@Override
	public void create(ComputerDTO computer) {
		LOG.trace("create(" + computer + ")");
		checkComputerDTO(computer);
		Computer cmptrBean = computerDTOMapper.DTOToBean(computer);
		computerDao.create(cmptrBean);
		computer.setId(cmptrBean.getId().toString());
	}

	@Override
	public void update(ComputerDTO computer) {
		LOG.trace("update(" + computer + ")");
		checkComputerDTO(computer);
		Computer cmptrBean = computerDTOMapper.DTOToBean(computer);
		computerDao.update(cmptrBean);
	}

	@Override
	public void delete(Long id) {
		LOG.trace("delete(" + id + ")");
		if (id != null) {
			computerDao.delete(id);
		}
	}
	
	private void checkComputerDTO(ComputerDTO computer) {
		LOG.trace("checkComputerDTO(" + computer + ")");
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		String name = computer.getName();
		String introDate = computer.getIntroducedDate();
		String disDate = computer.getDiscontinuedDate();
		String companyIdStr = computer.getCompanyId();
		// Test de l'objet.
		if ((name == null) || (!UserInputsValidator.isValidString(name))) {
			LOG.error("Nom non valide.\n");
			throw new IllegalArgumentException("Nom non valide.\n");
		}
		if (!dateValidator.isValidDate(introDate)) {
			LOG.error("Date d'introduction non valide.\n");
			throw new IllegalArgumentException("Date d'introduction non valide.\n");
		}
		if (!dateValidator.isValidDate(disDate)) {
			LOG.error("Date de sortie non valide.\n");
			throw new IllegalArgumentException("Date de sortie non valide.\n");
		}
		if ((companyIdStr != null) 
				&& (!UserInputsValidator.isValidNumber(companyIdStr))) {
			LOG.error("Numéro de companie non valide.\n");
			throw new IllegalArgumentException("Numéro de companie non valide.\n");
		}
	}
}

package com.excilys.computerDatabase.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.model.dtos.ComputerDTO;
import com.excilys.computerDatabase.model.dtos.ComputerDTOMapper;
import com.excilys.computerDatabase.persistence.daos.ComputerColumn;
import com.excilys.computerDatabase.persistence.daos.ComputerDAO;
import com.excilys.computerDatabase.persistence.daos.OrderingWay;
import com.excilys.computerDatabase.validators.DateValidator;
import com.excilys.computerDatabase.validators.UserInputsValidator;

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
		LOG.info("getById(" + id + ")");
		ComputerDTO result;
		result = computerDTOMapper.BeanToDTO(computerDao.getById(id));
		return result;
	}

	@Override
	public List<ComputerDTO> getAll(int limit, int offset) {
		LOG.info(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs((computerDao.getAll(limit, offset)));
		return result;
	}

	@Override
	public List<ComputerDTO> getFiltered(String name, int limit, int offset) {
		LOG.info("getByNameOrCompanyName(" + name + ")");
		List<ComputerDTO> result;
		result = computerDTOMapper.BeansToDTOs(computerDao.getFiltered(limit, offset, name));
		return result;
	}
	
	@Override
	public List<ComputerDTO> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) {
		LOG.info(new StringBuilder("getAll(")
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
		LOG.info(new StringBuilder("getAll(")
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
		LOG.info("countLines()");
		int result;
		result = computerDao.countLines();
		return result;
	}

	@Override
	public int countFilteredLines(String name) {
		LOG.info("countFilteredLines(" + name + ")");
		int result;
		result = computerDao.countFilteredLines(name);
		return result;
	}

	@Override
	public void create(ComputerDTO computer) {
		LOG.info("create(" + computer + ")");
		checkComputerDTO(computer);
		Computer cmptrBean = computerDTOMapper.DTOToBean(computer);
		computerDao.create(cmptrBean);
		computer.setId(cmptrBean.getId().toString());
	}

	@Override
	public void update(ComputerDTO computer) {
		LOG.info("update(" + computer + ")");
		checkComputerDTO(computer);
		Computer cmptrBean = computerDTOMapper.DTOToBean(computer);
		computerDao.update(cmptrBean);
	}

	@Override
	public void delete(ComputerDTO computer) {
		LOG.info("delete(" + computer + ")");
		if (computer != null) {
			computerDao.delete(computerDTOMapper.DTOToBean(computer));
		}
	}
	
	private void checkComputerDTO(ComputerDTO computer) {
		LOG.info("checkComputerDTO(" + computer + ")");
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

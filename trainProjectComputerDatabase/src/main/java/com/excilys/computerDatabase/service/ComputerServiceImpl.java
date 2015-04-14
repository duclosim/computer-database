package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.model.dto.ComputerDTO;
import com.excilys.computerDatabase.model.dto.ComputerDTOMapper;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.utils.UserInputsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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
	
	@Override
	public ComputerDTO getById(Long id) {
		LOG.info("getById(" + id + ")");
		ComputerDTO result;
		result = ComputerDTOMapper.BeanToDTO(computerDao.getById(id));
		return result;
	}

	@Override
	public List<ComputerDTO> getAll(int limit, int offset) {
		LOG.info(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<ComputerDTO> result;
		result = ComputerDTOMapper.BeansToDTOs((computerDao.getAll(limit, offset)));
		return result;
	}

	@Override
	public List<ComputerDTO> getFiltered(String name, int limit, int offset) {
		LOG.info("getByNameOrCompanyName(" + name + ")");
		List<ComputerDTO> result;
		result = ComputerDTOMapper.BeansToDTOs(computerDao.getFiltered(limit, offset, name));
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
		result = ComputerDTOMapper.BeansToDTOs((computerDao.getOrdered(limit, offset,
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
		result = ComputerDTOMapper.BeansToDTOs((computerDao.getFilteredAndOrdered(
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
		Computer cmptrBean = ComputerDTOMapper.DTOToBean(computer);
		computerDao.create(cmptrBean);
		computer.setId(cmptrBean.getId().toString());
	}

	@Override
	public void update(ComputerDTO computer) {
		LOG.info("update(" + computer + ")");
		checkComputerDTO(computer);
		Computer cmptrBean = ComputerDTOMapper.DTOToBean(computer);
		computerDao.update(cmptrBean);
	}

	@Override
	public void delete(ComputerDTO computer) {
		LOG.info("delete(" + computer + ")");
		if (computer != null) {
			computerDao.delete(ComputerDTOMapper.DTOToBean(computer));
		}
	}
	
	private void checkComputerDTO(ComputerDTO computer) {
		LOG.info("checkComputerDTO(" + computer + ")");
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		String name = computer.getName();
		String introducedDate = computer.getIntroducedDate();
		String discontinuedDate = computer.getDiscontinuedDate();
		String companyIdStr = computer.getCompanyId();
		// Test de l'objet.
		if ((name == null) || (!UserInputsValidator.isValidString(name))) {
			LOG.error("Nom non valide.\n");
			throw new IllegalArgumentException("Nom non valide.\n");
		}
		if (!UserInputsValidator.isValidOrNullDate(introducedDate)) {
			LOG.error("Date d'introduction non valide.\n");
			throw new IllegalArgumentException("Date d'introduction non valide.\n");
		}
		if (!UserInputsValidator.isValidOrNullDate(discontinuedDate)) {
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

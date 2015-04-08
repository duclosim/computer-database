package com.excilys.computerDatabase.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.dto.ComputerDTO;
import com.excilys.computerDatabase.service.dto.ComputerDTOMapper;

/**
 * 
 * @author excilys
 *
 */
@Service
public class ComputerServiceImpl implements ComputerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerServiceImpl.class);
	
	@Autowired
	private ComputerDAO dao;
	@Autowired
	private ComputerDTOMapper dtoMapper;
	
	@Override
	public ComputerDTO getById(Long id) {
		LOG.info("getById(" + id + ")");
		ComputerDTO result = null;
		try {
			result = dtoMapper.BeanToDTO(dao.getById(id));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public List<ComputerDTO> getFiltered(String name, int limit, int offset) {
		LOG.info("getByNameOrCompanyName(" + name + ")");
		List<ComputerDTO> result = null;
		try {
			result = dtoMapper.BeansToDTOs(dao.getFiltered(limit, offset, name));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public List<ComputerDTO> getAll(int limit, int offset) {
		LOG.info(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<ComputerDTO> result = new ArrayList<>();
		try {
			result = dtoMapper.BeansToDTOs((dao.getAll(limit, offset)));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
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
		List<ComputerDTO> result = new ArrayList<>();
		try {
			result = dtoMapper.BeansToDTOs((dao.getOrdered(limit, offset,
					column, way)));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
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
		List<ComputerDTO> result = new ArrayList<>();
		try {
			result = dtoMapper.BeansToDTOs((dao.getFilteredAndOrdered(
					limit, offset, name, column, way)));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}
	
	@Override
	public int countAllLines() {
		LOG.info("countLines()");
		int result = 0;
		try {
			result = dao.countLines();
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public int countFilteredLines(String name) {
		LOG.info("countFilteredLines(" + name + ")");
		int result = 0;
		try {
			result = dao.countFilteredLines(name);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		}
		return result;
	}

	@Override
	public void create(ComputerDTO computer) {
		LOG.info("create(" + computer + ")");
		checkComputerDTO(computer);
		try {
			dao.create(dtoMapper.DTOToBean(computer));
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		}

	}

	@Override
	public void update(ComputerDTO computer) {
		LOG.info("update(" + computer + ")");
		checkComputerDTO(computer);
		try {
			dao.update(dtoMapper.DTOToBean(computer));
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		}
	}

	@Override
	public void delete(ComputerDTO computer) {
		LOG.info("delete(" + computer + ")");
		checkComputerDTO(computer);
		try {
			dao.delete(dtoMapper.DTOToBean(computer));
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
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
		if ((name != null) && (!UserInputsValidator.isValidString(name))) {
			LOG.error("Nom non valide.\n");
			throw new IllegalArgumentException("Nom non valide.\n");
		}
		if ((introducedDate != null) 
				&& (!UserInputsValidator.isValidDate(introducedDate))) {
			LOG.error("Date d'introduction non valide.\n");
			throw new IllegalArgumentException("Date d'introduction non valide.\n");
		}
		if ((discontinuedDate != null) 
				&& (!UserInputsValidator.isValidDate(discontinuedDate))) {
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

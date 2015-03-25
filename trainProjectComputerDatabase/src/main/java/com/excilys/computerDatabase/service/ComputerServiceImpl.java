package com.excilys.computerDatabase.service;

import com.excilys.computerDatabase.model.UserInputsValidator;
import com.excilys.computerDatabase.persistence.ConnectionFactory;
import com.excilys.computerDatabase.persistence.PersistenceException;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;
import com.excilys.computerDatabase.service.dto.ComputerDTO;
import com.excilys.computerDatabase.service.dto.ComputerDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author excilys
 *
 */
public enum ComputerServiceImpl implements ComputerService {
	INSTANCE;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComputerServiceImpl.class);
	
	private final ComputerDAO dao;
	private final ComputerDTOMapper dtoMapper;
	
	private ComputerServiceImpl() {
		dao = ComputerDAOImpl.INSTANCE;
		dtoMapper = ComputerDTOMapper.INSTANCE;
	}
	
	@Override
	public ComputerDTO getById(Long id) {
		LOG.trace("getById(" + id + ")");
		Connection connection = null;
		ComputerDTO result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dtoMapper.BeanToDTO(dao.getById(id, connection));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<ComputerDTO> getByNameOrCompanyName(String name) {
		LOG.trace("getByName(" + name + ")");
		Connection connection = null;
		List<ComputerDTO> result = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dtoMapper.BeansToDTOs(dao.getByNameOrCompanyName(name, connection));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<ComputerDTO> getAll(int limit, int offset) {
		Connection connection = null;
		List<ComputerDTO> result = new ArrayList<>();
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dtoMapper.BeansToDTOs((dao.getAll(limit, offset, connection)));
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public int countLines() {
		Connection connection = null;
		int result = 0;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			result = dao.countLines(connection);
		} catch (SQLException e) {
			LOG.error("Lecture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Lecture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
		return result;
	}

	@Override
	public void create(ComputerDTO computer) {
		checkComputerDTO(computer);
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.create(dtoMapper.DTOToBean(computer), connection);
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}

	}

	@Override
	public void update(ComputerDTO computer) {
		checkComputerDTO(computer);
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.update(dtoMapper.DTOToBean(computer), connection);
		} catch (SQLException e) {
			LOG.error("Ecriture impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Ecriture impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
	}

	@Override
	public void delete(ComputerDTO computer) {
		checkComputerDTO(computer);
		Connection connection = null;
		try {
			connection = ConnectionFactory.INSTANCE.getConnection();
			dao.delete(dtoMapper.DTOToBean(computer), connection);
		} catch (SQLException e) {
			LOG.error("Suppression impossible dans la bdd.");
			e.printStackTrace();
			throw new PersistenceException("Suppression impossible dans la bdd.");
		} finally {
			ConnectionFactory.INSTANCE.closeConnection(connection);
		}
	}
	
	private void checkComputerDTO(ComputerDTO computer) {
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

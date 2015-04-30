package com.excilys.services;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

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
	// The local DateTimeFormatter pattern.
	private static final String DATE_FORMAT_MESSAGE_CODE = "date.format";
	
	@Autowired
	private ComputerDAO computerDao;
	@Autowired
	private DateValidator dateValidator;
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public Computer getById(Long id) {
		LOG.trace("getById(" + id + ")");
		Computer result;
		result = computerDao.getById(id);
		return result;
	}

	@Override
	public List<Computer> getAll(int limit, int offset) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(")")
			.toString());
		List<Computer> result;
		result = computerDao.getAll(limit, offset);
		return result;
	}

	@Override
	public List<Computer> getFiltered(String name, int limit, int offset) {
		LOG.trace("getByNameOrCompanyName(" + name + ")");
		List<Computer> result;
		result = computerDao.getFiltered(limit, offset, name);
		return result;
	}
	
	@Override
	public List<Computer> getOrdered(int limit, int offset, 
			ComputerColumn column, OrderingWay way) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		List<Computer> result;
		result = computerDao.getOrdered(limit, offset, column, way);
		return result;
	}

	@Override
	public List<Computer> getFilteredAndOrdered(int limit, int offset,
			String name, ComputerColumn column, OrderingWay way) {
		LOG.trace(new StringBuilder("getAll(")
			.append(limit).append(", ")
			.append(offset).append(",")
			.append(name).append(", ")
			.append(column).append(",")
			.append(way).append(")")
			.toString());
		List<Computer> result;
		result = computerDao.getFilteredAndOrdered(limit, offset, name,
				column, way);
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
	public void create(Computer computer) {
		LOG.trace("create(" + computer + ")");
		checkComputer(computer);
		computerDao.create(computer);
	}

	@Override
	public void update(Computer computer) {
		LOG.trace("update(" + computer + ")");
		checkComputer(computer);
		computerDao.update(computer);
	}

	@Override
	public void delete(Long id) {
		LOG.trace("delete(" + id + ")");
		if (id != null) {
			computerDao.delete(id);
		}
	}
	
	/*
	 * Checks if this is  a well formed Computer object.
	 */
	private void checkComputer(Computer computer) {
		LOG.trace("checkComputerDTO(" + computer + ")");
		if (computer == null) {
			throw new IllegalArgumentException("computer est à null.");
		}
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(messageSource
						.getMessage(DATE_FORMAT_MESSAGE_CODE, null, LocaleContextHolder.getLocale()));
		String name = computer.getName();
		// Get the locals values of the dates, null if they are not set.
		String introDate = computer.getIntroduced() == null ? null : computer.getIntroduced().toLocalDate().format(dateFormatter);
		String disDate = computer.getDiscontinued() == null ? null : computer.getDiscontinued().toLocalDate().format(dateFormatter);
		String companyIdStr = computer.getCompany() == null ? null : computer.getCompany().getId().toString();
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

	@Override
	public void setComputerDao(ComputerDAO dao) {
		this.computerDao = dao;
	}
}

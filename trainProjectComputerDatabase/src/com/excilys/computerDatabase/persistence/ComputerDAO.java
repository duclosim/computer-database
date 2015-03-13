package com.excilys.computerDatabase.persistence;

import java.util.List;

import com.excilys.computerDatabase.model.ComputerBean;

public interface ComputerDAO {
	public ComputerBean getById(Long id);
	public List<ComputerBean> getAll();
	public void createComputer(ComputerBean computerBean);
	public void updateComputer(ComputerBean computerBean);
	public void deleteComputer(ComputerBean computerBean);
}

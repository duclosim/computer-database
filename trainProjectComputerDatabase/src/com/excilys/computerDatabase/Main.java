package com.excilys.computerDatabase;

import java.time.LocalDateTime;

import com.excilys.computerDatabase.model.CompanyBean;
import com.excilys.computerDatabase.model.ComputerBean;
import com.excilys.computerDatabase.persistence.CompanyDAO;
import com.excilys.computerDatabase.persistence.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.ComputerDAO;
import com.excilys.computerDatabase.persistence.ComputerDAOImpl;

public class Main {
	public static void main(String args[]) {
		CompanyDAO companyDAO = CompanyDAOImpl.getInstance();
		ComputerDAO computerDAO = ComputerDAOImpl.getInstance();
		// Company tests
		CompanyBean companyBean = companyDAO.getById(new Long(6));
		System.out.println(companyBean);
		for (CompanyBean bean : companyDAO.getAll()) {
			System.out.println(bean);
		}
		// Computer tests
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
		// update
		ComputerBean updatedBean = computerDAO.getById(new Long (574));
		updatedBean.setDiscontinued(LocalDateTime.now());
		computerDAO.updateComputer(updatedBean);
		updatedBean = computerDAO.getById(new Long (574));
		System.out.println(updatedBean);
		updatedBean.setDiscontinued(null);
		computerDAO.updateComputer(updatedBean);
		updatedBean = computerDAO.getById(new Long (574));
		System.out.println(updatedBean);
		// insert
		ComputerBean insertedBean = new ComputerBean();
		insertedBean.setName("HAL9000");
		insertedBean.setIntroduced(LocalDateTime.now());
		insertedBean.setDiscontinued(null);
		insertedBean.setCompanyId(new Long(36));
		computerDAO.createComputer(insertedBean);
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
		// delete
		insertedBean = computerDAO.getById(new Long(575));
		computerDAO.deleteComputer(insertedBean);
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
	}
}

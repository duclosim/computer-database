package com.excilys.computerDatabase;

import java.time.LocalDateTime;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.CompanyBean;
import com.excilys.computerDatabase.model.beans.ComputerBean;
import com.excilys.computerDatabase.persistence.dao.CompanyDAO;
import com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;
import com.excilys.computerDatabase.persistence.dao.ComputerDAO;
import com.excilys.computerDatabase.persistence.dao.ComputerDAOImpl;

public class Main {
	public static void main(String args[]) {
		int limit = 40;
		int offset = 0;
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
		// Company tests
		CompanyBean companyBean = companyDAO.getById(new Long(6));
		System.out.println(companyBean);
		for (CompanyBean bean : companyDAO.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// Computer tests
		for (ComputerBean bean : computerDAO.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// update
		ComputerBean updatedBean = computerDAO.getById(new Long (576));
		if (updatedBean != null) {
			updatedBean.setDiscontinued(LocalDateTime.now());
			computerDAO.update(updatedBean);
			updatedBean = computerDAO.getById(new Long (576));
			System.out.println(updatedBean);
			updatedBean.setDiscontinued(null);
			computerDAO.update(updatedBean);
			updatedBean = computerDAO.getById(new Long (576));
			System.out.println(updatedBean);
		}
		// insert
		ComputerBean insertedBean = new ComputerBean();
		insertedBean.setName("HAL9000");
		insertedBean.setIntroduced(LocalDateTime.now());
		insertedBean.setDiscontinued(null);
		insertedBean.setCompany(companyDAO.getById(new Long(36)));
		computerDAO.create(insertedBean);
		for (ComputerBean bean : computerDAO.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// delete
		insertedBean = computerDAO.getById(new Long(575));
		if (insertedBean != null) {
			computerDAO.delete(insertedBean);
		}
		for (ComputerBean bean : computerDAO.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// Page
		Page<ComputerBean> page = new Page<ComputerBean>(computerDAO, limit, offset);
		System.out.println(page + "\n");
		page.goToNextPage();
		page.goToNextPage();
		System.out.println(page + "\n");
		page.goToPreviousPage();
		System.out.println(page);
	}
}

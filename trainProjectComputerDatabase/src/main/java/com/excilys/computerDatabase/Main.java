package main.java.com.excilys.computerDatabase;

import java.time.LocalDateTime;

import main.java.com.excilys.computerDatabase.model.CompanyBean;
import main.java.com.excilys.computerDatabase.model.ComputerBean;
import main.java.com.excilys.computerDatabase.model.pages.PageContainer;
import main.java.com.excilys.computerDatabase.persistence.CompanyDAO;
import main.java.com.excilys.computerDatabase.persistence.CompanyDAOImpl;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAO;
import main.java.com.excilys.computerDatabase.persistence.ComputerDAOImpl;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;
		ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
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
		ComputerBean updatedBean = computerDAO.getById(new Long (576));
		if (updatedBean != null) {
			updatedBean.setDiscontinued(LocalDateTime.now());
			computerDAO.updateComputer(updatedBean);
			updatedBean = computerDAO.getById(new Long (576));
			System.out.println(updatedBean);
			updatedBean.setDiscontinued(null);
			computerDAO.updateComputer(updatedBean);
			updatedBean = computerDAO.getById(new Long (576));
			System.out.println(updatedBean);
		}
		// insert
		ComputerBean insertedBean = new ComputerBean();
		insertedBean.setName("HAL9000");
		insertedBean.setIntroduced(LocalDateTime.now());
		insertedBean.setDiscontinued(null);
		insertedBean.setCompany(companyDAO.getById(new Long(36)));
		computerDAO.createComputer(insertedBean);
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
		// delete
		insertedBean = computerDAO.getById(new Long(575));
		if (insertedBean != null) {
			computerDAO.deleteComputer(insertedBean);
		}
		for (ComputerBean bean : computerDAO.getAll()) {
			System.out.println(bean);
		}
		// PageContainer
		PageContainer<ComputerBean> pages = new PageContainer<ComputerBean>(computerDAO.getAll());
	}
}

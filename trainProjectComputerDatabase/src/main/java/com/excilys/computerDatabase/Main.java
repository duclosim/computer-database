package com.excilys.computerDatabase;

import java.time.LocalDateTime;

import com.excilys.computerDatabase.model.Page;
import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.beans.Computer;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;
import com.excilys.computerDatabase.service.ComputerService;
import com.excilys.computerDatabase.service.ComputerServiceImpl;

public class Main {
	public static void main(String args[]) {
		int limit = 40;
		int offset = 0;
		CompanyService companyService = CompanyServiceImpl.INSTANCE;
		ComputerService computerService = ComputerServiceImpl.INSTANCE;
		// Company tests
		Company companyBean = companyService.getById(new Long(6));
		System.out.println(companyBean);
		for (Company bean : companyService.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// Computer tests
		for (Computer bean : computerService.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// update
		Computer updatedBean = computerService.getById(new Long (576));
		if (updatedBean != null) {
			updatedBean.setDiscontinued(LocalDateTime.now());
			computerService.update(updatedBean);
			updatedBean = computerService.getById(new Long (576));
			System.out.println(updatedBean);
			updatedBean.setDiscontinued(null);
			computerService.update(updatedBean);
			updatedBean = computerService.getById(new Long (576));
			System.out.println(updatedBean);
		}
		// insert
		Computer insertedBean = new Computer();
		insertedBean.setName("HAL9000");
		insertedBean.setIntroduced(LocalDateTime.now());
		insertedBean.setDiscontinued(null);
		insertedBean.setCompany(companyService.getById(new Long(36)));
		computerService.create(insertedBean);
		for (Computer bean : computerService.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// delete
		insertedBean = computerService.getById(new Long(575));
		if (insertedBean != null) {
			computerService.delete(insertedBean);
		}
		for (Computer bean : computerService.getAll(limit, offset)) {
			System.out.println(bean);
		}
		// Page
		Page<Computer> page = new Page<Computer>(computerService, limit, offset);
		System.out.println(page + "\n");
		page.goToNextPage();
		page.goToNextPage();
		System.out.println(page + "\n");
		page.goToPreviousPage();
		System.out.println(page);
	}
}

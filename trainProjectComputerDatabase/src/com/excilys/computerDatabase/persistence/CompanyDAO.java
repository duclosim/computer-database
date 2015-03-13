package com.excilys.computerDatabase.persistence;

import java.util.List;

import com.excilys.computerDatabase.model.CompanyBean;

public interface CompanyDAO {
	public CompanyBean getById(Long id);
	public List<CompanyBean> getAll();
}

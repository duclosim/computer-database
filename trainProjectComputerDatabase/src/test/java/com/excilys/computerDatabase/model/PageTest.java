package com.excilys.computerDatabase.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.excilys.computerDatabase.model.beans.Company;
import com.excilys.computerDatabase.model.page.Page;
import com.excilys.computerDatabase.service.CompanyService;
import com.excilys.computerDatabase.service.CompanyServiceImpl;

public class PageTest {
	private static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 2;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void constructorShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;

		int expectedCurrentPageNum = DEFAULT_OFFSET / DEFAULT_LIMIT + 1;
		int offset = (expectedCurrentPageNum - 1) * DEFAULT_LIMIT;
		List<Company> expectedEntities = new ArrayList<>(service.getAll(DEFAULT_LIMIT, offset));
		int expectedLastPageNb = (service.countLines() - offset) / DEFAULT_LIMIT + 1;
		if (expectedEntities.size() % DEFAULT_LIMIT != 0) {
			++expectedLastPageNb;
		}
		// WHEN
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);
		// THEN
		Assert.assertEquals("Erreur sur le numéro de la dernière page.", expectedLastPageNb, page.getLastPageNb());
		Assert.assertEquals("Erreur sur le numéro de la page courante.", expectedCurrentPageNum, page.getPageNum());
		Assert.assertEquals("Erreur sur les entités.", expectedEntities, page.getEntities());
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForNullDAO() {
		// GIVEN
		CompanyService service = null;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<Company>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForLimitZero() {
		// GIVEN
		int limit = 0;
		CompanyService service = CompanyServiceImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<Company>(service, limit, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForLimitNegative() {
		// GIVEN
		int limit = -1;
		CompanyService service = CompanyServiceImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<Company>(service, limit, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForOffsetNegative() {
		// GIVEN
		int offset = -1;
		CompanyService service = CompanyServiceImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<Company>(service, DEFAULT_LIMIT, offset);
		// THEN
	}
	
	@Test
	public void changingMaxItemByPageShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);
		
		int newLimit = 10;
		int newOffset = (page.getPageNum() - 1) * newLimit;
		int expectedLastPageNb = (service.countLines() - newOffset) / newLimit + 1;
		List<Company> expectedEntities = new ArrayList<>(service.getAll(newLimit, newOffset));
		if (expectedEntities.size() % newLimit != 0) {
			++expectedLastPageNb;
		}
		int expectedCurrentPageNum = newOffset / newLimit + 1;
		// WHEN
		page.setMaxNbItemsByPage(newLimit);
		// THEN
		Assert.assertEquals("Erreur sur le numéro de la dernière page.", expectedLastPageNb, page.getLastPageNb());
		Assert.assertEquals("Erreur sur le numéro de la page courante.", expectedCurrentPageNum, page.getPageNum());
		Assert.assertEquals("Erreur sur les entités.", expectedEntities, page.getEntities());
	}
	
	@Test
	public void changingMaxItemByPageShouldThrowAnIllegalArgumentExceptionForMaxNegative() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newLimit = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setMaxNbItemsByPage(newLimit);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = 2;
		int newOffset = (newPageNum - 1) * DEFAULT_LIMIT;
		List<Company> expectedEntities = new ArrayList<>(service.getAll(DEFAULT_LIMIT, newOffset));
		int expectedLastPageNb = service.countLines() / DEFAULT_LIMIT + 1;
		if (expectedEntities.size() % DEFAULT_LIMIT != 0) {
			++expectedLastPageNb;
		}
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
		Assert.assertEquals("Erreur sur le numéro de la dernière page.", expectedLastPageNb, page.getLastPageNb());
		Assert.assertEquals("Erreur sur le numéro de la page courante.", newPageNum, page.getPageNum());
		Assert.assertEquals("Erreur sur les entités.", expectedEntities, page.getEntities());
	}
	
	@Test
	public void changingPageNumShouldThrowAnIllegalArgumentExceptionForPageNumNegative() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldThrowAnIllegalArgumentExceptionForPageNumToHigh() {
		// GIVEN
		CompanyService service = CompanyServiceImpl.INSTANCE;
		Page<Company> page = new Page<>(service, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = page.getLastPageNb() + 1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
}

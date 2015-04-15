package com.excilys.computerDatabase.model.page;

import com.excilys.computerDatabase.model.dtos.ComputerDTO;
import com.excilys.computerDatabase.services.ComputerServiceImpl;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class PageTest {
	private static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 2;
	
	@Autowired
	private ComputerServiceImpl service;
	@Autowired
	private Page page;
	
	@Rule
    public final ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void constructorShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		int expectedCurrentPageNum = DEFAULT_OFFSET / DEFAULT_LIMIT + 1;
		int offset = (expectedCurrentPageNum - 1) * DEFAULT_LIMIT;
		List<ComputerDTO> expectedEntities = new ArrayList<>(service.getAll(DEFAULT_LIMIT, offset));
		int expectedLastPageNb = service.countAllLines() / DEFAULT_LIMIT + 1;
		if (expectedEntities.size() % DEFAULT_LIMIT != 0) {
			++expectedLastPageNb;
		}
		page.getEntities();
		// WHEN
		// THEN
		Assert.assertEquals("Erreur sur le numéro de la dernière page.", expectedLastPageNb, page.getLastPageNb());
		Assert.assertEquals("Erreur sur le numéro de la page courante.", expectedCurrentPageNum, page.getPageNum());
		Assert.assertEquals("Erreur sur les entités.", expectedEntities, page.getEntities());
	}
	
	@Test
	public void changingMaxItemByPageShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		int newLimit = 10;
		int newOffset = (page.getPageNum() - 1) * newLimit;
		int expectedLastPageNb = service.countAllLines() / newLimit + 1;
		List<ComputerDTO> expectedEntities = new ArrayList<>(service.getAll(newLimit, newOffset));
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
		int newLimit = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setMaxNbItemsByPage(newLimit);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		int newPageNum = 2;
		int newOffset = (newPageNum - 1) * DEFAULT_LIMIT;
		List<ComputerDTO> expectedEntities = new ArrayList<>(service.getAll(DEFAULT_LIMIT, newOffset));
		int expectedLastPageNb = service.countAllLines() / DEFAULT_LIMIT + 1;
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
		int newPageNum = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldThrowAnIllegalArgumentExceptionForPageNumToHigh() {
		// GIVEN
		int newPageNum = page.getLastPageNb() + 1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
}

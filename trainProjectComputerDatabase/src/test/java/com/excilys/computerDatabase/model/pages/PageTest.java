package test.java.com.excilys.computerDatabase.model.pages;

import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.computerDatabase.model.beans.CompanyBean;
import main.java.com.excilys.computerDatabase.model.pages.Page;
import main.java.com.excilys.computerDatabase.persistence.dao.CRUDDao;
import main.java.com.excilys.computerDatabase.persistence.dao.CompanyDAOImpl;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PageTest {
	private static final int DEFAULT_LIMIT = 10;
	private static final int DEFAULT_OFFSET = 2;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void constructorShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;

		int expectedCurrentPageNum = DEFAULT_OFFSET / DEFAULT_LIMIT + 1;
		int offset = (expectedCurrentPageNum - 1) * DEFAULT_LIMIT;
		List<CompanyBean> expectedEntities = new ArrayList<>(dao.getAll(DEFAULT_LIMIT, offset));
		int expectedLastPageNb = (dao.countLines() - offset) / DEFAULT_LIMIT + 1;
		if (expectedEntities.size() % DEFAULT_LIMIT != 0) {
			++expectedLastPageNb;
		}
		// WHEN
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);
		// THEN
		Assert.assertEquals("Erreur sur le numéro de la dernière page.", expectedLastPageNb, page.getLastPageNb());
		Assert.assertEquals("Erreur sur le numéro de la page courante.", expectedCurrentPageNum, page.getPageNum());
		Assert.assertEquals("Erreur sur les entités.", expectedEntities, page.getEntities());
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForNullDAO() {
		// GIVEN
		CRUDDao<CompanyBean> dao = null;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<CompanyBean>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForLimitZero() {
		// GIVEN
		int limit = 0;
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<CompanyBean>(dao, limit, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForLimitNegative() {
		// GIVEN
		int limit = -1;
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<CompanyBean>(dao, limit, DEFAULT_OFFSET);
		// THEN
	}
	
	@Test
	public void constructorShouldThrowAnIllegalArgumentExceptionForOffsetNegative() {
		// GIVEN
		int offset = -1;
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		new Page<CompanyBean>(dao, DEFAULT_LIMIT, offset);
		// THEN
	}
	
	@Test
	public void changingMaxItemByPageShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);
		
		int newLimit = 10;
		int newOffset = (page.getPageNum() - 1) * newLimit;
		int expectedLastPageNb = (dao.countLines() - newOffset) / newLimit + 1;
		List<CompanyBean> expectedEntities = new ArrayList<>(dao.getAll(newLimit, newOffset));
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
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newLimit = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setMaxNbItemsByPage(newLimit);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldSetTheLastPageNbAndCurrentPageNumAndEntities() {
		// GIVEN
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = 2;
		int newOffset = (newPageNum - 1) * DEFAULT_LIMIT;
		List<CompanyBean> expectedEntities = new ArrayList<>(dao.getAll(DEFAULT_LIMIT, newOffset));
		int expectedLastPageNb = (dao.countLines() - newOffset) / DEFAULT_LIMIT + 1;
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
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = -1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
	
	@Test
	public void changingPageNumShouldThrowAnIllegalArgumentExceptionForPageNumToHigh() {
		// GIVEN
		CRUDDao<CompanyBean> dao = CompanyDAOImpl.INSTANCE;
		Page<CompanyBean> page = new Page<>(dao, DEFAULT_LIMIT, DEFAULT_OFFSET);

		int newPageNum = page.getLastPageNb() + 1;
		thrown.expect(IllegalArgumentException.class);
		// WHEN
		page.setPageNum(newPageNum);
		// THEN
	}
}

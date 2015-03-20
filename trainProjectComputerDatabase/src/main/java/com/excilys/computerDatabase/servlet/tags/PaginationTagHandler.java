package com.excilys.computerDatabase.servlet.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PaginationTagHandler extends SimpleTagSupport {
	private int curPage;

	private int maxPage;
	private static final int NB_PAGE_NB = 3;
	
	public int getCurPage() {
		return curPage;
	}
	public int getMaxPage() {
		return maxPage;
	}
   public void setCurPage(int curPage) {
	   this.curPage = curPage;
	   if ((0 >= curPage) || (curPage > maxPage)) {
		   throw new IllegalArgumentException("curPage est hors-limites");
	   }
	}
	public void setMaxPage(int maxPage) {
	   if (maxPage <= 0) {
		   throw new IllegalArgumentException("maxPage est négatif ou nul.");
	   }
	   this.maxPage = maxPage;
	}

	@Override
	public void doTag() throws JspException, IOException {
		StringWriter sw = new StringWriter();
		/* use message from the body */
		int startPage = Integer.max(curPage - NB_PAGE_NB, 1);
		int endPage = Integer.min(curPage + NB_PAGE_NB, maxPage);
		
		getJspBody().invoke(sw);
		getJspContext().getOut().println(sw.toString());
	}
}

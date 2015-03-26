package com.excilys.computerDatabase.model.page;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerDatabase.persistence.dao.ComputerColumn;
import com.excilys.computerDatabase.persistence.dao.OrderingWay;
import com.excilys.computerDatabase.service.PageableService;

public class OrderedPage<T> extends Page<T> {
	private static final Logger LOG = LoggerFactory.getLogger(OrderedPage.class);
	
	private ComputerColumn column;
	private OrderingWay way;

	public OrderedPage(PageableService<T> service, int limit, 
			int offset, ComputerColumn column, OrderingWay way) {
		super(service, limit, offset);
		LOG.trace(new StringBuilder("new OrderedPage(")
			.append(service).append(", ")
			.append(limit).append(", ")
			.append(offset).append(", ")
			.append(column).append(", ")
			.append(way).append(")")
			.toString());
		this.column = column;
		this.way = way;
	}

	public OrderedPage(PageableService<T> service, ComputerColumn column, 
			OrderingWay way) {
		super(service);
		LOG.trace(new StringBuilder("new OrderedPage(")
			.append(service).append(", ")
			.append(column).append(", ")
			.append(way).append(")")
			.toString());
		this.column = column;
		this.way = way;
	}

	public ComputerColumn getColumn() {
		return column;
	}
	public void setColumn(ComputerColumn column) {
		this.column = column;
	}
	public OrderingWay getWay() {
		return way;
	}
	public void setWay(OrderingWay way) {
		this.way = way;
	}

	@Override
	public void reloadEntities() {
		LOG.trace("reloadEntities()");
		int offset = (getPageNum() - 1) * getMaxNbItemsByPage();
		setEntities(new ArrayList<>(getService()
				.getAll(getMaxNbItemsByPage(), offset, column, way)));
	}

}

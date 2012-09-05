/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin GroupTag.java 2012-8-3 22:23:39 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.beans.BeanUtils;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Group;

/**
 * The Class GroupTag.
 *
 * @author l.xue.nong
 */
public class GroupTag extends BodyTagSupport {
	private static final long serialVersionUID = 2649298781515690249L;

	/** The header. */
	private String header;

	/** The columns. */
	private List<Object> columns = new ArrayList<Object>();

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		Object parentTag = getParent();
		Group group = new Group();
		BeanUtils.copyProperties(this, group, new String[] { "columns" });
		group.getColumns().addAll(this.getColumns());

		if (GridTag.class.isInstance(parentTag)) {
			((GridTag) parentTag).addGroup(group);
		} else if (GroupTag.class.isInstance(parentTag)) {
			((GroupTag) parentTag).addGroup(group);
		}
		this.release();
		return super.doEndTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		if (null != this.getColumns()) {
			this.getColumns().clear();
		}
		super.release();
	}

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Sets the header.
	 *
	 * @param header the new header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public List<Object> getColumns() {
		return columns;
	}

	/**
	 * Sets the columns.
	 *
	 * @param columns the new columns
	 */
	public void setColumns(List<Object> columns) {
		this.columns = columns;
	}

	/**
	 * Adds the column.
	 *
	 * @param column the column
	 */
	public void addColumn(Column column) {
		int index = this.getColumns().indexOf(column);
		if (index == -1) {
			this.getColumns().add(column);
			return;
		}
		this.getColumns().set(index, column);
	}

	/**
	 * Adds the group.
	 *
	 * @param group the group
	 */
	public void addGroup(Group group) {
		this.getColumns().add(group);
	}
}
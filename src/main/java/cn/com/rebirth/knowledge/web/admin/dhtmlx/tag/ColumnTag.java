/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ColumnTag.java 2012-8-3 22:22:43 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.BeanUtils;

import cn.com.rebirth.commons.search.annotation.AbstractSearchProperty;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;

/**
 * The Class ColumnTag.
 *
 * @author l.xue.nong
 */
public class ColumnTag extends BodyTagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8628461037896420359L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		Column column = new Column();
		BeanUtils.copyProperties(this, column);
		Tag parentTag = TagSupport.findAncestorWithClass(this, GroupTag.class);
		if (parentTag == null) {
			parentTag = TagSupport.findAncestorWithClass(this, GridTag.class);
		}
		if (parentTag == null)
			throw new JspException("Column Tag parent Find null");
		column.setParent(parentTag);
		if (GridTag.class.isInstance(parentTag)) {
			((GridTag) parentTag).addColumn(column);
		} else if (GroupTag.class.isInstance(parentTag)) {
			((GroupTag) parentTag).addColumn(column);
		}
		return super.doEndTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		super.release();
	}

	/** The id. */
	private String id;

	/** The header. */
	private String header;

	/** The width. */
	private String width = "0";

	/** The align. */
	private String align = "left";

	/** The frozen. */
	private boolean frozen = false;

	/** The visible. */
	private boolean visible = true;

	/** The sortable. */
	private boolean sortable = true;

	/** The color. */
	private String color = "";

	/** The format. */
	private String format = "";

	/** The query expression. */
	private String queryExpression = "";

	/** The type. */
	private String type = "ro";

	/** The exportable. */
	private boolean exportable = true;

	/** The is key. */
	private boolean isKey = false;

	/** The column data sets. */
	private ColumnDataSets columnDataSets;

	/** The property. */
	private AbstractSearchProperty property;

	/** The group. */
	private boolean group = false;

	/**
	 * Checks if is key.
	 *
	 * @return true, if is key
	 */
	public boolean isKey() {
		return isKey;
	}

	/**
	 * Sets the checks if is key.
	 *
	 * @param isKey the new checks if is key
	 */
	public void setIsKey(boolean isKey) {
		this.isKey = isKey;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * Checks if is exportable.
	 *
	 * @return true, if is exportable
	 */
	public boolean isExportable() {
		return exportable;
	}

	/**
	 * Sets the exportable.
	 *
	 * @param exportable the new exportable
	 */
	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Gets the width.
	 *
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * Gets the align.
	 *
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * Sets the align.
	 *
	 * @param align the new align
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * Checks if is frozen.
	 *
	 * @return true, if is frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * Sets the frozen.
	 *
	 * @param frozen the new frozen
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		if ("0".equals(this.getWidth())) {
			return false;
		}
		return visible;
	}

	/**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Checks if is sortable.
	 *
	 * @return true, if is sortable
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * Sets the sortable.
	 *
	 * @param sortable the new sortable
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the format.
	 *
	 * @param format the new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Gets the query expression.
	 *
	 * @return the query expression
	 */
	public String getQueryExpression() {
		return queryExpression;
	}

	/**
	 * Sets the query expression.
	 *
	 * @param queryExpression the new query expression
	 */
	public void setQueryExpression(String queryExpression) {
		this.queryExpression = queryExpression;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the column data sets.
	 *
	 * @return the column data sets
	 */
	public ColumnDataSets getColumnDataSets() {
		return columnDataSets;
	}

	/**
	 * Sets the column data sets.
	 *
	 * @param columnDataSets the new column data sets
	 */
	public void setColumnDataSets(ColumnDataSets columnDataSets) {
		this.columnDataSets = columnDataSets;
	}

	/**
	 * Sets the key.
	 *
	 * @param isKey the new key
	 */
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	/**
	 * Gets the property.
	 *
	 * @return the property
	 */
	public AbstractSearchProperty getProperty() {
		return property;
	}

	/**
	 * Sets the property.
	 *
	 * @param property the new property
	 */
	public void setProperty(AbstractSearchProperty property) {
		this.property = property;
	}

	/**
	 * Checks if is group.
	 *
	 * @return true, if is group
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}

}
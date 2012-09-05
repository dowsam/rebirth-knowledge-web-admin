/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin DataSettingTag.java 2012-8-4 11:05:20 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import cn.com.rebirth.commons.utils.BeanUtils;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.DataSetting;

/**
 * The Class DataSettingTag.
 *
 * @author l.xue.nong
 */
public class DataSettingTag extends BodyTagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3766823275001879138L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
		gridTag.setDataSetting(BeanUtils.copyProperties(DataSetting.class, this));
		return super.doEndTag();
	}

	/** The action handler. */
	private String actionHandler;

	/** The row converter. */
	private String rowConverter;

	/** The include page params. */
	private boolean includePageParams = false;

	/** The fixed query condition. */
	private String fixedQueryCondition;

	/** The type. */
	private String type = "json";

	/**
	 * Gets the action handler.
	 *
	 * @return the action handler
	 */
	public String getActionHandler() {
		return actionHandler;
	}

	/**
	 * Sets the action handler.
	 *
	 * @param actionHandler the new action handler
	 */
	public void setActionHandler(String actionHandler) {
		this.actionHandler = actionHandler;
	}

	/**
	 * Gets the row converter.
	 *
	 * @return the row converter
	 */
	public String getRowConverter() {
		return rowConverter;
	}

	/**
	 * Sets the row converter.
	 *
	 * @param rowConverter the new row converter
	 */
	public void setRowConverter(String rowConverter) {
		this.rowConverter = rowConverter;
	}

	/**
	 * Checks if is include page params.
	 *
	 * @return true, if is include page params
	 */
	public boolean isIncludePageParams() {
		return includePageParams;
	}

	/**
	 * Sets the include page params.
	 *
	 * @param includePageParams the new include page params
	 */
	public void setIncludePageParams(boolean includePageParams) {
		this.includePageParams = includePageParams;
	}

	/**
	 * Gets the fixed query condition.
	 *
	 * @return the fixed query condition
	 */
	public String getFixedQueryCondition() {
		return fixedQueryCondition;
	}

	/**
	 * Sets the fixed query condition.
	 *
	 * @param fixedQueryCondition the new fixed query condition
	 */
	public void setFixedQueryCondition(String fixedQueryCondition) {
		this.fixedQueryCondition = fixedQueryCondition;
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

}

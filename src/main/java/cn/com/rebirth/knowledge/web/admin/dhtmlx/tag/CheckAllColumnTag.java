/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin CheckAllColumnTag.java 2012-8-3 22:24:31 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.CheckAllColumn;

/**
 * The Class CheckAllColumnTag.
 *
 * @author l.xue.nong
 */
public class CheckAllColumnTag extends ColumnTag {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 629296591753952668L;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CheckAllColumnTag.class);

	/**
	 * Instantiates a new check all column tag.
	 */
	public CheckAllColumnTag() {
		this.setAlign("center");
		this.setHeader("{#master_page_checkbox}");
		this.setType("cb");
		this.setId("_checkAll");
		this.setFrozen(true);
		this.setWidth("5");
		this.setExportable(false);
	}

	@Override
	public int doEndTag() throws JspException {
		GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
		CheckAllColumn checkAllColumn = new CheckAllColumn();
		try {
			BeanUtils.copyProperties(checkAllColumn, this);
			gridTag.setCheckAllColumn(checkAllColumn);
		} catch (IllegalAccessException e) {
			logger.error("cause error when copy columnTag's property into new Column", e);
		} catch (InvocationTargetException e) {
			logger.error("cause error when copy columnTag's property into new Column", e);
		}
		return super.doEndTag();
	}
}
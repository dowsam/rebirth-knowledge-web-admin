/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin OperationColumnTag.java 2012-8-3 22:25:12 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Action;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.OperationColumn;

/**
 * The Class OperationColumnTag.
 *
 * @author l.xue.nong
 */
public class OperationColumnTag extends ColumnTag {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3047716773391977562L;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(OperationColumnTag.class);

	/** The actions. */
	private List<Action> actions = new ArrayList<Action>();

	/** The on before menu render. */
	private String onBeforeMenuRender;

	@Override
	public int doEndTag() throws JspException {
		GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
		OperationColumn operationColumn = new OperationColumn();
		try {
			BeanUtils.copyProperties(operationColumn, this);
			gridTag.setOperationColumn(operationColumn);
		} catch (IllegalAccessException e) {
			logger.error("cause error when copy columnTag's property into new Column", e);
		} catch (InvocationTargetException e) {
			logger.error("cause error when copy columnTag's property into new Column", e);
		}
		return super.doEndTag();
	}

	/**
	 * Instantiates a new operation column tag.
	 */
	public OperationColumnTag() {
		this.setId("_operation");
		this.setAlign("center");
		this.setHeader("操作");
		this.setType("opt");
		this.setFrozen(true);
		this.setWidth("6");
		this.setExportable(false);
	}

	/**
	 * Gets the actions.
	 *
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * Sets the actions.
	 *
	 * @param actions the new actions
	 */
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	/**
	 * Gets the on before menu render.
	 *
	 * @return the on before menu render
	 */
	public String getOnBeforeMenuRender() {
		return onBeforeMenuRender;
	}

	/**
	 * Sets the on before menu render.
	 *
	 * @param onBeforeMenuRender the new on before menu render
	 */
	public void setOnBeforeMenuRender(String onBeforeMenuRender) {
		this.onBeforeMenuRender = onBeforeMenuRender;
	}
}
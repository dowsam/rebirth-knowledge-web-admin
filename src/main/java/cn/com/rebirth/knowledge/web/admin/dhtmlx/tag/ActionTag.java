/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ActionTag.java 2012-8-3 22:24:22 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Action;

/**
 * The Class ActionTag.
 *
 * @author l.xue.nong
 */
public class ActionTag extends BodyTagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1292323753353993673L;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ActionTag.class);

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		OperationColumnTag operationColumnTag = (OperationColumnTag) TagSupport.findAncestorWithClass(this,
				OperationColumnTag.class);
		Action action = new Action();
		try {
			BeanUtils.copyProperties(action, this);
			operationColumnTag.getActions().add(action);
		} catch (IllegalAccessException e) {
			logger.error("cause error when copy actionTag's property into new Action", e);
		} catch (InvocationTargetException e) {
			logger.error("cause error when copy actionTag's property into new Action", e);
		}
		return super.doEndTag();
	}

	/** The id. */
	private String id;

	/** The icon. */
	private String icon;

	/** The label. */
	private String label;

	/** The on click. */
	private String onClick;

	/** The visible. */
	private boolean visible = true;

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
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

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the on click.
	 *
	 * @return the on click
	 */
	public String getOnClick() {
		return onClick;
	}

	/**
	 * Sets the on click.
	 *
	 * @param onClick the new on click
	 */
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
}
/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin PageTag.java 2012-8-3 22:25:25 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.PageSetting;

/**
 * The Class PageTag.
 *
 * @author l.xue.nong
 */
public class PageTag extends BodyTagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4382229367105891932L;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(PageTag.class);

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		PageSetting pageSetting = new PageSetting();
		try {
			BeanUtils.copyProperties(pageSetting, this);
			GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
			gridTag.setPageSetting(pageSetting);
		} catch (IllegalAccessException e) {
			logger.error("cause error when copy PageTag's property into new PageSetting", e);
		} catch (InvocationTargetException e) {
			logger.error("cause error when copy PageTag's property into new PageSetting", e);
		}

		return super.doEndTag();
	}

	/** The enabled. */
	private boolean enabled = true;

	/** The size. */
	private int size;

	/** The size list. */
	private String sizeList;

	/** The style. */
	private String style = "background-color:white;overflow:hidden";

	/** The position. */
	private String position = "bottom";

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the size list.
	 *
	 * @return the size list
	 */
	public String getSizeList() {
		return sizeList;
	}

	/**
	 * Sets the size list.
	 *
	 * @param sizeList the new size list
	 */
	public void setSizeList(String sizeList) {
		this.sizeList = sizeList;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets the style.
	 *
	 * @param style the new style
	 */
	public void setStyle(String style) {
		this.style = style;
	}
}
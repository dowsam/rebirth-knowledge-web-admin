/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin DataProcessorTag.java 2012-8-10 23:20:09 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.DataProcessor;

/**
 * The Class DataProcessorTag.
 *
 * @author l.xue.nong
 */
public class DataProcessorTag extends DataProcessor {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5181320433376744914L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		if (isEnabled()) {
			GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
			if (gridTag != null) {
				gridTag.setDataProcessor(this);
			}
		}
		return super.doEndTag();
	}

}

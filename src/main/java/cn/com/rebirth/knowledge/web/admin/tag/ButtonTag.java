/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ButtonTag.java 2012-8-27 14:57:13 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import cn.com.rebirth.core.template.FreeMarkerTemplateEngine;
import cn.com.rebirth.core.template.TemplateEngineFactory;
import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.knowledge.commons.entity.system.SysPageButtonEntity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class ButtonTag.
 *
 * @author l.xue.nong
 */
public class ButtonTag extends BodyTagSupport {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -118982463601945912L;

	/** The button entities. */
	private List<SysPageButtonEntity> buttonEntities = Lists.newArrayList();

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		FreeMarkerTemplateEngine templateEngine = (FreeMarkerTemplateEngine) TemplateEngineFactory
				.createTemplateEngine("dhtmlxTree.flt");
		templateEngine.setPath("/templates/");
		Map<String, Object> param = Maps.newHashMap();
		param.put("servletContext", this.pageContext.getServletContext());
		param.put("mylist", buttonEntities);
		param.put("base", AbstractBaseController.getServerPath((HttpServletRequest) pageContext.getRequest()));
		try {
			String json = templateEngine.renderFile("button.flt", param);
			this.pageContext.getOut().write(json);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	/**
	 * Gets the button entities.
	 *
	 * @return the button entities
	 */
	public List<SysPageButtonEntity> getButtonEntities() {
		return buttonEntities;
	}

	/**
	 * Sets the button entities.
	 *
	 * @param buttonEntities the new button entities
	 */
	public void setButtonEntities(List<SysPageButtonEntity> buttonEntities) {
		this.buttonEntities = buttonEntities;
	}

}

/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin HttpEntityInfoDhtmlxConfig.java 2012-8-3 8:10:43 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx;

import java.util.Map;

import javax.servlet.ServletContext;

import cn.com.rebirth.core.template.FreeMarkerTemplateEngine;
import cn.com.rebirth.core.template.TemplateEngine;
import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxConfig;
import cn.com.rebirth.knowledge.commons.dhtmlx.impl.EntityInfoDhtmlxConfig;
import freemarker.template.Configuration;

/**
 * The Class HttpEntityInfoDhtmlxConfig.
 *
 * @author l.xue.nong
 */
public class HttpEntityInfoDhtmlxConfig extends EntityInfoDhtmlxConfig implements DhtmlxConfig {

	/** The servlet context. */
	private final ServletContext servletContext;

	/** The orther param. */
	private final Map<String, Object> ortherParam;

	/**
	 * Instantiates a new http entity info dhtmlx config.
	 *
	 * @param entityClass the entity class
	 * @param servletContext the servlet context
	 * @param ortherParam the orther param
	 */
	public HttpEntityInfoDhtmlxConfig(Class<?> entityClass, ServletContext servletContext,
			Map<String, Object> ortherParam) {
		super(entityClass);
		this.servletContext = servletContext;
		this.ortherParam = ortherParam;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.dhtmlx.AbstractDhtmlxConfig#createTemplateEngine(java.lang.String)
	 */
	@Override
	protected TemplateEngine createTemplateEngine(String templateName) {
		return new FreeMarkerTemplateEngine(new Configuration(), this.servletContext, "/templates");
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.dhtmlx.impl.EntityInfoDhtmlxConfig#toParam()
	 */
	@Override
	public Map<String, Object> toParam() {
		Map<String, Object> map = super.toParam();
		if (this.ortherParam != null) {
			map.putAll(ortherParam);
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.dhtmlx.AbstractDhtmlxConfig#getImagePath()
	 */
	@Override
	public String getImagePath() {
		System.setProperty("base", AbstractBaseController.getServerPath(RequestContext.get().request()));
		return super.getImagePath();
	}

}

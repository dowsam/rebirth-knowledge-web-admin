/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin TreeTag.java 2012-8-21 16:35:53 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import cn.com.rebirth.core.template.FreeMarkerTemplateEngine;
import cn.com.rebirth.core.template.TemplateEngineFactory;
import cn.com.rebirth.knowledge.commons.dhtmlx.GridRequest;
import cn.com.rebirth.knowledge.commons.dhtmlx.builder.DhtmlxGridBuilder;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Grid;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class TreeTag.
 *
 * @author l.xue.nong
 */
public class TreeTag extends Tree {

	/**
	 * The Class DhtmlxGridBuilderTo.
	 *
	 * @author l.xue.nong
	 */
	private class DhtmlxGridBuilderTo extends DhtmlxGridBuilder {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.commons.dhtmlx.builder.DhtmlxGridBuilder#bulidScriptTop(java.lang.StringBuilder, java.lang.String, cn.com.rebirth.knowledge.commons.dhtmlx.entity.Grid)
		 */
		@Override
		public void bulidScriptTop(StringBuilder scripts, String gridVar, Grid grid) {
			scripts.append("var ").append(gridVar).append(" = null").append(";\n");
			scripts.append("var ").append(gridVar).append("DataProcessor").append(" = null").append(";\n");
			bulidVar(scripts, grid);
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.commons.dhtmlx.builder.DhtmlxGridBuilder#bulidContext(cn.com.rebirth.knowledge.commons.dhtmlx.entity.Grid, java.lang.StringBuilder, java.lang.String, cn.com.rebirth.knowledge.commons.dhtmlx.GridRequest)
		 */
		@Override
		public void bulidContext(Grid grid, StringBuilder scripts, String gridVar, GridRequest request) {
			super.bulidContext(grid, scripts, gridVar, request);
		}

		@Override
		public void buildGridContainer(StringBuilder scripts, Grid grid) {
			super.buildGridContainer(scripts, grid);
		}

	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1989142746216916600L;

	/** The child. */
	private List<Object> children = Lists.newArrayList();

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
		param.put("item", this);
		param.put("clildContext", bulidChildContext(param));
		param.put("url", bulidUrl());
		try {
			String json = templateEngine.renderFile("dhtmlxTree.flt", param);
			this.pageContext.getOut().write(json);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

	/**
	 * Bulid url.
	 *
	 * @return the object
	 */
	private Object bulidUrl() {
		String url = getActionHandler();
		if (url.indexOf("?") == -1) {
			url = url + "?syn=" + isSyn();
		} else {
			url = url + "&syn=" + isSyn();
		}
		return url;
	}

	/**
	 * Bulid child context.
	 * @param param 
	 *
	 * @return the object
	 */
	private Object bulidChildContext(Map<String, Object> param) {
		DhtmlxGridBuilderTo builder = new DhtmlxGridBuilderTo();
		StringBuilder scripts = new StringBuilder();
		StringBuilder containerScripts = new StringBuilder();
		StringBuilder varScripts = new StringBuilder();
		for (Object object : this.children) {
			if (object instanceof Grid) {
				Grid grid = (Grid) object;
				GridRequest gridRequest = new GridRequest((HttpServletRequest) this.pageContext.getRequest(), true);
				grid.setParentGrid(this);
				builder.buildGridContainer(containerScripts, grid);
				builder.bulidScriptTop(varScripts, grid.getId(), grid);
				builder.bulidContext(grid, scripts, grid.getId(), gridRequest);
			}
		}
		param.put("gridContainer", containerScripts.toString());
		param.put("varScripts", varScripts.toString());
		return scripts.toString();
	}

	/**
	 * Adds the child.
	 *
	 * @param o the o
	 */
	public void addChild(Object o) {
		if (!this.children.contains(o)) {
			this.children.add(o);
		}
	}

	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public List<Object> getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 *
	 * @param children the new children
	 */
	public void setChildren(List<Object> children) {
		this.children = children;
	}

}

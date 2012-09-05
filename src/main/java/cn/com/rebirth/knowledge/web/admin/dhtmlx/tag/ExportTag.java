/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ExportTag.java 2012-8-3 22:24:49 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.tag;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.knowledge.commons.dhtmlx.entity.ExportSetting;

/**
 * The Class ExportTag.
 *
 * @author l.xue.nong
 */
public class ExportTag extends BodyTagSupport {

	private static final long serialVersionUID = -6747710310260240820L;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExportTag.class);

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		GridTag gridTag = (GridTag) TagSupport.findAncestorWithClass(this, GridTag.class);
		ExportSetting export = new ExportSetting();
		try {
			BeanUtils.copyProperties(export, this);
			gridTag.setExport(export);
		} catch (IllegalAccessException e) {
			logger.error("cause error when copy exportTag's property into new ExportSetting", e);
		} catch (InvocationTargetException e) {
			logger.error("cause error when copy exportTag's property into new ExportSetting", e);
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

	/** The enabled. */
	private boolean enabled = true;

	/** The file name. */
	private String fileName;

	/** The template file name. */
	private String templateFileName;

	/** The row converter. */
	private String rowConverter;

	/** The data exporter. */
	private String dataExporter;
	private String actionHandler;

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
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the template file name.
	 *
	 * @return the template file name
	 */
	public String getTemplateFileName() {
		return templateFileName;
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
	 * Gets the data exporter.
	 *
	 * @return the data exporter
	 */
	public String getDataExporter() {
		return dataExporter;
	}

	/**
	 * Sets the data exporter.
	 *
	 * @param dataExporter the new data exporter
	 */
	public void setDataExporter(String dataExporter) {
		this.dataExporter = dataExporter;
	}

	/**
	 * Sets the template file name.
	 *
	 * @param templateFileName the new template file name
	 */
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getActionHandler() {
		return actionHandler;
	}

	public void setActionHandler(String actionHandler) {
		this.actionHandler = actionHandler;
	}

}
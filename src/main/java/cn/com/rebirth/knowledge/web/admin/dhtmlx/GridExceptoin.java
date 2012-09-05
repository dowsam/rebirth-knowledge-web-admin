/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin GridExceptoin.java 2012-8-3 13:59:25 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx;

import cn.com.rebirth.commons.exception.RebirthException;

/**
 * The Class GridExceptoin.
 *
 * @author l.xue.nong
 */
public class GridExceptoin extends RebirthException {

	/**
	 * Instantiates a new grid exceptoin.
	 *
	 * @param msg the msg
	 */
	public GridExceptoin(String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new grid exceptoin.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public GridExceptoin(String msg, Throwable cause) {
		super(msg, cause);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4405366546399927897L;

}

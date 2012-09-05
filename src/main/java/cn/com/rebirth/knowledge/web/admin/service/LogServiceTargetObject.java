/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin LogServiceTargetObject.java 2012-8-14 10:44:02 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.service.middleware.server.support.TargetObjectContainer;

/**
 * The Class LogServiceTargetObject.
 *
 * @author l.xue.nong
 */
public class LogServiceTargetObject implements TargetObjectContainer<LogServiceImpl> {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.service.middleware.server.support.TargetObjectContainer#find(java.lang.Class)
	 */
	@Override
	public LogServiceImpl find(Class<?> targetObjectClass) {
		return RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
				.getInstance(LogServiceImpl.class);
	}

}

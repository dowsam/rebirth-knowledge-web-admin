/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ServiceMiddlewareServer.java 2012-8-14 10:58:24 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import cn.com.rebirth.commons.AbstractContainer;
import cn.com.rebirth.commons.Container;
import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.service.middleware.server.Bootstrap;

/**
 * The Class ServiceMiddlewareServer.
 *
 * @author l.xue.nong
 */
public class ServiceMiddlewareServer extends AbstractContainer implements Container {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.AbstractContainer#afterContainerStart(cn.com.rebirth.commons.RebirthContainer)
	 */
	@Override
	protected void afterContainerStart(RebirthContainer container) {
		Bootstrap.main(new String[] {});
		container.get(InjectInitialization.class).getInjector()
				.createChildInjector(new ServiceMiddlewareClientModule());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Container#start()
	 */
	@Override
	public void start() {

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Container#stop()
	 */
	@Override
	public void stop() {
	}

}

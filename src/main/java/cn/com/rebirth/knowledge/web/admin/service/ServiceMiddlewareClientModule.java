/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ServiceMiddlewareModule.java 2012-8-8 12:05:14 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import cn.com.rebirth.core.inject.AbstractModule;
import cn.com.rebirth.core.inject.util.Providers;
import cn.com.rebirth.service.middleware.client.ConsumerProxyFactory;

/**
 * The Class ServiceMiddlewareModule.
 */
public class ServiceMiddlewareClientModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(ConsumerProxyFactory.class).toProvider(Providers.of(ConsumerProxyFactory.getInstance()))
				.asEagerSingleton();
	}

}

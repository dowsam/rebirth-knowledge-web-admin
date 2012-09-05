/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin LogServiceModule.java 2012-8-14 10:47:15 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import cn.com.rebirth.commons.utils.CglibProxyUtils;
import cn.com.rebirth.commons.utils.SpringContextHolder;
import cn.com.rebirth.core.inject.AbstractModule;
import cn.com.rebirth.knowledge.commons.service.LogService;

/**
 * The Class LogServiceModule.
 *
 * @author l.xue.nong
 */
public class LogServiceModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(LogService.class).toInstance(CglibProxyUtils.getProxyInstance(LogService.class, new MethodInterceptor() {
			private LogService logService;

			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				if (logService == null) {
					logService = SpringContextHolder.getBean(LogServiceImpl.class);
				}
				return method.invoke(logService, args);
			}
		}));
	}

}

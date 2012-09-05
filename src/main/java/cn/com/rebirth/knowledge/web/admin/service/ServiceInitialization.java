/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ServiceMiddlewareInitialization.java 2012-8-8 12:34:05 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.com.rebirth.commons.settings.ThreadSafeVariableSettings;
import cn.com.rebirth.commons.settings.ThreadSafeVariableSettings.Builder;
import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.commons.utils.ClassResolverUtils.AbstractFindCallback;
import cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback;
import cn.com.rebirth.commons.utils.ResolverUtils;
import cn.com.rebirth.core.inject.Business;
import cn.com.rebirth.core.inject.ModulesBuilder;
import cn.com.rebirth.core.settings.SettingsModule;
import cn.com.rebirth.knowledge.commons.ResourceInitLoad;

import com.google.common.collect.Lists;

/**
 * The Class ServiceMiddlewareInitialization.
 *
 * @author l.xue.nong
 */
public class ServiceInitialization implements Business {

	/** The init loads. */
	private static List<ResourceInitLoad> initLoads = Lists.newArrayList();

	/** The find callback. */
	private static FindCallback<ResourceInitLoad> findCallback = new AbstractFindCallback<ResourceInitLoad>() {

		@Override
		protected void doFindType(ResolverUtils<ResourceInitLoad> resolverUtils, Class<ResourceInitLoad> entityClass) {
			resolverUtils.findImplementations(entityClass, StringUtils.EMPTY);
		}

	};
	static {
		initLoads = ClassResolverUtils.find(findCallback);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.inject.Business#toModules(cn.com.rebirth.core.inject.ModulesBuilder)
	 */
	@Override
	public void toModules(ModulesBuilder modulesBuilder) {
		ThreadSafeVariableSettings.Builder builder = ThreadSafeVariableSettings.settingsBuilder().putProperties(
				"rebirth.", System.getProperties());
		for (ResourceInitLoad initLoad : initLoads) {
			initLoad.loadResource(builder);
		}
		builder.replacePropertyPlaceholders();
		modulesBuilder
				.add(new SettingsModule(ThreadSafeVariableSettings.settingsBuilder().put(builder.build()).build()));
		modulesBuilder.add(new LogServiceModule());
	}

	/**
	 * The Class LogSqlProperties.
	 *
	 * @author l.xue.nong
	 */
	public static class LogSqlProperties implements ResourceInitLoad {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.ResourceInitLoad#loadResource(cn.com.rebirth.knowledge.web.admin.ThreadSafeVariableSettings.Builder)
		 */
		@Override
		public void loadResource(Builder builder) {
			builder.loadFromClasspath("logsql.properties");
		}

	}

	/**
	 * The Class EmailProperties.
	 *
	 * @author l.xue.nong
	 */
	public static class EmailProperties implements ResourceInitLoad {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.ResourceInitLoad#loadResource(cn.com.rebirth.knowledge.web.admin.ThreadSafeVariableSettings.Builder)
		 */
		@Override
		public void loadResource(Builder builder) {
			builder.loadFromClasspath("email/mail.properties");
		}

	}

	/**
	 * The Class AppProperties.
	 *
	 * @author l.xue.nong
	 */
	public static class AppProperties implements ResourceInitLoad {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.ResourceInitLoad#loadResource(cn.com.rebirth.knowledge.web.admin.ThreadSafeVariableSettings.Builder)
		 */
		@Override
		public void loadResource(Builder builder) {
			builder.loadFromClasspath("application.properties");
		}

	}

}

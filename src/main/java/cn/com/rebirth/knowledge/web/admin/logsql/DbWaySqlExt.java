/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin DbWaySqlExt.java 2012-8-16 11:35:01 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.logsql;

import java.util.concurrent.BlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.logsql.ConcurrentWaySql;
import cn.com.rebirth.core.logsql.DbWaySql;
import cn.com.rebirth.core.logsql.LogSqlEntity;
import cn.com.rebirth.core.queue.QueuesHolder;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

/**
 * The Class DbWaySqlExt.
 *
 * @author l.xue.nong
 */
@Component
@Lazy(value = false)
public class DbWaySqlExt extends DbWaySql implements ConcurrentWaySql {

	/**
	 * Instantiates a new db way sql ext.
	 */
	public DbWaySqlExt() {
		this(InjectInitialization.injector().getInstance(Settings.class));
	}

	/**
	 * Instantiates a new db way sql ext.
	 *
	 * @param settings the settings
	 */
	public DbWaySqlExt(Settings settings) {
		super(settings);
		setQueueName(settings.get("queueName"));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.logsql.ConcurrentWaySql#getBlockingQueue()
	 */
	@Override
	public BlockingQueue<LogSqlEntity> getBlockingQueue() {
		return QueuesHolder.getQueue(settings.get("queueName"));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.logsql.AbstractLogWaySql#action(cn.com.rebirth.core.logsql.LogSqlEntity)
	 */
	@Override
	public void action(LogSqlEntity entity) {
		RequestContext requestContext = RequestContext.get();
		if (requestContext != null) {
			HttpServletRequest httpServletRequest = requestContext.request();
			if (httpServletRequest != null) {
				entity.setRequestIp(requestContext.requestIp());
			}
		}
		entity.setUserId(UserService.currentUser() != null ? UserService.currentUser().getId() : null);
		entity.getContext().put("user", UserService.currentUser());
		super.action(entity);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.logsql.DbWaySql#execute(cn.com.rebirth.core.logsql.LogSqlEntity)
	 */
	@Override
	public void execute(LogSqlEntity entity) {
		String methodName = entity.getMethodName();
		if (methodName.length() > 1000) {
			methodName = methodName.substring(0, 1000);
		}
		if (methodName.indexOf("(") != -1) {
			methodName = methodName.substring(0, methodName.indexOf("("));
		}
		entity.setMethodName(methodName);
		SysLogEntity sysLogEntity = new SysLogEntity();
		sysLogEntity.setSysUserEntity((SysUserEntity) entity.getContext().get("user"));
		sysLogEntity.setRequestIp(entity.getRequestIp());
		sysLogEntity.setLogContext(getLogMessage(entity).getBytes());
		InjectInitialization.injector().getInstance(LogService.class).addLog(sysLogEntity);
	}
}

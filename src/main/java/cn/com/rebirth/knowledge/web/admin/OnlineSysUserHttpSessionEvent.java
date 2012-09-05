/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin OnlineSysUserHttpSessionEvent.java 2012-8-15 12:51:19 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin;

import javax.servlet.http.HttpSession;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.utils.DateUtils;
import cn.com.rebirth.commons.utils.IpUtils;
import cn.com.rebirth.commons.utils.ReflectionUtils;
import cn.com.rebirth.commons.utils.SpringContextHolder;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.web.HttpSessionEvent;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.knowledge.web.admin.filter.FailUserContextFilter;
import cn.com.rebirth.knowledge.web.admin.service.UserService;
import cn.com.rebirth.persistence.service.BaseService;

/**
 * The Class OnlineSysUserHttpSessionEvent.
 *
 * @author l.xue.nong
 */
public class OnlineSysUserHttpSessionEvent implements HttpSessionEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4687652941719363561L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(javax.servlet.http.HttpSessionEvent se) {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(javax.servlet.http.HttpSessionEvent se) {
		HttpSession httpSession = se.getSession();
		if (httpSession != null) {
			long time = httpSession.getLastAccessedTime() + httpSession.getMaxInactiveInterval() * 1000L;
			long sysNowTime = System.currentTimeMillis();
			boolean b = sysNowTime >= time;
			Boolean a = (Boolean) httpSession.getAttribute(FailUserContextFilter.FAIL_USER_T);
			if (a == null) {
				a = false;
			}
			if (b || a) {
				OnlineSysUserEntity onlineSysUserEntity = UserService.remove(httpSession.getId());
				if (onlineSysUserEntity == null) {
					onlineSysUserEntity = UserService.removeFail(httpSession.getId());
				}
				if (onlineSysUserEntity == null)
					return;
				//log
				LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
						.getInstance(LogService.class);
				SysLogEntity logEntity = new SysLogEntity();
				try {
					logEntity.setSysUserEntity(SpringContextHolder.getBeanFactory().getBeansOfType(BaseService.class)
							.values().iterator().next()
							.get(SysUserEntity.class, (Long) ReflectionUtils.getFieldValue(onlineSysUserEntity, "id")));
				} catch (Exception e) {
				}
				if (RequestContext.get() != null)
					logEntity.setRequestIp(RequestContext.get().request() != null ? IpUtils
							.getClientIpAddr(RequestContext.get().request()) : "");
				logEntity.setLogContext(("用户:" + onlineSysUserEntity.getLoginName() + ",时间:"
						+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",exit系统!").getBytes());
				logService.addLog(logEntity);
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionActivationListener#sessionWillPassivate(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionWillPassivate(javax.servlet.http.HttpSessionEvent se) {
		HttpSession httpSession = se.getSession();
		if (httpSession != null) {
			OnlineSysUserEntity onlineSysUserEntity = UserService.remove(httpSession.getId());
			if (onlineSysUserEntity == null) {
				onlineSysUserEntity = UserService.removeFail(httpSession.getId());
			}
			if (onlineSysUserEntity == null)
				return;
			//log
			LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
					.getInstance(LogService.class);
			SysLogEntity logEntity = new SysLogEntity();
			try {
				logEntity.setSysUserEntity(SpringContextHolder.getBeanFactory().getBeansOfType(BaseService.class)
						.values().iterator().next()
						.get(SysUserEntity.class, (Long) ReflectionUtils.getFieldValue(onlineSysUserEntity, "id")));
			} catch (Exception e) {
			}
			if (RequestContext.get() != null)
				logEntity.setRequestIp(RequestContext.get().request() != null ? IpUtils.getClientIpAddr(RequestContext
						.get().request()) : "");
			logEntity.setLogContext(("用户:" + onlineSysUserEntity.getLoginName() + ",时间:"
					+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",exit系统!").getBytes());
			logService.addLog(logEntity);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionActivationListener#sessionDidActivate(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDidActivate(javax.servlet.http.HttpSessionEvent se) {
	}

	public static void main(String[] args) {
		long a = DateUtils.getCurrentDateTime().getTime();
		long b = System.currentTimeMillis();
		System.out.println(a + "," + b);

	}

}

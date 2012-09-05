/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysUserAuthenticationListener.java 2012-8-14 14:04:55 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.utils.BeanUtils;
import cn.com.rebirth.commons.utils.DateUtils;
import cn.com.rebirth.commons.utils.IpUtils;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.web.SessionListener;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.knowledge.web.admin.service.SystemService;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

/**
 * The listener interface for receiving sysUserAuthentication events.
 * The class that is interested in processing a sysUserAuthentication
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSysUserAuthenticationListener<code> method. When
 * the sysUserAuthentication event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SysUserAuthenticationEvent
 */
public class SysUserAuthenticationListener implements AuthenticationListener {

	/** The system service. */
	@Autowired
	protected SystemService systemService;

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationListener#onSuccess(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo)
	 */
	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		SysUserEntity sysUserEntity = systemService.findUniqueBy(SysUserEntity.class, "loginName",
				usernamePasswordToken.getUsername());
		OnlineSysUserEntity onlineSysUserEntity = BeanUtils.copyProperties(OnlineSysUserEntity.class, sysUserEntity);
		onlineSysUserEntity.setSysUserEntity(sysUserEntity);
		onlineSysUserEntity.setSessionId(RequestContext.get().session().getId());
		onlineSysUserEntity.setCurrentRequestUrl(RequestContext.get().request().getRequestURI());
		onlineSysUserEntity.setSessionInformations(SessionListener.getSessionInformationsBySessionId(RequestContext
				.get().session().getId()));
		onlineSysUserEntity.setClinetIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
		UserService.put(RequestContext.get().session().getId(), onlineSysUserEntity);
		//log
		LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
				.getInstance(LogService.class);
		SysLogEntity logEntity = new SysLogEntity();
		logEntity.setSysUserEntity(sysUserEntity);
		logEntity.setRequestIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
		logEntity.setLogContext(("用户:" + sysUserEntity.getLoginName() + ",时间:"
				+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",登入系统!").getBytes());
		logService.addLog(logEntity);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationListener#onFailure(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationException)
	 */
	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		//log exception
		LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
				.getInstance(LogService.class);
		SysLogEntity logEntity = new SysLogEntity();
		logEntity.setRequestIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
		logEntity.setLogContext(("用户:" + usernamePasswordToken.getUsername() + ",时间:"
				+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",密码:"
				+ new String(usernamePasswordToken.getPassword()) + ",登入失败:" + ae.getMessage()).getBytes());
		logService.addLog(logEntity);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationListener#onLogout(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	public void onLogout(PrincipalCollection principals) {
		SysUserEntity sysUserEntity = systemService.getCurrentUser();
		UserService.remove(SecurityUtils.getSubject().getSession(false).getId().toString());
		//log
		LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class).getInjector()
				.getInstance(LogService.class);
		SysLogEntity logEntity = new SysLogEntity();
		logEntity.setSysUserEntity(sysUserEntity);
		logEntity.setRequestIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
		logEntity.setLogContext(("用户:" + sysUserEntity.getLoginName() + ",时间:"
				+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",exit系统!").getBytes());
		logService.addLog(logEntity);
	}

}

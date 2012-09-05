/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ExamJdbcRealm.java 2012-7-21 13:00:02 l.xue.nong$$
 */

package cn.com.rebirth.knowledge.web.admin.realm;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.utils.BeanUtils;
import cn.com.rebirth.commons.utils.DateUtils;
import cn.com.rebirth.commons.utils.IpUtils;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.web.SessionListener;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysAuthorityEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysGroupEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysRoleEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.knowledge.web.admin.service.SystemService;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

/**
 * The Class ExamJdbcRealm.
 *
 * @author l.xue.nong
 */
public class ExamJdbcRealm extends AuthorizingRealm {

	/** The system service. */
	@Resource
	private SystemService systemService;

	/**
	 * Instantiates a new exam jdbc realm.
	 */
	public ExamJdbcRealm() {
		setName("ExamJdbcRealm"); //This name must match the name in the User class's getPrincipals() method
		setCredentialsMatcher(new HashedCredentialsMatcher("SHA-256"));
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#getAuthorizationCacheKey(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		String loginName = (String) principals.fromRealm(getName()).iterator().next();
		HttpSession httpSession = RequestContext.get().session();
		if (httpSession != null) {
			String sessionId = httpSession.getId();
			if (sessionId != null) {
				OnlineSysUserEntity onlineSysUserEntity = UserService.get(sessionId);
				if (onlineSysUserEntity == null) {
					SysUserEntity user = systemService.findUniqueBy(SysUserEntity.class, "loginName", loginName);
					if (user != null) {
						onlineSysUserEntity = new OnlineSysUserEntity();
						BeanUtils.copyProperties(onlineSysUserEntity, user);
						onlineSysUserEntity.setSysUserEntity(user);
						onlineSysUserEntity.setSessionId(sessionId);
						onlineSysUserEntity.setCurrentRequestUrl(RequestContext.get().request().getRequestURI());
						onlineSysUserEntity.setClinetIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
						onlineSysUserEntity.setSessionInformations(SessionListener
								.getSessionInformationsBySessionId(sessionId));
						UserService.put(sessionId, onlineSysUserEntity);
						//log
						LogService logService = RebirthContainer.getInstance().get(InjectInitialization.class)
								.getInjector().getInstance(LogService.class);
						SysLogEntity logEntity = new SysLogEntity();
						logEntity.setSysUserEntity(user);
						logEntity.setRequestIp(IpUtils.getClientIpAddr(RequestContext.get().request()));
						logEntity.setLogContext(("用户:" + user.getLoginName() + ",时间:"
								+ DateUtils.formatDate(DateUtils.getCurrentDateTime(), null) + ",登入系统!").getBytes());
						logService.addLog(logEntity);
					}
				}
			}
		}
		return super.getAuthorizationCacheKey(principals);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = (String) principals.fromRealm(getName()).iterator().next();
		SysUserEntity user = systemService.findUniqueBy(SysUserEntity.class, "loginName", loginName);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			//add user group role
			for (SysGroupEntity groupEntity : user.getGroupEntities()) {
				addRole(groupEntity.getRoleEntities(), info);
			}
			//add user role
			addRole(user.getRoleList(), info);
			return info;
		} else {
			return null;
		}
	}

	/**
	 * Adds the role.
	 *
	 * @param sysRoleEntities the sys role entities
	 * @param info the info
	 */
	protected void addRole(List<SysRoleEntity> sysRoleEntities, SimpleAuthorizationInfo info) {
		for (SysRoleEntity sysRole : sysRoleEntities) {
			info.addRole(sysRole.getName());
			for (SysAuthorityEntity sysAuthority : sysRole.getAuthorityList()) {
				info.addStringPermission(sysAuthority.getName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		SysUserEntity sysUser = systemService.findUniqueBy(SysUserEntity.class, "loginName", token.getUsername());
		if (sysUser != null) {
			if (sysUser.isDisabled())
				throw new DisabledAccountException();
			if (sysUser.isLocked())
				throw new LockedAccountException();
			return new SimpleAuthenticationInfo(sysUser.getLoginName(), sysUser.getPassWord(), getName());
		} else {
			return null;
		}
	}

	/**
	 * Clear cached authorization info.
	 *
	 * @param principal the principal
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * Clear all cached authorization info.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}

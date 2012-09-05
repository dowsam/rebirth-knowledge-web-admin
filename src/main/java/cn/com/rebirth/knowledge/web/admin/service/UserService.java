/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin UserService.java 2012-8-14 14:43:36 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.OnlineUserService;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.persistence.service.BaseService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class UserService.
 *
 * @author l.xue.nong
 */
@Service
@Transactional
public class UserService extends BaseService implements OnlineUserService {

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** The user context. */
	private static volatile LinkedHashMap<String, OnlineSysUserEntity> userContext = Maps.newLinkedHashMap();

	/** The fail user context. */
	private static volatile LinkedHashMap<String, OnlineSysUserEntity> failUserContext = Maps.newLinkedHashMap();

	/**
	 * Put.
	 *
	 * @param sessionId the session id
	 * @param sysUserEntity the sys user entity
	 */
	public static synchronized void put(String sessionId, OnlineSysUserEntity sysUserEntity) {
		userContext.put(sessionId, sysUserEntity);
	}

	/**
	 * Removes the.
	 *
	 * @param sessionId the session id
	 * @return the sys user entity
	 */
	public static synchronized OnlineSysUserEntity remove(String sessionId) {
		return userContext.remove(sessionId);
	}

	/**
	 * Gets the.
	 *
	 * @param sessionId the session id
	 * @return the online sys user entity
	 */
	public static synchronized OnlineSysUserEntity get(String sessionId) {
		return userContext.get(sessionId);
	}

	//fail 
	/**
	 * Put fail.
	 *
	 * @param sessionId the session id
	 */
	public static synchronized void putFail(String sessionId) {
		OnlineSysUserEntity onlineSysUserEntity = remove(sessionId);
		onlineSysUserEntity.setFail(true);
		failUserContext.put(sessionId, onlineSysUserEntity);
	}

	//get fail session
	/**
	 * Gets the fail.
	 *
	 * @param sessionId the session id
	 * @return the fail
	 */
	public static synchronized OnlineSysUserEntity getFail(String sessionId) {
		return failUserContext.get(sessionId);
	}

	//remove fail session
	/**
	 * Removes the fail.
	 *
	 * @param sessionId the session id
	 * @return the online sys user entity
	 */
	public static synchronized OnlineSysUserEntity removeFail(String sessionId) {
		return failUserContext.remove(sessionId);
	}

	/**
	 * Removes the eq user.
	 *
	 * @param sysUserEqCallback the sys user eq callback
	 */
	public static synchronized void removeEqUser(SysUserEqCallback sysUserEqCallback) {
		List<String> sessionIds = Lists.newArrayList();
		for (Map.Entry<String, OnlineSysUserEntity> entry : userContext.entrySet()) {
			if (sysUserEqCallback.eq(entry.getKey(), entry.getValue())) {
				sessionIds.add(entry.getKey());
			}
		}
		for (String sessionId : sessionIds) {
			putFail(sessionId);
		}
	}

	/**
	 * The Interface SysUserEqCallback.
	 *
	 * @author l.xue.nong
	 */
	public static interface SysUserEqCallback {

		/**
		 * Eq.
		 *
		 * @param sessionId the session id
		 * @param onlineSysUserEntity the online sys user entity
		 * @return true, if successful
		 */
		boolean eq(String sessionId, OnlineSysUserEntity onlineSysUserEntity);
	}

	/**
	 * Page request.
	 *
	 * @return the page
	 */
	public static synchronized Collection<OnlineSysUserEntity> totalCount() {
		return userContext.values();
	}

	/**
	 * All.
	 *
	 * @return the collection
	 */
	public static synchronized Collection<OnlineSysUserEntity> all() {
		List<OnlineSysUserEntity> c = Lists.newArrayList();
		c.addAll(userContext.values());
		c.addAll(failUserContext.values());
		return c;
	}

	/**
	 * Current user.
	 *
	 * @return the sys user entity
	 */
	public static SysUserEntity currentUser() {
		RequestContext requestContext = RequestContext.get();
		if (requestContext != null) {
			HttpSession httpSession = requestContext.session();
			if (httpSession != null) {
				OnlineSysUserEntity entity = userContext.get(httpSession.getId());
				if (entity != null) {
					return entity.getSysUserEntity();
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.Shell#passive(java.io.Serializable)
	 */
	@Override
	public void passive(OnlineSysUserEntity t) {
		put(t.getSessionId(), t);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.Shell#initiative()
	 */
	@Override
	public OnlineSysUserEntity initiative() {
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.OnlineUserService#t(java.lang.String)
	 */
	@Override
	public void t(String sessionId) {
		remove(sessionId);
	}
}

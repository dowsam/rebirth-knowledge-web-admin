/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SystemService.java 2012-7-19 15:56:59 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.persistence.service.BaseService;

/**
 * The Class SystemService.
 *
 * @author l.xue.nong
 */
@Service
@Transactional
public class SystemService extends BaseService {

	/**
	 * Gets the url resource with authorities.
	 *
	 * @return the url resource with authorities
	 */
	@Transactional(readOnly = true)
	public SysResourceEntity getUrlResourceWithAuthorities(String moduleName) {
		return getBaseDao().findUnique(SysResourceEntity.QUERY_BY_RESOURCETYPE_WITH_NO_NULL_AUTHORITY, moduleName);
	}

	/**
	 * Gets the current user.
	 *
	 * @return the current user
	 */
	@Transactional(readOnly = true)
	public SysUserEntity getCurrentUser() {
		final String loginName = (String) SecurityUtils.getSubject().getPrincipal();
		if (loginName != null) {
			return findUniqueBy(SysUserEntity.class, "loginName", loginName);
		} else {
			return null;
		}
	}

	/**
	 * Load menu.
	 *
	 * @return the list
	 */
	@Transactional(readOnly = true)
	public List<SysResourceEntity> loadMenu() {
		return getBaseDao().find(SysResourceEntity.QUERY_BY_RESOURCEPARENT_WHERE_NULL, SysResourceEntity.MENU_TYPE);
	}
}

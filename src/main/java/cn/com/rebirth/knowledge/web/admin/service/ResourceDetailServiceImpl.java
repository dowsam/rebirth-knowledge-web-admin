/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ResourceDetailServiceImpl.java 2012-7-19 15:58:47 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.VersionFactory;
import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.core.OpenEntityManagerInViewFilter;
import cn.com.rebirth.core.security.ResourceDetailsService;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;

/**
 * The Class ResourceDetailServiceImpl.
 *
 * @author l.xue.nong
 */
public class ResourceDetailServiceImpl implements ResourceDetailsService {

	/** The service. */
	@Autowired
	private SystemService service;

	/* (non-Javadoc)
	 * @see com.chinacti.security.ResourceDetailsService#getRequestMap()
	 */
	/**
	 * Gets the request map.
	 *
	 * @return the request map
	 * @throws Exception the exception
	 */
	@Override
	public LinkedHashMap<String, String> getRequestMap() throws RebirthException {
		LinkedHashMap<String, String> requestMap = new LinkedHashMap<String, String>();
		OpenEntityManagerInViewFilter entityManagerInViewFilter = new OpenEntityManagerInViewFilter();
		try {
			entityManagerInViewFilter.beginFilter();
			SysResourceEntity moduleResource = service.getUrlResourceWithAuthorities(RebirthContainer.getInstance()
					.get(VersionFactory.class).currentVersion().getModuleName());
			bulidUrl(moduleResource.getChildResource(), requestMap);
		} finally {
			entityManagerInViewFilter.endFilter();
		}
		return requestMap;
	}

	private void bulidUrl(List<SysResourceEntity> childResource, LinkedHashMap<String, String> requestMap) {
		for (SysResourceEntity resource : childResource) {
			String authNames = resource.getAuthNames();
			if (StringUtils.isNotBlank(authNames)) {
				requestMap.put(resource.getValue(), "authc,perms[" + authNames + "]");
			}
			List<SysResourceEntity> resourceEntities = resource.getChildResource();
			if (resourceEntities != null && !resourceEntities.isEmpty()) {
				bulidUrl(resourceEntities, requestMap);
			}
		}
	}

}

/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin DhtmlxRenderVariableInterceptor.java 2012-8-3 8:18:48 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.dhtmlx.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.rebirth.commons.utils.ShortUrlGenerator;
import cn.com.rebirth.core.web.controller.RenderVariableInterceptor;
import cn.com.rebirth.knowledge.web.admin.service.SystemService;

/**
 * The Class DhtmlxRenderVariableInterceptor.
 *
 * @author l.xue.nong
 */
public class DhtmlxRenderVariableInterceptor extends RenderVariableInterceptor {
	@Autowired
	private SystemService systemService;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.RenderVariableInterceptor#perRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected Map<String, Object> perRequest(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = super.perRequest(request, response);
		String requestUrl = request.getRequestURL().toString();
		String url = getServerPath(request);
		requestUrl = requestUrl.substring(url.length(), requestUrl.length());
		param.put("dhtmlUrl", ShortUrlGenerator.shortUrl(requestUrl));
		param.put("currentUser", systemService.getCurrentUser());
		return param;
	}
}

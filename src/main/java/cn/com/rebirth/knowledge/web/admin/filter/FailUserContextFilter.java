/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin FailUserContextFilter.java 2012-8-15 9:57:40 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.com.rebirth.core.web.SessionListener;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

/**
 * The Class FailUserContextFilter.
 *
 * @author l.xue.nong
 */
public class FailUserContextFilter extends OncePerRequestFilter implements Filter {

	/** The Constant USER_CONTEXT_FAIL_URL. */
	public static final String USER_CONTEXT_FAIL_URL = "user.context.fail.url";
	public static final String FAIL_USER_T = "fail_user_t";
	/** The fail url. */
	protected String failUrl;

	/* (non-Javadoc)
	 * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		this.failUrl = getFilterConfig().getInitParameter(USER_CONTEXT_FAIL_URL);
		if (StringUtils.isBlank(this.failUrl)) {
			this.failUrl = "/login";
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
			String sessionId = httpSession.getId();
			if (sessionId != null) {
				OnlineSysUserEntity onlineSysUserEntity = UserService.getFail(sessionId);
				if (onlineSysUserEntity != null) {
					if (onlineSysUserEntity.isFail()) {
						httpSession.setAttribute(FAIL_USER_T, true);
						//失效sessionId
						SessionListener.invalidateSession(onlineSysUserEntity.getSessionId());
						//跳转
						redirectFailureUrl(request, response, onlineSysUserEntity);
						return;
					}
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Redirect failure url.
	 *
	 * @param request the request
	 * @param response the response
	 * @param onlineSysUserEntity the online sys user entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void redirectFailureUrl(final HttpServletRequest request, final HttpServletResponse response,
			OnlineSysUserEntity onlineSysUserEntity) throws IOException {
		String url = failUrl;
		if (url.indexOf("?") == -1) {
			url = url + "?error=" + onlineSysUserEntity.getErrorCode();
		} else {
			url = url + "&error=" + onlineSysUserEntity.getErrorCode();
		}
		response.sendRedirect(request.getContextPath() + url);
	}
}

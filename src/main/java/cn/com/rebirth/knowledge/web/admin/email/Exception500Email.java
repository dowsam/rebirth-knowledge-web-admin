/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin Exception500Email.java 2012-8-16 17:22:22 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.email;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.utils.DateUtils;
import cn.com.rebirth.commons.utils.IpUtils;
import cn.com.rebirth.commons.utils.SpringContextHolder;
import cn.com.rebirth.core.email.AsyncJavaMailSender;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

import com.google.common.collect.Maps;

/**
 * The Class Exception500Email.
 *
 * @author l.xue.nong
 */
public abstract class Exception500Email {

	/** The log. */
	protected static Logger log = LoggerFactory.getLogger(Exception500Email.class);

	/**
	 * Send.
	 *
	 * @param req the req
	 */
	public static void send(HttpServletRequest req) {
		reportError(req, null);
	}

	/**
	 * 报告错误信息.
	 *
	 * @param req the req
	 * @param excp the excp
	 */
	public static void reportError(HttpServletRequest req, Throwable excp) {
		boolean is_localhost = (req != null) ? "127.0.0.1".equals(IpUtils.getClientIpAddr(req))
				|| "0:0:0:0:0:0:0:1".equals(IpUtils.getClientIpAddr(req)) : false;
		Throwable t = excp;
		if (t == null)
			t = _GetException(req);
		if (t == null)
			return;
		log.error("System Exception", t);
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		if (!is_localhost && settings != null) {
			String content = null;
			//发送电子邮件通知
			try {
				String email = settings.get("system.administrator.email.add", "l.xue.nong@gmail.com");
				content = getErrorHtml(req, t);
				//发送邮件到指定邮箱
				AsyncJavaMailSender mimeMailService = SpringContextHolder.getBean("asyncJavaMailSender");
				if (mimeMailService != null) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("content", content);
					map.put("requestContext", RequestContext.get());
					map.put("servletContext", RequestContext.get().context());
					mimeMailService.send(settings.get("mail.username", "xuenong520"), StringUtils.split(email, ","),
							"应用程序出错", "errormailTemplate.ftl", map);
				}
			} catch (Exception e) {
				log.error("Failed to send error report.", e);
			}
			//并写入系统日志中
			LogService logService = InjectInitialization.injector().getInstance(LogService.class);
			SysLogEntity sysLogEntity = new SysLogEntity();
			sysLogEntity.setSysUserEntity(UserService.currentUser());
			sysLogEntity.setRequestIp(RequestContext.get().requestIp());
			if (content != null) {
				sysLogEntity.setLogContext(content.getBytes());
			}
			logService.addLog(sysLogEntity);
		}
	}

	/**
	 * 格式化错误信息.
	 *
	 * @param req the req
	 * @param t 错误信息
	 * @return the error html
	 * <h2>Request Headers</h2>
	 */
	@SuppressWarnings("rawtypes")
	public static String getErrorHtml(HttpServletRequest req, Throwable t) {
		StringBuilder html = new StringBuilder();
		if (req != null) {
			html.append("<h2>Request Headers</h2><table>");
			html.append("<tr><th>Request URL</th><td>");
			html.append(req.getRequestURL().toString());
			if (req.getQueryString() != null) {
				html.append('?');
				html.append(req.getQueryString());
			}
			html.append("</td></tr>");
			html.append("<tr><th>Remote Addr</th><td>");
			html.append(IpUtils.getClientIpAddr(req));
			html.append("</td></tr>");
			html.append("<tr><th>Request Method</th><td>");
			html.append(req.getMethod());
			html.append("</td></tr>");
			html.append("<tr><th>CharacterEncoding</th><td>");
			html.append(req.getCharacterEncoding());
			html.append("</td></tr>");
			html.append("<tr><th>Request Locale</th><td>");
			html.append(req.getLocale());
			html.append("</td></tr>");
			html.append("<tr><th>Content Type</th><td>");
			html.append(req.getContentType());
			html.append("</td></tr>");
			Enumeration headers = req.getHeaderNames();
			while (headers.hasMoreElements()) {
				String key = (String) headers.nextElement();
				html.append("<tr><th>");
				html.append(key);
				html.append("</th><td>");
				html.append(req.getHeader(key));
				html.append("</td></tr>");
			}
			html.append("</table><h2>Request Parameters</h2><table>");
			Enumeration params = req.getParameterNames();
			while (params.hasMoreElements()) {
				String key = (String) params.nextElement();
				html.append("<tr><th>");
				html.append(key);
				html.append("</th><td>");
				html.append(req.getParameter(key));
				html.append("</td></tr>");
			}
			html.append("</table>");
		}
		html.append("<h2>");
		html.append(t.getClass().getName());
		html.append('(');
		html.append(DateUtils.formatDate(DateUtils.getCurrentDateTime(), null));
		html.append(")</h2><pre>");
		try {
			html.append(_Exception(t));
		} catch (IOException ex) {
		}
		html.append("</pre>");

		html.append("<h2>System Properties</h2><table>");
		Set props = System.getProperties().keySet();
		for (Object prop : props) {
			html.append("<tr><th>");
			html.append(prop);
			html.append("</th><td>");
			html.append(System.getProperty((String) prop));
			html.append("</td></tr>");
		}
		html.append("</table>");
		return html.toString();
	}

	/**
	 * 将当前上下文发生的异常转为字符串.
	 *
	 * @param req the req
	 * @return the throwable
	 */
	private static Throwable _GetException(HttpServletRequest req) {
		if (req == null)
			return null;
		Throwable t = (Throwable) req.getAttribute("javax.servlet.jsp.jspException");
		if (t == null) {
			//Tomcat的错误处理方式
			t = (Throwable) req.getAttribute("javax.servlet.error.exception");
		}
		return t;
	}

	/**
	 * 将异常信息转化成字符串.
	 *
	 * @param t the t
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String _Exception(Throwable t) throws IOException {
		if (t == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			t.printStackTrace(new PrintStream(baos));
		} finally {
			baos.close();
		}
		return baos.toString();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		//URL url=new URL("/templates/");
		UrlResource resource = new UrlResource("/templates/");
		File file = resource.getFile();
		//		URI uri=new URI(StringUtils.replace(url.toString(), " ", "%20"));
		//		File file=new File(uri.getSchemeSpecificPart());
		System.out.println(file.getName());
	}

}

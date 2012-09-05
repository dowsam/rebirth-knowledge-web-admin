/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin JavaMailSenderExt.java 2012-8-16 16:55:45 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.InjectInitialization;

/**
 * The Class JavaMailSenderExt.
 *
 * @author l.xue.nong
 */
public class JavaMailSenderExt extends JavaMailSenderImpl implements JavaMailSender {

	/** The settings. */
	private final Settings settings;

	/**
	 * Instantiates a new java mail sender ext.
	 */
	public JavaMailSenderExt() {
		this(InjectInitialization.injector().getInstance(Settings.class));
	}

	/**
	 * Instantiates a new java mail sender ext.
	 *
	 * @param settings the settings
	 */
	public JavaMailSenderExt(Settings settings) {
		super();
		this.settings = settings;
		setHost(settings.get("mail.host", "smtp.163.com"));
		setUsername(settings.get("mail.username", "xuenong520@163.com"));
		setPassword(settings.get("mail.password", "xuenong"));
		setPort(settings.getAsInt("mail.port", 25));
	}

	public Settings getSettings() {
		return settings;
	}

}

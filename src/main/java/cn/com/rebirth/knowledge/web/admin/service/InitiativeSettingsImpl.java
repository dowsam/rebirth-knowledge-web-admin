/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin InitiativeSettingsImpl.java 2012-8-28 11:27:48 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.knowledge.commons.AbstractSettingsShare;
import cn.com.rebirth.knowledge.commons.InitiativeSettings;

/**
 * The Class InitiativeSettingsImpl.
 *
 * @author l.xue.nong
 */
public class InitiativeSettingsImpl extends AbstractSettingsShare implements InitiativeSettings {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.AbstractSettingsShare#initiative()
	 */
	@Override
	public Settings initiative() {
		return InjectInitialization.injector().getInstance(Settings.class);
	}

}

/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin RebirthKnowledgeWebAdminVersion.java 2012-7-19 13:24:59 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin;

import cn.com.rebirth.commons.AbstractVersion;
import cn.com.rebirth.commons.Version;

/**
 * The Class RebirthKnowledgeWebAdminVersion.
 *
 * @author l.xue.nong
 */
public class RebirthKnowledgeWebAdminVersion extends AbstractVersion implements Version {

	private static final long serialVersionUID = 4649220107908427966L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "Rebirth-Knowledge-Web-Admin";
	}

}

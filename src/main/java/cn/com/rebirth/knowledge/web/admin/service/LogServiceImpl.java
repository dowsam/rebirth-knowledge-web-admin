/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin LogServiceImpl.java 2012-8-14 10:11:04 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.service;

import org.springframework.stereotype.Component;

import cn.com.rebirth.commons.utils.SpringContextHolder;
import cn.com.rebirth.knowledge.commons.entity.system.AbstractLogBaseEntity;
import cn.com.rebirth.knowledge.commons.service.LogService;
import cn.com.rebirth.persistence.service.BaseService;

/**
 * The Class LogServiceImpl.
 *
 * @author l.xue.nong
 */
@Component
public class LogServiceImpl implements LogService {
	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.commons.service.LogService#addLog(cn.com.rebirth.knowledge.commons.entity.system.AbstractLogBaseEntity)
	 */
	@Override
	public void addLog(AbstractLogBaseEntity abstractLogBaseEntity) {
		SpringContextHolder.getBeanFactory().getBeansOfType(BaseService.class).values().iterator().next()
				.save(abstractLogBaseEntity);
	}

}

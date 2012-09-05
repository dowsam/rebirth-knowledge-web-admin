/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysLogEntityController.java 2012-8-14 13:44:48 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxBaseType;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.entity.system.SysLogEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;

/**
 * The Class SysLogEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysLog")
@Resource(names = "系统日志")
public class SysLogEntityController extends AbstractDhtmlxController<SysLogEntity, Long> {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#columns(org.springframework.ui.ModelMap)
	 */
	@Override
	protected List<Column> columns(Model model) {
		List<Column> columns = super.columns(model);
		for (Column column : columns) {
			if (column.getType().equalsIgnoreCase(DhtmlxBaseType.SUB_ROW.name())) {
				continue;
			}
			column.setType(DhtmlxBaseType.RO.name().toLowerCase());
		}
		return columns;
	}

}

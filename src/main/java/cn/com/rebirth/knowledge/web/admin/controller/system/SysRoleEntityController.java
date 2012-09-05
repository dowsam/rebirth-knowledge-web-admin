/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysRoleEntityController.java 2012-8-11 15:03:28 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.entity.system.SysAuthorityEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity.AuthorityListColumnDataSets;
import cn.com.rebirth.knowledge.commons.entity.system.SysRoleEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;

/**
 * The Class SysRoleEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysRole")
@Resource(names = "角色管理")
public class SysRoleEntityController extends AbstractDhtmlxController<SysRoleEntity, Long> {

	public SysRoleEntityController() {
		super();
	}

	public SysRoleEntityController(Class<SysRoleEntity> entityClass) {
		super(entityClass);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#constructorColumnData(cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn)
	 */
	@Override
	protected ColumnDataSets constructorColumnData(DhtmlColumn dhtmlColumn) {
		Class<? extends ColumnDataSets> clas = dhtmlColumn.columnDataSets();
		if (clas != null && AuthorityListColumnDataSets.class.isAssignableFrom(clas)) {
			return new AuthorityListColumnDataSets(this.baseService.getAll(SysAuthorityEntity.class));
		}
		return super.constructorColumnData(dhtmlColumn);
	}

}

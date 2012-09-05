/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: SysUserController.java 2011-6-26 10:47:04 l.xue.nong$$
*/
package cn.com.rebirth.knowledge.web.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.entity.system.SysRoleEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity.RoleListDataSets;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;
import cn.com.rebirth.knowledge.web.admin.realm.ExamJdbcRealm;

/**
 * The Class SysUserController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysUser")
@Resource(names = "用户管理")
public class SysUserEntityController extends AbstractDhtmlxController<SysUserEntity, Long> {
	
	/** The exam jdbc realm. */
	@Autowired
	private ExamJdbcRealm examJdbcRealm;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#constructorColumnData(cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn)
	 */
	@Override
	protected ColumnDataSets constructorColumnData(DhtmlColumn dhtmlColumn) {
		Class<? extends ColumnDataSets> clas = dhtmlColumn.columnDataSets();
		if (clas != null && RoleListDataSets.class.isAssignableFrom(clas)) {
			return new RoleListDataSets(this.baseService.getAll(SysRoleEntity.class));
		}
		return super.constructorColumnData(dhtmlColumn);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterAddRow(SysUserEntity t) {
		examJdbcRealm.clearCachedAuthorizationInfo(t.getLoginName());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterUpdateRow(SysUserEntity t) {
		examJdbcRealm.clearCachedAuthorizationInfo(t.getLoginName());
	}

}

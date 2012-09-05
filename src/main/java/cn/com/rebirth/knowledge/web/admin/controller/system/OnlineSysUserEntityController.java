/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin OnlineSysUserEntityController.java 2012-8-14 17:07:23 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.mapper.BeanMapper;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxBaseType;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.entity.AbstractBaseEntity;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

/**
 * The Class OnlineSysUserEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/onlineSysUser")
@Resource(names = "在线用户")
public class OnlineSysUserEntityController extends AbstractDhtmlxController<OnlineSysUserEntity, Long> {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#columns(org.springframework.ui.ModelMap)
	 */
	@Override
	protected List<Column> columns(Model model) {
		List<Column> columns = super.columns(model);
		for (Column column : columns) {
			if (!column.getType().equalsIgnoreCase(DhtmlxBaseType.ACHECK.name()))
				column.setType(DhtmlxBaseType.RO.name().toLowerCase());
		}
		return columns;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		final Collection<OnlineSysUserEntity> data = UserService.totalCount();
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

			@Override
			public void business(Map<String, Object> returnMsg) throws RebirthException {
				DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg, data);
			}
		});
		return new OutputType(ServletUtils.JSON_TYPE, json);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#updateRow(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public AbstractBaseEntity updateRow(Model model, Long id, OnlineSysUserEntity entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		String sessionId = entity.getSessionId();
		if (entity.isFail()) {
			UserService.putFail(sessionId);
		}
		OnlineSysUserEntity onlineSysUserEntity = UserService.getFail(sessionId);
		onlineSysUserEntity.setErrorCode(4);
		UserService.put(sessionId, onlineSysUserEntity);
		return onlineSysUserEntity;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#toModel(java.io.Serializable)
	 */
	@Override
	protected OnlineSysUserEntity toModel(Long id) {
		SysUserEntity sysUserEntity = baseService.get(SysUserEntity.class, id);
		if (sysUserEntity == null)
			return new OnlineSysUserEntity();
		return BeanMapper.map(sysUserEntity, OnlineSysUserEntity.class);
	}

}

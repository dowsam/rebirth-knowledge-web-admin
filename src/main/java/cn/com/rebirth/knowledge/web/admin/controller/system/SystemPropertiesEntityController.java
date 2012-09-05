/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SystemPropertiesEntityController.java 2012-8-16 13:29:11 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.service.middleware.annotation.Rmi;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.commons.utils.NonceUtils;
import cn.com.rebirth.commons.utils.ResolverUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.PassiveSettings;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.entity.AbstractBaseEntity;
import cn.com.rebirth.knowledge.commons.entity.SystemPropertiesEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;
import cn.com.rebirth.service.middleware.client.ConsumerProxyFactory;

import com.google.common.collect.Lists;

/**
 * The Class SystemPropertiesEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/systemProperties")
@Resource(names = "系统属性")
public class SystemPropertiesEntityController extends AbstractDhtmlxController<SystemPropertiesEntity, Long> {

	/** The find callback. */
	private static ClassResolverUtils.FindCallback<Class<? extends PassiveSettings>> findCallback = new ClassResolverUtils.FindCallback<Class<? extends PassiveSettings>>() {

		@Override
		public void findType(ResolverUtils<Class<? extends PassiveSettings>> resolverUtils) {
			resolverUtils.find(new ResolverUtils.Matche() {

				@Override
				public boolean matches(Class<?> type) {
					return type != null && PassiveSettings.class.isAssignableFrom(type) && type.isInterface()
							&& type.isAnnotationPresent(Rmi.class);
				}
			}, StringUtils.EMPTY);
		}

		@Override
		public boolean needFor(int mod) {
			return Modifier.isInterface(mod);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<? extends PassiveSettings> constructor(Class<?> entityClass) throws RebirthException {
			return (Class<? extends PassiveSettings>) entityClass;
		}

	};

	/** The passive settings. */
	private static List<Class<? extends PassiveSettings>> passiveSettings = ClassResolverUtils.find(findCallback);

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		Map<String, String> map = settings.getAsMap();
		final List<SystemPropertiesEntity> entities = Lists.newArrayListWithCapacity(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			SystemPropertiesEntity entity = new SystemPropertiesEntity();
			entity.setAttributeName(entry.getKey());
			entity.setAttributeValue(entry.getValue());
			entity.setId(NonceUtils.randomLong());
			entities.add(entity);
		}
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

			@Override
			public void business(Map<String, Object> returnMsg) throws RebirthException {
				DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg, entities);
			}
		});
		return new OutputType(ServletUtils.JSON_TYPE, json);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#addRow(org.springframework.ui.ModelMap, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public AbstractBaseEntity addRow(Model model, SystemPropertiesEntity entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		beforeAddRow(entity);
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		settings.getAsMap().put(entity.getAttributeName(), entity.getAttributeValue());
		entity.setId(entity.getGr_id() == null ? entity.getTr_id() : entity.getGr_id());
		afterAddRow(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#deleteRow(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	public void deleteRow(Model model, Long id, SystemPropertiesEntity entity) throws RebirthException {
		beforeDeleteRow(id, entity);
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		settings.getAsMap().remove(entity.getAttributeName());
		afterDeleteRow(id, entity);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#updateRow(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public AbstractBaseEntity updateRow(Model model, Long id, SystemPropertiesEntity entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		beforeUpdateRow(entity);
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		settings.getAsMap().put(entity.getAttributeName(), entity.getAttributeValue());
		entity.setId(entity.getGr_id() == null ? entity.getTr_id() : entity.getGr_id());
		afterUpdateRow(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterAddRow(SystemPropertiesEntity t) {
		noticePassiveSettings();
	}

	/**
	 * Notice passive settings.
	 */
	private static void noticePassiveSettings() {
		Settings settings = InjectInitialization.injector().getInstance(Settings.class);
		for (Class<? extends PassiveSettings> p : passiveSettings) {
			ConsumerProxyFactory.getInstance().proxy(p).passive(settings);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterDeleteRow(java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterDeleteRow(Long id, SystemPropertiesEntity entity) {
		noticePassiveSettings();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterUpdateRow(SystemPropertiesEntity t) {
		noticePassiveSettings();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#toModel(java.io.Serializable)
	 */
	@Override
	protected SystemPropertiesEntity toModel(Long id) {
		return null;
	}

}

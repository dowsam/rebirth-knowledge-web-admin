/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysGroupEntityController.java 2012-8-20 13:45:03 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.CglibProxyUtils;
import cn.com.rebirth.commons.utils.ResponseTypeOutputUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.mapper.BeanMapper;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.CheckedTreeJsonInfo;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest;
import cn.com.rebirth.knowledge.commons.dhtmlx.TreeJson;
import cn.com.rebirth.knowledge.commons.dhtmlx.TreeJsonInfo;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.Dhtmlx;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxBaseType;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.entity.AbstractBaseEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysAuthorityEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysGroupEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity.AuthorityListColumnDataSets;
import cn.com.rebirth.knowledge.commons.entity.system.SysRoleEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;
import cn.com.rebirth.knowledge.web.admin.realm.ExamJdbcRealm;

import com.google.common.collect.Lists;

/**
 * The Class SysGroupEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysGroup")
@Resource(names = "组织管理")
public class SysGroupEntityController extends AbstractDhtmlxController<SysGroupEntity, Long> {

	/** The exam jdbc realm. */
	@Autowired
	private ExamJdbcRealm examJdbcRealm;

	/**
	 * The Class SysRoleEntityGridExt.
	 *
	 * @author l.xue.nong
	 */
	@Dhtmlx(createCheckAllColumn = true, isDataProcessor = false, autoHeigth = true, isPage = false, enableUndoRedo = false)
	public static class SysRoleEntityGridExt extends SysRoleEntity {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 8462295554255239540L;
	}

	/**
	 * The Class SysRoleEntityGridExtController.
	 *
	 * @author l.xue.nong
	 */
	private static class SysRoleEntityGridExtController extends AbstractDhtmlxController<SysRoleEntityGridExt, Long> {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#columns(org.springframework.ui.ModelMap)
		 */
		@Override
		protected List<Column> columns(Model model) {
			List<Column> columns = super.columns(model);
			for (Column column : columns) {
				if (DhtmlxBaseType.CLIST.name().equalsIgnoreCase(column.getType())) {
					column.setVisible(false);
					continue;
				}
				column.setType(DhtmlxBaseType.RO.name().toLowerCase());
			}
			return columns;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#requestMapping()
		 */
		@Override
		protected String requestMapping() {
			RequestMapping requestMapping = SysGroupEntityController.class.getAnnotation(RequestMapping.class);
			if (requestMapping != null) {
				return requestMapping.value()[0] + "/subGrid";
			}
			return "";
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		@Override
		public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
				throws RebirthException {
			final List<SysRoleEntity> list = baseService.getAll(SysRoleEntity.class);
			String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

				@Override
				public void business(Map<String, Object> returnMsg) throws RebirthException {
					DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg,
							BeanMapper.mapList(list, SysRoleEntityGridExt.class));
				}
			});
			return new OutputType(ServletUtils.JSON_TYPE, json);
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

	/** The sys role entity grid ext controller. */
	private SysRoleEntityGridExtController sysRoleEntityGridExtController = new SysRoleEntityGridExtController();

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		List<SysResourceEntity> data = Lists.newArrayList();
		if (null == id || id == 0) {
			data = baseService.find("from SysGroupEntity x where x.parentId is null order by x.id desc");
		} else {
			data = baseService.find("from SysGroupEntity x where x.parentId=? order by x.id desc", id);
		}
		String xml = DhtmlxJsonObjectUtils.getAbstractXmlTreeGirdEntities(id == null ? 0 : Long.valueOf(id), data);
		return new OutputType(ServletUtils.XML_TYPE, xml);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#init(org.springframework.ui.ModelMap)
	 */
	@Override
	public void init(final Model model) {
		sysRoleEntityGridExtController.setBaseService(this.baseService);
		sysRoleEntityGridExtController.init(model);
		super.init(CglibProxyUtils.getProxyInstance(model.getClass(), new MethodInterceptor() {

			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				String methodName = method.getName();
				if ("addAttribute".equalsIgnoreCase(methodName) && args != null && args.length == 2) {
					if (!"scriptObject".equals(args[0])) {
						args[0] = "sub_" + args[0];
					}
					if (!model.containsAttribute("sub")) {
						model.addAttribute("sub", true);
					}
				}
				return method.invoke(model, args);
			}
		}));
	}

	/**
	 * Sub grid load dhtmlx data.
	 *
	 * @param model the model
	 * @param id the id
	 * @param pageRequest the page request
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/subGrid/loadDhtmlxData")
	public void subGridLoadDhtmlxData(ModelMap model, @RequestParam(required = false) Long id,
			DhtmlxPageRequest pageRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysRoleEntityGridExtController.setBaseService(this.baseService);
		cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController.OutputType outputType = sysRoleEntityGridExtController
				.loadData(model, id, request, response);
		ResponseTypeOutputUtils.render(outputType.getMimeType(), outputType.getData(), response);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#beforeAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void beforeAddRow(SysGroupEntity entity) {
		Long parentId = entity.getGr_pid() == null ? entity.getTr_pid() : entity.getGr_pid();
		if (parentId != null) {
			entity.setParentSysGroup(baseService.get(SysGroupEntity.class, parentId));
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#beforeUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity, cn.com.rebirth.knowledge.commons.entity.AbstractBaseEntity)
	 */
	@Override
	protected void beforeUpdateRow(SysGroupEntity t) {
		Long parentId = t.getGr_pid() == null ? t.getTr_pid() : t.getGr_pid();
		if (parentId != null) {
			t.setParentSysGroup(baseService.get(SysGroupEntity.class, parentId));
		}
	}

	/**
	 * Sub tree load dhtmlx data.
	 *
	 * @param model the model
	 * @param id the id
	 * @param syn the syn
	 * @param pageRequest the page request
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/subTree/loadDhtmlxData")
	public void subTreeLoadDhtmlxData(ModelMap model, final @RequestParam(required = false) Long id,
			final @RequestParam(required = false) Boolean syn, DhtmlxPageRequest pageRequest,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<? extends SysGroupEntity> groupEntities = Lists.newArrayList();
		if (null == id || "".equals(id) || "0".equals(id)) {
			groupEntities = baseService.find("from SysGroupEntity x where x.parentId is null");
		} else {
			groupEntities = baseService.find("from SysGroupEntity x where x.parentId=?", id);
		}
		groupEntities = BeanMapper.mapList(groupEntities, SubTreeGroupEntity.class);
		final Map<String, List<TreeJsonInfo>> map = DhtmlxJsonObjectUtils.getAbstractJsonTreeEntities(groupEntities,
				syn);
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.SingleObjectMeesageCallBack(response) {

			@Override
			protected Object toObject() throws RebirthException {
				Boolean a = syn == null ? true : syn;
				return new TreeJson(a ? id : 0, map.get(DhtmlxJsonObjectUtils.ROWS));
			}
		});
		logger.info("Output json data:" + json);
	}

	/**
	 * The Class SubTreeGroupEntity.
	 *
	 * @author l.xue.nong
	 */
	public static class SubTreeGroupEntity extends SysGroupEntity {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -4498450874773359844L;

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity#getDhtmlxJsonOrXmlObject()
		 */
		@Override
		public Collection<Object> getDhtmlxJsonOrXmlObject() {
			List<Object> infos = Lists.newArrayList();
			CheckedTreeJsonInfo info = new CheckedTreeJsonInfo();
			info.setId(getId() == null ? "" : getId());
			info.setText(getGroupName() == null ? "" : getGroupName());
			info.setChild(getChildSysGroup().isEmpty() ? "0" : "1");
			infos.add(info);
			return infos;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.commons.entity.system.SysGroupEntity#getChildObject()
		 */
		@Override
		public Collection<? extends AbstractBaseEntity> getChildObject() {
			return BeanMapper.mapList(super.getChildObject(), SubTreeGroupEntity.class);
		}

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterAddRow(SysGroupEntity t) {
		cleanGroupRole(t.getSysUserEntities());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterUpdateRow(SysGroupEntity t) {
		cleanGroupRole(t.getSysUserEntities());
	}

	/**
	 * Clean group role.
	 *
	 * @param sysUserEntities the sys user entities
	 */
	private void cleanGroupRole(List<SysUserEntity> sysUserEntities) {
		if (sysUserEntities != null && !sysUserEntities.isEmpty()) {
			for (SysUserEntity sysUserEntity : sysUserEntities) {
				examJdbcRealm.clearCachedAuthorizationInfo(sysUserEntity.getLoginName());
			}
		}
	}

}

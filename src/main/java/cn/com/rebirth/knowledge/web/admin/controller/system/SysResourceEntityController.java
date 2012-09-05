/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysResourceEntityController.java 2012-7-19 16:16:11 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.CglibProxyUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.mapper.BeanMapper;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.Dhtmlx;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxBaseType;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.entity.system.SysAuthorityEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysPageButtonEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity.AuthorityListColumnDataSets;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class SysResourceEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysResource")
@Resource(names = "资源管理")
public class SysResourceEntityController extends AbstractDhtmlxController<SysResourceEntity, Long> {

	/** The shiro filter. */
	@Autowired
	protected AbstractShiroFilter shiroFilter;

	/**
	 * The Class SysPageButtonEntityExt.
	 *
	 * @author l.xue.nong
	 */
	@Dhtmlx(createCheckAllColumn = true, isDataProcessor = false, autoHeigth = true, isPage = false, enableUndoRedo = false)
	public static class SysPageButtonEntityExt extends SysPageButtonEntity {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6773794468908198564L;

	}

	/**
	 * The Class SysPageButtonEntityExtController.
	 *
	 * @author l.xue.nong
	 */
	private static class SysPageButtonEntityExtController extends
			AbstractDhtmlxController<SysPageButtonEntityExt, Long> {

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#columns(org.springframework.ui.Model)
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
			RequestMapping requestMapping = SysResourceEntityController.class.getAnnotation(RequestMapping.class);
			if (requestMapping != null) {
				return requestMapping.value()[0] + "/subGrid";
			}
			return "";
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		@Override
		public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
				throws RebirthException {
			final List<SysPageButtonEntity> list = baseService.getAll(SysPageButtonEntity.class);
			String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

				@Override
				public void business(Map<String, Object> returnMsg) throws RebirthException {
					DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg,
							BeanMapper.mapList(list, SysPageButtonEntityExt.class));
				}
			});
			return new OutputType(ServletUtils.JSON_TYPE, json);
		}

	}

	/** The button entity ext controller. */
	private static SysPageButtonEntityExtController buttonEntityExtController = new SysPageButtonEntityExtController();

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#beforeAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void beforeAddRow(SysResourceEntity entity) {
		Long parentId = entity.getGr_pid() == null ? entity.getTr_pid() : entity.getGr_pid();
		if (parentId != null) {
			entity.setParentResource(baseService.get(SysResourceEntity.class, parentId));
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#beforeUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void beforeUpdateRow(SysResourceEntity t) {
		Long parentId = t.getGr_pid() == null ? t.getTr_pid() : t.getGr_pid();
		if (parentId != null) {
			t.setParentResource(baseService.get(SysResourceEntity.class, parentId));
		}
	}

	/**
	 * Load data.
	 *
	 * @param model the model
	 * @param id the id
	 * @param request the request
	 * @param response the response
	 * @return the output type
	 * @throws RebirthException the rebirth exception
	 */
	@Override
	public OutputType loadData(ModelMap model, Long id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		List<SysResourceEntity> data = Lists.newArrayList();
		if (null == id || id == 0) {
			data = baseService.find("from SysResourceEntity x where x.parentId is null order by x.id desc");
		} else {
			data = baseService.find("from SysResourceEntity x where x.parentId=? order by x.id desc", id);
		}
		String xml = DhtmlxJsonObjectUtils.getAbstractXmlTreeGirdEntities(id == null ? 0 : Long.valueOf(id), data);
		return new OutputType(ServletUtils.XML_TYPE, xml);
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

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterAddRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterAddRow(SysResourceEntity t) {
		afterUpdateRow(t);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterUpdateRow(cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterUpdateRow(SysResourceEntity t) {
		List<String> removeUrl = Lists.newArrayList();
		String authNames = t.getAuthNames();
		PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
				.getFilterChainResolver();
		DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver
				.getFilterChainManager();
		String url = t.getValue();
		String requestMethod = t.getRequestType();
		String[] r_s = StringUtils.split(requestMethod, ",");
		if (r_s == null || r_s.length == 0) {
			if (StringUtils.isNotBlank(authNames)) {
				defaultFilterChainManager.createChain(url, "authc,perms[" + authNames + "]");
			} else {
				removeUrl.add(url);
			}
		} else {
			for (String method : r_s) {
				if (StringUtils.isNotBlank(authNames)) {
					defaultFilterChainManager.createChain(url + "![" + method + "]", "authc,perms[" + authNames + "]");
				} else {
					removeUrl.add(url + "![" + method + "]");
				}
			}
		}
		Set<String> keys = defaultFilterChainManager.getChainNames();
		String[] arr = keys.toArray(new String[keys.size()]);
		//string length
		Arrays.sort(arr, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (o1.endsWith("**") && o2.endsWith("**")) {
					return o2.length() - o1.length();
				}
				if (o1.endsWith("**"))
					return 1;
				if (o2.endsWith("**"))
					return 1;
				return o2.length() - o1.length();
			}
		});
		Map<String, NamedFilterList> map = Maps.newLinkedHashMap();
		for (String key : arr) {
			if (!removeUrl.contains(key))
				map.put(key, defaultFilterChainManager.getChain(key));
		}
		defaultFilterChainManager.setFilterChains(map);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#afterDeleteRow(java.io.Serializable, cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity)
	 */
	@Override
	protected void afterDeleteRow(Long id, SysResourceEntity entity) {
		afterUpdateRow(entity);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#init(org.springframework.ui.Model)
	 */
	@Override
	@ModelAttribute
	public void init(final Model model) {
		buttonEntityExtController.setBaseService(this.baseService);
		buttonEntityExtController.init(model);
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
	 * @param syn the syn
	 * @param pageRequest the page request
	 * @param request the request
	 * @param response the response
	 * @throws RebirthException the rebirth exception
	 */
	@RequestMapping(value = "/subGrid/loadDhtmlxData")
	@Resource(names = { "读取按钮数据" }, openMenu = false, showMenu = false)
	public void subGridLoadDhtmlxData(ModelMap model, final @RequestParam(required = false) Long id,
			final @RequestParam(required = false) Boolean syn, DhtmlxPageRequest pageRequest,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		buttonEntityExtController.setBaseService(baseService);
		buttonEntityExtController.loadDhtmlxData(model, id, pageRequest, request, response);
	}

}

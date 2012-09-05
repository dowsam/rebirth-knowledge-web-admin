/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin AbstractDhtmlxController.java 2012-8-7 10:48:24 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.rebirth.commons.PageRequest.Direction;
import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.search.annotation.AbstractSearchProperty;
import cn.com.rebirth.commons.settings.Settings;
import cn.com.rebirth.commons.utils.HttpRequestUtils;
import cn.com.rebirth.commons.utils.ResponseTypeOutputUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.inject.InjectInitialization;
import cn.com.rebirth.core.mapper.JaxbMapper;
import cn.com.rebirth.core.web.controller.AbstractBaseRestController;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.ColumnDataSets;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxConfig;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxPageRequest;
import cn.com.rebirth.knowledge.commons.dhtmlx.GridType;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.Dhtmlx;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxBaseType;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlxSort;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.AbstractDhtmlxBaseEntity;
import cn.com.rebirth.knowledge.commons.dhtmlx.entity.Column;
import cn.com.rebirth.knowledge.commons.dhtmlx.utils.DhtmlxInfoUtils;
import cn.com.rebirth.knowledge.commons.entity.AbstractBaseEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysPageButtonEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.persistence.service.BaseService;

import com.google.common.collect.Lists;

/**
 * The Class AbstractDhtmlxController.
 *
 * @param <T> the generic type
 * @param <PK> the generic type
 * @author l.xue.nong
 */
public abstract class AbstractDhtmlxController<T extends AbstractDhtmlxBaseEntity, PK extends Serializable> extends
		AbstractBaseRestController<T, PK> {

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** The base service. */
	@Autowired
	protected BaseService baseService;

	/**
	 * The Class AuthButton.
	 *
	 * @author l.xue.nong
	 */
	public static class AuthButton {

		/** The auth. */
		private final String auth;

		/** The button entities. */
		private final List<SysPageButtonEntity> buttonEntities;

		/**
		 * Instantiates a new auth button.
		 *
		 * @param sysResourceEntity the sys resource entity
		 */
		public AuthButton(SysResourceEntity sysResourceEntity) {
			this(sysResourceEntity.getAuthNames(), sysResourceEntity.getButtonEntitie());
		}

		/**
		 * Instantiates a new auth button.
		 *
		 * @param auth the auth
		 * @param buttonEntities the button entities
		 */
		public AuthButton(String auth, List<SysPageButtonEntity> buttonEntities) {
			super();
			this.auth = auth;
			this.buttonEntities = buttonEntities;
		}

		/**
		 * Gets the auth.
		 *
		 * @return the auth
		 */
		public String getAuth() {
			return auth;
		}

		/**
		 * Gets the button entities.
		 *
		 * @return the button entities
		 */
		public List<SysPageButtonEntity> getButtonEntities() {
			return buttonEntities;
		}

	}

	/**
	 * The Class GridTypePropertyEditor.
	 *
	 * @author l.xue.nong
	 */
	public static class GridTypePropertyEditor extends PropertyEditorSupport implements PropertyEditor {

		/* (non-Javadoc)
		 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
		 */
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(GridType.valueOf(text));
		}

	}

	/**
	 * The Class DirectionPropertyEditor.
	 *
	 * @author l.xue.nong
	 */
	public static class DirectionPropertyEditor extends PropertyEditorSupport implements PropertyEditor {

		/* (non-Javadoc)
		 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
		 */
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(Direction.fromString(text));
		}

	}

	/**
	 * Instantiates a new abstract dhtmlx controller.
	 */
	public AbstractDhtmlxController() {
		super();
	}

	/**
	 * Instantiates a new abstract dhtmlx controller.
	 *
	 * @param entityClass the entity class
	 */
	public AbstractDhtmlxController(Class<T> entityClass) {
		super(entityClass);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#initBinder(org.springframework.web.bind.WebDataBinder)
	 */
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) throws RebirthException {
		binder.registerCustomEditor(GridType.class, new GridTypePropertyEditor());
		binder.registerCustomEditor(Direction.class, new DirectionPropertyEditor());
		super.initBinder(binder);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#init(org.springframework.ui.Model)
	 */
	@Override
	@ModelAttribute
	public void init(Model model) {
		super.init(model);
		model.addAttribute("requestMappingUrl", requestMapping());
		model.addAttribute("gridObjectDiv", getGridObjectDiv() + "_div");
		model.addAttribute("gridObject", getGridObjectDiv());
		model.addAttribute("columns", columns(model));
		model.addAttribute("skin", skin());
		model.addAttribute("checkAllColumn", checkAllColumn());
		model.addAttribute("gridType", getGridType());
		model.addAttribute("dataType", getDataType());
		model.addAttribute("page", isPage());
		model.addAttribute("dataProcessor", getDataProcessor());
		//设置javascript调用的对象名称
		model.addAttribute("scriptObject", getScriptObject());
		model.addAttribute("autoHeigth", getAutoHeigth());
		model.addAttribute("enableUndoRedo", getEnableUndoRedo());
		model.addAttribute("buttons", buttonEntities());
	}

	/**
	 * Button entities.
	 *
	 * @return the list
	 */
	protected List<AuthButton> buttonEntities() {
		List<AuthButton> buttonEntities = Lists.newArrayList();
		Long resourcId = HttpRequestUtils.getLongParameter(RequestContext.get().request(),
				SysResourceEntity.RESOURCE_PARAM, 0);
		if (resourcId != 0) {
			SysResourceEntity resourceEntity = baseService.get(SysResourceEntity.class, resourcId);
			if (resourceEntity != null) {
				bulidChildButton(buttonEntities, resourceEntity);
			}
			Settings settings = InjectInitialization.injector().getInstance(Settings.class);
			if (settings.getAsBoolean("development.model", false) && buttonEntities.isEmpty()) {
				buttonEntities.add(new AuthButton("", Arrays.asList(SysPageButtonEntity.add,
						SysPageButtonEntity.delete, SysPageButtonEntity.update)));
			}
		}
		return buttonEntities;
	}

	/**
	 * Bulid child button.
	 *
	 * @param buttonEntities the button entities
	 * @param resourceEntity2 the resource entity2
	 */
	private void bulidChildButton(List<AuthButton> buttonEntities, SysResourceEntity resourceEntity2) {
		if (resourceEntity2.getButtonEntitie() != null && !resourceEntity2.getButtonEntitie().isEmpty())
			buttonEntities.add(new AuthButton(resourceEntity2.getAuthNames(), resourceEntity2.getButtonEntitie()));
		List<SysResourceEntity> child = resourceEntity2.getChildResource();
		if (child != null && !child.isEmpty()) {
			for (SysResourceEntity sysResourceEntity : child) {
				bulidChildButton(buttonEntities, sysResourceEntity);
			}
		}
	}

	/**
	 * Request mapping.
	 *
	 * @return the string
	 */
	protected String requestMapping() {
		RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
		if (requestMapping != null) {
			return requestMapping.value()[0];
		}
		return "";
	}

	/**
	 * Gets the enable undo redo.
	 *
	 * @return the enable undo redo
	 */
	protected Object getEnableUndoRedo() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.enableUndoRedo();
		}
		return false;
	}

	/**
	 * Gets the auto heigth.
	 *
	 * @return the auto heigth
	 */
	private Object getAutoHeigth() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.autoHeigth();
		}
		return false;
	}

	/**
	 * Gets the script object.
	 *
	 * @return the script object
	 */
	protected Object getScriptObject() {
		return getGridObjectDiv();
	}

	/**
	 * Gets the data processor.
	 *
	 * @return the data processor
	 */
	protected boolean getDataProcessor() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.isDataProcessor();
		}
		return false;
	}

	/**
	 * Checks if is page.
	 *
	 * @return true, if is page
	 */
	protected boolean isPage() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.isPage();
		}
		return true;
	}

	/**
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	private String getDataType() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.dataType();
		}
		return "json";
	}

	/**
	 * Gets the grid type.
	 *
	 * @return the grid type
	 */
	protected Object getGridType() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.gridType().name();
		}
		return GridType.dhtmlxGrid.name();
	}

	/**
	 * Check all column.
	 *
	 * @return the object
	 */
	protected Object checkAllColumn() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.createCheckAllColumn();
		}
		return false;
	}

	/**
	 * Skin.
	 *
	 * @return the object
	 */
	private Object skin() {
		Dhtmlx dhtmlx = this.entityClass.getAnnotation(Dhtmlx.class);
		if (dhtmlx != null) {
			return dhtmlx.skin();
		}
		return "";
	}

	/**
	 * Gets the grid object div.
	 *
	 * @return the grid object div
	 */
	protected Object getGridObjectDiv() {
		return StringUtils.uncapitalize(this.entityClass.getSimpleName());
	}

	/**
	 * Columns.
	 *
	 * @param model the model
	 * @return the list
	 */
	protected List<Column> columns(Model model) {
		List<String> strings = Lists.newArrayList();
		List<AbstractSearchProperty> columnPropertys = DhtmlxInfoUtils.getProperties(this.entityClass);
		List<Column> list = Lists.newArrayListWithCapacity(columnPropertys.size());
		for (AbstractSearchProperty property : columnPropertys) {
			DhtmlColumn dhtmlColumn = property.getElement().getAnnotation(DhtmlColumn.class);
			if (dhtmlColumn != null) {
				Column column = new Column();
				column.setHeader(dhtmlColumn.headerName());
				column.setWidth(dhtmlColumn.initWidth() + "");
				column.setId(findIdName(dhtmlColumn, property));
				column.setAlign(dhtmlColumn.coulumnAlign().name().toLowerCase());
				column.setType(dhtmlColumn.coulumnType().name().toLowerCase());
				column.setVisible(dhtmlColumn.columnVisibility());
				column.setQueryExpression(convterSort(dhtmlColumn, property));
				column.setColumnDataSets(constructorColumnData(dhtmlColumn));
				column.setProperty(property);
				column.setGroup(dhtmlColumn.group());
				list.add(column);
				if (!dhtmlColumn.sortable()) {
					strings.add(column.getId());
				}
			}
		}
		model.addAttribute("notSortFields", StringUtils.join(strings, ","));
		return list;
	}

	/**
	 * Constructor column data.
	 *
	 * @param dhtmlColumn the dhtml column
	 * @return the column data sets
	 */
	protected ColumnDataSets constructorColumnData(DhtmlColumn dhtmlColumn) {
		Class<? extends ColumnDataSets> class1 = dhtmlColumn.columnDataSets();
		if (class1.isInterface())
			return null;
		if (ColumnDataSets.class.isAssignableFrom(class1)) {
			try {
				return ConstructorUtils.invokeConstructor(class1);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Convter sort.
	 *
	 * @param dhtmlColumn the dhtml column
	 * @param property the property
	 * @return the string
	 */
	private String convterSort(DhtmlColumn dhtmlColumn, AbstractSearchProperty property) {
		String name = dhtmlColumn.columnSort().name().toLowerCase();
		if (DhtmlxSort.NONE.equals(dhtmlColumn.columnSort())) {
			Class<?> class1 = property.getPropertyClass();
			if (Number.class.isAssignableFrom(class1)) {
				return DhtmlxSort.INT.name().toLowerCase();
			} else if (Date.class.isAssignableFrom(class1)) {
				return DhtmlxSort.DATE.name().toLowerCase();
			} else {
				return DhtmlxSort.STR.name().toLowerCase();
			}
		}
		return name;
	}

	/**
	 * Find id name.
	 *
	 * @param dhtmlColumn the dhtml column
	 * @param property the property
	 * @return the string
	 */
	private String findIdName(DhtmlColumn dhtmlColumn, AbstractSearchProperty property) {
		String name = dhtmlColumn.columnId();
		if (DhtmlxConfig.FIELDNAME.equals(name)) {
			return property.getFieldName();
		}
		return name;
	}

	/**
	 * Load dhtmlx data.
	 *
	 * @param model the model
	 * @param id the id
	 * @param pageRequest the page request
	 * @param request the request
	 * @param response the response
	 * @throws RebirthException the rebirth exception
	 */
	@RequestMapping(value = "/loadDhtmlxData")
	@Resource(names = "读取数据", showMenu = false, openMenu = false)
	public void loadDhtmlxData(ModelMap model, @RequestParam(required = false) PK id, DhtmlxPageRequest pageRequest,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		//TODO 统一前台分页
		OutputType outputType = loadData(model, id, request, response);
		ResponseTypeOutputUtils.render(outputType.getMimeType(), outputType.getData(), response);
	}

	/**
	 * The Class OutputType.
	 *
	 * @author l.xue.nong
	 */
	protected class OutputType {

		/** The mime type. */
		private String mimeType;

		/** The data. */
		private String data;

		/**
		 * Instantiates a new output type.
		 *
		 * @param mimeType the mime type
		 * @param data the data
		 */
		public OutputType(String mimeType, String data) {
			super();
			this.mimeType = mimeType;
			this.data = data;
		}

		/**
		 * Gets the mime type.
		 *
		 * @return the mime type
		 */
		public String getMimeType() {
			return mimeType;
		}

		/**
		 * Sets the mime type.
		 *
		 * @param mimeType the new mime type
		 */
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		/**
		 * Gets the data.
		 *
		 * @return the data
		 */
		public String getData() {
			return data;
		}

		/**
		 * Sets the data.
		 *
		 * @param data the new data
		 */
		public void setData(String data) {
			this.data = data;
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
	public OutputType loadData(ModelMap model, PK id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		final List<T> list = baseService.getAll(entityClass);
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

			@Override
			public void business(Map<String, Object> returnMsg) throws RebirthException {
				DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg, list);
			}
		});
		return new OutputType(ServletUtils.JSON_TYPE, json);
	}

	/**
	 * Execute.
	 *
	 * @param model the model
	 * @param entity the entity
	 * @param errors the errors
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	//	@RequestMapping(method = RequestMethod.POST)
	//	@Resource(names = "Dhtmlx组件", showMenu = false, openMenu = false)
	public void execute(Model model, @Valid T entity, BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!(entity instanceof AbstractBaseEntity)) {
			throw new IllegalArgumentException("Entity Not Instanceof AbstractBaseEntity!");
		}
		AbstractBaseEntity abstractBaseEntity = (AbstractBaseEntity) entity;
		Action action = new Action();
		action.setSid(abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id() : abstractBaseEntity
				.getGr_id());
		if (errors.hasErrors()) {
			action.setTid(abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id() : abstractBaseEntity
					.getGr_id());
			if (action.getTid() == null) {
				action.setTid(abstractBaseEntity.getId());
			}
			action.setType("error");
		} else {
			//处理业务逻辑
			if ("inserted".equals(abstractBaseEntity.getNativeeditor_status())) {
				abstractBaseEntity = addRow(model, entity, errors, request, response);
				action.setTid(abstractBaseEntity.getId());
				action.setType("insert");
				action.setValue(abstractBaseEntity.getDhtmlxValue());
			} else if ("deleted".equals(abstractBaseEntity.getNativeeditor_status())) {
				deleteRow(model, (PK) (abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id()
						: abstractBaseEntity.getGr_id()), entity);
				action.setTid(abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id()
						: abstractBaseEntity.getGr_id());
				action.setType("delete");
			} else {
				abstractBaseEntity = updateRow(model,
						(PK) (abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id()
								: abstractBaseEntity.getGr_id()), entity, errors, request, response);
				action.setTid(abstractBaseEntity.getGr_id() == null ? abstractBaseEntity.getTr_id()
						: abstractBaseEntity.getGr_id());
				action.setType("update");
				action.setValue(abstractBaseEntity.getDhtmlxValue());
			}
		}

		Data data = new Data();
		data.setAction(action);
		String xml = new JaxbMapper(Data.class).toXml(data);
		ResponseTypeOutputUtils.renderXml(response, xml);
	}

	/**
	 * Adds the row.
	 *
	 * @param model the model
	 * @param entity the entity
	 * @param errors the errors
	 * @param request the request
	 * @param response the response
	 * @return the abstract base entity
	 * @throws RebirthException the rebirth exception
	 */
	public AbstractBaseEntity addRow(Model model, @Valid T entity, BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) throws RebirthException {
		copy(model, entity, request);
		beforeAddRow(entity);
		T t = baseService.save(entity);
		afterAddRow(t);
		return t;
	}

	/**
	 * After add row.
	 *
	 * @param t the t
	 */
	protected void afterAddRow(T t) {

	}

	/**
	 * Before add row.
	 *
	 * @param entity the entity
	 */
	protected void beforeAddRow(T entity) {

	}

	/**
	 * Delete row.
	 *
	 * @param model the model
	 * @param id the id
	 * @param entity the entity
	 * @throws RebirthException the rebirth exception
	 */
	public void deleteRow(Model model, @PathVariable PK id, T entity) throws RebirthException {
		beforeDeleteRow(id, entity);
		baseService.delete(entityClass, id);
		afterDeleteRow(id, entity);
	}

	/**
	 * After delete.
	 *
	 * @param id the id
	 * @param entity the entity
	 */
	protected void afterDeleteRow(PK id, T entity) {

	}

	/**
	 * Before delete.
	 *
	 * @param id the id
	 * @param entity the entity
	 */
	protected void beforeDeleteRow(PK id, T entity) {

	}

	/**
	 * Copy.
	 *
	 * @param model the model
	 * @param entity the entity
	 * @param request the request
	 */
	protected void copy(Model model, T entity, HttpServletRequest request) {
		List<Column> columns = columns(model);
		for (Column column : columns) {
			AbstractSearchProperty property = column.getProperty();
			property.setProperty(entity, updateRowPropertyConverter(property, entity, column, request));
		}
	}

	/**
	 * Update row.
	 *
	 * @param model the model
	 * @param id the id
	 * @param entity the entity
	 * @param errors the errors
	 * @param request the request
	 * @param response the response
	 * @return the abstract base entity
	 * @throws RebirthException the rebirth exception
	 */
	public AbstractBaseEntity updateRow(Model model, @PathVariable PK id, @Valid T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		copy(model, entity, request);
		beforeUpdateRow(entity);
		baseService.save(entity);
		afterUpdateRow(entity);
		return entity;
	}

	/**
	 * After update row.
	 *
	 * @param t the t
	 */
	protected void afterUpdateRow(T t) {

	}

	/**
	 * Before update row.
	 *
	 * @param t the t
	 */
	protected void beforeUpdateRow(T t) {

	}

	/**
	 * Update row property converter.
	 *
	 * @param property the property
	 * @param entity the entity
	 * @param column the column
	 * @param request the request
	 * @return the object
	 */
	protected Object updateRowPropertyConverter(AbstractSearchProperty property, T entity, Column column,
			HttpServletRequest request) {
		String type = column.getType();
		if (StringUtils.isNotBlank(type)) {
			DhtmlxBaseType dhtmlxBaseType = DhtmlxBaseType.valueOf(type.toUpperCase());
			if (dhtmlxBaseType != null) {
				if (DhtmlxBaseType.CLIST.equals(dhtmlxBaseType) || DhtmlxBaseType.GRID.equals(dhtmlxBaseType)
						|| DhtmlxBaseType.STREE.equals(dhtmlxBaseType)) {
					String ids = request.getParameter(column.getId());
					if (StringUtils.isNotBlank(ids)) {
						String id[] = StringUtils.split(ids, ",");
						List<Long> longs = Lists.newArrayListWithCapacity(id.length);
						for (String string : id) {
							longs.add(Long.valueOf(string));
						}
						return baseService.findByIds(property.getPropertyClass(), longs);
					} else {
						return null;
					}
				}
			}
		}
		return property.getProperty(entity);
	}

	/**
	 * The Class SerializableAdapter.
	 *
	 * @author l.xue.nong
	 */
	public static class SerializableAdapter extends XmlAdapter<String, Serializable> {

		/* (non-Javadoc)
		 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
		 */
		@Override
		public Serializable unmarshal(String v) throws Exception {
			return v;
		}

		/* (non-Javadoc)
		 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
		 */
		@Override
		public String marshal(Serializable v) throws Exception {
			return v.toString();
		}

	}

	/**
	 * The Class Action.
	 *
	 * @author l.xue.nong
	 */
	public static class Action {

		/** The type. */
		private String type;

		/** The sid. */
		private Serializable sid;

		/** The tid. */
		private Serializable tid;

		/** The value. */
		private String value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		@XmlValue
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		@XmlAttribute
		public String getType() {
			return type;
		}

		/**
		 * Sets the type.
		 *
		 * @param type the new type
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * Gets the sid.
		 *
		 * @return the sid
		 */
		@XmlAttribute
		@XmlJavaTypeAdapter(SerializableAdapter.class)
		public Serializable getSid() {
			return sid;
		}

		/**
		 * Sets the sid.
		 *
		 * @param sid the new sid
		 */
		public void setSid(Serializable sid) {
			this.sid = sid;
		}

		/**
		 * Gets the tid.
		 *
		 * @return the tid
		 */
		@XmlAttribute
		@XmlJavaTypeAdapter(SerializableAdapter.class)
		public Serializable getTid() {
			return tid;
		}

		/**
		 * Sets the tid.
		 *
		 * @param tid the new tid
		 */
		public void setTid(Serializable tid) {
			this.tid = tid;
		}
	}

	/**
	 * The Class Data.
	 *
	 * @author l.xue.nong
	 */
	@XmlRootElement
	@XmlType(propOrder = { "action" })
	public static class Data {

		/** The action. */
		private Action action;

		/**
		 * Gets the action.
		 *
		 * @return the action
		 */
		public Action getAction() {
			return action;
		}

		/**
		 * Sets the action.
		 *
		 * @param action the new action
		 */
		@XmlElement(name = "action")
		public void setAction(Action action) {
			this.action = action;
		}
	}

	/**
	 * Sets the base service.
	 *
	 * @param baseService the new base service
	 */
	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#toModel(java.io.Serializable)
	 */
	@Override
	protected T toModel(PK id) {
		return baseService.get(entityClass, id);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#index(org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		return requestMapping() + "/index";
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#create(org.springframework.ui.Model, java.lang.Object, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	@Resource(names = "创建", openMenu = false, showMenu = false)
	public String create(Model model, @Valid T entity, BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) throws RebirthException {
		Action action = new Action();
		action.setSid(entity.getGr_id() == null ? entity.getTr_id() : entity.getGr_id());
		if (errors.hasErrors()) {
			action.setTid(entity.getGr_id() == null ? entity.getTr_id() : entity.getGr_id());
			if (action.getTid() == null) {
				action.setTid(entity.getId());
			}
			action.setType("error");
		} else {
			action.setTid(addRow(model, entity, errors, request, response).getId());
			action.setType("insert");
			action.setValue(entity.getDhtmlxValue());
		}
		outputXml(action, response);
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#update(org.springframework.ui.Model, java.io.Serializable, java.lang.Object, org.springframework.validation.BindingResult, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Resource(names = "更新", openMenu = false, showMenu = false)
	public String update(Model model, @PathVariable PK id, @Valid T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) throws RebirthException {
		Action action = new Action();
		action.setSid(id);
		if (errors.hasErrors()) {
			action.setTid(id);
			action.setType("error");
		} else {
			action.setTid(updateRow(model, id, entity, errors, request, response).getId());
			action.setType("update");
			action.setValue(entity.getDhtmlxValue());
		}
		outputXml(action, response);
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.core.web.controller.AbstractBaseRestController#delete(org.springframework.ui.Model, java.io.Serializable, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Resource(names = "删除", openMenu = false, showMenu = false)
	public String delete(Model model, @PathVariable PK id, T entity, HttpServletRequest request,
			HttpServletResponse response) throws RebirthException {
		deleteRow(model, id, entity);
		Action action = new Action();
		action.setSid(id);
		action.setTid(id);
		action.setType("delete");
		outputXml(action, response);

		return null;
	}

	/**
	 * Output xml.
	 *
	 * @param action the action
	 * @param response the response
	 */
	protected void outputXml(Action action, HttpServletResponse response) {
		Data data = new Data();
		data.setAction(action);
		String xml = new JaxbMapper(Data.class).toXml(data);
		ResponseTypeOutputUtils.renderXml(response, xml);
	}

}
/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin BootstrapDataPopulator.java 2012-7-19 16:25:23 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.VersionFactory;
import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ClassResolverUtils;
import cn.com.rebirth.commons.utils.ClassResolverUtils.AbstractFindCallback;
import cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback;
import cn.com.rebirth.commons.utils.ResolverUtils;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.entity.system.SysPageButtonEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.persistence.service.BaseService;

import com.google.common.collect.Lists;

/**
 * The Class BootstrapDataPopulator.
 *
 * @author l.xue.nong
 */
public class BootstrapDataPopulator implements InitializingBean {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BootstrapDataPopulator.class);

	/** The base service. */
	@Autowired
	private BaseService baseService;

	/** The find callback. */
	private static FindCallback<Object> findCallback = new AbstractFindCallback<Object>() {

		@Override
		protected void doFindType(ResolverUtils<Object> resolverUtils, Class<Object> entityClass) {
			resolverUtils.findAnnotated(Controller.class, StringUtils.EMPTY);
		}
	};

	/** The annotations. */
	private List<ResourceDic> packageAnnotation = Lists.newArrayList();

	/**
	 * Adds the ann.
	 *
	 * @param annotation the annotation
	 */
	private void addAnn(ResourceDic annotation) {
		if (!packageAnnotation.contains(annotation)) {
			packageAnnotation.add(annotation);
		}
	}

	/**
	 * The Class ResourceDic.
	 *
	 * @author l.xue.nong
	 */
	class ResourceDic {

		/** The resource. */
		public Resource resource;

		/** The target class. */
		public Class<?> targetClass;

		/**
		 * Instantiates a new resource dic.
		 *
		 * @param resource the resource
		 * @param targetClass the target class
		 */
		public ResourceDic(Resource resource, Class<?> targetClass) {
			super();
			this.resource = resource;
			this.targetClass = targetClass;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((resource == null) ? 0 : resource.hashCode());
			result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ResourceDic other = (ResourceDic) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (resource == null) {
				if (other.resource != null)
					return false;
			} else if (!resource.equals(other.resource))
				return false;
			if (targetClass == null) {
				if (other.targetClass != null)
					return false;
			} else if (!targetClass.equals(other.targetClass))
				return false;
			return true;
		}

		/**
		 * Gets the outer type.
		 *
		 * @return the outer type
		 */
		private BootstrapDataPopulator getOuterType() {
			return BootstrapDataPopulator.this;
		}

	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<Object> controllers = ClassResolverUtils.find(findCallback);
		Collections.sort(controllers, new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				String packageName1 = o1.getClass().getPackage().getName();
				String packageName2 = o2.getClass().getPackage().getName();
				return packageName1.length() - packageName2.length();
			}
		});
		for (Object controller : controllers) {
			//处理包
			Package package1 = Package.getPackage(controller.getClass().getPackage().getName());
			Resource resource = package1.getAnnotation(Resource.class);
			if (resource != null) {
				addAnn(new ResourceDic(resource, null));
				//处理类
				Class<?> tagerClass = controller.getClass();
				if (tagerClass.isAnnotationPresent(Controller.class)) {
					Resource resource2 = tagerClass.getAnnotation(Resource.class);
					if (resource2 != null) {
						addAnn(new ResourceDic(resource2, tagerClass));
					}
				}
			}
		}
		//save package-info.java
		SysResourceEntity parent = null;
		for (int i = 0; i < packageAnnotation.size(); i++) {
			ResourceDic resourceDic = packageAnnotation.get(i);
			SysResourceEntity sysResourceEntity = new SysResourceEntity();
			sysResourceEntity.setParentResource(parent);
			sysResourceEntity.setResourceType(resourceDic.resource.resourceType());
			sysResourceEntity.setShowMenu(resourceDic.resource.showMenu());
			sysResourceEntity.setOpenMenu(resourceDic.resource.openMenu());
			sysResourceEntity.setPosition(i);
			String[] values = resourceDic.resource.values();
			if (values == null || values.length == 0) {
				RequestMapping requestMapping = resourceDic.targetClass.getAnnotation(RequestMapping.class);
				if (requestMapping != null) {
					values = requestMapping.value();
				}
			}
			String[] names = resourceDic.resource.names();
			if (values.length != names.length)
				throw new RebirthException("values length not eq names length");
			if (resourceDic.targetClass == null) {
				if (values.length > 1 || values.length < 0)
					throw new RebirthException("Package resource value length eq one");
				if (names.length > 1 || names.length < 0)
					throw new RebirthException("Package resource name length eq one");
				if ("/".equalsIgnoreCase(values[0]) && "/".equalsIgnoreCase(names[0])) {
					names[0] = RebirthContainer.getInstance().get(VersionFactory.class).currentVersion()
							.getModuleName();
				}
				sysResourceEntity.setValue(values[0]);
				sysResourceEntity.setValueName(names[0]);
				parent = baseService.save(sysResourceEntity);
			} else {
				String params[] = resourceDic.resource.params();
				if (params == null || params.length == 0) {
					RequestMapping requestMapping = resourceDic.targetClass.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						params = requestMapping.params();
					}
				}
				String[] methods = resourceDic.resource.method();
				if (methods == null || methods.length == 0) {
					RequestMapping requestMapping = resourceDic.targetClass.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						RequestMethod[] requestMethods = requestMapping.method();
						methods = StringUtils.split(StringUtils.join(requestMethods, ","), ",");
					}
					if (methods == null || methods.length == 0) {
						methods = new String[1];
						methods[0] = RequestMethod.GET.name();
					}
				}
				for (int j = 0; j < values.length; j++) {
					sysResourceEntity.setId(null);
					sysResourceEntity.setValue(values[j]);
					sysResourceEntity.setValueName(names[j]);
					sysResourceEntity.setRequestType(StringUtils.join(methods, ","));
					baseService.save(sysResourceEntity);
					if (params != null && params.length > 0) {
						for (int j2 = 0; j2 < params.length; j2++) {
							sysResourceEntity.setId(null);
							String url = values[j];
							if (url.indexOf("?") == -1) {
								url = url + "?" + params[j2];
							} else {
								url = url + "&" + params[j2];
							}
							sysResourceEntity.setValue(url);
							baseService.save(sysResourceEntity);
						}
					}
				}
				actionClassMethodRequestUrl(sysResourceEntity, resourceDic);
			}
		}
		//用户
		SysUserEntity sysUser = new SysUserEntity();
		sysUser.setLoginName("admin");
		sysUser.setUserName("系统管理员");
		sysUser.setEmail("user1@163.com");
		sysUser.setPassWord(new Sha256Hash("admin").toHex());
		baseService.save(sysUser);
		//添加默认的按钮事件
		//新增
		baseService.save(SysPageButtonEntity.add);
		//删除
		baseService.save(SysPageButtonEntity.delete);
		//保存
		baseService.save(SysPageButtonEntity.update);
		logger.info("初始化数据完成!");
	}

	/**
	 * Action class method request url.
	 *
	 * @param sysResourceEntity the sys resource entity
	 * @param resourceDic the resource dic
	 */
	protected void actionClassMethodRequestUrl(SysResourceEntity sysResourceEntity, ResourceDic resourceDic) {
		if (resourceDic.targetClass != null) {
			List<String> methodNames = Lists.newArrayList();
			for (Class<?> superClass = resourceDic.targetClass; superClass != Object.class; superClass = superClass
					.getSuperclass()) {
				putMethods(sysResourceEntity, superClass, methodNames);
			}
		}
	}

	/**
	 * Put methods.
	 *
	 * @param parent the parent
	 * @param c the c
	 * @param methodNames the method names
	 */
	protected void putMethods(SysResourceEntity parent, Class<?> c, List<String> methodNames) {
		Method[] methods = c.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			int modifiers = method.getModifiers();
			if (Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isFinal(modifiers))
				continue;
			if (methodNames.contains(method.getName()))
				continue;
			Resource resource = method.getAnnotation(Resource.class);
			if (resource != null) {
				methodNames.add(method.getName());
				SysResourceEntity sysResourceEntity = new SysResourceEntity();
				sysResourceEntity.setParentResource(parent);
				sysResourceEntity.setResourceType(resource.resourceType());
				sysResourceEntity.setShowMenu(resource.showMenu());
				sysResourceEntity.setOpenMenu(resource.openMenu());
				sysResourceEntity.setPosition(i);
				String[] names = resource.names();
				String[] values = resource.values();
				if (values == null || values.length == 0) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						values = requestMapping.value();
					}
					if (values == null || values.length == 0) {
						values = new String[names.length];
					}
				}
				String params[] = resource.params();
				if (params == null || params.length == 0) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						params = requestMapping.params();
					}
				}
				String[] requestMethods = resource.method();
				if (requestMethods == null || requestMethods.length == 0) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						RequestMethod[] _requestMethods = requestMapping.method();
						requestMethods = StringUtils.split(StringUtils.join(_requestMethods, ","), ",");
					}
					if (requestMethods == null || requestMethods.length == 0) {
						requestMethods = new String[1];
						requestMethods[0] = RequestMethod.GET.name();
					}
				}
				if (values.length != names.length)
					throw new RebirthException("values length not eq names length");
				for (int j = 0; j < values.length; j++) {
					sysResourceEntity.setId(null);
					sysResourceEntity.setValue(parent.getValue() + bulidUrl((values[j] == null ? "" : values[j])));
					sysResourceEntity.setValueName(names[j]);
					sysResourceEntity.setRequestType(StringUtils.join(requestMethods, ","));
					baseService.save(sysResourceEntity);
					String url = sysResourceEntity.getValue();
					String valueName = sysResourceEntity.getValueName();
					if (params != null && params.length > 0) {
						for (int j2 = 0; j2 < params.length; j2++) {
							sysResourceEntity.setId(null);
							String _url = url;
							String _valueName = valueName;
							if (_url.indexOf("?") == -1) {
								_url = _url + "?" + params[j2];
							} else {
								_url = _url + "&" + params[j2];
							}
							sysResourceEntity.setValue(_url);
							sysResourceEntity.setValueName(_valueName + "(" + params[j2] + ")");
							baseService.save(sysResourceEntity);
						}
					}
				}
			}
		}
	}

	/**
	 * Bulid url.
	 *
	 * @param string the string
	 * @return the string
	 */
	protected String bulidUrl(String string) {
		return string;
	}

}

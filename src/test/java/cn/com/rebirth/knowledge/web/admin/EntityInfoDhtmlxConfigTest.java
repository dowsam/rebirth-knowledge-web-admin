/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin EntityInfoDhtmlxConfigTest.java 2012-8-2 10:51:48 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;

import cn.com.rebirth.commons.search.annotation.AbstractSearchProperty;
import cn.com.rebirth.commons.search.annotation.AnnotationInfo;
import cn.com.rebirth.commons.search.annotation.AnnotationManager;
import cn.com.rebirth.knowledge.commons.dhtmlx.annotation.DhtmlColumn;
import cn.com.rebirth.knowledge.commons.dhtmlx.impl.EntityInfoDhtmlxConfig;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.scheduler.QrtzTriggersJobDetails;

/**
 * The Class EntityInfoDhtmlxConfigTest.
 *
 * @author l.xue.nong
 */
public class EntityInfoDhtmlxConfigTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		AnnotationInfo annotationInfo = AnnotationManager.getInstance().getAnnotationInfo(OnlineSysUserEntity.class);
		Map<String, AbstractSearchProperty> map = annotationInfo.getProperties();
		for (Map.Entry<String, AbstractSearchProperty> entry : map.entrySet()) {
			AbstractSearchProperty property = entry.getValue();
			DhtmlColumn column = property.execute(new AbstractSearchProperty.ElementCallback<DhtmlColumn>() {

				@Override
				public DhtmlColumn doExecute(AnnotatedElement element) {
					return element.getAnnotation(DhtmlColumn.class);
				}
			});
			System.out.println(column);
		}
		System.out.println(map);
		EntityInfoDhtmlxConfig config = new EntityInfoDhtmlxConfig(QrtzTriggersJobDetails.class);
		System.out.println(config);
	}
}

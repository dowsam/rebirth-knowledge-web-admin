/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysPageButtonEntityController.java 2012-8-27 10:56:32 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.entity.system.SysPageButtonEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;

/**
 * The Class SysPageButtonEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysPageButton")
@Resource(names = { "按钮管理" })
public class SysPageButtonEntityController extends AbstractDhtmlxController<SysPageButtonEntity, Long> {

}

/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin SysAuthorityEntityController.java 2012-8-11 11:00:33 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.entity.system.SysAuthorityEntity;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;

/**
 * The Class SysAuthorityEntityController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/sysAuthority")
@Resource(names = "权限管理")
public class SysAuthorityEntityController extends AbstractDhtmlxController<SysAuthorityEntity, Long> {

}

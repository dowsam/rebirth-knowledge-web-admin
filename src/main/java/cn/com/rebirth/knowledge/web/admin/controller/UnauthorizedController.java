/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin UnauthorizedController.java 2012-8-23 11:01:03 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.knowledge.commons.annotation.Resource;

/**
 * The Class UnauthorizedController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/unauthorized")
@Resource(names = "无授权",openMenu=false,showMenu=false)
public class UnauthorizedController extends AbstractBaseController {

	/**
	 * Index.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "/unauthorized";
	}
}

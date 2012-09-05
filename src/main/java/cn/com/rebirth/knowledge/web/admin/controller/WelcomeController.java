/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin WelcomeController.java 2012-7-19 15:56:11 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.commons.xcontent.ToXContent;
import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentFactory;
import cn.com.rebirth.core.monitor.MonitorService;
import cn.com.rebirth.core.monitor.fs.FsService;
import cn.com.rebirth.core.monitor.fs.FsStats;
import cn.com.rebirth.core.monitor.jvm.JvmInfo;
import cn.com.rebirth.core.monitor.jvm.JvmService;
import cn.com.rebirth.core.monitor.jvm.JvmStats;
import cn.com.rebirth.core.monitor.network.NetworkInfo;
import cn.com.rebirth.core.monitor.network.NetworkService;
import cn.com.rebirth.core.monitor.network.NetworkStats;
import cn.com.rebirth.core.monitor.os.OsInfo;
import cn.com.rebirth.core.monitor.os.OsService;
import cn.com.rebirth.core.monitor.os.OsStats;
import cn.com.rebirth.core.monitor.process.ProcessInfo;
import cn.com.rebirth.core.monitor.process.ProcessService;
import cn.com.rebirth.core.monitor.process.ProcessStats;
import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.web.admin.monitor.MonitorServiceProvider;

/**
 * The Class WelcomeController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/welcome")
@Resource(names = "欢迎",openMenu=false,showMenu=false)
public class WelcomeController extends AbstractBaseController {
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
		MonitorServiceProvider monitorServiceProvider = MonitorServiceProvider.getInstance();
		MonitorService monitorService = monitorServiceProvider.getMonitorService();
		OsService osService = monitorService.osService();
		OsInfo osInfo = osService.info();
		OsStats osStats = osService.stats();
		JvmService jvmService = monitorService.jvmService();
		JvmInfo jvmInfo = jvmService.info();
		JvmStats jvmStats = jvmService.stats();
		ProcessService processService = monitorService.processService();
		ProcessInfo processInfo = processService.info();
		ProcessStats processStats = processService.stats();

		NetworkService networkService = monitorService.networkService();
		NetworkInfo networkInfo = networkService.info();
		NetworkStats networkStats = networkService.stats();

		FsService fsService = monitorService.fsService();
		FsStats fsStats = fsService.stats();
		model.addAttribute("osInfo", osInfo);
		model.addAttribute("osStats", osStats);
		model.addAttribute("jvmInfo", jvmInfo);
		model.addAttribute("jvmStats", jvmStats);
		model.addAttribute("processInfo", processInfo);
		model.addAttribute("processStats", processStats);
		model.addAttribute("networkInfo", networkInfo.getPrimaryInterface());
		model.addAttribute("networkStats", networkStats);
		model.addAttribute("fsStats", fsStats.iterator().next());
		return "/welcome";
	}

	protected static void println(ToXContent content) {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.prettyPrint();
			builder.startObject();
			content.toXContent(builder, ToXContent.EMPTY_PARAMS);
			builder.endObject();
			builder.close();
			System.out.println(builder.string());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

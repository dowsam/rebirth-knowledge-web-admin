/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin QrtzTriggersJobDetailsController.java 2012-8-4 14:07:27 l.xue.nong$$
 */
package cn.com.rebirth.knowledge.web.admin.controller.system;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ResponseTypeOutputUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.core.web.filter.RequestContext;
import cn.com.rebirth.knowledge.commons.JsonActionTemplate;
import cn.com.rebirth.knowledge.commons.annotation.Authority;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.dhtmlx.DhtmlxJsonObjectUtils;
import cn.com.rebirth.knowledge.commons.scheduler.QrtzTriggersJobDetails;
import cn.com.rebirth.knowledge.commons.scheduler.TaskService;
import cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController;
import cn.com.rebirth.service.middleware.client.ConsumerProxyFactory;

/**
 * The Class QrtzTriggersJobDetailsController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/system/scheduler")
@Authority
@Resource(names = "调度管理")
public class SchedulerController extends AbstractDhtmlxController<QrtzTriggersJobDetails, String> {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.knowledge.web.admin.controller.AbstractDhtmlxController#loadData(org.springframework.ui.ModelMap, java.io.Serializable, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public OutputType loadData(ModelMap model, String id, HttpServletRequest request, HttpServletResponse response)
			throws RebirthException {
		ConsumerProxyFactory consumerProxyFactory = ConsumerProxyFactory.getInstance();
		TaskService taskService = consumerProxyFactory.proxy(TaskService.class);
		final Collection<QrtzTriggersJobDetails> data = taskService.getQrtzTriggers();
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {
			@Override
			public void business(Map<String, Object> returnMsg) throws RebirthException {
				DhtmlxJsonObjectUtils.getAbstractJsonEntities(returnMsg, data);
			}
		});
		return new OutputType(ServletUtils.JSON_TYPE, json);
	}


	/**
	 * Pause.
	 *
	 * @param model the model
	 * @param items the items
	 */
	@RequestMapping(value = "/pause")
	@RequiresPermissions("system:scheduler:qrtzTriggersJobDetails:pause")
	public void pause(ModelMap model, @RequestParam(value = "items[]") String[] items, HttpServletRequest request,
			HttpServletResponse response) {
		action(model, items, new ActionCallback() {

			@Override
			public void run(TaskService taskService, String taskName) {
				taskService.pauseTask(taskName);
			}
		});
	}

	/**
	 * The Interface ActionCallback.
	 *
	 * @author l.xue.nong
	 */
	interface ActionCallback {

		/**
		 * Run.
		 *
		 * @param taskService the task service
		 * @param taskName the task name
		 */
		void run(TaskService taskService, String taskName);
	}

	/**
	 * Action.
	 *
	 * @param model the model
	 * @param items the items
	 * @param callback the callback
	 */
	void action(ModelMap model, final String[] items, final ActionCallback callback) {
		String json = JsonActionTemplate.renderJson(new JsonActionTemplate.AbstractMeesageCallback() {

			@Override
			public void business(Map<String, Object> returnMsg) throws RebirthException {
				TaskService taskService = ConsumerProxyFactory.getInstance().proxy(TaskService.class);
				for (String taskName : items) {
					callback.run(taskService, taskName);
				}
			}
		});
		ResponseTypeOutputUtils.renderJson(RequestContext.get().response(), json);
	}

	/**
	 * Resume.
	 *
	 * @param model the model
	 * @param items the items
	 */
	@RequestMapping(value = "/resume")
	@RequiresPermissions("system:scheduler:qrtzTriggersJobDetails:resume")
	public void resume(ModelMap model, @RequestParam("items[]") String[] items, HttpServletRequest request,
			HttpServletResponse response) {
		action(model, items, new ActionCallback() {

			@Override
			public void run(TaskService taskService, String taskName) {
				taskService.resumeTask(taskName);
			}
		});
	}

	/**
	 * Delete.
	 *
	 * @param model the model
	 * @param items the items
	 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions("system:scheduler:qrtzTriggersJobDetails:delete")
	public void delete(ModelMap model, @RequestParam("items[]") String[] items, HttpServletRequest request,
			HttpServletResponse response) {
		action(model, items, new ActionCallback() {

			@Override
			public void run(TaskService taskService, String taskName) {
				taskService.deleteTask(taskName);
			}
		});
	}

}

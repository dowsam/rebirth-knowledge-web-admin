/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: LoginController.java 2011-6-24 15:45:16 l.xue.nong$$
*/
package cn.com.rebirth.knowledge.web.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.rebirth.commons.utils.ReflectionUtils;
import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.core.web.jcaptcha.JCaptchaFilter;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.entity.system.OnlineSysUserEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.web.admin.service.UserService;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * The Class LoginController.
 *
 * @author l.xue.nong
 */
@Controller
@RequestMapping("/login")
@Resource(names = "登入",openMenu=false,showMenu=false)
public class LoginController extends AbstractBaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/** The captcha service. */
	@Autowired
	private CaptchaService captchaService;

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
		model.addAttribute("sysUserEntity", new SysUserEntity());
		return "/login";
	}

	/**
	 * Login.
	 *
	 * @param model the model
	 * @param user the user
	 * @param errors the errors
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String login(Model model, @ModelAttribute @Valid SysUserEntity user, BindingResult errors,
			HttpServletRequest request) {
		if (errors.hasErrors()) {
			return "/login";
		}
		//验证验证码是否正确
		if (!validateCaptchaChallenge(request)) {
			errors.reject("login.validateCaptchaChallenge.error.message");
			return "/login";
		}
		UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassWord(),
				user.isRememberMe());
		try {
			SecurityUtils.getSubject().login(token);
		} catch (AuthenticationException e) {
			if (e instanceof LockedAccountException) {
				errors.reject("error.login.loginName.locked.message");
			} else if (e instanceof DisabledAccountException) {
				errors.reject("error.login.loginName.disabled.message");
			} else {
				errors.reject("error.login.generic.message");
			}
		}

		if (errors.hasErrors()) {
			return "/login";
		} else {
			//独有的登入，踢掉所有相同帐号的用户
			if (user.getManner() == 1) {
				HttpSession httpSession = request.getSession(false);
				if (httpSession != null) {
					final String newSessionId = httpSession.getId();
					OnlineSysUserEntity o = UserService.get(newSessionId);
					if (o != null) {
						final Object oId = ReflectionUtils.getFieldValue(o, "id");
						UserService.removeEqUser(new UserService.SysUserEqCallback() {

							@Override
							public boolean eq(String sessionId, OnlineSysUserEntity onlineSysUserEntity) {
								if (newSessionId.equals(sessionId))
									return false;
								Object id = ReflectionUtils.getFieldValue(onlineSysUserEntity, "id");
								boolean b = false;
								if (id == null && oId == null)
									b = true;
								if (id == null) {
									b = oId.equals(id);
								}
								if (oId == null) {
									b = id.equals(oId);
								}
								b = id.equals(oId);
								if (b) {
									onlineSysUserEntity.setErrorCode(3);
								}
								return b;
							}
						});
					}
				}
			}
			return "redirect:/index";
		}
	}

	/**
	 * Validate captcha challenge.
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	private boolean validateCaptchaChallenge(HttpServletRequest request) {
		try {
			String captchaID = request.getSession(true).getId();
			String challengeResponse = request.getParameter(JCaptchaFilter.DEFAULT_CAPTCHA_PARAMTER_NAME);
			return captchaService.validateResponseForID(captchaID, challengeResponse);
		} catch (CaptchaServiceException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Logout.
	 *
	 * @return the string
	 */
	@RequestMapping("/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/";
	}

}

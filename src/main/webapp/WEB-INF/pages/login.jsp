<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="app.title.message" /></title>
<link href="${base}/css/loading.css" rel="stylesheet" type="text/css" />
<link href="${base}/css/layoutzhgb2312.css" rel="stylesheet"
	type="text/css" />
<link href="${base}/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/js/prototype.js"></script>
<script type="text/javascript" src="${base}/js/Common.js"></script>
<script type="text/javascript" src="${base}/js/Dialog.js"></script>
<script type="text/javascript" src="${base}/js/DivDialog.js"></script>
<script type="text/javascript" src="${base}/js/Utils.js"></script>
<script type="text/javascript">
	var loadingIframeId = "test";
	var Dialog_Btn_Submit = "确  定";
	function AttachEventToForgetPassword() {
		if (DivDialog.isExistById("DivAlert") != null) {
			var btn = $("DivAlert").getElementsByTagName("INPUT");
			btn[0].onclick = BackToRecordForgetPasswordDialog;
		}
	}
	function BackToRecordForgetPasswordDialog() {
		RemoveObj('DivAlert');
		ForgetPassword();
	}
	function ForgetPassword() {
		DivDialog.Forgetpasswd('取回密码', '工      号：', '电子邮件：', '确  定',
				'工号和电子邮件不能为空！');
		if (DivDialog.isExistById("lblMessage") != null) {
			$('lblMessage').style.display = 'none';
		}
	}
</script>

<script type="text/javascript">
	window.onload = function() {
		document.loginForm.loginName.focus();
	};
	//验证码
	function refreshCaptcha() {
		var captchaImg = document.getElementById("captchaImg");
		captchaImg.innerHTML = '<img src="${base}/images/jcaptcha.jpg?'
				+ Math.round(Math.random() * 100000)
				+ '" height="30" align="absMiddle" />';
		//captchaImg.html();
	}

	//验证信息
	function validateFrm() {

		if ($("txtUserName").value.trim() == "") {
			$("txtUserName").focus();
			new HintDialog($("txtUserName"), "用户名不能为空！");
			return false;
		}
		if ($("txtPassword").value.trim() == "") {
			$("txtPassword").focus();
			new HintDialog($("txtPassword"), "密码不能为空！");
			return false;
		}
		 if ($("txtImg").value.trim() == "")
         {
             $("txtImg").focus();
             new HintDialog($("txtImg"), "验证码不能为空！");
             return false;
         }
		$("UpdateProgress1").show();

		return true;

	}
	function autologon() {
		document.loginForm.reset();
		return false;
	}

	function fDragging(obj, e, limit) {
		if (!e)
			e = window.event;
		var x = parseInt(obj.style.left);
		var y = parseInt(obj.style.top);

		var x_ = e.clientX - x;
		var y_ = e.clientY - y;

		if (document.addEventListener) {
			document.addEventListener('mousemove', inFmove, true);
			document.addEventListener('mouseup', inFup, true);
		} else if (document.attachEvent) {
			document.attachEvent('onmousemove', inFmove);
			document.attachEvent('onmouseup', inFup);
		}

		inFstop(e);
		inFabort(e)

		function inFmove(e) {
			var evt;
			if (!e)
				e = window.event;

			if (limit) {
				var op = obj.parentNode;
				var opX = parseInt(op.style.left);
				var opY = parseInt(op.style.top);

				if ((e.clientX - x_) < 0)
					return false;
				else if ((e.clientX - x_ + obj.offsetWidth + opX) > (opX + op.offsetWidth))
					return false;

				if (e.clientY - y_ < 0)
					return false;
				else if ((e.clientY - y_ + obj.offsetHeight + opY) > (opY + op.offsetHeight))
					return false;
				//status=e.clientY-y_;
			}
			obj.style.left = e.clientX - x_ + 'px';
			obj.style.top = e.clientY - y_ + 'px';

			inFstop(e);
		} // shawl.qiu script
		function inFup(e) {
			var evt;
			if (!e)
				e = window.event;

			if (document.removeEventListener) {
				document.removeEventListener('mousemove', inFmove, true);
				document.removeEventListener('mouseup', inFup, true);
			} else if (document.detachEvent) {
				document.detachEvent('onmousemove', inFmove);
				document.detachEvent('onmouseup', inFup);
			}

			inFstop(e);
		} // shawl.qiu script

		function inFstop(e) {
			if (e.stopPropagation)
				return e.stopPropagation();
			else
				return e.cancelBubble = true;
		} // shawl.qiu script
		function inFabort(e) {
			if (e.preventDefault)
				return e.preventDefault();
			else
				return e.returnValue = false;
		} // shawl.qiu script
	}
</script>
</head>
<body>
	<center>
		<form:form method="post" name="loginForm"
			modelAttribute="sysUserEntity" action="${base}/login">
			<div id="position">
				<div id="inposition">
					<div class="login-left">
						<div class="login-left-logo">
							<img src="images/login-logo.gif" width="297" height="78" />
						</div>
						<div class="login-left-content">
							<ul>
								<li>运行环境:</li>
								<li><img src="${base}/images/s.gif" class="IE"
									align="bottom" /> Internet Explorer 6+</li>
								<li><img src="${base}/images/s.gif" class="FF"
									align="bottom" /> Mozilla Firefox 2+</li>
								<li><img src="images/s.gif" class="FP" align="bottom" />
									Adobe Flash Player 9+</li>
								<li><img src="images/s.gif" class="AP" align="bottom" />
									Adobe Acrobat Reader 5+</li>
							</ul>
						</div>
						<div class="login-left-sever">
							<a href="#" onclick="DivDialog.Alert('友情提示','此功能正在开发中...');">
								<img src="images/icon-demo.gif" width="16" height="16"
								align="middle" /> 帮 助
							</a><a href="#"> <img src="images/icon-login-seaver.gif"
								width="16" height="16" align="middle" /> 在线客服
							</a>
						</div>
					</div>
					<div class="login-right">
						<div id="UpdatePanel1">
							<div class="login-right-title">系统组知识管理平台</div>
							<div class="login-right-input">
								<form:errors path="*" cssClass="error" element="p"></form:errors>
								<table width="200" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td nowrap="nowrap" align="right" id="tdLeft">用户名：</td>
										<td><input name="loginName" type="text" id="txtUserName"
											class="input" value="" /></td>
									</tr>
									<tr>
										<td style="height: 30px" align="right">密码：</td>
										<td style="height: 30px"><input name="passWord"
											type="password" id="txtPassword" class="input" /></td>
									</tr>
									<tr>
										<td align="right" id="img">验证码：</td>
										<td style="height: 30px"><input name="j_captcha"
											type="text" id="txtImg" class="imginput" /> <span
											id="captchaImg"><img src="${base}/images/jcaptcha.jpg"
												alt="验证码" height="30px" align="middle" id="videImg" /> </span></td>
									</tr>
									<tr>
										<td></td>
										<td><a href="javascript:refreshCaptcha()">看不清楚换一张</a></td>
									</tr>
									<tr>
										<td nowrap="nowrap" align="right" id="tdLeft">登入方式：</td>
										<td><select name="manner" class="imginput">
												<option value="0" selected="selected">共享</option>
												<option value="1">独有</option>
										</select></td>
									</tr>
									<tr>
										<td></td>
										<td><input type="checkbox" name="rememberMe" /> 两周内记住我</td>
									</tr>
									<tr>
										<td style="height: 30px" align="right"></td>
										<td style="height: 30px" nowrap="nowrap"><input
											type="submit" name="btnLogin" value="登 录"
											onclick="return validateFrm();" id="btnLogin" class="button" />
											<input type="reset" name="btnTest" value="重 置"
											onclick="return autologon();" id="btnTest" tabindex="5"
											class="button" /> <a href="#" onclick="ForgetPassword()">
												<img src="images/login-key.gif" width="12" height="16"
												align="middle" /> 忘记密码
										</a></td>
									</tr>
								</table>
							</div>


						</div>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td id="tdloading"></td>
								<td>
									<div id="UpdateProgress1" style="display: none;">

										<div
											style="position: absolute; top: 0px; left: 0px; width: 100%; height: 100%; z-index: 9999;"
											class="popup-man-tabloading"></div>
										<div id="divLoging"
											style="font-size: 14px; background: #C5C5C5;">
											<div
												style="position: relative; left: -2px; top: -2px; background: #F0F7FA; padding: 4px; border: solid 1px #0C83C1; color: #19538E;">
												<img src="images/Loading.gif" align="middle" />
												操作正在进行中，请等待...
											</div>
										</div>

									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</form:form>
	</center>

	<div id="header">
		<div class="lan"></div>
	</div>
	<div id="content">
		<img src="images/login-wel.gif" />
	</div>
	<div id="buttom">
		<div class="copy2">
			<nobr> Copyright &copy; 2004-2009 solex.com Co., Ltd. All
			Rights Reserved. </nobr>
		</div>
		<div class="copy">
			<img src="images/login-copy.gif" />
		</div>
	</div>
	<%
		if (request.getParameter("error") != null && request.getParameter("error").equals("1")) {
	%>
	<script type="text/javascript">
		$("txtUserName").focus();
		new HintDialog($("txtUserName"), "登录失败，请重试！");
		</script>
	<%
		} else if (request.getParameter("error") != null && request.getParameter("error").equals("3")) {
	%>
	<script type="text/javascript">

		$("txtUserName").focus();
		new HintDialog($("txtUserName"), "系统发现您在别处登录！");
		</script>
	<%
		} else if (request.getParameter("error") != null && request.getParameter("error").equals("5")) {
	%>
	<script type="text/javascript">
		$("txtImg").focus();
		new HintDialog($("txtImg"), "验证码错误！");
	</script>
	<%
		} else if (request.getParameter("error") != null && request.getParameter("error").equals("4")) {
	%>
	<script type="text/javascript">
		$("txtUserName").focus();
		new HintDialog($("txtUserName"), "管理员踢你下线了!");
	</script>
	<%
		}
	%>
</body>
</html>
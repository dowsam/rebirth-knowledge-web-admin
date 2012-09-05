<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>编辑用户</title>
<link href="${base}/css/layoutzhgb2312.css" rel="stylesheet"
	type="text/css" />
<link href="${base}/css/loading.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/js/prototype.js"></script>
<script type="text/javascript" src="${base}/js/Common.js"></script>
<script type="text/javascript" src="${base}/js/Dialog.js"></script>
<script type="text/javascript" src="${base}/js/Utils.js"></script>
<script language="JavaScript" type="text/JavaScript">
        //验证信息
        function validateFrm()
        {
            //判断邮箱
            if (Common.isEmpty("user.email")) {
                new HintDialog($("user.email"), "邮件地址不能为空");
                return false;
            }

            if (!Common.isEmail("user.email")) {
                new HintDialog($("user.email"), "您输入的邮件地址不合法");
                return false;
            }

            if (Common.isEmpty("user.password") && Common.isEmpty("user.password1")) {

            } else {
                if (Common.isEmpty("user.password")) {
                    new HintDialog($("user.password"), "密码不能为空！");
                    return false;
                }

                if (Common.isEmpty("user.password1")) {
                    new HintDialog($("user.password1"), "密码不能为空！");
                    return false;
                }

                if ($("user.password").value != $("user.password1").value) {
                    new HintDialog($("user.password"), "输入的两次密码不相等！");
                    new HintDialog($("user.password1"), "输入的两次密码不相等！");
                    return false;
                }
            }
			return false;

            var url = "user!chUserEditor.html";
            var pas = "a=" + $("user.usernametext").value.trim() + "&b=" + $("user.password").value.trim() + "&c=" + $("user.email").value.trim();

            $("UpdateProgress1").show();
            var ajax = new Ajax.Request(url, {
                method:'post',
                parameters:pas,
                onComplete:function(response) {
                    var jsonData = eval('(' + response.responseText + ')');
                    var msg = document.getElementById("msg");
                    msg.innerHTML = "<div class=\"accurate\"><div class=\"accurate-content\">" + jsonData.msg + "</div></div>";

                    $("UpdateProgress1").hide();
                }
            });

        }
    </script>
</head>
<body onresize="SetTabPageHW('${url}')"
	onload="SetTabPageHW('${url}')">
	<div id="incontent">
		<div>
			<div class="clear"></div>

			<div class="contable">
				<div class="con-ico">
					<div class="con-fl">当前位置：用户信息</div>
					<div class="con-fr">
						<img src="${base}/images/img-fontid.gif" title="本页技术支持编号" />${sysUser.id}
					</div>
				</div>
				<div class="clear"></div>
				<div class="hr"></div>
				<div class="imgadnfont">
					<div class="float">
						<img src="${base}/images/user-info.gif" />
					</div>
					<div style="line-height: 20px;">
						您可以在此修改您的个人信息。<br />其中<span class="blue">邮件地址</span>
					</div>
				</div>
				<div class="clear"></div>
				<div id="UpdateProgress1" style="display: none;">
					<div
						style="position: absolute; width: 100%; height: 100%; z-index: 100;"
						class="popup-man-tabloading" id="divOperateMark"></div>
					<div
						style="position: absolute; right: 50%; top: 180px; background: #C5C5C5; z-index: 10000;">
						<div
							style="position: relative; left: -2px; top: -2px; font-size: 14px; background: #F0F7FA; padding: 6px 8px; border: solid 1px #0C83C1; color: #19538E;">
							<img src="${base}/images/Loading.gif" align="middle" />操作正在进行中，请等待...
						</div>
					</div>

				</div>
				<div id="UpdatePanel1">

					<div id="msg"></div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="content-table">
						<tr>
							<td colspan="4"><span id="star1" class="star">
									以下*号为必填项 </span></td>
						</tr>
						<tr onmouseover="setbg2(this)" onmouseout="setbg1(this)">
							<th colspan="2" class="thleft none">用户信息</th>
						</tr>
						<tr onmouseover="setbg2(this)" onmouseout="setbg1(this)">
							<td width="120" class="tdrightpad0">登入名：</td>
							<td><input name="user.username" disabled="disabled"
								type="text" value="${sysUser.loginName}" id="user.username"
								style="width: 200px;" /></td>
						</tr>
						<tr style="background: #F2F2F2;" onmouseover="setbg2(this)"
							onmouseout="setbg1(this)">
							<td width="120" class="tdrightpad0">姓名：</td>
							<td><input name="user.usernametext" type="text"
								value="${sysUser.userName}" id="user.usernametext"
								style="width: 200px;" /></td>
						</tr>
						<tr onmouseover="setbg2(this)" onmouseout="setbg1(this)">
							<td class="tdrightpad0">密码：</td>
							<td><input name="user.password" type="password"
								id="user.password" style="width: 200px;" /></td>
						</tr>
						<tr style="background: #F2F2F2;" onmouseover="setbg2(this)"
							onmouseout="setbg1(this)">
							<td width="120" class="tdrightpad0">新密码：</td>
							<td><input name="user.password" type="password"
								id="user.password1" style="width: 200px;" /></td>
						</tr>
						<tr onmouseover="setbg2(this)" onmouseout="setbg1(this)">
							<td class="tdrightpad0">邮件地址：</td>
							<td><input name="user.email" type="text"
								value="${sysUser.email}" id="user.email" style="width: 200px;" /> <span
								id="star" class="star">*</span>
							</td>
						</tr>
						<tr>
							<td class="tdrightpad0" id="href-blue">&nbsp;</td>
							<td class="food"><input type="button" name="btnSave"
								value="保  存" onclick="validateFrm();" id="btnSave"
								class="button" /> <input type="button" name="btnCancel"
								value="取  消" id="btnCancel" class="button" />&nbsp;</td>
						</tr>
					</table>


				</div>
			</div>
		</div>
	</div>
</body>
</html>
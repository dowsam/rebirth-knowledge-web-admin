<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="app.title.message" /></title>
<link href="${base}/css/layoutzhgb2312.css" rel="stylesheet"
	type="text/css" />
<link href="${base}/css/leftzhgb2312.css" rel="stylesheet"
	type="text/css" />
<link href="${base}/css/loading.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript"
	src="${base}/js/menu.js"></script>
<script type="text/javascript">
	var baseUrl="${base}";
	var outUrl = "${base}/login/logout";
</script>
<script type="text/javascript" src="${base}/js/web.js"></script>
<script type="text/javascript" src="${base}/js/prototype.js"></script>
<script type="text/javascript" src="${base}/js/MenuOnclickEvent.js"></script>
<script type="text/javascript" src="${base}/js/Utils.js"></script>
<script type="text/javascript" src="${base}/js/DivDialog.js"></script>
<script type="text/javascript" src="${base}/js/Dialog.js"></script>
</head>
<body>
	<div id="layout" style="width: 100%; height: 100%;" class="wrapper">
		<div id="container">
			<form name="Form1" method="post" action="#" id="Form1">
				<table width="100%" border="0" height="100%" cellspacing="0"
					cellpadding="0">
					<tr>
						<td colspan="2" style="background: #EEF2FB;">
							<!-----头部开始----->
							<div id="header">
								<div class="logo">
									<a href="${base}" target="_blank"> <img
										src="${base}/images/logo.gif" width="262" height="64" />
									</a>
								</div>
								<div id="right">
									<table width="50%" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td>
												<div class="welcom" style="height: 20px;">
													<div style="float: right; margin-left: 5px;">
														<input name="top_button" type="button" class="top_button"
															value="退出"
															onclick="DivDialog.Logout(Logout_Msg,'Logou(outUrl)');" />
													</div>

													<div style="float: right; color: #fff; line-height: 19px;">
														<script>
															document
																	.write(Time());
														</script>

														<a
															href="javascript:CreateNewTab('${base}/system/sysUser/${currentUser.id}/edit','sysUser-${currentUser.id}',' 用户信息');"
															title="点击此处修改您的个人信息">${currentUser.userName}</a> ，欢迎您！
													</div>
												</div>
											</td>
										</tr>
									</table>
								</div>
							</div> <!-----头部结束----->
						</td>
					</tr>
					<tr>
						<td colspan="2" align="left" valign="top" class="wrapper">
							<!-----中间开始----->
							<div id="div_Content" class="wrapper">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td id="Div_Left" valign="top" width="196px">
											<!-----左菜单开始-----> <script type="text/javascript"
												src="${base}/index/loadMenu"></script>
											<div style="height: 2px; margin-top: -2px;">
												<img src="${base}/images/menu_bottomimg.gif" />
											</div>
											<div style="height: 2px; margin-top: -2px;">
												<img src="${base}/images/menu_bottomimg.gif" />
											</div>
											<div style="padding-left: 11px; margin-top: 12px;">
												<a href="javascript:SendRequest()"><img
													src="${base}/images/servicezhgb2312.gif" border="0" /> </a>
											</div> <!-----左菜单结束----->
										</td>
										<td valign="top" align="left" class="content">
											<div id="div_Main" class="content">
												<div id="div_Main_Page">
													<!-----导航开始----->
													<div id="nav">
														<div id="tabtitle" class="right">
															<table cellpadding="0" cellspacing="0" border="0">
																<tr>
																	<td width="12"><a href="#" id="ATabLeft"
																		onclick="SelectTabList('left')"> <img
																			id="imgBtnLeft" src="${base}/images/icon-pve.gif"
																			width="12" height="12" border="0" />
																	</a></td>
																	<td style="width: 5px;"></td>
																	<td width="12"><a href="#" id="ATabRight"
																		onclick="SelectTabList('right')"> <img
																			id="imgBtnRight" src="${base}/images/icon-next.gif"
																			width="12" height="12" border="0" />
																	</a></td>
																</tr>
															</table>
														</div>
													</div>
													<!-----导航结束----->
													<!-----主体内容开始----->
													<div>
														<div id="div_Main_Content"></div>
													</div>
													<!-----主体内容结束----->
												</div>
											</div>
										</td>
									</tr>
								</table>
							</div> <!-----中间结束----->
						</td>
					</tr>
					<tr>
						<td colspan="2" valign="top">
							<!-----底部开始----->
							<div id="buttom">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="198"><img src="${base}/images/buttom-left.gif" />
										</td>
										<td class="copyright"><nobr>Copyright &copy;
											2004-2009 solex.com Co., Ltd. All Rights Reserved.</nobr></td>
										<td align="right" width="161"><img
											src="${base}/images/buttom-right.gif" /></td>
									</tr>
								</table>
							</div> <!-----底部结束----->
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		CreateNewTab("${base}/welcome", "Default", "首 页");
		ReturnDefault();
		if (DivDialog.isExistById("currentSite") != null
				&& DivDialog.isExistById("my-site") != null) {
			if ($("currentSite").offsetWidth > ($("my-site").offsetWidth + 2)) {
				$("my-site").style.width = $("currentSite").offsetWidth + "px";
			} else {
				$("currentSite").style.width = $("my-site").offsetWidth + 2
						+ "px";
			}
		}
	</script>
</body>
</html>

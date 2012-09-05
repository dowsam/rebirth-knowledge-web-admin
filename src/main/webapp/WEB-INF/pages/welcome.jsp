<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<link href="${base}/css/layoutzhgb2312.css" rel="stylesheet"
	type="text/css" />
<link href="${base}/css/loading.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/js/prototype.js"></script>
<script type="text/javascript" src="${base}/js/Common.js"></script>
<script type="text/javascript">
	function switchTag(tag, content) {
		for ( var i = 1; i <= 5; i++) {
			if ("tag" + i == tag) {
				document.getElementById(tag).className = "current";
			} else {
				document.getElementById("tag" + i).className = "info-nav-nomer";
			}
			if ("content" + i == content) {
				document.getElementById(content).className = "";
			} else {
				document.getElementById("content" + i).className = "hidecontent";
			}
			document.getElementById(content).className = "";
		}
	}
</script>

</head>
<body onload="SetTabPageHW('Default');"
	onresize="SetTabPageHW('Default');">
	<div id="incontent-wel">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top">
					<div class="left">
						<div class="inwelcome">欢迎登录系统组知识管理平台！</div>
						<div class="note">
							提示：系统组知识管理平台测试版，您现在使用的是全新 <span>SysManage@solex 1.0</span>版本。如果您有何疑问请点击左下角
							<span>在线客服</span>进行咨询。
						</div>
						<div class="info-nav">
							<ul>
								<li id="tag1" class="current"><a style="cursor: pointer;"
									onclick="switchTag('tag1','content1');this.blur();"> 系统信息 </a>
								</li>
								<li id="tag2"><a style="cursor: pointer;"
									onclick="switchTag('tag2','content2');this.blur();"> JVM信息
								</a></li>
								<li id="tag3"><a style="cursor: pointer;"
									onclick="switchTag('tag3','content3');this.blur();"> 进程信息 </a>
								</li>
								<li id="tag4"><a style="cursor: pointer;"
									onclick="switchTag('tag4','content4');this.blur();"> 网络信息 </a>
								</li>
								<li id="tag5"><a style="cursor: pointer;"
									onclick="switchTag('tag5','content5');this.blur();"> 文件信息 </a>
								</li>
							</ul>
							<div class="clear"></div>
							<div></div>
						</div>
						<div class="info">
							<div class="info-in">
								<div id="content">
									<div id="content1" style="height: 250px;">
										<div class="info-title">
											CPU：${osInfo.cpu.vendor}-${osInfo.cpu.model}<span>(MEM：${osInfo.mem.total})(SWAP:${osInfo.swap.total})</span>
										</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="90" align="right">总量MHz：</td>
												<td width="131"><span>${osInfo.cpu.mhz}</span></td>
												<td width="90" align="right">总CPUs：</td>
												<td width="131"><span>${osInfo.cpu.totalCores} </span>
												</td>
											</tr>
											<tr>
												<td width="90" align="right">Physical CPUs：</td>
												<td width="131"><span>${osInfo.cpu.totalSockets}</span>
												</td>
												<td width="90" align="right">Cores per CPU：</td>
												<td width="131"><span>${osInfo.cpu.coresPerSocket}
												</span></td>
											</tr>
										</table>
										<div class="info-title">当前系统信息:</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="90" align="right">运行时间：</td>
												<td width="131"><span>${osStats.uptime}</span>
												</td>
												<td width="90" align="right">系统使用率：</td>
												<td width="131"><span>${osStats.cpu.sys}</span></td>
											</tr>
											<tr>
												<td width="90" align="right">用户使用率：</td>
												<td width="131"><span>${osStats.cpu.user}</span>
												</td>
												<td width="90" align="right">当前空闲率：</td>
												<td width="131"><span>${osStats.cpu.idle}</span></td>
											</tr>
											<tr>
												<td width="90" align="right">内存剩余量：</td>
												<td width="131"><span>${osStats.mem.free}</span>
												</td>
												<td width="90" align="right">内存使用量：</td>
												<td width="131"><span>${osStats.mem.used}</span></td>
											</tr>
											<tr>
												<td width="90" align="right">交换区剩余量：</td>
												<td width="131"><span>${osStats.swap.free}</span>
												</td>
												<td width="90" align="right">交换区使用量：</td>
												<td width="131"><span>${osStats.swap.used}</span></td>
											</tr>
										</table>
									</div>
									<div id="content2" class="hidecontent" style="height: 250px;">
										<div class="info-title">JVM信息:(${jvmInfo.vmName})-(${jvmInfo.version})</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="90" align="right">PID：</td>
												<td width="131"><span>${jvmInfo.pid}</span>
												</td>
												<td width="90" align="right">JVM-version：</td>
												<td width="131"><span>${jvmInfo.vmVersion}</span></td>
											</tr>
											<tr>
												<td width="90" align="right">JVM-vendor：</td>
												<td width="131"><span>${jvmInfo.vmVendor}</span>
												</td>
												<td width="90" align="right">JVM-startTime：</td>
												<td width="131"><span>${jvmInfo.startTime}</span></td>
											</tr>
											<tr>
												<td width="90" align="right">heap init：</td>
												<td width="131"><span>${jvmInfo.mem.heapInit}</span>
												</td>
												<td width="90" align="right">heap max：</td>
												<td width="131"><span>${jvmInfo.mem.heapMax}</span></td>
											</tr>
										</table>
										<div class="info-title">当前JVM信息:</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="90" align="right">运行时间：</td>
												<td width="131"><span>${jvmStats.uptime}</span>
												</td>
												<td width="90" align="right">heap used：</td>
												<td width="131"><span>${jvmStats.mem.heapUsed}</span></td>
											</tr>
											<tr>
												<td width="180" align="right">heap committed：</td>
												<td width="131"><span>${jvmStats.mem.heapCommitted}</span>
												</td>
												<td width="90" align="right">threads：</td>
												<td width="131"><span>${jvmStats.threads.count}</span>
												</td>
											</tr>

										</table>
									</div>
									<div id="content3" class="hidecontent" style="height: 150px;">
										<div class="hidecontent-bg">
											<table border="0" cellspacing="0" cellpadding="0"
												align="center">
												<tr>
													<td width="150" align="right">Id：</td>
													<td width="71"><span>${processInfo.id}</span></td>
													<td width="150" align="right">max file descriptors：</td>
													<td width="71"><span>${processInfo.maxFileDescriptors}</span>
													</td>
												</tr>
												<tr>
													<td align="right">cpu.percent：</td>
													<td><span>${processStats.cpu.percent}</span></td>
													<td align="right">cpu.sys：</td>
													<td><span>${processStats.cpu.sys}</span></td>
												</tr>
												<tr>
													<td align="right">cpu.user：</td>
													<td><span>${processStats.cpu.user}</span></td>
													<td align="right">cpu.total：</td>
													<td><span>${processStats.cpu.total}</span></td>
												</tr>
												<tr>
													<td align="right">mem.resident：</td>
													<td><span>${processStats.mem.resident}</span></td>
													<td align="right">mem.share：</td>
													<td><span>${processStats.mem.share}</span></td>
												</tr>
											</table>
										</div>
									</div>
									<div id="content4" class="hidecontent" style="height: 220px;">
										<div class="info-title">网卡信息:</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="100" align="right">address：</td>
												<td width="71"><span>${networkInfo.address}</span></td>
												<td width="131" align="right">name：</td>
												<td width="71"><span>${networkInfo.name}</span>
												</td>
												<td width="131" align="right">mac_address：</td>
												<td width="71"><span>${networkInfo.macAddress}</span>
												</td>
											</tr>
										</table>
										<div class="info-title">网卡流量:</div>
										<table border="0" cellspacing="0" cellpadding="0"
											align="center">
											<tr>
												<td width="100" align="right">active opens：</td>
												<td width="71"><span>${networkStats.tcp.activeOpens}</span>
												</td>
												<td width="131" align="right">passive opens：</td>
												<td width="71"><span>${networkStats.tcp.passiveOpens}</span>
												</td>
												<td width="131" align="right">curr estab：</td>
												<td width="71"><span>${networkStats.tcp.currEstab}</span>
												</td>
											</tr>
											<tr>
												<td width="100" align="right">in segs：</td>
												<td width="71"><span>${networkStats.tcp.inSegs}</span>
												</td>
												<td width="131" align="right">out segs：</td>
												<td width="71"><span>${networkStats.tcp.outSegs}</span>
												</td>
												<td width="131" align="right">retrans segs：</td>
												<td width="71"><span>${networkStats.tcp.retransSegs}</span>
												</td>
											</tr>
											<tr>
												<td width="100" align="right">estab resets：</td>
												<td width="71"><span>${networkStats.tcp.estabResets}</span>
												</td>
												<td width="131" align="right">attempt fails：</td>
												<td width="71"><span>${networkStats.tcp.attemptFails}</span>
												</td>
												<td width="131" align="right">in errs：</td>
												<td width="71"><span>${networkStats.tcp.inErrs}</span>
												</td>
											</tr>
											<tr>
												<td width="100" align="right">out rsts：</td>
												<td width="71"><span>${networkStats.tcp.outRsts}</span>
												</td>
											</tr>
										</table>
									</div>
									<div id="content5" class="hidecontent" style="height: 220px;">
										<div class="info-title">path:${fsStats.path}</div>
										<div class="hidecontent-bg">
											<table border="0" cellspacing="0" cellpadding="0"
												align="center">
												<tr>
													<td width="20" align="right">total：</td>
													<td width="71"><span>${fsStats.total}</span></td>
													<td width="20" align="right">free：</td>
													<td width="71"><span>${fsStats.free}</span>
													</td>
													<td width="20" align="right">available：</td>
													<td width="71"><span>${fsStats.available}</span>
													</td>
													<td width="20" align="right">diskReads：</td>
													<td width="71"><span>${fsStats.diskReadSizeSize}</span>
													</td>
													<td width="20" align="right">diskWrites：</td>
													<td width="71"><span>${fsStats.diskWriteSizeSize}</span>
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div></td>
				<td align="left" valign="top">
					<div class="right">
						<div style="height: 133px;">
							<div class="news">
								<div class="title">
									<div class="title-left">最新通知</div>
								</div>
								<div
									style="overflow: auto; height: 60px; margin: 10px 10px 10px 0px;">
									<div style="padding: 0px 10px 0px 0px; width: 210px;">
										<ul>
											<li>222</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="news" style="height: 234px;">
							<div class="title">
								<div class="title-left">版本：${aboutUpdate.lblVersion}</div>
								<div class="title-right">
									[ <a
										href="javascript:parent.CreateNewTab('${base}/about.html','about','版本说明');">更多</a>]
								</div>
							</div>
							<div
								style="color: #5E8E00; font-weight: bold; height: 20px; padding-left: 10px; margin-top: 3px;">
								发布时间： <span class="red"><fmt:formatDate
										value="${aboutUpdate.lblUpdatedDate}" /> </span> <br />
							</div>
							<div style="padding: 0px 10px 10px 10px; line-height: 20px;">
								<div style="overflow: auto; height: 173px;">
									<div
										style="padding: 0px 10px 0px 10px; width: 210px; overflow: auto; height: 173px;">
										${aboutUpdate.lblUpdated}</div>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="clear">
			<div class="hr" style="margin-top: 10px;"></div>
			<div class="seaver">
				<img src="${base}/images/icon-mail2.gif" align="middle" /> 客户服务信箱：
				<span id='servicemail'><a href="mailto:xuenong_li@solex.cn">xuenong_li@solex.cn</a>
				</span>
			</div>
			<div class="seaver">
				<img src="${base}/images/icon-phone.gif" width="17" height="14"
					align="middle" /> 客户服务专线： <span> 0592-28839379 </span>
			</div>
		</div>
	</div>
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
</body>
</html>
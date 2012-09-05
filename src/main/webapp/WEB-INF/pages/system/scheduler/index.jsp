<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../../common/taglibs.jsp"%>
<template:override name="title">调度管理</template:override>
<template:override name="location">调度管理</template:override>
<template:override name="button">
	<shiro:hasPermission name="system:scheduler:qrtzTriggersJobDetails:pause">
	<span id="comeback"> <A href="#"
		onclick="javascript:er('pause');return false;"><img
			src="${base}/images/icon/bt_detail.gif" width="16" height="16"
			border=0 align="middle">暂&nbsp;停</A>
	</span>
	</shiro:hasPermission>
	<shiro:hasPermission name="system:scheduler:qrtzTriggersJobDetails:resume">
	<span id="comeback"> <A href="#"
		onclick="javascript:er('resume');"><img
			src="${base}/images/icon/icon_lc.gif" width="16" height="16" border=0
			align="middle">恢复</A>
	</span>
	</shiro:hasPermission>
	<shiro:hasPermission name="system:scheduler:qrtzTriggersJobDetails:delete">
	<span id="del"> <A href="#"
		onclick="javascript:er('delete');return false;"><img
			src="${base}/images/icon/trash.gif" width="16" height="16" border=0
			align="middle"> 删&nbsp;除</A>
	</span>
	</shiro:hasPermission>
	<script type="text/javascript">
		function er(actionUrl){
			var a=${gridObject}.getCheckedRowIds();
			if(a!=null && a!=undefined && a!=""){
				var arr=a.split(",");
				var as=new Array();
				for(var i=0;i<arr.length;i++){
					as.push(${gridObject}.getCellValue(arr[i],"job_name"));
				}
				
				jQuery.getJSON("${base}${requestMappingUrl}/"+actionUrl,{"items":as},function(data){
					if(data.success || data.success=="true"){
						${gridObject}.refresh();
					}
				});
				
			}
		}
	</script>
</template:override>
<%@ include file="/WEB-INF/template/dhtmlxBase.jsp"%>
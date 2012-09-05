<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../../common/taglibs.jsp"%>
<template:override name="title">在线用户</template:override>
<template:override name="location">在线用户</template:override>
<template:sub name="button">
	<span id="update"> <a href="#"
		onclick="javascript:update();return false;"><img
			src="${base}/images/icon/icon_save.gif" width="16" height="16"
			border=0 align="middle" /> 保&nbsp;存</a>
	</span>
</template:sub>
<%@ include file="/WEB-INF/template/dhtmlxBase.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../../common/taglibs.jsp"%>
<template:override name="style">
	<style type="text/css">
.dhx_clist {
	overflow-y: scroll;
	height: 200px;
	z-index: 9999;
}
</style>
</template:override>
<template:override name="title">用户管理</template:override>
<template:override name="location">用户管理</template:override>
<template:override name="dhtmlcontent">
	<dg:tree container="treeboxbox_tree" id="tree" skin="dhx_skyblue"
		imagePath="${base}/js/dhtmlx/imgs/csh_bluebooks/"
		actionHandler="${base}/system/sysGroup/subTree/loadDhtmlxData"
		syn="false" enableCheckBoxes="true">
		<dg:grid container="${gridObjectDiv}" id="${gridObject}"
			columnWidthUnit="px" skin="${skin}" allowCopyData="false"
			enableAutoHiddenColumnsSaving="true" enableOrderSaving="true"
			enableAutoSizeSaving="true" gridType="${gridType}"
			autoHeight="${autoHeigth}">
			<dg:dataSetting actionHandler="${requestMappingUrl}/loadDhtmlxData"
				type="${dataType}"></dg:dataSetting>
			<c:if test="${checkAllColumn}">
				<dg:checkAllColumn width="20" />
			</c:if>
			<c:forEach var="item" items="${columns}">
				<dg:column id="${item.id}" width="${item.width}"
					header="${item.header}" queryExpression="${item.queryExpression}"
					type="${item.type}" columnDataSets="${item.columnDataSets}"
					property="${item.property}" group="${item.group}"
					visible="${item.visible}"></dg:column>
			</c:forEach>
			<dg:dataProcessor actionHandler="${requestMappingUrl}"
				enabled="${dataProcessor}" onAfterUpdate="afterUpdate"
				onBeforeUpdate="beforeUpdate"></dg:dataProcessor>
			<dg:page enabled="${page}" />
		</dg:grid>
	</dg:tree>
</template:override>
<%@ include file="/WEB-INF/template/dhtmlxBase.jsp"%>
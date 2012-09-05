<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="../../common/meta.jsp"%>
<title><template:block name="title" /></title>
<link href="${base}/css/layoutzhgb2312.css" rel="stylesheet"
	type="text/css"></link>
<link href="${base}/css/leftzhgb2312.css" rel="stylesheet"
	type="text/css"></link>
<link href="${base}/css/loading.css" rel="stylesheet" type="text/css"></link>
<link href="${base}/css/icons.css" rel="stylesheet" type="text/css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/dhtmlxtree.css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/dhtmlxgrid.css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/skins/dhtmlxgrid_dhx_skyblue.css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/skins/dhtmlxtoolbar_dhx_skyblue.css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/ext/dhtmlxgrid_pgn_bricks.css" />
<link rel="STYLESHEET" type="text/css"
	href="${base}/js/dhtmlx/rebirth-extend/rebirth-extend.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/pagination.css" />
<style type="text/css">
.dragAccessD {
	background-image: url(${base}/images/icon/false.gif);
	width: 22px;
	height: 22px;
}

.dragAccessA {
	width: 22px;
	height: 22px;
	background-image: url(${base}/images/icon/true.gif);
}
</style>
<template:block name="style"></template:block>
<script type="text/javascript" src="${base}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	var jQuery = $;
</script>
<script type="text/javascript" src="${base}/js/web.js"></script>
<script type="text/javascript" src="${base}/js/prototype.js"></script>
<script type="text/javascript" src="${base}/js/Common.js"></script>
<script type="text/javascript" src="${base}/js/Dialog.js"></script>
<script type="text/javascript" src="${base}/js/Utils.js"></script>
<script type="text/javascript" src="${base}/js/DivDialog.js"></script>
<!--dthmlx lib-->
<script type="text/javascript">
	var Global = {
		"contextPath" : "${base}"
	};
</script>
<script src="${base}/js/dhtmlx/dhtmlxcommon.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxgrid.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxgridcell.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxtree.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxtree_json.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_srnd.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_splt.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_pgn.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_hmenu.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_mcol.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_filter.js"></script>
<script src="${base}/js/dhtmlx/ext/json.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_nxml.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_ssc.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_selection.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_json.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_drag.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlx_extdrag.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_math.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_group.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_keymap_excel.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxgrid_undo.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_sub_row.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxdataprocessor.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxtreegrid.js"></script>
<script src="${base}/js/dhtmlx/dhtmlxtoolbar.js"></script>
<script src="${base}/js/dhtmlx/ext/dhtmlxtreegrid_lines.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_cntr.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_grid.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_link.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_acheck.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_clist.js"></script>
<script src="${base}/js/dhtmlx/excells/dhtmlxgrid_excell_tree.js"></script>
<script src="${base}/js/dhtmlx/rebirth-extend/dhtmlxgrid_ext.js"></script>
<script src="${base}/js/dhtmlx/rebirth-extend/dhtmlxgrid_cell_ext.js"></script>
<script
	src="${base}/js/dhtmlx/rebirth-extend/dhtmlxgrid_dhx_skyblue_pgn.js"></script>
<!--end-->
<script type="text/javascript">
	//sort
	function customColumnSort(ind, type, direction) {
		var colId = (${scriptObject}.getColumnId(ind));
		var notSortFields="${notSortFields}".split(",");
		if (colId == "_checkAll")
			return false;
		for(var i=0;i<notSortFields.length;i++){
			if(colId==notSortFields[i])return false;
		}
		var a_state = ${scriptObject}.getSortingState();
		var d = ((a_state[1] == "des") ? "asc" : "desc");
		${scriptObject}
				.clearAndLoad(
						"${base}${requestMappingUrl}/loadDhtmlxData?sort.orders[0].property="+colId+"&sort.orders[0].direction="+d,
						function() {
									${scriptObject}.enableHeaderMenu();
									${scriptObject}.enableHeaderMenu();
									${scriptObject}.loadSizeFromCookie();
									${scriptObject}.loadOrderFromCookie();
									<c:forEach var="item" items="${columns}" varStatus="status">
										<c:if test="${item.group}">
										${scriptObject}.groupBy(${status.index});
										</c:if>
									</c:forEach>
									
						}, "${dataType}");
				${scriptObject}.setSortImgState(true, ind, d == "desc" ? "des" : "asc");
		return true;
	}
	//
	function not_empty(value, id, ind) {
    	return value != "";
	}
	function greater(value, id, ind) {
		if(value==""){
			return false;
		}
		var tempValue=parseInt(value,10);
		if(isNaN(tempValue)){
			return false;
		}
		return true;
	}
	//
	function add(){
		var param=new Array();
		var colNum=${scriptObject}.getColumnsNum();
		for(var i=0;i<colNum;i++){
			param.push("");
		}
		var selectId=${scriptObject}.getSelectedId();
		if(selectId){
			if("dhtmlxTreeGrid"=="${gridType}"){
				${scriptObject}.openItem(selectId);
			}
			${scriptObject}.addRow((new Date()).valueOf(),param,0,selectId);
		}else{
			${scriptObject}.addRow((new Date()).valueOf(),param,0);
		}
	}
	function del(){
		${scriptObject}.deleteSelectedItem();
	}
	function update(){
		${scriptObject}DataProcessor.sendData();
	}
	function undo(){
		${scriptObject}.doUndo();
	}
	function redo(){
		${scriptObject}.doRedo();
	}
	function p(){
		${scriptObject}.printView();
	}
	var dataProcessorUrl="${base}${requestMappingUrl}";
	<c:if test="${sub}">
		dataProcessorUrl="${base}${sub_requestMappingUrl}";
	</c:if>
	function afterUpdate(sid,action,tid,xml_node){
		 DivDialog.RemoveOperating();
		 this.serverProcessor=dataProcessorUrl;
         return true;
	}
	function beforeUpdate(id,status){
		if("inserted"==status){
			return true;
		}else if("updated"==status){
			this.serverProcessor=this.serverProcessor+"/"+id+"?id="+id+"&_method=put";
		}else if("deleted"==status){
			this.serverProcessor=this.serverProcessor+"/"+id+"?_method=delete&id="+id;
		}else{
			return false;
		}
		DivDialog.Operating();
		return true;
	}
</script>
</head>
<body onresize="SetTabPageHW('${dhtmlUrl}')"
	onload="SetTabPageHW('${dhtmlUrl}')">
	<div id="incontent">
		<div class="contable">
			<div class="con-ico">
				<div class="con-fl">
					当前位置：
					<template:block name="location" />
				</div>
				<div class="con-fr">
					<template:block name="button">
						<c:forEach items="${buttons}" var="item">
							<c:choose>
								<c:when test="${item.auth==null || item.auth==''}">
									<dg:button buttonEntities="${item.buttonEntities}" />
								</c:when>
								<c:otherwise>
									<shiro:hasPermission name="${item.auth}">
										<dg:button buttonEntities="${item.buttonEntities}" />
									</shiro:hasPermission>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</template:block>
				</div>
			</div>
			<div class="clear"></div>
			<div class="hr"></div>
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
		<div id="UpdatePanel1">
			<template:block name="dhtmlcontent">
				<dg:grid container="${gridObjectDiv}" id="${gridObject}"
					columnWidthUnit="px" skin="${skin}" allowCopyData="true"
					enableAutoHiddenColumnsSaving="false" enableOrderSaving="false"
					enableAutoSizeSaving="false" gridType="${gridType}"
					autoHeight="${autoHeigth}" enableUndoRedo="${enableUndoRedo}">
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
					<c:if test="${sub}">
						<dg:grid container="${sub_gridObjectDiv}" id="${sub_gridObject}"
							columnWidthUnit="px" skin="${sub_skin}" allowCopyData="true"
							enableAutoHiddenColumnsSaving="false" enableOrderSaving="false"
							enableAutoSizeSaving="false" gridType="${sub_gridType}"
							autoHeight="${sub_autoHeigth}"
							enableUndoRedo="${sub_enableUndoRedo}">
							<dg:dataSetting
								actionHandler="${sub_requestMappingUrl}/loadDhtmlxData"
								type="${sub_dataType}"></dg:dataSetting>
							<c:if test="${sub_checkAllColumn}">
								<dg:checkAllColumn width="20" />
							</c:if>
							<c:forEach var="item" items="${sub_columns}">
								<dg:column id="${item.id}" width="${item.width}"
									header="${item.header}"
									queryExpression="${item.queryExpression}" type="${item.type}"
									columnDataSets="${item.columnDataSets}"
									property="${item.property}" group="${item.group}"
									visible="${item.visible}"></dg:column>
							</c:forEach>
							<dg:dataProcessor actionHandler="${sub_requestMappingUrl}"
								enabled="${sub_dataProcessor}" onAfterUpdate="afterUpdate"
								onBeforeUpdate="beforeUpdate"></dg:dataProcessor>
							<dg:page enabled="${sub_page}" />
						</dg:grid>
					</c:if>
				</dg:grid>
			</template:block>
			<template:block name="initAfterScript">
				<script type="text/javascript">
			</script>
			</template:block>
		</div>
	</div>
</body>
</html>
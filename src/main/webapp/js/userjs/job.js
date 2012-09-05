
/*******************************************************************************
 * 工作计划模块的javascript
 */
/*******************************************************************************
 * 计划进度的描述（新增）
 */
function tabs_new(obj, arrobj) {
	try {
		if (!isObject(obj)) {
			return false;
		}
		obj.hY(obj.uid(), arrobj, 0);
		// gridbox1.hY(gridbox1.uid(), [0, "\u8bf7\u8f93\u5165...", "",
		// parent.domainUser[1], DateUtils.getDateYMD()], 0);
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/** *全选与反选** */
function tabs_selectAll(obj, check) {
	try {
		if (typeof(obj) != "object") {
			parent.jQuery.messager.show("友情提示", "传入的数据不为对象！");

			return false;
		}
		obj.checkAll(check);
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * 删除
 * 
 * @param {}
 *            obj grid 的对象
 * @param {}
 *            objvalue grid对象的名称
 * @return {Boolean}
 */
function tabs_del(obj, objvalue) {
	try {
		if (!isObject(obj)) {
			return false;
		}
		var rIds = obj.aiZ(0);
		if (rIds != "") {
			parent.DivDialog
					.Confirm(
							"友情提示",
							"是否确定要删除所选的数据",
							"document.getElementById(loadingIframeId).contentWindow."
									+ objvalue
									+ ".delaIz('"
									+ rIds
									+ "');RemoveObj('DivConfirm');RemoveObj('DivMark');");
		}
	} catch (e) {
		parent.DivDialog.error(e);
	}
}
/*******************************************************************************
 * 远程删除附件 AJAX调用删除
 * 
 * @param {}
 *            rIds
 */
function delServerHaner(rIds) {
	try {

	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/** *导入** */
function tabs_importxls(objstr) {
	try {
		if (objstr == "" || objstr == null || objstr == undefined) {
			parent.jQuery.messager.show("友情提示", "传入的数据不存在!");
			return false;
		}
		parent.DivDialog
				.Excelv2(
						"请选择要上传Excel表格文件",
						"请上传Excel表格文件",
						"document.getElementById(loadingIframeId).contentWindow.impExl(document.getElementById('uploadfile'),document.getElementById(loadingIframeId).contentWindow."
								+ objstr
								+ ");RemoveObj('Excelv2');RemoveObj('DivMark');");
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/** *读取exl表格的数据** */
function impExl(obj, gridobj) {
	var oWB;
	var oXl;
	try {
		if (typeof(obj) != "object") {
			parent.jQuery.messager.show("友情提示", "传入的数据不为对象！");
			return false;
		}
		if (obj.value == "") {
			parent.jQuery.messager.show("友情提示", "上传的文件为空!");
			return false;
		}
		if (!isObject(gridobj)) {
			return false;
		}
		if (gridobj instanceof D) {
			var gridRowsCount = gridobj.getRowsNum();
			var gridColsCount = gridobj.getColumnCount();
			var filePath = obj.value;
			var fileType = filePath.substring(filePath.lastIndexOf(".") + 1,
					filePath.length);
			if ("xls" != fileType) {
				parent.jQuery.messager
						.show("友情提示", "上传的文传格式不对,请重新上传ecexl表格文件!");
				return false;
			}
			filePath = filePath.replace("\\", "\\\\");
			try {
				oXl = new ActiveXObject("Excel.Application");
			} catch (e) {
				parent.jQuery.messager.show("友情提示",
						"请确认你的电脑上面是否装有execl软件或者请检查是否把该站点添加受信认的站点！");
				return false;
			}
			oWB = oXl.Workbooks.open(filePath);
			var oSheet = oWB.ActiveSheet;
			var colcount = oWB.Worksheets(1).UsedRange.Cells.Rows.Count;
			var colcolumn = oWB.Worksheets(1).UsedRange.Columns.Count;
			if (colcount == 0) {
				parent.jQuery.messager.show("友情提示", "表格当中没有数据!");
				return false;
			}
			for (var i = 1; i <= colcount; i++) {
				var arr = new Array();
				for (var j = 1; j <= gridColsCount - 1; j++) {
					arr.push(isNull(oSheet.Cells(i, j).value));
				}
				arr.unshift(0);// 删除数组第一个元素，并把新元素放入第一位
				arr.pop();// 删除数组最后一个元素，数组长度减一
				arr.pop();
				arr.push(parent.domainUser[1]);
				arr.push(DateUtils.getDateYMD());
				gridobj.hY(gridobj.uid(), arr);
			}
		} else {
			parent.jQuery.messager.show("友情提示", "传入的对象不是dhtmlx grid的实例");
		}
	} catch (e) {
		parent.DivDialog.error(e);
	} finally {
		if (oWB != undefined || oWB != null) {
			oWB.close();
		}
	}
}
/**
 * 判断是否为null,如果是返回空,不是的话返回obj
 * 
 * @param {}
 *            obj
 * @return {String}
 */
function isNull(obj) {
	if (obj == null || obj == undefined) {
		return "";
	}
	return obj;
}
/*******************************************************************************
 * 导出grid数据
 * 
 * @param obj
 *            传入grid的对象
 */
function tabs_expxls(obj) {
	try {
		if (typeof(obj) != "object") {
			parent.jQuery.messager.show("友情提示", "传入的数据不为对象！");
			return false;
		}
		var form = document.getElementById("form");
		var txt = "";
		var headValue = "";
		var arr = new Array();
		var rowsCount = obj.getRowsNum();
		var colsCount = obj.getColumnCount();
		if (rowsCount == 0) {
			parent.jQuery.messager.show("友情提示", "表格中数据为空！");
			return false;
		}
		for (var i = 0; i < colsCount; i++) {
			headValue = obj.getHeaderCol(i);
			arr[i] = headValue;
		}
		for (var i = 0; i < rowsCount; i++) {
			var rId = obj.aD[i].idd;
			for (var j = 0; j < colsCount; j++) {
				var val = obj.cells(rId, j).getValue();
				txt += "<input type=text name=" + i + "_" + j + " value='"
						+ val + "'/>";
			}
		}
		form.excelHead.value = arr;
		form.rowsCount.value = rowsCount;
		document.getElementById("data").innerHTML = txt;
		// document.all.data.innerHTML = txt;
		form.action = "job!exportExcel.action";
		form.submit();
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * grid 打印 传入dhtmlx grid对象
 * 
 * @param {}
 *            obj
 */
function tabs_print(obj) {
	try {
		if (typeof(obj) != "object") {
			parent.jQuery.messager.show("友情提示", "传入的数据不为对象！");
			return false;
		}
		obj.aem("", "");
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * grid 单击事件弹出上传文件的对话框
 * 
 * @param {}
 *            obj grid 对象
 * @param {}
 *            rId 行号
 * @param {}
 *            cIndex 列号
 * @param {}
 *            fileName 字段的名称
 */
function doRowDblClick(obj, rId, cIndex, fileName, url, objstr,processorstr) {
	try {
		if (!isObject(obj)) {
			return false;
		}
		if (fileName == "" || fileName == null || fileName == undefined) {
			parent.jQuery.messager.show("友情提示", "传入的字段不存在!");
			return false;
		}
		if (obj.Mr(cIndex) == fileName) {
			parent.DivDialog
					.DivUpload(
							"上传附件",
							url,
							"document.getElementById('iframeDivUpload').contentWindow.fileUploadStart('"
									+ rId
									+ "','"
									+ cIndex
									+ "',document.getElementById(loadingIframeId).contentWindow."
									+ objstr + ",document.getElementById(loadingIframeId).contentWindow."+processorstr+");");
			return false
		}
		return true;
	} catch (e) {
		parent.DivDialog.error(e);
	}
}
/*******************************************************************************
 * 更新操作
 * 
 * @param {}
 *            obj dhtmlx grid 对象
 * @param processor
 *            动态保存的对象
 */
function update(obj, processor) {
	try {
		if (!isObject(obj)) {
			return false;
		}
		if (!isObject(processor)) {
			return false;
		}
		obj.callEvent("onValidatationError",function(){
			return false;
		});
		obj.editStop();
		processor.agy();
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * 出错监听事件
 * 
 * @param obj
 */
function myErrorHandler(obj) {
	processor(obj);
	// alert("Error occured.n" + obj.firstChild.nodeValue);
	// alert('d');
	// aad.amF=true;
	parent.DivDialog.Alert("\u51fa\u9519\u4e86", "\u9519\u8bef\u539f\u56e0:"
					+ obj.firstChild.nodeValue);
	return false;
}
/*******************************************************************************
 * 添回监听的事件
 * 
 * @param obj
 */
function myinsertHandler(obj) {
	try {
		parent.jQuery.messager.show("\u53cb\u60c5\u63d0\u793a",
				"\u6570\u636e\u6dfb\u52a0\u6210\u529f!!!");
		return true;
	} catch (e) {
		parent.DivDialog.error(e);
	}
}
/*******************************************************************************
 * 更新监听的事件
 * 
 * @param obj
 */
function myupdateHandler(obj) {
	try {
		parent.jQuery.messager.show("\u53cb\u60c5\u63d0\u793a",
				"\u6570\u636e\u66f4\u65b0\u6210\u529f!!!");
		return true;
	} catch (e) {
		parent.DivDialog.error(e);
	}
}
/*******************************************************************************
 * 删除监听的事件
 * 
 * @param obj
 */
function mydeleteHandler(obj) {
	try {
		parent.jQuery.messager.show("\u53cb\u60c5\u63d0\u793a",
				"\u6570\u636e\u5220\u9664\u6210\u529f!!!");
		return true;
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * 判断是否为对象 如果是对象返回 true，如果不是返回false
 * 
 * @param {}
 *            obj
 * @return {Boolean}
 */
function isObject(obj) {
	try {
		if (typeof(obj) == "object") {
			return true;
		} else {
			parent.jQuery.messager.show("友情提示", "传入的数据不为对象！");
			return false;
		}
	} catch (e) {
		parent.DivDialog.error(e);
	}
}


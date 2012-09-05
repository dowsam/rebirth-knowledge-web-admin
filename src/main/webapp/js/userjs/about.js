/*******************************************************************************
 * 软件更新记录
 ******************************************************************************/

/*******************************************************************************
 * 新增
 */
function add() {
	try {

	} catch (e) {

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
									+ "('"
									+ rIds
									+ "');RemoveObj('DivConfirm');RemoveObj('DivMark');");
		}
	} catch (e) {
		parent.DivDialog.error(e);
	}
}

/*******************************************************************************
 * 
 * @param {}
 *            ids
 */
function delaIz(ids) {
	try {
		var tem = ids.split(",");
		for (var i = 0; i < tem.length; i++) {
			jQuery.post("about!delete.action", {
						id : tem[i].trim()
					}, function(data) {
						parent.jQuery.messager.show("友情提示", data.msg);
					}, "json");
		}
		grid.delaIz(ids);
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

/*******************************************************************************
 * 单击事件
 * 
 * @param {}
 *            rId
 * @param {}
 *            cIndex
 */
function doRowDblClick(rId, cIndex) {
	try {
		window.location.href("about!input.html?id="+rId);
	} catch (e) {
		parent.DivDialog.error(e);
	}
}
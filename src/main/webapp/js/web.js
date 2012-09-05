var LastLeftID = "ChildMenu1";
var LastChild = "";

// 点击刷新
var Dialog_Btn_Refresh = "点击刷新";
// Div对话框提交按钮
var Dialog_Btn_Submit = "确  定";
// Div对话框取消按钮
var Dialog_Btn_Cancel = "取  消";
// Div对话框关闭按钮
var Dialog_Btn_Close = "";
// 退出系统的标题
var Logout_Title = "退出虚拟主机管理系统";
// Logout_Msg
var Logout_Msg = "您确定要退出系统组知识管理平台吗？";
// Save
var Dialog_Btn_Save = "保  存";
// 页面加载中
var Dialog_Loading = "页面载入中，请稍候...";
// 操作进行中
var Dialog_Operating = "操作正在进行中，请等待...";
// 权限值
var CpFeatures = "-1";
// 消息头文字
var SysMsgTitle = "友情提示";
// 消息
var SysMsg = "对不起，您购买的主机类型不支持该操作功能。";

function DoMenu(emid) {
	var obj = document.getElementById(emid);
	obj.className = (obj.className.toLowerCase() == "expanded"
			? "collapsed"
			: "expanded");
	if ((LastLeftID != "") && (emid != LastLeftID)) // 关闭上一个Menu
	{
		document.getElementById(LastLeftID).className = "collapsed";
	}
	LastLeftID = emid;
}

function GetMenuID() {

	var MenuID = "";
	var _paramStr = new String(window.location.href);

	var _sharpPos = _paramStr.indexOf("#");
	if (_sharpPos >= 0 && _sharpPos < _paramStr.length - 1) {
		_paramStr = _paramStr.substring(_sharpPos + 1, _paramStr.length);
	} else {
		_paramStr = "";
	}

	if (_paramStr.length > 0) {
		var _paramArr = _paramStr.split("&");
		if (_paramArr.length > 0) {
			var _paramKeyVal = _paramArr[0].split("=");
			if (_paramKeyVal.length > 0) {
				MenuID = _paramKeyVal[1];
			}
		}
	}
	if (MenuID != "") {
		DoMenu(MenuID)
	}
}

GetMenuID(); // *这两个function的顺序要注意一下，不然在Firefox里GetMenuID()不起效果

function Time() {
	today = new Date();
	var hours = today.getHours();
	var Message;
	if (parseInt(hours) >= 0 && parseInt(hours) <= 11) {
		Message = "早上好";
		return Message;
	} else if (parseInt(hours) > 11 && parseInt(hours) <= 13) {
		Message = "中午好";
		return Message;
	} else if (parseInt(hours) > 13 && parseInt(hours) <= 17) {
		Message = "下午好";
		return Message;
	} else if (parseInt(hours) > 17) {
		Message = "晚上好";
		return Message;
	}
}
function Logou(outUrl) {
	window.location.href = outUrl;
}

function ReturnDefault() {
	var objTop = window.top.document;
	var iframeList = objTop.getElementsByTagName("IFRAME");
	for (var i = 0; i < iframeList.length; i++) {
		var url = iframeList[i].contentWindow.document.URL.toLowerCase();
		if (url.indexOf("login") > -1
				|| url.indexOf("login/logout") > -1) {
			//window.top.location.href = baseUrl+"/login?error=3";
			window.top.location.href=url;
		}
	}
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
			// status=e.clientY-y_;
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
/**
 * 
 * Modification:创建 菜单项点击事件
 * 
 * ...
 */

// 当前正在加载的页面ID
var loadingIframeId = "";
// 当前正在加载的页面Url
var loadingRrl = "";
// 存放TabPage标志(按打开顺序)
var TabPageList = "";
// 存放TabPage标志(按创建顺序顺序)
var TabPages = "";
// 最左边标签页的Id;
var LeftTab = 1;
// 最右边标签页的Id;
var RightTab = 1;
// 显示的ID号
var SelectdId = 1;
// 关闭标志
var CloseTab = 1;
// 进度
var PlanBar = 100;
// 当前进度
var CurrPlan = 0;
// 已经运行的完的时间
var TimeUsed = 0;
// 页面加载标志
var loadingFlag = 0;

var intervalID;
var intervalID2;
var intervalID3;

// 页面跳转
function GotoFunction(url, target) {
	document.getElementById(target).src = url;
}

// 判断该对象是否存在
function ChkObjectIsExists(id) {
	try {
		var iframeList = document.getElementById(id);
		if (iframeList == null || iframeList == "undefined") {
			return false;
		}
		return true;
	} catch (e) {
		return false;
	}
}

function SetIframe() {
	try {
		// window.status += "-" + $(loadingIframeId).contentWindow.document;
		// $(loadingIframeId).style.height =
		// $(loadingIframeId).document.body.scrollHeight + "px";
		// $(loadingIframeId).style.width =
		// $(loadingIframeId).document.body.scrollWidth + "px";
		// alert($(loadingIframeId).contentWindow.document.body.innerHTML);
	} catch (e) {
	}
}

// 创建新的Tab页面
function CreateNewTab(url, id, title) {
	// if(!ChkAuthority(id)){
	// SetOtherTabpageStyle(loadingIframeId,0);
	// DivDialog.Alert(SysMsgTitle,SysMsg);
	// return;
	// }
	loadingRrl = url;
	// 判断该Tab页面是否已被创建，如果已经创建则显示该Tab页面
	if (!ChkObjectIsExists(id)) {
		loadingFlag = 1;
		SetDropList(false, loadingIframeId);
		loadingIframeId = id;
		// 初始化加载页面
		DivDialog.Loading();

		// 增加一个Iframe页面
		var newTab = document.createElement("IFRAME");
		newTab.id = id;
		newTab.frameBorder = 0;
		if (id == "DataBaseAdmin") {
			newTab.scrolling = "auto";
			newTab.className = "inframeStyle";
		} else {
			newTab.scrolling = "no";
		}
		newTab.allowTransparency = "true";
		// 设置Tab页面的宽度和高度;
		$("div_Main_Content").appendChild(newTab);

		// 增加一个Tab标签
		CreateTabTitle(id, title);

		$(id).src = url;
		$(id).style.display = "none";

		if (id != "Default") {
			var preTab = GetPreTabPage();
			if (preTab == "FileManager" || preTab == "DataBaseAdmin"
					|| id == "EnterpriseCard") {
				$(preTab).style.display = "none";
			}
		}

		// 加载页面
		intervalID = setInterval("Loading()", 500);

		// 增加一个Tab页面标示
		TabPageList += "|" + id;
		TabPages += "|cp_tab_Title_Div_" + id;
	} else {
		loadingIframeId = id;
		SetOtherTabpageStyle(id);
	}
}

// 设置其它Tab页面的样式
function SetOtherTabpageStyle(id, flag) {
	loadingIframeId = id;
	SetDropList(true, loadingIframeId);
	var iframeList = document.getElementById("div_Main_Content")
			.getElementsByTagName("iframe");

	for ( var i = 0; i < iframeList.length; i++) {
		if (iframeList[i].id != id) {
			iframeList[i].style.display = "none";
		} else {
			iframeList[i].style.display = "block";
			if (id == "FileManager" || id == "DataBaseAdmin"
					|| id == "EnterpriseCard") {
				iframeList[i].style.width = "100%";
				if (id == "EnterpriseCard") {
					iframeList[i].style.height = "750px";
				} else {
					iframeList[i].style.height = "500px";
				}
			} else {
				iframeList[i].style.width = "100%";
			}

		}
	}

	try {
		// 设置Tab标签的样式
		SetTabTitleStyle(id);
		// 如果是新建页面的话
		if (flag == 1) {
			// 设置可显示的Tab标签
			SetTabPageTitleDisplay();
		}

		ResizeTab();

		// 重新设置Tab页面的宽度
		$("container").style.width = "100px";
		$(loadingIframeId).style.width = "100px";
		var LargLength = 0;
		try {
			if (loadingIframeId != "Default" && loadingIframeId != "LmInfo") {
				var list = $(loadingIframeId).contentWindow.document
						.getElementsByTagName("TABLE");
				for ( var i = 0; i < list.length; i++) {
					if (list[i].className == "content-table") {
						LargLength = list[i].offsetWidth;
						break;
					}
				}
				LargLength += 100;
			}

			if (loadingIframeId == "Default") {
				LargLength = 800;
			}

			if (loadingIframeId == "FileManager") {
				LargLength = 800;
			}

			if (loadingIframeId == "DataBaseAdmin") {
				LargLength = 800;
			}

			if (loadingIframeId == "EnterpriseCard") {
				LargLength = 800;
			}

			if (loadingIframeId == "LmInfo") {
				LargLength = 700;
			}
		} catch (e) {
		}

		LargLength += $("Div_Left").offsetWidth;

		$(loadingIframeId).style.width = "100%";
		if ($("container").offsetWidth < LargLength) {
			$("container").style.width = LargLength + "px";
		} else {
			$("container").style.width = $("layout").offsetWidth + "px";
		}

		if ($("container").offsetWidth < $("layout").offsetWidth) {
			$("container").style.width = ($("layout").offsetWidth) + "px";
		}
	} catch (e) {
	}

	if (loadingIframeId == "FileManager" || loadingIframeId == "DataBaseAdmin"
			|| loadingIframeId == "EnterpriseCard") {
		$(loadingIframeId).style.width = $("div_Main_Content").offsetWidth - 10
				+ "px";
	}

	// 设置打开页面的页面文本框聚焦
	try {
		var list = $(loadingIframeId).contentWindow.document
				.getElementsByTagName("INPUT");
		for ( var i = 0; i < list.length; i++) {
			if (list[i].type == "text") {
				list[i].focus();
				break;
			}
		}
		;
	} catch (e) {
	}
	if (flag) {
		try {
			$(loadingIframeId).contentWindow.initGrid();
		} catch (e) {
		}
	}
	return true;

}

function SetSpecialTabPageWH(id) {
	try {
		var h = 0;
		try {
			// h = $(loadingIframeId).contentWindow.document.body.innerHTML;
			// alert($(id).contentWindow.document.body.innerHTML);
		} catch (e) {
		}
	} catch (e) {
	}
}

// 增加一个Tab标签
function CreateTabTitle(id, title) {
	var tabTitle = document.getElementById("nav");

	// 判断是否已经创建该Tab标签
	try {
		if (!ChkObjectIsExists("cp_tab_Title_Div_" + id)) {
			// 创建新的Tab页面
			var divItem = document.createElement("DIV");
			divItem.id = "cp_tab_Title_Div_" + id;
			divItem.className = "add";
			divItem.innerHTML = "<a href='#' onclick='SetTabTitleStyle(\""
					+ id
					+ "\")'>"
					+ title
					+ "</a>"
					+ "<img src=\"images/icon-nav-close.gif\" border=\"0\"/>"
					+ "<img src=\"images/nav-curr-line.gif\" align=\"absmiddle\" />"
					+ "<div></div>";
			divItem.style.display = "none";
			tabTitle.appendChild(divItem);
		}
	} catch (e) {
	}
}

// 删除一个Tab标签
function DeleteTabTitle(id) {
	// 删除Tab标签
	if (ChkObjectIsExists("cp_tab_Title_Div_" + id)) {
		try {
			// 删除Tab页面标志
			TabPageList = TabPageList.replace("|" + id, "");
			TabPages = TabPages.replace("|cp_tab_Title_Div_" + id, "");

			var delTab = document.getElementById("cp_tab_Title_Div_" + id);
			document.getElementById("nav").removeChild(delTab);

			// 重新设置Tab列表
			loadingIframeId = GetPreTabPage();
			// 设置把最后一个Tab页面设置为显示状态
			SetOtherTabpageStyle(loadingIframeId, 0);

			if ($("cp_tab_Title_Div_" + loadingIframeId).style.display == "none") {
				CloseTab = 2;
			} else {
				CloseTab = 3;
			}
			CloseTabAndSetTabList();

		} catch (e) {
		}
	}
	// 删除Tab页面
	try {
		$("div_Main_Content").removeChild($(id));
	} catch (e) {
	}
}

// 设置把最后一个Tab页面设置为显示状态
function SetLastPageDisplay(id) {
	// 判断该Tab页面是否出于打开状态
	var flag = 0;
	try {
		if ($("cp_tab_Title_Div_" + id).className == "curr") {
			flag = 1;
		}
	} catch (e) {
		flag = 1;
	}

	if (flag == 1) {
		// Tab页面处于显示状态，显示前次打开的页面
		SetTabTitleStyle(GetPreTabPage());

		// 显示最后一个iframe页面
		$(GetPreTabPage()).style.display = "block";
	}

}

function ChangeCloseBtn(id, flag) {
	try {
		var obj = $(id + "_icon_nav_close");
		if (flag == 1) {
			obj.src = "images/icon-nav-close-over.gif";
		} else {
			obj.src = "images/icon-nav-close.gif";
		}
	} catch (e) {
	}
}

// 设置选中Tab标签的样式
function SetTabTitleStyle(id) {
	var divList = document.getElementById("nav").getElementsByTagName("DIV");
	for ( var i = 0; i < divList.length; i++) {
		if (divList[i].className == "curr") {
			var obj = divList[i];
			var list = obj.getElementsByTagName("DIV")[1].innerHTML;
			var close = "<a onmouseover=\"ChangeCloseBtn('"
					+ obj.id
					+ "',1)\" onmouseout=\"ChangeCloseBtn('"
					+ obj.id
					+ "',2)\" href=\"javascript:DeleteTabTitle('"
					+ obj.id.replace("cp_tab_Title_Div_", "")
					+ "')\"><img id=\""
					+ obj.id
					+ "_icon_nav_close\" src=\"images/icon-nav-close.gif\" border=\"0\" /></a>&nbsp;&nbsp;";
			var line = "<img src=\"images/nav-curr-line.gif\" align=\"absmiddle\" /><div></div>";

			if (obj.id == "cp_tab_Title_Div_Default") {
				var temp1 = list.split('>');
				obj.innerHTML = "<img src=\"images/icon-index.gif\" /><a href='#' onclick='SetOtherTabpageStyle(\""
						+ obj.id.replace("cp_tab_Title_Div_", "")
						+ "\")'>"
						+ temp1[1] + "</a>&nbsp;&nbsp;&nbsp;&nbsp;" + line;
			} else {
				var temp2 = list.split('<');
				obj.innerHTML = "<a href='#' onclick='SetOtherTabpageStyle(\""
						+ obj.id.replace("cp_tab_Title_Div_", "") + "\")'>"
						+ temp2[0] + "</a>" + close + line;
			}
			obj.className = "add";
		}
	}
	var selectTab = document.getElementById("cp_tab_Title_Div_" + id);
	selectTab.style.display = "block";
	var text = selectTab.getElementsByTagName("A")[0].innerHTML;

	if (selectTab.id == "cp_tab_Title_Div_Default") {
		text = "<img src=\"images/icon-index.gif\" /> " + text;
	} else {
		text += " <a onmouseover=\"ChangeCloseBtn('"
				+ selectTab.id
				+ "',1)\" onmouseout=\"ChangeCloseBtn('"
				+ selectTab.id
				+ "',2)\" href=\"javascript:DeleteTabTitle('"
				+ id
				+ "')\"><img id=\""
				+ selectTab.id
				+ "_icon_nav_close\" src=\"images/icon-nav-close.gif\" border=\"0\" /></a>";
	}
	selectTab.innerHTML = "<div class=\"curr-left\"><img src=\"images/nav-curr-left.gif\" width=\"3\" height=\"25\" /></div>"
			+ "<div class=\"curr-text\">"
			+ text
			+ "</div>"
			+ "<div class=\"curr-left\"><img src=\"images/nav-curr-right.gif\" width=\"3\" height=\"25\" /></div>";
	selectTab.className = "curr";

	// 重新调整Tab页面次序
	ResetTabPage(id);

	CloseTab = 2;
	ResizeTab();

}

// 获取上一次打开的Tab页面
function GetPreTabPage() {
	var list = TabPageList.split('|');
	return list[list.length - 1]
}

// 重新调整Tab页面次序
function ResetTabPage(id) {
	var list = TabPageList.split('|');
	TabPageList = "";
	for ( var i = 0; i < list.length; i++) {
		if (list[i].length > 0 && list[i] != id) {
			TabPageList = TabPageList + "|" + list[i];
		}
	}
	TabPageList = TabPageList + "|" + id;
}

// 实现页面加载效果
function Loading() {
	if (CurrPlan < PlanBar) {
		var flag = 0;
		try {
			if (CurrPlan > 1) {
				var objIframeInnerHTML = $(loadingIframeId).contentWindow.document.body.innerHTML;
				if (objIframeInnerHTML.length > 0) {
					clearInterval(intervalID);
					if (loadingIframeId == "Default") {
						TimeUsed = CurrPlan;
						CurrPlan = 99;
						flag = 1;
					} else {
						// 完成加载
						CompletePlanBar();
						window.top.ReturnDefault();
					}
				}
			}
		} catch (e) {
		}
		CurrPlan = CurrPlan * 1 + 1;
		if (loadingIframeId == "Default") {
			if (flag == 0) {
				try {
					$("loading_font").innerHTML = CurrPlan + "%";
					$("currplanbar").style.width = CurrPlan + "%";
				} catch (e) {
				}
			}
		}
	}
	if (loadingIframeId == "Default") {
		if (CurrPlan == 100) {
			intervalID2 = setInterval("CompletePlanBar()", 1);
			window.top.ReturnDefault();
		}
	}
}
// 完成进度条
function CompletePlanBar() {
	if (loadingIframeId == "Default") {
		if (TimeUsed < PlanBar) {
			try {
				$("loading_font").innerHTML = TimeUsed + "%";
				$("currplanbar").style.width = TimeUsed + "%";
				TimeUsed = TimeUsed * 1 + 1;
			} catch (e) {
			}
		} else {
			try {
				clearInterval(intervalID2);
				loadingFlag = 0;
				RemoveObj("PageLoading");
				RemoveObj("DivMark");
			} catch (e) {
			}
			CurrPlan = 0;
			TimeUsed = 0;
			// 初始话满意度调查
			// ShowFunction();
			// 设置其它Tab页面为样式
			SetOtherTabpageStyle(loadingIframeId, 1);
		}
	} else {
		CurrPlan = 0;
		TimeUsed = 0;
		loadingFlag = 0;

		RemoveObj("PageLoading");
		RemoveObj("DivMark");
		// 设置其它Tab页面为样式
		SetOtherTabpageStyle(loadingIframeId, 1);
	}
}

// 页面加载点击刷新
function RefreshPage(id, url) {
	$(id).src = url;
	CurrPlan = 0;
	TimeUsed = 0;
	// 加载页面
	intervalID = setInterval("Loading()", 1000);
}

// 设置可显示的Tab标签
function SelectTabList(direction) {
	var CurrentLength = GetTab();
	// 可供显示的总长度
	var totalwidth = $("nav").offsetWidth - $("tabtitle").offsetWidth - 100;
	var list = TabPages.split('|');
	if (direction == "left") {
		if (LeftTab > 1) {
			$(list[LeftTab - 1]).style.display = "block";
			var temp = CurrentLength * 1 + $(list[LeftTab - 1]).offsetWidth;
			if (temp > totalwidth) {
				$(list[RightTab]).style.display = "none";
			}
		}
	}

	if (direction == "right") {
		if (RightTab < (list.length - 1)) {
			$(list[RightTab + 1]).style.display = "block";
			var temp = CurrentLength * 1 + $(list[RightTab + 1]).offsetWidth;
			if (temp > totalwidth) {
				$(list[LeftTab]).style.display = "none";
			}
		}
	}
	// 设置Tab标签移动的按钮
	SetTabMoveBtn();
}
function CloseTabAndSetTabList() {
	var length = GetTab();
	ResizeTab();
	CloseTab = 1;

}
function SetTabPageTitleDisplay() {
	// 可供显示的总长度
	var totalwidth = $("nav").offsetWidth - $("tabtitle").offsetWidth - 100;
	var list = TabPages.split('|');
	var begin = list.length - 1;
	var total = 0;

	for ( var i = begin; i > 0; i--) {
		total += $(list[i]).offsetWidth;
		if (total > totalwidth) {
			$(list[i]).style.display = "none";
		}
	}
}

// 获取当前显示的Tab标签范围
function GetTab() {
	var flag = 0;
	var list = TabPages.split('|');
	var CurrentLength = 0;
	for ( var i = 1; i < list.length; i++) {
		var obj = $(list[i]);
		if (obj.style.display != "none") {
			if (flag == 0) {
				flag = 1;
				LeftTab = i;
			} else {
				RightTab = i;
			}
			CurrentLength += $(list[i]).offsetWidth;
		}
		if (list[i] == "cp_tab_Title_Div_" + loadingIframeId) {
			SelectdId = i;
		}
	}
	return CurrentLength;
}

// 获取投票数据
// function ShowFunction(){
// if(loadingIframeId == "Default"){
// $("iframeFunctionVote").src = "#"+loadingIframeId;
// }
// }

/*
 * * 重新设置Tab页面标签
 */
function ResizeTab() {
	// 当前Tab容器的宽度
	var AlowLength = $("nav").offsetWidth - $("tabtitle").offsetWidth - 100;
	// 现有的Tab标签总宽度
	var CurrentLength = 0;
	// 选中的标签位置
	var currentPos = 0;

	var SelectingTab = $("cp_tab_Title_Div_" + loadingIframeId);
	var list = TabPages.split('|');

	// 隐藏所有的Tab页面标签
	for ( var i = 0; i < list.length; i++) {
		if (list[i].length > 0) {
			$(list[i]).style.display = "none";
			if (currentPos == 0
					&& list[i] == "cp_tab_Title_Div_" + loadingIframeId) {
				currentPos = i;
			}
		}
	}
	if (CloseTab == 2) {
		currentPos = SelectdId;
	}
	if (CloseTab == 3 && LeftTab > 1) {
		currentPos = LeftTab - 1;
	}
	// 向右查询
	for ( var i = currentPos; i < list.length; i++) {
		CurrentLength = GetTab();
		// 如果允许添加新的标签页面
		if (CurrentLength < AlowLength) {
			$(list[i]).style.display = "block";
			RightTab = i;
		}
		CurrentLength = CurrentLength + $(list[i]).offsetWidth;
		if (CurrentLength > AlowLength) {
			$(list[i]).style.display = "none";
			if (i > 1) {
				RightTab = i - 1;
			}
		}
	}

	// 向左查询
	flag = false;
	currentPos = currentPos - 1;
	for ( var i = currentPos; i > 0; i--) {
		CurrentLength = GetTab();
		// 如果允许添加新的标签页面
		if (CurrentLength < AlowLength) {
			$(list[i]).style.display = "block";
			LeftTab = i;
		}
		CurrentLength = CurrentLength + $(list[i]).offsetWidth;
		if (CurrentLength > AlowLength) {
			$(list[i]).style.display = "none";
			if (i < list.length) {
				LeftTab = i + 1;
			}
		}
	}

	// 设置Tab标签移动的按钮
	SetTabMoveBtn();

	// 设置Tab页面的宽度
	if ($("container").offsetWidth < $("layout").offsetWidth) {
		$("container").style.width = $("layout").offsetWidth + "px";
	}

	// divOperateMark
	var divOperateMark = $(loadingIframeId).contentWindow.document
			.getElementById("divOperateMark");
	if (divOperateMark != null && divOperateMark != "undefined") {
		divOperateMark.style.height = $(loadingIframeId).offsetHeight + "px";
	}

}

/*
 * * 设置Tab标签移动的按钮
 */
function SetTabMoveBtn() {
	try {
		var list = TabPages.split('|');
		if ($(list[1]).style.display == "none") {
			$("ATabLeft").style.display = "block";
		} else {
			$("ATabLeft").style.display = "none";
		}
		if ($(list[list.length - 1]).style.display == "none") {
			$("ATabRight").style.display = "block";
		} else {
			$("ATabRight").style.display = "none";
		}
	} catch (e) {
	}
}

/*
 * * 判断权限
 */
function ChkAuthority(id) {
	var AuthorityList = new Array(14);
	var FunctionList = new Array(14);

	FunctionList[0] = "DomainBindings";
	AuthorityList[0] = 0x1;

	FunctionList[1] = "DefaultDocument";
	AuthorityList[1] = 0x2;

	FunctionList[2] = "Redirect";
	AuthorityList[2] = 0x8;

	FunctionList[3] = "CustomError";
	AuthorityList[3] = 0x10;

	FunctionList[4] = "Mimemaps";
	AuthorityList[4] = 0x20;

	FunctionList[5] = "IpSecurity";
	AuthorityList[5] = 0x40;

	FunctionList[6] = "LmInfo";
	AuthorityList[6] = 0x800;

	FunctionList[7] = "FileProtected";
	AuthorityList[7] = 0x100;

	FunctionList[8] = "FileManager";
	AuthorityList[8] = 0x200;

	FunctionList[9] = "DataBaseAdmin";
	AuthorityList[9] = 0x400;

	FunctionList[10] = "Controls";
	AuthorityList[10] = 0x2000;

	FunctionList[11] = "ChildSiteAdmin";
	AuthorityList[11] = 0x4000;

	FunctionList[12] = "BackUp";
	AuthorityList[12] = 0x8000;

	FunctionList[13] = "FileEncrypt";
	AuthorityList[13] = 0x20000;

	for ( var i = 0; i < 14; i++) {
		if (FunctionList[i] == id) {
			if ((CpFeatures & AuthorityList[i]) == AuthorityList[i]) {
				return true;
			} else {
				return false;
			}
		}
	}
	return true;
}
window.onresize = ResizeTab;

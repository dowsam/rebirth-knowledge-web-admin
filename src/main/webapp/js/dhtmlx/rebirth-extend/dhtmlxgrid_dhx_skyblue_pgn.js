function StringBuffer() {
	this.data = [];
	if (arguments.length == 1) {
		this.data.push(arguments[0]);
	}
}
StringBuffer.prototype.append = function() {
	this.data.push(arguments[0]);
	return this;
}
StringBuffer.prototype.toString = function() {
	return this.data.join("");
}
dhtmlXGridObject.prototype.defaultPageSizeList = [ 10, 20, 30, 40, 50 ];

dhtmlXGridObject.prototype.showTotalRecords = false;

dhtmlXGridObject.prototype.setShowTotalRecords = function(showOrHide) {
	this.showTotalRecords = showOrHide;
}

dhtmlXGridObject.prototype.setPageSizeList = function(pageSizeList) {
	this.pageSizeList = pageSizeList;
}
dhtmlXGridObject.prototype.getPageSizeList = function() {
	return this.pageSizeList || this.defaultPageSizeList;
}

/**
 * 初始化分页工具条
 */
dhtmlXGridObject.prototype._pgn_dhx_skyblue = function(current, start, end,
		total) {
	this._pgn_parentObj.className = 'grid_pagerstyle';
	// 构建html
	this._pgn_parentObj.innerHTML = this.createPagingBar(current, start, end,
			total);
	// 给a添加grid属性
	this._pgn_template_active_esms(this._pgn_parentObj);
}
dhtmlXGridObject.prototype._pgn_pgToolbar = function(current, start, end, total) {
	this._pgn_parentObj.className = 'pgToolbar';
	// 构建html
	this._pgn_parentObj.innerHTML = this.createPagingBarPgToolbar(current,
			start, end, total);
	// 给a添加grid属性
	this._pgn_template_active_esms_o(this._pgn_parentObj);
}

dhtmlXGridObject.prototype._pgn_template_active_esms_o = function(block) {
	var selects = block.getElementsByTagName("SELECT");
	if (selects)
		for ( var i = 0; i < selects.length; i++) {
			selects[i].grid = this;
		}
	;
	var divs=block.getElementsByTagName("DIV");
	if(divs){
		for ( var j = 0; j < divs.length; j++) {
			divs[j].grid = this;
		}
	}
	var inputs=block.getElementsByTagName("INPUT");
	if(inputs){
		for ( var k = 0; k < inputs.length; k++) {
			inputs[k].grid = this;
		}
	}
};

dhtmlXGridObject.prototype._pgn_template_active_esms = function(block) {
	var tags = block.getElementsByTagName("A");
	if (tags)
		for ( var i = 0; i < tags.length; i++) {
			tags[i].grid = this;
		}
	;
	var selects = block.getElementsByTagName("SELECT");
	if (selects)
		for ( var i = 0; i < selects.length; i++) {
			selects[i].grid = this;
		}
	;
}

dhtmlXGridObject.prototype.jumpToPage = function() {
	var pageNum = document.getElementById(this.id + '_jumpToTxt').value;
	if (pageNum == '') {
		return;
	}
	if (pageNum < 0 || pageNum != parseInt(pageNum)) {
		alert("页码必须是正整数！");
		return;
	}
	this.changePage(pageNum);
	return false;
}

function __filterNum(e) {
	var _e = window.event || e;
	var keyCode = _e.keyCode || _e.charCode;
	if(keyCode==13){
		return e.grid.jumpToPage();
	}
	// 48-57 0-9
	// 37 left 38 up 39 left 40 down 8 backspace 46 delete
	if (keyCode == 8 || (keyCode >= 37 && keyCode <= 40) || keyCode == 46) {// backspace
		return true;
	}
	if ((keyCode > 95) && (keyCode < 106)) {// 小键盘数字输入
		return true;
	}
	if (_e.keyCode) {// 在IE环境下
		sChar = String.fromCharCode(_e.keyCode);
	} else {// 非IE下
		sChar = String.fromCharCode(_e.charCode);
	}
	return '0123456789'.indexOf(sChar) != -1;
}
dhtmlXGridObject.prototype.createPagingBarPgToolbar = function(current, start,
		end, totalRecords) {
	var buffer = new StringBuffer();
	var pageSize = this.rowsBufferOutSize;
	var totalPages = parseInt((totalRecords + pageSize - 1) / pageSize);
	var _sep = '&nbsp;';
	buffer.append('<div class="pgPanel"><div>');
	buffer
			.append('<select id="'
					+ this.id
					+ '_pageSize_select" class="pgPerPage" onchange="this.grid.changePageSize();">');
	var pageSizeList = this.getPageSizeList();
	var appendPageSize = true;
	for ( var i = 0; i < pageSizeList.length; i++) {// 检查是否已经包含
		if (pageSizeList[i] == pageSize) {
			appendPageSize = false;
			break;
		}
	}
	if (appendPageSize) {
		this.pageSizeList = [ pageSize ].concat(pageSizeList);
	} else if (typeof this.pageSizeList == 'undefined') {
		this.pageSizeList = pageSizeList;
	}
	for ( var i = 0; i < this.pageSizeList.length; i++) {
		buffer.append('<option '
				+ (pageSize == this.pageSizeList[i] ? 'selected="true"' : '')
				+ ' value="' + this.pageSizeList[i] + '">'
				+ this.pageSizeList[i] + '</option>');
	}
	if (this.showTotalRecords || false) {
		buffer.append('<option '
				+ (pageSize == totalRecords ? 'selected="true"' : '')
				+ 'value="' + totalRecords + '">全部</option>');
	}
	buffer.append('</select>&nbsp;</div>');
	buffer.append('<div class="separator"></div>');
	if(this.currentPage==1){
		buffer.append('<div class="pgBtn pgFirstDisabled" title="首页"></div>');
		buffer.append('<div class="pgBtn pgPrevDisabled" title="上页"></div>');
	}else{
		buffer.append('<div class="pgBtn pgFirst" title="首页" onclick="javascript:this.grid.changePage(1);return false;"></div>');
		buffer.append('<div class="pgBtn pgPrev" title="上页" onclick="javascript:this.grid.changePage('+(current-1)+');return false;"></div>');
	}
	buffer.append('<div class="separator"></div>');
	buffer
			.append('<div>第<input class="pgCurrentPage" type="text" id="'+this.id+'_jumpToTxt" name="pageNo" value="'
					+ current + '" title="页码" onkeydown="return __filterNum(this);" />');
	buffer.append('页/共');
	buffer.append('<span class="pgTotalPage">');
	buffer.append(totalPages);
	buffer.append('</span>页</div>');
	buffer.append('<div class="separator"></div>');
	if(this.currentPage==totalPages || totalPages==0){
		buffer.append('<div class="pgBtn pgNextDisabled" title="下页"></div>');
		buffer.append('<div class="pgBtn pgLastDisabled" title="尾页"></div>');
	}else{
		buffer.append('<div class="pgBtn pgNext" title="下页" onclick="javascript:this.grid.changePage('+(current+1)+');return false;"></div>');
		buffer.append('<div class="pgBtn pgLast" title="尾页" onclick="javascript:this.grid.changePage('+totalPages+');return false;"></div>');
	}
	buffer.append('<div class="separator"></div>');
	buffer
			.append('<div class="pgBtn pgRefresh" title="刷新" onclick="javascript:this.grid.refresh();return false;" onmousedown="javascript:addClass(this,\'pgPress\');" onmouseup="javascript:removeClass(this,\'pgPress\');" onmouseout="javascript:removeClass(this,\'pgPress\');"></div>');
	buffer.append('<div class="separator"></div>');
	buffer.append('<div class="pgSearchInfo">');
	buffer.append('检索到&nbsp;');
	buffer.append(totalRecords);
	buffer.append('&nbsp;条记录，显示第&nbsp;');
	buffer.append('<span class="pgStartRecord">' + (totalRecords==0?start:(start+1)) + '</span>');
	buffer.append('&nbsp;条&nbsp;-&nbsp;第&nbsp;');
	buffer.append('<span class="pgEndRecord">' + end + '</span>');
	buffer.append('&nbsp;条记录');
	buffer.append('</div>');
	buffer.append('<hr class="cleanFloat" />');
	// html 拼接
	return buffer.toString();
};
dhtmlXGridObject.prototype.createPagingBar = function(current, start, end,
		totalRecords) {
	var buffer = new StringBuffer();
	var pageSize = this.rowsBufferOutSize;
	var totalPages = parseInt((totalRecords + pageSize - 1) / pageSize);
	var _sep = '&nbsp;';
	buffer
			.append(
					'<div class="pageinfo"><div class="pageinfo_li"><span class="span">共')
			.append(totalPages).append('页/').append(totalRecords).append(
					'条记录 每页</span><span class="span">');
	buffer.append('<select id="' + this.id
			+ '_pageSize_select" onchange="this.grid.changePageSize();">');
	var pageSizeList = this.getPageSizeList();
	var appendPageSize = true;
	for ( var i = 0; i < pageSizeList.length; i++) {// 检查是否已经包含
		if (pageSizeList[i] == pageSize) {
			appendPageSize = false;
			break;
		}
	}
	if (appendPageSize) {
		this.pageSizeList = [ pageSize ].concat(pageSizeList);
	} else if (typeof this.pageSizeList == 'undefined') {
		this.pageSizeList = pageSizeList;
	}
	for ( var i = 0; i < this.pageSizeList.length; i++) {
		buffer.append('<option '
				+ (pageSize == this.pageSizeList[i] ? 'selected="true"' : '')
				+ ' value="' + this.pageSizeList[i] + '">'
				+ this.pageSizeList[i] + '</option>');
	}
	if (this.showTotalRecords || false) {
		buffer.append('<option '
				+ (pageSize == totalRecords ? 'selected="true"' : '')
				+ 'value="' + totalRecords + '">全部</option>');
	}
	buffer.append('</select></span>');
	buffer.append('<span class="span" style="margin-top:-3px;">');
	buffer.append(_sep);
	buffer
			.append(' 跳转<input type="text" id="'
					+ this.id
					+ '_jumpToTxt" onkeydown="return __filterNum();" value="'
					+ current
					+ '" class=""/>页<a href="javascript:void(0);" onclick="return this.grid.jumpToPage();return false;" class="btn_go"><span>GO</span></a>');
	buffer.append(_sep);
	if (this.exportable) {
		buffer
				.append(
						'<a href="javascript:void(0);" onclick=this.grid.refresh(); return false;><span>refresh</span></a>')
				.append(_sep);
	}
	buffer.append('<a href="javascript:void(0);" onclick=this.grid.changePage('
			+ 1 + '); return false;><span>首页</span></a>');
	buffer.append(_sep);
	buffer.append(this._pgn_link_esms('prevpages', '<span>&lt;&lt;</span>',
			_sep));
	buffer.append(_sep);
	buffer.append(this._pgn_block_esms(_sep));
	buffer.append(_sep);
	buffer.append(this._pgn_link_esms('nextpages', '<span>&gt;&gt;</span>',
			_sep));
	buffer.append(_sep);
	buffer.append('<a href="javascript:void(0);" onclick=this.grid.changePage('
			+ totalPages + '); return false;><span>尾页</span></a></span>');
	buffer.append('</div></div>');
	// html 拼接
	return buffer.toString();
}

dhtmlXGridObject.prototype.changePageSize = function(pageSize) {
	var eSelect = document.getElementById(this.id + '_pageSize_select');
	var pageSize = eSelect.options[eSelect.selectedIndex].value;
	if (pageSize == 0)
		return;
	this.rowsBufferOutSize = parseInt(pageSize);
	this.changePage();
}

dhtmlXGridObject.prototype._pgn_block_esms = function(sep) {
	var start = Math.floor((this.currentPage - 1) / this.pagesInGroup)
			* this.pagesInGroup;
	var max = Math.min(Math.ceil(this.rowsBuffer.length
			/ this.rowsBufferOutSize), start + this.pagesInGroup);
	var str = [];
	for ( var i = start + 1; i <= max; i++)
		if (i == this.currentPage)
			str.push("<a href='javascript:void(0);' class='current'><span>" + i
					+ "</span></a>");
		else
			str
					.push("<a href='javascript:void(0);' onclick='this.grid.changePage("
							+ i
							+ "); return false;'><span>"
							+ i
							+ "</span></a>");
	return str.join(sep);
}
dhtmlXGridObject.prototype._pgn_link_esms = function(mode, ac, ds) {
	var _page = 0;
	if (mode == "prevpages" || mode == "prev") {
		if (this.currentPage == 1)
			return ds;
		if (mode == "prevpages") {
			_page = -(this.currentPage % this.pagesInGroup == 0 ? this.pagesInGroup
					: this.currentPage % this.pagesInGroup);
		} else {
			_page = -1;
		}
		return '<a href="javascript:void(0);" onclick=\'this.grid.changePageRelative('
				+ _page + '); return false;\'>' + ac + '</a>'
	}
	if (mode == "nextpages" || mode == "next") {
		if (this.rowsBuffer.length / this.rowsBufferOutSize <= this.currentPage)
			return ds;
		if (this.rowsBuffer.length
				/ (this.rowsBufferOutSize * (mode == "next" ? '1'
						: this.pagesInGroup)) <= 1)
			return ds;
		if (mode == "nextpages") {
			_page = this.currentPage % this.pagesInGroup == 0 ? 1
					: this.pagesInGroup - this.currentPage % this.pagesInGroup
							+ 1;
		} else {
			_page = 1;
		}

		return '<a href="javascript:void(0);" onclick=\'this.grid.changePageRelative('
				+ _page + '); return false;\'>' + ac + '</a>'
	}

	if (mode == "current") {
		var i = this.currentPage + (ac ? parseInt(ac) : 0);
		if (i < 1
				|| Math.ceil(this.rowsBuffer.length / this.rowsBufferOutSize) < i)
			return ds;
		return '<a href="javascript:void(0);" '
				+ (i == this.currentPage ? "class='dhx_active_page_link' " : "")
				+ 'onclick=\'this.grid.changePage(' + i + '); return false;\'>'
				+ i + '</a>';
	}
	return ac;
}
function hasClass(ele, cls) {
	return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}
function addClass(ele, cls) {
	if (!this.hasClass(ele, cls))
		ele.className += " " + cls;
}
function removeClass(ele, cls) {
	if (hasClass(ele, cls)) {
		var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
		ele.className = ele.className.replace(reg, ' ');
	}
}
function toggleClass(ele, cls) {
	if (hasClass(ele, cls)) {
		removeClass(ele, cls);
	} else
		addClass(ele, cls);
}
function changeClass(ele, oldcls, newcls) {
	if (!hasClass(ele, newcls)) {
		if (hasClass(ele, oldcls)) {
			removeClass(ele, oldcls);
		}
		addClass(ele, newcls);
	}
}

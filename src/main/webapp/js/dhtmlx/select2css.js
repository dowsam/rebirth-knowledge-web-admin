var tm2008style = document.getElementById("tm2008style");
var glosname = new Date().getTime();
var isIE = (document.all && window.ActiveXObject && !window.opera) ? true
		: false;

function $(id) {
	return document.getElementById(id);
}

function stopBubbling(ev) {
	ev.stopPropagation();
}

function rSelects() {
	select_tag = document.createElement('div');
	select_tag.id = 'select_' + glosname;
	select_tag.className = 'select_box';
	$("tm2008style").appendChild(select_tag);

	select_info = document.createElement('div');
	select_info.id = 'select_info_' + glosname;
	select_info.className = 'tag_select';
	select_info.style.cursor = 'pointer';
	select_info.innerText = "«Î—°‘Ò";
	select_tag.appendChild(select_info);

	select_ul = document.createElement('ul');	
	select_ul.id = 'options_' + glosname;
	select_ul.className = 'tag_options';
	select_ul.style.position='absolute';
	select_ul.style.display='none';
	select_ul.style.zIndex='999';
	select_tag.appendChild(select_ul);

	rOptions(1, glosname);
	mouseSelects(glosname);

}

function rOptions(i, name) {
	var options_ul = 'options_' + name;
	option_li = document.createElement('li');
	option_li.style.cursor = 'pointer';
	option_li.className = 'open';
	tree = document.createElement("div");
	tree.id = "select_tree";
	option_li.appendChild(tree);
	$(options_ul).appendChild(option_li);
	
		

	//option_li.onclick = new Function("clickOptions(" + i + "," + n + ",'"
			//+ selects[i].name + "')");
}

function mouseSelects(name) {
	var sincn = 'select_info_' + name;

	$(sincn).onmouseover = function() {
		if (this.className == 'tag_select')
			this.className = 'tag_select_hover';
	}
	$(sincn).onmouseout = function() {
		if (this.className == 'tag_select_hover')
			this.className = 'tag_select';
	}

	if (isIE) {
		$(sincn).onclick = new Function("clickSelects('" + name
				+ "');window.event.cancelBubble = true;");
	} else if (!isIE) {
		$(sincn).onclick = new Function("clickSelects('" + name + "');");
		$('select_info_' + name).addEventListener("click", stopBubbling, false);
	}

}

function clickSelects(name) {
	var sincn = 'select_info_' + name;
	var sinul = 'options_' + name;
	if ($(sincn).className == 'tag_select_hover') {
		$(sincn).className = 'tag_select_open';
		$(sinul).style.display = '';
	} else if ($(sincn).className == 'tag_select_open') {
		$(sincn).className = 'tag_select_hover';
		$(sinul).style.display = 'none';
	}
}

function clickOptions(i, n, name) {
	var li = $('options_' + name).getElementsByTagName('li');

	$('selected_' + name).className = 'open';
	$('selected_' + name).id = '';
	li[n].id = 'selected_' + name;
	li[n].className = 'open_hover';
	$('select_' + name).removeChild($('select_info_' + name));

	select_info = document.createElement('div');
	select_info.id = 'select_info_' + name;
	select_info.className = 'tag_select';
	select_info.style.cursor = 'pointer';
	$('options_' + name).parentNode.insertBefore(select_info,
			$('options_' + name));

	mouseSelects(name);

	$('select_info_' + name).appendChild(
			document.createTextNode(li[n].innerHTML));
	$('options_' + name).style.display = 'none';
	$('select_info_' + name).className = 'tag_select';
	selects[i].options[n].selected = 'selected';

}

//window.onload = function(e) {
//	bodyclick = document.getElementsByTagName('body').item(0);
//	rSelects();
//	var select_tree = new dhtmlXTreeObject("select_tree","250px","218px",0);
//	select_tree.setSkin("dhx_skyblue");
//	select_tree.setImagePath("" + path + "/comparison/js/imgs/csh_vista/");
//	select_tree.setDataMode("json");
//	select_tree
//			.loadJSON(""
//					+ path
//					+ "/comparison/comparisonAction.do?method=configTreeJson&id=0&syn=0");
//	bodyclick.onclick = function() {
//		//$('select_info_' + glosname).className = 'tag_select';
//		//$('options_' + glosname).style.display = 'none';
//	}
//}
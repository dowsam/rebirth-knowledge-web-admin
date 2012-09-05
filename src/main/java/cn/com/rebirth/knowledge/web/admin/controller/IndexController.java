/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: IndexController.java 2011-6-25 0:58:09 l.xue.nong$$
*/
package cn.com.rebirth.knowledge.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.rebirth.commons.utils.ResponseTypeOutputUtils;
import cn.com.rebirth.commons.utils.ServletUtils;
import cn.com.rebirth.commons.utils.ShortUrlGenerator;
import cn.com.rebirth.core.web.controller.AbstractBaseController;
import cn.com.rebirth.knowledge.commons.annotation.Resource;
import cn.com.rebirth.knowledge.commons.entity.system.SysResourceEntity;
import cn.com.rebirth.knowledge.commons.entity.system.SysUserEntity;
import cn.com.rebirth.knowledge.web.admin.service.SystemService;

/**
 * The Class IndexController.
 */
@Controller
@RequestMapping("/index")
@Resource(names = "首页", openMenu = false, showMenu = false)
public class IndexController extends AbstractBaseController {

	/** The system service. */
	@Autowired
	private SystemService systemService;

	/**
	 * Index.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "/index";
	}

	/**
	 * Load menu.
	 *
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/loadMenu")
	public void loadMenu(HttpServletRequest request, HttpServletResponse response) {
		SysUserEntity sysUser = systemService.getCurrentUser();
		List<SysResourceEntity> resourceList = systemService.loadMenu();
		String main = "var domainUser=new Array();domainUser[0]='" + sysUser.getId() + "';domainUser[1]='"
				+ sysUser.getUserName() + "';GV.rootMenu =[";
		String scripts = "";
		StringBuffer b = new StringBuffer();
		StringBuffer a = new StringBuffer();
		bulidMenuScript(b, a, resourceList, request);
		scripts = b.toString();
		main = main + a.toString();
		main = main.substring(0, main.lastIndexOf(","));
		main = main + "];";
		main = "var GV = new GlobalVar();\n" + "GV.title = \"\";\n" + "GV.td_target = \"\";\n"
				+ "GV.href_target = \"\";" + main;
		scripts = scripts
				+ "createMenu();"
				+ "function GlobalVar(){\n"
				+ "\tthis.rootMenu = new Object();\n"
				+ "\tthis.menus = new Array();\n"
				+ "\tthis.title = null;\n"
				+ "\tthis.td_target = null;\n"
				+ "\tthis.href_target = null;\n"
				+ "}"
				+ "function createMenu(){\n"
				+ "\tvar name,url,currIndex,currOverClass,currOutClass,extended,tdID;\n"
				+ "\tvar subName,subUrl,subOverClass,subOutClass,subTdId;\n"
				+ "\tvar sHtml = \"\";\n"
				+ "\tsHtml += '<div style=\"position:absolute; left:189px;\"><img src=\""
				+ getServerPath(request)
				+ "/images/left-top-right.gif\" /></div>';\n"
				+ "\tsHtml += '<img src=\""
				+ getServerPath(request)
				+ "/images/menu_topimg.gif\"></div>';\n"
				+ "\n"
				+ "\tsHtml += '<table width=\"182\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:url("
				+ getServerPath(request)
				+ "/images/menu_linebg.gif)\">';\n"
				+ "\tsHtml += '\t<tr><td class=\"menuTitle\">' + GV.title + '</td></tr>';\n"
				+ "\tfor(var i=0; i<GV.rootMenu.length; i++){\n"
				+ "\t\tname = GV.rootMenu[i][0];\turl = GV.rootMenu[i][1];\tcurrIndex = GV.rootMenu[i][2];\n"
				+ "\t\tcurrOverClass = GV.rootMenu[i][3];\tcurrOutClass = GV.rootMenu[i][4];\textended = GV.rootMenu[i][5];\n"
				+ "\t\ttdID = \"rootMenu_\" + i;\n"
				+ "\t\tsHtml += ' <tr>';\n"
				+ "\n"
				+ "\t\t//链接为加在单元格上时将调用以下代码\n"
				+ "\t\tsHtml += '<td' + (currOutClass?' class=\"' + (extended?'cTD' + currOverClass:'oTD' + currOutClass) + '\"':'') + ' id=\"' + tdID + '\" onclick=\"setBg(\\'' + tdID + '\\',\\'' + currOverClass + '\\',\\'' + tdID + '\\'); hideDiv(\\'subMenu_' + i + '\\');' + ((url)?((GV.td_target)?GV.td_target + '.':'') + 'location.href=\\'' + url + '\\';':'') + ';\"';\n"
				+ "\t\tsHtml += ' onmouseover=\"over(\\'' + tdID + '\\',\\'' + currOverClass + '\\',\\'' + tdID + '\\');\" onmouseout=\"out(\\'' + tdID + '\\',\\'' + currOutClass + '\\',\\'' + tdID + '\\');\">'\n"
				+ "\t\tsHtml += '' + name + '</td>';\n"
				+ "\n"
				+ "\t\t/*//链接为加在文字上时调用以下代码\n"
				+ "\t\tsHtml += '<td' + (currOutClass?' class=\"' + (extended?'cTD' + currOverClass:'oTD' + currOutClass) + '\"':'') + ' id=\"' + tdID + '\" onclick=\"setBg(\\'' + tdID + '\\',\\'' + currOverClass + '\\',\\'' + tdID + '\\');setDefault(\\'subMenu_' + i + '_0\\',\\'2\\');hideDiv(subMenu_' + i + ');\" onmouseover=\"over(\\'' + tdID + '\\',\\'' + currOverClass + '\\',\\'' + tdID + '\\');\" onmouseout=\"out(\\'' + tdID + '\\',\\'' + currOutClass + '\\',\\'' + tdID + '\\');\">';\n"
				+ "\t\tsHtml += ((url)?'<a href=\"' + url + '\"' + ((GV.href_target)?' target=\"' + GV.href_target + '\"':'') + '>' + name + '</a>':name) + '</td>';*/\n"
				+ "\n"
				+ "\t\tsHtml += '</tr>';\n"
				+ "\n"
				+ "\t\tsHtml += ' <tr><td>';\n"
				+ "\t\tsHtml += '<div id=\"subMenu_' + i + '\" style=\"display:' + (extended?'block':'none') + '\">';\n"
				+ "\t\t//sHtml += '<div id=\"subMenu_' + i + '\" style=\"display:block\">';\n"
				+ "\t\tsHtml += '<img src=\""
				+ getServerPath(request)
				+ "/images/menu_topline.gif\">';\n"
				+ "\t\tsHtml += '<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#ff3300; height:25px;\">';\n"
				+ "\t\tif(typeof(GV.menus[currIndex])==\"object\"){\n"
				+ "\t\tfor(var j = 0; j<GV.menus[currIndex].length; j++){\n"
				+ "\t\t\tsubName = GV.menus[currIndex][j][0]; subUrl = GV.menus[currIndex][j][1];\n"
				+ "\t\t\tsubOverClass = GV.menus[currIndex][j][2]; subOutClass = GV.menus[currIndex][j][3]\n"
				+ "\t\t\tsubTdId = \"subMenu_\" + i + \"_\" + j;\n"
				+ "\t\t\tsHtml += '  <tr>';\n"
				+ "\n"
				+ "\t\t\t/*//链接加在文字上时调用以下链接\n"
				+ "\t\t\tsHtml += '<td' + (subOutClass?' class=\"oTD' + subOutClass + '\"':'') + ' id=\"' + subTdId + '\" onclick=\"setBg(\\'' + subTdId + '\\',\\'' + subOverClass + '\\',\\'' + tdID + '\\');\" onmouseover=\"over(\\'' + subTdId + '\\',\\'' + subOverClass + '\\',\\'' + tdID + '\\');\" onmouseout=\"out(\\'' + subTdId + '\\',\\'' + subOutClass + '\\',\\'' + tdID + '\\');\">';\n"
				+ "\t\t\tsHtml += ((subUrl)?'<a href=\"' + subUrl + '\"' + ((GV.href_target)?' target=\"' + GV.href_target + '\"':'') + '>' + subName + '</a>':subName) + '</td>';*/\n"
				+ "\n"
				+ "\t\t\t//链接加在单元格时上时调用以下链接\n"
				+ "\t\t\t//sHtml += '<td' + (subOutClass?' class=\"oTD' + subOutClass + '\"':'') + ' id=\"' + subTdId + '\" onclick=\"setBg(\\'' + subTdId + '\\',\\'' + subOverClass + '\\',\\'' + tdID + '\\');' + ((subUrl)?((GV.td_target)?GV.td_target + '.':'') + 'location.href=\\'' + subUrl + '\\';':'') + '\"';\n"
				+ "\t\t\tsHtml += '<td' + (subOutClass?' class=\"oTD' + subOutClass + '\"':'') + ' id=\"' + subTdId + '\" onclick=\"setBg(\\'' + subTdId + '\\',\\'' + subOverClass + '\\',\\'' + tdID + '\\');' + ((subUrl)?((GV.td_target)?GV.td_target + '':'') + subUrl + ';':'') + '\"';\n"
				+ "\t\t\tsHtml += ' onmouseover=\"over(\\'' + subTdId + '\\',\\'' + subOverClass + '\\',\\'' + tdID + '\\');\" onmouseout=\"out(\\'' + subTdId + '\\',\\'' + subOutClass + '\\',\\'' + tdID + '\\');\">';\n"
				+ "\t\t\tsHtml += '' + subName + '</td>';\n"
				+ "\n"
				+ "\t\t\tsHtml += '</tr>';\n"
				+ "\t\t}\n"
				+ "\t\t}\n"
				+ "\t\tsHtml += '</table>';\n"
				+ "\t\tsHtml += '</div>';\n"
				+ "\t\tsHtml += '</td></tr>';\n"
				+ "\t}\n"
				+ "\tsHtml += ' </table>';\n"
				+ "\t//setMouseEvent(\"menu\");\n"
				+ "\tdocument.write(sHtml);\n"
				+ "}"
				+ "var cObj,sObj,lastrCSS,lastCSS;"
				+ "function over(id,css,rObj){\t\t//移上操作\n"
				+ "\tvar obj = document.getElementById(id);\n"
				+ "\tobj.className = \"cTD\" + css;\n"
				+ "}"
				+ "function out(id,css,rObj){\t\t//移走操作\n"
				+ "\tvar obj = document.getElementById(id);\n"
				+ "\tvar rObj = document.getElementById(rObj);\n"
				+ "\t\n"
				+ "\tif(obj == rObj){\t//属于目录操作\n"
				+ "\t\tif(sObj == undefined){\n"
				+ "\t\t\tobj.className = \"oTD\" + css;\n"
				+ "\t\t}else{\n"
				+ "\t\t\tif(sObj == rObj){\n"
				+ "\t\t\t\tobj.className = \"sTD\" + lastrCSS;\n"
				+ "\t\t\t}else{\n"
				+ "\t\t\t\tobj.className = \"oTD\" + css;\n"
				+ "\t\t\t}\n"
				+ "\t\t}\n"
				+ "\t}else{\n"
				+ "\t\tif(cObj == undefined){\n"
				+ "\t\t\tobj.className = \"oTD\" + css;\n"
				+ "\t\t}else{\n"
				+ "\t\t\tif(cObj == obj){\n"
				+ "\t\t\t\tobj.className = \"sTD\" + lastCSS;\n"
				+ "\t\t\t}else{\n"
				+ "\t\t\t\tobj.className = \"oTD\" + css;\n"
				+ "\t\t\t}\n"
				+ "\t\t}\n"
				+ "\t}\n"
				+ "}"
				+ "function setBg(id,css,rObj){\t\t//点击操作\n"
				+ "\tvar obj = document.getElementById(id);\n"
				+ "\tvar rObj = document.getElementById(rObj);\n"
				+ "\t\n"
				+ "\tif(obj == rObj){\t\t//属于父目录操作\n"
				+ "\t\tif(sObj != undefined){\n"
				+ "\t\t\tsObj.className = \"oTD\" + lastrCSS;\n"
				+ "\t\t}\n"
				+ "\t\trObj.className = \"sTD\" + css;\n"
				+ "\t\tif(cObj != undefined){\n"
				+ "\t\t\tcObj.className = \"oTD\" + lastCSS;\n"
				+ "\t\t\tcObj = undefined;\n"
				+ "\t\t}\n"
				+ "\t\tlastrCSS = css;\n"
				+ "\t\tsObj = rObj;\n"
				+ "\t\t//alert(cObj);\n"
				+ "\t}else{\t\t//属于子目录操作\n"
				+ "\t\tif(cObj != undefined){\n"
				+ "\t\t\tcObj.className = \"oTD\" + lastCSS;\n"
				+ "\t\t}\n"
				+ "\t\tobj.className = \"sTD\" + css;\n"
				+ "\t\tlastCSS = css;\n"
				+ "\t\tcObj = obj;\n"
				+ "\t}\n"
				+ "}"
				+ "function setDefault(obj,css){\n"
				+ "\tvar obj = document.getElementById(obj);\n"
				+ "\tobj.className = \"sTD\" + css;\n"
				+ "\tcObj = obj;\n"
				+ "\tlastCSS = css;\n"
				+ "}"
				+ "function hideDiv(id){\n"
				+ "    var obj = document.getElementById(id);\n"
				+ "\tvar menuc = new Array();\n"
				+ "\tfor(var i = 0; i < GV.rootMenu.length; i++){\n"
				+ "\t\tmenuc[menuc.length] = \"subMenu_\" + i;\n"
				+ "\t}\n"
				+ "\n"
				+ "\tfor(var i = 0; i<menuc.length;i++){\n"
				+ "\t\tvar td = document.getElementById(menuc[i]);\n"
				+ "\t\tobj.id!= menuc[i]||td.style.display == \"block\" ? td.style.display = \"none\":td.style.display = \"block\";\n"
				+ "\t}\n" + "}";
		ResponseTypeOutputUtils.render(ServletUtils.JS_TYPE, (main + "\n" + scripts), response);
	}

	private void bulidMenuScript(StringBuffer scripts, StringBuffer main, List<SysResourceEntity> resourceList,
			HttpServletRequest request) {
		for (SysResourceEntity resource : resourceList) {
			if (resource.isShowMenu()) {
				main.append("['" + resource.getValueName() + "'," + "''," + resource.getId() + ",'1','1',"
						+ resource.isOpenMenu() + "],");

				List<SysResourceEntity> list = resource.getChildResource();
				StringBuffer buffer = new StringBuffer();
				buffer.append("GV.menus[" + resource.getId() + "]=[");
				for (SysResourceEntity re : list) {
					if (re.isShowMenu() && SysResourceEntity.URL_TYPE.equalsIgnoreCase(re.getResourceType())) {
						buffer.append("[\"" + re.getValueName() + "\",\"CreateNewTab('" + getServerPath(request)
								+ url(re.getValue(), re.getId()) + "','"
								+ ShortUrlGenerator.shortUrl((re.getValue())) + "','"
								+ re.getValueName() + "')\",\"2\",\"2\"],");
					}
					List<SysResourceEntity> ch = re.getChildResource();
					if (ch != null && !ch.isEmpty()) {
						bulidXia(buffer, ch, request);
					}
				}
				String script = buffer.toString();
				if (script.lastIndexOf(",") != -1) {
					script = script.substring(0, script.lastIndexOf(","));
				}
				script = script + "];";
				scripts.append(script);
			} else {
				List<SysResourceEntity> list = resource.getChildResource();
				if (list != null && !list.isEmpty()) {
					bulidMenuScript(scripts, main, list, request);
				}
			}
		}
	}

	private String url(String value, Long id) {
		if (value.indexOf("?") == -1) {
			return value + "?" + SysResourceEntity.RESOURCE_PARAM + "=" + id;
		}
		return value + "&" + SysResourceEntity.RESOURCE_PARAM + "=" + id;
	}

	private void bulidXia(StringBuffer buffer, List<SysResourceEntity> ch, HttpServletRequest request) {
		for (SysResourceEntity re : ch) {
			if (re.isShowMenu() && SysResourceEntity.URL_TYPE.equalsIgnoreCase(re.getResourceType())) {
				buffer.append("[\"" + re.getValueName() + "\",\"CreateNewTab('" + getServerPath(request)
						+ re.getValue() + "','" + ShortUrlGenerator.shortUrl(re.getValue()) + "','" + re.getValueName()
						+ "')\",\"2\",\"2\"],");
			}
			List<SysResourceEntity> _ch = re.getChildResource();
			if (_ch != null && !_ch.isEmpty()) {
				bulidXia(buffer, _ch, request);
			}
		}
	}

}

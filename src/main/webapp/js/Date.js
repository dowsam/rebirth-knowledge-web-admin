
/*******************************************************************************
 * 日期处理工具
 */
var DateUtils = Class.create();

// 格式化日期和数字
// 用法：
// var date=new Date();
// var dateText = format("YYYY-MM-DD",date)
// var numberText=format("###.#",5432.545)
// document.write(dateText)
// document.write("<br />"+numberText)
// like the ISO 8895
// also see Java's SimpleDateFormat.
//
// Letter Date or Time Component Presentation Examples UserDic
// Y Year Year 1996; 96
// M Month in year Month July; Jul; 07 *
// D Day in month Number 10
// w Day in week Text Tuesday; Tue; 2 *
// h Hour in day (0-23) Number 0
// m Minute in hour Number 30
// s Second in minute Number 55

// Pattern Sample
// YYYY-MM-DD hh:mm:ss 2001-07-04 12:08:56
// YYYY-MM-DDThh:mm:ss 2001-07-04T12:08:56
// YYYY/MM/DDThh:mm:ss 2001/07/04T12:08:56
// YYYY年MM月DD日,周w 2008年12月12日,周3
// hh:mm 12:08

// 符号说明：
// 0 表示补0 的数字占位
// . 表示小数点
// , 数字分组符号 如123,456.123
// # 表示不补0 的数字占位
//
// Number Pattern Result
// 10000000000001124 #,###.### 10,000,000,000,001,124.000
// 123.125 ##,#.#,# 1,2,3.1,3
// 123.125 ###.# 123.1
// 123.125 00000 00123
// 123.125 .000 .125
// 0.125 0.0000 0.1250
// 0.125 00.0000 00.1250
//
// 使用代码:
// var numberText = format("##.#",123.456)//output 123.45
DateUtils.format = function (pattern, data) {
	if (data instanceof Date) {
		function dl(data, format) {// 3
			format = format.length;
			data = data || 0;
			return format = 1 ? data : String(Math.pow(10, format) + data).substr(-format);
		}
		return pattern.replace(/([YMDhsm])\1*/g, function (format) {
			switch (format.charAt()) {
			  case "Y":
				return dl(data.getFullYear(), format);
			  case "M":
				return dl(data.getMonth() + 1, format);
			  case "D":
				return dl(data.getDate(), format);
			  case "w":
				return data.getDay() + 1;
			  case "h":
				return dl(data.getHours(), format);
			  case "m":
				return dl(data.getMinutes(), format);
			  case "s":
				return dl(data.getSeconds(), format);
			}
		});
	} else {
		if ("number" == typeof data) {
		// hack:purePattern as floatPurePattern
			function trim(data, pattern, purePattern) {
				if (pattern) {
					if (purePattern) {
						if (purePattern.charAt() == "0") {
							data = data + purePattern.substr(data.length);
						}
						if (purePattern != pattern) {
							var pattern = new RegExp("(\\d{" + pattern.search(/[^\d#]/) + "})(\\d)");
							while (data.length < (data = data.replace(pattern, "$1,$2")).length) {
							}
						}
						data = "." + data;
					} else {
						var purePattern = pattern.replace(/[^\d#]/g, "");
						if (purePattern.charAt() == "0") {
							data = purePattern.substr(data.length) + data;
						}
						if (purePattern != pattern) {
							var pattern = new RegExp("(\\d)(\\d{" + (pattern.length - pattern.search(/[^\d#]/) - 1) + "})\\b");
							while (data.length < (data = data.replace(pattern, "$1,$2")).length) {
							}
						}
					}
					return data;
				} else {
					return "";
				}
			}
			return pattern.replace(/([#0,]*)?(?:\.([#0,]+))?/, function (param, intPattern, floatPattern) {
				var floatPurePattern = floatPattern.replace(/[^\d#]/g, "");
				data = data.toFixed(floatPurePattern.length).split(".");
				return trim(data[0], intPattern) + trim(data[1] || "", floatPattern, floatPurePattern);
			});
		}
	}
};
DateUtils.getYearWeek = function (a, b, c) {
	var date1 = new Date(a, parseInt(b) - 1, c), date2 = new Date(a, 0, 1), d = Math.round((date1.valueOf() - date2.valueOf()) / 86400000);
	return Math.ceil((d + ((date2.getDay() + 1) - 1)) / 7);
};
/*******************************************************************************
 * 得到周号并且生成：年份-周号
 */
DateUtils.getYearWeekYYYY = function () {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth()) + 1;
	var day = date.getDate();
	var yearweek = DateUtils.getYearWeek(year, month, day);
	return year + "-" + yearweek;
};
DateUtils.getDateYYYMMDD = function (obj) {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth()) + 1;
	var day = date.getDate();
	month = month > 10 ? month : "0" + month;
	day = day > 10 ? day : "0" + day;
	var yearweek = year + "-" + month + "-" + day;
	if (obj != undefined) {
		obj.value = yearweek;
	}
};
DateUtils.getWeek = function (obj, fobj) {
	if (obj.value == "") {
		return false;
	}
	var tem = obj.value.split("-");
	var date = new Date(parseInt(tem[0]), parseInt(tem[1]) - 1, parseInt(tem[2]));
	var arrDays = ["\u661f\u671f\u65e5", "\u661f\u671f\u4e00", "\u661f\u671f\u4e8c", "\u661f\u671f\u4e09", "\u661f\u671f\u56db", "\u661f\u671f\u4e94", "\u661f\u671f\u516d"];
	if (typeof (fobj) == "string") {
		var tempobj = document.getElementById(fobj);
		tempobj.value = arrDays[date.getDay()];
	} else {
		if (typeof (fobj) == "object") {
			fobj.value = arrDays[date.getDay()];
		}
	}
};
/***
*得到当前日期
****/
DateUtils.getDateYMD = function () {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth()) + 1;
	var day = date.getDate();
	month = month > 10 ? month : "0" + month;
	day = day > 10 ? day : "0" + day;
	var yearweek = year + "-" + month + "-" + day + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	return yearweek;
};
/***
 * 得到当前年月日
 * @return {}
 */
DateUtils.getYMD = function () {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth()) + 1;
	var day = date.getDate();
	month = month > 10 ? month : "0" + month;
	day = day > 10 ? day : "0" + day;
	var yearweek = year + "-" + month + "-" + day;
	return yearweek;
};

/***
验证日期格式
***/
DateUtils.checkDate = function (str) {
	try {
		if (/\-/.test(str) && /\//.test(str)) {
			return false;
		}
		str = str.replace(/\-/g, "/");//必须要用正则来替换，否则只替换第一个出现的字符串
		var tmp = str.split("/");
		if (tmp.length > 3) {
			return false;
		}
		var tempDate = new Date(str);
		var year = tempDate.getYear();
		var month = tempDate.getMonth() + 1;
		var day = tempDate.getDate();
            
            //判断中间不允许有空格
		if (/\s/.test(tmp[0]) || /\s/.test(tmp[1]) || /\s/.test(tmp[2])) {
			alert("\u4e2d\u95f4\u4e0d\u5141\u8bb8\u51fa\u73b0\u7a7a\u683c\uff01");
			return false;
		}
            
            //判断年、月、日位数,可以根据自己需求修改
		if (tmp[0].length < 3 || tmp[0].length > 4) {
			return false;
		}
		if (tmp[1].length > 2) {
			return false;
		}
		if (tmp[2].length > 2) {
			return false;
		}
            //判断年、月、日位数,可以根据自己需求修改
		if (tempDate != null) {
			return year == tmp[0] && month == tmp[1] && day == tmp[2];
		} else {
			return false;
		}
	}
	catch (ex) {
            //alert(ex.message);
		return false;
	}
};


/**
 * 对于jquery.validator框架message的定制
 */
jQuery.extend(jQuery.validator.messages, {
	required : "该字段不能为空",
	remote : "请修正该字段",
	email : "请输入正确格式的电子邮件",
	url : "请输入合法的网址",
	date : "请输入合法的日期",
	dateISO : "请输入合法的日期 (ISO).",
	number : "请输入合法的数字",
	digits : "只能输入整数",
	creditcard : "请输入合法的信用卡号",
	equalTo : "请再次输入相同的值",
	accept : "请输入拥有合法后缀名的字符串",
	maxlength : jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	minlength : jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	rangelength : jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	range : jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max : jQuery.validator.format("请输入一个最大为{0} 的值"),
	min : jQuery.validator.format("请输入一个最小为{0} 的值")
});

jQuery.validator.addMethod("byteMaxLength", function(value, element, param) {
	var length = value.length;
	for (var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length <= param);
}, $.validator.format("不能超过{0}个字节(一个中文字算2个字节)"));

jQuery.validator.addMethod("numFormat", function(value, element, param) {
	return this.optional(element) || /^[0-9]+$/.test(value);
}, $.validator.format("请输入数字{0}位以内"));
// number(9,3)
jQuery.validator.addMethod("numFormat63", function(value, element) {
	return this.optional(element) || /^[0-9]+\.{0,1}[0-9]{0,3}$/.test(value);
}, $.validator.format("请输入合法数字,精度格式123456.123"));

jQuery.validator.addMethod("postcodeVal", function(value, element) {
	return this.optional(element) || /^[0-9]\d{5}(?!\d)$/.test(value);
}, $.validator.format("请输入合法的邮编"));

jQuery.validator.addMethod("numberAndLettersVal", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, $.validator.format("请输入字母或数字"));

jQuery.validator.addMethod("valiEnglish", function(value, element) {
	return this.optional(element) || /^[a-zA-Z ]*$/.test(value);
}, $.validator.format("请输入字母或者空格"));
// 身份证号码验证
jQuery.validator.addMethod("isIdCardNo", function(value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "请输入正确的身份证号码");

// 手机号码验证
jQuery.validator
		.addMethod(
				"isMobile",
				function(value, element) {
					var length = value.length;
					var mobile = /^1[3|4|5|7|8][0-9]\d{4,8}$/;
					return this.optional(element)
							|| (length == 11 && mobile.test(value));
				}, "请输入正确的手机号码");

// 电话号码验证
jQuery.validator.addMethod("isTel", function(value, element) {
	var tel = /^d{3,4}-?d{7,9}$/; // 电话号码格式010-12345678
	return this.optional(element) || (tel.test(value));
}, "请输入正确的电话号码");

// 联系电话(手机/电话皆可)验证
jQuery.validator
		.addMethod(
				"isPhone",
				function(value, element) {
					var length = value.length;
					var mobile = /^1[3|4|5|7|8][0-9]\d{4,8}$/;
					var tel = /^d{3,4}-?d{7,9}$/;
					return this.optional(element)
							|| (tel.test(value) || mobile.test(value));

				}, "请输入正确的联系电话");

function isIdCardNo(num) {

	var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
			2, 1);
	var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3",
			"2");
	var varArray = new Array();
	var intValue;
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
	// initialize
	if ((intStrLen != 15) && (intStrLen != 18)) {
		return false;
	}
	// check and set value
	for (i = 0; i < intStrLen; i++) {
		varArray[i] = idNumber.charAt(i);
		if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
			return false;
		} else if (i < 17) {
			varArray[i] = varArray[i] * factorArr[i];
		}
	}

	if (intStrLen == 18) {
		// check date
		var date8 = idNumber.substring(6, 14);
		if (isDate8(date8) == false) {
			return false;
		}
		// calculate the sum of the products
		for (i = 0; i < 17; i++) {
			lngProduct = lngProduct + varArray[i];
		}
		// calculate the check digit
		intCheckDigit = parityBit[lngProduct % 11];
		// check last digit
		if (varArray[17] != intCheckDigit) {
			return false;
		}
	} else { // length is 15
		// check date
		var date6 = idNumber.substring(6, 12);
		if (isDate6(date6) == false) {

			return false;
		}
	}
	return true;

}

function isDate8(sDate) {
	if (!/^[0-9]{8}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	day = sDate.substring(6, 8);
	var iaMonthDays = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ]
	if (year < 1700 || year > 2500)
		return false
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
		iaMonthDays[1] = 29;
	if (month < 1 || month > 12)
		return false
	if (day < 1 || day > iaMonthDays[month - 1])
		return false
	return true
}

function isDate6(sDate) {
	if (!/^[0-9]{6}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	if (year < 1700 || year > 2500)
		return false
	if (month < 1 || month > 12)
		return false
	return true
}
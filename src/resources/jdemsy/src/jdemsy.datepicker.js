(function($, jDemsy) {
	String.prototype.parseDate = function(format) {
		var val = this.toString() + "";
		var i_val = 0;
		var i_format = 0;
		var c = "";
		var token = "";
		var token2 = "";
		var x, y;
		var now = new Date();
		var year = now.getYear();
		var month = now.getMonth() + 1;
		var date = 1;
		var hh = now.getHours();
		var mm = now.getMinutes();
		var ss = now.getSeconds();
		var ampm = "";
		function _getInt(str, i, minlength, maxlength) {
			for ( var x = maxlength; x >= minlength; x--) {
				var token = str.substring(i, i + x);
				if (token.length < minlength) {
					return null;
				}
				if (token.isInteger()) {
					return token;
				}
			}
			return null;
		}

		while (i_format < format.length) {
			// Get next token from format string
			c = format.charAt(i_format);
			token = "";
			while ((format.charAt(i_format) == c) && (i_format < format.length)) {
				token += format.charAt(i_format++);
			}
			// Extract contents of value based on format token
			if (token == "yyyy" || token == "yy" || token == "y") {
				if (token == "yyyy") {
					x = 4;
					y = 4;
				} else if (token == "yy") {
					x = 2;
					y = 2;
				} else if (token == "y") {
					x = 2;
					y = 4;
				}
				year = _getInt(val, i_val, x, y);
				if (year == null) {
					return 0;
				}
				i_val += year.length;
				if (year.length == 2) {
					if (year > 70) {
						year = 1900 + (year - 0);
					} else {
						year = 2000 + (year - 0);
					}
				}
			} else if (token == "MMM" || token == "NNN") {
				month = 0;
				for ( var i = 0; i < jDemsy.nls.months.length; i++) {
					var month_name = jDemsy.nls.months[i];
					if (val.substring(i_val, i_val + month_name.length).toLowerCase() == month_name.toLowerCase()) {
						if (token == "MMM" || (token == "NNN" && i > 11)) {
							month = i + 1;
							if (month > 12) {
								month -= 12;
							}
							i_val += month_name.length;
							break;
						}
					}
				}
				if ((month < 1) || (month > 12)) {
					return 0;
				}
			} else if (token == "EE" || token == "E") {
				for ( var i = 0; i < jDemsy.nls.weeks.length; i++) {
					var day_name = jDemsy.nls.weeks[i];
					if (val.substring(i_val, i_val + day_name.length).toLowerCase() == day_name.toLowerCase()) {
						i_val += day_name.length;
						break;
					}
				}
			} else if (token == "MM" || token == "M") {
				month = _getInt(val, i_val, token.length, 2);
				if (month == null || (month < 1) || (month > 12)) {
					return 0;
				}
				i_val += month.length;
			} else if (token == "dd" || token == "d") {
				date = _getInt(val, i_val, token.length, 2);
				if (date == null || (date < 1) || (date > 31)) {
					return 0;
				}
				i_val += date.length;
			} else if (token == "hh" || token == "h") {
				hh = _getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 1) || (hh > 12)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "HH" || token == "H") {
				hh = _getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 0) || (hh > 23)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "KK" || token == "K") {
				hh = _getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 0) || (hh > 11)) {
					return 0;
				}
				i_val += hh.length;
			} else if (token == "kk" || token == "k") {
				hh = _getInt(val, i_val, token.length, 2);
				if (hh == null || (hh < 1) || (hh > 24)) {
					return 0;
				}
				i_val += hh.length;
				hh--;
			} else if (token == "mm" || token == "m") {
				mm = _getInt(val, i_val, token.length, 2);
				if (mm == null || (mm < 0) || (mm > 59)) {
					return 0;
				}
				i_val += mm.length;
			} else if (token == "ss" || token == "s") {
				ss = _getInt(val, i_val, token.length, 2);
				if (ss == null || (ss < 0) || (ss > 59)) {
					return 0;
				}
				i_val += ss.length;
			} else if (token == "a") {
				if (val.substring(i_val, i_val + 2).toLowerCase() == "am") {
					ampm = "AM";
				} else if (val.substring(i_val, i_val + 2).toLowerCase() == "pm") {
					ampm = "PM";
				} else {
					return 0;
				}
				i_val += 2;
			} else {
				if (val.substring(i_val, i_val + token.length) != token) {
					return 0;
				} else {
					i_val += token.length;
				}
			}
		}
		// If there are any trailing characters left in the value, it doesn't
		// match
		if (i_val != val.length) {
			return 0;
		}
		// Is date valid for month?
		if (month == 2) {
			// Check for leap year
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) { // leap
				// year
				if (date > 29) {
					return 0;
				}
			} else {
				if (date > 28) {
					return 0;
				}
			}
		}
		if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			if (date > 30) {
				return 0;
			}
		}
		// Correct hours value
		if (hh < 12 && ampm == "PM") {
			hh = hh - 0 + 12;
		} else if (hh > 11 && ampm == "AM") {
			hh -= 12;
		}
		return new Date(year, month - 1, date, hh, mm, ss);
	}
	Date.prototype.formatDate = function(format) {
		var date = this;
		format = format + "";
		var result = "";
		var i_format = 0;
		var c = "";
		var token = "";
		var y = date.getYear() + "";
		var M = date.getMonth() + 1;
		var d = date.getDate();
		var E = date.getDay();
		var H = date.getHours();
		var m = date.getMinutes();
		var s = date.getSeconds();
		var yyyy, yy, MMM, MM, dd, hh, h, mm, ss, ampm, HH, H, KK, K, kk, k;
		// Convert real date parts into formatted versions
		var value = {};
		function LZ(x) {
			return (x < 0 || x > 9 ? "" : "0") + x
		}
		if (y.length < 4) {
			y = "" + (y - 0 + 1900);
		}
		value["y"] = "" + y;
		value["yyyy"] = y;
		value["yy"] = y.substring(2, 4);
		value["M"] = M;
		value["MM"] = LZ(M);
		value["MMM"] = jDemsy.nls.months[M - 1];
		value["NNN"] = jDemsy.nls.months[M + 11];
		value["d"] = d;
		value["dd"] = LZ(d);
		value["E"] = jDemsy.nls.weeks[E + 7];
		value["EE"] = jDemsy.nls.weeks[E];
		value["H"] = H;
		value["HH"] = LZ(H);
		if (H == 0) {
			value["h"] = 12;
		} else if (H > 12) {
			value["h"] = H - 12;
		} else {
			value["h"] = H;
		}
		value["hh"] = LZ(value["h"]);
		if (H > 11) {
			value["K"] = H - 12;
		} else {
			value["K"] = H;
		}
		value["k"] = H + 1;
		value["KK"] = LZ(value["K"]);
		value["kk"] = LZ(value["k"]);
		if (H > 11) {
			value["a"] = "PM";
		} else {
			value["a"] = "AM";
		}
		value["m"] = m;
		value["mm"] = LZ(m);
		value["s"] = s;
		value["ss"] = LZ(s);
		while (i_format < format.length) {
			c = format.charAt(i_format);
			token = "";
			while ((format.charAt(i_format) == c) && (i_format < format.length)) {
				token += format.charAt(i_format++);
			}
			if (value[token] != null) {
				result += value[token];
			} else {
				result += token;
			}
		}
		return result;
	}

	/**
	 * 定义DatePicker对象
	 */
	function DatePicker(sDate, options) {
		this.options = options;
		this.sDate = sDate.trim();
	}

	$.extend(DatePicker.prototype, {
		get : function(name) {
			return this.options[name];
		},
		_getDays : function(y, m) {// 获取某年某月的天数
			return m == 2 ? (y % 4 || !(y % 100) && y % 400 ? 28 : 29) : (/4|6|9|11/.test(m) ? 30 : 31);
		},
		getDateWrapper : function(date) { // 得到年、月、日、时、分、秒等日期时间值
			if (!date)
				date = this.parseDate(this.sDate) || new Date();
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var days = this._getDays(y, m);
			return {
				year : y,
				month : m,
				day : date.getDate(),
				hour : date.getHours(),
				minute : date.getMinutes(),
				second : date.getSeconds(),
				days : days,
				date : date
			}
		},
		changeDate : function(y, m, d) {
			var date = new Date(y, m - 1, d || 1);
			this.sDate = this.formatDate(date);
			return date;
		},
		/*
		 * 选择日历中的日期：chMonth=-1表示选中的日期是上月日期，chMonth=1表示选中的日期是下月日期。 返回日期对象
		 */
		changeDay : function(day, chMonth) {
			if (!chMonth)
				chMonth = 0;
			var dateWrapper = this.getDateWrapper();
			return this.changeDate(dateWrapper.year, dateWrapper.month + parseInt(chMonth), day);
		},
		parseDate : function(sDate) {
			return sDate.parseDate(this.options.pattern);
		},
		formatDate : function(date) {
			return date.formatDate(this.options.pattern);
		},
		hasHour : function() {
			return this.options.pattern.indexOf("H") != -1;
		},
		hasMinute : function() {
			return this.options.pattern.indexOf("m") != -1;
		},
		hasSecond : function() {
			return this.options.pattern.indexOf("s") != -1;
		},
		hasTime : function() {
			return this.hasHour() || this.hasMinute() || this.hasSecond();
		},
		hasDate : function() {
			var _dateKeys = [ 'y', 'M', 'd', 'E' ];
			for ( var i = 0; i < _dateKeys.length; i++) {
				if (this.options.pattern.indexOf(_dateKeys[i]) != -1)
					return true;
			}

			return false;
		}
	});
	/**
	 * 定义datepicker jquery对象
	 */
	$.fn.datepicker = function(options) {
		options = $.extend({}, $.fn.datepicker.defaults, $.fn.datepicker.parseOptions(this), options);
		var setting = {
			template : '\
				<div id="datepicker">\
					<div class="main">\
						<div class="head">\
							<table width="100%" border="0" cellpadding="0" cellspacing="2">\
							<tr>\
								<td><select name="year"></select></td>\
								<td><select name="month"></select></td>\
								<td width="20"><span class="close">×</span></td>\
							</tr>\
							</table>\
						</div>\
						<div class="body">\
							<dl class="weeks"><dt>日</dt><dt>一</dt><dt>二</dt><dt>三</dt><dt>四</dt><dt>五</dt><dt>六</dt></dl>\
							<dl class="days">日期列表选项</dl>\
							<div style="clear:both;height:0;line-height:0"></div>\
							<div class="menu">\
								<ul class="menu-hh">\
									<li>0</li>\
									<li>1</li>\
									<li>2</li>\
									<li>3</li>\
									<li>4</li>\
									<li>5</li>\
									<li>6</li>\
									<li>7</li>\
									<li>8</li>\
									<li>9</li>\
									<li>10</li>\
									<li>11</li>\
									<li>12</li>\
									<li>13</li>\
									<li>14</li>\
									<li>15</li>\
									<li>16</li>\
									<li>17</li>\
									<li>18</li>\
									<li>19</li>\
									<li>20</li>\
									<li>21</li>\
									<li>22</li>\
									<li>23</li>\
								</ul>\
								<ul class="menu-mm">\
									<li>0</li>\
									<li>5</li>\
									<li>10</li>\
									<li>15</li>\
									<li>20</li>\
									<li>25</li>\
									<li>30</li>\
									<li>35</li>\
									<li>40</li>\
									<li>45</li>\
									<li>50</li>\
									<li>55</li>\
								</ul>\
								<ul class="menu-ss">\
									<li>0</li>\
									<li>5</li>\
									<li>10</li>\
									<li>15</li>\
									<li>20</li>\
									<li>25</li>\
									<li>30</li>\
									<li>35</li>\
									<li>40</li>\
									<li>45</li>\
									<li>50</li>\
									<li>55</li>\
								</ul>\
							</div>\
						</div>\
						<div class="foot">\
							<table class="time">\
								<tr>\
									<td>\
										<input type="text" class="time-hh" maxlength="2" start="0" end="23"/>:\
										<input type="text" class="time-mm" maxlength="2" start="0" end="59"/>:\
										<input type="text" class="time-ss" maxlength="2" start="0" end="59"/>\
									</td>\
									<td><ul><li class="time-up">&and;</li><li class="time-down">&or;</li></ul></td>\
								</tr>\
							</table>\
							<button type="button" class="clear">清空</button>\
							<button type="button" class="confirm">确定</button>\
						<div>\
					</div>\
				</div>',
			box$ : "#datepicker",
			year$ : "#datepicker .head [name=year]",
			month$ : "#datepicker .head [name=month]",
			time$ : "#datepicker .time :text",
			hour$ : "#datepicker .time-hh",
			minute$ : "#datepicker .time-mm",
			second$ : "#datepicker .time-ss",
			menu$ : "#datepicker .menu",
			timeUp$ : "#datepicker .time-up",
			timeDown$ : "#datepicker .time-down",
			close$ : "#datepicker .close",
			calIcon$ : "a.inputDateButton",
			main$ : "#datepicker .main",
			days$ : "#datepicker .days",
			weeks$ : "#datepicker .weeks",
			clear$ : "#datepicker .clear",
			confirm$ : "#datepicker .confirm"
		};

		/*
		 * 改变并显示时间菜单, type可以是hh、mm、ss。
		 */
		function changeTimeMenu(type) {
			var $menu = $(setting.menu$).removeClass("menu-hh").removeClass("menu-mm").removeClass("menu-ss");
			if (type) {
				$menu.addClass("menu-" + type);
				$(setting.time$).removeClass("selected").filter(".time-" + type).addClass("selected");
			}
		}

		/*
		 * 通过时间菜单选择时间值
		 */
		function clickTimeMenu($input, type) {
			$(setting.menu$).find(".menu-" + type + " li").each(function() {
				var $li = $(this);
				$li.click(function() {
					$input.val($li.text());
				});
			});
		}

		/*
		 * 通过微调按钮调整时间数值
		 */
		function changeTime($input, type) {
			var timeValue = parseInt($input.val()), timeStart = parseInt($input.attr("start")), timeEnd = parseInt($input.attr("end"));
			if (type == 1) {
				if (timeValue < timeEnd) {
					$input.val(timeValue + 1);
				}
			} else if (type == -1) {
				if (timeValue > timeStart) {
					$input.val(timeValue - 1);
				}
			} else if (timeValue > timeEnd) {
				$input.val(timeEnd);
			} else if (timeValue < timeStart) {
				$input.val(timeStart);
			}
		}

		return this.each(function() {
			var $this = $(this);
			var datepicker = new DatePicker($this.val(), options);

			function makeDatePicker(datepicker) {
				var dateWrapper = datepicker.getDateWrapper();

				var monthStart = new Date(dateWrapper.year, dateWrapper.month - 1, 1);
				var startDay = monthStart.getDay();
				var daysHtml = "";
				// 上个月的日期
				if (startDay > 0) {
					monthStart.setMonth(monthStart.getMonth() - 1);
					var prevDateWrapper = datepicker.getDateWrapper(monthStart);
					for ( var t = prevDateWrapper.days - startDay + 1; t <= prevDateWrapper.days; t++) {
						daysHtml += '<dd class="other" chMonth="-1">' + t + '</dd>';
					}
				}
				// 本月中的日期
				for ( var t = 1; t <= dateWrapper.days; t++) {
					if (t == dateWrapper.day) {
						daysHtml += '<dd class="selected">' + t + '</dd>';
					} else {
						daysHtml += '<dd>' + t + '</dd>';
					}
				}
				// 下个月的日期
				for ( var t = 1; t <= 42 - startDay - dateWrapper.days; t++) {
					daysHtml += '<dd class="other" chMonth="1">' + t + '</dd>';
				}

				// 选择日历框中的日期
				var $days = $(setting.days$).html(daysHtml).find("dd");
				$days.click(function() {
					var $day = $(this);
					$this.val(datepicker.formatDate(datepicker.changeDay($day.html(), $day.attr("chMonth"))));
					if (!datepicker.hasTime()) {
						closeDatePicker();
					} else {
						$days.removeClass("selected");
						$day.addClass("selected");
					}
				});

				// 仅时间，无日期
				if (!datepicker.hasDate())
					$(setting.main$).addClass('datepicker-nodate');

				if (datepicker.hasTime()) {
					$("#datepicker .time").show();

					var $hour = $(setting.hour$).val(dateWrapper.hour).focus(function() {
						changeTimeMenu("hh");
					});
					var $minute = $(setting.minute$).val(dateWrapper.minute).focus(function() {
						changeTimeMenu("mm");
					});
					var $second = $(setting.second$).val(dateWrapper.second).focus(function() {
						changeTimeMenu("ss");
					});
					$hour.add($minute).add($second).click(function() {
						return false;
					});

					clickTimeMenu($hour, "hh");
					clickTimeMenu($minute, "mm");
					clickTimeMenu($second, "ss");
					// 隐藏时间菜单
					$(setting.box$).click(function() {
						changeTimeMenu();
					});

					var $inputs = $(setting.time$);
					$inputs.keydown(function(e) {
						if (!((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode == jDemsy.keyCode.DELETE || e.keyCode == jDemsy.keyCode.BACKSPACE))) {
							return false;
						}
					}).each(function() {
						var $input = $(this);
						$input.keyup(function() {
							changeTime($input, 0);
						});
					});
					$(setting.timeUp$).click(function() {
						$inputs.filter(".selected").each(function() {
							changeTime($(this), 1);
						});
					});
					$(setting.timeDown$).click(function() {
						$inputs.filter(".selected").each(function() {
							changeTime($(this), -1);
						});
					});

					if (!datepicker.hasHour())
						$hour.attr("disabled", true);
					if (!datepicker.hasMinute())
						$minute.attr("disabled", true);
					if (!datepicker.hasSecond())
						$second.attr("disabled", true);
				}

			}

			function closeDatePicker() {
				$(setting.box$).remove();
				$(document).unbind("click", closeDatePicker);
			}

			$this.click(function(event) {
				closeDatePicker();
				var datepicker = new DatePicker($this.val(), options);
				var offset = $this.offset();
				var iTop = offset.top + this.offsetHeight;
				$(setting.template).appendTo("body").css({
					left : offset.left + 'px',
					top : iTop + 'px'
				}).show().click(function(event) {
					event.stopPropagation();
				});

				($.fn.bgiframe && $(setting.box$).bgiframe());

				var weeks = "";
				$.each(jDemsy.nls.weeks, function(i, v) {
					weeks += "<dt>" + v + "</dt>"
				});
				$(setting.weeks$).html(weeks);

				var dateWrapper = datepicker.getDateWrapper();
				var $year = $(setting.year$);
				var yearstart = dateWrapper.year + parseInt(datepicker.get("yearstart"));
				var yearend = dateWrapper.year + parseInt(datepicker.get("yearend"));
				for (y = yearstart; y <= yearend; y++) {
					$year.append('<option value="' + y + '"' + (dateWrapper.year == y ? 'selected="selected"' : '') + '>' + y + '</option>');
				}
				var $month = $(setting.month$);
				$.each(jDemsy.nls.months, function(i, v) {
					var m = i + 1;
					$month.append('<option value="' + m + '"' + (dateWrapper.month == m ? 'selected="selected"' : '') + '>' + v + '</option>');
				});

				// generate datepicker
				makeDatePicker(datepicker);
				$year.add($month).change(function() {
					datepicker.changeDate($year.val(), $month.val());
					makeDatePicker(datepicker);
				});

				// fix top
				var iBoxH = $(setting.box$).outerHeight(true);
				if (iTop > iBoxH && iTop > $(window).height() - iBoxH) {
					$(setting.box$).css("top", offset.top - iBoxH);
				}

				$(setting.close$).click(function() {
					closeDatePicker();
				});
				$(setting.clear$).click(function() {
					$this.val("");
					closeDatePicker();
				});
				$(setting.confirm$).click(function() {
					var $dd = $(setting.days$).find("dd.selected");
					var date = datepicker.changeDay($dd.html(), $dd.attr("chMonth"));

					if (datepicker.hasTime()) {
						date.setHours(parseInt($(setting.hour$).val()));
						date.setMinutes(parseInt($(setting.minute$).val()));
						date.setSeconds(parseInt($(setting.second$).val()));
					}

					$this.val(datepicker.formatDate(date));
					closeDatePicker();
				});
				$(document).bind("click", closeDatePicker);
				return false;
			});

			$this.parent().find(setting.calIcon$).click(function() {
				$this.trigger("click");
				return false;
			});
		});

	}
	$.fn.datepicker.parseOptions = function($container) {
		return jDemsy.parseOptions($container, [ "pattern", {
			"yearstart" : "number",
			"yearend" : "number"
		} ])
	};

	$.fn.datepicker.defaults = {
		pattern : 'yyyy-MM-dd',
		yearstart : -10,
		yearend : 10
	};
})(jQuery, jDemsy);
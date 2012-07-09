(function($, jDemsy) {
	$.fn.datepicker = function(opts) {
		var setting = {
			box$ : "#jdemsy_calendar",
			year$ : "#calendar [name=year]",
			month$ : "#calendar [name=month]",
			time$ : "#calendar .time :text",
			hour$ : "#calendar .timeHH",
			minute$ : "#calendar .timeMM",
			second$ : "#calendar .timeSS",
			timeMenu$ : "#calendar .timeMenu",
			timeUpBtn$ : "#calendar .timeUpBtn",
			timeDownBtn$ : "#calendar .timeDownBtn",
			close$ : "#calendar .close",
			calIcon$ : "a.inputDateButton",
			main$ : "#calendar .main",
			days$ : "#calendar .days",
			weeks$ : "#calendar .weeks",
			btnClear$ : "#calendar .btnClear",
			btnConfirm$ : "#calendar .btnConfirm"
		};

		/*
		 * 改变并显示时间菜单
		 */
		function changeTimeMenu(sltClass) {
			var $tm = $(setting.timeMenu$);
			$tm.removeClass("hh").removeClass("mm").removeClass("ss");
			if (sltClass) {
				$tm.addClass(sltClass);
				$(setting.time$).removeClass("slt").filter("." + sltClass).addClass("slt");
			}
		}

		/*
		 * 通过时间菜单选择时间值
		 */
		function clickTimeMenu($input, type) {
			$(setting.timeMenu$).find("." + type + " li").each(function() {
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
			var ivalue = parseInt($input.val()), istart = parseInt($input.attr("start")), iend = parseInt($input.attr("end"));
			if (type == 1) {
				if (ivalue < iend) {
					$input.val(ivalue + 1);
				}
			} else if (type == -1) {
				if (ivalue > istart) {
					$input.val(ivalue - 1);
				}
			} else if (ivalue > iend) {
				$input.val(iend);
			} else if (ivalue < istart) {
				$input.val(istart);
			}
		}

		return this.each(function() {
			var $this = $(this);
			var datepicker = new Datepicker($this.val(), opts);

			function makeCalendar(datepicker) {
				var datewrap = datepicker.getDateWrap();

				var monthStart = new Date(datewrap.year, datewrap.month - 1, 1);
				var startDay = monthStart.getDay();
				var dayStr = "";
				// 上个月的日期
				if (startDay > 0) {
					monthStart.setMonth(monthStart.getMonth() - 1);
					var prevDateWrap = datepicker.getDateWrap(monthStart);
					for ( var t = prevDateWrap.days - startDay + 1; t <= prevDateWrap.days; t++) {
						dayStr += '<dd class="other" chMonth="-1">' + t + '</dd>';
					}
				}
				// 本月中的日期
				for ( var t = 1; t <= datewrap.days; t++) {
					if (t == datewrap.day) {
						dayStr += '<dd class="slt">' + t + '</dd>';
					} else {
						dayStr += '<dd>' + t + '</dd>';
					}
				}
				// 下个月的日期
				for ( var t = 1; t <= 42 - startDay - datewrap.days; t++) {
					dayStr += '<dd class="other" chMonth="1">' + t + '</dd>';
				}

				// 选择日历框中的日期
				var $days = $(setting.days$).html(dayStr).find("dd");
				$days.click(function() {
					var $day = $(this);
					$this.val(datepicker.formatDate(datepicker.changeDay($day.html(), $day.attr("chMonth"))));
					if (!datepicker.hasTime()) {
						closeCalendar();
					} else {
						$days.removeClass("slt");
						$day.addClass("slt");
					}
				});

				// 仅时间，无日期
				if (!datepicker.hasDate())
					$(setting.main$).addClass('nodate');

				if (datepicker.hasTime()) {
					$("#calendar .time").show();

					var $hour = $(setting.hour$).val(datewrap.hour).focus(function() {
						changeTimeMenu("hh");
					});
					var $minute = $(setting.minute$).val(datewrap.minute).focus(function() {
						changeTimeMenu("mm");
					});
					var $second = $(setting.second$).val(datewrap.second).focus(function() {
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
					$(setting.timeUpBtn$).click(function() {
						$inputs.filter(".slt").each(function() {
							changeTime($(this), 1);
						});
					});
					$(setting.timeDownBtn$).click(function() {
						$inputs.filter(".slt").each(function() {
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

			function closeCalendar() {
				$(setting.box$).remove();
				$(document).unbind("click", closeCalendar);
			}

			$this.click(function(event) {
				closeCalendar();
				var datepicker = new Datepicker($this.val(), opts);
				var offset = $this.offset();
				var iTop = offset.top + this.offsetHeight;
				$(jDemsy.tpl.calendar).appendTo("body").css({
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

				var datewrap = datepicker.getDateWrap();
				var $year = $(setting.year$);
				var yearstart = datewrap.year + parseInt(datepicker.get("yearstart"));
				var yearend = datewrap.year + parseInt(datepicker.get("yearend"));
				for (y = yearstart; y <= yearend; y++) {
					$year.append('<option value="' + y + '"' + (datewrap.year == y ? 'selected="selected"' : '') + '>' + y + '</option>');
				}
				var $month = $(setting.month$);
				$.each($.regional.datepicker.monthNames, function(i, v) {
					var m = i + 1;
					$month.append('<option value="' + m + '"' + (datewrap.month == m ? 'selected="selected"' : '') + '>' + v + '</option>');
				});

				// generate calendar
				makeCalendar(datepicker);
				$year.add($month).change(function() {
					datepicker.changeDate($year.val(), $month.val());
					makeCalendar(datepicker);
				});

				// fix top
				var iBoxH = $(setting.box$).outerHeight(true);
				if (iTop > iBoxH && iTop > $(window).height() - iBoxH) {
					$(setting.box$).css("top", offset.top - iBoxH);
				}

				$(setting.close$).click(function() {
					closeCalendar();
				});
				$(setting.btnClear$).click(function() {
					$this.val("");
					closeCalendar();
				});
				$(setting.btnConfirm$).click(function() {
					var $dd = $(setting.days$).find("dd.slt");
					var date = datepicker.changeDay($dd.attr("day"), $dd.attr("chMonth"));

					if (datepicker.hasTime()) {
						date.setHours(parseInt($(setting.hour$).val()));
						date.setMinutes(parseInt($(setting.minute$).val()));
						date.setSeconds(parseInt($(setting.second$).val()));
					}

					$this.val(datepicker.formatDate(date));
					closeCalendar();
				});
				$(document).bind("click", closeCalendar);
				return false;
			});

			$this.parent().find(setting.calIcon$).click(function() {
				$this.trigger("click");
				return false;
			});
		});

	}

	function Datepicker(sDate, opts) {
		this.options = $.extend({
			pattern : 'yyyy-MM-dd',
			yearstart : -10,
			yearend : 10
		}, options);

		this.sDate = sDate.trim();
	}

	$.extend(Datepicker.prototype, {
		get : function(name) {
			return this.options[name];
		},
		_getDays : function(y, m) {// 获取某年某月的天数
			return m == 2 ? (y % 4 || !(y % 100) && y % 400 ? 28 : 29) : (/4|6|9|11/.test(m) ? 30 : 31);
		},
		getDateWrap : function(date) { // 得到年、月、日、时、分、秒等日期时间值
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
			var datewrap = this.getDateWrap();
			return this.changeDate(datewrap.year, datewrap.month + parseInt(chMonth), day);
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
})(jQuery, jDemsy);

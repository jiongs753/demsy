/**
 * 参数说明：
 * <p>
 * tabList:包裹选项卡的父级层
 * <p>
 * tabTxt :包裹内容层的父级层
 * <p>
 * options.currentTab:激活选项卡的序列号
 * <p>
 * options.defaultClass:当前选项卡激活状态样式名，默认名字为“current”
 * <p>
 * isAutoPlay:是否自动切换
 * <p>
 * stepTime:切换间隔时间
 * <p>
 * switchingMode:切换方式（'c'表示click切换;'o'表示mouseover切换）
 */
(function($) {
	$.fn.tabs = function(options) {
		var _tabList = $(this).find(".tabsMenu UL");
		var _tabTxt = $(this).find(".tabsBox");

		var tabListLi = _tabList.find("li");
		var defaults = {
			currentTab : 0,
			defaultClass : "current",
			isAutoPlay : false,
			stepTime : 2000,
			switchingMode : "c"
		};
		var o = $.extend({}, defaults, options);
		var _isAutoPlay = o.isAutoPlay;
		var _stepTime = o.stepTime;
		var _switchingMode = o.switchingMode;
		_tabList.find("li:eq(" + o.currentTab + ")").addClass(o.defaultClass);

		_tabTxt.children("div").each(function(i) {
			$(this).attr("id", "wp_div" + i);
		}).eq(o.currentTab).css({
			"display" : "block"
		});

		tabListLi.each(function(i) {
			$(tabListLi[i]).mouseover(function() {
				if (_switchingMode == "o") {
					$(this).click();
				}
				_isAutoPlay = false;
			});
			$(tabListLi[i]).mouseout(function() {
				_isAutoPlay = true;
			})
		});

		_tabTxt.each(function(i) {
			$(_tabTxt[i]).mouseover(function() {
				_isAutoPlay = false;
			});
			$(_tabTxt[i]).mouseout(function() {
				_isAutoPlay = true;
			})
		});

		tabListLi.each(function(i) {
			$(tabListLi[i]).click(function() {
				if ($(this).className != o.defaultClass) {
					$(this).addClass(o.defaultClass).siblings().removeClass(o.defaultClass);
				}
				if ($.browser.msie) {
					_tabTxt.children("div").eq(i).siblings().css({
						"display" : "none"
					});
					_tabTxt.children("div").eq(i).fadeIn(600);
				} else {
					_tabTxt.children("div").eq(i).css({
						"display" : "block"
					}).siblings().css({
						"display" : "none"
					});
				}

			})
		});

		function selectMe(oo) {
			if (oo != null && oo.html() != null && _isAutoPlay) {
				oo.click();
			}
			if (oo.html() == null) {
				selectMe(_tabList.find("li").eq(0));

			} else {
				window.setTimeout(selectMe, _stepTime, oo.next());
			}
		}
		if (_isAutoPlay) {
			selectMe(_tabList.find("li").eq(o.currentTab));
		}
		return this;
	};
})(jQuery);
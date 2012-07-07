(function($, jDemsy) {
	var jdAccordions = new Map();

	// 添加窗口resize事件：浏览器窗口大小改变时：重新填充折叠式菜单空白
	$(window).resize(function() {
		setTimeout(function() {
			for ( var i = 0; i < jdAccordions.size(); i++) {
				evalHeight(jdAccordions.element(i).key);
			}
		}, 100);
	});

	/*
	 * 创建回调函数：即将fun函数作为obj的对象函数来调用
	 */
	function makeMethod(obj, fun) {
		return function() {
			return fun.apply(obj, arguments);
		};
	}

	/*
	 * accordion菜单创建完成时调用该函数
	 */
	function completed(cancel) {
		// 不是一个accordion对象
		if (!$.data(this, "accordion"))
			return;

		// 获取accordion实例
		var instance = $.data(this, "accordion");
		var options = instance.options;
		options.running = cancel ? 0 : --options.running;
		if (options.running)
			return;

		if (options.clearStyle) {
			options.toShow.add(options.toHide).css({
				height : "",
				overflow : ""
			});
		}

		// 触发change事件
		$(this).triggerHandler("change.accordion", [ options.data ], options.change);
	}

	/*
	 * 调整accordion菜单高度
	 */
	function evalHeight(key) {
		var obj = jdAccordions.get(key);
		if (!obj)
			return;

		var parent = $(obj).parent();
		var parentHeight = parent.height();
		if (parent.isTag("body")) {
			parentHeight = $(window).height();
		}

		// 计算accordion菜单内容面板高度
		var height = parentHeight - (($(".accordion-header", obj).size()) * ($(".accordion-header:first-child", obj).outerHeight())) - 2;

		var os = parent.children().not(obj);
		$.each(os, function(i) {
			height -= $(os[i]).outerHeight();
		});

		$(".accordion-content", obj).height(height);
	}

	function toggle(toShow, toHide, data, clickedActive, down) {
		var options = $.data(this, "accordion").options;
		options.toShow = toShow;
		options.toHide = toHide;
		options.data = data;
		var complete = makeMethod(this, completed);

		// count elements to animate
		options.running = toHide.size() == 0 ? toShow.size() : toHide.size();

		// if (options.animated) {
		// if (!options.alwaysOpen && clickedActive) {
		// jdAccordion.animations[options.animated]({
		// toShow : jQuery([]),
		// toHide : toHide,
		// complete : complete,
		// down : down,
		// autoHeight : options.autoHeight
		// });
		// } else {
		// jdAccordion.animations[options.animated]({
		// toShow : toShow,
		// toHide : toHide,
		// complete : complete,
		// down : down,
		// autoHeight : options.autoHeight
		// });
		// }
		// } else {
		if (!options.alwaysOpen && clickedActive) {
			toShow.toggle();
		} else {
			toHide.hide();
			toShow.show();
		}
		complete(true);
		// }
	}

	function clickHandler(event) {
		var options = $.data(this, "accordion").options;
		if (options.disabled)
			return false;

		// called only when using activate(false) to close all parts
		// programmatically
		if (!event.target && !options.alwaysOpen) {
			options.active.toggleClass(options.selectedClass);
			var toHide = options.active.next(), data = {
				instance : this,
				options : options,
				newHeader : jQuery([]),
				oldHeader : options.active,
				newContent : jQuery([]),
				oldContent : toHide
			}, toShow = options.active = $([]);
			toggle.call(this, toShow, toHide, data);
			return false;
		}
		// get the click target
		var clicked = $(event.target);

		// due to the event delegation model, we have to check if one
		// of the parent elements is our actual header, and find that
		if (clicked.parents(options.header).length)
			while (!clicked.is(options.header))
				clicked = clicked.parent();

		var clickedActive = clicked[0] == options.active[0];

		// if animations are still active, or the active header is the target,
		// ignore click
		if (options.running || (options.alwaysOpen && clickedActive))
			return false;
		if (!clicked.is(options.header))
			return;

		// switch classes
		options.active.toggleClass(options.selectedClass);
		if (!clickedActive) {
			clicked.addClass(options.selectedClass);
		}

		// find elements to show and hide
		var toShow = clicked.next(), toHide = options.active.next(),
		// data = [clicked, options.active, toShow, toHide],
		data = {
			instance : this,
			options : options,
			newHeader : clicked,
			oldHeader : options.active,
			newContent : toShow,
			oldContent : toHide
		}, down = options.headers.index(options.active[0]) > options.headers.index(clicked[0]);

		options.active = clickedActive ? $([]) : clicked;
		toggle.call(this, toShow, toHide, data, clickedActive, down);

		return false;
	}

	function findActive(headers, selector) {
		return selector != undefined ? typeof selector == "number" ? headers.filter(":eq(" + selector + ")") : headers.not(headers.not(selector)) : selector === false ? $([]) : headers.filter(":eq(0)");
	}

	function jdAccordion(container, options) {
		// 合并配置项
		this.options = options;
		this.element = container;
		var $container = $(container);

		// $container.addClass("accordion");
		if (options.navigation) {
			var current = $container.find("a").filter(options.navigationFilter);
			if (current.length) {
				if (current.filter(options.header).length) {
					options.active = current;
				} else {
					options.active = current.parent().parent().prev();
					current.addClass("current");
				}
			}
		}

		// 计算选中的菜单
		options.headers = $container.find(options.header).hoverClass(options.hoverClass);
		options.active = findActive(options.headers, options.active);

		if (options.fillSpace) {
			evalHeight(options.mapKey);
		} else if (options.autoHeight) {
			var maxHeight = 0;
			options.headers.next().each(function() {
				maxHeight = Math.max(maxHeight, $(this).outerHeight());
			}).height(maxHeight);
		}

		options.active.addClass(options.selectedClass).next().show();

		$container.bind((options.event) + ".accordion", clickHandler);
	}

	// jdAccordion.prototype = {
	// activate : function(index) {
	// // call clickHandler with custom event
	// clickHandler.call(this.element, {
	// target : findActive(this.options.headers, index)[0]
	// });
	// },
	//
	// enable : function() {
	// this.options.disabled = false;
	// },
	// disable : function() {
	// this.options.disabled = true;
	// },
	// destroy : function() {
	// this.options.headers.next().css("display", "");
	// if (this.options.fillSpace || this.options.autoHeight) {
	// this.options.headers.next().css("height", "");
	// }
	// $.removeData(this.element, "accordion");
	// $(this.element).removeClass("accordion").unbind(".accordion");
	// }
	// }

	// jdAccordion.animations = {
	// slide : function(options, additions) {
	// options = $.extend({
	// easing : "swing",
	// duration : 300
	// }, options, additions);
	// if (!options.toHide.size()) {
	// options.toShow.animate({
	// height : "show"
	// }, options);
	// return;
	// }
	// var hideHeight = options.toHide.height(), showHeight =
	// options.toShow.height(), difference = showHeight / hideHeight;
	// options.toShow.css({
	// height : 0
	// }).show();
	// options.toHide.filter(":hidden").each(options.complete).end().filter(":visible").animate({
	// height : "hide"
	// }, {
	// step : function(now) {
	// var current = (hideHeight - now) * difference;
	// if ($.browser.msie || $.browser.opera) {
	// current = Math.ceil(current);
	// }
	// options.toShow.height(current);
	// },
	// duration : options.duration,
	// easing : options.easing,
	// complete : function() {
	// if (!options.autoHeight) {
	// options.toShow.css({
	// height : "auto"
	// });
	// }
	// options.toShow.css({
	// overflow : "auto"
	// });
	// options.complete();
	// }
	// });
	// },
	// bounceslide : function(options) {
	// this.slide(options, {
	// easing : options.down ? "bounceout" : "swing",
	// duration : options.down ? 1000 : 200
	// });
	// },
	// easeslide : function(options) {
	// this.slide(options, {
	// easing : "easeinout",
	// duration : 700
	// })
	// }
	// };

	// 扩展jQuery对象支持 accordion
	$.fn.accordion = function(options, data) {
		options = $.extend({}, $.fn.accordion.defaults, options);

		var args = Array.prototype.slice.call(arguments, 1);

		return this.each(function() {
			if (options.fillSpace) {
				options.mapKey = jdAccordions.size();
				jdAccordions.put(options.mapKey, this);
			}

			// 如果不是一个 accordion 对象， 则创建 accordion 对象
			var accordion = $.data(this, "accordion");
			if (!accordion) {
				accordion = new jdAccordion(this, options);
				$.data(this, "accordion", accordion);
			}

			// 获取 accordion 对象并调用对象上的方法
			if (typeof options == "string") {
				accordion[options].apply(accordion, args);
			}

		});
	}
	$.fn.accordion.defaults = {
		selectedClass : "accordion-selected",
		hoverClass : "accordion-hover",
		alwaysOpen : true,
		// animated : false,
		event : "click",
		header : ".accordion-header",
		autoHeight : true,
		fillSpace : true,
		running : 0,
		navigationFilter : function() {
			return this.href.toLowerCase() == location.href.toLowerCase();
		}
	};

})(jQuery, jDemsy);

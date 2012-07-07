/**
 * 2012-07-05 11:40以前, demo耗时65ms左右
 */
(function($, jDemsy) {

	/*
	 * 获取tab项距离左边的位置
	 */
	function getTabLeftPosition($container, tab) {
		var w = 0;

		// 查找tab条目并计算当前条目之前的宽度
		var items = $('>div.tabs-header ul.tabs li', $container)
		var len = items.length;
		for (i = 0; i < len; i++) {
			if (this == items[i])
				break;
			w += $(items[i]).outerWidth(true);
		}

		return w;
	}

	/*
	 * 计算最大可滚动宽度 = tab项总宽度 - 可见部分宽度
	 */
	function getMaxScrollWidth($container) {
		var header = $('>div.tabs-header', $container);
		var tabsWidth = 0;
		$('ul.tabs li', header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});
		var wrapWidth = $('.tabs-wrap', header).width();
		var padding = $('.tabs', header).getCssValue('padding-left');

		return tabsWidth - wrapWidth + padding;
	}

	function setScrollers($container) {
		var header = $('>div.tabs-header', $container);

		// 计算tab项总宽度
		var tabsWidth = 0;
		$('ul.tabs li', header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});

		var leftScroller = $('.tabs-scroller-left', header);
		var rightScroller = $('.tabs-scroller-right', header);
		var wrap = $('.tabs-wrap', header);

		// 总宽度已经超出tab栏目表头宽度
		if (tabsWidth > header.width()) {
			leftScroller.css('display', 'block');
			rightScroller.css('display', 'block');
			wrap.addClass('tabs-scrolling');

			if ($.boxModel == true) {
				wrap.css('left', 2);
			} else {
				wrap.css('left', 0);
			}
			var width = header.width() - leftScroller.outerWidth() - rightScroller.outerWidth();
			wrap.width(width);

		} else {
			leftScroller.css('display', 'none');
			rightScroller.css('display', 'none');
			wrap.removeClass('tabs-scrolling').scrollLeft(0);
			wrap.width(header.width());
			wrap.css('left', 0);
		}
	}

	function setSize($container) {
		var opts = $.data($container[0], 'tabs').options;

		// 获取父节点宽度和高度作为tabs的大小
		if (opts.fit == true) {
			var p = $container.parent();
			opts.width = p.width();
			opts.height = p.height();
		}

		// 设置tabs容器尺寸
		$container.width(opts.width).height(opts.height);

		// 设置tabs表头宽度
		var header = $('.tabs-header', $container);
		if ($.boxModel == true) {
			var delta = header.outerWidth() - header.width();
			header.width($container.width() - delta);
		} else {
			header.width($container.width());
		}

		// 设置滚动条
		setScrollers($container);

		var panels = $('.tabs-panels', $container);
		var height = opts.height;
		if (!isNaN(height)) {
			if ($.boxModel == true) {
				var delta = panels.outerHeight() - panels.height();
				panels.css('height', (height - header.outerHeight() - delta) || 'auto');
			} else {
				panels.css('height', height - header.outerHeight());
			}
		} else {
			panels.height('auto');
		}
		var width = opts.width;
		if (!isNaN(width)) {
			if ($.boxModel == true) {
				var delta = panels.outerWidth() - panels.width();
				// var delta = panels.outerWidth(true) - panels.width();
				panels.width(width - delta);
			} else {
				panels.width(width);
			}
		} else {
			panels.width('auto');
		}

		if (jDemsy.parse) {
			jDemsy.parse($container);
		}

	}

	/*
	 * 定位当前活动选项卡内容
	 */
	function fitContent($container) {
		var tab = $('>div.tabs-header ul.tabs li.tabs-selected', $container);
		if (tab.length) {
			var panel = $(tab.attr("rel"));
			var panels = $('>div.tabs-panels', $container);
			if (panels.css('height').toLowerCase() != 'auto') {
				if ($.boxModel == true) {
					panel.height(panels.height() - (panel.outerHeight() - panel.height()));
					panel.width(panels.width() - (panel.outerWidth() - panel.width()));
				} else {
					panel.height(panels.height());
					panel.width(panels.width());
				}
			}
			$('>div', panel).triggerHandler('_resize');
		}

	}

	function setProperties($container) {
		var opts = $.data($container[0], 'tabs').options;
		var header = $('>div.tabs-header', $container);
		var panels = $('>div.tabs-panels', $container);
		var tabs = $('ul.tabs', header);

		if (opts.plain == true) {
			header.addClass('tabs-header-plain');
		} else {
			header.removeClass('tabs-header-plain');
		}
		if (opts.border == true) {
			header.removeClass('tabs-header-noborder');
			panels.removeClass('tabs-panels-noborder');
		} else {
			header.addClass('tabs-header-noborder');
			panels.addClass('tabs-panels-noborder');
		}

		$('li', tabs).unbind('.tabs').bind('click.tabs', function() {
			$('.tabs-selected', tabs).removeClass('tabs-selected');
			var $this = $(this);
			$this.addClass('tabs-selected');
			$this.blur();

			$('>div.tabs-panels>div', $container).css('display', 'none');

			var wrap = $('.tabs-wrap', header);
			var leftPos = getTabLeftPosition($container, this);
			var left = leftPos - wrap.scrollLeft();
			var right = left + $this.outerWidth();
			if (left < 0 || right > wrap.innerWidth()) {
				var pos = Math.min(leftPos - (wrap.width() - $this.width()) / 2, getMaxScrollWidth($container));
				wrap.animate({
					scrollLeft : pos
				}, opts.scrollDuration);
			}

			var tabAttr = $.data(this, 'tabs.tab');
			var panel = $($this.attr("rel"));
			panel.css('display', 'block');

			if (tabAttr.href && (!tabAttr.loaded || !tabAttr.cache)) {
				panel.load(tabAttr.href, null, function() {
					if ($.parser) {
						$.parser.parse(panel);
					}
					opts.onLoad.apply(this, arguments);
					tabAttr.loaded = true;
				});
			}

			fitContent($container);

			opts.onSelect.call(panel, tabAttr.title);
		}).each(function() {
			$.data(this, 'tabs.tab', $.fn.tabs.parseTabOptions($(this)));
		});

		// 不支持关闭tab
		// $('a.tabs-close', tabs).unbind('.tabs').bind('click.tabs', function()
		// {
		// var elem = $this.parent()[0];
		// var tabAttr = $.data(elem, 'tabs.tab');
		// closeTab($container, tabAttr.title);
		// });

		$('.tabs-scroller-left', header).unbind('.tabs').bind('click.tabs', function() {
			var wrap = $('.tabs-wrap', header);
			var pos = wrap.scrollLeft() - opts.scrollIncrement;
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		}).hoverClass('tabs-scroller-over');

		$('.tabs-scroller-right', header).unbind('.tabs').bind('click.tabs', function() {
			var wrap = $('.tabs-wrap', header);
			var pos = Math.min(wrap.scrollLeft() + opts.scrollIncrement, getMaxScrollWidth($container));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		}).hoverClass('tabs-scroller-over');

		$container.bind('_resize', function() {
			var opts = $.data($container[0], 'tabs').options;
			if (opts.fit == true) {
				setSize($container);
				fitContent($container);
			}
			return false;
		});
	}

	// function createTab(container, options) {
	// var header = $('>div.tabs-header', container);
	// var tabs = $('ul.tabs', header);
	//
	// var tab = $('<li></li>');
	// var tab_span = $('<span></span>').html(options.title);
	// var tab_a = $('<a class="tabs-inner"></a>').attr('href',
	// 'javascript:void(0)').append(tab_span);
	// tab.append(tab_a).appendTo(tabs);
	//
	// // 不支持关闭tab
	// // if (options.closable) {
	// // tab_span.addClass('tabs-closable');
	// // tab_a.after('<a href="javascript:void(0)" class="tabs-close"></a>');
	// // }
	// if (options.icon) {
	// tab_span.addClass('tabs-with-icon');
	// tab_span.after($('<span/>').addClass('tabs-icon').addClass(options.icon));
	// }
	// if (options.selected) {
	// tab.addClass('tabs-selected');
	// }
	// if (options.content) {
	// $('#' + options.id).html(options.content);
	// }
	//
	// //$('#' + options.id).removeAttr('title');
	// $.data(tab[0], 'tabs.tab', {
	// id : options.id,
	// title : options.title,
	// href : options.href,
	// loaded : false,
	// cache : options.cache
	// });
	// }

	// 不支持添加tab
	// function addTab(container, options) {
	// options = $.extend({
	// id : null,
	// title : '',
	// content : '',
	// href : null,
	// cache : true,
	// icon : null,
	// closable : false,
	// selected : true,
	// height : 'auto',
	// width : 'auto'
	// }, options || {});
	//
	// if (options.selected) {
	// $('.tabs-header .tabs-wrap .tabs li',
	// container).removeClass('tabs-selected');
	// }
	// options.id = options.id || 'gen-tabs-panel' +
	// $.fn.tabs.defaults.idSeed++;
	//
	// $('<div></div>').attr('id', options.id).attr('title',
	// options.title).height(options.height).width(options.width).appendTo($('>div.tabs-panels',
	// container));
	//
	// createTab(container, options);
	// }

	// 不支持关闭tab
	// function closeTab(container, title) {
	// var opts = $.data(container, 'tabs').options;
	// var elem = $('>div.tabs-header li:has(a span:contains("' + title + '"))',
	// container)[0];
	// if (!elem)
	// return;
	//
	// var tabAttr = $.data(elem, 'tabs.tab');
	// var panel = $('#' + tabAttr.id);
	//
	// if (opts.onClose.call(panel, tabAttr.title) == false)
	// return;
	//
	// var selected = $(elem).hasClass('tabs-selected');
	// $.removeData(elem, 'tabs.tab');
	// $(elem).remove();
	// panel.remove();
	//
	// setSize(container);
	// if (selected) {
	// selectTab(container);
	// } else {
	// var wrap = $('>div.tabs-header .tabs-wrap', container);
	// var pos = Math.min(wrap.scrollLeft(), getMaxScrollWidth(container));
	// wrap.animate({
	// scrollLeft : pos
	// }, opts.scrollDuration);
	// }
	// }

	/*
	 * 选中tab项
	 */
	function selectTab($container, title) {
		// 按标题选中
		if (title) {
			var elem = $('>div.tabs-header li:has(a span:contains("' + title + '"))', $container)[0];
			if (elem) {
				$(elem).trigger('click');
			}
		} else {
			var tabs = $('>div.tabs-header ul.tabs', $container);
			var selected = $('.tabs-selected', tabs);

			if (selected.length == 0) {// 选中第一项
				$('li:first', tabs).trigger('click');
			} else {// 选中class标志的项
				selected.trigger('click');
			}
		}
	}

	// 不支持：判断是否存在
	// function exists(container, title) {
	// return $('>div.tabs-header li:has(a span:contains("' + title + '"))',
	// container).length > 0;
	// }

	$.fn.tabs = function(options, param) {
		if (typeof options == 'string') {
			switch (options) {
			case 'resize':
				return this.each(function() {
					setSize($(this));
				});
				// 不支持添加tab
				// case 'add':
				// return this.each(function() {
				// addTab(this, param);
				// $(this).tabs();
				// });
				// 不支持关闭tab
				// case 'close':
				// return this.each(function() {
				// closeTab(this, param);
				// });
			case 'select':
				return this.each(function() {
					selectTab($(this), param);
				});
				// 不支持：判断是否存在
				// case 'exists':
				// return exists(this[0], param);
			}
		}

		options = options || {};

		return this.each(function() {
			var $this = $(this);
			var state = $.data(this, 'tabs');
			if (state) {
				state.options = $.extend(state.options, options);
			} else {
				$.data(this, 'tabs', {
					options : $.extend({}, $.fn.tabs.defaults, $.fn.tabs.parseOptions(this), options)
				});
			}

			setProperties($this);
			setSize($this);
			selectTab($this);
		});
	};
	$.fn.tabs.parseOptions = function($container) {
		return $.extend({}, jDemsy.parseOptions($container, [ "width", "height", {
			fit : "boolean",
			border : "boolean",
			plain : "boolean"
		} ]));
	};
	$.fn.tabs.parseTabOptions = function($tab) {
		return jDemsy.parseOptions($tab, [ "href", {
			selected : "boolean",
			cache : "boolean"
		} ])
	};
	$.fn.tabs.defaults = {
		width : 'auto',
		height : 'auto',
		idSeed : 0,
		plain : false,
		fit : false,
		border : true,
		scrollIncrement : 100,
		scrollDuration : 400,
		onLoad : function() {
		},
		onSelect : function(title) {
		},
		onClose : function(title) {
		}
	};
})(jQuery, jDemsy);
/**
 * 2012-07-05 11:40以前, demo耗时65ms左右
 */
(function($, jDemsy) {
	function getMaxScrollWidth(container) {
		var header = $(container).children("div.tabs-header");
		var tabsWidth = 0;
		$("ul.tabs li", header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});
		var wrapWidth = header.children("div.tabs-wrap").width();
		var padding = parseInt(header.find("ul.tabs").css("padding-left"));
		return tabsWidth - wrapWidth + padding;
	}

	function setScrollers(container) {
		var opts = $.data(container, "tabs").options;
		var header = $(container).children("div.tabs-header");
		var tool = header.children("div.tabs-tool");
		var scrollerLeft = header.children("div.tabs-scroller-left");
		var scrollerRight = header.children("div.tabs-scroller-right");
		var wrap = header.children("div.tabs-wrap");
		tool._outerHeight(header.outerHeight() - (opts.plain ? 2 : 0));
		var tabsWidth = 0;
		$("ul.tabs li", header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});
		var headerWidth = header.width() - tool.outerWidth();
		if (tabsWidth > headerWidth) {
			scrollerLeft.show();
			scrollerRight.show();
			tool.css("right", scrollerRight.outerWidth());
			wrap.css({
				marginLeft : scrollerLeft.outerWidth(),
				marginRight : scrollerRight.outerWidth() + tool.outerWidth(),
				left : 0,
				width : headerWidth - scrollerLeft.outerWidth() - scrollerRight.outerWidth()
			});
		} else {
			scrollerLeft.hide();
			scrollerRight.hide();
			tool.css("right", 0);
			wrap.css({
				marginLeft : 0,
				marginRight : tool.outerWidth(),
				left : 0,
				width : headerWidth
			});
			wrap.scrollLeft(0);
		}
	}

	function setTool(container) {
		var opts = $.data(container, "tabs").options;
		var header = $(container).children("div.tabs-header");
		if (opts.tools) {
			if (typeof opts.tools == "string") {
				$(opts.tools).addClass("tabs-tool").appendTo(header);
				$(opts.tools).show();
			} else {
				header.children("div.tabs-tool").remove();
				var toolContainer = $("<div class=\"tabs-tool\"></div>").appendTo(header);
				for ( var i = 0; i < opts.tools.length; i++) {
					var tool = $("<a href=\"javascript:void(0);\"></a>").appendTo(toolContainer);
					tool[0].onclick = eval(opts.tools[i].handler || function() {
					});
					tool.linkbutton($.extend({}, opts.tools[i], {
						plain : true
					}));
				}
			}
		} else {
			header.children("div.tabs-tool").remove();
		}
	}

	function setSize(container) {
		var opts = $.data(container, "tabs").options;
		var cc = $(container);
		if (opts.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
			opts.width = p.width();
			opts.height = p.height();
		}
		cc.width(opts.width).height(opts.height);
		var header = $(container).children("div.tabs-header");
		header._outerWidth(opts.width);
		setScrollers(container);
		var panels = $(container).children("div.tabs-panels");
		var height = opts.height;
		if (!isNaN(height)) {
			panels._outerHeight(height - header.outerHeight());
		} else {
			panels.height("auto");
		}
		var width = opts.width;
		if (!isNaN(width)) {
			panels._outerWidth(width);
		} else {
			panels.width("auto");
		}
	}

	function fitContent(container) {
		var opts = $.data(container, "tabs").options;
		var tab = getSelected(container);
		if (tab) {
			var panels = $(container).children("div.tabs-panels");
			var width = opts.width == "auto" ? "auto" : panels.width();
			var height = opts.height == "auto" ? "auto" : panels.height();
			tab.panel("resize", {
				width : width,
				height : height
			});
		}
	}

	function wrapTabs(container) {
		var cc = $(container);
		cc.addClass("tabs-container");
		cc.wrapInner("<div class=\"tabs-panels\"/>");
		$("<div class=\"tabs-header\">" + "<div class=\"tabs-scroller-left\"></div>" + "<div class=\"tabs-scroller-right\"></div>" + "<div class=\"tabs-wrap\">" + "<ul class=\"tabs\"></ul>" + "</div>" + "</div>").prependTo(container);
		var tabs = [];
		var tp = cc.children("div.tabs-panels");
		tp.children("div").each(function() {
			var opts = $.extend({}, jDemsy.parseOptions(this), {
				selected : ($(this).attr("selected") ? true : undefined)
			});
			var pp = $(this);
			tabs.push(pp);
			createTab(container, pp, opts);
		});
		cc.children("div.tabs-header").find(".tabs-scroller-left, .tabs-scroller-right").hover(function() {
			$(this).addClass("tabs-scroller-over");
		}, function() {
			$(this).removeClass("tabs-scroller-over");
		});
		cc.bind("_resize", function(e, _28d) {
			var opts = $.data(container, "tabs").options;
			if (opts.fit == true || _28d) {
				setSize(container);
				fitContent(container);
			}
			return false;
		});
		return tabs;
	}

	function setProperties(container) {
		var opts = $.data(container, "tabs").options;
		var header = $(container).children("div.tabs-header");
		var panels = $(container).children("div.tabs-panels");
		if (opts.plain == true) {
			header.addClass("tabs-header-plain");
		} else {
			header.removeClass("tabs-header-plain");
		}
		if (opts.border == true) {
			header.removeClass("tabs-header-noborder");
			panels.removeClass("tabs-panels-noborder");
		} else {
			header.addClass("tabs-header-noborder");
			panels.addClass("tabs-panels-noborder");
		}
		$(".tabs-scroller-left", header).unbind(".tabs").bind("click.tabs", function() {
			var wrap = $(".tabs-wrap", header);
			var pos = wrap.scrollLeft() - opts.scrollIncrement;
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		});
		$(".tabs-scroller-right", header).unbind(".tabs").bind("click.tabs", function() {
			var wrap = $(".tabs-wrap", header);
			var pos = Math.min(wrap.scrollLeft() + opts.scrollIncrement, getMaxScrollWidth(container));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		});
		var tabs = $.data(container, "tabs").tabs;
		for ( var i = 0, len = tabs.length; i < len; i++) {
			var _292 = tabs[i];
			var tab = _292.panel("options").tab;
			tab.unbind(".tabs").bind("click.tabs", {
				p : _292
			}, function(e) {
				select(container, getTabIndex(container, e.data.p));
			}).bind("contextmenu.tabs", {
				p : _292
			}, function(e) {
				opts.onContextMenu.call(container, e, e.data.p.panel("options").title, getTabIndex(container, e.data.p));
			});
			tab.find("a.tabs-close").unbind(".tabs").bind("click.tabs", {
				p : _292
			}, function(e) {
				closeTab(container, getTabIndex(container, e.data.p));
				return false;
			});
		}
	}

	function createTab(container, pp, options) {
		options = options || {};
		pp.panel($.extend({}, options, {
			border : false,
			noheader : true,
			closed : true,
			doSize : false,
			iconCls : (options.icon ? options.icon : undefined),
			onLoad : function() {
				if (options.onLoad) {
					options.onLoad.call(this, arguments);
				}
				$.data(container, "tabs").options.onLoad.call(container, pp);
			}
		}));
		var opts = pp.panel("options");
		var header = $(container).children("div.tabs-header");
		var tabs = $("ul.tabs", header);
		var tab = $("<li></li>").appendTo(tabs);
		var tab_a = $("<a href=\"javascript:void(0)\" class=\"tabs-inner\"></a>").appendTo(tab);
		var tab_title = $("<span class=\"tabs-title\"></span>").html(opts.title).appendTo(tab_a);
		var tab_icon = $("<span class=\"tabs-icon\"></span>").appendTo(tab_a);
		if (opts.closable) {
			tab_title.addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
		}
		if (opts.iconCls) {
			tab_title.addClass("tabs-with-icon");
			tab_icon.addClass(opts.iconCls);
		}
		if (opts.tools) {
			var tab_tool = $("<span class=\"tabs-p-tool\"></span>").insertAfter(tab_a);
			if (typeof opts.tools == "string") {
				$(opts.tools).children().appendTo(tab_tool);
			} else {
				for ( var i = 0; i < opts.tools.length; i++) {
					var t = $("<a href=\"javascript:void(0)\"></a>").appendTo(tab_tool);
					t.addClass(opts.tools[i].iconCls);
					if (opts.tools[i].handler) {
						t.bind("click", eval(opts.tools[i].handler));
					}
				}
			}
			var pr = tab_tool.children().length * 12;
			if (opts.closable) {
				pr += 8;
			} else {
				pr -= 3;
				tab_tool.css("right", "5px");
			}
			tab_title.css("padding-right", pr + "px");
		}
		opts.tab = tab;
	}

	function addTab(container, options) {
		var opts = $.data(container, "tabs").options;
		var tabs = $.data(container, "tabs").tabs;
		if (options.selected == undefined) {
			options.selected = true;
		}
		var pp = $("<div></div>").appendTo($(container).children("div.tabs-panels"));
		tabs.push(pp);
		createTab(container, pp, options);
		opts.onAdd.call(container, options.title, tabs.length - 1);
		setScrollers(container);
		setProperties(container);
		if (options.selected) {
			select(container, tabs.length - 1);
		}
	}

	function update(container, param) {
		var selectHis = $.data(container, "tabs").selectHis;
		var pp = param.tab;
		var title = pp.panel("options").title;
		pp.panel($.extend({}, param.options, {
			iconCls : (param.options.icon ? param.options.icon : undefined)
		}));
		var opts = pp.panel("options");
		var tab = opts.tab;
		tab.find("span.tabs-icon").attr("class", "tabs-icon");
		tab.find("a.tabs-close").remove();
		tab.find("span.tabs-title").html(opts.title);
		if (opts.closable) {
			tab.find("span.tabs-title").addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-closable");
		}
		if (opts.iconCls) {
			tab.find("span.tabs-title").addClass("tabs-with-icon");
			tab.find("span.tabs-icon").addClass(opts.iconCls);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-with-icon");
		}
		if (title != opts.title) {
			for ( var i = 0; i < selectHis.length; i++) {
				if (selectHis[i] == title) {
					selectHis[i] = opts.title;
				}
			}
		}
		setProperties(container);
		$.data(container, "tabs").options.onUpdate.call(container, opts.title, getTabIndex(container, pp));
	}

	function closeTab(container, title) {
		var opts = $.data(container, "tabs").options;
		var tabs = $.data(container, "tabs").tabs;
		var selectHis = $.data(container, "tabs").selectHis;
		if (!exists(container, title)) {
			return;
		}
		var tab = getTab(container, title);
		var title = tab.panel("options").title;
		var index = getTabIndex(container, tab);
		if (opts.onBeforeClose.call(container, title, index) == false) {
			return;
		}
		var tab = getTab(container, title, true);
		tab.panel("options").tab.remove();
		tab.panel("destroy");
		opts.onClose.call(container, title, index);
		setScrollers(container);
		for ( var i = 0; i < selectHis.length; i++) {
			if (selectHis[i] == title) {
				selectHis.splice(i, 1);
				i--;
			}
		}
		var _2ad = selectHis.pop();
		if (_2ad) {
			select(container, _2ad);
		} else {
			if (tabs.length) {
				select(container, 0);
			}
		}
	}

	function getTab(container, title, _2b0) {
		var tabs = $.data(container, "tabs").tabs;
		if (typeof title == "number") {
			if (title < 0 || title >= tabs.length) {
				return null;
			} else {
				var tab = tabs[title];
				if (_2b0) {
					tabs.splice(title, 1);
				}
				return tab;
			}
		}
		for ( var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").title == title) {
				if (_2b0) {
					tabs.splice(i, 1);
				}
				return tab;
			}
		}
		return null;
	}

	function getTabIndex(container, tab) {
		var tabs = $.data(container, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			if (tabs[i][0] == $(tab)[0]) {
				return i;
			}
		}
		return -1;
	}

	function getSelected(container) {
		var tabs = $.data(container, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").closed == false) {
				return tab;
			}
		}
		return null;
	}

	function setSelected(container) {
		var tabs = $.data(container, "tabs").tabs;
		for ( var i = 0; i < tabs.length; i++) {
			if (tabs[i].panel("options").selected) {
				select(container, i);
				return;
			}
		}
		if (tabs.length) {
			select(container, 0);
		}
	}

	function select(container, index) {
		var opts = $.data(container, "tabs").options;
		var tabs = $.data(container, "tabs").tabs;
		var selectHis = $.data(container, "tabs").selectHis;
		if (tabs.length == 0) {
			return;
		}
		var tab = getTab(container, index);
		if (!tab) {
			return;
		}
		var selectedTab = getSelected(container);
		if (selectedTab) {
			selectedTab.panel("close");
			selectedTab.panel("options").tab.removeClass("tabs-selected");
		}
		tab.panel("open");
		var title = tab.panel("options").title;
		selectHis.push(title);
		var tab = tab.panel("options").tab;
		tab.addClass("tabs-selected");
		var wrap = $(container).find(">div.tabs-header div.tabs-wrap");
		var _2bb = tab.position().left + wrap.scrollLeft();
		var left = _2bb - wrap.scrollLeft();
		var _2bc = left + tab.outerWidth();
		if (left < 0 || _2bc > wrap.innerWidth()) {
			var pos = Math.min(_2bb - (wrap.width() - tab.width()) / 2, getMaxScrollWidth(container));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		} else {
			var pos = Math.min(wrap.scrollLeft(), getMaxScrollWidth(container));
			wrap.animate({
				scrollLeft : pos
			}, opts.scrollDuration);
		}
		fitContent(container);
		opts.onSelect.call(container, title, getTabIndex(container, tab));
	}

	function exists(container, title) {
		return getTab(container, title) != null;
	}

	$.fn.tabs = function(options, args) {
		var beginTime = new Date().getTime();

		if (typeof options == "string") {
			return $.fn.tabs.methods[options](this, args);
		}
		options = options || {};
		var ret = this.each(function() {
			var tabsObj = $.data(this, "tabs");
			var opts;
			if (tabsObj) {
				opts = $.extend(tabsObj.options, options);
				tabsObj.options = opts;
			} else {
				$.data(this, "tabs", {
					options : $.extend({}, $.fn.tabs.defaults, $.fn.tabs.parseOptions(this), options),
					tabs : wrapTabs(this),
					selectHis : []
				});
			}
			setTool(this);
			setProperties(this);
			setSize(this);
			setSelected(this);
		});

		jDemsy.log("创建选项卡对象tabs (length = {1})", beginTime, ret.length);

		return ret;
	};
	$.fn.tabs.methods = {
		options : function(jq) {
			return $.data(jq[0], "tabs").options;
		},
		tabs : function(jq) {
			return $.data(jq[0], "tabs").tabs;
		},
		resize : function(jq) {
			return jq.each(function() {
				setSize(this);
				fitContent(this);
			});
		},
		add : function(jq, param) {
			return jq.each(function() {
				addTab(this, param);
			});
		},
		close : function(jq, param) {
			return jq.each(function() {
				closeTab(this, param);
			});
		},
		getTab : function(jq, title) {
			return getTab(jq[0], title);
		},
		getTabIndex : function(jq, tab) {
			return getTabIndex(jq[0], tab);
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		select : function(jq, index) {
			return jq.each(function() {
				select(this, index);
			});
		},
		exists : function(jq, title) {
			return exists(jq[0], title);
		},
		update : function(jq, param) {
			return jq.each(function() {
				update(this, param);
			});
		}
	};
	$.fn.tabs.parseOptions = function(container) {
		return $.extend({}, jDemsy.parseOptions(container, [ "width", "height", "tools", {
			fit : "boolean",
			border : "boolean",
			plain : "boolean"
		} ]));
	};
	$.fn.tabs.defaults = {
		width : "auto",
		height : "auto",
		plain : false,
		fit : false,
		border : true,
		tools : null,
		scrollIncrement : 100,
		scrollDuration : 400,
		onLoad : function(_2c9) {
		},
		onSelect : function(title, index) {
		},
		onBeforeClose : function(title, index) {
		},
		onClose : function(title, index) {
		},
		onAdd : function(title, index) {
		},
		onUpdate : function(title, index) {
		},
		onContextMenu : function(e, _2d4, _2d5) {
		}
	};
})(jQuery, jDemsy);
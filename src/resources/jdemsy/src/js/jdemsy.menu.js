/**
 * linkbutton
 */
(function($) {
	function createButton(target) {
		var opts = $.data(target, "linkbutton").options;
		$(target).empty();
		$(target).addClass("l-btn");
		if (opts.id) {
			$(target).attr("id", opts.id);
		} else {
			$(target).attr("id", "");
		}
		if (opts.plain) {
			$(target).addClass("l-btn-plain");
		} else {
			$(target).removeClass("l-btn-plain");
		}
		if (opts.text) {
			$(target).html(opts.text).wrapInner("<span class=\"l-btn-left\">" + "<span class=\"l-btn-text\">" + "</span>" + "</span>");
			if (opts.iconCls) {
				$(target).find(".l-btn-text").addClass(opts.iconCls).css("padding-left", "20px");
			}
		} else {
			$(target).html("&nbsp;").wrapInner("<span class=\"l-btn-left\">" + "<span class=\"l-btn-text\">" + "<span class=\"l-btn-empty\"></span>" + "</span>" + "</span>");
			if (opts.iconCls) {
				$(target).find(".l-btn-empty").addClass(opts.iconCls);
			}
		}
		$(target).unbind(".linkbutton").bind("focus.linkbutton", function() {
			if (!opts.disabled) {
				$(this).find("span.l-btn-text").addClass("l-btn-focus");
			}
		}).bind("blur.linkbutton", function() {
			$(this).find("span.l-btn-text").removeClass("l-btn-focus");
		});
		setDisabled(target, opts.disabled);
	}
	;
	function setDisabled(target, disabled) {
		var state = $.data(target, "linkbutton");
		if (disabled) {
			state.options.disabled = true;
			var href = $(target).attr("href");
			if (href) {
				state.href = href;
				$(target).attr("href", "javascript:void(0)");
			}
			if (target.onclick) {
				state.onclick = target.onclick;
				target.onclick = null;
			}
			$(target).addClass("l-btn-disabled");
		} else {
			state.options.disabled = false;
			if (state.href) {
				$(target).attr("href", state.href);
			}
			if (state.onclick) {
				target.onclick = state.onclick;
			}
			$(target).removeClass("l-btn-disabled");
		}
	}
	;
	$.fn.linkbutton = function(options, _68) {
		if (typeof options == "string") {
			return $.fn.linkbutton.methods[options](this, _68);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "linkbutton");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "linkbutton", {
					options : $.extend({}, $.fn.linkbutton.defaults, $.fn.linkbutton.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			createButton(this);
		});
	};
	$.fn.linkbutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "linkbutton").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				setDisabled(this, false);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				setDisabled(this, true);
			});
		}
	};
	$.fn.linkbutton.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [ "id", "iconCls", {
			plain : "boolean"
		} ]), {
			disabled : (t.attr("disabled") ? true : undefined),
			text : $.trim(t.html()),
			iconCls : (t.attr("icon") || t.attr("iconCls"))
		});
	};
	$.fn.linkbutton.defaults = {
		id : null,
		disabled : false,
		plain : false,
		text : "",
		iconCls : null
	};
})(jQuery);

/**
 * menu
 */
(function($) {
	function init(target) {
		$(target).appendTo("body");
		$(target).addClass("menu-top");
		var menus = [];
		adjust($(target));
		var time = null;
		for ( var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			wrapMenu(menu);
			menu.children("div.menu-item").each(function() {
				_31a(target, $(this));
			});
			menu.bind("mouseenter", function() {
				if (time) {
					clearTimeout(time);
					time = null;
				}
			}).bind("mouseleave", function() {
				time = setTimeout(function() {
					hideAll(target);
				}, 100);
			});
		}
		function adjust(menu) {
			menus.push(menu);
			menu.find(">div").each(function() {
				var item = $(this);
				var submenu = item.find(">div");
				if (submenu.length) {
					submenu.insertAfter(target);
					item[0].submenu = submenu;
					adjust(submenu);
				}
			});
		}
		;
		function wrapMenu(menu) {
			menu.addClass("menu").find(">div").each(function() {
				var item = $(this);
				if (item.hasClass("menu-sep")) {
					item.html("&nbsp;");
				} else {
					var options = $.extend({}, $.parser.parseOptions(this, [ "name", "iconCls", "href" ]), {
						disabled : (item.attr("disabled") ? true : undefined)
					});
					item.attr("name", options.name || "").attr("href", options.href || "");
					var text = item.addClass("menu-item").html();
					item.empty().append($("<div class=\"menu-text\"></div>").html(text));
					if (options.iconCls) {
						$("<div class=\"menu-icon\"></div>").addClass(options.iconCls).appendTo(item);
					}
					if (options.disabled) {
						enableItem(target, item[0], true);
					}
					if (item[0].submenu) {
						$("<div class=\"menu-rightarrow\"></div>").appendTo(item);
					}
					item._outerHeight(22);
				}
			});
			menu.hide();
		}
		;
	}
	;
	function _31a(_31b, item) {
		item.unbind(".menu");
		item.bind("mousedown.menu", function() {
			return false;
		}).bind("click.menu", function() {
			if ($(this).hasClass("menu-item-disabled")) {
				return;
			}
			if (!this.submenu) {
				hideAll(_31b);
				var href = $(this).attr("href");
				if (href) {
					location.href = href;
				}
			}
			var item = $(_31b).menu("getItem", this);
			$.data(_31b, "menu").options.onClick.call(_31b, item);
		}).bind("mouseenter.menu", function(e) {
			item.siblings().each(function() {
				if (this.submenu) {
					hideMenu(this.submenu);
				}
				$(this).removeClass("menu-active");
			});
			item.addClass("menu-active");
			if ($(this).hasClass("menu-item-disabled")) {
				item.addClass("menu-active-disabled");
				return;
			}
			var _31c = item[0].submenu;
			if (_31c) {
				var left = item.offset().left + item.outerWidth() - 2;
				if (left + _31c.outerWidth() + 5 > $(window).width() + $(document).scrollLeft()) {
					left = item.offset().left - _31c.outerWidth() + 2;
				}
				var top = item.offset().top - 3;
				if (top + _31c.outerHeight() > $(window).height() + $(document).scrollTop()) {
					top = $(window).height() + $(document).scrollTop() - _31c.outerHeight() - 5;
				}
				showTopMenu(_31c, {
					left : left,
					top : top
				});
			}
		}).bind("mouseleave.menu", function(e) {
			item.removeClass("menu-active menu-active-disabled");
			var _31d = item[0].submenu;
			if (_31d) {
				if (e.pageX >= parseInt(_31d.css("left"))) {
					item.addClass("menu-active");
				} else {
					hideMenu(_31d);
				}
			} else {
				item.removeClass("menu-active");
			}
		});
	}
	;
	function hideAll(target) {
		var opts = $.data(target, "menu").options;
		hideMenu($(target));
		$(document).unbind(".menu");
		opts.onHide.call(target);
		return false;
	}
	;
	function showMenu(target, pos) {
		var opts = $.data(target, "menu").options;
		if (pos) {
			opts.left = pos.left;
			opts.top = pos.top;
			if (opts.left + $(target).outerWidth() > $(window).width() + $(document).scrollLeft()) {
				opts.left = $(window).width() + $(document).scrollLeft() - $(target).outerWidth() - 5;
			}
			if (opts.top + $(target).outerHeight() > $(window).height() + $(document).scrollTop()) {
				opts.top -= $(target).outerHeight();
			}
		}
		showTopMenu($(target), {
			left : opts.left,
			top : opts.top
		}, function() {
			$(document).unbind(".menu").bind("mousedown.menu", function() {
				hideAll(target);
				$(document).unbind(".menu");
				return false;
			});
			opts.onShow.call(target);
		});
	}
	;
	function showTopMenu(menu, pos, callback) {
		if (!menu) {
			return;
		}
		if (pos) {
			menu.css(pos);
		}
		menu.show(0, function() {
			if (!menu[0].shadow) {
				menu[0].shadow = $("<div class=\"menu-shadow\"></div>").insertAfter(menu);
			}
			menu[0].shadow.css({
				display : "block",
				zIndex : $.fn.menu.defaults.zIndex++,
				left : menu.css("left"),
				top : menu.css("top"),
				width : menu.outerWidth(),
				height : menu.outerHeight()
			});
			menu.css("z-index", $.fn.menu.defaults.zIndex++);
			if (callback) {
				callback();
			}
		});
	}
	;
	function hideMenu(menu) {
		if (!menu) {
			return;
		}
		hideit(menu);
		menu.find("div.menu-item").each(function() {
			if (this.submenu) {
				hideMenu(this.submenu);
			}
			$(this).removeClass("menu-active");
		});
		function hideit(m) {
			m.stop(true, true);
			if (m[0].shadow) {
				m[0].shadow.hide();
			}
			m.hide();
		}
		;
	}
	;
	function findItem(_327, text) {
		var _328 = null;
		var tmp = $("<div></div>");
		function find(menu) {
			menu.children("div.menu-item").each(function() {
				var item = $(_327).menu("getItem", this);
				var s = tmp.empty().html(item.text).text();
				if (text == $.trim(s)) {
					_328 = item;
				} else {
					if (this.submenu && !_328) {
						find(this.submenu);
					}
				}
			});
		}
		;
		find($(_327));
		tmp.remove();
		return _328;
	}
	;
	function enableItem(_329, _32a, _32b) {
		var t = $(_32a);
		if (_32b) {
			t.addClass("menu-item-disabled");
			if (_32a.onclick) {
				_32a.onclick1 = _32a.onclick;
				_32a.onclick = null;
			}
		} else {
			t.removeClass("menu-item-disabled");
			if (_32a.onclick1) {
				_32a.onclick = _32a.onclick1;
				_32a.onclick1 = null;
			}
		}
	}
	;
	function appendItem(_32d, _32e) {
		var menu = $(_32d);
		if (_32e.parent) {
			menu = _32e.parent.submenu;
		}
		var item = $("<div class=\"menu-item\"></div>").appendTo(menu);
		$("<div class=\"menu-text\"></div>").html(_32e.text).appendTo(item);
		if (_32e.iconCls) {
			$("<div class=\"menu-icon\"></div>").addClass(_32e.iconCls).appendTo(item);
		}
		if (_32e.id) {
			item.attr("id", _32e.id);
		}
		if (_32e.href) {
			item.attr("href", _32e.href);
		}
		if (_32e.name) {
			item.attr("name", _32e.name);
		}
		if (_32e.onclick) {
			if (typeof _32e.onclick == "string") {
				item.attr("onclick", _32e.onclick);
			} else {
				item[0].onclick = eval(_32e.onclick);
			}
		}
		if (_32e.handler) {
			item[0].onclick = eval(_32e.handler);
		}
		_31a(_32d, item);
		if (_32e.disabled) {
			enableItem(_32d, item[0], true);
		}
	}
	;
	function removeItem(_330, _331) {
		function _332(el) {
			if (el.submenu) {
				el.submenu.children("div.menu-item").each(function() {
					_332(this);
				});
				var _333 = el.submenu[0].shadow;
				if (_333) {
					_333.remove();
				}
				el.submenu.remove();
			}
			$(el).remove();
		}
		;
		_332(_331);
	}
	;
	function destroy(target) {
		$(target).children("div.menu-item").each(function() {
			removeItem(target, this);
		});
		if (target.shadow) {
			target.shadow.remove();
		}
		$(target).remove();
	}
	;
	$.fn.menu = function(options, param) {
		if (typeof options == "string") {
			return $.fn.menu.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "menu");
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $.data(this, "menu", {
					options : $.extend({}, $.fn.menu.defaults, $.fn.menu.parseOptions(this), options)
				});
				init(this);
			}
			$(this).css({
				left : state.options.left,
				top : state.options.top
			});
		});
	};
	$.fn.menu.methods = {
		show : function(jq, pos) {
			return jq.each(function() {
				showMenu(this, pos);
			});
		},
		hide : function(jq) {
			return jq.each(function() {
				hideAll(this);
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				destroy(this);
			});
		},
		setText : function(jq, param) {
			return jq.each(function() {
				$(param.target).children("div.menu-text").html(param.text);
			});
		},
		setIcon : function(jq, param) {
			return jq.each(function() {
				var item = $(this).menu("getItem", param.target);
				if (item.iconCls) {
					$(item.target).children("div.menu-icon").removeClass(item.iconCls).addClass(param.iconCls);
				} else {
					$("<div class=\"menu-icon\"></div>").addClass(param.iconCls).appendTo(param.target);
				}
			});
		},
		getItem : function(jq, target) {
			var t = $(target);
			var item = {
				target : target,
				id : t.attr("id"),
				text : $.trim(t.children("div.menu-text").html()),
				disabled : t.hasClass("menu-item-disabled"),
				href : t.attr("href"),
				name : t.attr("name"),
				onclick : target.onclick
			};
			var icon = t.children("div.menu-icon");
			if (icon.length) {
				var cc = [];
				var aa = icon.attr("class").split(" ");
				for ( var i = 0; i < aa.length; i++) {
					if (aa[i] != "menu-icon") {
						cc.push(aa[i]);
					}
				}
				item.iconCls = cc.join(" ");
			}
			return item;
		},
		findItem : function(jq, text) {
			return findItem(jq[0], text);
		},
		appendItem : function(jq, param) {
			return jq.each(function() {
				appendItem(this, param);
			});
		},
		removeItem : function(jq, param) {
			return jq.each(function() {
				removeItem(this, param);
			});
		},
		enableItem : function(jq, param) {
			return jq.each(function() {
				enableItem(this, param, false);
			});
		},
		disableItem : function(jq, param) {
			return jq.each(function() {
				enableItem(this, param, true);
			});
		}
	};
	$.fn.menu.parseOptions = function(target) {
		return $.extend({}, $.parser.parseOptions(target, [ "left", "top" ]));
	};
	$.fn.menu.defaults = {
		zIndex : 110000,
		left : 0,
		top : 0,
		onShow : function() {
		},
		onHide : function() {
		},
		onClick : function(item) {
		}
	};
})(jQuery);

/**
 * menubutton
 */
(function($) {
	function init(target) {
		var opts = $.data(target, "menubutton").options;
		var btn = $(target);
		btn.removeClass("m-btn-active m-btn-plain-active").addClass("m-btn");
		btn.linkbutton($.extend({}, opts, {
			text : opts.text + "<span class=\"m-btn-downarrow\">&nbsp;</span>"
		}));
		if (opts.menu) {
			$(opts.menu).menu({
				onShow : function() {
					btn.addClass((opts.plain == true) ? "m-btn-plain-active" : "m-btn-active");
				},
				onHide : function() {
					btn.removeClass((opts.plain == true) ? "m-btn-plain-active" : "m-btn-active");
				}
			});
		}
		disable(target, opts.disabled);
	}
	;
	function disable(target, disabled) {
		var opts = $.data(target, "menubutton").options;
		opts.disabled = disabled;
		var btn = $(target);
		if (disabled) {
			btn.linkbutton("disable");
			btn.unbind(".menubutton");
		} else {
			btn.linkbutton("enable");
			btn.unbind(".menubutton");
			btn.bind("click.menubutton", function() {
				showMenu();
				return false;
			});
			var timeout = null;
			btn.bind("mouseenter.menubutton", function() {
				timeout = setTimeout(function() {
					showMenu();
				}, opts.duration);
				return false;
			}).bind("mouseleave.menubutton", function() {
				if (timeout) {
					clearTimeout(timeout);
				}
			});
		}
		function showMenu() {
			if (!opts.menu) {
				return;
			}
			var left = btn.offset().left;
			if (left + $(opts.menu).outerWidth() + 5 > $(window).width()) {
				left = $(window).width() - $(opts.menu).outerWidth() - 5;
			}
			$("body>div.menu-top").menu("hide");
			$(opts.menu).menu("show", {
				left : left,
				top : btn.offset().top + btn.outerHeight()
			});
			btn.blur();
		}
		;
	}
	;
	$.fn.menubutton = function(options, param) {
		if (typeof options == "string") {
			return $.fn.menubutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "menubutton");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "menubutton", {
					options : $.extend({}, $.fn.menubutton.defaults, $.fn.menubutton.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			init(this);
		});
	};
	$.fn.menubutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "menubutton").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				disable(this, false);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				disable(this, true);
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var opts = $(this).menubutton("options");
				if (opts.menu) {
					$(opts.menu).menu("destroy");
				}
				$(this).remove();
			});
		}
	};
	$.fn.menubutton.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.linkbutton.parseOptions(target), $.parser.parseOptions(target, [ "menu", {
			plain : "boolean",
			duration : "number"
		} ]));
	};
	$.fn.menubutton.defaults = $.extend({}, $.fn.linkbutton.defaults, {
		plain : true,
		menu : null,
		duration : 100
	});
})(jQuery);
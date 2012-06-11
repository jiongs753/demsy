(function($) {
	function _rf() {
		return false;
	}
	$.demsy = {
		zIndex : function(v) {
			if (!this.zindex) {
				this.zindex = 999;
			}
			if (v && v > this.zindex) {
				this.zindex = v;
			}
			return this.zindex++;
		},
		stringLen : function(value, igloreZH) {
			if (value == null)
				return 0;
			if (igloreZH)
				return value.length;
			var str, num = 0;
			for ( var i = 0; i < value.length; i++) {
				str = value.substring(i, i + 1);
				if (str <= "~") {
					num += 1;
				} else {
					num += 2;
				}
			}
			return num;
		},
		substring : function(value, from, to, igloreZH) {
			if (igloreZH) {
				return value.substring(from, to);
			}
			var str, num = 0;
			for ( var i = 0; i < value.length; i++) {
				str = value.substring(i, i + 1);
				if (str <= "~") {
					num += 1;
				} else {
					num += 2;
				}
				if (num > to) {
					return value.substring(from, i);
				}
			}
			return value.substring(from, to);
		},
		inject : function(self, methods) {
			var n;
			for (n in methods) {
				self["super_" + n] = self[n];
			}
			$.extend(self, methods);
		}
	};
	$.fn.extend({
		cssVal : function(style) {
			return parseInt(this.css(style)) || 0;
		}
	});
	var menu = {
		buildGroup : function() {
			var self = this, cfg = this.config, klass = this.klass;
			var grpTpl = "<DIV unselectable=on class='" + klass.name + "_panel hide'><DIV unselectable=on class='" + klass.scroll + "L hide'></DIV><DIV unselectable=on class='" + klass.name
					+ "_mover_panel'><UL unselectable=on class='" + klass.name + "_mover'></UL></DIV><DIV unselectable=on class='" + klass.scroll + "R hide'></DIV></DIV>";
			var grp = $(grpTpl.replace(/\$\[([^\]]+)\]/g, function() {
				return cfg[arguments[1]] || "";
			})).bind("contextmenu", _rf).width(cfg.vertical ? cfg.maxWidth || "auto" : "auto").height(cfg.vertical ? "auto" : cfg.maxHeight || "auto");
			grp.bind("mousewheel", function(event) {
				self.hideGroup(grp.activeItem);
				$.Scrollable.scroll(grp);
			});
			grp.leftScroll = $("." + klass.scroll + "L", grp).click(function() {
				self.hideGroup(grp.activeItem);
				$.Scrollable.moveLeft(grp);
			}).bind("mouseover", function() {
				var l = $(this);
				l.addClass(klass.scroll + "L_over");
				l.bind("mouseout", function() {
					l.unbind("mouseout");
					l.removeClass(klass.scroll + "L_over");
				});
			}).css("z-index", 999);
			grp.rightScroll = $("." + klass.scroll + "R", grp).click(function() {
				self.hideGroup(grp.activeItem);
				$.Scrollable.moveRight(grp);
			}).bind("mouseover", function() {
				var l = $(this);
				l.addClass(klass.scroll + "R_over");
				l.bind("mouseout", function() {
					l.unbind("mouseout");
					l.removeClass(klass.scroll + "R_over");
				});
			}).css("z-index", 999);
			grp.moverPanel = $("." + klass.name + "_mover_panel", grp).css("z-index", 1);
			grp.mover = $("." + klass.name + "_mover", grp);
			grp.menu = self;
			$.extend(grp, cfg || {});
			return grp;
		},
		buildItem : function(itm) {
			var cfg = this.config, klass = this.klass.item;
			var btnTpl = "<LI unselectable=on class='" + klass + "'><TABLE unselectable=on class='" + klass + "' cellPadding=0 cellSpacing=0><TR unselectable=on><TD unselectable=on class='" + klass
					+ "L'></TD><TD unselectable=on class='" + klass + "C'><div unselectable=on class='" + klass + "_title'><div title='$[title]' unselectable=on class='" + klass
					+ "_text'>$[innerText]</div></div></TD><TD unselectable=on class='" + klass + "R'></TD></TR></TABLE></LI>";
			var btn = $(btnTpl.replace(/\$\[([^\]]+)\]/g, function() {
				return itm[arguments[1]] || "";
			}));
			if (itm.disable) {
				btn.addClass(klass + "_dis");
			}
			btn.button = $("table." + klass, btn);
			if (cfg.icon && itm.icon && itm.icon.length > 0) {
				var icon = itm.icon;
				if (icon.indexOf("/") < 0) {
					icon = (cfg.iconPath || "") + "/" + icon;
				}
				icon = cfg.contextPath + icon;
				$("." + klass + "_title", btn).css("background", "transparent url(" + icon + ") no-repeat 0 2px").addClass(klass + "_icon");
				if (itm.text.length == 0 && !itm.items && !cfg.vertical) {
					$("." + klass + "_title", btn).css("background-position", "center");
				}
			}
			var text = $("." + klass + "_text", btn);
			if (cfg.arrow && itm.items) {
				text.addClass(klass + "_arrow");
			}
			text.css(cfg.textCss);
			return btn;
		},
		buildFix : function(btn) {
			return $("<div class='" + this.klass.name + "_fix'></div>").appendTo(this);
		},
		parseItems : function(grp, obj, cb) {
			var items = obj.config.items;
			if (!grp.loading) {
				grp.loading = true;
				if ($.isArray(items)) {
					this.addItems(grp, items, cb);
				} else {
					if (typeof items == "string") {
						var self = this, cfg = this.config;
						var klass = ((obj.button && obj.pGroup) ? obj.pGroup.menu.klass.item : self.klass.item);
						var txt = $("." + klass + "_text", obj);
						txt.length == 0 ? obj.addClass("rootloading") : txt.addClass(klass + "_loading").removeClass(klass + "_arrow");
						setTimeout(function() {
							$.ajax({
								url : items,
								success : function(json) {
									txt.length == 0 ? obj.removeClass("rootloading") : (self.config.arrow ? txt.removeClass(klass + "_loading").addClass(klass + "_arrow") : txt.addClass(klass
											+ "_arrow"));
									obj.config.items = json;
									self.addItems(grp, json, cb);
								},
								complete : function(json) {
									txt.length == 0 ? obj.removeClass("rootloading") : (self.config.arrow ? txt.removeClass(klass + "_loading").addClass(klass + "_arrow") : txt.addClass(klass
											+ "_arrow"));
									grp.loading = false;
								},
								type : "GET",
								dataType : "json",
								async : true
							});
						}, 100);
					}
				}
			}
		},
		parseItem : function(itm) {
			var cfg = this.config;
			itm.innerText = itm.text || "";
			if ($.demsy.stringLen(itm.text) > cfg.titleSize) {
				itm.title = itm.text;
				itm.innerText = $.demsy.substring(itm.text, 0, this.config.titleSize - 1) + "...";
			}
		},
		addItems : function(grp, itms, cb) {
			var self = this;
			$.each(itms, function() {
				var itm = this;
				if (itm == "") {
					return;
				}
				if (itm == "-") {
					$("<LI class='" + self.klass.item + " " + self.klass.name + "_split'><SPAN unselectable=on class='" + self.klass.name + "_split'></SPAN></LI>").appendTo(grp.mover);
				} else {
					self.parseItem(itm);
					self.addItem(grp, itm);
				}
			});
			grp.removeClass("hide");
			this.evalScroll(grp, cb);
		},
		removeBtnClass : function(btn, cls) {
			var n = btn.pGroup.menu.klass.item;
			$("table." + n + "_" + cls, btn).removeClass(n + "_" + cls);
			$("." + n + "L_" + cls, btn).removeClass(n + "L_" + cls);
			$("." + n + "C_" + cls, btn).removeClass(n + "C_" + cls);
			$("." + n + "R_" + cls, btn).removeClass(n + "R_" + cls);
		},
		addBtnClass : function(btn, cls) {
			var n = btn.pGroup.menu.klass.item;
			$("table." + n, btn).addClass(n + "_" + cls);
			$("." + n + "L", btn).addClass(n + "L_" + cls);
			$("." + n + "C", btn).addClass(n + "C_" + cls);
			$("." + n + "R", btn).addClass(n + "R_" + cls);
		},
		addItem : function(grp, itm) {
			var self = this, klass = this.klass.item;
			var btn = this.buildItem(itm).appendTo(grp.mover);
			btn.config = itm;
			btn.pGroup = grp;
			btn.hover(function() {
				if (self.config.eventType == "mouseover") {
					self.hideGroup(btn.pGroup.activeItem);
				}
				if (!itm.disable) {
					self.addBtnClass(btn, "over");
					if (itm.items && self.config.eventType == "mouseover") {
						self.switchGroup(btn);
					}
				}
				return true;
			}, function() {
				self.removeBtnClass(btn, "over");
			});
			btn.click(function() {
				if (!itm.disable) {
					if (itm.items) {
						if (self.config.eventType == "click")
							self.switchGroup(btn);
					} else {
						self.execAction(btn);
					}
				}
				return true;
			});
			return btn;
		},
		execAction : function(btn) {
			(this.options.execActionCallback || _rf)(this.options, btn.config);
		},
		evalScroll : function(grp, callback) {
			$.Scrollable.update(grp);
			$.Scrollable.showScroll(grp);
			$.Scrollable.update(grp);
			$.Scrollable.useableScroll(grp);
			if (grp.vertical) {
				if (grp.maxHeight && grp.mover.maxHeight < grp.maxHeight) {
					grp.height(grp.mover.maxHeight);
				}
			} else {
				if (grp.maxWidth && grp.mover.maxWidth < grp.maxWidth) {
					// grp.css({
					// left : grp.maxWidth - grp.mover.maxWidth
					// });
					grp.width(grp.mover.maxWidth);
				}
			}
			(callback || _rf)();
			grp.loading = false;
			grp.loaded = true;
		},
		adjustPos : function(btn) {
			btn.group.maxHeight = document.documentElement.clientHeight / 2 + 5;
			this.evalScroll(btn.group);
			return this.evalPos(btn);
		},
		oneMouseDown : function(event) {
			var self = this;
			var target = event.target;
			while (target) {
				if (target == self.get(0)) {
					$(document).one("mousedown", function(event) {
						self.oneMouseDown(event);
					});
					return true;
				}
				target = $(target).parent().get(0);
			}
			self.hide();
		},
		switchGroup : function(btn) {
			if (btn != btn.pGroup.activeItem) {
				this.hideGroup(btn.pGroup.activeItem);
				(btn.group && btn.group.loaded) ? this.showGroup(btn.group, null, btn) : this.parseGroup(btn);
			} else {
				if (this.config.eventType == "click") {
					this.hideGroup(btn.pGroup.activeItem);
				}
			}
		},
		parseGroup : function(btn) {
			var self = this, grp = btn.group;
			if (!grp) {
				btn.group = grp = self.buildGroup().appendTo(self);
				grp.root = self.root;
				grp.pItem = btn;
				grp.pGroup = btn.pGroup;
			}
			if (!grp.loaded) {
				self.parseItems(grp, btn, function() {
					btn.fix = self.buildFix(btn);
					self.showGroup(grp, null, btn);
				});
			}
		},
		showGroup : function(grp, pos, btn) {
			if (!grp.shown) {
				var self = this, klass = this.klass.item;
				btn.pGroup.activeItem = btn;
				self.removeBtnClass(btn, "over");
				self.addBtnClass(btn, "active");
				if (btn.fix) {
					btn.fix.show();
				}
				grp.show().css(pos || self.evalPos(btn));
				grp.shown = true;
				if (!this.config.toggle) {
					$(document).one("mousedown", function(event) {
						self.oneMouseDown(event);
					});
				}
			}
		},
		hideGroup : function(g) {
			if (g) {
				if (g.fix) {
					g.fix.hide();
				}
				if (g.button) {
					var klass = this.klass.item;
					this.removeBtnClass(g, "over");
					this.removeBtnClass(g, "active");
				}
				if (g.group) {// g is a button
					this.hideGroup(g.group);
				} else {
					if (g.activeItem) {// g is a group
						this.hideGroup(g.activeItem);
					}
					g.hide();
					g.shown = false;
					if (g.pGroup) {
						g.pGroup.activeItem = null;
					}
				}
			}
		},
		show : function(pos) {
			this.parseGroup(this.group, pos || {
				left : 0,
				top : 0
			}, 0);
		},
		hide : function() {
			this.hideGroup(this.group.activeItem);
		},
		init : function(parse) {
			var cfg = this.config, kn;
			if (cfg.vertical) {
				kn = cfg.vclassName;
				cfg.maxWidth = cfg.vWidth || this.width() - 2;
				cfg.maxHeight = cfg.vHeight || this.height();
			} else {
				kn = cfg.hclassName;
				cfg.maxWidth = cfg.hWidth || this.width() - 2;
				cfg.maxHeight = cfg.hHeight || this.height();
			}
			this.klass = {
				"name" : kn,
				"item" : kn + "_item",
				"scroll" : kn + "_scroll"
			};
			cfg.textCss = {};
			if (jQuery.browser.msie) {
				cfg.textCss = {
					"padding-top" : "2px"
				};
			}
			var pos = this.css("position");
			if (pos != "absolute" && pos != "relative") {
				this.css("position", "relative");
			}
			this.addClass(kn);
			this.zIndex = $.demsy.zIndex(this.cssVal("z-index"));
			this.group = this.buildGroup().appendTo(this).css("z-index", this.zIndex);
			this.group.root = this;
			if (parse) {
				this.parseItems(this.group, cfg.trigger || this, cfg.success);
			}
			return this;
		}
	};
	var hmenu = function(opts) {
		var self = this, config = $.extend($.extend(true, {}, hmenu.options), opts || {});
		self.config = config;
		self.options = opts;
		$.extend(this, menu);
		for ( var i = 1; i < arguments.length; i++) {
			$.demsy.inject(this, arguments[i]);
		}
	};
	var vmenu = function(opts) {
		var self = this, config = $.extend($.extend(true, {}, vmenu.options), opts || {});
		this.config = config;
		self.options = opts;
		this.evalPos = function(btn) {
			btn.fix.css("z-index", $.demsy.zIndex()).height(btn.height());
			var pos = {
				"z-index" : $.demsy.zIndex()
			};
			//
			var gW = btn.group.outerWidth();
			var bW = btn.outerWidth();
			var bL = btn.offset().left;
			var sL = $(document).scrollLeft();
			if (bL + bW + gW - sL > $("body").width()) {
				pos.left = bL - self.offset().left - gW + btn.cssVal("paddingLeft") - 1;
				btn.fix.css("left", pos.left + gW);
			} else {
				pos.left = bL - self.offset().left + bW - btn.cssVal("paddingRight") + 1;
				btn.fix.css("left", pos.left - 3);
			}
			//
			var mT = self.offset().top;
			var gH = btn.group.outerHeight();
			var bT = btn.offset().top;
			var bH = btn.outerHeight();
			var pT = btn.cssVal("paddingTop");
			var pB = btn.cssVal("paddingBottom");
			var sT = $(document).scrollTop();
			if (bT - sT + gH + pT > document.documentElement.clientHeight) {// 超出浏览器底部
				var _top = bH - pB - gH + bT - mT;
				if (_top + mT - sT > 0) {// 超出浏览器顶部
					pos.top = _top;
					btn.fix.css("top", _top + gH - bH + pT + pB);
				} else {
					return this.adjustPos(btn);
				}
			} else {
				pos.top = bT - mT + pT;
				btn.fix.css("top", pos.top);
			}
			return pos;
		};
		$.extend(this, menu);
		for ( var i = 1; i < arguments.length; i++) {
			$.demsy.inject(this, arguments[i]);
		}
	};
	hmenu.options = {
		eventType : "click",
		hclassName : "hmenu",
		subDirection : "auto",
		vclassName : "vmenu",
		fade : 0,
		titleSize : 20,
		scrolled : false,
		scrollFinish : true,
		fixNum : 2
	};
	vmenu.options = {
		eventType : "click",
		vclassName : "vmenu",
		subDirection : "auto",
		fade : 0,
		titleSize : 20,
		icon : true,
		arrow : true,
		scrolled : false,
		scrollFinish : true,
		fixNum : 2,
		vertical : true
	};
	var ddmenu = {
		parseGroup : function(btn) {
			var self = this, grp = btn.group;
			if (!grp) {
				btn.group = grp = $("<DIV></DIV>").appendTo(self)._demsyVMenu(self.options).css("position", "absolute").css("z-index", $.demsy.zIndex()).group;
				grp.pItem = btn;
				grp.pGroup = btn.pGroup;
			}
			if (!grp.loaded) {
				grp.menu.parseItems(grp, btn, function() {
					btn.fix = self.buildFix(btn);
					self.showGroup(grp, null, btn);
				});
			}
		},
		evalPos : function(btn) {
			var pL = btn.cssVal("paddingLeft");
			var pR = btn.cssVal("paddingRight");
			btn.fix.css("z-index", $.demsy.zIndex()).width(btn.outerWidth() - pL - pR - 2);
			btn.group.parent().css("z-index", $.demsy.zIndex());
			var pos = {};
			var bL = btn.offset().left;
			var gW = btn.group.outerWidth();
			var sL = $(document).scrollLeft();
			if (bL + gW - sL > btn.pGroup.offset().left - btn.pGroup.position().left + (btn.pGroup.menu.config.maxWidth || btn.pGroup.outerWidth())) {
				pos.left = bL - btn.pGroup.offset().left + btn.pGroup.position().left + btn.outerWidth() - gW - pR;
				btn.fix.css("left", pos.left + gW - btn.width());
			} else {
				pos.left = bL - btn.pGroup.offset().left + btn.pGroup.position().left + pL;
				btn.fix.css("left", pos.left);
			}
			var bH = btn.outerHeight();
			var gH = btn.group.outerHeight();
			var sT = $(document).scrollTop();
			if ((btn.offset().top - sT + bH + gH > document.documentElement.clientHeight && this.config.subDirection == "auto") || this.config.subDirection == "up") {
				pos.top = -1 * gH + btn.cssVal("paddingTop");
				if (pos.top + btn.pGroup.offset().top - sT < 0) {
					return this.adjustPos(btn);
				}
				btn.fix.css("top", 0);
			} else {
				pos.top = bH - btn.cssVal("paddingBottom");
				btn.fix.css("top", bH - btn.cssVal("paddingBottom") - 2);
			}
			return pos;
		}
	};
	var tabs = {
		init : function(flag) {
			var self = this;
			var cfg = self.config;
			var cb = cfg.success;
			cfg.success = function() {
				self.buildContentPanel();
				if (cb) {
					cb();
				}
			};
			self.super_init(flag);
		},
		buildContentPanel : function() {
			var gH = this.group.height();
			this.contentPanel = $("<DIV class='" + this.klass.name + "_content_panel'></DIV>").appendTo(this).height(this.height() - gH - 2).width(this.group.maxWidth).css("marginTop", gH);
			var items = this.config.items;
			for (i = items.length - 1; i >= 0; i--) {
				if (items[i].tab)
					this.loadContent(items[i].tab);
			}
			this.execAction(items[0].tab);
		},
		addItem : function(grp, itm) {
			itm.tab = this.super_addItem(grp, itm);
		},
		loadContent : function(btn) {
			if (btn.content) {
				return;
			}
			var itm = btn.config;
			btn.content = $("<DIV></DIV>").addClass(this.klass.name + "_content").appendTo(this.contentPanel);
			if (itm.html) {
				btn.content.append($("#" + itm.html));
			} else {
				if (itm.href) {
					var content = btn.content;
					content.text("Loading...");
					content.load(itm.href, {}, function() {
					});
				}
			}
			btn.content.hide();
		},
		execAction : function(btn) {
			btn.pGroup.activeItem = btn;
			this.removeBtnClass(btn, "active");
			this.addBtnClass(btn, "over");
			if (!btn.fix) {
				btn.fix = this.buildFix(btn);
			}
			btn.fix.show().css(this.evalFixPos(btn));
			//
			var itm = btn.config;
			if (!btn.content) {
				this.loadContent();
			} else {
				this.showGroup(btn.group, evalFixPos(btn), btn);
			}
		},
		showGroup : function(grp, pos, btn) {
			if (!grp.shown) {
				this.super_showGroup(grp, pos, btn);
				if (btn.content) {
					btn.content.show();
				}
			}
		},
		hideGroup : function(btn) {
			this.super_hideGroup(btn);
			if (btn && btn.content) {
				btn.content.hide();
			}
		},
		evalFixPos : function(btn) {
			var pL = btn.cssVal("paddingLeft");
			var pos = {
				"z-index" : $.demsy.zIndex(),
				"width" : (btn.outerWidth() - pL - btn.cssVal("paddingRight") - 2)
			};
			pos.left = btn.offset().left - btn.pGroup.offset().left + pL;
			pos.top = btn.outerHeight() - btn.cssVal("paddingBottom") - 1;
			return pos;
		}
	};
	$.fn._demsyVMenu = function(opts) {
		vmenu.apply(this, [ $.extend({}, opts || {}) ]);
		this.init();
		return this;
	};
	$.fn.demsyVMenu = function(opts) {
		vmenu.apply(this, [ $.extend({}, opts || {}) ]);
		this.init(true);
		return this;
	};
	$.fn.demsyDDToolbar = function(opts) {
		opts = $.extend(true, {
			eventType : "mouseover",
			icon : true,
			split : true,
			arrow : true
		}, opts || {});
		hmenu.apply(this, [ opts, ddmenu ]);
		this.init(true);
		return this;
	};
	$.fn.demsyTabbar = function(opts) {
		opts = $.extend(true, {
			eventType : "click",
			hclassName : "tabs",
			subDirection : "down",
			hHeight : 25,
			icon : true,
			split : true,
			arrow : true
		}, opts || {});
		hmenu.apply(this, [ opts, ddmenu, tabs ]);
		this.init(true);
		return this;
	};
})(jQuery);

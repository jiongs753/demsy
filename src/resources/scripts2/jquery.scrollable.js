
(function ($) {
	$.Scrollable = {moveLeft:function (bar) {
		if (bar.scrollFinish) {
			bar.scrollFinish = false;
			$.Fader.apply(bar, new Array({element:bar.mover, style:(bar.vertical ? "top" : "left"), num:(bar.vertical ? (bar.maxHeight * 2 / 3) : (bar.maxWidth * 2 / 3)), maxMove:bar.maxMove, onFinish:function () {
				$.Scrollable.useableScroll(bar);
			}}));
			bar.run();
		}
	}, moveRight:function (bar) {
		if (bar.scrollFinish) {
			bar.scrollFinish = false;
			$.Fader.apply(bar, new Array({element:bar.mover, style:(bar.vertical ? "top" : "left"), num:(bar.vertical ? (bar.maxHeight * 2 / 3) : (bar.maxWidth * 2 / 3)) * -1, maxMove:bar.maxMove, onFinish:function () {
				$.Scrollable.useableScroll(bar);
			}}));
			bar.run();
		}
	}, scroll:function (bar, e) {
		if (bar.scrolled) {
			var e = event || window.event;
			if (bar.scrollFinish) {
				var delta = 0;
				if (e.wheelDelta) {
					delta = event.wheelDelta / 2;
				} else {
					if (e.detail) {
						delta = -event.detail / 3;
					}
				}
				bar.scrollFinish = false;
				$.Fader.apply(bar, new Array({element:bar.mover, style:(bar.vertical ? "top" : "left"), num:delta, maxMove:bar.maxMove, onFinish:function () {
					$.Scrollable.useableScroll(bar);
				}}));
				bar.run();
			}
			e.returnValue = false;
		}
	}, useableScroll:function (bar) {
		var style = bar.vertical ? "top" : "left";
		if (bar.scrolled) {
			var klass = bar.menu.klass;
			if (bar.mover.cssVal(style) == 0) {
				bar.leftScroll.addClass(klass.scroll + "L_dis");
				bar.leftScroll.attr("disabled", true);
				bar.rightScroll.removeClass(klass.scroll + "R_dis");
				bar.rightScroll.removeAttr("disabled");
			} else {
				if (bar.mover.cssVal(style) * -1 == bar.maxMove) {
					bar.leftScroll.removeClass(klass.scroll + "L_dis");
					bar.leftScroll.removeAttr("disabled", true);
					bar.rightScroll.addClass(klass.scroll + "R_dis");
					bar.rightScroll.attr("disabled");
				} else {
					bar.leftScroll.removeClass(klass.scroll + "L_dis");
					bar.leftScroll.removeAttr("disabled", true);
					bar.rightScroll.removeClass(klass.scroll + "R_dis");
					bar.rightScroll.removeAttr("disabled");
				}
			}
		}
		bar.scrollFinish = true;
	}, update:function (bar) {
		if (bar.vertical) {
			var cHeight = bar.maxHeight || bar.height();
			bar.moverPanel.height(cHeight);
			bar.mover.maxHeight = 0;
			$.each(bar.mover.children(), function () {
				bar.mover.maxHeight += $(this).outerHeight();
			});
			bar.maxMove = bar.mover.maxHeight - cHeight;
		} else {
			var cWidth = bar.maxWidth;
			bar.moverPanel.width(cWidth);
			bar.mover.maxWidth = 0;
			window.status = "";
			$.each(bar.mover.children(), function () {
				bar.mover.maxWidth += ($(this).width() == 0 ? 0 : $(this).outerWidth());
			});
			bar.maxMove = bar.mover.maxWidth - cWidth;
		}
	}, showScroll:function (bar) {
		if (bar.vertical) {
			if (bar.mover.maxHeight > (bar.maxHeight || bar.height()) && !bar.scrolled) {
				bar.moverPanel.addClass(bar.menu.klass.name + "_mover_panel_scroll");
				bar.leftScroll.removeClass("hide");
				bar.rightScroll.removeClass("hide");
				bar.scrolled = true;
			}
		} else {
			if (bar.mover.maxWidth > bar.maxWidth && !bar.scrolled) {
				bar.moverPanel.addClass(bar.menu.klass.name + "_mover_panel_scroll");
				bar.leftScroll.removeClass("hide");
				bar.rightScroll.removeClass("hide");
				bar.scrolled = true;
			}
		}
	}};
	$.Fader = function (config) {
		this.element = config.element;
		this.elementID = config.elementID;
		this.style = config.style;
		this.num = config.num;
		this.maxMove = config.maxMove;
		this.finishNum = "string";
		this.interval = config.interval || 10;
		this.step = config.step || 1000;
		this.onFinish = config.onFinish;
		this.isFinish = false;
		this.timer = null;
		this.method = this.num >= 0;
		this.c = this.elementID ? $("#" + this.elementID) : this.element;
		this.run = function () {
			clearInterval(this.timer);
			this.fade();
			if (this.isFinish) {
				this.onFinish && this.onFinish();
			} else {
				var f = this;
				this.timer = setInterval(function () {
					f.run();
				}, this.interval);
			}
		};
		this.fade = function () {
			if (this.finishNum == "string") {
				this.finishNum = this.c.cssVal(this.style) + this.num;
			}
			var a = this.c.cssVal(this.style);
			if (this.finishNum > a && this.method) {
				a += this.step;
				if (a >= 0) {
					this.finishNum = a = 0;
				}
			} else {
				if (this.finishNum < a && !this.method) {
					a -= this.step;
					if (a * -1 >= this.maxMove) {
						this.finishNum = a = this.maxMove * -1;
					}
				}
			}
			if (this.finishNum <= a && this.method || this.finishNum >= a && !this.method) {
				this.c.css(this.style, this.finishNum + "px");
				this.isFinish = true;
				this.finishNum = "string";
			} else {
				this.c.css(this.style, a + "px");
			}
		};
	};
})(jQuery);


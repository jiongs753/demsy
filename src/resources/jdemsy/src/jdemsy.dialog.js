(function($, jDemsy) {
	jDemsy.dialog = {
		defaults : {
			height : 300,
			width : 580,
			minH : 40,
			minW : 50,
			total : 20,
			max : false,
			mask : false,
			resizable : true,
			draggable : true,
			maxable : true,
			// minable : true,
			fresh : true,
			template : '\
			<div class="dialog" style="top:150px; left:300px;">\
				<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">\
				<div class="dialogHeader_r">\
					<div class="dialogHeader_c">\
						<a class="close" href="#close">close</a>\
						<a class="maximize" href="#maximize">maximize</a>\
						<a class="restore" href="#restore">restore</a>\
						<a class="minimize" style="display:none;" href="#minimize">minimize</a>\
						<h1></h1>\
					</div>\
				</div>\
			</div>\
			<div class="dialogContent layoutBox unitBox">\
			</div>\
			<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>\
			</div>'
		},
		currentDialog : null,
		zIndex : 42,
		/*
		 * 获取当前窗口
		 */
		getCurrent : function() {
			return this.currentDialog;
		},
		/*
		 * 重新加载窗口内容
		 */
		reload : function(url, opts) {
			var self = this;
			var options = $.extend({
				data : {},
				dialogId : "",
				callback : null
			}, opts);
			var $dialog = (options.dialogId && $("body").data(options.dialogId)) || self.currentDialog;
			if ($dialog) {
				var $content = $dialog.find(".dialogContent");
				$content.jdLoad({
					type : "POST",
					url : url,
					data : options.data,
					callback : function(response) {
						$content.find("[jdHeight]").jdHeight($content);
						$(".pageContent", $dialog).width($dialog.width() - 14);
						$(":button.close", $dialog).click(function() {
							self.close($dialog);
							return false;
						});
						if ($.isFunction(options.callback))
							options.callback(response);
					}
				});
			}
		},
		/*
		 * 打开一个窗口
		 */
		open : function(url, dialogId, title, opts) {
			var self = this;
			var options = $.extend({}, self.defaults, opts);
			var $dialog = $("body").data(dialogId);

			if ($dialog) {// 重复打开一个窗口
				if ($dialog.is(":hidden")) {
					$dialog.show();
				}
				// 需要刷新或url地址已经改变，则重新加载窗口内容
				if (options.fresh || url != $dialog.data("url")) {
					$dialog.data("url", url);
					$dialog.find(".dialogHeader").find("h1").html(title);
					self.switchDialog($dialog);
					var $content = $dialog.find(".dialogContent");
					$content.loadUrl(url, {}, function() {
						$content.find("[jdHeight]").jdHeight($content);
						$(".pageContent", $dialog).width($dialog.width() - 14);
						$("button.close").click(function() {
							self.close($dialog);
							return false;
						});
					});
				}

			} else { // 打开一个全新的窗口

				$("body").append(options.template);
				$dialog = $(">.dialog:last-child", "body");
				$dialog.data("id", dialogId);
				$dialog.data("url", url);
				if (opts.close)
					$dialog.data("close", opts.close);
				if (opts.param)
					$dialog.data("param", opts.param);
				($.fn.bgiframe && $dialog.bgiframe());

				var dialogHeader = $dialog.find(".dialogHeader").find("h1").html(title);
				$dialog.css("zIndex", (self.zIndex += 2));
				$("div.shadow").css("zIndex", self.zIndex - 3).show();
				self._init($dialog, opts);
				$dialog.click(function() {
					self.switchDialog($dialog);
				});

				if (options.resizable)
					$dialog.resizable();
				if (options.draggable)
					$dialog.draggable({
						handle : dialogHeader
					});
				$("a.close", $dialog).click(function(event) {
					self.close($dialog);
					return false;
				});
				if (options.maxable) {
					$("a.maximize", $dialog).show().click(function(event) {
						self.switchDialog($dialog);
						self.maxsize($dialog);
						$dialog.resizable("destroy").draggable("destroy");
						return false;
					});
				} else {
					$("a.maximize", $dialog).hide();
				}
				$("a.restore", $dialog).click(function(event) {
					self.restore($dialog);
					$dialog.resizable().draggable();
					return false;
				});
				// 取消最小化窗口功能
				// if (options.minable) {
				// $("a.minimize", $dialog).show().click(function(event) {
				// self.minimize($dialog);
				// return false;
				// });
				// } else {
				// $("a.minimize", $dialog).hide();
				// }
				$("a", dialogHeader).mousedown(function() {
					return false;
				}).dblclick(function() {
					if ($("a.restore", $dialog).is(":hidden"))
						$("a.maximize", $dialog).trigger("click");
					else
						$("a.restore", $dialog).trigger("click");
				});
				if (options.max) {
					// self.switchDialog($dialog);
					self.maxsize($dialog);
					$dialog.resizable("destroy").draggable("destroy");
				}
				$("body").data(dialogId, $dialog);
				self.currentDialog = $dialog;
				self.attachShadow($dialog);
				// load data
				var $content = $(".dialogContent", $dialog);
				$content.loadUrl(url, {}, function() {
					$content.find("[jdHeight]").jdHeight($content);
					$(".pageContent", $dialog).width($dialog.width() - 14);
					$("button.close").click(function() {
						self.close($dialog);
						return false;
					});
				});
			}
			// if (options.mask) {
			$dialog.css("zIndex", 1000);
			//$("a.minimize", $dialog).hide();
			$dialog.data("mask", true);
			$("#dialogBackground").show();
			// } else {
			// add a task to task bar
			// if (options.minable)
			// $.taskBar.addDialog(dialogId, title);
			// }
		},
		/**
		 * 切换当前窗口
		 */
		switchDialog : function($dialog) {
			var self = this;
			var index = $dialog.css("zIndex");
			self.attachShadow($dialog);
			if (self.currentDialog) {
				var cindex = $(self.currentDialog).css("zIndex");
				self.currentDialog.css("zIndex", index);
				$dialog.css("zIndex", cindex);
				$("div.shadow").css("zIndex", cindex - 1);
				self.currentDialog = $dialog;
			}
			// $.taskBar.switchTask($dialog.data("id"));
		},
		/**
		 * 给当前窗口附上阴隐层
		 */
		attachShadow : function($dialog) {
			var shadow = $("div.shadow");
			if (shadow.is(":hidden"))
				shadow.show();
			shadow.css({
				top : parseInt($dialog[0].style.top) - 2,
				left : parseInt($dialog[0].style.left) - 4,
				height : parseInt($dialog.height()) + 8,
				width : parseInt($dialog.width()) + 8,
				zIndex : parseInt($dialog.css("zIndex")) - 1
			});
			$(".shadow_c", shadow).children().andSelf().each(function() {
				$(this).css("height", $dialog.outerHeight() - 4);
			});
		},
		_init : function($dialog, options) {
			var op = $.extend({}, this.defaults, options);
			var height = op.height > op.minH ? op.height : op.minH;
			var width = op.width > op.minW ? op.width : op.minW;
			if (isNaN($dialog.height()) || $dialog.height() < height) {
				$dialog.height(height + "px");
				$(".dialogContent", $dialog).height(height - $(".dialogHeader", $dialog).outerHeight() - $(".dialogFooter", $dialog).outerHeight() - 6);
			}
			if (isNaN($dialog.css("width")) || $dialog.width() < width) {
				$dialog.width(width + "px");
			}

			var iTop = ($(window).height() - $dialog.height()) / 2;
			$dialog.css({
				left : ($(window).width() - $dialog.width()) / 2,
				top : iTop > 0 ? iTop : 0
			});
		},
		/**
		 * 初始化半透明层
		 */
		initResize : function(resizable, $dialog, target) {
			$("body").css("cursor", target + "-resize");
			resizable.css({
				top : $dialog.css("top"),
				left : $dialog.css("left"),
				height : $dialog.css("height"),
				width : $dialog.css("width")
			});
			resizable.show();
		},
		/**
		 * 改变阴隐层
		 */
		repaint : function(target, options) {
			var shadow = $("div.shadow");
			if (target != "w" && target != "e") {
				shadow.css("height", shadow.outerHeight() + options.tmove);
				$(".shadow_c", shadow).children().andSelf().each(function() {
					$(this).css("height", $(this).outerHeight() + options.tmove);
				});
			}
			if (target == "n" || target == "nw" || target == "ne") {
				shadow.css("top", options.otop - 2);
			}
			if (options.owidth && (target != "n" || target != "s")) {
				shadow.css("width", options.owidth + 8);
			}
			if (target.indexOf("w") >= 0) {
				shadow.css("left", options.oleft - 4);
			}
		},
		/**
		 * 改变左右拖动窗口的高度
		 */
		resizeTool : function(target, tmove, $dialog) {
			$("div[class^='resizable']", $dialog).filter(function() {
				return $(this).attr("tar") == 'w' || $(this).attr("tar") == 'e';
			}).each(function() {
				$(this).css("height", $(this).outerHeight() + tmove);
			});
		},
		/**
		 * 改变原始窗口的大小
		 */
		resizeDialog : function(obj, $dialog, target) {
			var self = this;
			var oleft = parseInt(obj.style.left);
			var otop = parseInt(obj.style.top);
			var height = parseInt(obj.style.height);
			var width = parseInt(obj.style.width);
			if (target == "n" || target == "nw") {
				tmove = parseInt($dialog.css("top")) - otop;
			} else {
				tmove = height - parseInt($dialog.css("height"));
			}
			$dialog.css({
				left : oleft,
				width : width,
				top : otop,
				height : height
			});
			$(".dialogContent", $dialog).css("width", (width - 12) + "px");
			$(".pageContent", $dialog).css("width", (width - 14) + "px");
			if (target != "w" && target != "e") {
				var content = $(".dialogContent", $dialog);
				content.css({
					height : height - $(".dialogHeader", $dialog).outerHeight() - $(".dialogFooter", $dialog).outerHeight() - 6
				});
				content.find("[jdHeight]").jdHeight(content);
				self.resizeTool(target, tmove, $dialog);
			}
			self.repaint(target, {
				oleft : oleft,
				otop : otop,
				tmove : tmove,
				owidth : width
			});

			// $(window).trigger("resizeGrid");
		},
		close : function($dialog) {
			if (typeof $dialog == 'string')
				$dialog = $("body").data($dialog);
			var close = $dialog.data("close");
			var go = true;
			if (close && $.isFunction(close)) {
				var param = $dialog.data("param");
				if (param && param != "") {
					param = this.toJson(param);
					go = close(param);
				} else {
					go = close();
				}
				if (!go)
					return;
			}
			if ($.fn.xheditor) {
				$("textarea.editor", $dialog).xheditor(false);
			}
			$dialog.unbind("click").hide();
			$("div.dialogContent", $dialog).html("");
			$("div.shadow").hide();
			// if ($dialog.data("mask")) {
			// $("#dialogBackground").hide();
			// } else {
			// if ($dialog.data("id"))
			// $.taskBar.closeDialog($dialog.data("id"));
			// }
			$("body").removeData($dialog.data("id"));
			$dialog.remove();
		},
		closeCurrent : function() {
			this.close(this.currentDialog);
		},
		checkTimeout : function() {
			var $conetnt = $(".dialogContent", this.currentDialog);
			var json = this.toJson($conetnt.html());
			if (json && json.statusCode == this.status.timeout)
				this.closeCurrent();
		},
		maxsize : function($dialog) {
			$dialog.data("original", {
				top : $dialog.css("top"),
				left : $dialog.css("left"),
				width : $dialog.css("width"),
				height : $dialog.css("height")
			});
			$("a.maximize", $dialog).hide();
			$("a.restore", $dialog).show();
			var iContentW = $(window).width();
			var iContentH = $(window).height() - 34;
			$dialog.css({
				top : "0px",
				left : "0px",
				width : iContentW + "px",
				height : iContentH + "px"
			});
			this._resizeContent($dialog, iContentW, iContentH);
		},
		restore : function($dialog) {
			var self = this;
			var original = $dialog.data("original");
			var dwidth = parseInt(original.width);
			var dheight = parseInt(original.height);
			$dialog.css({
				top : original.top,
				left : original.left,
				width : dwidth,
				height : dheight
			});
			self._resizeContent($dialog, dwidth, dheight);
			$("a.maximize", $dialog).show();
			$("a.restore", $dialog).hide();
			self.attachShadow($dialog);
		},
		// minimize : function($dialog) {
		// $dialog.hide();
		// $("div.shadow").hide();
		// var task = $.taskBar.getTask($dialog.data("id"));
		// $(".resizable").css({
		// top : $dialog.css("top"),
		// left : $dialog.css("left"),
		// height : $dialog.css("height"),
		// width : $dialog.css("width")
		// }).show().animate({
		// top : $(window).height() - 60,
		// left : task.position().left,
		// width : task.outerWidth(),
		// height : task.outerHeight()
		// }, 250, function() {
		// $(this).hide();
		// $.taskBar.inactive($dialog.data("id"));
		// });
		// },
		_resizeContent : function($dialog, width, height) {
			var content = $(".dialogContent", $dialog);
			content.css({
				width : (width - 12) + "px",
				height : height - $(".dialogHeader", $dialog).outerHeight() - $(".dialogFooter", $dialog).outerHeight() - 6
			});
			content.find("[jdHeight]").jdHeight(content);
			$(".pageContent", $dialog).css("width", (width - 14) + "px");

			// $(window).trigger("resizeGrid");
		}
	};
})(jQuery);
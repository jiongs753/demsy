(function($) {
	$.fn.extend({
		/**
		 * 通过AJAX访问URL
		 * <p>
		 * 1. 如果返回内容是一个JSON对象：且访问的URL需要登录认证，则未登录或会话过期时将会自动弹出登录框；
		 * <p>
		 * 2. 如果返回内容是一个JSON对象：且访问URL出现错误，则将弹出错误信息提示框；
		 * <p>
		 * 3. 如果返回内容不是一个JSON对象：则将返回内容作为当前元素的HTML内容并初始化UI；
		 */
		dajax : function(options) {
			if ($.fn.xheditor) {
				$("textarea.editor", this).xheditor(false);
			}

			$.ajax({
				type : options.type || 'GET',
				url : options.url,
				data : options.data,
				cache : false,
				success : function(response) {
					// 将内容转换成JSON对象
					var json = Dutils.toJson(response);

					// 返回值是“会话过期/未登录”：弹出登录框
					if (json.statusCode == Dstatus.timeout) {
						Dutils.login(json.message || Dmsg("sessionTimout"));
					}
					// 返回值是“错误信息”：弹出错误提示框
					else if (json.statusCode == Dstatus.error) {
						Derror(json.message);
					}
					// 返回值是“HTML内容”：将其作为元素的HTML内容
					else {
						$this.html(response).demsyInit();
						if ($.isFunction(options.callback))
							options.callback(response);
					}
				},
				error : Dutils.ajaxError
			});
		},
		/**
		 * 发送GET请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		dget : function(url, data, callback) {
			this.dajax({
				url : url,
				data : data,
				callback : callback
			});
		},
		/**
		 * 发送POST请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		dpost : function(url, data, callback) {
			this.dajax({
				url : url,
				type : "POST",
				data : data,
				callback : callback
			});
		},
		/**
		 * 初始化元素内容中的UI
		 */
		dinit : function() {
			return this.each(function() {
				if ($.isFunction(initUI))
					initUI(this);
			});
		},
		/**
		 * 校正元素高度为指定对象的高度
		 */
		dlayout : function($refBox) {
			return this.each(function() {
				var $this = $(this);
				if (!$refBox)
					$refBox = $this.parents("div.layoutBox:first");
				var iRefH = $refBox.height();
				var iLayoutH = parseInt($this.attr("layoutH"));
				var iH = iRefH - iLayoutH > 50 ? iRefH - iLayoutH : 50;

				if ($this.isTag("table")) {
					$this.removeAttr("layoutH").wrap('<div layoutH="' + iLayoutH + '" style="overflow:auto;height:' + iH + 'px"></div>');
				} else {
					$this.height(iH).css("overflow", "auto");
				}
			});
		},
		/**
		 * 鼠标移动到元素上时套用指定的CSS
		 */
		hoverClass : function(className) {
			var _className = className || "hover";
			return this.each(function() {
				$(this).hover(function() {
					$(this).addClass(_className);
				}, function() {
					$(this).removeClass(_className);
				});
			});
		},
		/**
		 * 元素获得焦点时套用指定的CSS
		 */
		focusClass : function(className) {
			var _className = className || "textInputFocus";
			return this.each(function() {
				$(this).focus(function() {
					$(this).addClass(_className);
				}).blur(function() {
					$(this).removeClass(_className);
				});
			});
		},
		/**
		 * 输入框获得焦点时显示提示信息
		 */
		altBox : function() {
			return this.each(function() {

				var $this = $(this);

				function getAltBox() {
					return $this.parent().find("label.alt");
				}
				function altBoxCss(opacity) {
					var position = $this.position();
					return {
						width : $this.width(),
						top : position.top + 'px',
						left : position.left + 'px',
						opacity : opacity || 1
					}
				}
				if (getAltBox().size() < 1) {
					if (!$this.attr("id"))
						$this.attr("id", $this.attr("name") + "_" + Math.round(Math.random() * 10000));
					var $label = $('<label class="alt" for="' + $this.attr("id") + '">' + $this.attr("alt") + '</label>').appendTo($this.parent());

					$label.css(altBoxCss(1));
					if ($this.val())
						$label.hide();
				}

				$this.focus(function() {
					getAltBox().css(altBoxCss(0.3));
				}).blur(function() {
					if (!$(this).val())
						getAltBox().show().css("opacity", 1);
				}).keydown(function() {
					getAltBox().hide();
				});
			});
		},
		/**
		 * 判断元素是不是指定的HTML标签
		 */
		isTag : function(tn) {
			if (!tn)
				return false;
			return this.tagName.toLowerCase() == tn.toLowerCase() ? true : false;
		},
		/**
		 * 判断当前元素是否已经绑定某个事件
		 */
		hasEvent : function(type) {
			var _events = $(this).data("events");
			return _events && type && _events[type];
		},
		/**
		 * 输出firebug日志
		 */
		log : function(msg) {
			return this.each(function() {
				if (console)
					console.log("%s: %o", msg, this);
			});
		}
	});

})(jQuery);

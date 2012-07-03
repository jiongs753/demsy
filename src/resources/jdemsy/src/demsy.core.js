/**
 * 定义jDemsy类
 */
(function($) {
	jError = jWarn = jInfo = jSuccess = alert;
	jConfirm = confirm;
	jPrompt = prompt;
	jDemsy = {
		_debug : true,
		// 后台URL地址配置
		urls : {
			login : "/login/load"// 登录页面的urls地址
		},
		nls : {
			login : "Login",
		},
		tpl : {},
		// AJAX状态码配置
		status : {
			ok : 200,
			error : 300,
			timeout : 301
		},
		// 键盘编码
		keyCode : {
			ENTER : 13,
			ESC : 27,
			END : 35,
			HOME : 36,
			SHIFT : 16,
			TAB : 9,
			LEFT : 37,
			RIGHT : 39,
			UP : 38,
			DOWN : 40,
			DELETE : 46,
			BACKSPACE : 8
		},
		// isOverAxis : function(x, ref, size) {
		// return (x > ref) && (x < (ref + size));
		// },
		// isOver : function(y, x, top, left, height, width) {
		// return this.isOverAxis(y, top, height) && this.isOverAxis(x, left,
		// width);
		// },
		/**
		 * 调试
		 */
		log : function(msg) {
			if (this._debug) {
				var args = Array.prototype.slice.call(arguments, 1);
				if (typeof (console) != "undefined")
					console.log(msg.format(args));
			}
		},
		/**
		 * 登录：弹出登录框
		 */
		login : function(title) {
			if ($.pdialog && this.urls.login) {
				$.pdialog.open(this.urls.login, "login", title || this.nls.login, {
					mask : true,
					width : 520,
					height : 260
				});
			} else {
				window.location = this.urls.login;
			}
		},
		/**
		 * 将字符串转换成JSON对象
		 */
		toJson : function(str) {
			try {
				if ($.type(str) == 'string')
					return $.parseJSON('(' + str + ')');
				else
					return str;
			} catch (e) {
				return {};
			}
		},
		/**
		 * 通过AJAX访问URL出错后调用该函数显示错误信息。
		 */
		doAjaxError : function(xhr, ajaxOptions, thrownError) {
			jError("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" + "<div>ajaxOptions: " + ajaxOptions + "</div>" + "<div>thrownError: " + thrownError + "</div>" + "<div>" + xhr.responseText + "</div>");
		},
		/**
		 * AJAX访问URL成功后调用该函数处理返回结果
		 */
		doAjaxSuccess : function(json) {
			// 返回值不是一个JSON对象
			if (json.statusCode === undefined && json.message === undefined) {
				return jError(json);
			}
			// 返回值是错误信息
			if (json.statusCode == this.status.error && json.message) {
				jError(json.message);
			}
			// 返回值是登录超时
			else if (json.statusCode == this.status.timeout) {
				this.login();
			} else if (json.message) {
				jSuccess(json.message);
			}
		}
	};
})(jQuery);
/**
 * 扩展jQuery对象方法
 */
(function($, jDemsy) {
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
		jdLoad : function(options) {
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
					var json = jDemsy.toJson(response);

					// 返回值是“会话过期/未登录”：弹出登录框
					if (json.statusCode == jDemsy.status.timeout) {
						jDemsy.login(json.message);
					}
					// 返回值是“错误信息”：弹出错误提示框
					else if (json.statusCode == jDemsy.status.error) {
						jError(json.message);
					}
					// 返回值是“HTML内容”：将其作为元素的HTML内容
					else {
						$this.html(response).jdSetup();
						if ($.isFunction(options.callback))
							options.callback(response);
					}
				},
				error : jDemsy.doAjaxError
			});
		},
		/**
		 * 发送GET请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		jdGet : function(url, data, callback) {
			this.jdLoad({
				url : url,
				data : data,
				callback : callback
			});
		},
		/**
		 * 发送POST请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		jdPost : function(url, data, callback) {
			this.jdLoad({
				url : url,
				type : "POST",
				data : data,
				callback : callback
			});
		},
		/**
		 * 初始化元素内容中的UI
		 */
		jdSetup : function() {
			return this.each(function() {
				if ($.isFunction(jDemsy.setup))
					jDemsy.setup(this);
			});
		},
		/**
		 * 校正元素高度为指定对象的高度
		 */
		jdHeight : function($refBox) {
			return this.each(function() {
				var $this = $(this);
				if (!$refBox)
					$refBox = $this.parents("div.layoutBox:first");
				var refHeight = $refBox.height();
				var jdHeight = parseInt($this.attr("jdHeight"));
				var height = refHeight - jdHeight > 50 ? refHeight - jdHeight : 50;

				if ($this.isTag("table")) {
					$this.removeAttr("jdHeight").wrap('<div jdHeight="' + jdHeight + '" style="overflow:auto;height:' + height + 'px"></div>');
				} else {
					$this.height(height).css("overflow", "auto");
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
			return this.get(0).tagName.toUpperCase() == tn.toUpperCase() ? true : false;
		},
		tag : function() {
			return this.get(0).tagName.toUpperCase();
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

})(jQuery, jDemsy);

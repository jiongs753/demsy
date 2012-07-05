/**
 * JS Map 类
 */
var Map = function() {

	this.elements = new Array();

	this.size = function() {
		return this.elements.length;
	}

	this.isEmpty = function() {
		return (this.elements.length < 1);
	}

	this.clear = function() {
		this.elements = new Array();
	}

	this.put = function(_key, _value) {
		this.remove(_key);
		this.elements.push({
			key : _key,
			value : _value
		});
	}

	this.remove = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	}

	this.get = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return null;
		}
	}

	this.element = function(_index) {
		if (_index < 0 || _index >= this.elements.length) {
			return null;
		}
		return this.elements[_index];
	}

	this.containsKey = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	}

	this.values = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	}

	this.keys = function() {
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	}
}

/**
 * 扩展后支持参数传递的的定时调度函数
 */
var __sti = setInterval;
window.setInterval = function(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	__sti(_cb, timeout);
};
var __sto = setTimeout;
window.setTimeout = function(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	__sto(_cb, timeout);
};
/**
 * 扩展String方法
 */
$.extend(String.prototype, {
	/**
	 * 判断字符串是否是正整数
	 */
	isPositiveInteger : function() {
		return (new RegExp(/^[1-9]\d*$/).test(this));
	},
	/**
	 * 判断字符串是否是整数？
	 */
	isInteger : function() {
		return (new RegExp(/^\d+$/).test(this));
	},
	/**
	 * 判断字符串是否是数字？
	 */
	isNumber : function(value, element) {
		return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
	},
	/**
	 * 去除字符串头尾空白
	 */
	trim : function() {
		return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
	},
	/**
	 * 判断字符串是否以...开头
	 */
	startsWith : function(pattern) {
		return this.indexOf(pattern) === 0;
	},
	/**
	 * 判断字符串是否以...结尾？
	 */
	endsWith : function(pattern) {
		var d = this.length - pattern.length;
		return d >= 0 && this.lastIndexOf(pattern) === d;
	},
	replaceSuffix : function(index) {
		return this.replace(/\[[0-9]+\]/, '[' + index + ']').replace('#index#', index);
	},
	trans : function() {
		return this.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"');
	},
	replaceAll : function(os, ns) {
		return this.replace(new RegExp(os, "gm"), ns);
	},
	replaceTm : function($data) {
		if (!$data)
			return this;
		return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
			return $data[$1.replace(/[{}]+/g, "")];
		});
	},
	replaceTmById : function(_box) {
		var $parent = _box || $(document);
		return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
			var $input = $parent.find("#" + $1.replace(/[{}]+/g, ""));
			return $input.val() ? $input.val() : $1;
		});
	},
	isFinishedTm : function() {
		return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this));
	},
	/**
	 * 跳过指定字符
	 */
	skipChar : function(ch) {
		if (!this || this.length === 0) {
			return '';
		}
		if (this.charAt(0) === ch) {
			return this.substring(1).skipChar(ch);
		}
		return this;
	},
	/**
	 * 判断字符串是不是合法的密码？即只包含数字、字母、下划线且长度至少6位最大32位。
	 */
	isPassword : function() {
		return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this));
	},
	/**
	 * 判断字符串是不是Email地址？
	 */
	isEmail : function() {
		return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
	},
	/**
	 * 判断字符串是不是空白？即字符串是否只包含“空格、回车、换行、制表”等字符？
	 */
	isSpaces : function() {
		for ( var i = 0; i < this.length; i += 1) {
			var ch = this.charAt(i);
			if (ch != ' ' && ch != "\n" && ch != "\t" && ch != "\r") {
				return false;
			}
		}
		return true;
	},
	/**
	 * 判断字符串是不是电话号码？
	 */
	isPhone : function() {
		return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));
	},
	/**
	 * 判断字符串是否是一个URL地址？
	 */
	isUrl : function() {
		return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
	},
	/**
	 * 判断字符串是不是外部URL地址？
	 */
	isExternalUrl : function() {
		return this.isUrl() && this.indexOf("://" + document.domain) == -1;
	},
	/**
	 * 将字符串转换成JavaBean规范中的属性名，即将减号(-)后面的字母转换成大写，并将减号(-)去掉。
	 */
	toProp : function() {
		var idx = this.indexOf("-");
		if (idx > -1) {
			var str = this.substring(0, idx) + this.substring(idx + 1, idx + 2).toUpperCase() + this.substring(idx + 2);
			return str.toProp();
		}

		return this;
	},
	/**
	 * 格式化字符串，{0},{1},...等将被对应的参数替换
	 */
	format : function() {
		var args = [];
		if (arguments) {
			if (arguments.length == 1 && $.type(arguments[0]) == "array") {
				args = arguments[0];
			} else {
				args = arguments;
			}
		}
		var result = "" + this;
		for ( var i = 0; i < args.length; i++) {
			result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
		}
		return result;
	}
});

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
		 * 调试:
		 * 第1个参数为日志模版，如果有更多参数，则第2个参数必需是执行代码的开始时间，第3个参数开始才用于填充日志占位符(日志模版中的占位符为{number}，花括号中的数字从1开始)。
		 */
		log : function(msg) {
			if (this._debug && typeof (console) != "undefined") {
				var args = [];
				if (arguments.length > 1) {
					args = Array.prototype.slice.call(arguments, 1);
					args[0] = new Date().getTime() - args[0];
					console.log((msg + " - 耗时：{0}").format(args));
				} else {
					console.log(msg);
				}
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
		},
		_outerWidth : function(_e) {
			return this.each(function() {
				if (!$.boxModel && $.browser.msie) {
					$(this).width(_e);
				} else {
					$(this).width(_e - ($(this).outerWidth() - $(this).width()));
				}
			});
		},
		_outerHeight : function(_f) {
			return this.each(function() {
				if (!$.boxModel && $.browser.msie) {
					$(this).height(_f);
				} else {
					$(this).height(_f - ($(this).outerHeight() - $(this).height()));
				}
			});
		},
		getCssValue : function(css) {
			var val = parseInt(this.css(css));
			if (isNaN(val)) {
				return 0;
			} else {
				return val;
			}
		}
	});

})(jQuery, jDemsy);

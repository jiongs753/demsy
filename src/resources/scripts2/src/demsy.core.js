var Demsy = {
	isOverAxis : function(x, ref, size) {
		return (x > ref) && (x < (ref + size));
	},
	isOver : function(y, x, top, left, height, width) {
		return this.isOverAxis(y, top, height) && this.isOverAxis(x, left, width);
	},
	/*
	 * key对应的字符串中，{0}{1}...将被参数替换
	 */
	msg : function(key, args) {
		var _format = function(str, args) {
			args = args || [];
			var result = str
			for ( var i = 0; i < args.length; i++) {
				result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
			}
			return result;
		}
		return _format(this._msg[key], args);
	},
	debug : function(msg) {
		if (this.debug) {
			if (typeof (console) != "undefined")
				console.log(msg);
			else
				alert(msg);
		}
	},
	/**
	 * 登录
	 */
	login : function(title) {
		if ($.pdialog && Demsy.loginTitle) {
			$.pdialog.open(Demsy.loginUrl, "login", title || Demsy.loginTitle, {
				mask : true,
				width : 520,
				height : 260
			});
		} else {
			window.location = Demsy.loginUrl;
		}
	},
	// 将data转换为JSON对象
	toJson : function(data) {
		try {
			if ($.type(data) == 'string')
				return eval('(' + data + ')');
			else
				return data;
		} catch (e) {
			return {};
		}
	},
	/**
	 * 通过AJAX访问URL出错后调用该函数显示错误信息。
	 */
	ajaxError : function(xhr, ajaxOptions, thrownError) {
		if (alerts) {
			alerts.error("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" + "<div>ajaxOptions: " + ajaxOptions + "</div>" + "<div>thrownError: " + thrownError + "</div>" + "<div>" + xhr.responseText + "</div>");
		} else {
			alert("Http status: " + xhr.status + " " + xhr.statusText + "\najaxOptions: " + ajaxOptions + "\nthrownError:" + thrownError + "\n" + xhr.responseText);
		}
	},
	/**
	 * AJAX访问URL成功后调用该函数处理返回结果
	 */
	ajaxDone : function(json) {
		// 返回值不是一个JSON对象
		if (json.statusCode === undefined && json.message === undefined) {
			if (alerts)
				return alerts.error(json);
			else
				return alert(json);
		}
		// 返回值是错误信息
		if (json.statusCode == Demsy.statusCode.error) {
			if (json.message && alerts)
				alerts.error(json.message);
		}
		// 返回值是登录超时
		else if (json.statusCode == Demsy.statusCode.timeout) {
			if (alerts)
				alerts.error(json.message || Demsy.msg("sessionTimout"), {
					okCall : Demsy.login
				});
			else
				Demsy.login();
		} else {
			if (json.message && alerts)
				alerts.correct(json.message);
		}
	},

	/**
	 * 初始化 DEMSY 配置
	 */
	init : function(options) {
		var op = $.extend({
			callback : null
		}, options);
		$.extend(Demsy.pageInfo, op.pageInfo);
	}
};

/**
 * You can use this map like this: var myMap = new Map();
 * myMap.put("key","value"); var key = myMap.get("key"); myMap.remove("key");
 */
function Map() {

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

(function($) {
	/**
	 * 扩展String方法
	 */
	$.extend(String.prototype, {
		isPositiveInteger : function() {
			return (new RegExp(/^[1-9]\d*$/).test(this));
		},
		isInteger : function() {
			return (new RegExp(/^\d+$/).test(this));
		},
		isNumber : function(value, element) {
			return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
		},
		trim : function() {
			return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
		},
		startsWith : function(pattern) {
			return this.indexOf(pattern) === 0;
		},
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
		skipChar : function(ch) {
			if (!this || this.length === 0) {
				return '';
			}
			if (this.charAt(0) === ch) {
				return this.substring(1).skipChar(ch);
			}
			return this;
		},
		isValidPwd : function() {
			return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this));
		},
		isValidMail : function() {
			return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
		},
		isSpaces : function() {
			for ( var i = 0; i < this.length; i += 1) {
				var ch = this.charAt(i);
				if (ch != ' ' && ch != "\n" && ch != "\t" && ch != "\r") {
					return false;
				}
			}
			return true;
		},
		isPhone : function() {
			return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));
		},
		isUrl : function() {
			return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
		},
		isExternalUrl : function() {
			return this.isUrl() && this.indexOf("://" + document.domain) == -1;
		}
	});
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
		demsyAjax : function(options) {
			var $this = $(this);
			if ($.fn.xheditor) {
				$("textarea.editor", $this).xheditor(false);
			}

			$.ajax({
				type : options.type || 'GET',
				url : options.url,
				data : options.data,
				cache : false,
				success : function(response) {
					// 将内容转换成JSON对象
					var json = Demsy.toJson(response);

					// 返回值是“会话过期/未登录”：弹出登录框
					if (json.statusCode == Demsy.statusCode.timeout) {
						// 不再弹出错误提示框，直接弹出登录框
						// alerts.error(json.message ||
						// Demsy.msg("sessionTimout"), {
						// okCall : function() {
						// if ($.pdialog)
						// $.pdialog.checkTimeout();
						// if (navTab)
						// navTab.checkTimeout();
						//
						// Demsy.login();
						// }
						// });
						Demsy.login(json.message || Demsy.msg("sessionTimout"));
					}
					// 返回值是“错误信息”：弹出错误提示框
					else if (json.statusCode == Demsy.statusCode.error) {
						if (json.message)
							alerts.error(json.message);
					}
					// 返回值是“HTML内容”：将其作为元素的HTML内容
					else {
						$this.html(response).demsyInit();
						if ($.isFunction(options.callback))
							options.callback(response);
					}
				},
				error : Demsy.ajaxError
			});
		},
		/**
		 * 发送GET请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		demsyGet : function(url, data, callback) {
			$(this).demsyAjax({
				url : url,
				data : data,
				callback : callback
			});
		},
		/**
		 * 发送POST请求到指定的URL地址加载内容，该方法是demsyAjax的简化用法
		 */
		demsyPost : function(url, data, callback) {
			$(this).demsyAjax({
				url : url,
				type : "POST",
				data : data,
				callback : callback
			});
		},
		/**
		 * 初始化元素内容中的UI
		 */
		demsyInit : function() {
			return this.each(function() {
				if ($.isFunction(initUI))
					initUI(this);
			});
		},
		/**
		 * 校正元素高度为指定对象的高度
		 */
		layoutH : function($refBox) {
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
			return $(this)[0].tagName.toLowerCase() == tn.toLowerCase() ? true : false;
		},
		/**
		 * 判断当前元素是否已经绑定某个事件
		 */
		isBind : function(type) {
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

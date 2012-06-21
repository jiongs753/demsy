var Demsy = {
	// 键盘代码
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
	isOverAxis : function(x, ref, size) {
		return (x > ref) && (x < (ref + size));
	},
	isOver : function(y, x, top, left, height, width) {
		return this.isOverAxis(y, top, height) && this.isOverAxis(x, left, width);
	},

	pageInfo : {
		pageIndex : "pageIndex",
		pageSize : "pageSize",
		orderBy : "orderBy"
	},
	// 后台的JSON对象对应的状态码
	statusCode : {
		ok : 200,
		error : 300,
		timeout : 301
	},
	ui : {
		sbar : true
	},
	template : {
		dialog : '\
			<div class="dialog" style="top:150px; left:300px;">\
				<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">\
					<div class="dialogHeader_r">\
						<div class="dialogHeader_c">\
							<a class="close" href="#close">close</a>\
							<a class="maximize" href="#maximize">maximize</a>\
							<a class="restore" href="#restore">restore</a>\
							<a class="minimize" href="#minimize">minimize</a>\
							<h1>弹出窗口</h1>\
						</div>\
					</div>\
				</div>\
				<div class="dialogContent layoutBox unitBox">\
				</div>\
				<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>\
				<div class="resizable_h_l" tar="nw"></div>\
				<div class="resizable_h_r" tar="ne"></div>\
				<div class="resizable_h_c" tar="n"></div>\
				<div class="resizable_c_l" tar="w" style="height:300px;"></div>\
				<div class="resizable_c_r" tar="e" style="height:300px;"></div>\
				<div class="resizable_f_l" tar="sw"></div>\
				<div class="resizable_f_r" tar="se"></div>\
				<div class="resizable_f_c" tar="s"></div>\
			</div>\
			',
		dialogProxy : '\
			<div id="dialogProxy" class="dialog dialogProxy">\
				<div class="dialogHeader">\
					<div class="dialogHeader_r">\
						<div class="dialogHeader_c">\
							<a class="close" href="#close">close</a>\
							<a class="maximize" href="#maximize">maximize</a>\
							<a class="minimize" href="#minimize">minimize</a>\
							<h1></h1>\
						</div>\
					</div>\
				</div>\
				<div class="dialogContent"></div>\
				<div class="dialogFooter">\
					<div class="dialogFooter_r">\
						<div class="dialogFooter_c">\
						</div>\
					</div>\
				</div>\
			</div>\
			'
	},
	_msg : {},
	_cfg : {
		loginUrl : "/login/load",
		loginTitle : "登录",
		debug : false
	},
	// key对应的字符串中，{0}{1}...将被参数替换
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
		if (this._cfg.debug) {
			if (typeof (console) != "undefined")
				console.log(msg);
			else
				alert(msg);
		}
	},
	login : function() {
		if ($.pdialog && Demsy._cfg.loginTitle) {
			$.pdialog.open(Demsy._cfg.loginUrl, "login", Demsy._cfg.loginTitle, {
				mask : true,
				width : 520,
				height : 260
			});
		} else {
			window.location = Demsy._cfg.loginUrl;
		}
	},
	// 将data转换为Json对象
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
	ajaxError : function(xhr, ajaxOptions, thrownError) {
		if (alerts) {
			alerts.error("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" + "<div>ajaxOptions: " + ajaxOptions + "</div>" + "<div>thrownError: " + thrownError + "</div>" + "<div>" + xhr.responseText + "</div>");
		} else {
			alert("Http status: " + xhr.status + " " + xhr.statusText + "\najaxOptions: " + ajaxOptions + "\nthrownError:" + thrownError + "\n" + xhr.responseText);
		}
	},
	ajaxDone : function(json) {
		if (json.statusCode === undefined && json.message === undefined) {
			if (alerts)
				return alerts.error(json);
			else
				return alert(json);
		}
		if (json.statusCode == Demsy.statusCode.error) {
			if (json.message && alerts)
				alerts.error(json.message);
		} else if (json.statusCode == Demsy.statusCode.timeout) {
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

	init : function(tpl, options) {
		var op = $.extend({
			callback : null
		}, options);
		$.extend(Demsy.pageInfo, op.pageInfo);
	}
};

(function($) {
	$.fn.extend({
		/**
		 * @param {Object}
		 *            op: {type:GET/POST, url:ajax请求地址, data:ajax请求参数列表,
		 *            callback:回调函数 }
		 */
		ajaxUrl : function(op) {
			var $this = $(this);
			if ($.fn.xheditor) {
				$("textarea.editor", $this).xheditor(false);
			}

			$.ajax({
				type : op.type || 'GET',
				url : op.url,
				data : op.data,
				cache : false,
				success : function(response) {
					var json = Demsy.toJson(response);

					if (json.statusCode == Demsy.statusCode.timeout) {
						alerts.error(json.message || Demsy.msg("sessionTimout"), {
							okCall : function() {
								if ($.pdialog)
									$.pdialog.checkTimeout();
								if (navTab)
									navTab.checkTimeout();

								Demsy.login();
							}
						});
					}

					if (json.statusCode == Demsy.statusCode.error) {
						if (json.message)
							alerts.error(json.message);
					} else {
						$this.html(response).initUI();
						if ($.isFunction(op.callback))
							op.callback(response);
					}
				},
				error : Demsy.ajaxError
			});
		},
		loadUrl : function(url, data, callback) {
			$(this).ajaxUrl({
				url : url,
				data : data,
				callback : callback
			});
		},
		initUI : function() {
			return this.each(function() {
				if ($.isFunction(initUI))
					initUI(this);
			});
		},
		/**
		 * adjust component inner reference box height
		 * 
		 * @param {Object}
		 *            refBox: reference box jQuery Obj
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
		inputAlert : function() {
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
			return $(this)[0].tagName.toLowerCase() == tn ? true : false;
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
})(jQuery);

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
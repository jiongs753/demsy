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
		var result = this;
		for ( var i = 0; i < args.length; i++) {
			result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
		}
		return result;
	}
});

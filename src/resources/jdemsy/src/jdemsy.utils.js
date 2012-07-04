(function($, jDemsy) {
	$.extend(jDemsy, {
		/*
		 * 解析目标元素属性选项：[ "attr1","attr2", { bAttr1 : "boolean", nAttr1 :
		 * "number"}]
		 */
		parseOptions : function(target, props) {
			var $target = $(target);
			var opts = {};
			var s = $.trim($target.attr("data-options"));
			if (s) {
				var flag = s.substring(0, 1);
				var _a = s.substring(s.length - 1, 1);
				if (flag != "{") {
					s = "{" + s;
				}
				if (_a != "}") {
					s = s + "}";
				}
				opts = (new Function("return " + s))();
			}
			if (props) {
				var options = {};
				for ( var i = 0; i < props.length; i++) {
					var pp = props[i];
					if (typeof pp == "string") {
						if (pp == "width" || pp == "height" || pp == "left" || pp == "top") {
							options[pp] = parseInt(target.style[pp]) || undefined;
						} else {
							options[pp] = $target.attr(pp);
						}
					} else {
						for ( var name in pp) {
							var _d = pp[name];
							if (_d == "boolean") {
								options[name] = $target.attr(name) ? ($target.attr(name) == "true") : undefined;
							} else {
								if (_d == "number") {
									options[name] = $target.attr(name) == "0" ? 0 : parseFloat($target.attr(name)) || undefined;
								}
							}
						}
					}
				}
				$.extend(opts, options);
			}
			return opts;
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
	});
})(jQuery, jDemsy);
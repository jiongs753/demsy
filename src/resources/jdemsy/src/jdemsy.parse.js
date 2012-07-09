(function($, jDemsy) {
	$.extend(jDemsy, {
		plugins : [ "draggable", "droppable", "resizable", "pagination", "linkbutton", "menu", "menubutton", "splitbutton", "progressbar", "tree", "combobox", "combotree", "combogrid", "numberbox", "validatebox", "searchbox", "numberspinner", "timespinner", "calendar", "datepicker", "slider", "layout", "panel", "datagrid", "propertygrid", "treegrid", "tabs", "accordion", "window", "dialog" ],
		parse : function(context) {
			var jdemsy = this;
			var time0 = new Date().getTime();

			var $uis = $(".jdemsy-ui", context);
			$uis.each(function() {
				var $ui = $(this);
				for ( var i = 0; i < jdemsy.plugins.length; i++) {
					var time1 = new Date().getTime();

					var pluginName = jdemsy.plugins[i];
					if ($ui.hasClass(pluginName)) {
						if ($ui[pluginName]) {
							var plugin = $ui[pluginName]();

							jDemsy.log("{1}. (id = {2})", time1, pluginName, this.id);
						}
					}
				}
			});

			jDemsy.log("plugins.length = {1}", time0, $uis.length);
		},
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
		}
	});
})(jQuery, jDemsy);
$(document).ready(function() {
	jDemsy.parse();
});
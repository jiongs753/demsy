(function($, jDemsy) {
	$.extend(jDemsy, {
		plugins : [ "draggable", "droppable", "resizable", "datepicker", "tabs", "accordion", "dialogbutton", "viewgrid", "flexigrid", "pagination", "linkbutton", "menu", "menubutton", "splitbutton", "progressbar", "tree", "combobox", "combotree", "combogrid", "numberbox", "validatebox", "searchbox", "numberspinner", "timespinner", "calendar", "slider", "layout", "panel", "propertygrid", "treegrid", "window" ],
		/*
		 * 自动解析 jdemsy-ui
		 */
		parse : function(context) {
			var self = this;
			var time0 = new Date().getTime();

			var $uis = $(".jdemsy-ui", context);
			$uis.each(function() {
				var $ui = $(this);
				for ( var i = 0; i < self.plugins.length; i++) {
					var time1 = new Date().getTime();

					var plugin = self.plugins[i];
					if ($ui.hasClass(plugin)) {
						if ($ui[plugin]) {
							$ui[plugin]();

							jDemsy.log("{1}. (id = {2})", time1, plugin, this.id);
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
							var v = $target.attr(name);
							if (_d == "boolean") {
								options[name] = v ? (v == "true") : undefined;
							} else if (_d == "number") {
								options[name] = v == "0" ? 0 : parseFloat(v) || undefined;
							} else if (_d == "json") {
								options[name] = v ? jDemsy.toJson(v) : undefined;
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
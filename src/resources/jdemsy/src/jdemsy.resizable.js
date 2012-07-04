(function($, jDemsy) {
	$.fn.resizable = function(options, args) {

		if (typeof options == "string") {
			return $.fn.resizable.methods[options](this, args);
		}

		function resize(e) {
			var resizeData = e.data;
			var options = $.data(resizeData.target, "resizable").options;
			if (resizeData.dir.indexOf("e") != -1) {
				var width = resizeData.startWidth + e.pageX - resizeData.startX;
				width = Math.min(Math.max(width, options.minWidth), options.maxWidth);
				resizeData.width = width;
			}
			if (resizeData.dir.indexOf("s") != -1) {
				var height = resizeData.startHeight + e.pageY - resizeData.startY;
				height = Math.min(Math.max(height, options.minHeight), options.maxHeight);
				resizeData.height = height;
			}
			if (resizeData.dir.indexOf("w") != -1) {
				resizeData.width = resizeData.startWidth - e.pageX + resizeData.startX;
				if (resizeData.width >= options.minWidth && resizeData.width <= options.maxWidth) {
					resizeData.left = resizeData.startLeft + e.pageX - resizeData.startX;
				}
			}
			if (resizeData.dir.indexOf("n") != -1) {
				resizeData.height = resizeData.startHeight - e.pageY + resizeData.startY;
				if (resizeData.height >= options.minHeight && resizeData.height <= options.maxHeight) {
					resizeData.top = resizeData.startTop + e.pageY - resizeData.startY;
				}
			}
		}

		function applySize(e) {
			var resizeData = e.data;
			var target = resizeData.target;
			if (!$.boxModel && $.browser.msie) {
				$(target).css({
					width : resizeData.width,
					height : resizeData.height,
					left : resizeData.left,
					top : resizeData.top
				});
			} else {
				$(target).css({
					width : resizeData.width - resizeData.deltaWidth,
					height : resizeData.height - resizeData.deltaHeight,
					left : resizeData.left,
					top : resizeData.top
				});
			}
		}

		function doDown(e) {
			$.data(e.data.target, "resizing", true);
			$.data(e.data.target, "resizable").options.onStartResize.call(e.data.target, e);
			return false;
		}

		function doMove(e) {
			resize(e);
			if ($.data(e.data.target, "resizable").options.onResize.call(e.data.target, e) != false) {
				applySize(e);
			}
			return false;
		}

		function doUp(e) {
			$.data(e.data.target, "resizing", false);
			resize(e, true);
			applySize(e);
			$.data(e.data.target, "resizable").options.onStopResize.call(e.data.target, e);
			$(document).unbind(".resizable");
			$("body").css("cursor", "");
			return false;
		}

		return this.each(function() {
			var $this = $(this);
			var opts = null;
			var state = $.data(this, "resizable");
			if (state) {
				$this.unbind(".resizable");
				opts = $.extend(state.options, options || {});
			} else {
				opts = $.extend({}, $.fn.resizable.defaults, $.fn.resizable.parseOptions(this), options || {});
				$.data(this, "resizable", {
					options : opts
				});
			}
			if (opts.disabled == true) {
				return;
			}

			function getCssValue(css) {
				var val = parseInt($this.css(css));
				if (isNaN(val)) {
					return 0;
				} else {
					return val;
				}
			}

			function getDirection(e) {
				var dir = "";
				var offset = $this.offset();
				var width = $this.outerWidth();
				var height = $this.outerHeight();
				var edge = opts.edge;
				if (e.pageY > offset.top && e.pageY < offset.top + edge) {
					dir += "n";
				} else {
					if (e.pageY < offset.top + height && e.pageY > offset.top + height - edge) {
						dir += "s";
					}
				}
				if (e.pageX > offset.left && e.pageX < offset.left + edge) {
					dir += "w";
				} else {
					if (e.pageX < offset.left + width && e.pageX > offset.left + width - edge) {
						dir += "e";
					}
				}
				var handles = opts.handles.split(",");
				for ( var i = 0; i < handles.length; i++) {
					var handle = handles[i].replace(/(^\s*)|(\s*$)/g, "");
					if (handle == "all" || handle == dir) {
						return dir;
					}
				}
				return "";
			}

			$(this).bind("mousemove.resizable", {
				target : this
			}, function(e) {
				if ($.data(this, "resizing")) {
					return;
				}
				var dir = getDirection(e);
				if (dir == "") {
					if (!$.data(this, "draggable"))
						$this.css("cursor", "");
				} else {
					$this.css("cursor", dir + "-resize");
				}
			}).bind("mousedown.resizable", {
				target : this
			}, function(e) {
				var dir = getDirection(e);
				if (dir == "") {
					return;
				}

				var left = getCssValue("left");
				var top = getCssValue("top");
				var width = $this.outerWidth();
				var height = $this.outerHeight();
				var data = {
					target : e.data.target,
					dir : dir,
					startLeft : left,
					startTop : top,
					left : left,
					top : top,
					startX : e.pageX,
					startY : e.pageY,
					startWidth : width,
					startHeight : height,
					width : width,
					height : height,
					deltaWidth : width - $this.width(),
					deltaHeight : height - $this.height()
				};
				$(document).bind("mousedown.resizable", data, doDown);
				$(document).bind("mousemove.resizable", data, doMove);
				$(document).bind("mouseup.resizable", data, doUp);
				$("body").css("cursor", dir + "-resize");
			}).bind("mouseleave.resizable", {
				target : this
			}, function(e) {
				if (!$.data(this, "resizing")) {
					$this.css("cursor", "");
				}
			});

		});
	};
	$.fn.resizable.methods = {
		options : function(jq) {
			return $.data(jq[0], "resizable").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled : false
				});
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$(this).resizable({
					disabled : true
				});
			});
		}
	};
	$.fn.resizable.parseOptions = function(target) {
		var $target = $(target);
		return $.extend({}, jDemsy.parseOptions(target, [ "handles", {
			minWidth : "number",
			minHeight : "number",
			maxWidth : "number",
			maxHeight : "number",
			edge : "number"
		} ]), {
			disabled : ($target.attr("disabled") ? true : undefined)
		});
	};
	$.fn.resizable.defaults = {
		disabled : false,
		handles : "n, e, s, w, ne, se, sw, nw, all",
		minWidth : 10,
		minHeight : 10,
		maxWidth : 10000,
		maxHeight : 10000,
		edge : 10,
		onStartResize : function(e) {
		},
		onResize : function(e) {
		},
		onStopResize : function(e) {
		}
	};
})(jQuery, jDemsy);
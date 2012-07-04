(function($, jDemsy) {
	function drag(e) {
		var opts = $.data(e.data.target, "draggable").options;
		var dragData = e.data;
		var left = dragData.startLeft + e.pageX - dragData.startX;
		var top = dragData.startTop + e.pageY - dragData.startY;
		if (opts.deltaX != null && opts.deltaX != undefined) {
			left = e.pageX + opts.deltaX;
		}
		if (opts.deltaY != null && opts.deltaY != undefined) {
			top = e.pageY + opts.deltaY;
		}
		if (e.data.parent != document.body) {
			left += $(e.data.parent).scrollLeft();
			top += $(e.data.parent).scrollTop();
		}
		if (opts.axis == "h") {
			dragData.left = left;
		} else {
			if (opts.axis == "v") {
				dragData.top = top;
			} else {
				dragData.left = left;
				dragData.top = top;
			}
		}
	}

	function applyDrag(e) {
		var opts = $.data(e.data.target, "draggable").options;
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (proxy) {
			proxy.css("cursor", opts.cursor);
		} else {
			proxy = $(e.data.target);
			$.data(e.data.target, "draggable").handle.css("cursor", opts.cursor);
		}
		proxy.css({
			left : e.data.left,
			top : e.data.top
		});
	}

	function doDown(e) {
		$.data(this, "dragging", true);
		var opts = $.data(e.data.target, "draggable").options;
		var droppables = $(".droppable").filter(function() {
			return e.data.target != this;
		}).filter(function() {
			var accept = $.data(this, "droppable").options.accept;
			if (accept) {
				return $(accept).filter(function() {
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		$.data(e.data.target, "draggable").droppables = droppables;
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (!proxy) {
			if (opts.proxy) {
				if (opts.proxy == "clone") {
					proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				$.data(e.data.target, "draggable").proxy = proxy;
			} else {
				proxy = $(e.data.target);
			}
		}
		proxy.css("position", "absolute");
		drag(e);
		applyDrag(e);
		opts.onStartDrag.call(e.data.target, e);
		return false;
	}

	function doMove(e) {
		drag(e);
		if ($.data(e.data.target, "draggable").options.onDrag.call(e.data.target, e) != false) {
			applyDrag(e);
		}
		var source = e.data.target;
		$.data(e.data.target, "draggable").droppables.each(function() {
			var dropObj = $(this);
			var p2 = $(this).offset();
			if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()) {
				if (!this.entered) {
					$(this).trigger("_dragenter", [ source ]);
					this.entered = true;
				}
				$(this).trigger("_dragover", [ source ]);
			} else {
				if (this.entered) {
					$(this).trigger("_dragleave", [ source ]);
					this.entered = false;
				}
			}
		});
		return false;
	}

	function doUp(e) {
		$.data(this, "dragging", false);
		drag(e);
		var proxy = $.data(e.data.target, "draggable").proxy;
		var opts = $.data(e.data.target, "draggable").options;
		if (opts.revert) {
			if (checkDrop() == true) {
				removeProxy();
				$(e.data.target).css({
					position : e.data.startPosition,
					left : e.data.startLeft,
					top : e.data.startTop
				});
			} else {
				if (proxy) {
					proxy.animate({
						left : e.data.startLeft,
						top : e.data.startTop
					}, function() {
						removeProxy();
					});
				} else {
					$(e.data.target).animate({
						left : e.data.startLeft,
						top : e.data.startTop
					}, function() {
						$(e.data.target).css("position", e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position : "absolute",
				left : e.data.left,
				top : e.data.top
			});
			removeProxy();
			checkDrop();
		}
		opts.onStopDrag.call(e.data.target, e);
		$(document).unbind(".draggable");
		setTimeout(function() {
			$("body").css("cursor", "auto");
		}, 100);
		function removeProxy() {
			if (proxy) {
				proxy.remove();
			}
			$.data(e.data.target, "draggable").proxy = null;
		}

		function checkDrop() {
			var dropped = false;
			$.data(e.data.target, "draggable").droppables.each(function() {
				var dropObj = $(this);
				var p2 = $(this).offset();
				if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()) {
					if (opts.revert) {
						$(e.data.target).css({
							position : e.data.startPosition,
							left : e.data.startLeft,
							top : e.data.startTop
						});
					}
					$(this).trigger("_drop", [ e.data.target ]);
					dropped = true;
					this.entered = false;
				}
			});
			return dropped;
		}

		return false;
	}

	$.fn.draggable = function(options, args) {
		if (typeof options == "string") {
			return $.fn.draggable.methods[options](this, args);
		}
		return this.each(function() {
			var opts;
			var state = $.data(this, "draggable");
			if (state) {
				state.handle.unbind(".draggable");
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.draggable.defaults, $.fn.draggable.parseOptions(this), options || {});
			}
			if (opts.disabled == true) {
				$(this).css("cursor", "default");
				return;
			}
			var handle = null;
			if (typeof opts.handle == "undefined" || opts.handle == null) {
				handle = $(this);
			} else {
				handle = (typeof opts.handle == "string" ? $(opts.handle, this) : opts.handle);
			}
			$.data(this, "draggable", {
				options : opts,
				handle : handle
			});
			handle.unbind(".draggable").bind("mousemove.draggable", {
				target : this
			}, function(e) {
				if ($.data(this, "dragging") || $.data(this, "resizing")) {
					return;
				}
				var opts = $.data(e.data.target, "draggable").options;
				if (checkArea(e)) {
					$(this).css("cursor", opts.cursor);
					// } else {
					// $(this).css("cursor", "");
				}
			}).bind("mouseleave.draggable", {
				target : this
			}, function(e) {
				if (!$.data(this, "dragging") && !$.data(this, "resizing"))
					$(this).css("cursor", "");
			}).bind("mousedown.draggable", {
				target : this
			}, function(e) {
				if (checkArea(e) == false) {
					return;
				}
				var position = $(e.data.target).position();
				var data = {
					startPosition : $(e.data.target).css("position"),
					startLeft : position.left,
					startTop : position.top,
					left : position.left,
					top : position.top,
					startX : e.pageX,
					startY : e.pageY,
					target : e.data.target,
					parent : $(e.data.target).parent()[0]
				};
				$.extend(e.data, data);
				var options = $.data(e.data.target, "draggable").options;
				if (options.onBeforeDrag.call(e.data.target, e) == false) {
					return;
				}
				$(document).bind("mousedown.draggable", e.data, doDown);
				$(document).bind("mousemove.draggable", e.data, doMove);
				$(document).bind("mouseup.draggable", e.data, doUp);
				$("body").css("cursor", options.cursor);
			});
			function checkArea(e) {
				var dragObj = $.data(e.data.target, "draggable");
				var handle = dragObj.handle;
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				return Math.min(t, r, b, l) > dragObj.options.edge;
			}
		});
	};
	$.fn.draggable.methods = {
		options : function(jq) {
			return $.data(jq[0], "draggable").options;
		},
		proxy : function(jq) {
			return $.data(jq[0], "draggable").proxy;
		},
		enable : function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled : false
				});
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled : true
				});
			});
		}
	};
	$.fn.draggable.parseOptions = function(source) {
		var t = $(source);
		return $.extend({}, jDemsy.parseOptions(source, [ "cursor", "handle", "axis", {
			"revert" : "boolean",
			"deltaX" : "number",
			"deltaY" : "number",
			"edge" : "number"
		} ]), {
			disabled : (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.draggable.defaults = {
		proxy : null,
		revert : false,
		cursor : "move",
		deltaX : null,
		deltaY : null,
		handle : null,
		disabled : false,
		edge : 0,
		axis : null,
		onBeforeDrag : function(e) {
		},
		onStartDrag : function(e) {
		},
		onDrag : function(e) {
		},
		onStopDrag : function(e) {
		}
	};
})(jQuery, jDemsy);
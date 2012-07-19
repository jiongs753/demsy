(function($, jDemsy) {
	function init(target) {
		var options = $.data(target, "droppable").options;
		$(target).addClass("droppable").bind("_dragenter", function(e, source) {
			options.onDragEnter.apply(target, [ e, source ]);
		}).bind("_dragleave", function(e, source) {
			options.onDragLeave.apply(target, [ e, source ]);
		}).bind("_dragover", function(e, source) {
			options.onDragOver.apply(target, [ e, source ]);
		}).bind("_drop", function(e, source) {
			options.onDrop.apply(target, [ e, source ]);
		});
	}

	$.fn.droppable = function(options, args) {
		if (typeof options == "string") {
			return $.fn.droppable.methods[options](this, args);
		}
		options = options || {};
		return this.each(function() {
			var dropObj = $.data(this, "droppable");
			if (dropObj) {
				$.extend(dropObj.options, options);
			} else {
				init(this);
				$.data(this, "droppable", {
					options : $.extend({}, defaults, parseOptions(this), options)
				});
			}
		});
	};
	$.fn.droppable.methods = {};
	function parseOptions(target) {
		return jDemsy.parseOptions(target, [ "accept" ]);
	}
	var defaults = {
		accept : null,
		onDragEnter : function(e, source) {
		},
		onDragOver : function(e, source) {
		},
		onDragLeave : function(e, source) {
		},
		onDrop : function(e, source) {
		}
	};
})(jQuery, jDemsy);
(function($) {
	function Popup(el, content, options) {
		this.input = $(el);
		this.popupContent = content;
		this.options = options;

		this.bindMethodsToObj("show", "hide", "hideIfClickOutside", "selectItem");

		this.loaded = false;
		this.build();
		this.hide(false);
	}

	Popup.prototype = {
		build : function() {
			var w = this.options.width;
			var h = this.options.height;
			this.popupPanel = this.rootLayers = $('<div class="ui-widget-content popup" style="border: 1px solid #000000; padding:2px; overflow: auto;"></div>').css({
				display : "none",
				position : "absolute",
				zIndex : 99999
			// }).insertBefore(this.input);
			}).appendTo(document.body);
			if (w) {
				if (w != "auto")
					this.popupPanel.width(w);
			} else {
				this.popupPanel.width(this.input.width());
			}
			if (h) {
				this.popupPanel.height(h);
			}

			if (this.options.url) {
			} else {
				this.popupPanel.append(this.popupContent);
			}

			if ($.browser.msie && $.browser.version < 7) {
				this.ieframe = $('<iframe class="popupPanel_ieframe" frameborder="0" src="#"></iframe>').css({
					position : "absolute",
					display : "none",
					zIndex : 99
				}).insertBefore(this.popupPanel);
				this.rootLayers = this.rootLayers.add(this.ieframe);
			}
		},

		selectItem : function(item, text) {
			this.selectedItem = item;
			this.input.attr("SelectedItem", item);
			this.input.val(text);
		},

		show : function() {
			var self = this;
			this.setPosition();
			this.rootLayers.css("display", "block");
			this.input.unbind("click", this.show);
			$(document.body).click(this.hideIfClickOutside);
			if (this.options.show) {
				this.options.show();
			}
			if (this.options.url && this.loaded == false) {
				this.loaded = true;
				this.popupPanel.html(this.options.loadding);
				this.popupPanel.load(this.options.url, function() {
				});
			}
		},

		hide : function(fireEvent) {
			this.rootLayers.css("display", "none");
			$(document.body).unbind("click", this.hideIfClickOutside);
			this.input.click(this.show);
			if (this.options.hide && fireEvent != false) {
				this.options.hide();
			}
		},

		hideIfClickOutside : function(event) {
			if (event && event.pageY && event.pageX && event.target != this.input[0] && !this.insideSelector(event)) {
				this.hide();
			}
		},

		setPosition : function() {
			// this.rootLayers.css({
			// top : this.input.outerHeight(),
			// left : 0
			// });

			var offset = this.input.offset();
			this.rootLayers.css({
				top : offset.top + this.input.outerHeight(),
				left : offset.left
			});

			if (this.ieframe) {
				this.ieframe.css({
					width : this.popupPanel.outerWidth(),
					height : this.popupPanel.outerHeight()
				});
			}
		},

		insideSelector : function(event) {
			var offset = this.popupPanel.offset();
			offset.right = offset.left + this.popupPanel.outerWidth();
			offset.bottom = offset.top + this.popupPanel.outerHeight();

			return event.pageY < offset.bottom && event.pageY > offset.top && event.pageX < offset.right && event.pageX > offset.left;
		},

		bindToObj : function(fn) {
			var self = this;
			return function() {
				return fn.apply(self, arguments)
			};
		},

		bindMethodsToObj : function() {
			for ( var i = 0; i < arguments.length; i++) {
				this[arguments[i]] = this.bindToObj(this[arguments[i]]);
			}
		}

	};

	$.fn.popup = function(content, options) {
		return new Popup(this, content, options);
	};
})(jQuery);
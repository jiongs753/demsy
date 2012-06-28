(function($) {
	$.alerts = {
		verticalOffset : -75,
		horizontalOffset : 0,
		repositionOnResize : true,// 浏览器窗口大小变化后是否重新计算提示框的位置？
		overlayOpacity : .01,
		overlayColor : '#FFF',
		draggable : true,
		dialogClass : null,

		// Private methods
		_show : function(title, msg, value, type, callback) {

			$.alerts._hide();
			$.alerts._overlay('show');

			var $container = $('<div id="alerts_container"><h1 id="alerts_title"></h1><div id="alerts_content"><div class="alerts_logo"></div><div id="alerts_message"></div></div></div>').appendTo($("BODY"));

			if ($.alerts.dialogClass)
				$container.addClass($.alerts.dialogClass);

			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6) ? 'absolute' : 'fixed';

			$container.css({
				position : pos,
				zIndex : 99999,
				padding : 0,
				margin : 0
			});

			var $title = $("#alerts_title", $container).text(title);
			var $content = $("#alerts_content", $container).addClass(type);
			var $message = $("#alerts_message", $container).html(msg.replace(/\n/g, '<br />'));

			$container.css({
				minWidth : $container.outerWidth(),
				maxWidth : $container.outerWidth()
			});

			$.alerts._reposition();
			$.alerts._bindWindowResize(true);

			switch (type) {
			case 'confirm':
				$message.after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /><input type="button" value="{1}" id="alerts_cancel" /></div>'.format(Dnls.buttons.ok, Dnls.buttons.cancel));
				var $ok = $("#alerts_ok", $container);
				var $cancel = $("#alerts_cancel", $container);
				$ok.click(function() {
					$.alerts._hide();
					if (callback)
						callback(true);
				}).focus().keypress(function(e) {
					if (e.keyCode == DkeyCode.ENTER)
						$ok.trigger('click');
					if (e.keyCode == DkeyCode.ESC)
						$cancel.trigger('click');
				});
				$cancel.click(function() {
					$.alerts._hide();
					if (callback)
						callback(false);
				}).keypress(function(e) {
					if (e.keyCode == DkeyCode.ENTER)
						$ok.trigger('click');
					if (e.keyCode == DkeyCode.ESC)
						$cancel.trigger('click');
				});

				break;
			case 'prompt':
				$message.append('<br /><input type="text" size="30" id="alerts_prompt" />').after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /><input type="button" value="{1}" id="alerts_cancel" /></div>'.format(Dnls.buttons.ok, Dnls.buttons.cancel));
				var $prompt = $("#alerts_prompt", $container).width($message.width());
				var $ok = $("#alerts_ok", $container);
				var $cancel = $("#alerts_cancel", $container);
				$ok.click(function() {
					var val = $prompt.val();
					$.alerts._hide();
					if (callback)
						callback(val);
				}).keypress(function(e) {
					if (e.keyCode == DkeyCode.ENTER)
						$ok.trigger('click');
					if (e.keyCode == DkeyCode.ESC)
						$cancel.trigger('click');
				});
				$cancel.click(function() {
					$.alerts._hide();
					if (callback)
						callback(null);
				}).keypress(function(e) {
					if (e.keyCode == DkeyCode.ENTER)
						$ok.trigger('click');
					if (e.keyCode == DkeyCode.ESC)
						$cancel.trigger('click');
				});
				if (value)
					$prompt.val(value);
				$prompt.focus().select();

				break;
			default:
				$message.after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /></div>'.format(Dnls.buttons.ok));
				var $ok = $("#alerts_ok", $container);
				$ok.click(function() {
					$.alerts._hide();
					if (callback)
						callback(true);
				}).focus().keypress(function(e) {
					if (e.keyCode == DkeyCode.ENTER || e.keyCode == DkeyCode.ESC)
						$ok.trigger('click');
				});

				break;
			}

			// Make draggable
			if ($.alerts.draggable) {
				try {
					$container.draggable({
						handle : $title
					});
					$title.css({
						cursor : 'move'
					});
				} catch (e) {
				}
			}
		},

		/*
		 * 隐藏提示框
		 */
		_hide : function() {
			$("#alerts_container").remove();
			$.alerts._overlay('hide');
			$.alerts._bindWindowResize(false);
		},

		/*
		 * 锁定浏览器窗口内容
		 */
		_overlay : function(status) {
			switch (status) {
			case 'show':
				$.alerts._overlay('hide');
				$('<div id="alerts_overlay"></div>').css({
					position : 'absolute',
					zIndex : 99998,
					top : '0px',
					left : '0px',
					width : '100%',
					height : $(document).height(),
					background : $.alerts.overlayColor,
					opacity : $.alerts.overlayOpacity
				}).appendTo($("BODY"));

				break;
			case 'hide':
				$("#alerts_overlay").remove();

				break;
			}
		},

		/*
		 * 重新计算提示框在浏览器窗口中的位置
		 */
		_reposition : function() {
			var $container = $("#alerts_container");
			var $window = $(window);
			var top = (($window.height() / 2) - ($container.outerHeight() / 2)) + $.alerts.verticalOffset;
			var left = (($window.width() / 2) - ($container.outerWidth() / 2)) + $.alerts.horizontalOffset;
			if (top < 0)
				top = 0;
			if (left < 0)
				left = 0;

			// IE6 fix
			if ($.browser.msie && parseInt($.browser.version) <= 6)
				top = top + $window.scrollTop();

			$container.css({
				top : top + 'px',
				left : left + 'px'
			});
			$("#alerts_overlay").height($(document).height());
		},

		/*
		 * 绑定浏览器窗口 resize 事件：即浏览器窗口大小发生变化时，重新计算提示框位置。
		 */
		_bindWindowResize : function(status) {
			if ($.alerts.repositionOnResize) {
				switch (status) {
				case true:
					$(window).bind('resize', $.alerts._reposition);
					break;
				case false:
					$(window).unbind('resize', $.alerts._reposition);
					break;
				}
			}
		}
	}

	// Public methods
	Dalert = function(message, title, type, callback) {
		$.alerts._show(title, message, null, type, function(result) {
			if (callback)
				callback(result);
		});
	};
	Derror = function(message, title, callback) {
		Dalert(message, title || Dnls.alerts.error, "error", callback);
	};
	Dwarn = function(message, title, callback) {
		Dalert(message, title || Dnls.alerts.warn, "warn", callback);
	};
	Dinfo = function(message, title, callback) {
		Dalert(message, title || Dnls.alerts.info, "info", callback);
	};
	Dsuccess = function(message, title, callback) {
		Dalert(message, title || Dnls.alerts.success, "success", callback);
	};
	Dconfirm = function(message, title, callback) {
		$.alerts._show(title || Dnls.alerts.confirm, message, null, 'confirm', function(result) {
			if (callback)
				callback(result);
		});
	};
	Dprompt = function(message, value, title, callback) {
		$.alerts._show(title || Dnls.alerts.prompt, message, value, 'prompt', function(result) {
			if (callback)
				callback(result);
		});
	};

})(jQuery);
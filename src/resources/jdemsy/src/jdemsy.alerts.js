(function($, jDemsy) {
	jDemsy.alerts = {
		verticalOffset : -75,
		horizontalOffset : 0,
		repositionOnResize : true,// 浏览器窗口大小变化后是否重新计算提示框的位置？
		overlayOpacity : .01,
		overlayColor : '#FFF',
		draggable : true,
		dialogClass : null
	}

	/*
	 * 显示提示框
	 */
	function show(title, msg, value, type, callback) {
		var beginTime = new Date().getTime();

		hide();
		overlay('show');

		var $container = $('<div id="alerts_container"><h1 id="alerts_title"></h1><div id="alerts_content"><div class="alerts_logo"></div><div id="alerts_message"></div></div></div>').appendTo($("BODY"));

		if (jDemsy.alerts.dialogClass)
			$container.addClass(jDemsy.alerts.dialogClass);

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

		reposition();
		bindWindowResize(true);

		switch (type) {
		case 'confirm':
			$message.after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /><input type="button" value="{1}" id="alerts_cancel" /></div>'.format(jDemsy.nls.ok, jDemsy.nls.cancel));
			var $ok = $("#alerts_ok", $container);
			var $cancel = $("#alerts_cancel", $container);
			$ok.click(function() {
				hide();
				if (callback)
					callback(true);
			}).focus().keypress(function(e) {
				if (e.keyCode == jDemsy.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jDemsy.keyCode.ESC)
					$cancel.trigger('click');
			});
			$cancel.click(function() {
				hide();
				if (callback)
					callback(false);
			}).keypress(function(e) {
				if (e.keyCode == jDemsy.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jDemsy.keyCode.ESC)
					$cancel.trigger('click');
			});

			break;
		case 'prompt':
			$message.append('<br /><input type="text" size="30" id="alerts_prompt" />').after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /><input type="button" value="{1}" id="alerts_cancel" /></div>'.format(jDemsy.nls.ok, jDemsy.nls.cancel));
			var $prompt = $("#alerts_prompt", $container).width($message.width());
			var $ok = $("#alerts_ok", $container);
			var $cancel = $("#alerts_cancel", $container);
			$ok.click(function() {
				var val = $prompt.val();
				hide();
				if (callback)
					callback(val);
			}).keypress(function(e) {
				if (e.keyCode == jDemsy.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jDemsy.keyCode.ESC)
					$cancel.trigger('click');
			});
			$cancel.click(function() {
				hide();
				if (callback)
					callback(null);
			}).keypress(function(e) {
				if (e.keyCode == jDemsy.keyCode.ENTER)
					$ok.trigger('click');
				if (e.keyCode == jDemsy.keyCode.ESC)
					$cancel.trigger('click');
			});
			if (value)
				$prompt.val(value);
			$prompt.focus().select();

			break;
		default:
			$message.after('<div id="alerts_panel"><input type="button" value="{0}" id="alerts_ok" /></div>'.format(jDemsy.nls.ok));
			var $ok = $("#alerts_ok", $container);
			$ok.click(function() {
				hide();
				if (callback)
					callback(true);
			}).focus().keypress(function(e) {
				if (e.keyCode == jDemsy.keyCode.ENTER || e.keyCode == jDemsy.keyCode.ESC)
					$ok.trigger('click');
			});

			break;
		}

		// Make draggable
		if (jDemsy.alerts.draggable) {
			try {
				$container.draggable({
					handle : $title
				});
				$title.css({
					cursor : 'move'
				});
			} catch (e) {
				alert(e);
			}
		}

		jDemsy.log("创建提示框：耗时 {0}ms.", (new Date().getTime() - beginTime));
	}

	/*
	 * 隐藏提示框
	 */
	function hide() {
		$("#alerts_container").remove();
		overlay('hide');
		bindWindowResize(false);
	}

	/*
	 * 遮住窗口内容，即弹出提示框时，禁止点击。
	 */
	function overlay(status) {
		switch (status) {
		case 'show':
			overlay('hide');
			$('<div id="alerts_overlay"></div>').css({
				position : 'absolute',
				zIndex : 99998,
				top : '0px',
				left : '0px',
				width : '100%',
				height : $(document).height(),
				background : jDemsy.alerts.overlayColor,
				opacity : jDemsy.alerts.overlayOpacity
			}).appendTo($("BODY"));

			break;
		case 'hide':
			$("#alerts_overlay").remove();

			break;
		}
	}

	/*
	 * 重新计算提示框在浏览器窗口中的位置
	 */
	function reposition() {
		var $container = $("#alerts_container");
		var $window = $(window);
		var top = (($window.height() / 2) - ($container.outerHeight() / 2)) + jDemsy.alerts.verticalOffset;
		var left = (($window.width() / 2) - ($container.outerWidth() / 2)) + jDemsy.alerts.horizontalOffset;
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
	}

	/*
	 * 绑定浏览器窗口 resize 事件：即浏览器窗口大小发生变化时，重新计算提示框位置。
	 */
	function bindWindowResize(status) {
		if (jDemsy.alerts.repositionOnResize) {
			switch (status) {
			case true:
				$(window).bind('resize', reposition);
				break;
			case false:
				$(window).unbind('resize', reposition);
				break;
			}
		}
	}

	/*
	 * 弹出只有OK按钮的提示框
	 */
	function alert(message, title, type, callback) {
		show(title, message, null, type, function(result) {
			if (callback)
				callback(result);
		});
	}

	// 公共函数
	jError = function(message, title, callback) {
		alert(message, title || jDemsy.nls.error, "error", callback);
	};
	jWarn = function(message, title, callback) {
		alert(message, title || jDemsy.nls.warn, "warn", callback);
	};
	jInfo = function(message, title, callback) {
		alert(message, title || jDemsy.nls.info, "info", callback);
	};
	jSuccess = function(message, title, callback) {
		alert(message, title || jDemsy.nls.success, "success", callback);
	};
	jConfirm = function(message, title, callback) {
		show(title || jDemsy.nls.confirm, message, null, 'confirm', function(result) {
			if (callback)
				callback(result);
		});
	};
	jPrompt = function(message, value, title, callback) {
		show(title || jDemsy.nls.prompt, message, value, 'prompt', function(result) {
			if (callback)
				callback(result);
		});
	};

})(jQuery, jDemsy);
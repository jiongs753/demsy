(function($) {
	$.fn.sticklr = function(method) {
		var defaults = {
			animate : true,
			colWidth : '180px',
			showOn : 'click',
			stickTo : 'left'
		}, methods = {
			init : function(opts) {
				return this.each(function() {
					var $sticklr = $(this);
					var props = $.extend({}, defaults, opts);
					if (!$sticklr.hasClass('sticklr'))
						$sticklr.addClass('sticklr');
					if (props.stickTo == 'right')
						$sticklr.addClass('sticklr-right');
					if (props.showOn == 'hover')
						props.showOn = 'mouseenter mouseleave';
					$sticklr.addClass('sticklr-js').css({
						'position' : props.animate ? 'absolute' : 'fixed',
						'top' : props.animate ? (($(window).height() - $sticklr.height()) / 2) : (($(window).height() - $sticklr.height()) / 2) + $(window).scrollTop()
					}).find('a[href="#"]').bind('click', function(e) {
						e.preventDefault()
					}).end().children('li').children('a').bind(props.showOn, function() {
						if ($(this).siblings().hasClass('sticklr-active'))
							return false;
						methods.hide();
						$(this).append('<span class="sticklr-arrow"></span>').siblings().each(function() {
							$(this).css({
								'margin-left' : '50px',
								'margin-right' : '50px',
								'opacity' : 0,
								'position' : 'absolute',
								'top' : 0
							}).show();
							var totalHeight = $(this).height() + $(this).offset().top;
							var windowHeight = $(window).height() + $(window).scrollTop();
							var newTop = 0;
							if (totalHeight > windowHeight) {
								newTop = parseInt($(this).css('top'), 10) - (totalHeight - windowHeight)
							}
							$(this).css({
								'top' : newTop
							}).animate({
								'margin-left' : '20px',
								'margin-right' : '20px',
								'opacity' : 1.0
							}, 0)
						}).addClass('sticklr-active');
						return false
					});
					if ($.browser.msie || props.colWidth != defaults.colWidth) {
						for ( var i = 2; i < 10; i++) {
							var newLeft = 23 + i + (parseInt(props.colWidth, 10) * (i - 2));
							var newRight = 'auto';
							if (props.stickTo == 'right') {
								newRight = newLeft;
								newLeft = 'auto'
							}
							$sticklr.find('li').find('ul:nth-child(' + i + ')').css({
								'left' : newLeft,
								'right' : newRight,
								'width' : props.colWidth
							})
						}
					}
				})
			},
			fixPos : function() {
				$('.sticklr').each(function() {
					if ($(this).css('position') == 'absolute') {
						$(this).stop().animate({
							'top' : ($(window).height() / 2 - $(this).height() / 2) + $(window).scrollTop()
						}, 0)
					} else {
						$(this).css({
							'top' : ($(window).height() / 2 - $(this).height() / 2)
						})
					}
				})
			},
			hide : function() {
				$('.sticklr-active').animate({
					'margin-left' : '50px',
					'margin-right' : '50px',
					'opacity' : 0
				}, 0, function() {
					$(this).removeClass('sticklr-active').hide()
				});
				$('span.sticklr-arrow').remove()
			}
		};
		$(window).resize(function() {
			methods.fixPos()
		});
		$(window).scroll(function() {
			methods.fixPos()
		});
		$(document).click(function(e) {
			if ($(e.target).parents().hasClass('sticklr'))
				return;
			methods.hide()
		});
		if (methods[method] && method.toLowerCase() != 'init') {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1))
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments)
		} else {
			$.error('Method "' + method + '" does not exist in Sticklr')
		}
	}
})(jQuery);
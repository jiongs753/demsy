(function($) {
	$.fn.accordion = function(options) {
		var self = this;
		var menu = $(".menu", $(this));
		menu.click(function() {
			var ths = $(this);
			var href = $("span", ths).attr("href");

			menu.removeClass("menuCurrent");
			$(".content", self).hide();
			$(href, self).show();

			ths.addClass("menuCurrent");

			return false;
		}).hover(function() {
			$(this).addClass("menuHover");
		}, function() {
			$(this).removeClass("menuHover");
		});

		$(menu.get(0)).click();
	};
})(jQuery);
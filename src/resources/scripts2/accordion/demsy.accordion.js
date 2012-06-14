(function($) {
	$.fn.accordion = function(options) {
		var self = this;
		var menu = $(".menu", $(this));
		menu.click(function() {
			var ths = $(this);
			var href = $("span", ths).attr("href");
			
			menu.removeClass("currentmenu");
			$(".content", self).hide();
			$(href, self).show();
			
			ths.addClass("currentmenu");
			
			return false;
		});

		$(menu.get(0)).click();
	};
})(jQuery);
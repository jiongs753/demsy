// 模版配置
(function($, jDemsy) {
	var tplDialog = '<div class="dialog" style="top:150px; left:300px;">\
		<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">\
		<div class="dialogHeader_r">\
			<div class="dialogHeader_c">\
				<a class="close" href="#close">close</a>\
				<a class="maximize" href="#maximize">maximize</a>\
				<a class="restore" href="#restore">restore</a>\
				<a class="minimize" href="#minimize">minimize</a>\
				<h1></h1>\
			</div>\
		</div>\
	</div>\
	<div class="dialogContent layoutBox unitBox">\
	</div>\
	<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>\
	<div class="resizable_h_l" tar="nw"></div>\
	<div class="resizable_h_r" tar="ne"></div>\
	<div class="resizable_h_c" tar="n"></div>\
	<div class="resizable_c_l" tar="w" style="height:300px;"></div>\
	<div class="resizable_c_r" tar="e" style="height:300px;"></div>\
	<div class="resizable_f_l" tar="sw"></div>\
	<div class="resizable_f_r" tar="se"></div>\
	<div class="resizable_f_c" tar="s"></div>\
	</div>';

	// 扩展
	$.extend(jDemsy.tpl, {
		dialog : tplDialog
	});
})(jQuery, jDemsy);

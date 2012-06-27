(function($) {
	$.extend(Demsy, {
		debug : false,
		// 键盘代码
		keyCode : {
			ENTER : 13,
			ESC : 27,
			END : 35,
			HOME : 36,
			SHIFT : 16,
			TAB : 9,
			LEFT : 37,
			RIGHT : 39,
			UP : 38,
			DOWN : 40,
			DELETE : 46,
			BACKSPACE : 8
		},
		// 分页字段名称
		fields : {
			pageIndex : "pageIndex",
			pageSize : "pageSize",
			orderBy : "orderBy"
		},
		// 后台的JSON对象对应的状态码
		ajaxStatus : {
			ok : 200,
			error : 300,
			timeout : 301
		},
		urls : {
			login : "/login/load"// 登录页面的URL地址
		},
		// 弹出窗口模版
		templates : {
			dialog : '<div class="dialog" style="top:150px; left:300px;">'//
					+ '<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">'//
					+ '	<div class="dialogHeader_r">'//
					+ '		<div class="dialogHeader_c">'//
					+ '			<a class="close" href="#close">close</a>'//
					+ '			<a class="maximize" href="#maximize">maximize</a>'//
					+ '			<a class="restore" href="#restore">restore</a>'//
					+ '			<a class="minimize" href="#minimize">minimize</a>'//
					+ '			<h1>弹出窗口</h1>'//
					+ '		</div>'//
					+ '	</div>'//
					+ '</div>'//
					+ '<div class="dialogContent layoutBox unitBox">'//
					+ '</div>'//
					+ '<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>'//
					+ '<div class="resizable_h_l" tar="nw"></div>'//
					+ '<div class="resizable_h_r" tar="ne"></div>'//
					+ '<div class="resizable_h_c" tar="n"></div>'//
					+ '<div class="resizable_c_l" tar="w" style="height:300px;"></div>'//
					+ '<div class="resizable_c_r" tar="e" style="height:300px;"></div>'//
					+ '<div class="resizable_f_l" tar="sw"></div>'//
					+ '<div class="resizable_f_r" tar="se"></div>'//
					+ '<div class="resizable_f_c" tar="s"></div>'//
					+ '</div>'//
		}
	});

})(jQuery);
// 模版配置
(function($, jDemsy) {
	var tplDialog = '\
	<div class="dialog" style="top:150px; left:300px;">\
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
	var tplCalendar = '\
	<div id="calendar">\
		<div class="main">\
			<div class="head">\
				<table width="100%" border="0" cellpadding="0" cellspacing="2">\
				<tr>\
					<td><select name="year"></select></td>\
					<td><select name="month"></select></td>\
					<td width="20"><span class="close">×</span></td>\
				</tr>\
				</table>\
			</div>\
			<div class="body">\
				<dl class="weeks"><dt>日</dt><dt>一</dt><dt>二</dt><dt>三</dt><dt>四</dt><dt>五</dt><dt>六</dt></dl>\
				<dl class="days">日期列表选项</dl>\
				<div style="clear:both;height:0;line-height:0"></div>\
				<div class="timeMenu">\
					<ul class="hh">\
						<li>0</li>\
						<li>1</li>\
						<li>2</li>\
						<li>3</li>\
						<li>4</li>\
						<li>5</li>\
						<li>6</li>\
						<li>7</li>\
						<li>8</li>\
						<li>9</li>\
						<li>10</li>\
						<li>11</li>\
						<li>12</li>\
						<li>13</li>\
						<li>14</li>\
						<li>15</li>\
						<li>16</li>\
						<li>17</li>\
						<li>18</li>\
						<li>19</li>\
						<li>20</li>\
						<li>21</li>\
						<li>22</li>\
						<li>23</li>\
					</ul>\
					<ul class="mm">\
						<li>0</li>\
						<li>5</li>\
						<li>10</li>\
						<li>15</li>\
						<li>20</li>\
						<li>25</li>\
						<li>30</li>\
						<li>35</li>\
						<li>40</li>\
						<li>45</li>\
						<li>50</li>\
						<li>55</li>\
					</ul>\
					<ul class="ss">\
						<li>0</li>\
						<li>10</li>\
						<li>20</li>\
						<li>30</li>\
						<li>40</li>\
						<li>50</li>\
					</ul>\
				</div>\
			</div>\
			<div class="foot">\
				<table class="time">\
					<tr>\
						<td>\
							<input type="text" class="timeHH" maxlength="2" start="0" end="23"/>:\
							<input type="text" class="timeMM" maxlength="2" start="0" end="59"/>:\
							<input type="text" class="timeSS" maxlength="2" start="0" end="59"/>\
						</td>\
						<td><ul><li class="timeUpBtn">&and;</li><li class="timeDownBtn">&or;</li></ul></td>\
					</tr>\
				</table>\
				<button type="button" class="btnClear">清空</button>\
				<button type="button" class="btnConfirm">确定</button>\
			<div>\
		</div>\
	</div>';
	// 扩展
	$.extend(jDemsy.tpl, {
		dialog : tplDialog,
		calendar : tplCalendar
	});
})(jQuery, jDemsy);

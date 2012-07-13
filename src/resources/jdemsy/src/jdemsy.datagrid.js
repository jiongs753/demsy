(function($) {
	// function getLeft(obj) {
	// var width = 0;
	// $(obj).prevAll().each(function() {
	// width += $(this).outerWidth();
	// });
	// return width - 1;
	// }
	// function getRight(obj) {
	// var width = 0;
	// $(obj).prevAll().andSelf().each(function() {
	// width += $(this).outerWidth();
	// });
	// return width - 1;
	// }
	// function getTop(obj) {
	// var height = 0;
	// $(obj).parent().prevAll().each(function() {
	// height += $(this).outerHeight();
	// });
	// return height;
	// }
	// function getHeight(obj, parent) {
	// var height = 0;
	// var head = $(obj).parent();
	// head.nextAll().andSelf().each(function() {
	// height += $(this).outerHeight();
	// });
	// $(".gridTbody", parent).children().each(function() {
	// height += $(this).outerHeight();
	// });
	// return height;
	// }
	// function getCellNum(obj) {
	// return $(obj).prevAll().andSelf().size();
	// }
	// function getColspan(obj) {
	// return $(obj).attr("colspan") || 1;
	// }
	// function getStart(obj) {
	// var start = 1;
	// $(obj).prevAll().each(function() {
	// start += parseInt($(this).attr("colspan") || 1);
	// });
	// return start;
	// }
	// function getPageCoord(element) {
	// var coord = {
	// x : 0,
	// y : 0
	// };
	// while (element) {
	// coord.x += element.offsetLeft;
	// coord.y += element.offsetTop;
	// element = element.offsetParent;
	// }
	// return coord;
	// }
	// function getOffset(obj, evt) {
	// if ($.browser.msie) {
	// var objset = $(obj).offset();
	// var evtset = {
	// offsetX : evt.pageX || evt.screenX,
	// offsetY : evt.pageY || evt.screenY
	// };
	// var offset = {
	// offsetX : evtset.offsetX - objset.left,
	// offsetY : evtset.offsetY - objset.top
	// };
	// return offset;
	// }
	// var target = evt.target;
	// if (target.offsetLeft == undefined) {
	// target = target.parentNode;
	// }
	// var pageCoord = getPageCoord(target);
	// var eventCoord = {
	// x : window.pageXOffset + evt.clientX,
	// y : window.pageYOffset + evt.clientY
	// };
	// var offset = {
	// offsetX : eventCoord.x - pageCoord.x,
	// offsetY : eventCoord.y - pageCoord.y
	// };
	// return offset;
	// }
	$.fn.datagrid = function(options) {
		return this.each(function() {
			var $table = $(this), nowrapTD = $table.attr("nowrapTD");
			var twidth = $table.width();
			var $container = $table.parent().addClass("j-resizeGrid");
			// var layoutH = $(this).attr("layoutH");

			// 缓存表头最后一行的样式
			var hLastThStyles = [];
			var $hLastThList = $table.find("thead>tr:last-child").find("th");
			for ( var i = 0, l = $hLastThList.size(); i < l; i++) {
				var $th = $($hLastThList[i]);
				var style = [], width = $th.innerWidth() - (100 * $th.innerWidth() / twidth) - 2;
				style[0] = parseInt(width);
				style[1] = $th.attr("align");
				hLastThStyles[hLastThStyles.length] = style;
			}

			// 创建DataGrid对象将原始Table包装进去
			$(this).wrap("<div class='grid'></div>");
			var $grid = $table.parent().html($table.html());

			// 创建DataGrid表头将原来的Table表头包装起来
			var $thead = $grid.find("thead");
			$thead.wrap("<div class='gridHeader'><div class='gridThead'><table style='width:" + (twidth - 20) + "px;'></table></div></div>");

			// 包装表头单元格
			$("th", $thead).each(function() {
				var $th = $(this);
				$th.html("<div class='gridCol' title='" + $th.text() + "'>" + $th.html() + "</div>");
			});

			// 恢复表头最后一行的样式
			// var $hLastRow = $(">tr:last-child", $thead);
			// $(">th", $hLastRow).
			$hLastThList.each(function(i) {
				var $th = $(this), style = hLastThStyles[i];
				$th.addClass(style[1]).hoverClass("hover").removeAttr("align").removeAttr("width").width(style[0]);
				// 取消排序
				// }).filter("[orderField]").orderBy({
				// targetType : $table.attr("targetType"),
				// rel : $table.attr("rel"),
				// asc : $table.attr("asc") || "asc",
				// desc : $table.attr("desc") || "desc"
			});

			// 创建DataGrid滚动列表将原来的Table内容包装起来
			var $tbody = $grid.find(">tbody");
			$tbody.wrap("<div class='gridScroller' style='width:" + $container.width() + "px;'><div class='gridTbody'><table style='width:" + (twidth - 20) + "px;'></table></div></div>");
			var $bFirstTrList = $(">tr:first-child", $tbody);
			var $bTrList = $tbody.find('>tr');
			$bTrList.hoverClass().each(function() {
				var $tr = $(this);
				var $tdList = $(">td", this);

				// 设置内容单元样式
				for ( var i = 0, len = $tdList.size(); i < len; i++) {
					var $td = $($tdList[i]);
					if (nowrapTD != "false")
						$td.html("<div>" + $td.html() + "</div>");
					if (i < hLastThStyles.length)
						$td.addClass(hLastThStyles[i][1]);
				}

				// 选中一行
				$tr.click(function() {
					$bTrList.filter(".selected").removeClass("selected");
					$tr.addClass("selected");
					var sTarget = $tr.attr("target");
					if (sTarget) {
						var $target = $("#" + sTarget, $grid);
						if ($target.size() == 0) {
							$grid.prepend('<input id="' + sTarget + '" type="hidden" />');
						}
						$target.val($tr.attr("rel"));
					}
				});
			});

			// 设置DataGrid首行单元格宽度
			$(">td", $bFirstTrList).each(function(i) {
				if (i < hLastThStyles.length)
					$(this).width(hLastThStyles[i][0]);
			});
			// $grid.append("<div class='resizeMarker' style='height:300px;
			// left:57px;display:none;'></div><div class='resizeProxy'
			// style='height:300px; left:377px;display:none;'></div>");

			var $scroller = $(".gridScroller", $grid).height(300);
			$scroller.scroll(function(event) {
				var header = $(".gridThead", $grid);
				if ($scroller.scrollLeft() > 0) {
					header.css("position", "relative");
					var scroll = $scroller.scrollLeft();
					header.css("left", $scroller.getCssValue("left") - scroll);
				}
				if ($scroller.scrollLeft() == 0) {
					header.css("position", "relative");
					header.css("left", "0px");
				}
				return false;
			});

			// $(">tr", $thead).each(function() {
			// $(">th", this).each(function(i) {
			// var th = this, $th = $(this);
			// $th.mouseover(function(event) {
			// var offset = getOffset(th, event).offsetX;
			// if ($th.outerWidth() - offset < 5) {
			// $th.css("cursor", "col-resize").mousedown(function(event) {
			// $(".resizeProxy", $grid).show().css({
			// left : getRight(th) - $(".gridScroller", $grid).scrollLeft(),
			// top : getTop(th),
			// height : getHeight(th, $grid),
			// cursor : "col-resize"
			// });
			// $(".resizeMarker", $grid).show().css({
			// left : getLeft(th) + 1 - $(".gridScroller", $grid).scrollLeft(),
			// top : getTop(th),
			// height : getHeight(th, $grid)
			// });
			// $(".resizeProxy", $grid).jDrag($.extend(options, {
			// scop : true,
			// cellMinW : 20,
			// relObj : $(".resizeMarker", $grid)[0],
			// move : "horizontal",
			// event : event,
			// stop : function() {
			// var pleft = $(".resizeProxy", $grid).position().left;
			// var mleft = $(".resizeMarker", $grid).position().left;
			// var move = pleft - mleft - $th.outerWidth() - 9;
			//
			// var cols = getColspan($th);
			// var cellNum = getCellNum($th);
			// var oldW = $th.width(), newW = $th.width() + move;
			// var $dcell = $(">td", $bFirstTrList).eq(cellNum - 1);
			//
			// $th.width(newW + "px");
			// $dcell.width(newW + "px");
			//
			// var $table1 = $thead.parent();
			// $table1.width(($table1.width() - oldW + newW) + "px");
			// var $table2 = $tbody.parent();
			// $table2.width(($table2.width() - oldW + newW) + "px");
			//
			// $(".resizeMarker,.resizeProxy", $grid).hide();
			// }
			// }));
			// });
			// } else {
			// $th.css("cursor", $th.attr("orderField") ? "pointer" :
			// "default");
			// $th.unbind("mousedown");
			// }
			// return false;
			// });
			// });
			// });

			function _resizeGrid() {
				$("div.j-resizeGrid").each(function() {
					var width = $(this).innerWidth();
					if (width) {
						$("div.gridScroller", this).width(width + "px");
					}
				});
			}
			$(window).unbind("resizeGrid").bind("resizeGrid", _resizeGrid);
		});
	};

})(jQuery);

// JQuery Tab Plugin
// Name:    KandyTabs
// Author:  kandytang[at]msn.com
// Home:    www.jgpy.cn
// Pubdate: 2011-1-27
// Version: 3.0.0715
// LastModify: 2011-7-15 23:56:57
// Demo: http://demo.jgpy.cn/demo/kandyTabs/

;
(function($) {
	$.fn.KandyTabs = function(options) {
		var defaults = {
			classes : "kandyTabs",
			type : "tab",
			trigger : "mouseover",
			action : "toggle",
			custom : {},
			delay : 200,
			last : 400,
			current : 1,
			direct : "top",
			except : "",
			child : [],
			ready : {},
			done : {},
			auto : false,
			stall : 5000,
			play : true,
			process : false,
			ctrl : false,
			nav : false,
			loop : false,
			column : 0,
			lang : {
				first : [ "已是首个", "<" ],
				prev : [ "前一个", "<" ],
				next : [ "后一个", ">" ],
				last : [ "已是末个", ">" ],
				empty : "暂无内容",
				play : [ "播放", ">>" ],
				pause : [ "暂停", "" ]
			}
		};
		var options = $.extend(defaults, options);
		if (typeof options.ready == "function")
			options.ready();
		this
				.each(function() {
					var _current = options.current - 1, _except = options.except, _child = options.child[0], _childOptions = options.child[1], _stall = options.stall, _last = options.last, _col = options.column, $this = $(this), $tab, $title, $body, $btn, $cont, $roll, $process, $except, $tmpbtn, $tmpcont, $child = $this
							.children(), _childlen = _all = $child.length, i, _process = false, _tagname = this.tagName, _tag = "div";

					if (options.process && options.auto)
						_process = true;

					if ($this.hasClass(options.classes))
						return false;

					if (_tagname == "DL") {
						$title = $("<dt/>");
						$body = $("<dd/>");
						_tag = "dd";
					} else if (_tagname == "UL" || _tagname == "OL") {
						$title = $("<li/>");
						$body = $("<li/>");
						_tag = "li";
					} else {
						$title = $("<div/>");
						$body = $("<div/>");
					}
					$tab = $(this).addClass(options.classes);
					$tab.data("html", this.innerHTML);
					if (options.type != "fold")
						$tab.append($title.addClass("tabtitle"), $body.addClass("tabbody"));
					if (_process)
						$tab.append($process = $('<' + _tag + ' class="tabprocess"/>'));

					if (_except != "") {
						var $except = $(_except, this);
						_childlen = _childlen - $except.length;
						for (i = 0; i < $except.length; i++) {
							var _etagname = $except[i].tagName, _eclass = $except[i].className, _eid = $except[i].id;
							if (_eid != "")
								_eid = " id='" + _eid + "'";
							if (_eclass != "")
								_eclass = " " + _eclass;
							if (_etagname.indexOf("H") > -1 || _eclass.toLowerCase().indexOf("title") > -1) {
								$title.before("<" + _etagname + " class='tabexcept" + _eclass + "'" + _eid + ">" + $except[i].innerHTML + "</" + _etagname + ">");
							} else {
								$tab.append("<" + _etagname + " class='tabexcept" + _eclass + "'" + _eid + ">" + $except[i].innerHTML + "</" + _etagname + ">");
							}
							$except.eq(i).remove();
						}
						$child = $this.children();
					}

					if (_col > 0) {
						_childlen = _childlen / _col;
						for (i = 0; i < _childlen; i++) {
							$child.slice(i * _col, i * _col + _col).wrapAll("<div/>");
						}
						$child = $this.children();
					}

					if (options.type == "slide") {
						for (i = 0; i < _childlen; i++) {
							$tmpcont = $child.eq(i);
							// _tagname = $child[i].tagName;
							if ($child[i].tagName == "A" || $child[i].tagName == "IMG" || $child[i].tagName == "IFRAME") {
								$tmpcont = $tmpcont.wrap('<div class="tabcont"/>').parent();
							} else {
								$tmpcont.addClass("tabcont");
							}
							$title.append('<span class="tabbtn">' + (i + 1) + '</span>');
							$body.append($tmpcont);
						}
						_all = _childlen;
					} else if (options.type == "fold") {
						_childlen = _childlen / 2;
						if (_childlen.toString().indexOf(".") > -1)
							_childlen = parseInt(_childlen) + 1;
						for (i = 0; i < _childlen; i++) {
							$child.eq(i * 2).addClass("tabbtn").next().addClass("tabcont");
						}
						if ($(".tabbtn", $tab).length > $(".tabcont", $tab).length)
							$tab.append('<div class="tabcont">' + options.lang.empty + '</div>');
					} else {
						_childlen = _childlen / 2;
						if (_childlen.toString().indexOf(".") > -1)
							_childlen = parseInt(_childlen) + 1;
						for (i = 0; i < _childlen; i++) {
							$tmpbtn = $child.eq(i * 2);
							// 如果是连接或图片
							if ($child[(i * 2)].tagName == "A" || $child[(i * 2)].tagName == "IMG") {
								$tmpbtn = $tmpbtn.wrap('<span class="tabbtn"/>').parent();
							} else {
								$tmpbtn = $('<span class="tabbtn">' + $child[(i * 2)].innerHTML + '</span>');
								if (options.type != "fold")
									$child.eq(i * 2).remove()
							}
							$tmpcont = $child.eq(i * 2 + 1);
							// 如果是连接或图片
							if ($child[(i * 2 + 1)]) {
								if ($child[(i * 2 + 1)].tagName == "A" || $child[(i * 2 + 1)].tagName == "IMG" || $child[(i * 2 + 1)].tagName == "UL" || $child[(i * 2 + 1)].tagName == "OL"
										|| $child[(i * 2 + 1)].tagName == "DL" || $child[(i * 2 + 1)].tagName == "IFRAME") {
									$tmpcont = $tmpcont.wrap('<div class="tabcont"/>').parent();
								} else {
									$tmpcont.addClass("tabcont");
								}
							} else {
								$tmpcont = $('<div class="tabcont">' + options.lang.empty + '</div>');
							}
							$title.append($tmpbtn);
							$body.append($tmpcont);
						}
						_all = Math.round(_childlen);
					}

					if (_tagname == "UL" || _tagname == "OL" || _tagname == "DL")
						$body.wrapInner("<" + _tagname + "/>");

					$btn = $(".tabbtn", $tab);
					$cont = $(".tabcont", $tab).hide();
					if (options.type != "fold") {
						$btn.eq(_current).addClass("tabcur");
						$cont.eq(_current).show();
					} else {
						if (options.direct == "left") {
							var _foldwidth = $cont.width();
							// alert(_foldwidth)
							$tab.css({
								overflow : "hidden",
								height : $cont.outerHeight()
							});
							$cont.css({
								overflow : "auto",
								float : "left"
							}).show();
							$btn.css({
								overflow : "auto",
								float : "left"
							}).eq(_current).addClass("tabcur").next(".tabcont").width(_foldwidth).siblings(".tabcont:visible").width(0).addClass("tabfold");
						} else {
							$cont.hide();
							$btn.eq(_current).addClass("tabcur").next(".tabcont").show();
						}
						if (options.action == "roll") /* alert("fold不支持"+options.action+"的切换效果，已转为slide效果"), */
							options.action = "slide";
						if (options.action == "slifade") /* alert("fold不支持"+options.action+"的切换效果，已转为fade效果"), */
							options.action = "fade";
					}

					var _autoSize = function(index, speed) {
						$body.stop(false, true).animate({
							height : $cont.eq(index).height(),
							width : $cont.eq(index).width()
						}, speed);
					};

					var _rolls = function(index) {
						switch (options.direct) {
						case "left":
							_col == 0 ? $cont.css("float", "left").width($body.width()) : $cont.css("float", "left");
							// if (_col>0)
							// alert($roll.width()-$cont.eq(index).position().left+"
							// "+$body.width())
							if (_col > 0 && $roll.width() - $cont.eq(index).position().left < $body.width()) {
								index = 0;
								$btn.removeClass("tabcur").eq(index).addClass("tabcur");
							}
							$roll.stop(false, true).animate({
								left : -$cont.eq(index).position().left
							}, _last, _autoSize(index, _last * 2));
							break;
						default:
							$roll.stop(false, true).animate({
								top : -$cont.eq(index).position().top
							}, _last, _autoSize(index, _last * 2));
						}
					};
					var _slifade = function(index) {
						// $cont.fadeOut();
						$cont.eq(index).stop(false, true).fadeIn(0, function() {
							$(this).siblings().fadeOut(_last, _autoSize(index, _last * 2)).css("z-index", _all);
						}).css("z-index", 0);
					};
					// action
					if (options.action == "roll") {
						$body.css({
							position : "relative",
							overflow : "hidden"
						}).height($cont.eq(_current).height()).width($cont.eq(_current).width());
						_tagname == "UL" || _tagname == "OL" || _tagname == "DL" ? $body.children().css({
							position : "absolute",
							width : $body.width()
						}).addClass("tabroll") : $body.wrapInner("<div class='tabroll' style='position:absolute;width:" + $body.width() + "px'/>");
						$cont.show();
						var _rollWidth = 0;
						for (i = 0; i < $cont.length; i++) {
							_rollWidth += $cont.eq(i).outerWidth(true);
						}
						$roll = $(".tabroll", $body);
						if (options.direct == "left")
							$roll.width(_rollWidth + $cont.width());
						// if (_col>0)
						// alert(Math.round($roll.width()/$body.width()))
						if (_col > 0) {
							var _average = $roll.width() / $body.width();
							// if
							// (!_average.toString().match(/^[1-9]+[0-9]*]*$/)&&!($body.width()/_col).toString().match(/^[1-9]+[0-9]*]*$/))
							// alert($this.attr("id")+"的tabbody宽度必须为tabcont宽度（"+$cont.width()+"）的整倍数，否则无法正常显示")
							$btn.slice($btn.length - Math.round(_average) + 1, $btn.length).remove();
							$btn = $(".tabbtn", $title);
							// _current--
						}
						setTimeout(function() {
							_rolls(_current);
						}, 100);
					}
					;
					if (options.action == "slifade") {
						$body.css({
							position : "relative",
							overflow : "hidden"
						}).height($cont.eq(_current).height());
						$cont.css({
							position : "absolute",
							width : $body.width()
						});
						setTimeout(function() {
							_slifade(_current);
						}, 100);
					}
					;
					var _actions = function(index) {
						$btn.eq(index).stop(false, true).addClass("tabcur").siblings(".tabbtn").removeClass("tabcur");
						if (_process && _isProcess)
							$process.stop().width("").animate({
								width : 0
							}, _stall, function() {
								_isProcess = false
							});
						switch (options.action) {
						case "fade":
							$cont.eq(index).stop(false, true).fadeIn(_last).siblings(".tabcont").hide();
							break;
						case "slide":
							if (options.direct == "left") {
								$cont.eq(index).stop(false, true).animate({
									width : _foldwidth
								}, _last, function() {
									$(this).removeClass("tabfold");
								}).siblings(".tabcont").animate({
									width : 0
								}, _last, function() {
									$(this).addClass("tabfold");
								})
							} else {
								$cont.eq(index).stop(false, true).slideDown(_last).siblings(".tabcont").slideUp(_last);
							}
							break;
						case "roll":
							_rolls(index);
							break;
						case "slifade":
							_slifade(index);
							break;
						default:
							$cont.eq(index).stop(false, true).show().siblings(".tabcont").hide();
						}
						if (typeof options.custom == "function")
							options.custom($btn, $cont, index, $this);
						if ($prev)
							index == 0 ? $prev.addClass("tabprevno").attr("title", options.lang.first[0]) : $prev.removeClass("tabprevno").attr("title", options.lang.prev[0]);
						if ($next)
							index == $btn.length - 1 ? $next.addClass("tabnextno").attr("title", options.lang.last[0]) : $next.removeClass("tabnextno").attr("title", options.lang.next[0]);
						if ($now)
							$now.text(index + 1);
						if (options.loop)
							$prev.removeClass("tabprevno").attr("title", options.lang.prev[0]), $next.removeClass("tabnextno").attr("title", options.lang.next[0]);
					};

					// auto
					var $ctrl, $pause, $play, $nav, $prev, $next, $now, $tabprev, $tabnext, $autostop, _auto = null, setAuto = null, _isAuto = true, _isProcess = true;

					_auto = function() {
						setAuto && clearTimeout(setAuto);
						setAuto = null;
						window.CollectGarbage && CollectGarbage();
						if (options.process)
							_isProcess = true;
						options.type != "fold" ? $tabnext = $(".tabcur", $title).next() : $tabnext = $(".tabcur", $this).next().next();
						$tabnext.html() == null ? $btn.first().trigger(options.trigger) : $tabnext.trigger(options.trigger);
						if (options.trigger == "mouseover")
							if (options.process)
								$process.stop().width("").animate({
									width : 0
								}, _stall);
						// alert($tabnext)
						setAuto = setTimeout(_auto, _stall);
					};

					if (options.auto) {
						if (options.process)
							$process.animate({
								width : 0
							}, _stall), _isProcess = true;
						setTimeout(_auto, _stall);

						options.type != "fold" ? $autostop = $(".tabtitle,.tabcont", $tab) : $autostop = $this;

						$autostop.mouseover(function() {
							if (options.process)
								$process.stop().width(""), _isProcess = false;
							clearTimeout(setAuto);
						}).mouseout(function() {
							if (options.process)
								$process.stop().width("").animate({
									width : 0
								}, _stall), _isProcess = true;
							if (_isAuto)
								setAuto = setTimeout(_auto, _stall);
						});

						if (options.ctrl) {
							$tab.append($ctrl = $('<' + _tag + ' class="tabctrl"/>'));
							$ctrl.append($pause = $('<b class="tabpause" title="' + options.lang.pause[0] + '">' + options.lang.pause[1] + '</b>'), $play = $('<b class="tabplay" title="'
									+ options.lang.play[0] + '" style="display:none">' + options.lang.play[1] + '</b>'));

							$pause.click(function() {
								$(this).hide();
								if (options.process)
									$process.stop().hide();
								clearTimeout(setAuto);
								$play.show();
								_isAuto = false;
							});
							$play.click(function() {
								$(this).hide();
								if (options.process)
									$process.show().stop().width("").animate({
										width : 0
									}, _stall);
								setAuto = setTimeout(_auto, _stall);
								$pause.show();
								_isAuto = true;
							});

							if (!options.play) {
								$pause.trigger("click");
							}
						}

					}

					if (options.nav) {
						_all = _all.toString();
						if (_all.indexOf(".") > -1)
							_all = parseInt(_all) + 1;
						$tab.append($nav = $('<' + _tag + ' class="tabnav"/>'));
						$nav.append($prev = $('<em class="tabprev" title="' + options.lang.prev[0] + '">' + options.lang.prev[1] + '</em>'), '<span class="tabpage"/>',
								$next = $('<em class="tabnext" title="' + options.lang.next[0] + '">' + options.lang.next[1] + '</em>'));
						$("span.tabpage", $nav).append($now = $('<b class="tabnow">' + (_current + 1) + '</b>'), '<i>&nbsp;/&nbsp;</i>', '<b class="taball">' + _all + '</b>');

						if (_current == 0 && !options.loop)
							$prev.addClass("tabprevno");

						$prev.mouseover(function() {
							if (_process)
								$process.stop().width(""), _isProcess = false;
							if (options.auto)
								clearTimeout(setAuto);
						}).mousedown(function() {
							if ($(this).hasClass("tabprevno"))
								return false;
							$tabprev = $(".tabcur", $title).prev();
							$tabprev.html() == null ? $btn.last().trigger(options.trigger) : $tabprev.trigger(options.trigger);
							if (options.auto)
								setAuto = setTimeout(_auto, _stall);
						}).mouseup(function() {
							if (options.auto)
								clearTimeout(setAuto);
						}).mouseout(function() {
							if (_process)
								$process.animate({
									width : 0
								}, _stall), _isProcess = true;
							if (options.auto)
								setAuto = setTimeout(_auto, _stall);
						});
						$next.mouseover(function() {
							if (_process)
								$process.stop().width(""), _isProcess = false;
							if (options.auto)
								clearTimeout(setAuto);
						}).mousedown(function() {
							if ($(this).hasClass("tabnextno"))
								return false;
							$tabnext = $(".tabcur", $title).next();
							$tabnext.html() == null ? $btn.first().trigger(options.trigger) : $tabnext.trigger(options.trigger);
							if (options.auto)
								setAuto = setTimeout(_auto, _stall);
						}).mouseup(function() {
							if (options.auto)
								clearTimeout(setAuto);
						}).mouseout(function() {
							if (_process)
								$process.animate({
									width : 0
								}, _stall), _isProcess = true;
							if (options.auto)
								setAuto = setTimeout(_auto, _stall);
						});
					}

					var _delay = null;
					if (options.trigger != "mouseover")
						options.delay = 0;
					$btn.bind(options.trigger, function() {
						var _index = $btn.index($(this));
						clearTimeout(_delay);
						_delay = setTimeout(function() {
							_actions(_index);
						}, options.delay);
					});
					if (options.trigger == "mouseover") {
						$btn.mouseout(function() {
							clearTimeout(_delay);
						});
					}
					if (options.type == "fold") {
						$btn.hover(function() {
							$(this).addClass("tabon");
						}, function() {
							$(this).removeClass("tabon");
						});
					}

					if (options.child != "" && $tab.find(_child).length) {
						$(_child).KandyTabs(_childOptions);
					}
					if (typeof options.done == "function")
						options.done($btn, $cont, $this);
				});

	}
	// $(function(){$(".kandyTabs").KandyTabs()});
})(jQuery);
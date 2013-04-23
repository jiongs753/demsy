var pngfix = function() {
	var els = document.getElementsByTagName("img"), ip = /\.png/i, al = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='", i = els.length, uels = new Array(), c = 0;
	while (i-- > 0) {
		if (els[i].className.match(/unitPng/)) {
			uels[c] = els[i];
			c++;
		}
	}
	if (uels.length == 0) {
		pfx(els);
	} else {
		pfx(uels);
	}
	function pfx(els) {
		i = els.length;
		while (i-- > 0) {
			var el = els[i], es = el.style, elc = el.currentStyle, elb = elc.backgroundImage;
			if (el.src && el.src.match(ip) && !es.filter) {
				es.height = el.height;
				es.width = el.width;
				es.filter = al + el.src + "',sizingMethod='crop')";
				el.src = "/themes2/images/_blank.gif";
			} else {
				if (elb.match(ip)) {
					var path = elb.split("\""), rep = (elc.backgroundRepeat == "no-repeat") ? "crop" : "scale", elkids = el.getElementsByTagName("*"), j = elkids.length;
					es.filter = al + path[1] + "',sizingMethod='" + rep + "')";
					es.height = el.clientHeight + "px";
					es.backgroundImage = "none";
					if (j != 0) {
						if (elc.position != "absolute") {
							es.position = "static";
						}
						while (j-- > 0) {
							if (!elkids[j].style.position) {
								elkids[j].style.position = "relative";
							}
						}
					}
				}
			}
		}
	}
};
/**
 * 
 * //JQuery Tab Plugin //Name: KandyTabs //Author: kandytang[at]msn.com //Home:
 * www.jgpy.cn //Pubdate: 2011-1-27 //Version: 3.0.0715 //LastModify: 2011-7-15
 * 23:56:57 //Demo: http://demo.jgpy.cn/demo/kandyTabs/
 */
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
					// $tab.data("html", this.innerHTML);
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
							$body.append($tmpcont).css({
								"z-index" : 2
							});
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
						}, 5000);
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
	$.fn.marquee = function(options) {
		var speed = 500;

		this.each(function() {
			var $this = $(this);
			var $this1 = $(".marquee1", $this);
			var $this2 = $(".marquee2", $this);
			var self = this;
			var this1 = $this1.get(0);
			var this2 = $this2.get(0);
			$this2.html($this1.html());

			var direction = $this.attr("direction");

			var marquee = function() {
				if (direction == "left") {
					if (this2.offsetWidth - self.scrollLeft <= 0) {
						self.scrollLeft -= this1.offsetWidth;
					} else {
						self.scrollLeft += 50;
					}
				} else {
					if (this2.offsetTop - self.scrollTop <= 0) {
						self.scrollTop -= this1.offsetHeight;
					} else {
						self.scrollTop += 5;
					}
				}
			}
			var scroll = setInterval(marquee, speed);
			$this.mouseover(function() {
				clearInterval(scroll);
			});
			$this.mouseout(function() {
				scroll = setInterval(marquee, speed);
			});
		});

		return this;
	};
})
/**
 * Modal
 */
(jQuery);
(function($) {
	$.fn.extend({
		demsyModal : function(opt) {
			return this.each(function() {
				var o = opt;
				var href = $(this).attr("href");
				if (href) {
					$(this).click(function(e) {
						var href = $(this).attr("href");
						var dialog = $(href);
						show(dialog, o);
						e.preventDefault();
					});
				} else {
					show($(this), o);
				}
			});
			function show(dialog, o) {
				if ($(".close", dialog).length == 0) {
					var close = $("<div class='close'><a><span style='font-size:16px'>×</span>关闭</a></div>").css({
						position : "absolute",
						right : "2px",
						top : "-2px"
					});
					dialog.append(close);
				}

				dialog.css({
					display : "block",
					zIndex : 10001
				});
				dialog.fadeTo(200, 1);
				$(".close", dialog).click(function() {
					dialog.hide();
				});
			}
		}
	});
})(jQuery);

/**
 * FLASH
 */
(function() {
	var $$;
	$$ = jQuery.fn.flash = function(htmlOptions, pluginOptions, replace, update) {
		var block = replace || $$.replace;
		pluginOptions = $$.copy($$.pluginOptions, pluginOptions);
		if (!$$.hasFlash(pluginOptions.version)) {
			if (pluginOptions.expressInstall && $$.hasFlash(6, 0, 65)) {
				var expressInstallOptions = {
					flashvars : {
						MMredirectURL : location,
						MMplayerType : 'PlugIn',
						MMdoctitle : jQuery('title').text()
					}
				};
			} else if (pluginOptions.update) {
				block = update || $$.update;
			} else {
				return this;
			}
		}

		htmlOptions = $$.copy($$.htmlOptions, expressInstallOptions, htmlOptions);
		return this.each(function() {
			block.call(this, $$.copy(htmlOptions));
		});

	};
	$$.copy = function() {
		var options = {}, flashvars = {};
		for ( var i = 0; i < arguments.length; i++) {
			var arg = arguments[i];
			if (arg == undefined)
				continue;
			jQuery.extend(options, arg);
			if (arg.flashvars == undefined)
				continue;
			jQuery.extend(flashvars, arg.flashvars);
		}
		options.flashvars = flashvars;
		return options;
	};
	$$.hasFlash = function() {
		if (/hasFlash\=true/.test(location))
			return true;
		if (/hasFlash\=false/.test(location))
			return false;
		var pv = $$.hasFlash.playerVersion().match(/\d+/g);
		var rv = String([ arguments[0], arguments[1], arguments[2] ]).match(/\d+/g) || String($$.pluginOptions.version).match(/\d+/g);
		for ( var i = 0; i < 3; i++) {
			pv[i] = parseInt(pv[i] || 0);
			rv[i] = parseInt(rv[i] || 0);
			if (pv[i] < rv[i])
				return false;
			if (pv[i] > rv[i])
				return true;
		}
		return true;
	};
	$$.hasFlash.playerVersion = function() {
		try {
			try {
				var axo = new ActiveXObject('ShockwaveFlash.ShockwaveFlash.6');
				try {
					axo.AllowScriptAccess = 'always';
				} catch (e) {
					return '6,0,0';
				}
			} catch (e) {
			}
			return new ActiveXObject('ShockwaveFlash.ShockwaveFlash').GetVariable('$version').replace(/\D+/g, ',').match(/^,?(.+),?$/)[1];
		} catch (e) {
			try {
				if (navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin) {
					return (navigator.plugins["Shockwave Flash 2.0"] || navigator.plugins["Shockwave Flash"]).description.replace(/\D+/g, ",").match(/^,?(.+),?$/)[1];
				}
			} catch (e) {
			}
		}
		return '0,0,0';
	};
	$$.htmlOptions = {
		height : 240,
		flashvars : {},
		pluginspage : 'http://www.adobe.com/go/getflashplayer',
		wmode : "transparent",
		src : '#',
		type : 'application/x-shockwave-flash',
		width : 320
	};
	$$.pluginOptions = {
		expressInstall : false,
		update : true,
		version : '6.0.65'
	};
	$$.replace = function(htmlOptions) {
		this.innerHTML = '<div class="alt">' + this.innerHTML + '</div>';
		jQuery(this).addClass('flash-replaced').prepend($$.transform(htmlOptions));
	};
	$$.update = function(htmlOptions) {
		var url = String(location).split('?');
		url.splice(1, 0, '?hasFlash=true&');
		url = url.join('');
		var msg = '<p>This content requires the Flash Player. <a href="http://www.adobe.com/go/getflashplayer">Download Flash Player</a>. Already have Flash Player? <a href="' + url
				+ '">Click here.</a></p>';
		this.innerHTML = '<span class="alt">' + this.innerHTML + '</span>';
		jQuery(this).addClass('flash-update').prepend(msg);
	};
	function toAttributeString() {
		var s = '';
		for ( var key in this)
			if (typeof this[key] != 'function')
				s += key + '="' + this[key] + '" ';
		return s;
	}
	;
	function toFlashvarsString() {
		var s = '';
		for ( var key in this)
			if (typeof this[key] != 'function')
				s += key + '=' + encodeURIComponent(this[key]) + '&';
		return s.replace(/&$/, '');
	}
	;
	$$.transform = function(htmlOptions) {
		htmlOptions.toString = toAttributeString;
		if (htmlOptions.flashvars)
			htmlOptions.flashvars.toString = toFlashvarsString;
		return '<embed ' + String(htmlOptions) + '/>';
	};
	if (window.attachEvent) {
		window.attachEvent("onbeforeunload", function() {
			__flash_unloadHandler = function() {
			};
			__flash_savedUnloadHandler = function() {
			};
		});
	}

})();
/*
 * ! jQuery blockUI plugin Version 2.39 (23-MAY-2011) @requires jQuery v1.2.3 or
 * later
 * 
 * Examples at: http://malsup.com/jquery/block/ Copyright (c) 2007-2010 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Thanks to Amir-Hossein Sobhi for some excellent contributions!
 */
(function($) {
	$.fn._fadeIn = $.fn.fadeIn;

	var noOp = function() {
	};

	var mode = document.documentMode || 0;
	var setExpr = $.browser.msie && (($.browser.version < 8 && !mode) || mode < 8);
	var ie6 = $.browser.msie && /MSIE 6.0/.test(navigator.userAgent) && !mode;

	$.blockUI = function(opts) {
		install(window, opts);
	};
	$.unblockUI = function(opts) {
		remove(window, opts);
	};

	$.growlUI = function(title, message, timeout, onClose) {
		var $m = $('<div class="growlUI"></div>');
		if (title)
			$m.append('<h1>' + title + '</h1>');
		if (message)
			$m.append('<h2>' + message + '</h2>');
		if (timeout == undefined)
			timeout = 3000;
		$.blockUI({
			message : $m,
			fadeIn : 700,
			fadeOut : 1000,
			centerY : false,
			timeout : timeout,
			showOverlay : false,
			onUnblock : onClose,
			css : $.blockUI.defaults.growlCSS
		});
	};

	$.fn.block = function(opts) {
		return this.unblock({
			fadeOut : 0
		}).each(function() {
			if ($.css(this, 'position') == 'static')
				this.style.position = 'relative';
			if ($.browser.msie)
				this.style.zoom = 1;
			install(this, opts);
		}).ajaxComplete(function() {
			$(this).unblock();
		}).ajaxError(function(event, xhr, settings) {
			alert(xhr.statusText + ": " + xhr.status);
		});
	};

	$.fn.unblock = function(opts) {
		return this.each(function() {
			remove(this, opts);
		});
	};

	$.blockUI.version = 2.39;

	$.blockUI.defaults = {
		message : '<img src="/themes2/images/busy.gif" />',

		title : null,
		draggable : true,

		theme : false,

		css : {
			padding : '20px',
			margin : 0,
			width : '50px',
			height : '20px',
			top : '20%',
			left : '40%',
			textAlign : 'center',
			color : 'transparent',
			border : '0px solid #aaa',
			backgroundColor : 'transparent',
			cursor : 'wait',
			fontSize : '12px',
			cursor : 'default'
		},

		themedCSS : {
			width : '30%',
			top : '40%',
			left : '35%'
		},

		overlayCSS : {
			backgroundColor : '#000',
			opacity : 0,
			cursor : 'wait'
		},

		growlCSS : {
			width : '350px',
			top : '10px',
			left : '',
			right : '10px',
			border : 'none',
			padding : '5px',
			opacity : 0.6,
			cursor : 'default',
			color : '#fff',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			'border-radius' : '10px'
		},

		iframeSrc : /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank',

		forceIframe : false,

		baseZ : 1000,

		centerX : true,
		centerY : true,

		allowBodyStretch : true,

		bindEvents : true,

		constrainTabKey : true,

		fadeIn : 0,

		fadeOut : 0,

		timeout : 0,

		showOverlay : true,

		focusInput : true,

		applyPlatformOpacityRules : true,

		onBlock : null,

		onUnblock : null,

		quirksmodeOffsetHack : 4,

		blockMsgClass : 'blockMsg'
	};

	var pageBlock = null;
	var pageBlockEls = [];

	function install(el, opts) {
		var full = (el == window);
		var msg = opts && opts.message !== undefined ? opts.message : undefined;
		opts = $.extend({}, $.blockUI.defaults, opts || {});
		opts.overlayCSS = $.extend({}, $.blockUI.defaults.overlayCSS, opts.overlayCSS || {});
		var css = $.extend({}, $.blockUI.defaults.css, opts.css || {});
		var themedCSS = $.extend({}, $.blockUI.defaults.themedCSS, opts.themedCSS || {});
		msg = msg === undefined ? opts.message : msg;

		if (full && pageBlock)
			remove(window, {
				fadeOut : 0
			});

		if (msg && typeof msg != 'string' && (msg.parentNode || msg.jquery)) {
			var node = msg.jquery ? msg[0] : msg;
			var data = {};
			$(el).data('blockUI.history', data);
			data.el = node;
			data.parent = node.parentNode;
			data.display = node.style.display;
			data.position = node.style.position;
			if (data.parent)
				data.parent.removeChild(node);
		}

		$(el).data('blockUI.onUnblock', opts.onUnblock);
		var z = opts.baseZ;

		var lyr1 = ($.browser.msie || opts.forceIframe) ? $('<iframe class="blockUI" style="z-index:' + (z++)
				+ ';display:none;border:none;margin:0;padding:0;position:absolute;width:100%;height:100%;top:0;left:0" src="' + opts.iframeSrc + '"></iframe>')
				: $('<div class="blockUI" style="display:none"></div>');

		var lyr2 = opts.theme ? $('<div class="blockUI blockOverlay ui-widget-overlay" style="z-index:' + (z++) + ';display:none"></div>') : $('<div class="blockUI blockOverlay" style="z-index:'
				+ (z++) + ';display:none;border:none;margin:0;padding:0;width:100%;height:100%;top:0;left:0"></div>');

		var lyr3, s;
		if (opts.theme && full) {
			s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage ui-dialog ui-widget ui-corner-all" style="z-index:' + (z + 10) + ';display:none;position:fixed">'
					+ '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">' + (opts.title || '&nbsp;') + '</div>' + '<div class="ui-widget-content ui-dialog-content"></div>'
					+ '</div>';
		} else if (opts.theme) {
			s = '<div class="blockUI ' + opts.blockMsgClass + ' blockElement ui-dialog ui-widget ui-corner-all" style="z-index:' + (z + 10) + ';display:none;position:absolute">'
					+ '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">' + (opts.title || '&nbsp;') + '</div>' + '<div class="ui-widget-content ui-dialog-content"></div>'
					+ '</div>';
		} else if (full) {
			s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage" style="z-index:' + (z + 10) + ';display:none;position:fixed"></div>';
		} else {
			s = '<div class="blockUI '
					+ opts.blockMsgClass
					+ ' blockElement" style="z-index:'
					+ (z + 10)
					+ ';display:none;position:absolute"><div class="msg"></div><div class="cancel" style="margin: 5px 0 5px 0;display:none;"><a class="cancel" href="javascript:void(0);">取消</a></div></div>';
		}
		lyr3 = $(s);

		if (msg) {
			if (opts.theme) {
				lyr3.css(themedCSS);
				lyr3.addClass('ui-widget-content');
			} else
				lyr3.css(css);
		}

		if (!opts.theme && (!opts.applyPlatformOpacityRules || !($.browser.mozilla && /Linux/.test(navigator.platform))))
			lyr2.css(opts.overlayCSS);
		lyr2.css('position', full ? 'fixed' : 'absolute');

		if ($.browser.msie || opts.forceIframe)
			lyr1.css('opacity', 0.0);

		var layers = [ lyr1, lyr2, lyr3 ], $par = full ? $('body') : $(el);
		$.each(layers, function() {
			this.appendTo($par);
		});

		if (opts.theme && opts.draggable && $.fn.draggable) {
			lyr3.draggable({
				handle : '.ui-dialog-titlebar',
				cancel : 'li'
			});
		}

		var expr = setExpr && (!$.boxModel || $('object,embed', full ? null : el).length > 0);
		if (ie6 || expr) {
			if (full && opts.allowBodyStretch && $.boxModel)
				$('html,body').css('height', '100%');

			if ((ie6 || !$.boxModel) && !full) {
				var t = sz(el, 'borderTopWidth'), l = sz(el, 'borderLeftWidth');
				var fixT = t ? '(0 - ' + t + ')' : 0;
				var fixL = l ? '(0 - ' + l + ')' : 0;
			}

			$
					.each(
							[ lyr1, lyr2, lyr3 ],
							function(i, o) {
								var s = o[0].style;
								s.position = 'absolute';
								if (i < 2) {
									full ? s.setExpression('height', 'Math.max(document.body.scrollHeight, document.body.offsetHeight) - (jQuery.boxModel?0:' + opts.quirksmodeOffsetHack + ') + "px"')
											: s.setExpression('height', 'this.parentNode.offsetHeight + "px"');
									full ? s.setExpression('width', 'jQuery.boxModel && document.documentElement.clientWidth || document.body.clientWidth + "px"') : s.setExpression('width',
											'this.parentNode.offsetWidth + "px"');
									if (fixL)
										s.setExpression('left', fixL);
									if (fixT)
										s.setExpression('top', fixT);
								} else if (opts.centerY) {
									if (full)
										s
												.setExpression(
														'top',
														'(document.documentElement.clientHeight || document.body.clientHeight) / 2 - (this.offsetHeight / 2) + (blah = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "px"');
									s.marginTop = 0;
								} else if (!opts.centerY && full) {
									var top = (opts.css && opts.css.top) ? parseInt(opts.css.top) : 0;
									var expression = '((document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + ' + top + ') + "px"';
									s.setExpression('top', expression);
								}
							});
		}

		if (msg) {
			if (opts.theme)
				lyr3.find('.ui-widget-content').append(msg);
			else
				lyr3.find('.msg').append(msg);
			if (msg.jquery || msg.nodeType)
				$(msg).show();

			lyr3.find('a.cancel').click(function() {
				if (el.$ajax)
					el.$ajax.abort();
			});
		}

		if (($.browser.msie || opts.forceIframe) && opts.showOverlay)
			lyr1.show();
		if (opts.fadeIn) {
			var cb = opts.onBlock ? opts.onBlock : noOp;
			var cb1 = (opts.showOverlay && !msg) ? cb : noOp;
			var cb2 = msg ? cb : noOp;
			if (opts.showOverlay)
				lyr2._fadeIn(opts.fadeIn, cb1);
			if (msg)
				lyr3._fadeIn(opts.fadeIn, cb2);
		} else {
			if (opts.showOverlay)
				lyr2.show();
			if (msg)
				lyr3.show();
			if (opts.onBlock)
				opts.onBlock();
		}

		bind(1, el, opts);

		if (full) {
			pageBlock = lyr3[0];
			pageBlockEls = $(':input:enabled:visible', pageBlock);
			if (opts.focusInput)
				setTimeout(focus, 20);
		} else
			center(lyr3[0], opts.centerX, opts.centerY);

		if (opts.timeout) {
			var to = setTimeout(function() {
				full ? $.unblockUI(opts) : $(el).unblock(opts);
			}, opts.timeout);
			$(el).data('blockUI.timeout', to);
		}
	}
	;

	function remove(el, opts) {
		var full = (el == window);
		var $el = $(el);
		var data = $el.data('blockUI.history');
		var to = $el.data('blockUI.timeout');
		if (to) {
			clearTimeout(to);
			$el.removeData('blockUI.timeout');
		}
		opts = $.extend({}, $.blockUI.defaults, opts || {});
		bind(0, el, opts);

		if (opts.onUnblock === null) {
			opts.onUnblock = $el.data('blockUI.onUnblock');
			$el.removeData('blockUI.onUnblock');
		}

		var els;
		if (full)
			els = $('body').children().filter('.blockUI').add('body > .blockUI');
		else
			els = $('.blockUI', el);

		if (full)
			pageBlock = pageBlockEls = null;

		if (opts.fadeOut) {
			els.fadeOut(opts.fadeOut);
			setTimeout(function() {
				reset(els, data, opts, el);
			}, opts.fadeOut);
		} else
			reset(els, data, opts, el);
	}
	;

	function reset(els, data, opts, el) {
		els.each(function(i, o) {
			if (this.parentNode)
				this.parentNode.removeChild(this);
		});

		if (data && data.el) {
			data.el.style.display = data.display;
			data.el.style.position = data.position;
			if (data.parent)
				data.parent.appendChild(data.el);
			$(el).removeData('blockUI.history');
		}

		if (typeof opts.onUnblock == 'function')
			opts.onUnblock(el, opts);
	}
	;

	function bind(b, el, opts) {
		var full = el == window, $el = $(el);

		if (!b && (full && !pageBlock || !full && !$el.data('blockUI.isBlocked')))
			return;
		if (!full)
			$el.data('blockUI.isBlocked', b);

		if (!opts.bindEvents || (b && !opts.showOverlay))
			return;

		var events = 'mousedown mouseup keydown keypress';
		b ? $(document).bind(events, opts, handler) : $(document).unbind(events, handler);

	}
	;

	function handler(e) {
		if (e.keyCode && e.keyCode == 9) {
			if (pageBlock && e.data.constrainTabKey) {
				var els = pageBlockEls;
				var fwd = !e.shiftKey && e.target === els[els.length - 1];
				var back = e.shiftKey && e.target === els[0];
				if (fwd || back) {
					setTimeout(function() {
						focus(back)
					}, 10);
					return false;
				}
			}
		}
		var opts = e.data;
		if ($(e.target).parents('div.' + opts.blockMsgClass).length > 0)
			return true;

		return $(e.target).parents().children().filter('div.blockUI').length == 0;
	}
	;

	function focus(back) {
		if (!pageBlockEls)
			return;
		var e = pageBlockEls[back === true ? pageBlockEls.length - 1 : 0];
		if (e)
			e.focus();
	}
	;

	function center(el, x, y) {
		var p = el.parentNode, s = el.style;
		var l = ((p.offsetWidth - el.offsetWidth) / 2) - sz(p, 'borderLeftWidth');
		var t = ((p.offsetHeight - el.offsetHeight) / 2) - sz(p, 'borderTopWidth');
		if (x)
			s.left = l > 0 ? (l + 'px') : '0';
		if (y)
			s.top = t > 0 ? (t + 'px') : '0';
	}
	;

	function sz(el, p) {
		return parseInt($.css(el, p)) || 0;
	}
	;

})(jQuery);

/**
 * 
 * @param options
 * @returns
 */
var WebUI = function(options) {
	this.gotoPage = function(block, data, param) {
		var $block = $("#" + block);
		var blockID = $block.attr("dataID");
		$block.block();
		$block.load(options.loadBlockUrl + blockID + "/" + (param || options.dataParam), data);
	};

	this.init = function() {
		// alert("marquee: " + $(".marquee").length + ", tabs: " +
		// $(".tabs").length
		// + ", slide: " + $(".slide").length+", ajaxLoad:
		// "+$(".ajaxLoad").length);
		$(".tabs").KandyTabs();
		$(".slide").KandyTabs({
			classes : "kandySlide",
			action : "slifade",
			stall : 5000,
			type : "slide",
			auto : true,
			// process : true,
			direct : "left",
			custom : function(b, c) {
				//$("p", c).fadeOut();
				//$("p", c).slideDown();
			},
			done : function(b, c, t) {
				//$("p", c).fadeTo(500, .7).hide();
				//c.first().find("p").slideDown();
			}
		});
		$(".marquee").marquee();
		if ($.fn.spinner) {
			$(".spinner").spinner({
				decimals : 0,
				stepping : 1,
				start : 0,
				incremental : true,
				currency : false,
				format : '%',
				editable : false,
				min : 1,
				items : []
			});
		}
		if ($.fn.xheditor) {
			$('.xheditor').each(function() {
				$(this).xheditor({
					tools : 'simple'
				});
			});
		}
		$(".ajaxLoad").each(function() {
			var $this = $(this);
			$this.load(options.loadBlockUrl + $this.attr("dataID") + "/" + options.dataParam);
		});
		try {
			pngfix();
		} catch (e) {
		}
	}
	this.init();
}

QrXPCOM.init = function() {
	if (QrXPCOM.inited)
		return;
	QrXPCOM.documentBodyOnClickOld = document.body.onclick;
	document.body.onclick = function(event) {
		if (QrXPCOM.documentBodyOnClickOld)
			QrXPCOM.documentBodyOnClickOld();
		id = "id5";
		if (QrXPCOM.popupblock == false && QrXPCOM.popup) {
			QrXPCOM.popup.style.display = "none";
		} else {
			QrXPCOM.popupblock = false;
		}
	}
	QrXPCOM.inited = true;
}

QrXPCOM.popupblock;
QrXPCOM.onPopup = function(popup) {
	if (popup) {
		if (QrXPCOM.popup && QrXPCOM.popup != popup)
			QrXPCOM.popup.style.display = "none";
		QrXPCOM.popup = popup;
	}
	QrXPCOM.popupblock = true;
}

QrXPCOM.Enter = 13;
QrXPCOM.LeftArrow = 37;
QrXPCOM.UpArrow = 38;
QrXPCOM.RightArrow = 39;
QrXPCOM.DownArrow = 40;

function QrXPCOM() {
}
function QrPoint(_x, _y) {
	this.x = _x;
	this.y = _y;
}

function QrDimension(_width, _height) {
	this.width = _width;
	this.height = _height;
}

QrXPCOM.isIE = function() {
	return window.ActiveXObject;
}

QrXPCOM.isImageFile = function(src) {
	if (src.substring(src.lastIndexOf(".")).toLowerCase() == ".gif" || src.substring(src.lastIndexOf(".")).toLowerCase() == ".jpg" || src.substring(src.lastIndexOf(".")).toLowerCase() == ".bmp"
			|| src.substring(src.lastIndexOf(".")).toLowerCase() == ".jpeg" || src.substring(src.lastIndexOf(".")).toLowerCase() == ".png") {
		return true;
	} else {
		return false;
	}
}

QrXPCOM.getEventKeyCode = function(e) {
	if (QrXPCOM.isIE()) {
		return event.keyCode;
	} else {
		return e.keyCode;
	}
}

QrXPCOM.onShift = function(e) {
	if (QrXPCOM.isIE())
		return event.shiftKey;
	else {
		return e.shiftKey;
	}
}

QrXPCOM.getMousePointForDrag = function(e) {
	if (QrXPCOM.isIE()) {
		return new QrPoint(event.clientX + document.body.scrollLeft, event.clientY + document.body.scrollTop);
	} else {
		return new QrPoint(e.clientX + document.body.scrollLeft, e.clientY + document.body.scrollTop);
	}
}

QrXPCOM.getMousePoint = function(e, div) {
	if (div) {
		var da = QrXPCOM.getMousePoint(e);
		var db = QrXPCOM.getDivPoint(div);
		return new QrPoint(da.x - db.x, da.y - db.y);
	}

	if (QrXPCOM.isIE()) {
		var p = QrXPCOM.getDivPoint(event.srcElement);
		return new QrPoint(p.x + event.offsetX, p.y + event.offsetY);
	} else {
		return new QrPoint(e.clientX + document.body.scrollLeft, e.clientY + document.body.scrollTop);
	}
}

QrXPCOM.setDivPoint = function(div, x, y) {
	div.style.top = y + "px";
	div.style.left = x + "px";
}

QrXPCOM.getDivPoint = function(div) {
	if (div.style && (div.style.position == "absolute" || div.style.position == "relative")) {
		return new QrPoint(div.offsetLeft + 1, div.offsetTop + 1);
	} else if (div.offsetParent) {
		var d = QrXPCOM.getDivPoint(div.offsetParent);
		return new QrPoint(d.x + div.offsetLeft, d.y + div.offsetTop);
	} else {
		return new QrPoint(0, 0);
	}
}

QrXPCOM.getDivSize = function(div) {
	if (QrXPCOM.isIE()) {
		return new QrDimension(div.offsetWidth, div.offsetHeight);
	} else {
		return new QrDimension(div.offsetWidth - 2, div.offsetHeight - 2);
	}
}

QrXPCOM.setDivSize = function(div, x, y) {
	div.style.width = x + "px";
	div.style.height = y + "px";
}

QrXPCOM.getBodySize = function() {
	return new QrDimension(document.body.clientWidth, document.body.clientHeight);
}

// www.kmetop.com Edit 2012-6-8
function QrPulldown(id) {
	this.id = id;
	QrXPCOM.init();
	QrPulldown.instanceMap[this.id] = this;
}

QrPulldown.prototype.set = function(value) {
	document.getElementById(this.id + "#input").value = value;
	if (QrPulldown.instanceMap[this.id].onChange) {
		QrPulldown.instanceMap[this.id].onChange(value);
	}
}

QrPulldown.prototype.get = function() {
	return document.getElementById(this.id + "#input").value;
}

QrPulldown.prototype.addItem = function(html, value) {
	var thisid = this.itemLastId++;
	var cashhtml = "<DIV class=\"QrPulldownItem\" id=\"$pulldownId#item$itemId\" onmouseover=\"QrPulldown.onHover('$pulldownId', '$pulldownId#item$itemId', '$value');\" onmouseout=\"QrPulldown.onOut('$pulldownId', '$pulldownId#item$itemId');\" onclick=\"QrPulldown.onSelect('$pulldownId','$value');\">$html</DIV>";
	cashhtml = cashhtml.replace(/\$pulldownId/g, this.id).replace(/\$itemId/g, thisid).replace(/\$html/g, html).replace(/\$value/g, value);

	this.itemHtml += cashhtml;
	document.getElementById(this.id + "#menuinner").innerHTML = this.itemHtml;
}

QrPulldown.instanceMap = new Array;

QrPulldown.onOut = function(id, itemid) {
	if (document.getElementById(itemid).className == "QrPulldownItemHover") {
		document.getElementById(itemid).className = "QrPulldownItem";
	}
	if (QrPulldown.instanceMap[id].onChange) {
		QrPulldown.instanceMap[id].onChange(document.getElementById(id + "#input").value);
	}
}

QrPulldown.onHover = function(id, itemid, value) {
	if (document.getElementById(itemid).className == "QrPulldownItem") {
		document.getElementById(itemid).className = "QrPulldownItemHover";
	}
	if (QrPulldown.instanceMap[id].onChange) {
		QrPulldown.instanceMap[id].onChange(value);
	}
}

QrPulldown.onButtonOut = function(id) {
	document.getElementById(id + "#img").src = QrXPCOM.designerPath + "/img/pulldown-normal.gif";
}

QrPulldown.onButtonHover = function(id) {
	document.getElementById(id + "#img").src = QrXPCOM.designerPath + "/img/pulldown-down.gif";
}

QrPulldown.onClick = function(id) {
	var p = QrXPCOM.getDivPoint(document.getElementById(id));
	var r = QrXPCOM.getDivSize(document.getElementById(id));

	document.getElementById(id + "#menu").style.display = "";
	QrXPCOM.onPopup(document.getElementById(id + "#menu"));
}

QrPulldown.onKeyup = function(id) {
	if (QrPulldown.instanceMap[id].onChange) {
		QrPulldown.instanceMap[id].onChange(document.getElementById(id + "#input").value);
	}
}

QrPulldown.onSelect = function(id, value) {
	document.getElementById(id + "#input").value = value;
	document.getElementById(id + "#menu").style.display = "none";

	if (QrPulldown.instanceMap[id].onSelect) {
		QrPulldown.instanceMap[id].onSelect(value);
	}
	if (QrPulldown.instanceMap[id].onChange) {
		QrPulldown.instanceMap[id].onChange(value);
	}
}

// www.kmetop.com Edit 2012-6-8
function QrSpinner(id) {
	this.id = id;

	QrSpinner.instanceMap[this.id] = this;
}

QrSpinner.getAbsPos = function(p) {
	var _x = 0;
	var _y = 0;
	while (p.offsetParent) {
		_x += p.offsetLeft;
		_y += p.offsetTop;
		p = p.offsetParent;
	}

	_x += p.offsetLeft;
	_y += p.offsetTop;

	return {
		x : _x,
		y : _y
	};
};

QrSpinner.getMousePos = function(evt) {
	var _x, _y;
	evt = evt || window.event;
	if (evt.pageX || evt.pageY) {
		_x = evt.pageX;
		_y = evt.pageY;
	} else if (evt.clientX || evt.clientY) {
		_x = evt.clientX + document.body.scrollLeft - document.body.clientLeft;
		_y = evt.clientY + document.body.scrollTop - document.body.clientTop;
	} else {
		return $.$getAbsPos(evt.target);
	}
	return {
		x : _x,
		y : _y
	};
}
QrSpinner.prototype.set = function(value) {
	document.getElementById(this.id + "#input").value = value;
	if (QrSpinner.instanceMap[this.id].onChange) {
		QrSpinner.instanceMap[this.id].onChange(value);
	}
}

QrSpinner.prototype.get = function() {
	return document.getElementById(this.id + "#input").value;
}

QrSpinner.instanceMap = new Array;

QrSpinner.onHover = function(e, id) {
	var mPos = QrSpinner.getMousePos(e);
	var iPos = QrSpinner.getAbsPos(e.target || e.srcElement);
	if ((mPos.y - iPos.y) < 8) {
		document.getElementById(id + "#button").src = QrXPCOM.designerPath + "/img/spinner-updown.gif";
	}
	if ((mPos.y - iPos.y) > 8) {
		document.getElementById(id + "#button").src = QrXPCOM.designerPath + "/img/spinner-downdown.gif";
	}
}

QrSpinner.onOut = function(e, id) {
	document.getElementById(id + "#button").src = QrXPCOM.designerPath + "/img/spinner-normal.gif";
}

QrSpinner.onKeyup = function(id) {
	if (QrSpinner.instanceMap[id].onChange) {
		QrSpinner.instanceMap[id].onChange(document.getElementById(id + "#input").value);
	}
}

QrSpinner.onDown = function(e, id) {
	var p = QrSpinner.getMousePos(e);
	var d = QrSpinner.getAbsPos(e.target || e.srcElement);

	var $input = $(document.getElementById(id + "#input"));

	var uom = $input.attr("uom") || "";
	var vStr = $input.val();
	var v = parseInt(vStr);
	if (!v)
		v = 0;
	else {
		var l = ("" + v).length;
		if (l < vStr.length) {
			uom = vStr.substring(l);
		}
	}

	if ((p.y - d.y) < 8) {
		$input.val("" + (++v) + uom);
	}
	if ((p.y - d.y) > 8) {
		$input.val("" + (--v) + uom);
	}
	if (QrSpinner.instanceMap[id].onChange) {
		QrSpinner.instanceMap[id].onChange($input.val());
	}
}

function CssDesigner($pad) {
	CssDesigner.instance = this;
	this.$pad = $pad;
	this.$inputs = $("input", this.$pad);
	this.$colors = $(".CssColorInput", this.$pad);
	this.connectInstanceMap = new Array;
	this.cacheStyles = new Array;
	this.outputEle = null;// 输出当前CSS到这个位置
	this.targetSelector = null;
	this.targetSubSelector = null;
	var self = this;
	this.$colors.each(function() {
		var ths = $(this);
		ths.css("background-color", ths.val());
	}).ColorPicker({
		zIndex : 99999,
		onSubmit : function(hsb, hex, rgb, el) {
			var v = "#" + hex;
			var $el = $(el);
			var styleName = $el.attr("styleName");
			$el.css("background-color", v).val(v);
			$el.ColorPickerHide();

			self.change(styleName, v);
		},
		onChange : function(hsb, hex, rgb, el) {
			var v = "#" + hex;
			var $el = $(el);
			var styleName = $el.attr("styleName");

			self.change(styleName, v);
		},
		onBeforeShow : function() {
			$(this).ColorPickerSetColor(this.value);
		}
	}).bind("keyup", function() {
		var ths = $(this);
		ths.css("background-color", ths.val());
		ths.ColorPickerSetColor(ths.val());
	}).blur(function() {
		var $this = $(this);
		var styleName = $this.attr("styleName");
		self.change(styleName, $this.val());
	});

	$(".CssUpload", $pad).each(function() {
		var $this = $(this);
		var $input = $(".CssUploadInput", $this);
		var uploadUrl = $this.attr("uploadUrl");
		var acceptType = $this.attr("acceptType");
		var stylePath = $this.attr("stylePath");
		var queueID = $(".CssUploadProgress", $this).attr("id");
		if ($("object", $this).length == 0) {
			$(".CssUploadFile", $this).uploadify({
				'script' : uploadUrl,
				'uploader' : stylePath + '/uploadify/uploadify.swf',
				'cancelImg' : stylePath + '/uploadify/cancel.png',
				'auto' : true,
				'fileDesc' : acceptType,
				'fileExt' : acceptType,
				'queueID' : queueID,
				'fileDataName' : 'upload',
				'multi' : false,
				'queueSizeLimit' : 1,
				height : 14,
				width : 16,
				'sizeLimit' : '512000000',
				'buttonImg' : stylePath + '/uploadify/uploadify.gif',
				'simUploadLimit' : 1,
				'onComplete' : function(event, queueID, fileObj, response, data) {
					var json = window["eval"]("(" + response + ")");
					if (json.success) {
						$input.val("url(" + json.fileUrl + ")");
						var styleName = $input.attr("styleName");
						self.change(styleName, $input.val());
					} else {
						alert(json.customMsg);
					}
					return true;
				},
				'onError' : function(event, queueID, fileObj, errorObj) {
					alert(errorObj.type + ' Error: ' + errorObj.info);
					return true;
				},
				'onCancel' : function(event, queueID, fileObj) {
					return true;
				},
				wmode : "transparent"

			});
		}
	});
	$(".QrPulldownInput", $pad).each(function() {
		self.connectCSS(new QrPulldown(this.name), $(this).attr("styleName"));
	});
	$(".QrSpinnerInput", $pad).each(function() {
		self.connectCSS(new QrSpinner(this.name), $(this).attr("styleName"));
	});
}
CssDesigner.switchCSS = function(e, selector2) {
	var switcher = $(e.srcElement || e.target);
	if (switcher.attr("src").indexOf("arrowclose.gif") > -1) {
		switcher.attr("src", QrXPCOM.designerPath + "/img/arrowopen.gif");

		$(selector2).show();
	} else {
		switcher.attr("src", QrXPCOM.designerPath + "/img/arrowclose.gif");

		$(selector2).hide();
	}
}
// 获取行内样式
CssDesigner.inlineStyles = new Array;
CssDesigner.getInlineStyles = function($one) {
	if ($one.length > 0) {
		var ele = $one.get(0);
		var ret = CssDesigner.inlineStyles[ele];
		if (!ret) {
			ret = new Array;
			CssDesigner.inlineStyles[ele] = ret;
			var styleText = $one.attr("style");
			if (styleText) {
				var styles = styleText.split(";");
				for ( var i = 0; i < styles.length; i++) {
					var st = styles[i];
					var kv = st.split(":");
					if (kv.length == 2) {
						ret[kv[0].trim()] = kv[1].trim();
					}
				}
			}
		}

		return ret;
	}
	return new Array;
}
CssDesigner.parseStyles = function(styleText) {
	var styles = new Array();
	var array = styleText.split(";");
	for (i = 0; i < array.length; i++) {
		var kv = array[i].split(":");
		if (kv.length == 2) {
			styles[kv[0].trim()] = kv[1].trim();
		}
	}
	return styles;
}
CssDesigner.setStyle = function($one, style, value) {
	var styles = CssDesigner.getInlineStyles($one);
	if (style && style.trim().length > 0 && !(styles[style])) {
		try {
			$one.css(style, value);
		} catch (e) {
			alert("Invoke CssDesigner.setStyle($one," + style + "," + value + ") ERROR：" + e);
		}
	}
}
CssDesigner.prototype.setTarget = function(targetSelector) {
	this.targetSelector = targetSelector;
}
CssDesigner.prototype.makeCssOptions = function(element, tagCssPrefix, displayPrefix) {
	var self = this;

	if (!element)
		element = this.targetSelector;
	if (!tagCssPrefix)
		tagCssPrefix = "";
	if (!displayPrefix)
		displayPrefix = "";

	var options = new Array;
	$(element).children().each(function() {
		var ths = $(this);
		var tagName = this.tagName.toUpperCase();

		// 忽略的元素
		if (ths.hasClass("block") //
				|| ths.hasClass("ui-resizable-handle") //
				|| ths.hasClass("ui-dialog") //
				|| ths.hasClass("colorpicker") //
				|| ths.hasClass("blank") //
				|| tagName == "STYLE"//
				|| this.id == "jqContextMenu"//
				|| tagName == "SCRIPT") {
			return;
		}

		// 元素tag作为css名
		var tagCssName = tagName;
		var displayName = tagName;

		// 查找元素class
		var cssName = null;
		var cssNames = ths.attr("class");
		if (cssNames && cssNames.trim().length > 0) {
			cssNames = cssNames.trim().split(" ");
			if (cssNames.length > 0 && cssNames[0] != "area") {
				cssName = "." + cssNames[0];
				displayName = tagName + cssName;
			}
		}
		// class属性未指定，则视图将ID作为css名字
		if (cssName == null && this.id && this.id.trim().length > 0 && self.targetSelector == "body") {
			cssName = "#" + this.id;
			displayName = tagName + "." + cssName;
		}

		//
		if (tagCssPrefix.length > 0)
			tagCssName = tagCssPrefix + " " + tagCssName;
		if (displayPrefix.length > 0)
			displayName = displayPrefix + " > " + displayName;

		// 添加选项
		if (cssName == null) {
			if (self.targetSelector != "body")
				options[tagCssName] = displayName;
		} else {
			options[cssName] = displayName;
		}

		// 查找子元素
		var nextOptions = self.makeCssOptions(this, tagCssName, displayName);
		for (o in nextOptions) {
			v = nextOptions[o];
			options[o] = v;
		}
	});

	return options;

}
/*
 * 获取当前CSS被应用于哪些目标对象？
 */
CssDesigner.prototype.getTargets = function(selector) {
	var $targets = $(this.targetSelector);
	if ($targets.length == 0)
		$targets = $(".CssDesignerDemo", this.$pad);
	if (selector != null && selector.trim().length > 0)
		$targets = $(selector, $targets);

	return $targets;
}
CssDesigner.prototype.connectCSS = function(obj, style) {
	var self = this;
	self.connectInstanceMap[style] = obj;
	obj.onChange = function(value) {
		self.change(style, value);
	}
}
CssDesigner.prototype.refreshCacheStyles = function(c) {
	// 清除当前缓存样式
	for (s in this.cacheStyles) {
		this.setStyles(this.getTargets(s), this.cacheStyles[s], true);
	}

	// 缓存新样式，并将新样式应用在元素上
	this.cacheStyles = c;
	for (s in this.cacheStyles) {
		this.setStyles(this.getTargets(s), this.cacheStyles[s], false);
	}

	// 解析输出框中的CSS文本并同步到CSS编辑器字段中
	this.$inputs.val("");
	if (this.outputEle) {
		var styleText = $(this.outputEle).val();
		var styles = CssDesigner.parseStyles(styleText);
		this.$inputs.each(function() {
			var ths = $(this);
			var styleName = ths.attr("styleName");
			var styleValue = styles[styleName];
			if (styleName && typeof styleValue == "string") {
				ths.val(styleValue);
			}
		});
	}
	this.$colors.each(function() {
		var ths = $(this);
		ths.css("background-color", ths.val());
	});
}
CssDesigner.prototype.setStyles = function($targets, styleText, clear) {
	var styles = CssDesigner.parseStyles(styleText);
	$targets.each(function() {
		var $target = $(this);
		for (style in styles) {
			CssDesigner.setStyle($target, style, (clear ? "" : styles[style]));
		}
	});
}
CssDesigner.prototype.change = function(style, value) {
	// 将样式应用于元素
	this.getTargets(this.targetSubSelector).each(function() {
		CssDesigner.setStyle($(this), style, value);
	});

	// 输出CSS内容到 this.outputEle 框中
	var styleText = "";
	this.$inputs.each(function() {
		var $me = $(this);
		var styleName = $me.attr("styleName");
		var v = $me.val();
		if (styleName && v.length > 0) {
			styleText += styleName + ": " + v + "; ";
		}
	});
	if (this.outputEle) {
		$(this.outputEle).val(styleText).change();
	}
	this.cacheStyles[this.targetSubSelector] = styleText;
}

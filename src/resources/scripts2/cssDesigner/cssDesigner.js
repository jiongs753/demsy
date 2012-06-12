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
	var p = QrXPCOM.getMousePoint(e);
	var d = QrXPCOM.getDivPoint(document.getElementById(id + "#button"));
	if ((p.y - d.y) < 8) {
		document.getElementById(id + "#button").src = QrXPCOM.designerPath + "/img/spinner-updown.gif";
	}
	if ((p.y - d.y) > 8) {
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
	var p = QrXPCOM.getMousePoint(e);
	var d = QrXPCOM.getDivPoint(document.getElementById(id + "#button"));

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

// ====================================
QrXPCOM.cssToJsMap = new Array;
QrXPCOM.cssToJsMap["background"] = "background";
QrXPCOM.cssToJsMap["background-attachment"] = "backgroundAttachment";
QrXPCOM.cssToJsMap["background-color"] = "backgroundColor";
QrXPCOM.cssToJsMap["background-image"] = "backgroundImage";
QrXPCOM.cssToJsMap["background-position"] = "backgroundPosition";
QrXPCOM.cssToJsMap["background-position-x"] = "backgroundPositionX";
QrXPCOM.cssToJsMap["background-position-y"] = "backgroundPositionY";
QrXPCOM.cssToJsMap["background-repeat"] = "backgroundRepeat";
QrXPCOM.cssToJsMap["behavior"] = "behavior";
QrXPCOM.cssToJsMap["border"] = "border";
QrXPCOM.cssToJsMap["border-bottom"] = "borderBottom";
QrXPCOM.cssToJsMap["border-bottom-color"] = "borderBottomColor";
QrXPCOM.cssToJsMap["border-bottom-style"] = "borderBottomStyle";
QrXPCOM.cssToJsMap["border-bottom-width"] = "borderBottomWidth";
QrXPCOM.cssToJsMap["border-color"] = "borderColor";
QrXPCOM.cssToJsMap["border-left"] = "borderLeft";
QrXPCOM.cssToJsMap["border-left-color"] = "borderLeftColor";
QrXPCOM.cssToJsMap["border-left-style"] = "borderLeftStyle";
QrXPCOM.cssToJsMap["border-left-width"] = "borderLeftWidth";
QrXPCOM.cssToJsMap["border-right"] = "borderRight";
QrXPCOM.cssToJsMap["border-right-color"] = "borderRightColor";
QrXPCOM.cssToJsMap["border-right-style"] = "borderRightStyle";
QrXPCOM.cssToJsMap["border-right-width"] = "borderRightWidth";
QrXPCOM.cssToJsMap["border-style"] = "borderStyle";
QrXPCOM.cssToJsMap["border-top"] = "borderTop";
QrXPCOM.cssToJsMap["border-top-color"] = "borderTopColor";
QrXPCOM.cssToJsMap["border-top-style"] = "borderTopStyle";
QrXPCOM.cssToJsMap["border-top-width"] = "borderTopWidth";
QrXPCOM.cssToJsMap["border-width"] = "borderWidth";
QrXPCOM.cssToJsMap["bottom"] = "bottom";
QrXPCOM.cssToJsMap["clear"] = "clear";
QrXPCOM.cssToJsMap["clip"] = "clip";
QrXPCOM.cssToJsMap["color"] = "color";
QrXPCOM.cssToJsMap["cursor"] = "cursor";
QrXPCOM.cssToJsMap["direction"] = "direction";
QrXPCOM.cssToJsMap["display"] = "display";
QrXPCOM.cssToJsMap["filter"] = "filter";
QrXPCOM.cssToJsMap["font"] = "font";
QrXPCOM.cssToJsMap["font-family"] = "fontFamily";
QrXPCOM.cssToJsMap["font-size"] = "fontSize";
QrXPCOM.cssToJsMap["font-style"] = "fontStyle";
QrXPCOM.cssToJsMap["font-variant"] = "fontVariant";
QrXPCOM.cssToJsMap["font-weight"] = "fontWeight";
QrXPCOM.cssToJsMap["font-stretch"] = "fontStretch";
QrXPCOM.cssToJsMap["height"] = "height";
QrXPCOM.cssToJsMap["layout-flow"] = "layoutFlow";
QrXPCOM.cssToJsMap["layout-grid"] = "layoutGrid";
QrXPCOM.cssToJsMap["layout-grid-char"] = "layoutGridChar";
QrXPCOM.cssToJsMap["layout-grid-line"] = "layoutGridLine";
QrXPCOM.cssToJsMap["layout-grid-mode"] = "layoutGridMode";
QrXPCOM.cssToJsMap["layout-grid-type"] = "layoutGridType";
QrXPCOM.cssToJsMap["left"] = "left";
QrXPCOM.cssToJsMap["letter-spacing"] = "letterSpacing";
QrXPCOM.cssToJsMap["line-break"] = "lineBreak";
QrXPCOM.cssToJsMap["line-height"] = "lineHeight";
QrXPCOM.cssToJsMap["margin"] = "margin";
QrXPCOM.cssToJsMap["margin-bottom"] = "marginBottom";
QrXPCOM.cssToJsMap["margin-left"] = "marginLeft";
QrXPCOM.cssToJsMap["margin-right"] = "marginRight";
QrXPCOM.cssToJsMap["margin-top"] = "marginTop";
QrXPCOM.cssToJsMap["overflow"] = "overflow";
QrXPCOM.cssToJsMap["overflow-x"] = "overflowX";
QrXPCOM.cssToJsMap["overflow-y"] = "overflowY";
QrXPCOM.cssToJsMap["padding"] = "padding";
QrXPCOM.cssToJsMap["padding-bottom"] = "paddingBottom";
QrXPCOM.cssToJsMap["padding-left"] = "paddingLeft";
QrXPCOM.cssToJsMap["padding-right"] = "paddingRight";
QrXPCOM.cssToJsMap["padding-top"] = "paddingTop";
QrXPCOM.cssToJsMap["page-break-after"] = "pageBreakAfter";
QrXPCOM.cssToJsMap["page-break-before"] = "pageBreakBefore";
QrXPCOM.cssToJsMap["position"] = "position";
QrXPCOM.cssToJsMap["right"] = "right";
QrXPCOM.cssToJsMap["scrollbar-3dlight-color"] = "scrollbar3dLightColor";
QrXPCOM.cssToJsMap["scrollbar-arrow-color"] = "scrollbarArrowColor";
QrXPCOM.cssToJsMap["scrollbar-base-color"] = "scrollbarBaseColor";
QrXPCOM.cssToJsMap["scrollbar-darkshadow-color"] = "scrollbarDarkShadowColor";
QrXPCOM.cssToJsMap["scrollbar-face-color"] = "scrollbarFaceColor";
QrXPCOM.cssToJsMap["scrollbar-highlight-color"] = "scrollbarHighlightColor";
QrXPCOM.cssToJsMap["scrollbar-shadow-color"] = "scrollbarShadowColor";
QrXPCOM.cssToJsMap["scrollbar-track-color"] = "scrollbarTrackColor";
QrXPCOM.cssToJsMap["float"] = "styleFloat";
QrXPCOM.cssToJsMap["text-align"] = "textAlign";
QrXPCOM.cssToJsMap["text-align-last"] = "textAlignLast";
QrXPCOM.cssToJsMap["text-autospace"] = "textAutospace";
QrXPCOM.cssToJsMap["text-decoration"] = "textDecoration";
QrXPCOM.cssToJsMap["text-indent"] = "textIndent";
QrXPCOM.cssToJsMap["text-justify"] = "textJustify";
QrXPCOM.cssToJsMap["text-kashida-space"] = "textKashidaSpace";
QrXPCOM.cssToJsMap["text-overflow"] = "textOverflow";
QrXPCOM.cssToJsMap["text-transform"] = "textTransform";
QrXPCOM.cssToJsMap["text-underline-position"] = "textUnderlinePosition";
QrXPCOM.cssToJsMap["text-shadow"] = "textShadow";
QrXPCOM.cssToJsMap["top"] = "top";
QrXPCOM.cssToJsMap["unicode-bidi"] = "unicodeBidi";
QrXPCOM.cssToJsMap["visibility"] = "visibility";
QrXPCOM.cssToJsMap["white-space"] = "whiteSpace";
QrXPCOM.cssToJsMap["width"] = "width";
QrXPCOM.cssToJsMap["word-break"] = "wordBreak";
QrXPCOM.cssToJsMap["word-spacing"] = "wordSpacing";
QrXPCOM.cssToJsMap["word-wrap"] = "wordWrap";
QrXPCOM.cssToJsMap["writing-mode"] = "writingMode";
QrXPCOM.cssToJsMap["z-index"] = "zIndex";
QrXPCOM.cssToJsMap["zoom"] = "zoom";
QrXPCOM.cssToJsMap["list-style-type"] = "listStyleType";
QrXPCOM.cssToJsMap["list-style-position"] = "listStylePosition";
QrXPCOM.cssToJsMap["list-style-image"] = "listStyleImage";
QrXPCOM.cssToJsMap["outline-color"] = "outlineColor";
QrXPCOM.cssToJsMap["outline-style"] = "outlineStyle";
QrXPCOM.cssToJsMap["outline-width"] = "outlineWidth";
QrXPCOM.cssToJsMap["border-collapse"] = "borderCollapse";
QrXPCOM.cssToJsMap["border-spacing"] = "borderSpacing";
QrXPCOM.cssToJsMap["empty-cells"] = "emptyCells";
QrXPCOM.cssToJsMap["caption-side"] = "captionSide";
QrXPCOM.cssToJsMap["table-layout"] = "tableLayout";

function CssDesigner($pad) {
	$(".CssColorInput", $pad).each(function() {
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

			CssDesigner.setTargetStyle(styleName, v);
		},
		onChange: function(hsb, hex, rgb, el){
			var v = "#" + hex;
			var $el = $(el);
			var styleName = $el.attr("styleName");

			CssDesigner.setTargetStyle(styleName, v);
		},
		onBeforeShow : function() {
			$(this).ColorPickerSetColor(this.value);
		}
	}).bind("keyup", function() {
		var ths = $(this);
		ths.css("background-color", ths.val());
		ths.ColorPickerSetColor(ths.val());
	}).blur(function(){
		var $this=$(this);
		var styleName = $this.attr("styleName");
		CssDesigner.setTargetStyle(styleName, $this.val());
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
						$input.val("url("+json.fileUrl+")");
						var styleName = $input.attr("styleName");
						CssDesigner.setTargetStyle(styleName, $input.val());
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
}
CssDesigner.connectInstanceMap = new Array;
CssDesigner.target = null;
CssDesigner.output = null;
CssDesigner.styles = new Array;
CssDesigner.connectCSS = function(obj, style) {
	CssDesigner.connectInstanceMap[style] = obj;
	obj.onChange = function(value) {
		CssDesigner.setTargetStyle(style, value);
	}
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
CssDesigner.setTargetStyle = function(style, value) {
	$(CssDesigner.target).each(function() {
		try {
			this.style[QrXPCOM.cssToJsMap[style]] = value;
		} catch (e) {
			this.style[QrXPCOM.cssToJsMap[style]] = "";
		}
	});
	CssDesigner.styles[style] = value;
	var styleText = "";
	for (st in CssDesigner.styles) {
		if (CssDesigner.styles[st] && CssDesigner.styles[st].length > 0)
			styleText += st + ":" + CssDesigner.styles[st] + "; ";
	}
	$(CssDesigner.output).val(styleText);
}
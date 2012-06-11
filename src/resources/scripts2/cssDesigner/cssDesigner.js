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
function QrColorPicker(id) {
	this.id = id;
	QrXPCOM.init();
	QrColorPicker.instanceMap[id] = this;
}

QrColorPicker.prototype.set = function(color) {
	if (QrColorPicker.instanceMap[this.id].onChange) {
		QrColorPicker.instanceMap[this.id].onChange(color);
	}
	if (color == "")
		color = "transparent";
	document.getElementById(this.id + "#input").value = color;
	document.getElementById(this.id + "#text").innerHTML = color;
	document.getElementById(this.id + "#color").style.background = color;
}

QrColorPicker.prototype.get = function() {
	return document.getElementById(this.id + "#input").value;
}

QrColorPicker.instanceMap = new Array;
QrColorPicker.restorePool = new Array;

QrColorPicker.transparent = function(id) {
	QrColorPicker.instanceMap[id].set("transparent");
	document.getElementById(id + "#menu").style.display = "none";
	if (QrColorPicker.instanceMap[id].onChange) {
		QrColorPicker.instanceMap[id].onChange("transparent");
	}
}

QrColorPicker.popupPicker = function(id) {
	var pop = document.getElementById(id);
	var p = QrXPCOM.getDivPoint(pop);
	QrXPCOM.setDivPoint(document.getElementById(id + "#menu"), p.x, p.y + 20);

	document.getElementById(id + "#menu").style.display = "";
	QrXPCOM.onPopup(document.getElementById(id + "#menu"));
}

QrColorPicker.setColor = function(event, id) {
	if (!QrColorPicker.restorePool[id])
		QrColorPicker.restorePool[id] = document.getElementById(id + "#input").value;

	var d = QrXPCOM.getMousePoint(event, document.getElementById(id + "#menu"));
	var picked = QrColorPicker.colorpicker(d.x, d.y).toUpperCase();

	document.getElementById(id + "#input").value = picked;
	document.getElementById(id + "#text").innerHTML = picked;
	document.getElementById(id + "#color").style.background = picked;
	if (QrColorPicker.instanceMap[id].onChange) {
		QrColorPicker.instanceMap[id].onChange(picked);
	}
	return picked;
};

QrColorPicker.keyColor = function(id) {
	try {
		document.getElementById(id + "#color").style.background = document.getElementById(id + "#input").value;
		QrColorPicker.restorePool[id] = document.getElementById(id + "#input").value;
		document.getElementById(id + "#text").innerHTML = QrColorPicker.restorePool[id];
	} catch (e) {
	}
};

QrColorPicker.selectColor = function(event, id) {
	var picked = QrColorPicker.setColor(event, id);

	document.getElementById(id + "#menu").style.display = "none";
	QrColorPicker.restorePool[id] = picked;
	if (QrColorPicker.instanceMap[id].onSelect) {
		QrColorPicker.instanceMap[id].onSelect(picked);
	}
};

QrColorPicker.restoreColor = function(id) {
	if (QrColorPicker.restorePool[id]) {
		document.getElementById(id + "#input").value = QrColorPicker.restorePool[id];
		document.getElementById(id + "#text").innerHTML = QrColorPicker.restorePool[id];
		document.getElementById(id + "#color").style.background = QrColorPicker.restorePool[id];
		if (QrColorPicker.instanceMap[id].onChange) {
			QrColorPicker.instanceMap[id].onChange(QrColorPicker.restorePool[id]);
		}
		QrColorPicker.restorePool[id] = null;
	}
};

QrColorPicker.colorpicker = function(prtX, prtY) {
	var colorR = 0;
	var colorG = 0;
	var colorB = 0;

	if (prtX < 32) {
		colorR = 256;
		colorG = prtX * 8;
		colorB = 1;
	}
	if (prtX >= 32 && prtX < 64) {
		colorR = 256 - (prtX - 32) * 8;
		colorG = 256;
		colorB = 1;
	}
	if (prtX >= 64 && prtX < 96) {
		colorR = 1;
		colorG = 256;
		colorB = (prtX - 64) * 8;
	}
	if (prtX >= 96 && prtX < 128) {
		colorR = 1;
		colorG = 256 - (prtX - 96) * 8;
		colorB = 256;
	}
	if (prtX >= 128 && prtX < 160) {
		colorR = (prtX - 128) * 8;
		colorG = 1;
		colorB = 256;
	}
	if (prtX >= 160) {
		colorR = 256;
		colorG = 1;
		colorB = 256 - (prtX - 160) * 8;
	}

	if (prtY < 64) {
		colorR = colorR + (256 - colorR) * (64 - prtY) / 64;
		colorG = colorG + (256 - colorG) * (64 - prtY) / 64;
		colorB = colorB + (256 - colorB) * (64 - prtY) / 64;
	}
	if (prtY > 64 && prtY <= 128) {
		colorR = colorR - colorR * (prtY - 64) / 64;
		colorG = colorG - colorG * (prtY - 64) / 64;
		colorB = colorB - colorB * (prtY - 64) / 64;
	}
	if (prtY > 128) {
		colorR = 256 - (prtX / 192 * 256);
		colorG = 256 - (prtX / 192 * 256);
		colorB = 256 - (prtX / 192 * 256);
	}

	colorR = parseInt(colorR);
	colorG = parseInt(colorG);
	colorB = parseInt(colorB);

	if (colorR >= 256) {
		colorR = 255;
	}
	if (colorG >= 256) {
		colorG = 255;
	}
	if (colorB >= 256) {
		colorB = 255;
	}

	colorR = colorR.toString(16);
	colorG = colorG.toString(16);
	colorB = colorB.toString(16);

	if (colorR.length < 2) {
		colorR = 0 + colorR;
	}
	if (colorG.length < 2) {
		colorG = 0 + colorG;
	}
	if (colorB.length < 2) {
		colorB = 0 + colorB;
	}

	return "#" + colorR + colorG + colorB;
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
	if (QrXPCOM.isIE())
		QrXPCOM.setDivPoint(document.getElementById(id + "#menu"), p.x + 1, p.y + 22);
	else
		QrXPCOM.setDivPoint(document.getElementById(id + "#menu"), p.x + 1, p.y + 22 - 1);

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

	if ((p.y - d.y) < 10) {
		document.getElementById(id + "#button").src = QrXPCOM.designerPath + "/img/spinner-updown.gif";
	}
	if ((p.y - d.y) > 10) {
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

	var v = parseInt(document.getElementById(id + "#input").value);
	if (!v)
		v = 0;
	if ((p.y - d.y) < 10) {
		document.getElementById(id + "#input").value = ++v;
	}
	if ((p.y - d.y) > 10) {
		document.getElementById(id + "#input").value = --v;
	}
	if (QrSpinner.instanceMap[id].onChange) {
		QrSpinner.instanceMap[id].onChange(v);
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
var connectInstanceMap = new Array;
var connectInstanceOverrideMap = new Array;
var styles = new Array;
function connectCSS(obj, style, override) {
	connectInstanceMap[style] = obj;
	connectInstanceOverrideMap[style] = override;
	obj.onChange = function(value) {
		setTargetStyle(style, value);
	}
}
function connectCSS2(obj, objB, style, override) {
	connectInstanceMap[style] = new Array;
	connectInstanceMap[style][0] = obj;
	connectInstanceMap[style][1] = objB;
	connectInstanceOverrideMap[style] = override;
	obj.onChange = function(value) {
		value = value + objB.get();
		setTargetStyle(style, value);
	}
	objB.onChange = function(value) {
		value = obj.get() + value;
		setTargetStyle(style, value);
	}
}
function setTargetStyle(style, value) {
	try {
		document.getElementById("target").style[QrXPCOM.cssToJsMap[style]] = value;
		document.getElementById("target2").style[QrXPCOM.cssToJsMap[style]] = value;
		document.getElementById("target3").style[QrXPCOM.cssToJsMap[style]] = value;
		styles[style] = value;
	} catch (e) {
		try {
			document.getElementById("target").style[QrXPCOM.cssToJsMap[style]] = "";
			document.getElementById("target2").style[QrXPCOM.cssToJsMap[style]] = "";
			document.getElementById("target3").style[QrXPCOM.cssToJsMap[style]] = "";
			styles[style] = null;
		} catch (e) {
		}
	}
	try {
		var st;
		var cssstr = ".someclass {\n";
		for (st in styles) {
			if (styles[st])
				cssstr += "\t" + st + " : " + styles[st] + ";\n";
		}
		cssstr += "}\n";
		document.getElementById("output").value = cssstr;

		var divstr = "<DIV style=\"";
		for (st in styles) {
			if (styles[st])
				divstr += st + ":" + styles[st] + "; ";
		}
		divstr += "\">\n\n</DIV>";
		document.getElementById("output2").value = divstr;
	} catch (e) {

	}
}
function collapseSwitch(style,style1,style2,style3,style4) {
	var $style = $("#"+style);
	if ($style.attr("hide") != "1") {
		$style.attr("hide", "1");
		$("#"+style + "#switch").attr("src", QrXPCOM.designerPath + "/img/arrowopen.gif");
		$("#"+style1).show();
		$("#"+style2).show();
		$("#"+style3).show();
		$("#"+style4).show();
	} else {
		$style.attr("hide", "0");
		$("#"+style + "#switch").attr("src", QrXPCOM.designerPath + "/img/arrowclose.gif");
		$("#"+style1).hide();
		$("#"+style2).hide();
		$("#"+style3).hide();
		$("#"+style4).hide();
	}
}

function innerset() {
	document.getElementById("target").innerHTML = document.getElementById("innerset").value;
	document.getElementById("target2").innerHTML = document.getElementById("innerset").value;
	document.getElementById("target3").innerHTML = document.getElementById("innerset").value;
}
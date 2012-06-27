var alerts = {
	_boxId : "#alertMsgBox",
	_bgId : "#alertBackground",
	_closeTimer : null,

	_types : {
		error : "error",
		info : "info",
		warn : "warn",
		correct : "correct",
		confirm : "confirm"
	},

	_getTitle : function(key) {
		return $.regional.alerts.title[key];
	},

	_open : function(type, msg, buttons) {
		$(this._boxId).remove();
		var butsHtml = "";
		if (buttons) {
			for ( var i = 0; i < buttons.length; i++) {
				var sRel = buttons[i].call ? "callback" : "";
				butsHtml += DWZ.frag["alertButFrag"].replace("#butMsg#", buttons[i].name).replace("#callback#", sRel);
			}
		}
		var boxHtml = DWZ.frag["alertBoxFrag"].replace("#type#", type).replace("#title#", this._getTitle(type)).replace("#message#", msg).replace("#butFragment#", butsHtml);
		$(boxHtml).appendTo("body").css({
			top : -$(this._boxId).height() + "px"
		}).animate({
			top : "0px"
		}, 500);

		if (this._closeTimer) {
			clearTimeout(this._closeTimer);
			this._closeTimer = null;
		}
		if (this._types.info == type || this._types.correct == type) {
			this._closeTimer = setTimeout(function() {
				alerts.close()
			}, 3500);
		} else {
			$(this._bgId).show();
		}
		var jCallButs = $(this._boxId).find("[rel=callback]");
		for ( var i = 0; i < buttons.length; i++) {
			if (buttons[i].call)
				jCallButs.eq(i).click(buttons[i].call);
		}
	},
	close : function() {
		$(this._boxId).animate({
			top : -$(this._boxId).height()
		}, 500, function() {
			$(this).remove();
		});
		$(this._bgId).hide();
	},
	error : function(msg, options) {
		this._alert(this._types.error, msg, options);
	},
	info : function(msg, options) {
		this._alert(this._types.info, msg, options);
	},
	warn : function(msg, options) {
		this._alert(this._types.warn, msg, options);
	},
	correct : function(msg, options) {
		this._alert(this._types.correct, msg, options);
	},
	_alert : function(type, msg, options) {
		var op = {
			okName : $.regional.alerts.butMsg.ok,
			okCall : null
		};
		$.extend(op, options);
		var buttons = [ {
			name : op.okName,
			call : op.okCall
		} ];
		this._open(type, msg, buttons);
	},
	confirm : function(msg, options) {
		var op = {
			okName : $.regional.alerts.butMsg.ok,
			okCall : null,
			cancelName : $.regional.alerts.butMsg.cancel,
			cancelCall : null
		};
		$.extend(op, options);
		var buttons = [ {
			name : op.okName,
			call : op.okCall
		}, {
			name : op.cancelName,
			call : op.cancelCall
		} ];
		this._open(this._types.confirm, msg, buttons);
	}
};

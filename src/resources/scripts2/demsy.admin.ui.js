var DemsyUIManager = function() {
	this.create = function(options) {
		var self = this;
		self.options = options;
		self.border = "1px dashed #336699";

		self.setupPage();
		self.setupBlock();
	};
	this.drop = function(event, ui, $parent) {
		var self = this;
		var offset = ui.offset;
		var pageOffset = $parent.offset();

		var $b = $(ui.draggable);
		var viewTypeID = parseInt($b.attr("dataID"));
		var viewExpr = $(".expr", $b).val();
		var moduleGuid = $(".moduleGuid", $b).val();
		var type = $b.attr("type");
		var w = $b.attr("defaultWidth");
		var h = $b.attr("defaultHeight");
		var pid = $parent.attr("dataID");
		if (type == "_ref_page") {
			type = 2;
			viewTypeID = 0;
		} else if (type == "_empty") {
			type = 9
		}
		if (typeof pid == "undefined") {
			pid = "";
		}
		var params = {
			"data.page.id" : self.options.pageID,
			"data.type" : type,
			"data.viewType.id" : viewTypeID,
			"data.parent.id" : pid,
			"data.viewExpression" : viewExpr,
			"data.dataset.moduleGuid" : moduleGuid,
			"data.position.area" : $parent.attr("id"),
			"data.position.position" : "absolute",
			"data.position.left" : parseInt(offset.left - pageOffset.left),
			"data.position.top" : parseInt(offset.top - pageOffset.top),
			"data.position.width" : w,
			"data.position.height" : h
		};
		self.edit(self.options.createUrl, self.options.saveUrl, "", params, 0, "");
	};
	this.setupPage = function() {
		var self = this;
		$(document).contextMenu('pageContextMenu', {
			bindings : {
				createBlock : function(t) {
					var dialog = $("#dialogUiModelLib")
					dialog.block();
					dialog.load(self.options.loadUilibUrl, "").dialog({
						title : "选择板块视图",
						width : 310,
						position : [ "right", 0 ],
						close : function() {
							$(this).dialog("destroy");
							self.$droppable.droppable("destroy").removeClass("ui-droppable");
						}
					});
					if (self.$droppable)
						self.$droppable.droppable("destroy").removeClass("ui-droppable");
					self.$droppable = $(".area").droppable({
						accept : ".viewComponent",
						drop : function(event, ui) {
							self.drop(event, ui, $(this));
						}
					});
				},
				editPage : function(t) {
					var pageID = $("body").attr("pageID");
					self.edit(self.options.editPageUrl + pageID, self.options.savePageUrl, pageID, "", 2, pageID);
				},
				edit1Page : function(t) {
					var pageID = $("body").attr("pageID");
					self.edit(self.options.edit1PageUrl + pageID, self.options.savePageUrl, pageID, "", 2, pageID);
				},
				createStyle : function(t) {
					var $body = $("body");
					var pageID = $body.attr("pageID");
					self.edit(self.options.createStyleUrl, self.options.saveStyleUrl, "", "data.detailState=0", 1, "");
				},
				editStyle : function(t) {
					var $body = $("body");
					var pageID = $body.attr("pageID");
					var styleID = $body.attr("styleID");
					if (typeof styleID == "undefined") {
						styleID = "";
					}
					self.edit(self.options.editStyleUrl + styleID, self.options.saveStyleUrl, styleID, "", 1, "");
				},
				createWebCata : function(t) {
					self.edit(self.options.createWebCataUrl, self.options.saveWebCataUrl, "", "", 3, "");
				},
				createWebCont : function(t) {
					window.open(self.options.createWebContUrl, "webcontent").focus();
				},
				showBox : function() {
					$(".block").addClass("box");
				},
				hideBox : function() {
					$(".block").removeClass("box");
				}
			}
		});
		self.evalPageHeight();
	};
	this.evalPageHeight = function() {
		$(".area").each(function() {
			var $area = $(this);
			var maxh = $area.height();
			$(".block", $area).each(function() {
				var me = $(this);
				var os = me.offset()
				var areaOS = $area.offset()
				var h = os.top - areaOS.top + me.height();
				if (h > maxh) {
					maxh = h;
				}
			})
			$area.height(maxh);
		});
	};
	this.setupViewComponent = function() {
		var self = this;
		var viewComponents = $("#viewComponents").accordion();
		var a = $(".viewComponent", viewComponents).disableSelection().css({
			"cursor" : "move"
		}).hover(function() {
			var me = $(this);

			me.css("border", self.border);
			me.draggable({
				helper : "clone"
			})
		}, function() {
			$(this).css("border", "1px solid #d2d2d2");
		});
	};
	this.setupBlock = function() {
		var self = this;
		$(".block").disableSelection().css({
			"cursor" : "move"
		}).click(function() {
			return false;
		}).hover(function() {
			var me = $(this);
			me.addClass("selectedBlock").css({
				zIndex : 100
			}).draggable({
				drag : function(event, ui) {
					me.addClass("selectedBlock");
				},
				stop : function(event, ui) {
					var p = ui.position;
					me.block();
					$.get(self.options.saveUrl + me.attr("dataID"), "data.position.left=" + p.left + "&data.position.top=" + p.top, function(jsonObj) {
						self.evalPageHeight();
					}, "json");
					me.removeClass("selectedBlock");
				}
			}).resizable({
				resize : function(event, ui) {
					me.addClass("selectedBlock");
				},
				stop : function(event, ui) {
					var size = ui.size;
					me.block();
					$.get(self.options.saveUrl + me.attr("dataID"), "data.position.width=" + size.width + "&data.position.height=" + size.height, function(jsonObj) {
						self.evalPageHeight();
					}, "json");
					me.removeClass("selectedBlock");
				}
			});

			return false;
		}, function() {
			$(this).removeClass("selectedBlock").css({
				zIndex : 0
			}).draggable("destroy").resizable("destroy");

			return false;
		}).dblclick(function() {
			var me = $(this);
			var refPage = me.attr("refpage");
			if (refPage.length > 0) {
				window.open(self.options.loadPageUrl + refPage, "refPage_" + refPage).focus();
			} else {
				var dataID = me.attr("dataID");
				self.edit(self.options.editUrl + dataID, self.options.saveUrl, dataID, "", 0, dataID);
			}
			return false;
		}).contextMenu('blockContextMenu', {
			bindings : {
				createBlock : function(t) {
					var me = $(t);
					var dataID = me.attr("dataID");
					var dialog = $("#dialogUiModelLib")
					dialog.block();
					dialog.load(self.options.loadUilibUrl + dataID, "").dialog({
						title : "选择板块视图",
						width : 310,
						position : [ "right", 0 ],
						close : function() {
							$(this).dialog("destroy");
							dialog.html("");
							self.$droppable.droppable("destroy").removeClass("ui-droppable");
						}
					});
					if (self.$droppable)
						self.$droppable.droppable("destroy").removeClass("ui-droppable");
					self.$droppable = me.droppable({
						activeClass : "droppable-active",
						hoverClass : "droppable-hover",
						accept : ".viewComponent",
						drop : function(event, ui) {
							self.drop(event, ui, $(this));
						}
					});
				},
				editBlock : function(t) {
					var me = $(t);
					var dataID = me.attr("dataID");
					self.edit(self.options.editUrl + dataID, self.options.saveUrl, dataID, "", 0, dataID);
				},
				edit1Block : function(t) {
					var me = $(t);
					var dataID = me.attr("dataID");
					self.edit(self.options.edit1Url + dataID, self.options.saveUrl, dataID, "", 0, dataID);
				},
				deleteBlock : function(t) {
					var me = $(t);
					var dataID = me.attr("dataID");
					if (dataID.length > 0 && confirm("你确定要删除吗？")) {
						me.block();
						$.post(self.options.saveUrl + dataID, "data.disabled=true", function(jsonObj) {
							if (jsonObj.success)
								$("#block" + dataID).remove();
							else
								alert(jsonObj.message);

							self.evalPageHeight();
						}, "json");
					}
				},
				editStyle : function(t) {
					var me = $(t);
					var styleID = me.attr("styleID");
					if (typeof styleID == "undefined")
						styleID = "";
					self.edit(self.options.editStyleUrl + styleID, self.options.saveStyleUrl, styleID, "", 1, me.attr("dataID"));
				}
			}
		});
	};
	this.reloadBlock = function(id, $form) {
		var self = this;
		var eles = $form[0].elements;
		var pos = {};
		if(!eles["data.parent.id"]){
			return;
		}
		var parentId = eles["data.parent.id"].value;
		if (eles["data.position.position"]) {
			pos = {
				position : eles["data.position.position"].value || "absolute",
				left : parseInt(eles["data.position.left"].value),
				top : parseInt(eles["data.position.top"].value),
				width : parseInt(eles["data.position.width"].value),
				height : parseInt(eles["data.position.height"].value)
			}
		}

		var block = $("#block" + id);
		if (block.length == 0) {
			var appendTarget = "";
			if (parentId.length == 0) {
				appendTarget = eles["data.position.area"].value;
			} else {
				appendTarget = "block" + parentId;
			}
			block = $("<div dataID='" + id + "'><img src='" + this.options.contextPath + "/themes2/images/busy.gif' /></div>").addClass("block").css({
				border : this.border
			}).appendTo($("#" + appendTarget));

			block.css(pos).attr("id", "block" + id);
		} else {
			block.css(pos);
		}

		var loadBlock = block;
		var loadBlockId = id;
		if (parentId.length > 0) {
			loadBlockId = parentId;
			loadBlock = $("#block" + parentId);
		}
		loadBlock.load(this.options.loadUrl + loadBlockId, "", function() {
			self.options.webUI.init();
			self.evalPageHeight();
			self.setupBlock();
		});
	};
	this.reloadStyle = function(id) {
		var self = this;
		var style = $(document.getElementById("style" + id));
		if (style.length == 0) {
			style = $("<div dataID='" + id + "'></div>").addClass("blank").insertBefore($("#top"));
			style.attr("id", "style" + id);
		}
		if ($.type(id)=="string"&&id.indexOf("#") == 0) {
			id = id.substring(1);
		}
		style.load(this.options.loadStyleUrl + id, "");
	}
	this.save = function(saveUrl, dialog, success, type, blockID) {
		var self = this;
		var $form = $("form", dialog);
		$form.block();
		$.post(saveUrl, $form.serializeArray(), function(jsonObj) {
			if (!jsonObj.success) {
				alert(jsonObj.message);
				return;
			}

			var oldStyleID = "";
			var newStyleID = "";
			try {
				if (type == 0) {
					blockID = "" + jsonObj.data;
					newStyleID = $form[0].elements["data.style.id"].value;
				} else if (type == 1) {
					newStyleID = jsonObj.data;
				}
			} catch (e) {
			}

			if (blockID.length > 0) {
				var block = $("#block" + blockID);
				oldStyleID = block.attr("styleID");
				block.attr("styleID", newStyleID);
				block.removeClass("css_" + oldStyleID);
				block.addClass("css_" + newStyleID);
			}
			
			if (type == 0) {
				var block = $("#block" + blockID);
				self.reloadBlock(blockID, $form);
				var css = $($form[0].elements["data.styleItems"]);
				if (css.length > 0 && typeof CssDesigner != "undefined" && typeof CssDesigner.instance != "undefined") {
					self.reloadStyle("#block" + blockID);
				}
			} else if (type == 1) {
				self.reloadStyle(newStyleID);
				if (blockID.length > 0 && oldStyleID.length == 0) {
					$.post(self.options.saveUrl + blockID, "data.style.id=" + newStyleID);
				}
			}

			if (success)
				success(jsonObj);
		}, "json");
	};
	this.clearCssDesigner = function() {
		if (typeof CssDesigner != "undefined" && typeof CssDesigner.instance != "undefined") {
			try {
				CssDesigner.instance.refreshCacheStyles(new Array());
			} catch (e) {
				alert("Css Designer error:" + e);
			}
		}
	}
	this.edit = function(loadUrl, saveUrl, dataID, params, type, blockID) {
		this.clearCssDesigner();
		var self = this;
		var dialog = $("#dialog");
		dialog.load(loadUrl + "?dialog=true", params, function() {
			if (typeof CssDesigner != "undefined" && typeof CssDesigner.instance != "undefined") {
				if (type == 0) {
					CssDesigner.instance.setTarget("#block" + blockID);
				} else if (type == 1) {
					if (blockID && blockID.length > 0)
						CssDesigner.instance.setTarget("#block" + blockID);
					else
						CssDesigner.instance.setTarget("body");
				} else if (type == 2) {
					CssDesigner.instance.setTarget("body");
				}
			}
		}).dialog({
			width : 500,
			zIndex : 9999,
			position : [ "left", 1 ],
			buttons : {
				"确定" : function() {
					var me = $(this);
					self.save(saveUrl + dataID, $(this), function(jsonObj) {
						me.dialog("destroy");
						dialog.html("");
						self.clearCssDesigner();
					}, type, blockID);
				},
				"关闭" : function() {
					$(this).dialog("destroy");
					dialog.html("");
					self.clearCssDesigner();
				},
				"另存为" : function() {
					self.save(saveUrl, $(this), function(jsonObj) {
						dataID = jsonObj.data;
						if (blockID.length == 0) {
							blockID = dataID;
						}
					}, type, "");
				},
				"应用" : function() {
					// 新增时dataID为空，因此需要在点击应用按钮时回写dataID
					self.save(saveUrl + dataID, $(this), function(jsonObj) {
						dataID = jsonObj.data;
						if (blockID.length == 0) {
							blockID = dataID;
						}
						self.clearCssDesigner();
					}, type, blockID);
				}
			},
			close : function() {
				$(this).dialog("destroy");
				dialog.html("");
				self.clearCssDesigner();
			}
		}).block();
	};
}
if (typeof demsyUIManager == "undefined" || demsyUIManager == null) {
	demsyUIManager = new DemsyUIManager();
}
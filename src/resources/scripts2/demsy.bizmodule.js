/**
 * sysObj: {navi:obj, grid:obj, tmenu:obj}
 * <p>
 * combMap: Map<comboboxID, sysObj>
 * 
 * @returns
 */
var BizModule = function() {
	this.$tabs = null;
	this.config = {};

	this.currentAction = null;

	this.naviMenus = {};
	this.tmenus = {};
	this.grids = {};
	this.actionTabs = {};
	this.moduleTabs = {};
	this.slaveFKs = {};
	this.masterModules = {};

	this.combMap = {};

	this.hideDialog = function(moduleID, actionID) {
		if (this.$tabs) {
			this.$tabs.tabs("remove", this.actionTabs[actionID]);
			this.$tabs.tabs("select", this.moduleTabs[moduleID] || 0);
		} else {
			window.close();
		}
	};
	this.reloadGrid = function(moduleID, actionID) {
		var grid = this.grids[moduleID];
		if (grid)
			grid.trigger("reloadGrid");
	};
	this.gridParams = function(grid) {
		var params = new Array();

		var p = grid.getGridParam();

		var filters = p.postData.filters;
		if (filters && filters.length > 0) {
			params[params.length] = "_search=true";
			params[params.length] = "filters=" + filters;
		}
		var naviRules = p.postData.naviRules;
		if (naviRules && naviRules.length > 0) {
			params[params.length] = "naviRules=" + encodeURIComponent(naviRules);
		}

		return params;
	};
	this.gridSelected = function(grid) {
		if (typeof grid == "undefined" || grid == null) {
			grid = this.grids[this.config.moduleID];
		}
		var ids = new Array();
		if (typeof grid != "undefined" && grid != null) {
			var rows = grid.jqGrid("getGridParam", "selarrrow");
			for ( var i = rows.length - 1; i >= 0; i--)
				ids[i] = grid.getCell(rows[i], 1);
		}

		return ids;
	}
	this.execActionCallback = function(tmenu, item) {
		var self = this;

		this.currentAction = item;

		var mID = item.moduleID;
		var aID = item.id;
		var grid = this.grids[mID];

		//
		var rows = "";
		var target = null;// "_blank";
		if (typeof item.target != "undefined" && item.target.length > 0) {
			target = item.target;
		}

		// 生成参数
		var params = new Array();
		var type = parseInt(item.type);
		switch (type) {
		case 101:// add
			var rules = this.getNaviRules(mID, item.masterModuleID, item.params);
			if (rules != null && rules.length > 0) {
				params[0] = "naviRules=" + encodeURIComponent('["' + rules.join('","') + '"]');
			}
			break
		case 102:// edit
			var gridRows = this.gridSelected(grid);
			if (gridRows.length == 1) {
				rows = gridRows[0];
			} else {
				alert("请先选中一条记录");
				return;
			}
			break
		case 108:// 从XLS导入
			break;
		case 251:// 排序
		case 252:
		case 253:
			var gridRows = this.gridSelected(grid);
			if (gridRows.length > 0) {
				rows = gridRows.join(",");
			} else {
				alert("请先选中待排序的记录");
				return;
			}
			params = this.gridParams(grid);
		case 2:// sub module
			break
		default:
			var gridRows = this.gridSelected(grid);
			if (gridRows.length > 0) {
				if (type == 299 && !confirm("你确定要【" + item.text + "】选中的记录吗?")) {
					return;
				}
				rows = gridRows.join(",");
			} else {
				if (!confirm("尚未选中任何记录，你确定要对分页列表中的所有记录执行【" + item.text + "】操作吗?")) {
					return;
				}
				params = this.gridParams(grid);
			}
		}

		// 生成URL
		var url;
		if (typeof item.url != "undefined" && item.url.length > 0) {
			url = tmenu.contextPath + item.url.replace(/\*/, rows);
		} else {
			url = tmenu.contextPath + tmenu.type + type + "/" + mID + (target == null && this.$tabs != null ? ":/" : "/") + item.mode + ":" + rows;
		}
		if (params.length > 0) {
			url += "?" + params.join("&");
		}

		// 执行业务逻辑
		if (type > 200) {// 立即执行
			if (item.params.length > 0) {
				if (params.length > 0) {
					url += "&data." + item.params;
				} else {
					url += "?data." + item.params;
				}
			}
			self.$tabs.block();
			$.getJSON(url, "", function(jsonObj) {
				if (jsonObj.success) {
					self.reloadGrid(mID);
				} else {
					alert(jsonObj.message);
				}
			});
		} else if (type == 2) {// sub module
			var pID = item.masterModuleID;
			var gridRows = this.gridSelected(this.grids[pID]);
			if (gridRows.length > 0) {
				naviRules = '["' + item.params + ' in ' + gridRows.join(',') + '"]';
				url += "?fixedNaviRules=" + naviRules;
			}

			if ($("#" + this.moduleTabs[mID]).length == 0) {
				this.$tabs.tabs("add", url, item.text);
			} else {
				var navi = self.naviMenus[mID];
				if (typeof navi != "undefined" && navi != null) {
					navi.uncheckAll();
				}
				this.reloadGrid(mID);
			}
			this.$tabs.tabs("select", this.moduleTabs[mID]);
		} else {
			if (target != null) {
				window.open(url, target).focus();
			} else {
				if (this.$tabs == null) {
					window.open(url, "win_" + mID).focus();
				} else {
					if ($("#" + this.actionTabs[aID]).length > 0) {
						this.$tabs.tabs("url", this.actionTabs[aID], url);
					} else {
						this.$tabs.tabs("add", url, item.text);
					}
					this.$tabs.tabs("select", this.actionTabs[aID]);
				}
			}
		}

	};
	this.getNaviRules = function(mid, pID, pProp) {
		var self = this;
		var rules = new Array();

		var navi = self.naviMenus[mid];
		if (typeof navi != "undefined" && navi != null) {
			$(navi.getTSNs(false)).each(function() {
				if (this.params) {
					rules[rules.length] = this.params;
				}
			});
		}

		if (typeof pID == "undefined") {
			if (rules.length == 0) {
				pID = self.masterModules[mid];
				if (typeof pID != "undefined" && pID != null && pID.length > 0) {
					var rows = self.gridSelected(self.grids[pID]);
					if (rows.length > 0) {
						rules[rules.length] = self.slaveFKs[mid] + ' in ' + rows.join(',');
					}
				}
			}
		} else {
			var rows = this.gridSelected(self.grids[pID]);
			if (rows.length == 1 && pProp.length > 0) {
				rules[rules.length] = pProp + " eq " + rows[0];
			}
		}

		return rules;
	};
	this.combSys = function(comb) {
		var obj = this.combMap[comb];
		if (!obj) {
			obj = {};
			this.combMap[comb] = obj;
		}
		return obj;
	};
	this.addGrid = function(options) {
		var self = this;
		var uiid = options.uiid;
		var mid = options.moduleID;
		var comb = options.comboboxID;
		var combType = options.comboboxType;
		if (comb) {
			options.onSelectRow = function(rowid, status) {
				var grid = self.combSys(comb).grid;
				var $comb = $("#" + comb);
				var $label = $(".label", $comb);
				var $value = $(".value", $comb);
				var rows = grid.jqGrid("getGridParam", "selarrrow");
				if (combType == "fk") {
					if (rows.length > 0) {
						$value.val(grid.getCell(rows[0], 1));
						$label.val(grid.getCell(rows[0], 2));
					} else {
						$value.val("");
						$label.val("");
					}
				} else if (combType == "expr") {
					var ids = new Array();
					var names = new Array();
					for ( var i = rows.length - 1; i >= 0; i--) {
						ids[i] = grid.getCell(rows[i], 1);
						names[i] = grid.getCell(rows[i], 2);
					}
					$value.val("entityGuid in " + ids.join(","));
					$label.val(names.join(", "));
				}
			}
			options.ondblClickRow = function() {
				self.combSys(comb).popup.hide();
			}
		}
		$(document).ready(function() {
			$.doTimeout(100, function() {

				options.beforeRequest = function() {
					var grid = null;
					if (comb) {
						grid = self.combSys(comb).grid;
					} else {
						grid = self.grids[mid];
					}

					if (typeof grid != "undefined" && grid != null) {
						var rules = new Array();
						if (comb) {
							var navi = self.combSys(comb).navi;
							if (typeof navi != "undefined" && navi != null) {
								$(navi.getTSNs(false)).each(function() {
									if (this.params) {
										rules[rules.length] = this.params;
									}
								});
							}
						} else {
							rules = self.getNaviRules(mid);
						}

						var ruleJson = null;
						if (rules != null && rules.length > 0) {
							ruleJson = '["' + rules.join('","') + '"]';
						}

						grid.jqGrid('setGridParam', {
							postData : {
								naviRules : ruleJson
							}
						});
					}
				}

				var grid = $("#" + uiid).jqGrid(options).jqGrid('navGrid', '#pg' + uiid, {
					search : options.search,
					refresh : true,
					add : false,
					edit : false,
					del : false
				}, {}, {}, {}, {
					multipleSearch : true,
					multipleGroup : true
				});

				if (comb) {
					self.combSys(comb).grid = grid;
				} else {
					self.grids[mid] = grid;
				}
			});
		});
	};
	this.addNaviMenu = function(options) {
		var self = this;
		var mid = options.moduleID;
		var comb = options.comboboxID;
		var combType = options.comboboxType;
		$(document).ready(function() {
			$.doTimeout(100, function() {
				options.oncheckboxclick = function(root, item, status) {
					if (comb && (combType == "expr" || combType == "c_expr")) {
						var rules = new Array();
						var names = new Array();
						var navi = self.combSys(comb).navi;
						if (typeof navi != "undefined" && navi != null) {
							$(navi.getTSNs(false)).each(function() {
								if (this.params) {
									rules[rules.length] = this.params;
									names[names.length] = this.text;
								}
							});
						}
						var $comb = $("#" + comb);
						var $value = $(".value", $comb);
						var $label = $(".label", $comb);
						$value.val(rules.join(', '));
						$label.val(names.join(', '));
					}
					if (status == 1) {
						var grid = null;
						if (comb) {
							grid = self.combSys(comb).grid;
						} else {
							grid = self.grids[mid];
						}

						if (grid)
							grid.trigger("reloadGrid");
					}
				};

				var navi = $("#" + options.uiid).treeview(options);
				if (comb) {
					self.combSys(comb).navi = navi;
				} else {
					self.naviMenus[mid] = navi;
				}
			});
		});
	};
	this.addTMenu = function(options) {
		var self = this;
		var uiid = options.uiid;
		var mid = options.moduleID;
		$.doTimeout(100, function() {
			options.execActionCallback = function(menu, item) {
				self.execActionCallback(menu, item);
			}

			self.tmenus[mid] = $("#" + uiid).demsyDDToolbar(options);
		})
	};
	this.tabs = function(options) {
		var self = this;
		self.config = options;
		var uiid = options.uiid;
		var mid = options.moduleID;

		options.add = function(event, ui) {
			self.onAddTab(ui.tab, ui.panel, ui.index);
		};
		options.remove = function(event, ui) {
			self.onRemoveTab(ui.tab, ui.panel, ui.index);
		};
		self.$tabs = $("#" + uiid).tabs(options).css("visibility", "visible");
		$("#" + uiid + " span.ui-icon-close").live("click", function() {
			var index = $("li", self.$tabs).index($(this).parent());
			self.$tabs.tabs("remove", index);
		});
	};
	this.updateEditor = function(form) {
		$(".richtext", form).each(function() {
			var editor = CKEDITOR.instances[this.id];
			editor.updateElement();
		});
	};
	this.reloadForm = function(form, options, params) {
		var self = this;
		form.block();
		$(".bzgrps", form).load(options.reloadUrl, params, function() {
			$.doTimeout(100, function() {
				self.bindForm(form, options);
			});
		});
	};
	this.bindForm = function(form, options) {
		var self = this;

		var content = $(".bzgrps", form);
		if ($.fn.datepicker) {
			$(".datepicker", content).datepicker();
		}
		if ($.fn.multiselect) {
			$(".multiselect", content).multiselect({
				minWidth : options.minWidth,
				height : 300
			});
		}
		if ($.fn.spinner) {
			$(".spinner", content).spinner({
				decimals : 0,
				stepping : 1,
				start : 0,
				incremental : true,
				currency : false,
				format : '%',
				items : []
			});
		}
		if ($.fn.demsyUploadCombobox) {
			$(".upload", content).demsyUploadCombobox();
		}
		if ($.fn.ColorPicker) {
			$(".color", content).each(function() {
				var ths = $(this);
				ths.css("background-color", ths.val());
			}).ColorPicker({
				zIndex : 99999,
				onSubmit : function(hsb, hex, rgb, el) {
					$(el).css("background-color", "#" + hex).val("#" + hex);
					$(el).ColorPickerHide();
				},
				onBeforeShow : function() {
					$(this).ColorPickerSetColor(this.value);
				}
			}).bind("keyup", function() {
				var ths = $(this);
				ths.css("background-color", ths.val());
				ths.ColorPickerSetColor(ths.val());
			});
		}
		var button = $(".ComboboxSys", content).each(function() {
			var me = $(this);
			var comb = me.attr("id");
			var url = me.attr("url");
			if (url) {
				var idx = url.indexOf("?");
				if (idx > 0) {
					url += "&comboboxID=" + comb;
				}
			}
			var $label = $(".label", me);
			var $span = $(".ui-icon", me);
			$span.click(function() {
				$label.click();
				return false;
			});
			self.combSys(comb).popup = $label.popup("", {
				width : "auto",
				url : url,
				loadding : "<img src='" + options.contextPath + "/themes2/images/busy.gif' />",
				hide : function() {
					var $cascade = $(".cascade", me);
					if ($cascade.length > 0) {
						self.updateEditor(form);
						self.reloadForm(form, options, form.serializeArray());
					}
				}
			})
		});
		if (typeof CKEDITOR != "undefined") {
			CKFinder.setupCKEditor(null, {
				basePath : options.contextPath + '/scripts2/ckfinder/',
				filebrowserBrowseUrl : options.contextPath + '/scripts2/ckfinder/ckfinder.html',
				filebrowserImageBrowseUrl : options.contextPath + '/scripts2/ckfinder/ckfinder.html?type=Images',
				filebrowserFlashBrowseUrl : options.contextPath + '/scripts2/ckfinder/ckfinder.html?type=Flash',
				filebrowserUploadUrl : options.contextPath + '/ul/ckfinder?command=QuickUpload&type=Files',
				filebrowserImageUploadUrl : options.contextPath + '/ul/ckfinder?command=QuickUpload&type=Images',
				filebrowserFlashUploadUrl : options.contextPath + '/ul/ckfinder?command=QuickUpload&type=Flash'
			});
			$(".richtext", content).each(function() {
				delete CKEDITOR.instances[this.id];
				CKEDITOR.replace(this.id);
			});
		}

		// cascading
		$("select.cascade", content).bind("change", function() {
			self.updateEditor(form);
			self.reloadForm(form, options, form.serializeArray());
		});
		$("input.cascade", content).bind("click", function() {
			self.updateEditor(form);
			self.reloadForm(form, options, form.serializeArray());
		});
	};
	this.form = function(options) {
		var self = this;
		$.doTimeout(100, function() {
			var form = $("#" + options.uiid);

			self.bindForm(form, options);
			// if ($.fn.button) {
			// $("input:submit", form).button();
			// }
			if (self.$tabs) {
				$(".returnButton", form).click(function() {
					self.hideDialog(options.moduleID, options.actionID);
					return false;
				}).show();
			}
			if (typeof options.submitUrl != "undefined" && options.submitUrl != null) {
				$(".submitButton", form).show().click(function() {
					form.block();
					self.updateEditor(form);
					var formEle = this.form;
					$.post(options.submitUrl, form.serializeArray(), function(jsonObj) {
						if (jsonObj.success) {
							self.reloadGrid(options.moduleID);
							if (self.$tabs) {
								self.hideDialog(options.moduleID, options.actionID);
							} else {
								alert(jsonObj.message);
								// window.document.location.reload();
								window.document.location.href = "/";
							}
						} else {
							alert(jsonObj.message);
						}
					}, "json");

					return false;
				});
			}

		});
	};
	this.onAddTab = function(a, p, i) {// anchor,panel,index
		var aid = this.currentAction.id;
		var mid = this.currentAction.moduleID;
		var tab = a.href.substring(a.href.indexOf("#") + 1);

		if (aid.indexOf("submdl_") == 0) {
			a.mid = mid;
			this.slaveFKs[mid] = this.currentAction.params;
			this.moduleTabs[mid] = tab;
			this.masterModules[mid] = this.currentAction.masterModuleID;
		} else {
			a.aid = aid;
			this.actionTabs[aid] = tab;
		}
	};
	this.onRemoveTab = function(a, p, i) {
		if (typeof a.mid != "undefined" && a.mid != null) {
			delete this.moduleTabs[a.mid];
			delete this.masterModules[a.mid];
			delete this.slaveFKs[a.mid];
			delete this.grids[a.mid];
			delete this.naviMenus[a.mid];
			delete this.tmenus[a.mid];
		} else if (typeof a.aid != "undefined" && a.aid != null) {
			delete this.actionTabs[a.aid];
		}
	};
};
if (typeof bizmodule == "undefined" || bizmodule == null) {
	bizmodule = new BizModule();
}
var SubModule = function($table, config) {
	this.$table = $table;
	this.config = config;
	var self = this;

	// 添加一条记录
	this.addItem = function(json) {
		var $item = $(self.config.itemTemplate.html()).appendTo(self.$table);

		if (self.config.onAddItem) {
			self.config.onAddItem(self, $item, json);
		} else {
			$(".input", $item).each(function() {
				if (json && json[this.name]) {
					$(this).val(json[this.name]);
				}
			});
		}

		self.bindEval($item);

		self.eval();

		return false;
	}
	// 删除多条记录
	this.delItems = function() {
		$(".check", self.$table).each(function() {
			if (this.checked) {
				var $item = $(this).parent().parent();

				if (self.config.onDelItem) {
					self.config.onDelItem(self, $item);
				}

				$item.remove();
			}
		});

		self.eval();

		return false;
	}
	// 删除一条记录
	this.delItem = function() {
		var $item = $(this).parent().parent();
		if (self.config.onDelItem) {
			self.config.onDelItem(self, $item);
		}
		$item.remove();
		self.eval();

		return false;
	}
	// 上下移动对记录进行排序
	this.moveItem = function(down) {
		var $checks = $(".check", self.$table);
		var $from = null;
		var $to = null;
		var $items = [];
		for ( var i = 0; i < $checks.length; i++) {
			if ($checks[i].checked) {
				if ($from == null && $items.length == 0 && i > 0) {
					$from = $($checks[i - 1]).parent().parent();
				}
				if (i < $checks.length - 1) {
					$to = $($checks[i + 1]).parent().parent();
				} else {
					$to = null;
				}
				var $item = $($checks[i]).parent().parent();
				$items[$items.length] = $item;
				$item.remove();
			}
		}

		if (down) {
			if ($to != null) {
				for ( var i = $items.length - 1; i >= 0; i--) {
					$items[i].insertAfter($to);
					self.bindEval($items[i]);
				}
			} else {
				for ( var i = 0; i < $items.length; i++) {
					$items[i].appendTo(self.$table);
					self.bindEval($items[i]);
				}
			}
		} else {
			if ($from != null) {
				for ( var i = 0; i < $items.length; i++) {
					$items[i].insertBefore($from);
					self.bindEval($items[i]);
				}
			} else {
				for ( var i = $items.length - 1; i >= 0; i--) {
					var items = $(".bzsubsys_item", self.$table);
					if (items.length == 0) {
						$items[i].appendTo(self.$table);
					} else {
						$items[i].insertBefore($(items[0]));
					}
					self.bindEval($items[i]);
				}
			}
		}

		self.eval();

		return false;
	}
	// 上移排序
	this.moveUpItem = function() {
		return self.moveItem(false);
	}
	// 下移排序
	this.moveDownItem = function() {
		return self.moveItem(true);
	}
	// 选中全部记录
	this.checkall = function() {
		var checked = this.checked;
		$(".check", self.$table).each(function() {
			this.checked = checked;
		})
	}
	// 绑定触发字段计算的输入框事件
	this.bindEval = function($item, igloreUpload) {
		$(".input", $item).change(self.eval);
		$(".select", $item).change(self.eval);
		$(".textarea", $item).change(self.eval);
		$(".op_btn_del_item", $item).click(self.delItem);
		if (!igloreUpload) {
			var $upload = $(".upload", $item);
			if ($upload.length > 0) {
				$("object", $upload).remove();
				if (self.uploadifyID) {
					self.uploadifyID++;
				} else {
					self.uploadifyID = 1;
				}
				var fieldID = self.config.field.attr("id");
				$(".uploadify", $item).attr("id", fieldID + "_file_new_" + self.uploadifyID);
				$(".progress", $item).attr("id", fieldID + "_progress_new_" + self.uploadifyID);
				$upload.demsyUploadCombobox();
			}
		}
	}
	// 计算JSON值并存放到字段里面
	this.eval = function() {
		var jsonArray = [];
		$(".bzsubsys_item", self.$table).each(function() {
			var $item = $(this);

			var jsonItem = null;
			if (self.config.onItemToJson) {
				jsonItem = self.config.onItemToJson(self, $item);
			} else {
				$(":input", $item).each(function() {
					var value = $(this).val();
					if (value.length > 0 && this.name && this.name.length > 0) {
						if (jsonItem == null) {
							jsonItem = {};
						}
						var dot = this.name.indexOf(".");
						if (dot > 0) {
							var nextJson = {};
							var name = this.name.substring(0, dot);
							var name2 = this.name.substring(dot + 1);
							nextJson[name2] = value;
							jsonItem[name] = nextJson;
						} else {
							jsonItem[this.name] = value;
						}
					}
				});
			}

			if (jsonItem != null)
				jsonArray[jsonArray.length] = jsonItem;
		});

		config.field.val($.toJSON(jsonArray));
	}
	// 初始化
	this.init = function() {
		self.bindEval($table, true);
		$(".submitButton").click(self.eval);
		$(".op_btn_add", $table).click(self.addItem);
		$(".op_btn_del", $table).click(self.delItems);
		$(".op_btn_del_item", $table).click(self.delItem);
		$(".op_btn_up", $table).click(self.moveUpItem);
		$(".op_btn_down", $table).click(self.moveDownItem);
		$(".op_checkall", $table).click(self.checkall);
	}
	self.init();
}
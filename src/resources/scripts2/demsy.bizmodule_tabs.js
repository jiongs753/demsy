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

	this.hideDialog = function(moduleID, actionID) {
		this.$tabs.tabs("remove", this.actionTabs[actionID]);
		this.$tabs.tabs("select", this.moduleTabs[moduleID] || 0);
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
		var rows = grid.jqGrid("getGridParam", "selarrrow");
		var ids = new Array();
		for ( var i = rows.length - 1; i >= 0; i--)
			ids[i] = grid.getCell(rows[i], 2);

		return ids;
	}
	this.execActionCallback = function(tmenu, item) {
		var self = this;

		this.currentAction = item;

		var mID = item.moduleID;
		var aID = item.id;
		var grid = this.grids[mID];
		var action = item.mode + ":";
		var target = null;
		if (typeof item.target != "undefined" && item.target.length > 0) {
			target = item.target;
		}

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
			var rows = this.gridSelected(grid);
			if (rows.length == 1) {
				action += rows[0];
			} else {
				alert("请先选中一条记录");
				return;
			}
			break
		case 2:// sub module
			break
		default:
			var rows = this.gridSelected(grid);
			if (rows.length > 0) {
				if (type == 299 && !confirm("你确定要【" + item.text + "】选中的记录吗?")) {
					return;
				}
				action += rows.join(",");
			} else {
				if (!confirm("尚未选中任何记录，你确定要对分页列表中的所有记录执行【" + item.text + "】操作吗?")) {
					return;
				}
				params = this.gridParams(grid);
			}
		}

		var url = tmenu.contextPath + tmenu.type + type + "/" + mID + (target == null ? ":/" : "/") + action;
		if (params.length > 0) {
			url += "?" + params.join("&");
		}

		if (type > 200) {// 立即执行
			if (item.params.length > 0) {
				if (params.length > 0) {
					url += "&data." + item.params;
				} else {
					url += "?data." + item.params;
				}
			}
			$.getJSON(url, "", function(jsonObj) {
				alert(jsonObj.message);
				if (jsonObj.success) {
					self.reloadGrid(mID);
				}
			});
		} else if (type == 2) {// sub module
			var pID = item.masterModuleID;
			var rows = this.gridSelected(this.grids[pID]);
			if (rows.length > 0) {
				naviRules = '["' + item.params + ' in ' + rows.join(',') + '"]';
				url += "?naviRules=" + naviRules;
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
				if ($("#" + this.actionTabs[aID]).length > 0) {
					this.$tabs.tabs("url", this.actionTabs[aID], url);
				} else {
					this.$tabs.tabs("add", url, item.text);
				}
				this.$tabs.tabs("select", this.actionTabs[aID]);
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
	this.addGrid = function(options) {
		var self = this;
		$(document).ready(function() {
			$.doTimeout(100, function() {
				var uiid = options.uiid;
				var mid = options.moduleID;

				options.beforeRequest = function() {
					var grid = self.grids[mid];
					if (typeof grid == "undefined" || grid == null) {
						return;
					}
					var rules = self.getNaviRules(mid);
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

				self.grids[mid] = $("#" + uiid).jqGrid(options).jqGrid('navGrid', '#pg' + uiid, {
					search : true,
					refresh : true,
					add : false,
					edit : false,
					del : false
				}, {}, {}, {}, {
					multipleSearch : true,
					multipleGroup : true
				});
			})
		});
	};
	this.addNaviMenu = function(options) {
		var self = this;
		$(document).ready(function() {
			$.doTimeout(100, function() {
				var uiid = options.uiid;
				var mid = options.moduleID;

				options.oncheckboxclick = function(root, item, status) {
					if (status != 1) {
						return;
					}
					self.grids[mid].trigger("reloadGrid");
				};

				self.naviMenus[mid] = $("#" + uiid).treeview(options);
			})
		});
	};
	this.addTMenu = function(options) {
		var self = this;
		$(document).ready(function() {
			$.doTimeout(100, function() {
				var uiid = options.uiid;
				var mid = options.moduleID;

				options.execActionCallback = function(menu, item) {
					self.execActionCallback(menu, item);
				}

				self.tmenus[mid] = $("#" + uiid).demsyDDToolbar(options);
			})
		});
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
		self.$tabs = $("#" + uiid).tabs(options).show();
		$("#" + uiid + " span.ui-icon-close").live("click", function() {
			var index = $("li", self.$tabs).index($(this).parent());
			self.$tabs.tabs("remove", index);
		});
	};
	this.reloadForm = function(mid, aid, form, params) {
		this.$tabs.tabs("load", this.actionTabs[aid], {
			type : 'POST',
			data : params
		});
	}, this.updateEditor = function(form) {
		$(".richtext", form).each(function() {
			var editor = CKEDITOR.instances[this.id];
			editor.updateElement();
		});
	}, this.form = function(options) {
		var self = this;
		$.doTimeout(10, function() {
			var uiid = options.uiid;
			var mid = options.moduleID;
			var aid = options.actionID;

			var form = $("#" + uiid);

			$("input:submit", form).button();
			if ($.fn.datepicker) {
				$(".datepicker", form).datepicker();
			}
			if ($.fn.multiselect) {
				$(".multiselect", form).multiselect({
					minWidth : options.minWidth,
					height : 300
				});
			}
			if (CKEDITOR) {
				$(".richtext", form).each(function() {
					delete CKEDITOR.instances[this.id];
					CKEDITOR.replace(this.id);
				});
			}

			// cascading
			$("select.cascade", form).bind("change", function() {
				self.updateEditor(form);
				self.reloadForm(mid, aid, form, form.serialize());
				return false;
			});
			$("input.cascade", form).bind("click", function() {
				self.updateEditor(form);
				self.reloadForm(mid, aid, form, form.serialize());
				return false;
			});

			$(".returnButton", form).click(function() {
				self.hideDialog(mid, aid);
				return false;
			}).show();
			if (typeof options.submitUrl != "undefined" && options.submitUrl != null) {
				$(".submitButton", form).show().click(function() {
					self.updateEditor(form);
					$.post(options.submitUrl, $(this.form).serialize(), function(jsonObj) {
						alert(jsonObj.message);
						if (jsonObj.success) {
							self.reloadForm(mid, aid, form, "");
							self.reloadGrid(mid);
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

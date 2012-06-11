/**
 * @description {Class} wdTree This is the main class of wdTree.
 */
(function($) {
	$.fn.swapClass = function(c1, c2) {
		return this.removeClass(c1).addClass(c2);
	};
	$.fn.switchClass = function(c1, c2) {
		if (this.hasClass(c1)) {
			return this.swapClass(c1, c2);
		} else {
			return this.swapClass(c2, c1);
		}
	};
	$.fn.treeview = function(settings) {
		var dfop = {
			method : "POST",
			datatype : "json",
			url : false,
			cbiconpath : "./css/images/icons",
			icons : [ "/checkbox_0.gif", "/checkbox_1.gif", "/checkbox_2.gif" ],
			showcheck : false,
			oncheckboxclick : false,
			onnodeclick : false,
			cascadecheck : true,
			data : null,
			clicktoggle : true,
			theme : "bbit-tree-lines"
		};

		$.extend(dfop, settings);
		var treenodes = dfop.data;
		var me = $(this);
		var id = me.attr("id");
		if (id == null || id == "") {
			id = "bbtree" + new Date().getTime();
			me.attr("id", id);
		}

		var html = [];
		buildtree(dfop.data, html);
		me.addClass("bbit-tree").html(html.join(""));
		InitEvent(me);
		html = null;
		if (dfop.showcheck) {
			for ( var i = 0; i < 3; i++) {
				var im = new Image();
				im.src = dfop.cbiconpath + dfop.icons[i];
			}
		}

		function buildtree(data, ht) {
			ht.push("<div class='bbit-tree-bwrap'>");
			ht.push("<div class='bbit-tree-body'>");
			ht.push("<ul class='bbit-tree-root ", dfop.theme, "'>");
			if (data && data.length > 0) {
				var l = data.length;
				for ( var i = 0; i < l; i++) {
					buildnode(data[i], ht, 0, i, i == l - 1);
				}
			} else {
				asnyloadc(null, false, function(data) {
					if (data && data.length > 0) {
						treenodes = data;
						dfop.data = data;
						var l = data.length;
						for ( var i = 0; i < l; i++) {
							buildnode(data[i], ht, 0, i, i == l - 1);
						}
					}
				});
			}
			ht.push("</ul>");
			ht.push("</div>");
			ht.push("</div>");
		}
		// endregion
		function buildnode(nd, ht, deep, path, isend) {
			nd.isend = isend;
			var nid = nd.id.replace(/[^\w]/gi, "_");
			ht.push("<li class='bbit-tree-node'>");
			ht.push("<div id='", id, "_", nid, "' tpath='", path, "' unselectable='on' title='", nd.text, "'");
			var cs = [];
			cs.push("bbit-tree-node-el");
			cs.push("ui-tree-state-default");
			if (nd.hasChildren) {
				cs.push(nd.isexpand ? "bbit-tree-node-expanded" : "bbit-tree-node-collapsed");
			} else {
				cs.push("bbit-tree-node-leaf");
			}
			if (nd.classes) {
				cs.push(nd.classes);
			}

			ht.push(" class='", cs.join(" "), "'>");
			// span indent
			ht.push("<span class='bbit-tree-node-indent'>");
			if (deep == 1) {
				ht.push("<img class='bbit-tree-elbow-line' src='" + dfop.cbiconpath + "/s.gif'/>");
			} else if (deep > 1) {
				ht.push("<img class='bbit-tree-elbow-line' src='" + dfop.cbiconpath + "/s.gif'/>");
				for ( var j = 1; j < deep; j++) {
					if (nd.parent.isend && j == deep - 1) {
						ht.push("<img class='bbit-tree-icon' src='" + dfop.cbiconpath + "/s.gif'/>");
					} else
						ht.push("<img class='bbit-tree-elbow-line' src='" + dfop.cbiconpath + "/s.gif'/>");
				}
			}
			ht.push("</span>");
			// img
			cs.length = 0;
			if (nd.hasChildren) {
				if (nd.isexpand) {
					cs.push(isend ? "bbit-tree-elbow-end-minus" : "bbit-tree-elbow-minus");
				} else {
					cs.push(isend ? "bbit-tree-elbow-end-plus" : "bbit-tree-elbow-plus");
				}
			} else {
				cs.push(isend ? "bbit-tree-elbow-end" : "bbit-tree-elbow");
			}
			ht.push("<img class='bbit-tree-ec-icon ", cs.join(" "), "' src='" + dfop.cbiconpath + "/s.gif'/>");
			ht.push("<img class='bbit-tree-node-icon' src='" + dfop.cbiconpath + "/s.gif'/>");
			// checkbox
			if (dfop.showcheck && nd.showcheck) {
				if (nd.checkstate == null || nd.checkstate == undefined) {
					nd.checkstate = 0;
				}
				ht.push("<img  id='", id, "_", nid, "_cb' class='bbit-tree-node-cb' src='", dfop.cbiconpath, dfop.icons[nd.checkstate], "'/>");
			}
			// a
			ht.push("<a hideFocus class='bbit-tree-node-anchor' tabIndex=1 " + (nd.target ? ("target='" + nd.target + "'") : '') + " href='" + (nd.href ? nd.href : "javascript:void(0);") + "'>");
			ht.push("<span unselectable='on'>", nd.text, "</span>");
			ht.push("</a>");
			ht.push("</div>");
			// Child
			if (nd.hasChildren) {
				if (nd.isexpand) {
					ht.push("<ul  class='bbit-tree-node-ct'  style='z-index: 0; position: static; visibility: visible; top: auto; left: auto;'>");
					if (nd.ChildNodes) {
						var l = nd.ChildNodes.length;
						for ( var k = 0; k < l; k++) {
							nd.ChildNodes[k].parent = nd;
							buildnode(nd.ChildNodes[k], ht, deep + 1, path + "." + k, k == l - 1);
						}
					}
					ht.push("</ul>");
				} else {
					ht.push("<ul style='display:none;'></ul>");
				}
			}
			ht.push("</li>");
			nd.render = true;
		}
		function getItem(path) {
			var ap = path.split(".");
			var t = treenodes;
			for ( var i = 0; i < ap.length; i++) {
				if (i == 0) {
					t = t[ap[i]];
				} else {
					t = t.ChildNodes[ap[i]];
				}
			}
			return t;
		}
		function check(item, state, type) {
			var pstate = item.checkstate;
			if (type == 1) {
				item.checkstate = state;
			}
			if (item.hasChildren) {
				// go to childnodes
				var length = item.ChildNodes.length;
				var count = 0;
				for (i = 0; i < length; i++) {
					var s = item.ChildNodes[i].checkstate;
					if (s == 1 || s == 2) {
						count++;
					}
				}
				if (count > 0 && count < length) {
					// enabled cascade
					if (item.cascadecheck != false) {
						item.checkstate = 2;
					} else {
						if (item.checkstate != 1) {
							item.checkstate = 2;
						}
					}
				} else {
					if (item.cascadecheck != false) {
						if (count == 0) {
							item.checkstate = 0;
						} else {
							item.checkstate = 1;
						}
					} else if (item.checkstate != 1) {
						if (count == 0) {
							item.checkstate = 0;
						} else {
							item.checkstate = 2;
						}
					}
				}
			}
			// change show
			if (item.render && pstate != item.checkstate) {
				var nid = item.id.replace(/[^\w]/gi, "_");
				var et = $("#" + id + "_" + nid + "_cb");
				if (et.length == 1) {
					et.attr("src", dfop.cbiconpath + dfop.icons[item.checkstate]);
				}
			}
		}
		// iterate all children nodes
		function cascade(fn, item, state, auto) {
			if (auto == true ? item.cascadecheck != false : true) {
				if (item.ChildNodes != null && item.ChildNodes.length > 0) {
					var cs = item.ChildNodes;
					for ( var i = 0, len = cs.length; i < len; i++) {
						cascade(fn, cs[i], state);
					}
				}
			}
			fn(item, state, 1);
		}
		// bubble to parent
		function bubble(fn, item, state) {
			var p = item.parent;
			while (p) {
				fn(p, state, 0);
				p = p.parent;
			}
		}
		function nodeclick(e) {
			var path = $(this).attr("tpath");
			var et = e.target || e.srcElement;
			var item = getItem(path);
			if (et.tagName == "IMG") {
				// + if collapsed, expend it
				if ($(et).hasClass("bbit-tree-elbow-plus") || $(et).hasClass("bbit-tree-elbow-end-plus")) {
					var ul = $(this).next(); // "bbit-tree-node-ct"
					if (ul.hasClass("bbit-tree-node-ct")) {
						ul.show();
					} else {
						var deep = path.split(".").length;
						if (item.complete) {
							item.ChildNodes != null && asnybuild(item.ChildNodes, deep, path, ul, item);
						} else {
							$(this).addClass("bbit-tree-node-loading");
							asnyloadc(item, true, function(data) {
								item.complete = true;
								item.ChildNodes = data;
								asnybuild(data, deep, path, ul, item);
							});
						}
					}
					if ($(et).hasClass("bbit-tree-elbow-plus")) {
						$(et).swapClass("bbit-tree-elbow-plus", "bbit-tree-elbow-minus");
					} else {
						$(et).swapClass("bbit-tree-elbow-end-plus", "bbit-tree-elbow-end-minus");
					}
					$(this).swapClass("bbit-tree-node-collapsed", "bbit-tree-node-expanded");
				}
				// if expended, collapse it
				else if ($(et).hasClass("bbit-tree-elbow-minus") || $(et).hasClass("bbit-tree-elbow-end-minus")) {
					$(this).next().hide();
					if ($(et).hasClass("bbit-tree-elbow-minus")) {
						$(et).swapClass("bbit-tree-elbow-minus", "bbit-tree-elbow-plus");
					} else {
						$(et).swapClass("bbit-tree-elbow-end-minus", "bbit-tree-elbow-end-plus");
					}
					$(this).swapClass("bbit-tree-node-expanded", "bbit-tree-node-collapsed");
				} else if ($(et).hasClass("bbit-tree-node-cb")) {// click on
					// checkbox
					var s = item.checkstate != 1 ? 1 : 0;
					if (dfop.cascadecheck) {
						cascade(check, item, s, item.cascadecheck == false);
						bubble(check, item, s);
					} else {
						check(item, s, 1);
					}
					if (dfop.oncheckboxclick) {
						dfop.oncheckboxclick.call(et, me, item, 0);
					}
				}
			} else {
				dfop.citem = item;
				if (item.cascadecheck != false) {
					var nid = item.id.replace(/[^\w]/gi, "_");
					var img = $("#" + id + "_" + nid + " img.bbit-tree-ec-icon");
					if (img.length > 0) {
						img.click();
					}
				}
				if (!item.hasChildren || item.cascadecheck == false) {
					uncheckAll();

					check(item, 1, 1);
					bubble(check, item, 1);
					if (dfop.oncheckboxclick) {
						dfop.oncheckboxclick.call(et, me, item, 1);
					}
					if (dfop.onnodeclick) {
						dfop.onnodeclick.call(this, item);
					}
				}
			}
		}
		function nodedblclick(e) {
			var path = $(this).attr("tpath");
			var et = e.target || e.srcElement;
			var item = getItem(path);
			if (et.tagName == "IMG") {
				if ($(et).hasClass("bbit-tree-node-cb")) {
					var s = item.checkstate != 1 ? 1 : 0;
					if (dfop.cascadecheck) {
						cascade(check, item, s);
						bubble(check, item, s);
					} else {
						check(item, s, 1);
					}
					if (dfop.oncheckboxclick) {
						dfop.oncheckboxclick.call(et, me, item, 1);
					}
				}
			} else {
				//if (!item.hasChildren || item.cascadecheck == false) {
					//if (dfop.oncheckboxclick) {
						//dfop.oncheckboxclick.call(et, me, item, 1);
					//}
				//}
			}
		}
		function expandnode() {
			var item = this;
			var nid = item.id.replace(/[^\w]/gi, "_");
			var img = $("#" + id + "_" + nid + " img.bbit-tree-ec-icon");
			if (img.length > 0) {
				img.click();
			}
		}
		function asnybuild(nodes, deep, path, ul, pnode) {
			var l = nodes.length;
			if (l > 0) {
				var ht = [];
				for ( var i = 0; i < l; i++) {
					nodes[i].parent = pnode;
					buildnode(nodes[i], ht, deep, path + "." + i, i == l - 1);
				}
				ul.html(ht.join(""));
				ht = null;
				InitEvent(ul);
			}
			ul.addClass("bbit-tree-node-ct").css({
				"z-index" : 0,
				position : "static",
				visibility : "visible",
				top : "auto",
				left : "auto",
				display : ""
			});
			ul.prev().removeClass("bbit-tree-node-loading");
		}
		function asnyloadc(pnode, isAsync, callback) {
			if (dfop.url) {
				if (pnode && pnode != null)
					var param = builparam(pnode);
				$.ajax({
					type : dfop.method,
					url : dfop.url,
					data : param,
					async : isAsync,
					dataType : dfop.datatype,
					success : callback,
					error : function(e) {
						alert("error occur!");
					}
				});
			}
		}
		function builparam(node) {
			var p = [ {
				name : "id",
				value : encodeURIComponent(node.id)
			}, {
				name : "text",
				value : encodeURIComponent(node.text)
			}, {
				name : "value",
				value : encodeURIComponent(node.value)
			}, {
				name : "checkstate",
				value : node.checkstate
			} ];
			return p;
		}
		function bindevent() {
			$(this).hover(function() {
				$(this).addClass("bbit-tree-selected");
			}, function() {
				$(this).removeClass("bbit-tree-selected").addClass("ui-tree-state-default");
			}).dblclick(nodedblclick).click(nodeclick).find("img.bbit-tree-ec-icon").each(function(e) {
				if (!$(this).hasClass("bbit-tree-elbow")) {
					$(this).hover(function() {
						$(this).parent().addClass("bbit-tree-ec-over");
					}, function() {
						$(this).parent().removeClass("bbit-tree-ec-over");
					});
				}
			});
		}
		function InitEvent(parent) {
			var nodes = $("li.bbit-tree-node>div", parent);
			nodes.each(bindevent);
		}
		function reflash(itemId) {
			var nid = itemId.replace(/[^\w-]/gi, "_");
			var node = $("#" + id + "_" + nid);
			if (node.length > 0) {
				node.addClass("bbit-tree-node-loading");
				var isend = node.hasClass("bbit-tree-elbow-end") || node.hasClass("bbit-tree-elbow-end-plus") || node.hasClass("bbit-tree-elbow-end-minus");
				var path = node.attr("tpath");
				var deep = path.split(".").length;
				var item = getItem(path);
				if (item) {
					asnyloadc(item, true, function(data) {
						item.complete = true;
						item.ChildNodes = data;
						item.isexpand = true;
						if (data && data.length > 0) {
							item.hasChildren = true;
						} else {
							item.hasChildren = false;
						}
						var ht = [];
						buildnode(item, ht, deep - 1, path, isend);
						ht.shift();
						ht.pop();
						var li = node.parent();
						li.html(ht.join(""));
						ht = null;
						InitEvent(li);
						bindevent.call(li.find(">div"));
					});
				}
			} else {
				// node not created yet
			}
		}
		function getck(items, c, fn) {
			for ( var i = 0, l = items.length; i < l; i++) {
				(items[i].showcheck == true && items[i].checkstate == 1) && c.push(fn(items[i]));
				if (items[i].ChildNodes != null && items[i].ChildNodes.length > 0) {
					getck(items[i].ChildNodes, c, fn);
				}
			}
		}
		function getCkAndHalfCk(items, c, fn) {
			for ( var i = 0, l = items.length; i < l; i++) {
				(items[i].showcheck == true && (items[i].checkstate == 1 || items[i].checkstate == 2)) && c.push(fn(items[i]));
				if (items[i].ChildNodes != null && items[i].ChildNodes.length > 0) {
					getCkAndHalfCk(items[i].ChildNodes, c, fn);
				}
			}
		}
		function uncheckAll() {
			for ( var i = treenodes.length - 1; i >= 0; i--) {
				cascade(check, treenodes[i], 0);
				bubble(check, treenodes[i], 0);
			}
		}
		me[0].t = {
			uncheckAll : function() {
				uncheckAll();
			},
			getSelectedNodes : function(gethalfchecknode) {
				var s = [];
				if (gethalfchecknode) {
					getCkAndHalfCk(treenodes, s, function(item) {
						return item;
					});
				} else {
					getck(treenodes, s, function(item) {
						return item;
					});
				}
				return s;
			},
			getSelectedValues : function() {
				var s = [];
				getck(treenodes, s, function(item) {
					return item.value;
				});
				return s;
			},
			getCurrentItem : function() {
				return dfop.citem;
			},
			reflash : function(itemOrItemId) {
				var id;
				if (typeof (itemOrItemId) == "string") {
					id = itemOrItemId;
				} else {
					id = itemOrItemId.id;
				}
				reflash(id);
			}
		};
		return me;
	};
	// get all checked nodes, and put them into array. no hierarchy
	$.fn.getCheckedNodes = function() {
		if (this[0].t) {
			return this[0].t.getSelectedValues();
		}
		return null;
	};
	$.fn.uncheckAll = function() {
		if (this[0].t) {
			return this[0].t.uncheckAll();
		}
		return null;
	};
	$.fn.getTSNs = function(gethalfchecknode) {
		if (this[0].t) {
			return this[0].t.getSelectedNodes(gethalfchecknode);
		}
		return null;
	};
	$.fn.getCurrentNode = function() {
		if (this[0].t) {
			return this[0].t.getCurrentItem();
		}
		return null;
	};
	$.fn.reflash = function(ItemOrItemId) {
		if (this[0].t) {
			return this[0].t.reflash(ItemOrItemId);
		}
	};

})(jQuery);
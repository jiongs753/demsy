(function($, jDemsy) {
	$.addFlex = function(table, options) {
		var time0 = new Date().getTime();

		if (table.grid)
			return false; // 如果Grid已经存在则返回

		var grid = {
			hset : {},
			/*
			 * 调整列的位置
			 */
			rePosDrag : function() {

				var colDragLeft = 0 - this.hDiv.scrollLeft;
				if (this.hDiv.scrollLeft > 0)
					colDragLeft -= Math.floor(options.cgwidth / 2);

				$(grid.cDrag).css({
					top : grid.hDiv.offsetTop + 1
				});
				var cdpad = this.cdpad;

				$('div', grid.cDrag).hide();
				$('thead tr:first th:visible', this.hDiv).each(function(i) {
					if ($(this).css("display") == "none") {
						return;
					}

					// 表头宽度
					var cdpos = parseInt($('div', this).width());
					var ppos = cdpos;
					if (colDragLeft == 0)
						colDragLeft -= Math.floor(options.cgwidth / 2);

					cdpos = cdpos + colDragLeft + cdpad;

					$('div:eq(' + i + ')', grid.cDrag).css({
						'left' : cdpos + 'px'
					}).show();

					colDragLeft = cdpos;
				});
			},
			fixHeight : function(newH) {
				newH = false;
				if (!newH)
					newH = $(grid.bDiv).height();
				var hdHeight = $(this.hDiv).height();
				$('div', this.cDrag).each(function() {
					$(this).height(newH + hdHeight);
				});

				var nd = parseInt($(grid.nDiv).height());

				if (nd > newH)
					$(grid.nDiv).height(newH).width(200);
				else
					$(grid.nDiv).height('auto').width('auto');

				$(grid.block).css({
					height : newH,
					marginBottom : (newH * -1)
				});

				var hrH = grid.bDiv.offsetTop + newH;
				if (options.height != 'auto' && options.resizable)
					hrH = grid.vGrip.offsetTop;
				$(grid.hGrip).css({
					height : hrH
				});

			},
			dragStart : function(dragtype, e, obj) { // default drag function
				// start

				if (dragtype == 'colresize') // column resize
				{
					$(grid.nDiv).hide();
					$(grid.nBtn).hide();
					var n = $('div', this.cDrag).index(obj);
					// var ow = $('th:visible div:eq(' + n + ')',
					// this.hDiv).width();
					var ow = $('th:visible:eq(' + n + ') div', this.hDiv).width();
					$(obj).addClass('dragging').siblings().hide();
					$(obj).prev().addClass('dragging').show();

					this.colresize = {
						startX : e.pageX,
						ol : parseInt(obj.style.left),
						ow : ow,
						n : n
					};
					$('body').css('cursor', 'col-resize');
				} else if (dragtype == 'vresize') // table resize
				{
					var hgo = false;
					$('body').css('cursor', 'row-resize');
					if (obj) {
						hgo = true;
						$('body').css('cursor', 'col-resize');
					}
					this.vresize = {
						h : options.height,
						sy : e.pageY,
						w : options.width,
						sx : e.pageX,
						hgo : hgo
					};

				}

				else if (dragtype == 'colMove') // column header drag
				{
					$(grid.nDiv).hide();
					$(grid.nBtn).hide();
					this.hset = $(this.hDiv).offset();
					this.hset.right = this.hset.left + $('table', this.hDiv).width();
					this.hset.bottom = this.hset.top + $('table', this.hDiv).height();
					this.dcol = obj;
					this.dcoln = $('th', this.hDiv).index(obj);

					this.colCopy = document.createElement("div");
					this.colCopy.className = "colCopy";
					this.colCopy.innerHTML = obj.innerHTML;
					if ($.browser.msie) {
						this.colCopy.className = "colCopy ie";
					}

					$(this.colCopy).css({
						position : 'absolute',
						float : 'left',
						display : 'none',
						textAlign : obj.align
					});
					$('body').append(this.colCopy);
					$(this.cDrag).hide();

				}

				$('body').noSelect();

			},
			reSize : function() {
				this.gDiv.style.width = options.width;
				this.bDiv.style.height = options.height;
			},
			dragMove : function(e) {

				if (this.colresize) // column resize
				{
					var n = this.colresize.n;
					var diff = e.pageX - this.colresize.startX;
					var nleft = this.colresize.ol + diff;
					var nw = this.colresize.ow + diff;
					if (nw > options.minWidth) {
						$('div:eq(' + n + ')', this.cDrag).css('left', nleft);
						this.colresize.nw = nw;
					}
				} else if (this.vresize) // table resize
				{
					var v = this.vresize;
					var y = e.pageY;
					var diff = y - v.sy;
					if (!options.defwidth)
						options.defwidth = options.width;
					if (options.width != 'auto' && !options.nohresize && v.hgo) {
						var x = e.pageX;
						var xdiff = x - v.sx;
						var newW = v.w + xdiff;
						if (newW > options.defwidth) {
							this.gDiv.style.width = newW + 'px';
							options.width = newW;
						}
					}
					var newH = v.h + diff;
					if ((newH > options.minHeight || options.height < options.minHeight) && !v.hgo) {
						this.bDiv.style.height = newH + 'px';
						options.height = newH;
						this.fixHeight(newH);
					}
					v = null;
				} else if (this.colCopy) {
					$(this.dcol).addClass('thMove').removeClass('thOver');
					if (e.pageX > this.hset.right || e.pageX < this.hset.left || e.pageY > this.hset.bottom || e.pageY < this.hset.top) {
						// this.dragEnd();
						$('body').css('cursor', 'move');
					} else
						$('body').css('cursor', 'pointer');

					$(this.colCopy).css({
						top : e.pageY + 10,
						left : e.pageX + 20,
						display : 'block'
					});
				}

			},
			dragEnd : function() {
				if (this.colresize) {
					var n = this.colresize.n;
					var nw = this.colresize.nw;
					// $('th:visible div:eq(' + n + ')', this.hDiv).css('width',
					// nw);
					$('th:visible:eq(' + n + ') div', this.hDiv).css('width', nw);

					$('tr', this.bDiv).each(function() {
						// $('td:visible div:eq(' + n + ')', this).css('width',
						// nw);
						$('td:visible:eq(' + n + ') div', this).css('width', nw);
					});
					this.hDiv.scrollLeft = this.bDiv.scrollLeft;
					$('div:eq(' + n + ')', this.cDrag).siblings().show();
					$('.dragging', this.cDrag).removeClass('dragging');
					this.rePosDrag();
					this.fixHeight();
					this.colresize = false;
				} else if (this.vresize) {
					this.vresize = false;
				} else if (this.colCopy) {
					$(this.colCopy).remove();
					if (this.dcolt != null) {
						if (this.dcoln > this.dcolt) {
							$('th:eq(' + this.dcolt + ')', this.hDiv).before(this.dcol);
						} else {
							$('th:eq(' + this.dcolt + ')', this.hDiv).after(this.dcol);
						}
						this.switchCol(this.dcoln, this.dcolt);
						$(this.cdropleft).remove();
						$(this.cdropright).remove();
						this.rePosDrag();
					}
					this.dcol = null;
					this.hset = null;
					this.dcoln = null;
					this.dcolt = null;
					this.colCopy = null;
					$('.thMove', this.hDiv).removeClass('thMove');
					$(this.cDrag).show();
				}
				$('body').css('cursor', 'default');
				$('body').noSelect(false);
			},
			toggleCol : function(cid, visible) {
				var ncol = $("th[axis='col" + cid + "']", this.hDiv)[0];
				var n = $('thead th', grid.hDiv).index(ncol);
				var cb = $('input[value=' + cid + ']', grid.nDiv)[0];
				if (visible == null) {
					visible = ncol.hide;
				}
				if ($('input:checked', grid.nDiv).length < options.minColToggle && !visible)
					return false;
				if (visible) {
					ncol.hide = false;
					$(ncol).show();
					cb.checked = true;
				} else {
					ncol.hide = true;
					$(ncol).hide();
					cb.checked = false;
				}
				$('tbody tr', table).each(function() {
					if (visible)
						$('td:eq(' + n + ')', this).show();
					else
						$('td:eq(' + n + ')', this).hide();
				});
				this.rePosDrag();
				if (options.onToggleCol)
					options.onToggleCol(cid, visible);
				return visible;
			},
			switchCol : function(cdrag, cdrop) { // switch columns
				$('tbody tr', table).each(function() {
					if (cdrag > cdrop)
						$('td:eq(' + cdrop + ')', this).before($('td:eq(' + cdrag + ')', this));
					else
						$('td:eq(' + cdrop + ')', this).after($('td:eq(' + cdrag + ')', this));
				});
				// switch order in nDiv
				if (cdrag > cdrop)
					$('tr:eq(' + cdrop + ')', this.nDiv).before($('tr:eq(' + cdrag + ')', this.nDiv));
				else
					$('tr:eq(' + cdrop + ')', this.nDiv).after($('tr:eq(' + cdrag + ')', this.nDiv));
				if ($.browser.msie && $.browser.version < 7.0)
					$('tr:eq(' + cdrop + ') input', this.nDiv)[0].checked = true;
				this.hDiv.scrollLeft = this.bDiv.scrollLeft;
			},
			scroll : function() {
				this.hDiv.scrollLeft = this.bDiv.scrollLeft;
				this.rePosDrag();
			},
			hideLoading : function() {
				$('.pReload', this.pDiv).removeClass('loading');
				if (options.hideOnSubmit)
					$(grid.block).remove();
				$('.pPageStat', this.pDiv).html(options.errormsg);
				this.loading = false;
			},
			addData : function(data) { // parse data
				if (options.preProcess) {
					data = options.preProcess(data);
				}
				if (options.usePager) {
					$('.pReload', this.pDiv).removeClass('loading');
				}
				this.loading = false;

				if (!data) {
					if (options.usePager) {
						$('.pPageStat', this.pDiv).html(options.errormsg);
					}
					return false;
				}
				var temp = options.total;
				if (options.dataType == 'xml') {
					options.total = +$('rows total', data).text();
				} else {
					options.total = data.total;
				}
				if (options.total < 0) {
					options.total = temp;
				}
				if (options.total == 0) {
					$('tr, a, td, div', table).unbind();
					$(table).empty();
					options.pages = 1;
					options.page = 1;
					this.buildpager();
					$('.pPageStat', this.pDiv).html(options.nomsg);
					if (options.hideOnSubmit)
						$(grid.block).remove();
					return false;
				}

				options.pages = Math.ceil(options.total / options.rp);

				if (options.dataType == 'xml') {
					options.page = +$('rows page', data).text();
				} else {
					options.page = data.page;
				}
				if (options.usePager) {
					this.buildpager();
				}

				var ths = $('thead tr:first th', grid.hDiv);
				var thsdivs = $('thead tr:first th div', grid.hDiv);
				var tbhtml = [];
				tbhtml.push("<tbody>");
				if (options.dataType == 'json') {
					if (data.rows != null) {
						$.each(data.rows, function(i, row) {
							tbhtml.push("<tr id='", "row", row.id, "'");

							if (i % 2 && options.striped) {
								tbhtml.push(" class='erow'");
							}
							if (options.rowbinddata) {
								tbhtml.push("ch='", row.cell.join("_FG$SP_"), "'");
							}
							tbhtml.push(">");
							var trid = row.id;
							$(ths).each(function(j) {
								var tddata = "";
								var tdclass = "";
								tbhtml.push("<td align='", this.align, "'");
								var idx = $(this).attr('axis').substr(3);

								if (options.sortname && options.sortname == $(this).attr('abbr')) {
									tdclass = 'sorted';
								}
								if (this.hide) {
									tbhtml.push(" style='display:none;'");
								}
								var width = thsdivs[j].style.width;
								var div = [];
								div.push("<div style='text-align:", this.align, ";width:", width, ";");
								if (options.nowrap == false) {
									div.push("white-space:normal");
								}
								div.push("'>");
								if (idx == "-1") { // checkbox
									div.push("<input type='checkbox' id='chk_", row.id, "' class='itemchk' value='", row.id, "'/>");
									if (tdclass != "") {
										tdclass += " chboxtd";
									} else {
										tdclass += "chboxtd";
									}
								} else {
									var divInner = row.cell[idx] || "&nbsp;";
									if (this.process) {
										divInner = this.process(divInner, trid);
									}
									div.push(divInner);
								}
								div.push("</div>");
								if (tdclass != "") {
									tbhtml.push(" class='", tdclass, "'");
								}
								tbhtml.push(">", div.join(""), "</td>");
							});
							tbhtml.push("</tr>");
						});
					}

				} else if (options.dataType == 'xml') {
					i = 1;
					$("rows row", data).each(function() {
						i++;
						var robj = this;
						var arrdata = new Array();
						$("cell", robj).each(function() {
							arrdata.push($(this).text());
						});
						var nid = $(this).attr('id');
						tbhtml.push("<tr id='", "row", nid, "'");
						if (i % 2 && options.striped) {
							tbhtml.push(" class='erow'");
						}
						if (options.rowbinddata) {
							tbhtml.push("ch='", arrdata.join("_FG$SP_"), "'");
						}
						tbhtml.push(">");
						var trid = nid;
						$(ths).each(function(j) {
							tbhtml.push("<td align='", this.align, "'");
							if (this.hide) {
								tbhtml.push(" style='display:none;'");
							}
							var tdclass = "";
							var tddata = "";
							var idx = $(this).attr('axis').substr(3);

							if (options.sortname && options.sortname == $(this).attr('abbr')) {
								tdclass = 'sorted';
							}
							var width = thsdivs[j].style.width;

							var div = [];
							div.push("<div style='text-align:", this.align, ";width:", width, ";");
							if (options.nowrap == false) {
								div.push("white-space:normal");
							}
							div.push("'>");

							if (idx == "-1") { // checkbox
								div.push("<input type='checkbox' id='chk_", nid, "' class='itemchk' value='", nid, "'/>");
								if (tdclass != "") {
									tdclass += " chboxtd";
								} else {
									tdclass += "chboxtd";
								}
							} else {
								var divInner = arrdata[idx] || "&nbsp;";
								if (options.rowbinddata) {
									tddata = arrdata[idx] || "";
								}
								if (this.process) {
									divInner = this.process(divInner, trid);
								}
								div.push(divInner);
							}
							div.push("</div>");
							if (tdclass != "") {
								tbhtml.push(" class='", tdclass, "'");
							}
							tbhtml.push(" axis='", tddata, "'", ">", div.join(""), "</td>");
						});
						tbhtml.push("</tr>");
					});

				}
				tbhtml.push("</tbody>");
				$(table).html(tbhtml.join(""));

				// this.rePosDrag();
				this.addRowProp();
				if (options.onSuccess)
					options.onSuccess();
				if (options.hideOnSubmit)
					$(grid.block).remove(); // $(table).show();
				this.hDiv.scrollLeft = this.bDiv.scrollLeft;
				if ($.browser.opera)
					$(table).css('visibility', 'visible');

			},
			changeSort : function(th) { // change sortorder

				if (this.loading)
					return true;

				$(grid.nDiv).hide();
				$(grid.nBtn).hide();

				if (options.sortname == $(th).attr('abbr')) {
					if (options.sortorder == 'asc')
						options.sortorder = 'desc';
					else
						options.sortorder = 'asc';
				}

				$(th).addClass('sorted').siblings().removeClass('sorted');
				$('.sdesc', this.hDiv).removeClass('sdesc');
				$('.sasc', this.hDiv).removeClass('sasc');
				$('div', th).addClass('s' + options.sortorder);
				options.sortname = $(th).attr('abbr');

				if (options.onChangeSort)
					options.onChangeSort(options.sortname, options.sortorder);
				else
					this.populate();

			},
			buildpager : function() { // rebuild pager based on new properties

				$('.pcontrol input', this.pDiv).val(options.page);
				$('.pcontrol span', this.pDiv).html(options.pages);

				var r1 = (options.page - 1) * options.rp + 1;
				var r2 = r1 + options.rp - 1;

				if (options.total < r2)
					r2 = options.total;

				var stat = options.pagestat;

				stat = stat.replace(/{from}/, r1);
				stat = stat.replace(/{to}/, r2);
				stat = stat.replace(/{total}/, options.total);
				$('.pPageStat', this.pDiv).html(stat);
			},
			populate : function() { // get latest data
				// log.trace("开始访问数据源");
				if (this.loading)
					return true;
				if (options.onSubmit) {
					var gh = options.onSubmit();
					if (!gh)
						return false;
				}
				this.loading = true;
				if (!options.url)
					return false;
				$('.pPageStat', this.pDiv).html(options.procmsg);
				$('.pReload', this.pDiv).addClass('loading');
				$(grid.block).css({
					top : grid.bDiv.offsetTop
				});
				if (options.hideOnSubmit)
					$(this.gDiv).prepend(grid.block); // $(table).hide();
				if ($.browser.opera)
					$(table).css('visibility', 'hidden');
				if (!options.newp)
					options.newp = 1;
				if (options.page > options.pages)
					options.page = options.pages;
				// var param = {page:options.newp, rp: options.rp, sortname: options.sortname,
				// sortorder: options.sortorder, query: options.query, qtype: options.qtype};
				var param = [ {
					name : 'page',
					value : options.newp
				}, {
					name : 'rp',
					value : options.rp
				}, {
					name : 'sortname',
					value : options.sortname
				}, {
					name : 'sortorder',
					value : options.sortorder
				}, {
					name : 'query',
					value : options.query
				}, {
					name : 'qtype',
					value : options.qtype
				}, {
					name : 'qop',
					value : options.qop
				} ];
				// param = jQuery.extend(param, options.extParam);
				if (options.extParam) {
					for ( var pi = 0; pi < options.extParam.length; pi++)
						param[param.length] = options.extParam[pi];
				}

				$.ajax({
					type : options.method,
					url : options.url,
					data : param,
					dataType : options.dataType,
					success : function(data) {
						if (data != null && data.error != null) {
							if (options.onError) {
								options.onError(data);
								grid.hideLoading();
							}
						} else {
							grid.addData(data);
						}
					},
					error : function(data) {
						try {
							if (options.onError) {
								options.onError(data);
							} else {
								alert("获取数据发生异常;")
							}
							grid.hideLoading();
						} catch (e) {
						}
					}
				});
			},
			doSearch : function() {
				var queryType = $('select[name=qtype]', grid.sDiv).val();
				var qArrType = queryType.split("$");
				var index = -1;
				if (qArrType.length != 3) {
					options.qop = "Eq";
					options.qtype = queryType;
				} else {
					options.qop = qArrType[1];
					options.qtype = qArrType[0];
					index = parseInt(qArrType[2]);
				}
				options.query = $('input[name=q]', grid.sDiv).val();
				// 添加验证代码
				if (options.query != "" && options.searchItems && index >= 0 && options.searchItems.length > index) {
					if (options.searchItems[index].reg) {
						if (!options.searchItems[index].reg.test(options.query)) {
							alert("你的输入不符合要求!");
							return;
						}
					}
				}
				options.newp = 1;
				this.populate();
			},
			changePage : function(ctype) { // change page

				if (this.loading)
					return true;

				switch (ctype) {
				case 'first':
					options.newp = 1;
					break;
				case 'prev':
					if (options.page > 1)
						options.newp = parseInt(options.page) - 1;
					break;
				case 'next':
					if (options.page < options.pages)
						options.newp = parseInt(options.page) + 1;
					break;
				case 'last':
					options.newp = options.pages;
					break;
				case 'input':
					var nv = parseInt($('.pcontrol input', this.pDiv).val());
					if (isNaN(nv))
						nv = 1;
					if (nv < 1)
						nv = 1;
					else if (nv > options.pages)
						nv = options.pages;
					$('.pcontrol input', this.pDiv).val(nv);
					options.newp = nv;
					break;
				}

				if (options.newp == options.page)
					return false;

				if (options.onChangePage)
					options.onChangePage(options.newp);
				else
					this.populate();

			},
			addCellProp : function() {
				var self = this;
				$('>tbody >tr', table).each(function() {
					var $tr = $(this);
					self._addRowProp($tr);
					$(">td", this).each(function(i) {
						var colOption = grid.colOptions[i];

						var $td = $(this);
						var $tddiv = $("<div>" + $td.html() + "</div>").css({
							textAlign : colOption.align,
							width : colOption.width
						});
						if (colOption.hide)
							$td.hide();
						$td.html($tddiv);// .removeAttr('width'); // wrap
						
						if (colOption.isch) {
							$td.addClass("chboxtd");
							$(":checkbox", $td).addClass("itemchk");
						}

						// var pid = false;
						// if (tr.id)
						// pid = tr.id.substr(3);
						// th = $ths[i];
						// if (th.process) {
						// th.process($tddiv, pid);
						// }
						// $("input.itemchk", $tddiv).each(function() {
						// $(this).click(function() {
						// if (this.checked) {
						// $tr.addClass("trSelected");
						// } else {
						// $tr.removeClass("trSelected");
						// }
						// if (options.onRowChecked) {
						// options.onRowChecked.call(this);
						// }
						// });
						// });
						// content
						// add editable event here 'dblclick',如果需要可编辑在这里添加可编辑代码
					});
				});
			},
			getCheckedRows : function() {
				var ids = [];
				$(":checkbox:checked", grid.bDiv).each(function() {
					ids.push($(this).val());
				});
				return ids;
			},
			getCellDim : function(obj) // get cell prop for editable event
			{
				var ht = parseInt($(obj).height());
				var pht = parseInt($(obj).parent().height());
				var wt = parseInt(obj.style.width);
				var pwt = parseInt($(obj).parent().width());
				var top = obj.offsetParent.offsetTop;
				var left = obj.offsetParent.offsetLeft;
				var pdl = parseInt($(obj).css('paddingLeft'));
				var pdt = parseInt($(obj).css('paddingTop'));
				return {
					ht : ht,
					wt : wt,
					top : top,
					left : left,
					pdl : pdl,
					pdt : pdt,
					pht : pht,
					pwt : pwt
				};
			},
			_addRowProp : function($tr) {
				$tr.click(function(e) {
					// var obj = (e.target || e.srcElement);
					// if (obj.href || obj.type)
					// return true;
					// if (options.singleSelect && !grid.multisel) {
					$tr.siblings().removeClass('trSelected');
					$tr.toggleClass('trSelected');
					// } else {
					// $tr.toggleClass('trSelected');
					// }
					// }).mousedown(function(e) {
					// if (e.shiftKey) {
					// $(this).toggleClass('trSelected');
					// grid.multisel = true;
					// this.focus();
					// $(grid.gDiv).noSelect();
					// }
					// if (e.ctrlKey) {
					// $(this).toggleClass('trSelected');
					// grid.multisel = true;
					// this.focus();
					// }
					// }).mouseup(function(e) {
					// if (grid.multisel && !e.ctrlKey) {
					// grid.multisel = false;
					// $(grid.gDiv).noSelect(false);
					// }
					// }).hover(function(e) {
					// if (grid.multisel && e.shiftKey) {
					// $(this).toggleClass('trSelected');
					// }
					// }, function() {
				});
				$("input.itemchk", $tr).each(function() {
					$(this).click(function() {
						if (this.checked) {
							$tr.addClass("trSelected");
						} else {
							$tr.removeClass("trSelected");
						}
						if (options.onRowChecked) {
							options.onRowChecked.call(this);
						}
					});
				});
				if (options.rowHandler) {
					options.rowHandler(this);
				}
				if ($.browser.msie && $.browser.version < 7.0) {
					$tr.hover(function() {
						$tr.addClass('trOver');
					}, function() {
						$tr.removeClass('trOver');
					});
				}
			},
			addRowProp : function() {
				var self = this;
				$('>tbody >tr', table).each(function() {
					self._addRowProp($(this));
				});
			},
			checkAllOrNot : function(parent) {
				var ischeck = $(this).attr("checked");
				$('>table >tbody >tr', grid.bDiv).each(function() {
					if (ischeck) {
						$(this).addClass("trSelected");
					} else {
						$(this).removeClass("trSelected");
					}
				});
				$("input.itemchk", grid.bDiv).each(function() {
					this.checked = ischeck;
					// Raise Event
					if (options.onRowChecked) {
						options.onRowChecked.call(this);
					}
				});
			},
			pager : 0
		};

		// create model if any
		if (options.colModel) {
			thead = document.createElement('thead');
			tr = document.createElement('tr');
			if (options.showCheckbox) {
				$(tr).append($('<th width="25" isch="true"><input type="checkbox"/></th>'));
			}
			for (i = 0; i < options.colModel.length; i++) {
				var cm = options.colModel[i];
				var th = document.createElement('th');

				th.innerHTML = cm.display;

				if (cm.name && cm.sortable)
					$(th).attr('abbr', cm.name);

				// th.idx = i;
				$(th).attr('axis', 'col' + i);

				if (cm.align)
					th.align = cm.align;

				if (cm.width)
					$(th).attr('width', cm.width);

				if (cm.hide) {
					th.hide = true;
				}
				if (cm.toggle != undefined) {
					th.toggle = cm.toggle;
				}
				if (cm.process) {
					th.process = cm.process;
				}

				$(tr).append(th);
			}
			$(thead).append(tr);
			$(table).prepend(thead);
		} // end if options.colModel

		// 初始化Grid容器
		grid.gDiv = document.createElement('div'); // global container 全局容器
		grid.mDiv = document.createElement('div'); // title container 标题容器
		grid.hDiv = document.createElement('div'); // header container 表头容器
		grid.bDiv = document.createElement('div'); // body container 内容容器
		grid.vGrip = document.createElement('div'); // vertical grip 纵向拉手：上下拉伸调整Grid高度
		grid.hGrip = document.createElement('div'); // horizontal grip 横向拉手：左右拉伸调整Grid宽度
		grid.cDrag = document.createElement('div'); // column drag 表头拉伸容器：包含和列数量相同的竖线，用于左右拉伸调整列宽度
		grid.block = document.createElement('div'); // blocker 阻塞：Grid获取数据时，将阻塞禁用在Grid上的所有操作
		grid.nDiv = document.createElement('div'); // column show/hide popup 列选择器：弹出菜单，用于设置要显示的列
		grid.nBtn = document.createElement('div'); // create column show/hide button 列选择器按钮：点击按钮，将弹出列选择器菜单
		grid.iDiv = document.createElement('div'); // create editable layer
		grid.sDiv = document.createElement('div'); // search box 搜索栏

		// 检查是否显示分页控件，如果需要显示，则创建分页控件
		if (options.usePager)
			grid.pDiv = document.createElement('div'); // create pager 分页控件

		grid.hTable = document.createElement('table'); // Grid头：是一个表格

		grid.gDiv.className = options.gridClass;// 设置全局样式名称

		// 如果不需要自动调整Grid宽度，则设置为固定值
		if (options.width != 'auto')
			grid.gDiv.style.width = options.width + 'px';

		// add conditional classes
		if ($.browser.msie)
			$(grid.gDiv).addClass('ie');

		if (options.novstripe)
			$(grid.gDiv).addClass('novstripe');

		// 用 global div 容器将 table 包起来
		var $table = $(table).before(grid.gDiv);
		$(grid.gDiv).append(table);

		// TODO: 创建工具栏
		// if (options.buttons) {
		// grid.tDiv = document.createElement('div');
		// grid.tDiv.className = 'tDiv';
		// var tDiv2 = document.createElement('div');
		// tDiv2.className = 'tDiv2';
		//
		// for (i = 0; i < options.buttons.length; i++) {
		// var btn = options.buttons[i];
		// if (!btn.separator) {
		// var btnDiv = document.createElement('div');
		// btnDiv.className = 'fbutton';
		// btnDiv.innerHTML = "<div><span>" + btn.displayname + "</span></div>";
		// if (btn.title) {
		// btnDiv.title = btn.title;
		// }
		// if (btn.bclass)
		// $('span', btnDiv).addClass(btn.bclass);
		// btnDiv.onpress = btn.onpress;
		// btnDiv.name = btn.name;
		// if (btn.onpress) {
		// $(btnDiv).click(function() {
		// this.onpress(this.name, grid.gDiv);
		// });
		// }
		// $(tDiv2).append(btnDiv);
		// if ($.browser.msie && $.browser.version < 7.0) {
		// $(btnDiv).hover(function() {
		// $(this).addClass('fbOver');
		// }, function() {
		// $(this).removeClass('fbOver');
		// });
		// }
		//
		// } else {
		// $(tDiv2).append("<div class='btnseparator'></div>");
		// }
		// }
		// $(grid.tDiv).append(tDiv2);
		// $(grid.tDiv).append("<div style='clear:both'></div>");
		// $(grid.gDiv).prepend(grid.tDiv);
		// }

		var time1 = new Date().getTime();
		/*
		 * 处理Grid表头
		 */
		grid.hDiv.className = 'hDiv';
		$table.before(grid.hDiv);
		$(grid.hDiv).append('<div class="hDivBox"></div>');
		$('div', grid.hDiv).append(grid.hTable);
		var thead = $("thead:first", table).get(0);
		if (thead)
			$(grid.hTable).append(thead);
		thead = null;

		if (!options.colmodel)
			var ci = 0;
		var colOptions = grid.colOptions = {};
		var $thList = $('thead tr:first th', grid.hTable).each(function(colIndex) {
			var $th = $(this);
			var colOption = $.fn.flexigrid.parseColOptions($th);
			colOptions[colIndex] = colOption;

			var $thdiv = $("<div>" + $th.html() + "</div>").css({
				textAlign : colOption.align,
				width : colOption.width
			});
			if (colOption.hide) {
				$th.hide();
			}
			$th.html($thdiv).removeAttr('width');

			// 有abbr属性的列，支持点击排序，abbr的值即为排序字段
			if (colOption.abbr) {
				$th.click(function(e) {
					if (!$th.hasClass('thOver'))
						return false;
					var obj = (e.target || e.srcElement);
					if (obj.href || obj.type)
						return true;
					grid.changeSort(this);
				});

				// 如果数据是按当前列排序的，则在表头显示排序标记
				if (colOption.abbr == options.sortname) {
					$th.addClass('sorted');
					$thdiv.addClass('s' + options.sortorder);
				}
			}

			// 设置列的轴、并将列内容用div包起来
			if (!options.colmodel && !colOption.isch) {
				$th.attr('axis', 'col' + ci++);
			} else if (colOption.isch) {
				$th.addClass("cth").attr('axis', "col-1");
			}

			// 不是checkbox列，则为表头添加拖动事件
			if (!colOption.isch) {
				$th.mousedown(function(e) {
					grid.dragStart('colMove', e, this);
				}).hover(function() {

					if (!grid.colresize && !grid.colCopy) {
						if (!$th.hasClass('thMove'))
							$th.addClass('thOver');

						if (colOption.abbr) {
							if (colOption.abbr != options.sortname)
								$thdiv.addClass('s' + options.sortorder);
							else {
								var dir = options.sortorder == 'asc' ? 'desc' : 'asc';
								$thdiv.removeClass('s' + options.sortorder).addClass('s' + dir);
							}
						}
					}

					if (grid.colCopy) {
						if (colIndex == grid.dcoln)
							return false;

						if (colIndex < grid.dcoln)
							$th.append(grid.cdropleft);
						else
							$th.append(grid.cdropright);

						grid.dcolt = colIndex;

					} else if (!grid.colresize) {// 显示列选择器菜单
						var thsa = $('th:visible', grid.hDiv);
						var nv = -1;
						for ( var i = 0, j = 0, l = thsa.length; i < l; i++) {
							if ($(thsa[i]).css("display") != "none") {
								if (thsa[i] == this) {
									nv = j;
									break;
								}
								j++;
							}
						}
						// var nv = $('th:visible', grid.hDiv).index(this);
						var onl = parseInt($('div:eq(' + nv + ')', grid.cDrag).css('left'));
						var nw = parseInt($(grid.nBtn).width()) + parseInt($(grid.nBtn).css('borderLeftWidth'));
						nl = onl - nw + Math.floor(options.cgwidth / 2);

						var $nDiv = $(grid.nDiv).hide();
						var $nBtn = $(grid.nBtn).hide();

						$nBtn.css({
							'left' : nl,
							top : grid.hDiv.offsetTop
						}).show();

						var ndw = parseInt($nDiv.width());

						$nDiv.css({
							top : grid.bDiv.offsetTop
						});

						if ((nl + ndw) > $(grid.gDiv).width())
							$nDiv.css('left', onl - ndw + 1);
						else
							$nDiv.css('left', nl);

						if ($(this).hasClass('sorted'))
							$nBtn.addClass('srtd');
						else
							$nBtn.removeClass('srtd');

					}

				}, function() {
					$th.removeClass('thOver');
					if (colOption.abbr != options.sortname)
						$thdiv.removeClass('s' + options.sortorder);
					else if ($(this).attr('abbr') == options.sortname) {
						var no = options.sortorder == 'asc' ? 'desc' : 'asc';
						$thdiv.addClass('s' + options.sortorder).removeClass('s' + no);
					}
					if (grid.colCopy) {
						$(grid.cdropleft).remove();
						$(grid.cdropright).remove();
						grid.dcolt = null;
					}
				}); // wrap header content
			}
		});

		var time2 = new Date().getTime();
		/*
		 * 处理Grid内容
		 */
		grid.bDiv.className = 'bDiv';
		$table.before(grid.bDiv).removeAttr('width');;
		$(grid.bDiv).css({
			height : (options.height == 'auto') ? 'auto' : options.height + "px"
		}).scroll(function(e) {
			grid.scroll();
		}).append(table);

		if (options.height == 'auto') {
			$table.addClass('autoht');
		}
		// 处理内容cell
		if (options.url == false || options.url == "") {
			grid.addCellProp();
		}
		if (options.striped)
			$('>tbody tr:odd', table).addClass('erow');

		var time3 = new Date().getTime();
		/*
		 * 处理表头拉伸
		 */
		//if (options.colResizable) {
			var cdcol = $('thead tr:first th:first', grid.hTable).get(0);
			grid.cDrag.className = 'cDrag';
			grid.cdpad = 0;

			grid.cdpad += (isNaN(parseInt($('div', cdcol).css('borderLeftWidth'))) ? 0 : parseInt($('div', cdcol).css('borderLeftWidth')));
			grid.cdpad += (isNaN(parseInt($('div', cdcol).css('borderRightWidth'))) ? 0 : parseInt($('div', cdcol).css('borderRightWidth')));
			grid.cdpad += (isNaN(parseInt($('div', cdcol).css('paddingLeft'))) ? 0 : parseInt($('div', cdcol).css('paddingLeft')));
			grid.cdpad += (isNaN(parseInt($('div', cdcol).css('paddingRight'))) ? 0 : parseInt($('div', cdcol).css('paddingRight')));
			grid.cdpad += (isNaN(parseInt($(cdcol).css('borderLeftWidth'))) ? 0 : parseInt($(cdcol).css('borderLeftWidth')));
			grid.cdpad += (isNaN(parseInt($(cdcol).css('borderRightWidth'))) ? 0 : parseInt($(cdcol).css('borderRightWidth')));
			grid.cdpad += (isNaN(parseInt($(cdcol).css('paddingLeft'))) ? 0 : parseInt($(cdcol).css('paddingLeft')));
			grid.cdpad += (isNaN(parseInt($(cdcol).css('paddingRight'))) ? 0 : parseInt($(cdcol).css('paddingRight')));

			$(grid.bDiv).before(grid.cDrag);

			var cdheight = $(grid.bDiv).height();
			var hdheight = $(grid.hDiv).height();

			$(grid.cDrag).css({
				top : -hdheight + 'px'
			});

			// 为表头的每一列创建一个div用来左右拉伸列，即调整列宽度
			$thList.each(function() {
				var cgDiv = document.createElement('div');
				$(grid.cDrag).append(cgDiv);
				if (!options.cgwidth)
					options.cgwidth = $(cgDiv).width();
				$(cgDiv).css({
					height : cdheight + hdheight
				}).mousedown(function(e) {
					grid.dragStart('colresize', e, this);
				});
				if ($.browser.msie && $.browser.version < 7.0) {
					grid.fixHeight($(grid.gDiv).height());
					$(cgDiv).hover(function() {
						grid.fixHeight();
						$(this).addClass('dragging');
					}, function() {
						if (!grid.colresize)
							$(this).removeClass('dragging');
					});
				}
			});
		//}

		var time4 = new Date().getTime();
		/*
		 * 处理Grid拉伸
		 */
		if (options.resizable) {
			// 拉动底部调整Grid高度
			if (options.height != 'auto') {
				grid.vGrip.className = 'vGrip';
				$(grid.vGrip).mousedown(function(e) {
					grid.dragStart('vresize', e);
				}).html('<span></span>');
				$(grid.bDiv).after(grid.vGrip);
			}
			// 拉动右边调整Grid宽度
			if (options.width != 'auto' && !options.nohresize) {
				grid.hGrip.className = 'hGrip';
				$(grid.hGrip).mousedown(function(e) {
					grid.dragStart('vresize', e, true);
				}).html('<span></span>').css('height', $(grid.gDiv).height());
				if ($.browser.msie && $.browser.version < 7.0) {
					$(grid.hGrip).hover(function() {
						$(this).addClass('hgOver');
					}, function() {
						$(this).removeClass('hgOver');
					});
				}
				$(grid.gDiv).append(grid.hGrip);
			}
		}

		var time5 = new Date().getTime();
		/*
		 * 处理分页导航条
		 */
		if (options.usePager) {
			grid.pDiv.className = 'pDiv';
			grid.pDiv.innerHTML = '<div class="pDiv2"></div>';
			$(grid.bDiv).after(grid.pDiv);
			var html = '<div class="pGroup"><div class="pFirst pButton" title="转到第一页"><span></span></div><div class="pPrev pButton" title="转到上一页"><span></span></div> </div><div class="btnseparator"></div> <div class="pGroup"><span class="pcontrol">当前 <input type="text" size="1" value="1" /> ,总页数 <span> 1 </span></span></div><div class="btnseparator"></div><div class="pGroup"> <div class="pNext pButton" title="转到下一页"><span></span></div><div class="pLast pButton" title="转到最后一页"><span></span></div></div><div class="btnseparator"></div><div class="pGroup"> <div class="pReload pButton" title="刷新"><span></span></div> </div> <div class="btnseparator"></div><div class="pGroup"><span class="pPageStat"></span></div>';
			$('div', grid.pDiv).html(html);

			$('.pReload', grid.pDiv).click(function() {
				grid.populate();
			});
			$('.pFirst', grid.pDiv).click(function() {
				grid.changePage('first');
			});
			$('.pPrev', grid.pDiv).click(function() {
				grid.changePage('prev');
			});
			$('.pNext', grid.pDiv).click(function() {
				grid.changePage('next');
			});
			$('.pLast', grid.pDiv).click(function() {
				grid.changePage('last');
			});
			$('.pcontrol input', grid.pDiv).keydown(function(e) {
				if (e.keyCode == 13)
					grid.changePage('input');
			});
			if ($.browser.msie && $.browser.version < 7)
				$('.pButton', grid.pDiv).hover(function() {
					$(this).addClass('pBtnOver');
				}, function() {
					$(this).removeClass('pBtnOver');
				});

			if (options.useRp) {
				var opt = "";
				for ( var nx = 0; nx < options.rpOptions.length; nx++) {
					if (options.rp == options.rpOptions[nx])
						sel = 'selected="selected"';
					else
						sel = '';
					opt += "<option value='" + options.rpOptions[nx] + "' " + sel + " >" + options.rpOptions[nx] + "&nbsp;&nbsp;</option>";
				}
				;
				$('.pDiv2', grid.pDiv).prepend("<div class='pGroup'>每页 <select name='rp'>" + opt + "</select>条</div> <div class='btnseparator'></div>");
				$('select', grid.pDiv).change(function() {
					if (options.onRpChange)
						options.onRpChange(+this.value);
					else {
						options.newp = 1;
						options.rp = +this.value;
						grid.populate();
					}
				});
			}

			// add search button
			if (options.searchItems) {
				$('.pDiv2', grid.pDiv).prepend("<div class='pGroup'> <div class='pSearch pButton'><span></span></div> </div>  <div class='btnseparator'></div>");
				$('.pSearch', grid.pDiv).click(function() {
					$(grid.sDiv).slideToggle('fast', function() {
						$('.sDiv:visible input:first', grid.gDiv).trigger('focus');
					});
				});
				// add search box
				grid.sDiv.className = 'sDiv';

				sitems = options.searchItems;

				var sopt = "";
				var op = "Eq";
				for ( var s = 0; s < sitems.length; s++) {
					if (options.qtype == '' && sitems[s].isdefault == true) {
						options.qtype = sitems[s].name;
						sel = 'selected="selected"';
					} else
						sel = '';
					if (sitems[s].operater == "Like") {
						op = "Like";
					} else {
						op = "Eq";
					}
					sopt += "<option value='" + sitems[s].name + "$" + op + "$" + s + "' " + sel + " >" + sitems[s].display + "&nbsp;&nbsp;</option>";
				}

				if (options.qtype == '')
					options.qtype = sitems[0].name;

				$(grid.sDiv).append("<div class='sDiv2'>快速检索：<input type='text' size='30' name='q' class='qsbox' /> <select name='qtype'>" + sopt + "</select> <input type='button' name='qclearbtn' value='清空' /></div>");

				$('input[name=q],select[name=qtype]', grid.sDiv).keydown(function(e) {
					if (e.keyCode == 13)
						grid.doSearch();
				});
				$('input[name=qclearbtn]', grid.sDiv).click(function() {
					$('input[name=q]', grid.sDiv).val('');
					options.query = '';
					grid.doSearch();
				});
				$(grid.bDiv).after(grid.sDiv);

			}

		}
		$(grid.pDiv, grid.sDiv).append("<div style='clear:both'></div>");

		var time6 = new Date().getTime();
		/*
		 * 处理标题
		 */
		if (options.title) {
			grid.mDiv.className = 'mDiv';
			grid.mDiv.innerHTML = '<div class="ftitle">' + options.title + '</div>';
			$(grid.gDiv).prepend(grid.mDiv);
			if (options.showTableToggleBtn) {
				$(grid.mDiv).append('<div class="ptogtitle" title="Minimize/Maximize Table"><span></span></div>');
				$('div.ptogtitle', grid.mDiv).click(function() {
					$(grid.gDiv).toggleClass('hideBody');
					$(this).toggleClass('vsble');
				});
			}
			// grid.rePosDrag();
		}

		// setup cdrops
		grid.cdropleft = document.createElement('span');
		grid.cdropleft.className = 'cdropleft';
		grid.cdropright = document.createElement('span');
		grid.cdropright.className = 'cdropright';

		var time7 = new Date().getTime();
		/*
		 * 添加阻塞
		 */
		grid.block.className = 'gBlock';
		var blockloading = $("<div/>");
		blockloading.addClass("loading");
		$(grid.block).append(blockloading);
		var gh = $(grid.bDiv).height();
		var gtop = grid.bDiv.offsetTop;
		$(grid.block).css({
			width : grid.bDiv.style.width,
			height : gh,
			position : 'relative',
			marginBottom : (gh * -1),
			zIndex : 1,
			top : gtop,
			left : '0px'
		});
		$(grid.block).fadeTo(0, options.blockOpacity);

		// add column control
		if ($('th', grid.hDiv).length) {
			grid.nDiv.className = 'nDiv';
			grid.nDiv.innerHTML = "<table cellpadding='0' cellspacing='0'><tbody></tbody></table>";
			$(grid.nDiv).css({
				marginBottom : (gh * -1),
				display : 'none',
				top : gtop
			}).noSelect();

			var cn = 0;

			$('th div', grid.hDiv).each(function() {
				var kcol = $("th[axis='col" + cn + "']", grid.hDiv)[0];
				if (kcol == null)
					return;
				var chkall = $("input[type='checkbox']", this);
				if (chkall.length > 0) {
					chkall[0].onclick = grid.checkAllOrNot;
					return;
				}
				if (kcol.toggle == false || this.innerHTML == "") {
					cn++;
					return;
				}
				var chk = 'checked="checked"';
				if (kcol.style.display == 'none')
					chk = '';

				$('tbody', grid.nDiv).append('<tr><td class="ndcol1"><input type="checkbox" ' + chk + ' class="togCol noborder" value="' + cn + '" /></td><td class="ndcol2">' + this.innerHTML + '</td></tr>');
				cn++;
			});

			if ($.browser.msie && $.browser.version < 7.0)
				$('tr', grid.nDiv).hover(function() {
					$(this).addClass('ndcolover');
				}, function() {
					$(this).removeClass('ndcolover');
				});

			$('td.ndcol2', grid.nDiv).click(function() {
				if ($('input:checked', grid.nDiv).length <= options.minColToggle && $(this).prev().find('input')[0].checked)
					return false;
				return grid.toggleCol($(this).prev().find('input').val());
			});

			$('input.togCol', grid.nDiv).click(function() {

				if ($('input:checked', grid.nDiv).length < options.minColToggle && this.checked == false)
					return false;
				$(this).parent().next().trigger('click');
				// return false;
			});

			$(grid.gDiv).prepend(grid.nDiv);

			$(grid.nBtn).addClass('nBtn').html('<div></div>')
			// .attr('title', 'Hide/Show Columns')
			.click(function() {
				$(grid.nDiv).toggle();
				return true;
			});

			if (options.showToggleBtn)
				$(grid.gDiv).prepend(grid.nBtn);

		}

		// add date edit layer
		$(grid.iDiv).addClass('iDiv').css({
			display : 'none'
		});
		$(grid.bDiv).append(grid.iDiv);

		// add flexigrid events
		$(grid.bDiv).hover(function() {
			$(grid.nDiv).hide();
			$(grid.nBtn).hide();
		}, function() {
			if (grid.multisel)
				grid.multisel = false;
		});
		$(grid.gDiv).hover(function() {
		}, function() {
			$(grid.nDiv).hide();
			$(grid.nBtn).hide();
		});

		// add document events
		$(document).mousemove(function(e) {
			grid.dragMove(e);
		}).mouseup(function(e) {
			grid.dragEnd();
		}).hover(function() {
		}, function() {
			grid.dragEnd();
		});

		// browser adjustments
		if ($.browser.msie && $.browser.version < 7.0) {
			$('.hDiv,.bDiv,.mDiv,.pDiv,.vGrip,.tDiv, .sDiv', grid.gDiv).css({
				width : '100%'
			});
			$(grid.gDiv).addClass('ie6');
			if (options.width != 'auto')
				$(grid.gDiv).addClass('ie6fullwidthbug');
		}

		grid.rePosDrag();
		grid.fixHeight();

		// make grid functions accessible
		table.options = options;
		table.grid = grid;

		// load data
		if (options.url && options.autoload) {
			grid.populate();
		}

		var timen = new Date().getTime();

		jDemsy.log("创建FlexiGrid: 初始化({1}), 处理表头({2}), 处理内容({3}), 列宽调整({4}), Grid拉升({5}), 分页栏({6}), 标题({7}), 其他({8})", time0, time1 - time0, time2 - time1, time3 - time2, time4 - time3, time5 - time4, time6 - time5, time7 - time6, timen - time7);

		return table;

	};
	//
	// var docloaded = false;
	//
	// $(document).ready(function() {
	// docloaded = true
	// });

	$.fn.flexReload = function(options) { // function to reload grid

		return this.each(function() {
			if (this.grid && this.options.url)
				this.grid.populate();
		});

	}; // end flexReload
	// 重新指定宽度和高度
	$.fn.flexResize = function(w, h) {
		var options = {
			width : w,
			height : h
		};
		return this.each(function() {
			if (this.grid) {
				$.extend(this.options, options);
				this.grid.reSize();
			}
		});
	};
	$.fn.ChangePage = function(type) {
		return this.each(function() {
			if (this.grid) {
				this.grid.changePage(type);
			}
		})
	};
	$.fn.flexOptions = function(options) { // function to update general options

		return this.each(function() {
			if (this.grid)
				$.extend(this.options, options);
		});

	}; // end flexOptions
	$.fn.getCheckedRows = function() {
		if (this[0].grid) {
			return this[0].grid.getCheckedRows();
		}
		return [];
	};
	$.fn.flexToggleCol = function(cid, visible) { // function to reload grid

		return this.each(function() {
			if (this.grid)
				this.grid.toggleCol(cid, visible);
		});

	}; // end flexToggleCol

	$.fn.flexAddData = function(data) { // function to add data to grid

		return this.each(function() {
			if (this.grid)
				this.grid.addData(data);
		});

	};

	$.fn.noSelect = function(options) { // no select plugin by me :-)
		if (options == null)
			prevent = true;
		else
			prevent = options;

		if (prevent) {

			return this.each(function() {
				if ($.browser.msie || $.browser.safari)
					$(this).bind('selectstart', function() {
						return false;
					});
				else if ($.browser.mozilla) {
					$(this).css('MozUserSelect', 'none');
					$('body').trigger('focus');
				} else if ($.browser.opera)
					$(this).bind('mousedown', function() {
						return false;
					});
				else
					$(this).attr('unselectable', 'on');
			});

		} else {

			return this.each(function() {
				if ($.browser.msie || $.browser.safari)
					$(this).unbind('selectstart');
				else if ($.browser.mozilla)
					$(this).css('MozUserSelect', 'inherit');
				else if ($.browser.opera)
					$(this).unbind('mousedown');
				else
					$(this).removeAttr('unselectable', 'on');
			});

		}
		;

	}; // end noSelect

	$.fn.flexigrid = function(options) {

		return this.each(function() {
			$.addFlex(this, $.extend({}, $.fn.flexigrid.defaults, $.fn.flexigrid.parseOptions($(this)), options));
		});

	}; // end flexigrid
	$.fn.flexigrid.defaults = {
		height : 300, // flexigrid插件的高度，单位为px
		width : 'auto', // 宽度值，auto表示根据每列的宽度自动计算
		striped : true, // 是否显示斑纹效果，默认是奇偶交互的形式
		novstripe : false,
		minWidth : 30, // Grid最小宽度
		minHeight : 80, // Grid最小高度
		resizable : false, // resizable table是否可伸缩
		url : false, // ajax url,ajax方式对应的url地址
		method : 'POST', // data sending method,数据发送方式
		dataType : 'json', // type of data loaded,数据加载的类型，xml,json
		errormsg : '发生错误', // 错误提升信息
		usePager : false, // 是否分页
		nowrap : true, // 是否不换行
		page : 1, // current page,默认当前页
		total : 1, // total pages,总页面数
		useRp : true, // use the results per page select
		// box,是否可以动态设置每页显示的结果数
		rp : 25, // results per page,每页默认的结果数
		rpOptions : [ 10, 15, 20, 25, 40, 100 ], // 可选择设定的每页结果数
		title : false, // 是否包含标题
		pagestat : '显示记录从{from}到{to}，总数 {total} 条', // 显示当前页和总页面的样式
		procmsg : '正在处理数据，请稍候 ...', // 正在处理的提示信息
		query : '', // 搜索查询的条件
		qtype : '', // 搜索查询的类别
		qop : "Eq", // 搜索的操作符
		nomsg : '没有符合条件的记录存在', // 无结果的提示信息
		minColToggle : 1, // minimum allowed column to be hidden
		showToggleBtn : true, // show or hide column toggle popup
		hideOnSubmit : true, // 显示遮盖
		showTableToggleBtn : false, // 显示隐藏Grid
		autoload : true, // 自动加载
		blockOpacity : 0.5, // 透明度设置
		onToggleCol : false, // 当在行之间转换时
		onChangeSort : false, // 当改变排序时
		onSuccess : false, // 成功后执行
		onSubmit : false, // using a custom populate function,调用自定义的计算函数
		showCheckbox : false, // 是否显示checkbox
		rowHandler : false, // 是否启用行的扩展事情功能
		rowbinddata : false,
		extParam : {},
		gridClass : "jdemsy-grid",
		onRowChecked : false
	};

	$.fn.flexigrid.parseColOptions = function($container) {
		return jDemsy.parseOptions($container, [ "align",// 对齐方式
		"abbr",// 排序字段
		{
			width : "number",// 列宽度
			hide : "boolean",// 是否隐藏
			isch : "boolean"// 是否是checkbox多选列
		} ])
	};
	$.fn.flexigrid.parseOptions = function($container) {
		return jDemsy.parseOptions($container, [ "url", "gridClass", "title", {
			height : "number",
			width : "number",
			minHeight : "number",
			minWidth : "number",
			page : "number",
			total : "number",
			rp : "number",
			striped : "boolean",
			novstripe : "boolean",
			colResizable : "boolean",
			resizable : "boolean",
			usePager : "boolean",
			useRp : "boolean",
			singleSelect : "boolean",
			showCheckbox : "boolean",
			searchItems : "json",
			colModel : "json"
		} ])
	};
})(jQuery, jDemsy);
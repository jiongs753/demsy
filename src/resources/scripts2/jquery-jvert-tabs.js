(function($) {

	var allTabOptions = new Array();

	String.prototype.startsWith = function(str) {
		return (this.match("^" + str) == str);
	}

	$.fn.jVertTabs = function(attr, options) {

		var elmId = $(this).attr('id');

		var opts;
		var defaults = {
			selected : 0,
			select : function(index) {
			},
			spinner : "加载数据...",
			equalHeights : false
		};

		var tabColumnHeight = 0;

		if ($.isPlainObject(attr)) {
			options = attr;
			opts = $.extend(defaults, options);
			allTabOptions[elmId] = opts;
		} else {
			if (attr != null && options != null) {
				if (attr == "selected") {
					var thisTabOpts = allTabOptions[elmId];
					thisTabOpts.selected = options;
					var tabRoot = $(this);
					doSelectTab($(this), options);
					return;
				}
			} else {
				opts = $.extend(defaults, options);
				allTabOptions[elmId] = opts;

			}
		}

		return this.each(function() {
			var tabRoot = $(this);
			setStyle(tabRoot);

			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabContentColumn = tabRoot.children("div.vtabs-content-column");
			// tabColumnHeight = tabColumn.height();

			$(this).find(".vtabs-tab-column > ul > li").each(function(i) {

				var a = $(this).find("a");
				var href = a.attr("href");
				var hrefBase = href.split("#")[0], baseEl;
				if (hrefBase && (hrefBase === location.toString().split("#")[0] || (baseEl = $("base")[0]) && hrefBase === baseEl.href)) {
					href = a[0].hash;
					a[0].href = href;
				}

				if (i < 1) {
					$(this).addClass("open");
					$(this).find("a").addClass("open");
				} else {
					$(this).addClass("closed");
					$(this).find("a").addClass("closed");
				}

				$(this).click(function() {
					handleTabClick($(this), i, tabRoot, true);
					return false;
				});
			});

			$(this).children(".vtabs-content-column > div.vtabs-content-panel").each(function(i) {
				if (i > 0) {
					$(this).hide();
				}
			});

			var thisTabOpts = allTabOptions[elmId];
			if (thisTabOpts != null) {
				var preSelectLi = tabColumn.find("ul > li").eq(thisTabOpts.selected);
				handleTabClick(preSelectLi, thisTabOpts.selected, tabRoot, false);
			}

			var thisTabOpts = allTabOptions[elmId];
			if (thisTabOpts != null && thisTabOpts.equalHeights) {
				equalizeHeights(tabContentColumn);
			}

			tabColumnHeight = getTotalTabsHeight(tabRoot);
			setMinHeight(tabContentColumn, tabColumnHeight);

			adjustMargin(tabRoot);

		});

		function doSelectTab(tabRoot, index) {
			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabContentColumn = tabRoot.children("div.vtabs-content-column");
			var selectLi = tabColumn.find("ul > li").eq(index);
			handleTabClick(selectLi, index, tabRoot, true);
		}

		function handleTabClick(li, liIndex, tabRoot, doSelectedCallBack) {

			var elmId = tabRoot.attr('id');
			var tabCol = tabRoot.children("div.vtabs-tab-column");
			var tabContentCol = tabRoot.children("div.vtabs-content-column");

			tabCol.find("ul > li").each(function(i) {
				if ($(this).hasClass("open")) {
					$(this).removeClass("open").addClass("closed");
					$(this).find("a").removeClass("open").addClass("closed");
				}
			});
			li.removeClass("closed").addClass("open");
			li.find("a").removeClass("closed").addClass("open");

			var openContentPanel;
			tabContentCol.children("div.vtabs-content-panel").each(function(i) {
				$(this).hide();
				if (i == liIndex) {
					openContentPanel = $(this);
				}
			});

			var link = li.find("a");
			var linkText = link.text();
			var linkValue = link.attr("href");
			if (!linkValue.startsWith("#")) {
				if (opts.spinner != "") {
					link.text(opts.spinner);
				}
				$.ajax({
					url : linkValue,
					type : "POST",
					success : function(data) {
						openContentPanel.html(data);
						openContentPanel.fadeIn(0);
						link.text(linkText);
						var thisTabOpts = allTabOptions[elmId];
						if (thisTabOpts != null && thisTabOpts.equalHeights) {
							equalizeHeights(tabContentCol);
						}
					},
					error : function(request, status, errorThrown) {
						link.text(linkText);
						alert("Error requesting " + linkValue + ": " + errorThrown);
					}
				});
			} else {
				openContentPanel.fadeIn(0);
			}

			var thisTabOpts = allTabOptions[elmId];
			if (thisTabOpts != null && doSelectedCallBack) {
				if (jQuery.isFunction(thisTabOpts.select)) {
					thisTabOpts.select.call(this, liIndex);
				}
			}
		}

		function getTotalTabsHeight(tabRoot) {
			var height = 0;
			tabRoot.find(".vtabs-tab-column > ul > li").each(function(i) {
				height += $(this).outerHeight(true);
			});
			return height;
		}

		function equalizeHeights(tabContentCol) {
			var tallest = getTallestHeight(tabContentCol);
			setMinHeight(tabContentCol, tallest);
		}

		function getTallestHeight(tabContentCol) {
			var maxHeight = 0, currentHeight = 0;
			tabContentCol.children("div.vtabs-content-panel").each(function(i) {
				currentHeight = $(this).height();
				if (currentHeight > maxHeight) {
					maxHeight = currentHeight;
				}
			});
			return maxHeight;
		}

		function setMinHeight(tabContentCol, minHeight) {
			var panelHeight = 0;
			tabContentCol.children("div.vtabs-content-panel").each(function(i) {
				panelHeight = $(this).height();
				if (panelHeight < minHeight) {
					$(this).css("min-height", minHeight);
				}
			});
		}

		function setStyle(tabRoot) {
			tabRoot.addClass("vtabs");
			tabRoot.children("div").eq(0).addClass("vtabs-tab-column");
			tabRoot.children("div").eq(1).addClass("vtabs-content-column");
			tabRoot.children("div").eq(1).children("div").addClass("vtabs-content-panel");
		}

		function adjustMargin(tabRoot) {
			var tabColumn = tabRoot.children("div.vtabs-tab-column");
			var tabColWidth = tabColumn.width();
			$(tabRoot).children('div.vtabs-content-column').css({
				"margin-left" : tabColWidth - 1 + "px"
			});
		}

	};

})(jQuery);
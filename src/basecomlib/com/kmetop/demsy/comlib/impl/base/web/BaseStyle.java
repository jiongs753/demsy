package com.kmetop.demsy.comlib.impl.base.web;

import java.util.List;

import javax.persistence.Column;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.field.CssBox;
import com.kmetop.demsy.comlib.biz.field.CssLink;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.comlib.ui.IStyleItem;
import com.kmetop.demsy.lang.Str;

public abstract class BaseStyle extends BizComponent implements IStyle {
	@BzFld(name = "传统编辑器", disabledNavi = true, options = "1:显示,0:隐藏")
	protected boolean detailState;

	@BzFld(name = "样式用途", disabledNavi = true, options = "1:页面,0:板块")
	protected int usage;

	@Column(columnDefinition = "text")
	@BzFld(name = "整体样式", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox box;

	@Column(columnDefinition = "text")
	@BzFld(name = "顶部", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox top;

	@Column(columnDefinition = "text")
	@BzFld(name = "顶部-左", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox topL;

	@Column(columnDefinition = "text")
	@BzFld(name = "顶部-标题", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox topT;

	@Column(columnDefinition = "text")
	@BzFld(name = "顶部-右", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox topR;

	@Column(columnDefinition = "text")
	@BzFld(name = "顶部-链接", gridField = false, cascadeMode = "detailState:1:E")
	protected CssLink topLink;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox data;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容链接", gridField = false, cascadeMode = "detailState:1:E")
	protected CssLink dataLink;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容条目", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox item;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容条目-左(上)", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox itemL;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容条目-标题", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox itemT;

	@Column(columnDefinition = "text")
	@BzFld(name = "内容条目-右(下)", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox itemR;

	@Column(columnDefinition = "text")
	@BzFld(name = "底部", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox bottom;

	@Column(columnDefinition = "text")
	@BzFld(name = "底部-左", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox bottomL;

	@Column(columnDefinition = "text")
	@BzFld(name = "底部-标题", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox bottomT;

	@Column(columnDefinition = "text")
	@BzFld(name = "底部-右", gridField = false, cascadeMode = "detailState:1:E")
	protected CssBox bottomR;

	@Column(columnDefinition = "text")
	@BzFld(name = "底部-链接", gridField = false, cascadeMode = "detailState:1:E")
	protected CssLink bottomLink;

	@BzFld(name = "CSS样式", gridField = false, cascadeMode = "detailState:1:N", uiTemplate = "ui.widget.ext.cssDesigner")
	protected FakeSubSystem<StyleItem> items;

	public abstract IStyle getParent();

	public String getCssClass() {
		return "css_" + getId();
	}

	public String getCssStyle() {
		StringBuffer sb = new StringBuffer();

		if (this.getParent() != null) {
			sb.append(((BaseStyle) getParent()).makeCssStyle(getCssClass()));
		}
		sb.append(makeCssStyle(getCssClass()));

		return sb.toString();
	}

	public String makeCssStyle(String cssClass) {
		StringBuffer sb = new StringBuffer();

		String name = "";
		if (usage == 1) {
			name = "body";
		} else {
			name = "." + cssClass;
		}

		// 转换CSS设计器的内容
		FakeSubSystem<StyleItem> itemsObj = this.getItems();
		if (itemsObj != null) {
			List<? extends IStyleItem> items = itemsObj.getList();
			if (items != null && items.size() > 0) {
				for (IStyleItem s : items) {
					String code = s.getCode();
					String desc = s.getDesc();
					if (!Str.isEmpty(desc)) {
						if (Str.isEmpty(code))
							code = "";
						sb.append("\n").append(name).append(" ").append(code).append("{").append(desc).append("}");
					}
				}
			}
		}

		makeCssStyle(box, name, sb, true);

		//
		if (usage == 1) {
			name = "#top";
		} else {
			name = "." + cssClass + " .t";
		}
		makeCssStyle(top, name, sb, true);
		makeCssStyle(topL, name + "l", sb, true);
		makeCssStyle(topT, name + "t", sb, true);
		makeCssStyle(topR, name + "r", sb, true);
		if (topLink != null)
			sb.append(topLink.toCssStyle(name));

		//
		if (usage == 1) {
			name = "#page";
		} else {
			name = "." + cssClass + " .d";
		}
		makeCssStyle(data, name, sb, true);
		if (dataLink != null)
			sb.append(dataLink.toCssStyle(name));

		if (usage == 1) {
			name = "#page .i";
		} else {
			name = "." + cssClass + " .i";
		}
		makeCssStyle(item, name, sb, true);
		makeCssStyle(itemL, name + "l", sb, true);
		makeCssStyle(itemT, name + "t", sb, true);
		makeCssStyle(itemR, name + "r", sb, true);

		//
		if (usage == 1) {
			name = "#bottom";
		} else {
			name = "." + cssClass + " .b";
		}
		makeCssStyle(bottom, name, sb, true);
		makeCssStyle(bottomL, name + "l", sb, true);
		makeCssStyle(bottomT, name + "t", sb, true);
		makeCssStyle(bottomR, name + "r", sb, true);
		if (bottomLink != null)
			sb.append(bottomLink.toCssStyle(name));

		if (!Str.isEmpty(getDesc())) {
			if (this.id != null && this.id > 0) {
				if (usage == 1) {
					name = "body";
				} else {
					name = "." + cssClass;
				}
				String[] array = Str.toArray(getDesc(), "}");
				for (String str : array) {
					int url = str.indexOf("url(");
					if (url > -1) {
						url += 4;
						sb.append("\n").append(name).append(" ").append(str.substring(0, url))
								.append(Demsy.contextPath).append(str.substring(url)).append("}");
					} else
						sb.append("\n").append(name).append(" ").append(str).append("}");
				}
			} else {
				sb.append("\n").append(code).append(getDesc());
			}
		}

		if (sb.length() > 0) {
			// 去除第一个换行符
			return sb.substring(1);
		}

		return "";
	}

	private static void makeCssStyle(CssBox css, String cssClass, StringBuffer sb, boolean autoBgSize) {
		if (css != null) {
			String cssstyle = css.toCssStyle(autoBgSize);
			if (!Str.isEmpty(cssstyle)) {
				sb.append("\n").append(cssClass).append("{").append(cssstyle).append("}");
			}
			if (!Str.isEmpty(css.getStyle())) {
				String[] array = Str.toArray(css.getStyle(), "}");
				for (String str : array) {
					int url = str.indexOf("url(");
					if (url > -1) {
						url += 4;
						sb.append("\n").append(cssClass).append(str.indexOf("{") > -1 ? " " : " {")
								.append(str.substring(0, url)).append(Demsy.contextPath).append(str.substring(url))
								.append("}");
					} else {
						sb.append("\n").append(cssClass).append(str.indexOf("{") > -1 ? " " : " {").append(str)
								.append("}");
					}
				}
			}
		}
	}

	public CssBox getBox() {
		return box;
	}

	public CssBox getTop() {
		return top;
	}

	public CssBox getTopL() {
		return topL;
	}

	public CssBox getTopT() {
		return topT;
	}

	public CssBox getTopR() {
		return topR;
	}

	public CssBox getItem() {
		return item;
	}

	public CssBox getData() {
		return data;
	}

	public CssBox getItemL() {
		return itemL;
	}

	public CssBox getItemT() {
		return itemT;
	}

	public CssBox getItemR() {
		return itemR;
	}

	public void setBox(CssBox css) {
		this.box = css;
	}

	public void setTop(CssBox cssH) {
		this.top = cssH;
	}

	public void setTopL(CssBox cssHL) {
		this.topL = cssHL;
	}

	public void setTopT(CssBox cssHC) {
		this.topT = cssHC;
	}

	public void setTopR(CssBox cssHR) {
		this.topR = cssHR;
	}

	public void setItem(CssBox cssI) {
		this.item = cssI;
	}

	public void setData(CssBox cssIB) {
		this.data = cssIB;
	}

	public void setItemL(CssBox cssIL) {
		this.itemL = cssIL;
	}

	public void setItemT(CssBox cssIC) {
		this.itemT = cssIC;
	}

	public void setItemR(CssBox cssIR) {
		this.itemR = cssIR;
	}

	public CssLink getTopLink() {
		return topLink;
	}

	public CssLink getDataLink() {
		return dataLink;
	}

	public void setTopLink(CssLink linkHC) {
		this.topLink = linkHC;
	}

	public void setDataLink(CssLink linkIC) {
		this.dataLink = linkIC;
	}

	public CssBox getBottom() {
		return bottom;
	}

	public CssBox getBottomL() {
		return bottomL;
	}

	public CssBox getBottomT() {
		return bottomT;
	}

	public CssBox getBottomR() {
		return bottomR;
	}

	public CssLink getBottomLink() {
		return bottomLink;
	}

	public void setBottom(CssBox bottom) {
		this.bottom = bottom;
	}

	public void setBottomL(CssBox bottomL) {
		this.bottomL = bottomL;
	}

	public void setBottomT(CssBox bottomT) {
		this.bottomT = bottomT;
	}

	public void setBottomR(CssBox bottomR) {
		this.bottomR = bottomR;
	}

	public void setBottomLink(CssLink bottomLink) {
		this.bottomLink = bottomLink;
	}

	public FakeSubSystem<StyleItem> getItems() {
		return items;
	}

	public void setItems(FakeSubSystem<StyleItem> items) {
		this.items = items;
	}

}

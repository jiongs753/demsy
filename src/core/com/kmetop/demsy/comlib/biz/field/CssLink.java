package com.kmetop.demsy.comlib.biz.field;

import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.lang.Str;

@BzFld(uiTemplate = "ui.widget.field.Composite")
public class CssLink extends JsonField<CssLink> {
	@BzFld(name = "链接", order = 10, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont a;

	@BzFld(name = "未访问的链接", order = 11, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aLink;

	@BzFld(name = "已访问的链接", order = 12, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aVisited;

	@BzFld(name = "鼠标悬停链接", order = 13, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aHover;

	@BzFld(name = "被选中的链接", order = 14, uiTemplate = "ui.widget.field.CssFont")
	protected CssFont aActive;

	public CssLink() {
		this("");
	}

	public CssLink(String str) {
		super(str);
	}

	@Override
	protected void init(CssLink obj) {
		if (obj != null) {
			this.a = obj.a;
			this.aLink = obj.aLink;
			this.aVisited = obj.aVisited;
			this.aHover = obj.aHover;
			this.aActive = obj.aActive;
		}
	}

	public String toCssStyle(String cssClass) {
		StringBuffer sb = new StringBuffer();

		if (a != null) {
			String css = a.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a{").append(css).append("}");
		}
		if (aLink != null) {
			String css = aLink.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:link{").append(css).append("}");
		}
		if (aVisited != null) {
			String css = aVisited.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:visited{").append(css).append("}");
		}
		if (aHover != null) {
			String css = aHover.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:hover{").append(css).append("}");
		}
		if (aActive != null) {
			String css = aActive.toCssStyle();
			if (!Str.isEmpty(css))
				sb.append("\n").append(cssClass).append(" a:active{").append(css).append("}");
		}

		return sb.toString();
	}

}

package com.kmetop.demsy.comlib.biz.field;

import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.lang.Str;

@BzFld(name = "字体", uiTemplate = "ui.widget.field.Composite")
public class CssFont extends JsonField<CssFont> {

	@BzFld(name = "字体大小", order = 1, uiTemplate = "ui.widget.field.Spinner")
	protected Integer size;

	@BzFld(name = "字体粗细", order = 2, options = ":粗细,normal:正常,bold:粗体,bolder:更粗,lighter:更细,inherit:继承,100:100,200:200,300:300,400:400,500:500,600:600,700:700,800:800,900:900")
	protected String weight;

	@BzFld(name = "字体颜色", order = 3, uiTemplate = "ui.widget.field.CssColor")
	protected String color;

	@BzFld(name = "字体风格", order = 4, options = ":风格,normal:正常,italic:斜体,oblique:倾斜,inherit:继承")
	protected String style;

	@BzFld(name = "文本修饰", order = 5, options = ":修饰,none:默认,underline:下划线,overline:上划线,line-through:穿越线,blink:闪烁,inherit:继承")
	protected String decoration;

	// @BzFld(name = "名称")
	protected String name;

	public CssFont() {
		this("");
	}

	public CssFont(String str) {
		super(str);
	}

	@Override
	protected void init(CssFont obj) {
		if (obj != null) {
			this.size = obj.size;
			this.name = obj.name;
			this.style = obj.style;
			this.color = obj.color;
			this.weight = obj.weight;
			this.decoration = obj.decoration;
		}
	}

	public String toCssStyle() {
		if (Str.isEmpty(style) && Str.isEmpty(color) && Str.isEmpty(weight) && Str.isEmpty(decoration)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		if (size != null)
			sb.append("font-size:").append(size).append("px;");
		if (!Str.isEmpty(style))
			sb.append("font-style:").append(style).append(";");
		if (!Str.isEmpty(color))
			sb.append("color:").append(color).append(";");
		if (!Str.isEmpty(weight))
			sb.append("font-weight:").append(weight).append(";");
		if (!Str.isEmpty(decoration))
			sb.append("text-decoration:").append(decoration).append(";");

		return sb.toString();
	}

	public String getStyle() {
		return style;
	}

	public String getColor() {
		return color;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getWeight() {
		return weight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
}

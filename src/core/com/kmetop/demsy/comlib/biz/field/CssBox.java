package com.kmetop.demsy.comlib.biz.field;

import java.io.IOException;

import javax.persistence.Column;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.lang.Img;
import com.kmetop.demsy.lang.Str;

@BzFld(uiTemplate = "ui.widget.field.Composite")
public class CssBox extends JsonField<CssBox> {
	@BzFld(name = "边框宽度", order = 1, uiTemplate = "ui.widget.field.Spinner")
	protected Integer width;

	@BzFld(name = "边框高度", order = 2, uiTemplate = "ui.widget.field.Spinner")
	protected Integer height;

	@Column(length = 2000)
	@BzFld(name = "样式编辑", order = 4)
	protected String style;

	@BzFld(name = "背景", order = 10, uiTemplate = "ui.widget.field.CssBackground")
	protected CssBackground background;

	@BzFld(name = "边框", order = 11, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder border;

	@BzFld(name = "顶边框", order = 12, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderTop;

	@BzFld(name = "底边框", order = 13, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderBottom;

	@BzFld(name = "左边框", order = 14, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderLeft;

	@BzFld(name = "右边框", order = 15, uiTemplate = "ui.widget.field.CssBorder")
	protected CssBorder borderRight;

	// @BzFld(name = "字体", order = 9, uiTemplate = "ui.widget.field.CssFont")
	// protected CssFont font;

	public CssBox() {
		this("");
	}

	public CssBox(String str) {
		super(str);
	}

	@Override
	protected void init(CssBox obj) {
		if (obj != null) {
			this.width = obj.width;
			this.height = obj.height;
			this.background = obj.background;
			this.border = obj.border;
			this.borderTop = obj.borderTop;
			this.borderRight = obj.borderRight;
			this.borderBottom = obj.borderBottom;
			this.borderLeft = obj.borderLeft;
			// this.font = obj.font;
			this.style = obj.style;
		}
	}

	public String toCssStyle(boolean autoBgSize) {
		StringBuffer sb = new StringBuffer();

		if (autoBgSize && background != null && background.getImage() != null) {
			Upload upl = background.getImage();
			String img = upl.toString();
			if (!Str.isEmpty(img)) {
				try {
					String rpt = background.getRepeat();
					int[] size = Img.size(Demsy.contextDir + img);
					if (!"repeat".equals(rpt)) {
						if (width == null && !"repeat-x".equals(rpt))
							width = size[0];
						if (height == null && !"repeat-y".equals(rpt))
							height = size[1];
					}
				} catch (IOException e) {
				}
			}
		}

		if (width != null && width > 0) {
			sb.append("width:").append(width).append("px;");
		}
		if (height != null && height > 0)
			sb.append("height:").append(height).append("px;");
		if (background != null) {
			sb.append(background.toCssStyle());
		}
		if (border != null)
			sb.append(border.toCssStyle(""));
		if (borderTop != null)
			sb.append(borderTop.toCssStyle("-top"));
		if (borderRight != null)
			sb.append(borderRight.toCssStyle("-right"));
		if (borderBottom != null)
			sb.append(borderBottom.toCssStyle("-bottom"));
		if (borderLeft != null)
			sb.append(borderLeft.toCssStyle("-left"));
		// if (font != null)
		// sb.append(font.toCssStyle());

		return sb.toString();
	}

	public CssBackground getBackground() {
		return background;
	}

	public CssBorder getBorder() {
		return border;
	}

	//
	// public CssFont getFont() {
	// return font;
	// }

	public void setBackground(CssBackground background) {
		this.background = background;
	}

	public void setBorder(CssBorder border) {
		this.border = border;
	}

	//
	// public void setFont(CssFont font) {
	// this.font = font;
	// }

	public CssBorder getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(CssBorder borderTop) {
		this.borderTop = borderTop;
	}

	public CssBorder getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(CssBorder borderRight) {
		this.borderRight = borderRight;
	}

	public CssBorder getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(CssBorder borderBottom) {
		this.borderBottom = borderBottom;
	}

	public CssBorder getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(CssBorder borderLeft) {
		this.borderLeft = borderLeft;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}

package com.kmetop.demsy.comlib.biz.field;

import com.kmetop.demsy.comlib.biz.ann.BzFld;

@BzFld(precision = 2000, uiTemplate = "ui.widget.field.Composite")
public class CssPosition extends JsonField<CssPosition> {

	@BzFld(name = "区域", order = 1, options = "page:内容,top:顶部,bottom:底部")
	private String area;

	@BzFld(name = "位置", order = 2, options = "absolute:绝对位置,relative:相对位置")
	private String position;

	@BzFld(name = "对齐", order = 3, disabledNavi = true, options = "1:左,2:右,3:顶,4:底")
	private Integer align;

	@BzFld(name = "距左", order = 4, uiTemplate = "ui.widget.field.Spinner")
	private Integer left;

	@BzFld(name = "距顶", order = 5, uiTemplate = "ui.widget.field.Spinner")
	private Integer top;

	@BzFld(name = "宽度", order = 6, uiTemplate = "ui.widget.field.Spinner")
	private Integer width;

	@BzFld(name = "高度", order = 7, uiTemplate = "ui.widget.field.Spinner")
	private Integer height;

	public CssPosition() {
		this("");
	}

	public CssPosition(String str) {
		super(str);
	}

	@Override
	protected void init(CssPosition obj) {
		if (obj != null) {
			this.area = obj.area;
			this.position = obj.position;
			this.align = obj.align;
			this.left = obj.left;
			this.top = obj.top;
			this.width = obj.width;
			this.height = obj.height;
		}
	}

	public String getPosition() {
		return position;
	}

	public Integer getLeft() {
		return left;
	}

	public Integer getTop() {
		return top;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getAlign() {
		return align;
	}

	public void setAlign(Integer align) {
		this.align = align;
	}

}

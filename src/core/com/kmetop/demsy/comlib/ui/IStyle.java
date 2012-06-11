package com.kmetop.demsy.comlib.ui;

import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IStyle extends IBizComponent {
	//
	// public CssBox getBox();
	//
	// public CssBox getTop();
	//
	// public CssBox getTopL();
	//
	// public CssBox getTopT();
	//
	// public CssBox getTopR();
	//
	// public CssBox getData();
	//
	// public CssBox getItem();
	//
	// public CssBox getItemL();
	//
	// public CssBox getItemT();
	//
	// public CssBox getItemR();

	public String getCssClass();

	public String getCssStyle();

	public IStyle getParent();

	public void setCode(String code);
}

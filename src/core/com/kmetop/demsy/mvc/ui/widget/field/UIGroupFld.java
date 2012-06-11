package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UIGroupFld extends UIBizFld {

	public UIGroupFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public int getColSpan() {
		return rowSize * 2;
	}
}

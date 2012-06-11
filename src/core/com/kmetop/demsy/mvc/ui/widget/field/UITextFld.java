package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UITextFld extends UIBizFld {

	public UITextFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public boolean supportColSpan() {
		return true;
	}
}

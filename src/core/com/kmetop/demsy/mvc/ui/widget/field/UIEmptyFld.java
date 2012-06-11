package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UIEmptyFld extends UIBizFld {

	public UIEmptyFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public UIEmptyFld() {
		super(null, null);
	}
}

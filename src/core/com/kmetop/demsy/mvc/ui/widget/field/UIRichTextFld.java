package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UIRichTextFld extends UIBizFld {

	public UIRichTextFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public boolean supportColSpan() {
		return true;
	}
}

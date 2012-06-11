package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UIStrFld extends UIBizFld {

	private String inputType="text";// input字段类型： text、password、......

	public UIStrFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public String getInputType() {
		return inputType;
	}

	public UIStrFld setInputType(String inputType) {
		this.inputType = inputType;

		return this;
	}

}

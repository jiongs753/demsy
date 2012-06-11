package com.kmetop.demsy.comlib.biz.field;

import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.lang.Str;

@BzFld(columnDefinition = "text")
public class RichText implements IExtField {
	private String text;

	public RichText() {
		this("");
	}

	public RichText(String t) {
		this.text = t;
	}

	public String toString() {
		if (Str.isEmpty(text)) {
			return "";
		}
		return text;
	}
}

package com.kmetop.demsy.comlib.biz.field;

import com.kmetop.demsy.comlib.biz.ann.BzFld;

@BzFld(precision = 255)
public class Upload implements IExtField {

	private String path;

	public Upload() {
		this("");
	}

	public Upload(String str) {
		if (str == null)
			this.path = "";
		else
			this.path = str;
	}

	public String getPath() {
		return path;
	}

	public String toString() {
		return path;
	}

}

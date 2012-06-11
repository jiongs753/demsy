package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UIUploadFld extends UIBizFld {

	private String uploadUrl;

	private String uploadType;

	public UIUploadFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public UIUploadFld setContext(Map c) {
		super.context = c;

		return this;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public UIUploadFld setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;

		return this;
	}

	public String getUploadType() {
		return uploadType;
	}

	public UIUploadFld setUploadType(String uploadType) {
		if (uploadType != null)
			this.uploadType = uploadType.replace(",", ";").replace("\r\n", ";").replace("\n", ";").replace("  ", " ").replace(" ", ";").replace("\t", ";");

		return this;
	}
}

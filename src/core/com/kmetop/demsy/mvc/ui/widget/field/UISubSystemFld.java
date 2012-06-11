package com.kmetop.demsy.mvc.ui.widget.field;

import java.io.Serializable;
import java.util.Map;

public class UISubSystemFld extends UIBizFld {
	private String uploadUrl;

	private String uploadType;

	private boolean fake;

	public UISubSystemFld(Map ctx, Serializable id) {
		super(ctx, id);
	}

	public UISubSystemFld setContext(Map c) {
		super.context = c;

		return this;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public UISubSystemFld setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;

		return this;
	}

	public String getUploadType() {
		return uploadType;
	}

	public UISubSystemFld setUploadType(String uploadType) {
		if (uploadType != null)
			this.uploadType = uploadType.replace(",", ";").replace("\r\n", ";").replace("\n", ";").replace("  ", " ").replace(" ", ";").replace("\t", ";");

		return this;
	}

	public boolean supportColSpan() {
		return true;
	}

	public void addChild(UIBizFld field) {
		this.children.add(field);
	}

	public boolean isFake() {
		return fake;
	}

	public boolean getFake() {
		return fake;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}
}

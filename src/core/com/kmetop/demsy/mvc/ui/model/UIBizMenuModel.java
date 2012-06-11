package com.kmetop.demsy.mvc.ui.model;

import java.io.Serializable;

import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.mvc.ui.widget.menu.UIBizMenu;

public class UIBizMenuModel<T extends UIBizMenu> extends UIWidgetModel<T, Nodes> {

	public UIBizMenuModel(T model, Nodes data) {
		super(model, data);
	}

	public UIBizMenuModel<T> setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public UIBizMenuModel<T> setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}
}

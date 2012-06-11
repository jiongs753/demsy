package com.kmetop.demsy.mvc.ui.model;

import java.io.Serializable;

import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.mvc.ui.widget.UIBizNavi;

public class UIBizNaviModel extends UIWidgetModel<UIBizNavi, Nodes> {

	public UIBizNaviModel(UIBizNavi model, Nodes data) {
		super(model, data);
	}

	public UIBizNaviModel setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public UIBizNaviModel setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}
}

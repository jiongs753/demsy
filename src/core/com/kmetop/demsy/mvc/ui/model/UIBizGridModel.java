package com.kmetop.demsy.mvc.ui.model;

import java.io.Serializable;

import com.kmetop.demsy.mvc.ui.widget.UIBizGrid;
import com.kmetop.demsy.orm.Pager;

/**
 * GRID业务模型：窗体为GRID,数据为分页数据
 * 
 * @author yongshan.ji
 * 
 */
public class UIBizGridModel extends UIWidgetModel<UIBizGrid, Pager> {

	public UIBizGridModel(UIBizGrid model, Pager data) {
		super(model, data);
	}

	public UIBizGridModel setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public UIBizGridModel setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}
}

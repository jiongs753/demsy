package com.kmetop.demsy.mvc.ui.model;

import java.io.Serializable;

import com.kmetop.demsy.mvc.ui.widget.UIBizForm;

/**
 * 表单模型： 窗体为业务表单，数据为实体对象
 * 
 * @author yongshan.ji
 * 
 */
public class UIBizFormModel extends UIWidgetModel<UIBizForm, Object> {

	public UIBizFormModel(UIBizForm model, Object data) {
		super(model, data);
	}

	public UIBizFormModel setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public UIBizFormModel setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}
}

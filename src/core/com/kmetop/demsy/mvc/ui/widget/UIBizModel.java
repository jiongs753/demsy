package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Map;


/**
 * 业务窗体：支持业务逻辑的窗体，用来描述业务窗体展现模型。
 * <UL>
 * <LI>业务窗体具有和服务器的嵌入式交互功能(如：查询、保存、删除等)，而其他窗体则不具有该功能；
 * </UL>
 * 
 * @author yongshan.ji
 */
public abstract class UIBizModel extends UIWidget {

	public UIBizModel(Map ctx, Serializable id) {
		super(ctx, id);
	}

	protected String saveUrl;// 保存记录

	protected String deleteUrl;// 删除记录

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String editUrl) {
		this.saveUrl = editUrl;
	}
}

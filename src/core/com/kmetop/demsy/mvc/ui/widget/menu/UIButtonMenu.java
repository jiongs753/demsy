package com.kmetop.demsy.mvc.ui.widget.menu;

import java.io.Serializable;
import java.util.Map;

/**
 * 嵌入式菜单：通常用作表单提交按钮、数据列表单行操作按钮等
 * 
 * @author yongshan.ji
 * 
 */
public class UIButtonMenu extends UIBizMenu {

	public UIButtonMenu(Map ctx, Serializable id) {
		super(ctx,id);
	}

	public boolean isMultiple() {
		return false;
	}
}

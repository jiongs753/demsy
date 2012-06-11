package com.kmetop.demsy.mvc.ui.widget.menu;

import java.io.Serializable;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.widget.UIWidget;

/**
 * 业务操作菜单UI：用户点击业务操作菜单按钮并执行相关业务处理功能，通常被嵌入到业务窗口中。
 * 
 * @author yongshan.ji
 * 
 */
public abstract class UIBizMenu extends UIWidget {
	public UIBizMenu(Map ctx, Serializable id) {
		super(ctx, id);
	}

}

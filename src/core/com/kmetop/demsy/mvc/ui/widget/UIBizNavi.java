package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.widget.menu.UIContextMenu;

/**
 * 数据分类导航菜单: 是一个业务窗体。
 * <UL>
 * <LI>业务窗体更多特性请参见{@link UIBizModel}
 * <LI>支持异步加载下级节点；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UIBizNavi extends UIBizModel {
	public UIBizNavi(Map ctx, Serializable id) {
		super(ctx, id);
	}

	protected UIContextMenu contextMenu;// 嵌入式右键环境菜单

	public UIContextMenu getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(UIContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

}

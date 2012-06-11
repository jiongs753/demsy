package com.kmetop.demsy.mvc.ui.widget;

import java.io.Serializable;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.model.UIBizGridModel;
import com.kmetop.demsy.mvc.ui.model.UIBizMenuModel;
import com.kmetop.demsy.mvc.ui.model.UIBizNaviModel;
import com.kmetop.demsy.mvc.ui.widget.menu.UIButtonMenu;
import com.kmetop.demsy.mvc.ui.widget.menu.UIContextMenu;
import com.kmetop.demsy.mvc.ui.widget.menu.UIToolbarMenu;

/**
 * 业务系统主界面：由Grid、工具栏菜单、右键环境菜单、分页菜单、数据导航等部分组成。
 * 
 * @author yongshan.ji
 * 
 */
public class UIBizSystem extends UIWidget {

	public UIBizSystem(Map ctx, Serializable id) {
		super(ctx, id);
	}

	protected UIBizGridModel grid;

	protected UIBizMenuModel<UIToolbarMenu> toolbarMenu;// 工具栏菜单

	protected UIBizMenuModel<UIContextMenu> contextMenu;// 右键环境菜单

	protected UIBizMenuModel<UIButtonMenu> pageMenu;// 分页栏按钮

	protected UIBizNaviModel naviMenu;// 数据导航菜单

	public UIBizGridModel getGrid() {
		return grid;
	}

	public UIBizMenuModel<UIToolbarMenu> getToolbarMenu() {
		return toolbarMenu;
	}

	public UIBizMenuModel<UIContextMenu> getContextMenu() {
		return contextMenu;
	}

	public UIBizMenuModel<UIButtonMenu> getPageMenu() {
		return pageMenu;
	}

	public UIBizNaviModel getNaviMenu() {
		return naviMenu;
	}

	public void setGrid(UIBizGridModel grid) {
		this.grid = grid;
	}

	public void setToolbarMenu(UIBizMenuModel<UIToolbarMenu> toolbarMenu) {
		this.toolbarMenu = toolbarMenu;
	}

	public void setContextMenu(UIBizMenuModel<UIContextMenu> contextMenu) {
		this.contextMenu = contextMenu;
	}

	public void setPageMenu(UIBizMenuModel<UIButtonMenu> pageMenu) {
		this.pageMenu = pageMenu;
	}

	public void setNaviMenu(UIBizNaviModel navi) {
		this.naviMenu = navi;
	}
}
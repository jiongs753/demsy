package com.kmetop.demsy.comlib;

import java.util.List;

import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IUIViewType;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.mvc.ui.IUIViewController;
import com.kmetop.demsy.mvc.ui.model.UIBizFormModel;
import com.kmetop.demsy.mvc.ui.model.UIBizGridModel;
import com.kmetop.demsy.mvc.ui.model.UIBizMenuModel;
import com.kmetop.demsy.mvc.ui.model.UIBizNaviModel;
import com.kmetop.demsy.mvc.ui.model.UIBlockViewModel;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIBizModule;
import com.kmetop.demsy.mvc.ui.widget.UIBizSystem;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;
import com.kmetop.demsy.mvc.ui.widget.menu.UIToolbarMenu;
import com.kmetop.demsy.orm.IOrm;

public interface IUiEngine {

	/**
	 * 清空缓存的UI模型
	 */
	public void clearCache();

	public UIPageView makePageUI(String pageID);

	/**
	 * 获取功能模块菜单数据模型
	 * 
	 * @param module
	 * @return
	 * @throws DemsyException
	 */
	public UIWidgetModel makeModuleMenuUI(IDemsySoft soft) throws DemsyException;

	public UIBizModule makeBizModuleUI(IModule module, String gridColumns, String idField) throws DemsyException;

	/**
	 * 获取模块主界面
	 * 
	 * @param module
	 *            业务模块
	 * @return 业务模块主界面
	 * @throws DemsyException
	 */
	public UIBizSystem makeSystemUI(IModule module, String gridColumns, String idField) throws DemsyException;

	/**
	 * 获取模块数据网格
	 * 
	 * @param module
	 *            业务模块
	 * @param gridColumns
	 *            数据网格字段， 如果该值为一个数字，则表示在网格中显示多少列？否则表示显示在网格中显示哪些字段？
	 * @return 业务模块主界面
	 * @throws DemsyException
	 */
	public UIBizGridModel makeGridUI(IModule module, String gridColumns, String idField, boolean existNaviTree) throws DemsyException;

	/**
	 * 
	 * @param module
	 *            业务模块
	 * @return 业务模块导航菜单
	 * @throws DemsyException
	 */
	public UIBizNaviModel makeNaviUI(IModule module) throws DemsyException;

	/**
	 * 获取模块工具栏惨淡
	 * 
	 * @param module
	 * @return
	 * @throws DemsyException
	 */
	public UIBizMenuModel<UIToolbarMenu> makeToolbarUI(IModule module) throws DemsyException;

	/**
	 * 获取子模块界面
	 * 
	 * @param module
	 * @return
	 * @throws DemsyException
	 */
	// public TabsDataModel getSlaveUI(IModule module) throws DemsyException;

	/**
	 * 获取模块业务表单
	 * 
	 * @param module
	 * @param action
	 * @return
	 * @throws DemsyException
	 */
	public UIBizFormModel makeFormUI(IModule module, IAction action, Object data) throws DemsyException;

	/*
	 * 获取内置模块业务窗体模型
	 */

	// public BizModuleUI getStaticMainUI(int entityID) throws DemsyException;
	//
	// public GridDataModel getStaticGrid(int entityID) throws DemsyException;
	//
	// public NaviDataModel getStaticNaviMenu(int entityID) throws
	// DemsyException;
	//
	// public MenuDataModel<ToolbarMenu> getStaticActionTMenu(int entityID)
	// throws DemsyException;
	//
	// public FormDataModel getStaticForm(int entityID, int actionID) throws
	// DemsyException;

	public IPage loadPageTemplate(Long pageID);

	public IPageBlock loadPageBlock(Long blockID);

	public List<? extends IPageBlock> loadPageBlocks(Long pageID);

	public IUIViewType loadViewType(Long id);

	public IUIViewController getUIController(String classname);

	public UIPageView makePageView(Long pageID, Long dynamicBlockID, Long dynamicModuleID, Long dynamicDataID);

	public UIPageView makePageView(IPage uiPage, IPageBlock dynamicBlock, Long dynamicModuleID, Long dynamicDataID);

	/**
	 * 
	 * @param uiPage
	 *            页面视图
	 * @param parentExprModel
	 *            板块父视图
	 * @param pageBlock
	 *            页面板块
	 * @param dynamicModuleID
	 *            动态模块ID
	 * @param dynamicDataID
	 *            动态数据ID
	 * @param pathModule
	 *            URL路径模块
	 * @param pathData
	 *            URL路径模块指定的数据
	 * @return
	 */
	public UIBlockViewModel makeBlockView(IPageBlock pageBlock, Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData);

	public IStyle loadStyle(Long styleID);

	public Nodes makeViewTypeNodes(Long blockID);

	public IWebContentCatalog loadWebInfoCatalog(Long catalogID);

	public IWebContentCatalog loadWebInfoCatalog(String catalogGuid);

	public IPage loadIndexPage();

	public void addClickNum(IOrm orm, IStatistic obj);

	public void addCommentNum(IOrm orm, IStatistic obj);

}

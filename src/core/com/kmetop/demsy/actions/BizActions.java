package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.Demsy.uiEngine;
import static com.kmetop.demsy.comlib.LibConst.F_ID;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;
import static com.kmetop.demsy.mvc.MvcConst.VW_BIZ;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.biz.IBizManager;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Http;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Status;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.SystemExcel;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ObjcetNaviNode;
import com.kmetop.demsy.mvc.nutz.DemsyAdaptor;
import com.kmetop.demsy.mvc.nutz.DemsyUploadAdaptor;
import com.kmetop.demsy.mvc.ui.model.UIBizFormModel;
import com.kmetop.demsy.mvc.ui.model.UIBizGridModel;
import com.kmetop.demsy.mvc.ui.model.UIBizMenuModel;
import com.kmetop.demsy.mvc.ui.model.UIBizNaviModel;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIBizGrid;
import com.kmetop.demsy.mvc.ui.widget.UIBizModule;
import com.kmetop.demsy.mvc.ui.widget.UIBizSystem;
import com.kmetop.demsy.mvc.ui.widget.menu.UIToolbarMenu;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 访问模块业务窗体组件
 * <p>
 * <B>路径格式：/{widget}/{moduleID}[:[{dacorator}]]</B>
 * <UL>
 * <LI>描述：用特定的页面模板装饰模块业务窗体，即用模块业务窗体填充页面模板中的占位符；
 * <LI>widget——业务窗体组件名称；可以是：
 * <OL>
 * <LI>main——模块主界面
 * <LI>grid——模块数据网格
 * <LI>navi——模块数据分类导航菜单
 * <LI>tmenu——模块工具栏操作菜单
 * </OL>
 * <LI>moduleID——模块标识，必需的路径参数，可以是数字型的ID或字符串型的编号(编号必须唯一)；
 * <LI>dacorator——页面模板标识，可选的路径参数，可以是数字型的ID或字符串型的编号(编号必须唯一)；
 * <LI>dacorator为空串且冒号(:)不存在——表示装饰器为null，将使用默认页面模板装饰业务窗体；
 * <LI>dacorator为空但冒号(:)存在——表示装饰器为空串，将不装饰业务窗体，通常用于AJAX嵌入式异步加载页面板块；
 * <LI>如：
 * <OL>
 * <LI>/main/8——同[/main/8:]表示访问模块(8)主界面业务窗体，并使用(默认)页面模板对主界面进行装饰；
 * <LI>/grid/9:6——表示访问模块(9)数据网格业务窗体，并使用页面模板(6)对数据网格进行装饰；
 * <LI>/navi/10:——同[/navi/10]表示访问模块(10)数据分类导航菜单，并使用(默认)页面模板对数据分类菜单进行装饰；
 * </OL>
 * </UL>
 * <B>路径格式：/{widget}/^{moduleID}</B>
 * <UL>
 * <LI>描述：表示获取业务窗体的展示数据
 * <LI>widget——业务窗体组件名称；可以是：
 * <OL>
 * <LI>grid——模块数据网格
 * <LI>navi——模块数据分类导航菜单
 * <LI>tmenu——模块工具栏操作菜单
 * </OL>
 * <LI>moduleID——说明同上；
 * </UL>
 * <B>路径格式：/{widget}/{moduleID}[:{dacorator}]/</B>
 * <UL>
 * <LI>描述：访问模块业务表单。在浏览器中输入URL地址可以看到窗体用户界面；
 * <LI>widget——业务窗体组件名称；
 * <LI>moduleID——模块标识，必需的路径参数，可以是数字型的ID或字符串型的编号(编号必须唯一)；
 * <LI>dacorator——页面模板标识，可选的路径参数，可以是数字型的ID或字符串型的编号(编号必须唯一)；
 * <LI>如果没有指定dacorator，则表示使用默认页面模板装饰业务窗体；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
@Ok(VW_BIZ)
@Fail(VW_BIZ)
@AdaptBy(type = DemsyAdaptor.class)
public class BizActions extends ModuleActions implements BizConst, MvcConst {

	private UIWidgetModel buildModel(String title, String moduleParam, WidgetBuilder builder) throws DemsyException {
		log.debugf("创建业务窗体<%s>...[%s]", title, moduleParam);

		try {
			// security.checkLogin(IUserRole.ROLE_USER);

			boolean ajaxData = isAjaxParam(moduleParam);
			String[] params = parseParam(ajaxData ? moduleParam.substring(1) : moduleParam);
			String moduleID = params[0];
			String pageID = params[1];

			IBizManager bizManager = getBizManager(moduleID);

			UIWidgetModel ret = builder.build(bizManager, bizManager.getModule(), pageID, ajaxData);

			log.debugf("创建业务窗体<%s>结束. [module=%s]", title, bizManager.getModule());

			return ret;

		} catch (ConfigException e) {
			throw e;
		} catch (Throwable e) {
			String msg = Ex.msg(e);
			log.debugf("创建业务窗体<%s>出错! %s", title, msg);

			throw new DemsyException(Ex.msg(e));
		}
	}

	@At(URL_BZMAIN)
	public UIWidgetModel main(final String moduleParam) throws DemsyException {
		return tabs(moduleParam);
	}

	public UIWidgetModel<UIBizModule, Object> tabs(final String moduleParam) throws DemsyException {
		return (UIWidgetModel<UIBizModule, Object>) buildModel("TABS界面", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule masterModule, String pageID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizModule mainUI = uiEngine.makeModuleView(masterModule, getGridColumns(), idfld);

				if (mainUI.getMaster() != null) {
					Nodes masterRoot = Nodes.make();
					masterRoot.addNode(null, masterModule.getId()).setName(masterModule.getName()).setParams(MvcUtil.contextPath(URL_BZSYS, masterModule.getId()));
					mainUI.getMaster().setData(masterRoot);
				} else {
					if (mainUI.getNaviMenu() != null) {
						Nodes naviData = bizEngine.makeNaviNodes(moduleEngine.getSystem(masterModule), idfld);
						if (naviData.getSize() > 0) {
							mainUI.getNaviMenu().setData(naviData);
						} else {
							mainUI.setNaviMenu(null);
						}
					}
					mainUI.getToolbarMenu().setData(moduleEngine.makeNodesByAction(masterModule));
				}

				// 主系统和从系统分属不同的TABS中
				// if (mainUI.getSlave() != null) {
				// Nodes slaveRoot = Nodes.make();
				// List<IModule> slaveModules =
				// entityComLib.getSlaveModules(masterModule);
				// int len = Math.min(5, slaveModules.size());
				// for (int i = 0; i < len; i++) {
				// IModule module = slaveModules.get(i);
				// slaveRoot.addNode(null,
				// module.getId()).setName(module.getName()).setParams(MVCUtil.contextPath(AT_BZMASTER,
				// module.getId())).set("moduleID", module.getId());
				// }
				// mainUI.getSlave().setData(slaveRoot);
				// }

				return new UIWidgetModel(mainUI, null).setDacorator(pageID).setAjaxData(ajaxData);
			}
		});
	}

	/**
	 * 
	 * 业务数据查询表达式，表达式弹出窗口包括右边的GRID和左边的数据分类菜单。
	 * 
	 * @param moduleParam
	 *            参数结构：moduleID:comboboxFieldID
	 * @return
	 * @throws DemsyException
	 */
	@At(URL_BZSYS_COMB_EXPR)
	public UIWidgetModel<UIBizSystem, Object> systemCombExpr(String moduleParam, final String dataID) throws DemsyException {
		return (UIWidgetModel<UIBizSystem, Object>) buildModel("系统条件引用", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String comboboxFieldID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizSystem modelUI = uiEngine.makeSystemView(mdl, getGridColumns(), idfld);

				UIBizGrid grid = modelUI.getGrid().getModel();
				grid.setRownumbers(false);
				grid.setSearch(false);
				grid.setRowList(null);

				Nodes naviData = bizEngine.makeNaviNodes(moduleEngine.getSystem(mdl), idfld);
				if (naviData.getSize() > 0) {
					modelUI.getNaviMenu().setData(naviData);
				} else {
					modelUI.setNaviMenu(null);
				}
				// modelUI.getToolbarMenu().setData(moduleEngine.makeActionNodes(mdl));

				String queryString = MvcUtil.requestQueryJsonString(Demsy.me().request());
				if (!Str.isEmpty(queryString)) {
					UIBizGridModel gridModel = modelUI.getGrid();
					gridModel.set("postData", queryString);
				}

				UIWidgetModel model = new UIWidgetModel(modelUI, null).setDacorator("").setAjaxData(ajaxData);
				model.setTemplate("ui.widget.BizSystemUI_Comb");
				model.set("comboboxID", Demsy.me().param("comboboxID", String.class, null));
				model.set("comboboxType", "expr");

				return model;
			}
		});
	}

	/**
	 * 业务数据分类表达式，表达式弹出窗口不包括右边的GRID，只包含左边的分类菜单。
	 * 
	 * @param moduleParam
	 * @param dataID
	 * @return
	 * @throws DemsyException
	 */
	@At(URL_BZSYS_COMB_CATALOG_EXPR)
	public UIWidgetModel<UIBizSystem, Object> systemCombFkExpr(String moduleParam, final String dataID) throws DemsyException {
		UIWidgetModel<UIBizSystem, Object> ret = this.systemCombExpr(moduleParam, dataID);
		
		//授权模块BUG：注释下面一条代码
		//ret.set("comboboxType", "c_expr");

		return ret;
	}

	@At(URL_BZSYS_COMB_FK)
	public UIWidgetModel<UIBizSystem, Object> systemCombFK(String moduleParam, final String dataID) throws DemsyException {
		return (UIWidgetModel<UIBizSystem, Object>) buildModel("系统外键引用", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String comboboxFieldID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizSystem modelUI = uiEngine.makeSystemView(mdl, getGridColumns(), idfld);

				UIBizGrid grid = modelUI.getGrid().getModel();
				grid.setRownumbers(false);
				grid.setSearch(false);
				grid.setRowList(null);

				Nodes naviData = bizEngine.makeNaviNodes(moduleEngine.getSystem(mdl), idfld);
				if (naviData.getSize() > 0) {
					modelUI.getNaviMenu().setData(naviData);
				} else {
					modelUI.setNaviMenu(null);
				}
				// modelUI.getToolbarMenu().setData(moduleEngine.makeActionNodes(mdl));

				String queryString = MvcUtil.requestQueryJsonString(Demsy.me().request());
				if (!Str.isEmpty(queryString)) {
					UIBizGridModel gridModel = modelUI.getGrid();
					gridModel.set("postData", queryString);
				}

				UIWidgetModel model = new UIWidgetModel(modelUI, null).setDacorator("").setAjaxData(ajaxData);
				model.setTemplate("ui.widget.BizSystemUI_Comb");
				model.set("comboboxID", Demsy.me().param("comboboxID", String.class, null));
				model.set("comboboxType", "fk");

				// grid.setMultiselect(false);
				grid.setAltRows(true);

				return model;
			}
		});
	}

	/**
	 * 访问模块业务窗体(业务主界面)
	 * 
	 * @param moduleParam
	 *            模块参数——模块ID:页面ID
	 * @return 模块主界面UI
	 */
	@At(URL_BZSYS)
	public UIWidgetModel<UIBizSystem, Object> system(String moduleParam) throws DemsyException {
		return (UIWidgetModel<UIBizSystem, Object>) buildModel("系统主界面", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String pageID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizSystem modelUI = uiEngine.makeSystemView(mdl, getGridColumns(), idfld);

				Nodes naviData = bizEngine.makeNaviNodes(moduleEngine.getSystem(mdl), idfld);
				if (naviData.getSize() > 0) {
					modelUI.getNaviMenu().setData(naviData);
				} else {
					modelUI.setNaviMenu(null);
				}
				modelUI.getToolbarMenu().setData(moduleEngine.makeNodesByAction(mdl));

				String queryString = MvcUtil.requestQueryJsonString(Demsy.me().request());
				if (!Str.isEmpty(queryString)) {
					UIBizGridModel gridModel = modelUI.getGrid();
					gridModel.set("postData", queryString);
				}

				return new UIWidgetModel(modelUI, null).setDacorator(pageID).setAjaxData(ajaxData);
			}
		});
	}

	//
	// public TabsDataModel slave(String moduleParam) throws DemsyException {
	// return (TabsDataModel) buildModel(moduleParam, new WidgetBuilder() {
	// public DataModel build(IBizManager manager, IModule mdl, String pageID,
	// boolean ajaxData) throws DemsyException {
	// TabsDataModel dataModel =
	// uiEngine.getSlaveUI(mdl).setDacorator(pageID).setAjaxData(ajaxData);
	//
	// Nodes root = Nodes.make();
	// List<IModule> modules = moduleEngine.getSlaveModules(mdl);
	// for (IModule module : modules) {
	// root.addNode(null,
	// module.getId()).setName(module.getName()).setParams(MVCUtil.contextPath(URL_BZSYS,
	// module.getId()));
	// }
	// dataModel.setData(root);
	//
	// return dataModel;
	// }
	// });
	// }

	private String getIdField(Class type) {
		String ret = Demsy.me().param("idField", String.class, null);

		if (!Cls.hasField(type, ret)) {
			return LibConst.F_ID;
		}

		return ret;
	}

	private String getGridColumns() {
		return Demsy.me().param("gridColumns", String.class, null);
	}

	/**
	 * 访问模块业务窗体(数据网格)
	 * 
	 * @param moduleParam
	 *            模块参数——模块ID:页面ID
	 */
	@At(URL_BZGRID)
	public UIBizGridModel grid(String moduleParam) throws DemsyException {
		return (UIBizGridModel) buildModel("数据网格", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String pageID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizGridModel dataModel = uiEngine.makeSystemGridView(mdl, getGridColumns(), idfld, false).setDacorator(pageID).setAjaxData(ajaxData);

				if (ajaxData) {
					Class bizClass = bizEngine.getType(moduleEngine.getSystem(mdl));
					log.tracef("获取业务类 [%s]", bizClass);

					Pager pager = new Pager(bizClass);
					log.tracef("创建 DataPager [%s]", pager);

					pager.setQueryExpr(getBizCndPageExpr(manager.orm(), bizClass));
					if (log.isTraceEnabled())
						log.tracef("计算 DataPager 查询表达式 [%s]", pager.getQueryExpr());

					manager.query(pager, null);
					if (log.isTraceEnabled())
						log.tracef("查询数据 [index=%s/%s, size=%s, sum=%s]", pager.getPageIndex(), pager.getTotalPage(), pager.getPageSize(), pager.getTotalRecord());

					dataModel.setData(pager);
				}

				return dataModel;
			}
		});
	}

	/**
	 * 访问模块业务窗体(数据分类导航菜单)
	 * 
	 * @param moduleParam
	 *            模块参数——模块ID:页面ID
	 */
	@At(URL_BZNAVI)
	public UIBizNaviModel navi(String moduleParam) throws DemsyException {
		return (UIBizNaviModel) buildModel("导航菜单", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String pageID, boolean ajaxData) throws DemsyException {
				String idfld = getIdField(manager.getType());

				UIBizNaviModel dataModel = uiEngine.makeSystemNaviView(mdl);
				if (dataModel != null) {
					dataModel.setDacorator(pageID).setAjaxData(ajaxData);
					dataModel.setData(bizEngine.makeNaviNodes(moduleEngine.getSystem(mdl), idfld));
				}

				return dataModel;
			}
		});
	}

	/**
	 * 访问模块业务窗体(工具栏操作菜单)
	 * 
	 * @param moduleParam
	 *            模块参数——模块ID:页面ID
	 */
	@At(URL_BZMENU)
	public UIBizMenuModel<UIToolbarMenu> tmenu(String moduleParam) throws DemsyException {
		return (UIBizMenuModel<UIToolbarMenu>) buildModel("工具栏菜单", moduleParam, new WidgetBuilder() {
			public UIWidgetModel build(IBizManager manager, IModule mdl, String pageID, boolean ajaxData) throws DemsyException {
				UIBizMenuModel<UIToolbarMenu> dataModel = uiEngine.makeSystemActionView(mdl).setDacorator(pageID).setAjaxData(ajaxData);

				dataModel.setData(moduleEngine.makeNodesByAction(mdl));

				return dataModel;
			}
		});
	}

	public static UIBizFormModel buildForm(String title, final String moduleParam, final String actionParam, final ObjcetNaviNode dataNode, String submitUrl) throws DemsyException {
		log.debugf("创建业务表单<%s>...[module=%s, action=%s]", title, moduleParam, actionParam);

		try {
			// security.checkLogin(IUserRole.ROLE_USER);

			boolean ajaxData = isAjaxParam(moduleParam);
			String[] mParams = parseParam(ajaxData ? moduleParam.substring(1) : moduleParam);
			String[] aParams = parseParam(actionParam);
			String moduleID = mParams[0];
			String pageID = mParams[1];
			String actionID = aParams[0];
			String dataID = aParams[1];

			IBizManager bizManager = getBizManager(moduleID);
			IModule mdl = bizManager.getModule();

			IAction action = moduleEngine.getAction(mdl, actionID);
			log.tracef("获取模块操作 [actionMode=%s]", actionID);

			Class bizClass = bizEngine.getType(moduleEngine.getSystem(mdl));
			log.tracef("获取业务类 [%s]", bizClass);

			Mirror mirror = Mirror.me(bizClass);
			Object data = null;
			String[] idArray = Str.toArray(dataID, ",");
			if (idArray != null && idArray.length == 1) {
				Long id = null;
				if (!Str.isEmpty(idArray[0])) {
					try {
						id = Long.parseLong(idArray[0]);
					} catch (NumberFormatException e) {
						throw new DemsyException("非法数据ID! [%s] %s", idArray[0], e);
					}
					data = bizManager.load(id, actionID);
				}

				// } else {
				// CndExpr expr = this.getBizCndExpr(bizManager.orm(),
				// bizClass);
				// if (bizManager.count(bizClass, actionID, expr) == 1) {
				// List list = bizManager.query(bizClass, actionID, expr);
				// if (list != null && list.size() == 1) {
				// data = dataNode.inject(mirror, list.get(0), null);
				// }
				// }
				//
				// data = dataNode.inject(mirror, data, null);
			}
			if (dataNode != null)
				data = dataNode.inject(mirror, data, null);
			bizEngine.loadFieldValue(data, moduleEngine.getSystem(mdl));

			log.tracef("加载并注入业务数据 [dataID=%s]", dataID);

			UIBizFormModel dataModel = uiEngine.makeSystemFormView(mdl, action, data).setDacorator(pageID).setAjaxData(ajaxData);
			if (action != null) {
				dataModel.setTemplate(action.getTemplate());
				dataModel.getModel().setName(action.getDesc());
			}

			if (!ajaxData) {
				String queryString = MvcUtil.requestQueryString(Demsy.me().request());
				if (!"v".equals(actionID)) {
					submitUrl = MvcUtil.contextPath(submitUrl, moduleParam, actionParam, Demsy.me().addToken());
					if (!Str.isEmpty(queryString)) {
						submitUrl += "?" + queryString;
					}
					dataModel.set("submitUrl", submitUrl);
				}
				String reloadUrl = MvcUtil.requestURI(Demsy.me().request());
				int actionIdx = reloadUrl.indexOf(URL_PREFIX_BZ) + URL_PREFIX_BZ.length();
				int slashIdx = reloadUrl.substring(actionIdx).indexOf("/") + 1;
				int dataIdx = actionIdx + slashIdx;
				dataModel.set("reloadUrl", reloadUrl.substring(0, dataIdx) + MvcConst.URL_PREFIX_AJAX + reloadUrl.substring(dataIdx));
			}
			dataModel.set("mode", actionID);
			boolean dialog = Demsy.me().param("dialog", Boolean.class, false);
			if (dialog)
				dataModel.set("dialog", "true");

			log.debugf("创建业务表单<%s>成功. [module=%s]", title, mdl);

			return dataModel;
		} catch (ConfigException e) {
			throw e;
		} catch (Throwable e) {
			String msg = Ex.msg(e);

			log.error(String.format("创建业务表单<%s>失败! %s", title, msg), e);

			throw new DemsyException(e);
		}
	}

	private List list(IBizManager bizManager, Class bizClass, String dataID, String actionID, CndExpr expr) throws DemsyException {
		List idList = Str.toList(dataID, ",");
		if (idList != null && idList.size() > 0) {
			if (expr == null)
				expr = makeOrderExpr(bizManager.orm(), bizClass, CndExpr.in(F_ID, idList));
			else
				expr = expr.and(makeOrderExpr(bizManager.orm(), bizClass, CndExpr.in(F_ID, idList)));
		} else {
			if (expr == null)
				expr = getBizCndOrderExpr(bizManager.orm(), bizClass);
			else
				expr = expr.and(getBizCndOrderExpr(bizManager.orm(), bizClass));
		}

		if (expr == null) {
			return null;
		}

		return bizManager.query(actionID, expr);
	}

	private Status exec(String title, final String moduleParam, final String actionParam, ObjcetNaviNode dataNode, LogicExecutor executor) throws DemsyException {
		log.debugf("执行业务逻辑<%s>...[module=%s, action=%s]", title, moduleParam, actionParam);

		StringBuffer errorInfo = new StringBuffer();
		try {
			boolean ajaxData = isAjaxParam(moduleParam);
			String[] mParams = parseParam(ajaxData ? moduleParam.substring(1) : moduleParam);
			String[] aParams = parseParam(actionParam);
			String moduleID = mParams[0];
			String actionID = aParams[0];
			String dataID = aParams[1];

			IBizManager bizManager = getBizManager(moduleID);
			IModule mdl = bizManager.getModule();

			IAction action = moduleEngine.getAction(mdl, actionID);
			if (action == null) {
				try {
					action = moduleEngine.getAction(mdl, Long.parseLong(actionID));
				} catch (Throwable e) {
					log.warnf("获取模块操作出错! %s", e);
				}
			}
			if (action == null) {
				throw new DemsyException("业务操作不存在! [actionID=%s]", actionID);
			}

			if (!Str.isEmpty(action.getError())) {
				errorInfo.append(action.getError());
			}
			log.tracef("获取模块操作 [actionID=%s]", actionID);

			Class bizClass = bizEngine.getType(moduleEngine.getSystem(mdl));
			log.tracef("获取业务类 [%s]", bizClass);

			Mirror mirror = Mirror.me(bizClass);
			Object data = dataNode.inject(mirror, null, null);

			Map<String, String> fieldMode = bizEngine.getMode(bizManager.getSystem(), action, data);

			Long softID = (Long) Obj.getValue(data, F_SOFT_ID);
			List list = null;
			switch (action.getTypeCode()) {
			case BizConst.TYPE_BZ_DEL:
				if (Str.isEmpty(dataID)) {
					throw new DemsyException("请先选中待删除的记录!");
				}
			case BizConst.TYPE_BZFORM_EDIT_N:
				if (Str.isEmpty(dataID)) {
					throw new DemsyException("请先选中一条或多条记录!");
				}
			case BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS:
			case BizConst.TYPE_BZ_EXEC_SYNC:
			case BizConst.TYPE_BZ_EXEC_ASYN:
			case BizConst.TYPE_BZFORM_EXPORT_XLS:
			case BizConst.TYPE_BZ_ORDERBY_UP:
			case BizConst.TYPE_BZ_ORDERBY_DOWN:
			case BizConst.TYPE_BZ_ORDERBY_REVERSE:
			case BizConst.TYPE_BZ_ORDERBY_CANCEL:
				list = this.list(bizManager, bizClass, dataID, actionID, null);
				break;
			case BizConst.TYPE_BZ_CLEAR:
				if (!Str.isEmpty(dataID)) {
					list = this.list(bizManager, bizClass, dataID, actionID, null);
				}
				break;
			case BizConst.TYPE_BZFORM_EDIT:
			case BizConst.TYPE_BZFORM_PRINT:
				list = new LinkedList();
				if (!Str.isEmpty(dataID))
					list.add(bizManager.load(Long.parseLong(dataID), actionID));
				break;
			case BizConst.TYPE_BZFORM_ADD_N:
				list = dataNode.injectList(mirror, fieldMode);
				break;
			default:
			}
			if (list == null || list.size() == 0) {
				data = dataNode.inject(mirror, null, fieldMode);
				bizEngine.loadFieldValue(data, moduleEngine.getSystem(mdl));

				bizEngine.validate(bizManager.getSystem(), action, data, fieldMode);

				if (Cls.hasField(bizClass, F_SOFT_ID) && (softID == null || softID <= 0)) {
					Obj.setValue(data, F_SOFT_ID, mdl.getSoftID());
				}

				executor.exec(bizManager, data, action);
			} else {
				data = null;
				if (Cls.hasField(bizClass, F_SOFT_ID) && (softID == null || softID <= 0)) {
					if (list.size() == 1) {
						data = list.get(0);
						dataNode.inject(mirror, data, fieldMode);

						bizEngine.validate(bizManager.getSystem(), action, data, fieldMode);

						Obj.setValue(data, F_SOFT_ID, mdl.getSoftID());
						executor.exec(bizManager, data, action);
					} else {
						for (Object ele : list) {
							dataNode.inject(mirror, ele, fieldMode);

							bizEngine.validate(bizManager.getSystem(), action, ele, fieldMode);

							Obj.setValue(ele, F_SOFT_ID, mdl.getSoftID());
						}
						executor.exec(bizManager, list, action);
					}
				} else {
					if (list.size() == 1) {
						data = list.get(0);
						dataNode.inject(mirror, data, fieldMode);

						bizEngine.validate(bizManager.getSystem(), action, data, fieldMode);

						executor.exec(bizManager, data, action);
					} else {
						for (Object ele : list) {
							dataNode.inject(mirror, ele, fieldMode);

							bizEngine.validate(bizManager.getSystem(), action, ele, fieldMode);
						}
						executor.exec(bizManager, list, action);
					}
				}

			}

			String info = action.getInfo();
			if (Str.isEmpty(info)) {
				info = "操作成功! ";
			}

			log.debugf("执行业务逻辑<%s>成功. [module=%s, action=%s]", title, mdl, action);

			return new Status(true, info, null, Obj.getId(data));
		} catch (Throwable e) {
			String msg = Ex.msg(e);

			log.debugf("执行业务逻辑<%s>失败! %s", title, msg);

			return new Status(false, errorInfo + msg);
		}
	}

	@At(URL_BZFORM_ADD)
	public UIBizFormModel add(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		return buildForm("新增", moduleParam, actionParam, dataNode, URL_BZ_SAVE);
	}

	@At(URL_BZFORM_IMPORT_XLS)
	public UIBizFormModel prepareImportFromXls(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		UIBizFormModel form = buildForm("准备从XLS导入", moduleParam, actionParam, dataNode, URL_BZ_IMPORT_XLS);

		return form;
	}

	@At(URL_BZ_IMPORT_XLS)
	@AdaptBy(type = DemsyUploadAdaptor.class)
	public UIBizFormModel importFromXls(@Param("upload") TempFile tmpfile, final String moduleParam, final String actionParam, final String token) throws DemsyException {
		StringBuffer info = new StringBuffer();
		if (!Demsy.me().existToken(token)) {
			info.append("不允许重复提交数据，请刷新页面后重试！");
		}
		if (tmpfile == null || tmpfile.getFile() == null) {
			info.append("Excel数据文件不存在！");
		}
		if (info.length() == 0) {
			try {
				boolean ajaxData = isAjaxParam(moduleParam);
				String[] mParams = parseParam(ajaxData ? moduleParam.substring(1) : moduleParam);
				String[] aParams = parseParam(actionParam);
				String moduleID = mParams[0];
				String actionID = aParams[0];

				IBizManager bizManager = getBizManager(moduleID);
				IModule mdl = bizManager.getModule();

				IAction action = moduleEngine.getAction(mdl, actionID);
				if (action == null) {
					try {
						action = moduleEngine.getAction(mdl, Long.parseLong(actionID));
					} catch (Throwable e) {
						log.warnf("获取模块操作出错! %s", e);
					}
				}
				if (action == null) {
					throw new DemsyException("业务操作不存在! [actionID=%s]", actionID);
				}

				if (!Str.isEmpty(action.getError())) {
					info.append(action.getError());
				}
				log.tracef("获取模块操作 [actionID=%s]", actionID);

				Class bizClass = bizEngine.getType(moduleEngine.getSystem(mdl));
				log.tracef("获取业务类 [%s]", bizClass);

				SystemExcel excel = new SystemExcel(bizManager.getSystem(), moduleEngine.getAction(mdl, "c"), tmpfile.getFile());
				excel.save();

				info.append("从Excel导入数据成功！");
			} catch (Throwable e) {
				String msg = Ex.msg(e);

				info.append(String.format("从Excel导入数据失败! %s", msg));
			}
		}

		UIBizFormModel ret = prepareImportFromXls(moduleParam, actionParam, null);

		if (info.length() > 0)
			ret.set("info", info.toString());

		return ret;
	}

	@At(URL_BZFORM_EXPORT_XLS)
	public UIBizFormModel prepareExportToXls(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		UIBizFormModel form = buildForm("准备导出到XLS", moduleParam, actionParam, dataNode, URL_BZ_EXPORT_XLS);

		Demsy ctx = Demsy.me();
		String fixedNaviRules = ctx.param("fixedNaviRules", String.class, null);
		String naviRules = ctx.param("naviRules", String.class, null);
		String sidx = ctx.param("sidx", String.class, "orderby,id");
		String sord = ctx.param("sord", String.class, "asc,desc");
		form.set("fixedNaviRules", fixedNaviRules);
		form.set("naviRules", naviRules);
		form.set("sidx", sidx);
		form.set("sord", sord);

		return form;
	}

	@At(URL_BZ_EXPORT_XLS)
	public void exportToXls(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {

		exec("导出数据到Excel", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				File file = null;
				try {
					Map<String, IBizField> fieldMap = bizEngine.getFieldsMap(manager.getSystem());
					Demsy ctx = Demsy.me();
					String filename = Demsy.appconfig.getTempDir() + File.separator + "exportToXls" + File.separator
					// + manager.getSystem().getName() + "("
							+ Dates.formatDate(new Date(), "yyyyMMdd-HHmmss")
							// + ")"
							+ ".xls";
					file = new File(filename);
					file.getParentFile().mkdirs();
					file.delete();
					file.createNewFile();
					String[] fields = ctx.param("fields", String[].class, null);
					List<String[]> excelResult = new LinkedList();
					List list = (List) obj;
					// 表头
					String[] row = new String[fields.length];
					int count = 0;
					for (String fld : fields) {
						row[count++] = fieldMap.get(fld).getName();
					}
					excelResult.add(row);
					// 数据
					for (Object v : list) {
						row = new String[fields.length];
						count = 0;
						for (String fld : fields) {
							IBizField bzfld = fieldMap.get(fld);
							row[count++] = Obj.getStringValue(v, fld, bzfld.getPattern());
						}
						excelResult.add(row);
					}
					SystemExcel.makeExcel(file, excelResult);

					Http.write(file);
				} catch (Throwable e) {
					log.error(e);
				} finally {
					if (file != null)
						file.delete();
				}
				return null;

			}

		});
	}

	@At(URL_BZFORM_EDIT)
	public UIBizFormModel edit(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		return buildForm("编辑", moduleParam, actionParam, dataNode, URL_BZ_SAVE);
	}

	@At(URL_BZFORM_EDIT_N)
	public UIBizFormModel editn(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		return buildForm("批量修改", moduleParam, actionParam, dataNode, URL_BZ_SAVE);
	}

	@At(URL_BZFORM_PRINT)
	public UIBizFormModel print(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		StringBuffer errorInfo = new StringBuffer();
		try {
			boolean ajaxData = isAjaxParam(moduleParam);
			String[] mParams = parseParam(ajaxData ? moduleParam.substring(1) : moduleParam);
			String[] aParams = parseParam(actionParam);
			String moduleID = mParams[0];
			// String pageID = mParams[1];
			String actionID = aParams[0];
			String dataID = aParams[1];

			IBizManager bizManager = getBizManager(moduleID);
			IModule mdl = bizManager.getModule();

			IAction action = moduleEngine.getAction(mdl, actionID);
			if (action == null) {
				try {
					action = moduleEngine.getAction(mdl, Long.parseLong(actionID));
				} catch (Throwable e) {
					log.warnf("获取模块操作出错! %s", e);
				}
			}
			if (action == null) {
				throw new DemsyException("业务操作不存在! [actionID=%s]", actionID);
			}

			if (!Str.isEmpty(action.getError())) {
				errorInfo.append(action.getError());
			}
			log.tracef("获取模块操作 [actionID=%s]", actionID);

			Class bizClass = bizEngine.getType(moduleEngine.getSystem(mdl));
			log.tracef("获取业务类 [%s]", bizClass);

			List list = null;
			if (!Str.isEmpty(dataID)) {
				list = this.list(bizManager, bizClass, dataID, actionID, null);
			} else {
				String params = action.getParams();
				list = this.list(bizManager, bizClass, dataID, actionID, CndExpr.make(params));
			}

			UIBizFormModel dataModel = uiEngine.makeSystemFormView(mdl, action, null).setDacorator("").setAjaxData(ajaxData);
			String submitUrl = MvcUtil.contextPath(URL_BZ_SAVE, moduleParam, actionID + ":");
			dataModel.set("nextToken", Demsy.me().addToken());
			dataModel.set("submitUrl", submitUrl);
			if (action != null) {
				if (!Str.isEmpty(action.getTemplate()))
					dataModel.setTemplate(action.getTemplate());
				else {
					IAction actionLib = action.getActionLib();
					if (actionLib != null && !Str.isEmpty(actionLib.getTemplate()))
						dataModel.setTemplate(actionLib.getTemplate());
				}
				dataModel.getModel().setName(action.getDesc());
			}
			dataModel.setData(list);

			return dataModel;
		} catch (ConfigException e) {
			throw e;
		} catch (Throwable e) {
			String msg = Ex.msg(e);

			log.debugf("创建业务打印表单失败! %s", msg);

			throw new DemsyException(msg);
		}
	}

	@At(URL_BZ_SAVE)
	@Ok("json")
	public Status save(final String moduleParam, final String actionParam, final String token, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		if (!Demsy.me().existToken(token)) {
			return new Status(false, "不允许重复提交数据，请先刷新页面！");
		}
		Status ret = exec("保存", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				return manager.save(obj, action.getMode());
			}

		});
		if (ret.isSuccess()) {
			Demsy.me().removeToken(token);
			ret.setNextToken(Demsy.me().addToken());
		}

		return ret;
	}

	@At(URL_BZFORM_LOAD)
	@Ok("json")
	public Object load(String moduleID, String dataID) {
		log.debugf("加载业务数据... [module=%s, data=%s]", moduleID, dataID);
		try {
			IBizManager bizManager = getBizManager(moduleID);

			Object ret = bizManager.load(Long.parseLong(dataID), "v");

			log.debugf("加载业务数据成功. [module=%s, data=%s]", bizManager.getModule(), ret);

			return ret;
		} catch (Throwable e) {
			String msg = Ex.msg(e);

			log.debugf("加载业务数据失败! %s", msg);

			return null;
		}
	}

	@At(URL_BZ_DEL)
	@Ok("json")
	public Status del(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		final StringBuffer fkerror = new StringBuffer();
		return exec("删除", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				try {
					List list;
					if (obj instanceof List)
						list = (List) obj;
					else {
						list = new ArrayList();
						list.add(obj);
					}

					IBizSystem sys = manager.getSystem();
					List<? extends IBizField> exportFlds = bizEngine.getFieldsOfExport(sys);
					for (IBizField fk : exportFlds) {
						IBizSystem slaveSys = fk.getSystem();
						if (bizEngine.isSlave(slaveSys)) {
							Class slaveType = bizEngine.getType(slaveSys);
							String prop = bizEngine.getPropName(fk);
							for (Object ele : list) {
								manager.orm().deleteMore(slaveType, Expr.eq(prop, ele));
							}
						} else {
							fkerror.append(slaveSys.getName()).append("(").append(fk.getName()).append("),");
						}
					}

					return manager.delete(list, action.getMode());
				} catch (Throwable e) {
					if (fkerror.length() > 0) {
						throw new DemsyException("正在删除的数据可能被【" + fkerror.toString() + "】等字段引用! 错误详情:\n" + Ex.msg(e));
					} else {
						throw new DemsyException(Ex.msg(e));
					}
				}
			}
		});
	}

	@At(URL_BZ_CLEAR)
	@Ok("json")
	public Status clear(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("清空", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				List list = null;
				if (obj instanceof List)
					list = (List) obj;
				else if (!Obj.isEmpty(obj)) {
					list = new ArrayList();
					list.add(obj);
				}

				CndExpr expr;
				if (list != null && list.size() > 0) {
					expr = CndExpr.in(F_ID, list);
				} else {
					expr = getBizCndExpr(manager.orm(), manager.getType());
				}

				return manager.deleteMore(action.getMode(), expr);
			}
		});
	}

	@At(URL_BZ_ORDERBY_UP)
	@Ok("json")
	public Status orderUp(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("上移排序", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				List listCurrent;
				if (obj instanceof List)
					listCurrent = (List) obj;
				else {
					listCurrent = new ArrayList();
					listCurrent.add(obj);
				}

				Pager pager = new Pager(manager.getType());
				pager.setQueryExpr(getBizCndOrderExpr(manager.orm(), manager.getType()));
				List listAll = manager.query(pager, action.getMode());

				orderby(listAll, listCurrent, false);
				// listAll.addAll(listCurrent);

				return manager.orm().save(listAll, Expr.fieldRexpr(F_ORDER_BY + "$", false));
			}
		});
	}

	@At(URL_BZ_ORDERBY_CANCEL)
	@Ok("json")
	public Status orderByCancel(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("取消排序", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				List listCurrent;
				if (obj instanceof List)
					listCurrent = (List) obj;
				else {
					listCurrent = new ArrayList();
				}

				Pager pager = new Pager(manager.getType());
				pager.setQueryExpr(getBizCndOrderExpr(manager.orm(), manager.getType()));

				for (Object one : listCurrent) {
					Obj.setValue(one, F_ORDER_BY, null);
				}

				return manager.orm().save(listCurrent, Expr.fieldRexpr(F_ORDER_BY + "$", false));
			}
		});
	}

	@At(URL_BZ_ORDERBY_DOWN)
	@Ok("json")
	public Status orderDown(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("下移排序", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				IOrm orm = manager.orm();
				List listCurrent;
				if (obj instanceof List)
					listCurrent = (List) obj;
				else {
					listCurrent = new ArrayList();
					listCurrent.add(obj);
				}

				Pager pager = new Pager(manager.getType());
				pager.setQueryExpr(getBizCndOrderExpr(orm, manager.getType()));
				List listAll = manager.query(pager, action.getMode());

				orderby(listAll, listCurrent, true);
				// listAll.addAll(listCurrent);

				return orm.save(listAll, Expr.fieldRexpr(F_ORDER_BY + "$", false));
			}
		});
	}

	@At(URL_BZ_ORDERBY_REVERSE)
	@Ok("json")
	public Status orderReverse(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("颠倒排序", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				List listCurrent;
				if (obj instanceof List)
					listCurrent = (List) obj;
				else {
					listCurrent = new ArrayList();
					listCurrent.add(obj);
				}

				reserve(listCurrent);

				return manager.orm().save(listCurrent, Expr.fieldRexpr(F_ORDER_BY + "$", false));
			}
		});
	}

	protected void setOrderby(Object obj, int orderby) throws DemsyException {
		Obj.setValue(obj, F_ORDER_BY, orderby);
	}

	protected Integer getOrderby(Object obj) throws DemsyException {
		return (Integer) Obj.getValue(obj, F_ORDER_BY);
	}

	protected void orderby(List listAll, List listCurrent, boolean isDown) throws DemsyException {
		int from = listAll.indexOf(listCurrent.get(0));
		List otherDatas = new ArrayList();
		int len = listAll.size();
		int index = from - 1;
		int orderby = 1;
		if (isDown) {
			index = from;
			// 往下移动排序
			// 排序选中前的数据
			for (int i = 0; i < len; i++) {
				Object oneData = listAll.get(i);
				if (i < index) {// 排序选中前的数据
					setOrderby(oneData, orderby++);
				} else {
					// 忽略待排序的数据
					if (!listCurrent.contains(oneData)) {
						otherDatas.add(oneData);
						continue;
					}
				}
			}
			// 排序选中的数据和其他数据
			int otherLen = otherDatas.size();
			if (otherLen > 0) {// 排序其他数据中的第一条数据
				Object oneData = otherDatas.get(0);
				setOrderby(oneData, orderby++);
			}
			// 排序选中的数据

			for (Object obj : listAll) {
				if (listCurrent.contains(obj)) {
					setOrderby(obj, orderby++);
				}
			}
			// 排序除第一条外的其他数据
			for (int i = 1; i < otherLen; i++) {
				Object oneData = otherDatas.get(i);
				setOrderby(oneData, orderby++);
			}
		} else {
			// 往上移动排序
			// 排序选中前的数据和选中的数据
			for (int i = 0; i < len; i++) {
				Object oneData = listAll.get(i);
				if (i < index) {// 排序选中前的数据
					setOrderby(oneData, orderby++);
				} else {
					// 排序选中的数据
					if (listCurrent.contains(oneData)) {
						setOrderby(oneData, orderby++);
					} else {
						// 增加未排序的数据并继续
						otherDatas.add(oneData);
						continue;
					}
				}
			}
			// 排序其他数据
			for (Object oneData : otherDatas) {
				setOrderby(oneData, orderby++);
			}
		}
	}

	protected void reserve(List list) throws DemsyException {
		List<Integer> orders = new ArrayList<Integer>();
		for (Object obj : list) {
			Integer order = getOrderby(obj);
			if (order == null) {
				orders.add(0);
			} else {
				orders.add(order);
			}
		}
		int len = list.size() - 1;
		for (int i = len; i >= 0; i--) {
			Object data = list.get(i);
			setOrderby(data, orders.get(new Integer(len - i)));
		}
	}

	@At(URL_BZFORM_EXEC_SYNC)
	public UIBizFormModel execForm(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		return buildForm("同步业务表单", moduleParam, actionParam, dataNode, URL_BZ_EXEC_SYNC);
	}

	@At(URL_BZ_EXEC_SYNC)
	@Ok("json")
	public Status exec(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("同步执行", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				return manager.run(obj, action.getMode());
			}

		});
	}

	@At(URL_BZFORM_EXEC_ASYN)
	public UIBizFormModel asynExecForm(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		return buildForm("异步业务表单", moduleParam, actionParam, dataNode, URL_BZ_EXEC_ASYN);
	}

	@At(URL_BZ_EXEC_ASYN)
	@Ok("json")
	public Status asynExec(final String moduleParam, final String actionParam, @Param("::" + UI_BZFORM_PREFIX) ObjcetNaviNode dataNode) throws DemsyException {
		// security.checkLogin(IUserRole.ROLE_USER);

		return exec("异步执行", moduleParam, actionParam, dataNode, new LogicExecutor() {
			public Object exec(IBizManager manager, Object obj, IAction action) throws DemsyException {
				manager.asynRun(obj, action.getMode());
				return null;
			}

		});
	}

	private interface LogicExecutor {
		Object exec(IBizManager manager, Object data, IAction action) throws DemsyException;
	}

	private interface WidgetBuilder {
		UIWidgetModel build(IBizManager manager, IModule mdl, String pageID, boolean ajaxData) throws DemsyException;
	}
}

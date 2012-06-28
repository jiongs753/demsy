package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.appconfig;
import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_UIMODEL;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_STYLE;
import static com.kmetop.demsy.comlib.LibConst.F_DISABLED;
import static com.kmetop.demsy.comlib.LibConst.F_GUID;
import static com.kmetop.demsy.comlib.LibConst.F_ID;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.F_PARENT;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;
import static com.kmetop.demsy.comlib.LibConst.F_USAGE;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.globalVariables;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.initGlobalVariables;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.IUiEngine;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizFieldGroup;
import com.kmetop.demsy.comlib.biz.IBizFieldType;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.IRuntimeConfigable;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzSubFld;
import com.kmetop.demsy.comlib.biz.field.CssPosition;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.biz.field.IExtField;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.comlib.ui.IStyle.SimpleStyle;
import com.kmetop.demsy.comlib.ui.IStyleItem;
import com.kmetop.demsy.comlib.ui.IUIViewComponent;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Nodes.Node;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Option;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.IUIViewController;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.model.UIBizFormModel;
import com.kmetop.demsy.mvc.ui.model.UIBizGridModel;
import com.kmetop.demsy.mvc.ui.model.UIBizMenuModel;
import com.kmetop.demsy.mvc.ui.model.UIBizNaviModel;
import com.kmetop.demsy.mvc.ui.model.UIBlockViewModel;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIAccordion;
import com.kmetop.demsy.mvc.ui.widget.UIBizForm;
import com.kmetop.demsy.mvc.ui.widget.UIBizGrid;
import com.kmetop.demsy.mvc.ui.widget.UIBizGrid.UIGridFld;
import com.kmetop.demsy.mvc.ui.widget.UIBizModule;
import com.kmetop.demsy.mvc.ui.widget.UIBizNavi;
import com.kmetop.demsy.mvc.ui.widget.UIBizSystem;
import com.kmetop.demsy.mvc.ui.widget.UIBlockView;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;
import com.kmetop.demsy.mvc.ui.widget.UIWidget;
import com.kmetop.demsy.mvc.ui.widget.field.UIBizFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIBoolFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIDateFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIDicFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIDicManyFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIGroupFld;
import com.kmetop.demsy.mvc.ui.widget.field.UINumFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIRichTextFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIStrFld;
import com.kmetop.demsy.mvc.ui.widget.field.UISubSystemFld;
import com.kmetop.demsy.mvc.ui.widget.field.UISysFld;
import com.kmetop.demsy.mvc.ui.widget.field.UISysManyFld;
import com.kmetop.demsy.mvc.ui.widget.field.UITextFld;
import com.kmetop.demsy.mvc.ui.widget.field.UIUploadFld;
import com.kmetop.demsy.mvc.ui.widget.menu.UIToolbarMenu;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.util.sort.SortUtils;

public class UiEngine implements IUiEngine, MvcConst {
	protected static Log log = Logs.getLog(UiEngine.class);

	private Map<String, Map<Serializable, UIWidget>> uiModelCache;

	protected Map<Long, CacheUI> pageCache;

	protected Map<Long, IPageBlock> pageBlockCache;

	protected Map<Long, IStyle> styleCache;

	protected List<IUIViewComponent> viewComponentList;
	protected Map<Long, IUIViewComponent> viewTypeIdCache;

	protected Map<String, IUIViewController> controllerCache;

	protected Map<Long, CacheWeb> webCache;

	protected Map<String, CacheWeb> webGuidCache;

	protected Map<Long, UIBlockContext> uiBlockCache;

	public UiEngine() {
		pageCache = new HashMap();
		uiModelCache = new HashMap();
		viewComponentList = new LinkedList();
		viewTypeIdCache = new HashMap();
		controllerCache = new HashMap();
		styleCache = new HashMap();
		pageBlockCache = new HashMap();
		webCache = new HashMap();
		webGuidCache = new HashMap();
		uiBlockCache = new HashMap();
	}

	@Override
	public void clearCache() {
		synchronized (UiEngine.class) {
			uiModelCache.clear();
			pageCache.clear();
			viewTypeIdCache.clear();
			viewComponentList.clear();
			controllerCache.clear();
			styleCache.clear();
			pageBlockCache.clear();
			webCache.clear();
			webGuidCache.clear();
			uiBlockCache.clear();

			initGlobalVariables();
		}
	}

	//
	// private String staticModuleName(int staticModuleID) {
	// Class type = entityComLib.getType(staticModuleID);
	// if (type != null) {
	// BizInfo bizInfo = (BizInfo) type.getAnnotation(BizInfo.class);
	// if (bizInfo != null)
	// return bizInfo.name();
	// }
	//
	// return "";
	// }

	private <T extends UIWidget> T cached(String type, Serializable id) {
		synchronized (uiModelCache) {
			Map<Serializable, UIWidget> map = uiModelCache.get(type);
			if (map == null)
				return null;

			return (T) map.get(id);
		}
	}

	private <T extends UIWidget> T cache(String key, T model) {
		// if (!Demsy.appconfig.isProductMode()) {
		return model;
		// }
		// synchronized (modelCache) {
		// Map<Serializable, ModelUI> map = modelCache.get(model.getClass());
		// if (map == null) {
		// map = new HashMap();
		// modelCache.put(key, map);
		// }
		//
		// map.put(model.getId(), model);
		//
		// return model;
		// }

	}

	@Override
	public UIPageView makePageView(String pageID) {
		// Serializable pageID = dataModel.getPageID();
		UIPageView pageUI = new UIPageView(globalVariables);

		return pageUI;
	}

	@Override
	public UIBizModule makeModuleView(IModule mdl, String gridColumns, String idField) throws DemsyException {
		UIBizModule ui = new UIBizModule(globalVariables, mdl.getId());

		// Tabs menu = (Tabs) new Tabs(globalVariables, "master" +
		// mdl.getId()).set("moduleID", mdl.getId());
		// ui.setMaster(new TabsDataModel(menu, null));

		ui.set("moduleID", mdl.getId());
		ui.set("tabTemplate", "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>删除</span></li>");

		// 模块导航菜单
		ui.setNaviMenu(makeSystemNaviView(mdl));

		// 设置模块GRID
		ui.setGrid(makeSystemGridView(mdl, gridColumns, idField, ui.getNaviMenu() != null));
		ui.setName(mdl.getName());

		// 设置模块工具栏菜单
		ui.setToolbarMenu(makeSystemActionView(mdl));

		// 处理关联变量
		if (ui.getNaviMenu() != null) {
			UIBizNaviModel navi = ui.getNaviMenu();
			navi.set("grid", ui.getGrid().getModel());
		}

		ui.getToolbarMenu().set("grid", ui.getGrid().getModel());

		// ui.setSlave(this.getSlaveUI(mdl));

		return ui;
	}

	@Override
	public UIBizSystem makeSystemView(IModule mdl, String gridColumns, String idField) throws DemsyException {
		UIBizSystem ui = new UIBizSystem(globalVariables, mdl.getId());

		ui.set("moduleID", mdl.getId());
		// BizModuleUI ui = cached("BizModuleUI", mdl.getId());
		// if (ui == null) {
		// ui = cache("BizModuleUI", new BizModuleUI(globalVariables,
		// mdl.getId()));

		// 模块导航菜单
		ui.setNaviMenu(makeSystemNaviView(mdl));

		// 设置模块GRID
		ui.setGrid(makeSystemGridView(mdl, gridColumns, idField, ui.getNaviMenu() != null));
		ui.setName(mdl.getName());

		// 设置模块工具栏菜单
		ui.setToolbarMenu(makeSystemActionView(mdl));

		// }

		// 处理关联变量
		if (ui.getNaviMenu() != null) {
			UIBizNaviModel navi = ui.getNaviMenu();
			navi.set("grid", ui.getGrid().getModel());
		}

		ui.getToolbarMenu().set("grid", ui.getGrid().getModel());

		return ui;
	}

	// @Override
	// public TabsDataModel getSlaveUI(IModule mdl) throws DemsyException {
	//
	// Tabs menu = (Tabs) new Tabs(globalVariables, "slave" +
	// mdl.getId()).set("moduleID", mdl.getId());
	//
	// return new TabsDataModel(menu, null);
	// }

	@Override
	public UIBizGridModel makeSystemGridView(IModule mdl, String colNames, String idField, boolean existNaviTree) throws DemsyException {
		UIBizGrid grid = cached("BizGrid", mdl.getId());
		if (grid == null) {
			grid = cache("BizGrid", new UIBizGrid(globalVariables, mdl.getId()));

			grid.set("moduleID", mdl.getId());
			if (!Str.isEmpty(idField))
				grid.setIdField(idField);

			//
			grid.setName(mdl.getName());
			grid.setId(mdl.getId());
			grid.setHeight(400);

			grid.setDataUrl(contextPath(URL_BZGRID, URL_PREFIX_AJAX + mdl.getId()));
			grid.setDataType(DATA_XML);

			IBizSystem sys = moduleEngine.getSystem(mdl);
			List<? extends IBizField> flds = bizEngine.getFieldsOfGrid(sys, colNames);
			int count = 1;
			int width = 0;

			// 计算grid列数量
			// double contentWidth = Demsy.me().login().getBodyWidth() - 400;
			// if (!existNaviTree) {
			// contentWidth += 280;
			// }
			// int gridColSize = new Double(contentWidth / 120).intValue();
			int gridColSize = 8;

			for (IBizField fld : flds) {
				if (!fld.isGridField()) {
					continue;
				}
				UIGridFld col = new UIGridFld(null, fld.getId());
				if (count > gridColSize) {
					col.setHidden(true);
				} else {
					count++;
				}

				if (bizEngine.isString(fld) || bizEngine.isRichText(fld) || bizEngine.isSystemFK(fld)) {
					col.setString(true);
				}

				col.setLabel(fld.getName());
				col.setName(fld.getCode());
				col.setPropName(bizEngine.getPropName(fld));

				Option[] options = bizEngine.getOptions(fld);
				if (options.length > 0) {
					col.setSearchhidden(false);
					Map<String, String> map = new HashMap();
					for (Option o : options) {
						map.put(o.getValue(), o.getText());
					}
					col.setOptions(map);
				}

				IBizField field = fld.getRefrenceField();
				if (field == null) {
					field = fld;
				}
				col.setPattern(field.getPattern());
				if (field.isPrivacy()) {
					String ptn = col.getPattern();
					col.setPattern("*" + (ptn == null ? "" : ptn));
				}

				if (bizEngine.isNumber(field) && options.length == 0) {
					col.setAlign(Align.RIGHT);
				} else if (bizEngine.isBoolean(field)) {
					col.setAlign(Align.CENTER);
				}

				// 计算字段宽度
				int w = bizEngine.getGridWidth(field);
				col.setWidth(w);
				width += w;

				grid.addField(col);
			}

			grid.setWidth(width);
			grid.setAutowidth(true);
			grid.setShrinkToFit(true);
		}

		return new UIBizGridModel(grid, null);
	}

	@Override
	public UIBizNaviModel makeSystemNaviView(IModule mdl) throws DemsyException {
		UIBizNavi navi = cached("BizNavi", mdl.getId());
		if (navi == null) {
			navi = cache("BizNavi", new UIBizNavi(globalVariables, mdl.getId()));

			navi.set("moduleID", mdl.getId());

			if (bizEngine.getFieldsOfNavi(moduleEngine.getSystem(mdl)).size() == 0) {
				navi.setWidth(0);
			} else {
				navi.setWidth(300);

				// TODO: 通过配置指定数据类型，如果数据类型为null表示不采用AJAX方式加载数据
				navi.setDataUrl(contextPath(URL_BZNAVI, URL_PREFIX_AJAX + mdl.getId()));
				// navi.setDataType(DATA_HTML);
				navi.setDataType(DATA_JSON);

				// 设置环境变量——菜单树根节点标题
				navi.set("title", mdl.getName() + "[数据导航]");
			}
		}

		if (navi.getWidth() <= 0) {
			return null;
		}

		// Nodes data = null;
		// if (Strings.isEmpty(navi.getDataUrl())) {
		// data = bizEngine.makeNaviNodes(entityComLib.getSystem(mdl));
		// }

		return new UIBizNaviModel(navi, null);
	}

	@Override
	public UIBizMenuModel<UIToolbarMenu> makeSystemActionView(IModule module) throws DemsyException {

		UIToolbarMenu menu = cached("ToolbarMenu", module.getId());
		if (menu == null) {
			menu = cache("ToolbarMenu", new UIToolbarMenu(globalVariables, module.getId()));

			// 设置模版环境变量——遗留系统中的图标路径
			menu.set("moduleID", module.getId());
			menu.set("type", URL_PREFIX_BZ);

			// TODO: 通过配置指定数据类型，如果数据类型为null表示不采用AJAX方式加载数据
			menu.setDataUrl(contextPath(URL_BZMENU, URL_PREFIX_AJAX + module.getId()));
			menu.setDataType(DATA_JSON);
		}

		// 查询模块操作菜单
		// Nodes data = null;
		// if (Strings.isEmpty(menu.getDataUrl())) {
		// data = entityComLib.makeActionNodes(module);
		// }

		return new UIBizMenuModel<UIToolbarMenu>(menu, null);
	}

	@Override
	public UIBizFormModel makeSystemFormView(IModule module, IAction action, Object data) throws DemsyException {
		// String key = "BizForm" + (action == null ? "" : action.getId());
		// BizForm form = cached(key, module.getId());
		// if (form == null) {
		// form = cache(key, new BizForm(globalVariables, module.getId()));

		UIBizForm form = new UIBizForm(globalVariables, module.getId());
		form.setDataType(MvcConst.DATA_HTML);

		form.set("moduleID", module.getId());

		IBizSystem sys = moduleEngine.getSystem(module);

		// 获取运行时自定义字段
		List<IBizField> customFields = new LinkedList();
		Map<String, IBizField> customFieldsMap = new HashMap();
		List<IBizField> fkFieldsOfRuntimeCustom = (List<IBizField>) bizEngine.getFieldsOfSystemFK(sys, IRuntimeConfigable.class);
		List<String> fkPropsOfRuntimeCustom = new ArrayList();
		for (IBizField fld : fkFieldsOfRuntimeCustom) {
			String propname = bizEngine.getPropName(fld);
			fkPropsOfRuntimeCustom.add(propname);
			IRuntimeConfigable custom = Obj.getValue(data, propname);
			List<? extends IBizField> flds = bizEngine.makeFields(custom);
			if (flds != null) {
				for (IBizField f : flds) {
					customFields.add(f);
					customFieldsMap.put(f.getPropName(), f);
				}
			}
		}

		// 转换字段为UI
		/*
		 * 每行字段数量 = 右边内容窗口宽度 / 每个字段所占的宽度 ， 右边内容窗口宽度 = 浏览器宽度 * 80%
		 */
		// double contentWidth = Demsy.me().login().getBodyWidth() - 50;
		int rowSize = 1;// new Double(contentWidth / 285).intValue();//每行显示多少个字段
		form.setLayout(sys.getLayout());
		List<? extends IBizFieldGroup> bizGroups = bizEngine.getFieldGroups(sys);
		Map<String, UIBizFld> uiFieldMap = new HashMap();
		for (IBizFieldGroup bizGroup : bizGroups) {
			UIGroupFld uiGroup = new UIGroupFld(null, bizGroup.getId());
			if (sys.getLayout() == 0) {// 0: table 1: tab
				uiGroup.setRowSize(rowSize);
			}
			uiGroup.setName(bizGroup.getName());
			uiGroup.setMode(bizEngine.getMode(bizGroup, action));

			List<? extends IBizField> groupFields = bizEngine.getFieldsOfEnabled(bizGroup);
			for (IBizField bzField : groupFields) {
				try {

					// 用运行时自定义字段覆盖物理字段
					String bzFieldPropName = bizEngine.getPropName(bzField);
					IBizField customField = customFieldsMap.get(bzFieldPropName);
					if (customField != null) {
						if (!Str.isEmpty(customField.getName()))
							bzField.setName(customField.getName());
						if (!Str.isEmpty(customField.getMode()))
							bzField.setMode(customField.getMode());
						if (!Str.isEmpty(customField.getOptions()))
							bzField.setOptions(customField.getOptions());

						customFieldsMap.remove(bzFieldPropName);
					}
					// END: 用运行时自定义字段覆盖物理字段

					UIBizFld uiField = this.convertFld(module, form, bzField, action, null, data, String.class.equals(bizEngine.getType(bzField)) ? F_GUID : F_ID);
					if (uiField != null) {
						uiGroup.addChild(uiField, data);
						uiFieldMap.put(uiField.getPropName(), uiField);
					}
				} catch (Throwable e) {
					log.errorf("创建表单字段失败！field=%s, error=%s", bzField.getPropName(), e);
				}
			}

			uiGroup.fillBlank();

			form.addGroup(uiGroup);
		}

		// 添加运行时自定义字段到业务表单中
		UIGroupFld uiGroup = new UIGroupFld(null, System.currentTimeMillis());
		uiGroup.setName("扩展属性");
		uiGroup.setMode("S");
		Map<Long, IBizFieldType> types = bizEngine.getFieldTypesById();
		long dynId = System.currentTimeMillis();
		for (IBizField bzField : customFields) {
			try {
				if (customFieldsMap.get(bzField.getPropName()) == null)
					continue;

				IBizFieldType fieldType = bzField.getType();
				if (fieldType != null) {
					fieldType = types.get(fieldType.getId());
					bzField.setType(fieldType);
				}
				bzField.setId(dynId++);

				UIBizFld uiField = this.convertFld(module, form, bzField, action, null, data, F_ID);
				if (uiField != null) {
					uiGroup.addChild(uiField, data);
					uiFieldMap.put(uiField.getPropName(), uiField);
				}
			} catch (Throwable e) {
				log.error(String.format("创建表单字段失败！field=%s", bzField.getPropName()), e);
			}
		}
		uiGroup.fillBlank();
		form.addGroup(uiGroup);
		// END: 添加运行时自定义字段到业务表单中

		// 设置联动字段
		Iterator<UIBizFld> it = uiFieldMap.values().iterator();
		while (it.hasNext()) {
			UIBizFld f = it.next();
			// 外键字段是“运行时可配置的”
			if (fkPropsOfRuntimeCustom.contains(f.getPropName())) {
				f.set("cascade", "cascade");
				continue;
			}
			String cascadeBy = (String) f.get("cascadeBy");
			String[] array = Str.toArray(cascadeBy, ",");
			if (array != null && array.length > 0) {
				for (String str : array) {
					UIBizFld parent = uiFieldMap.get(str);
					if (parent != null) {
						parent.set("cascade", "cascade");
					}
				}
			}
		}

		UIBizFormModel ret = new UIBizFormModel(form, null);
		ret.setData(data);
		ret.setId(module.getId());
		ret.set("actionID", action == null ? 0 : action.getId());

		return ret;
	}

	@SuppressWarnings("deprecation")
	private UIBizFld convertFld(IModule module, UIBizForm form, IBizField bzField, IAction action, String defaultMode, Object data, String idField) {

		String[] cascade = bizEngine.getCascadeMode(bzField, data);
		String mode = bizEngine.getMode(bzField, action, false, defaultMode);
		if (bizEngine.getModeValue(mode) < bizEngine.getModeValue(cascade[1])) {
			mode = bizEngine.getUiMode(cascade[1]);
		}
		// log.tracef("bzField:%s, mode:%s, defaultMode:%s", bzField.getName(),
		// mode, defaultMode);

		String prop = bizEngine.getPropName(bzField);

		UIBizFld uiField = null;
		boolean isDate = false;
		boolean isUpload = false;
		boolean isRichText = false;
		boolean isMultiSelect = false;
		boolean isColor = false;
		boolean isCombobox = false;

		// ui template
		String tpl = bzField.getUiTemplate();
		if (Str.isEmpty(tpl)) {
			tpl = bzField.getType().getUiTemplate();
		}

		String namePost = "";
		if (bizEngine.isV1Dic(bzField)) {// 遗留系统字典
			namePost = ".id";
		}

		Class bzFieldType = bizEngine.getType(bzField);
		// ui model
		if (bizEngine.isBoolean(bzField)) {// boolean
			uiField = new UIBoolFld(null, bzField.getId()).setOptionNode(bizEngine.makeOptionNodes(bzField, mode, data, idField));
		} else if (bizEngine.isUpload(bzField)) {// upload
			uiField = new UIUploadFld(null, bzField.getId()).setUploadUrl(contextPath(URL_UPLOAD, module.getId(), bzField.getId())).setUploadType(bzField.getUploadType())
					.setContext(form.getContext());
			isUpload = true;
		} else if (bizEngine.isRichText(bzField)) {// richtext
			uiField = new UIRichTextFld(null, bzField.getId());
			isRichText = true;
		} else if (bizEngine.isText(bzField)) {// text
			uiField = new UITextFld(null, bzField.getId());
		} else if (bizEngine.getOptions(bzField).length > 0) {// auto dic
			if (bizEngine.isManyToMany(bzField)) {
				uiField = new UIDicManyFld(null, bzField.getId()).setOptionNode(bizEngine.makeOptionNodes(bzField, mode, data, idField));
				form.setHasMultiSelect(true);
			} else {
				uiField = new UIDicFld(null, bzField.getId()).setOptionNode(bizEngine.makeOptionNodes(bzField, mode, data, idField));
			}
		} else if (bizEngine.isDate(bzField)) {// date
			uiField = new UIDateFld(null, bzField.getId());
			isDate = true;
		} else if (bizEngine.isNumber(bzField)) {// number
			uiField = new UINumFld(null, bzField.getId());
		} else if (bizEngine.isString(bzField)) {// string
			uiField = new UIStrFld(null, bzField.getId());
			if (bzField.isPassword()) {
				((UIStrFld) uiField).setInputType("password");
			}
		} else if (bizEngine.isSystemFK(bzField)) {// system refrence
			Class type = null;
			if (bizEngine.isManyToMany(bzField)) {
				namePost = ".id";
				uiField = new UISysManyFld(null, bzField.getId());
				isMultiSelect = true;
			} else {
				uiField = new UISysFld(null, bzField.getId());
				try {
					type = Cls.getType(bizEngine.getType(bzField.getSystem()), bizEngine.getPropName(bzField));
					if (type.isAssignableFrom(bizEngine.getType(bzField.getRefrenceSystem()))) {
						namePost = ".id";
					}
				} catch (Throwable e) {
					log.warnf("系统引用字段类型不匹配! %s", Ex.msg(e));
				}
			}

			Nodes options = bizEngine.makeOptionNodes(bzField, mode, data, idField);
			uiField.setOptionNode(options);
			if (options.is((byte) 1)) {
				isCombobox = true;
				IModule refModule = moduleEngine.getModule(module.getSoftID(), bzField.getRefrenceSystem());
				List<String> rules = bizEngine.makeCascadeExpr(data, bzField, action.getMode());
				StringBuffer naviRules = new StringBuffer();
				if (rules != null && rules.size() > 0) {
					naviRules.append("[");
					int count = 0;
					for (String rule : rules) {
						if (count != 0) {
							naviRules.append(",");
						}
						naviRules.append("'").append(rule).append("'");
						count++;
					}
					naviRules.append("]");
				}
				String url = MvcUtil.contextPath(MvcConst.URL_BZSYS_COMB_FK, (refModule == null ? 0 : refModule.getId()) + ":") + "?gridColumns=3";
				if (type.equals(String.class)) {
					uiField.setKeyProp(LibConst.F_GUID);
					url += "&idField=" + LibConst.F_GUID;
				}
				if (naviRules.length() > 0)
					url += "&fixedNaviRules=" + java.net.URLEncoder.encode(naviRules.toString());

				uiField.setTemplate("ui.widget.field.SysFld_Comb");
				uiField.setComboboxUrl(url);
			}
		} else if (bizEngine.isString(bzField)) {// string
			uiField = new UIStrFld(null, bzField.getId());
		} else if (bizEngine.isFieldRef(bzField)) {// iglore field refrence
			return null;
		} else if (bizEngine.isSubSystem(bzField)) {
			uiField = new UISubSystemFld(null, bzField.getId()).setUploadUrl(contextPath(URL_UPLOAD, module.getId(), bzField.getId())).setUploadType(bzField.getUploadType())
					.setContext(form.getContext());
			UISubSystemFld uiSubSysFld = (UISubSystemFld) uiField;

			IBizSystem refSystem = bzField.getRefrenceSystem();
			String[] refProps = Str.toArray(bzField.getRefrenceFields());

			if (bizEngine.isFakeSubSystem(bzField)) {
				uiSubSysFld.setFake(true);
			}
			if (bizEngine.isMultiUpload(bzField)) {
				BzFld ann = (BzFld) bzFieldType.getAnnotation(BzFld.class);
				refSystem = bizEngine.getSystem(ann.refrenceSystem());
				refProps = Str.toArray(ann.refrenceFields());
			}

			Map<String, IBizField> refFields = bizEngine.getFieldsMap(refSystem);
			for (String refProp : refProps) {
				IBizField refFld = refFields.get(refProp);
				if (refFld != null) {
					UIBizFld uiRefFld = this.convertFld(module, form, refFld, action, defaultMode, null, null);

					uiRefFld.setMode("E");
					if (bizEngine.isNumber(refFld) && !bizEngine.isInteger(refFld)) {
						uiRefFld.setPattern("#.00");
					}
					if (bizEngine.isSystemFK(refFld))
						uiRefFld.setName(refProp + ".id");
					else
						uiRefFld.setName(refProp);

					uiSubSysFld.addChild(uiRefFld);
				}
			}
		} else {
			uiField = new UIStrFld(null, bzField.getId());
			uiField.setMode(mode);
			if (!"N".equals(mode))
				this.convertSubFields(module, form, action, data, bzField, uiField);
		}

		if (!Str.isEmpty(tpl)) {
			uiField.setTemplate(tpl);
		}
		tpl = uiField.getTemplate();
		if (!Str.isEmpty(tpl) && tpl.indexOf(".CssColor") > 0) {
			isColor = true;
		}

		uiField.setId(bzField.getId());
		uiField.setLabel(bzField.getName());
		uiField.setMode(mode);
		if (!Str.isEmpty(cascade[0])) {
			uiField.set("cascadeBy", cascade[0]);
		}
		uiField.setName(UI_BZFORM_PREFIX + prop + namePost);
		uiField.setPropName(prop);
		uiField.setPattern(bzField.getPattern());
		uiField.setParam(bzField);
		if (bzField.isPrivacy()) {
			String ptn = uiField.getPattern();
			uiField.setPattern("*" + (ptn == null ? "" : ptn));
		}

		if ("E".equals(mode) || "M".equals(mode)) {
			if (isDate)
				form.setHasDate(true);
			if (isUpload)
				form.setHasUpload(true);
			if (isRichText)
				form.setHasRichText(true);
			if (isMultiSelect)
				form.setHasMultiSelect(true);
			if (isColor)
				form.setHasColor(true);
			if (isCombobox)
				form.setHasCombobox(true);
		}

		return uiField;
	}

	private void convertSubFields(IModule module, UIBizForm form, IAction action, Object data, IBizField bzfld, UIBizFld uifld) {
		Class proptype = bizEngine.getType(bzfld);
		if (proptype == null || !IExtField.class.isAssignableFrom(proptype)) {
			return;
		}

		String prop = bzfld.getPropName();
		uifld.setPropName(prop);
		uifld.setId(bzfld.getId());
		Mirror propme = Mirror.me(proptype);

		List<IBizField> bzsubflds = new LinkedList();

		// template
		BzFld fann = (BzFld) proptype.getAnnotation(BzFld.class);
		if (fann != null && !Str.isEmpty(fann.uiTemplate())) {
			uifld.setTemplate(fann.uiTemplate());
		}

		long count = 1;
		Class systype = bizEngine.getType(bzfld.getSystem());
		Mirror sysme = Mirror.me(systype);
		BzSubFld[] subanns = null;
		try {
			Field propfld = sysme.getField(prop);
			BzFld propann = propfld.getAnnotation(BzFld.class);
			subanns = propann.children();
		} catch (NoSuchFieldException e) {
		}
		if (subanns != null && subanns.length > 0) {
			for (BzSubFld ann : subanns) {
				IBizField fld = ((BizEngine) bizEngine).parseBizField(module.getSoftID(), propme, ann.property(), bzfld.getSystem(), null);
				fld.setId(count++);
				fld.setPropName(prop + "." + fld.getPropName());
				if (!Str.isEmpty(ann.cascadeMode()))
					fld.setCascadeMode(ann.cascadeMode());
				if (!Str.isEmpty(ann.options()))
					fld.setOptions(ann.options());
				if (!Str.isEmpty(ann.name()))
					fld.setName(ann.name());

				bzsubflds.add(fld);
			}
		} else {
			Field[] fields = propme.getFields(BzFld.class);
			for (Field f : fields) {
				IBizField fld = ((BizEngine) bizEngine).parseBizField(module.getSoftID(), propme, f.getName(), bzfld.getSystem(), null);
				fld.setId(count++);
				fld.setPropName(prop + "." + fld.getPropName());

				bzsubflds.add(fld);
			}
		}

		SortUtils.sort(bzsubflds, LibConst.F_ORDER_BY, true);
		Map<String, UIBizFld> subMap = new HashMap();
		for (IBizField bzChild : bzsubflds) {
			UIBizFld subUiFld = this.convertFld(module, form, bzChild, action, uifld.getMode(), data, LibConst.F_GUID);
			subUiFld.setId(uifld.getId() + "_" + bzChild.getId());
			uifld.addChild(subUiFld, data);
			subMap.put(subUiFld.getPropName(), subUiFld);
		}

		Iterator<UIBizFld> it = subMap.values().iterator();
		while (it.hasNext()) {
			UIBizFld f = it.next();
			String cascadeBy = (String) f.get("cascadeBy");

			String[] casSubProps = Str.toArray(cascadeBy, ",");
			if (casSubProps == null)
				continue;

			for (String casSubProp : casSubProps) {
				UIBizFld parent = subMap.get(prop + "." + casSubProp);
				if (parent != null) {
					parent.set("cascade", "cascade");
				}
			}
		}

		Object subvalue = Obj.getValue(data, prop);

		// 处理Dataset字段
		if (subvalue instanceof Dataset) {
			convertDataset((Dataset) subvalue, uifld);
		}
	}

	private void convertDataset(Dataset dataset, UIBizFld uiField) {
		List<UIBizFld> children = uiField.getChildren();
		Map<String, UIBizFld> map = new HashMap();
		for (UIBizFld f : children) {
			map.put(f.getPropName(), f);
		}

		UIBizFld rules = map.get(uiField.getPropName() + ".rules");
		if (rules != null)
			rules.setComboboxUrl(dataset.getRulesUrl());
	}

	@Override
	public UIWidgetModel makeFunctionMenuView(IDemsySoft soft) throws DemsyException {

		return new UIWidgetModel(new UIAccordion(globalVariables, soft == null ? "0" : soft.getId()), null);
	}

	protected CacheUI page(Long id) {
		CacheUI ret = pageCache.get(id);
		if (ret == null)
			ret = new CacheUI(this, id);

		return ret;
	}

	@Override
	public IPage loadPageTemplate(Long pageID) {
		return page(pageID).get();
	}

	@Override
	public IPage loadIndexPage() {
		return (IPage) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_UIUDF_PAGE), Expr.eq(F_USAGE, IPage.USAGE_IDX).and(Expr.eq(F_SOFT_ID, Demsy.me().getSoft())).setFieldRexpr("id", true));
	}

	protected CacheWeb webInfoCatalog(Long id) {
		CacheWeb ret = this.webCache.get(id);
		if (ret == null)
			ret = new CacheWeb(this, id);

		return ret;
	}

	protected CacheWeb webInfoCatalog(String id) {
		CacheWeb ret = this.webGuidCache.get(id);
		if (ret == null)
			ret = new CacheWeb(this, id);

		return ret;
	}

	@Override
	public IWebContentCatalog loadWebContentCatalog(Long id) {
		return webInfoCatalog(id).get();
	}

	@Override
	public IWebContentCatalog loadWebContentCatalog(String guid) {
		return webInfoCatalog(guid).get();
	}

	@Override
	public IPageBlock loadPageBlock(Long blockID) {
		IPageBlock block = pageBlockCache.get(blockID);
		if (block == null) {
			return (IPageBlock) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_UIUDF_PAGE_BLOCK), Expr.eq(F_ID, blockID));
		}
		return block;
	}

	@Override
	public List<? extends IPageBlock> loadPageBlocks(Long pageID) {
		return page(pageID).blocks();
	}

	private void loadViewComponents() {
		if (this.viewComponentList.size() == 0 || !Demsy.appconfig.isProductMode()) {
			viewComponentList = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_DEMSY_LIB_UIMODEL), Expr.asc(F_ORDER_BY));
			for (IUIViewComponent ele : viewComponentList) {
				this.viewTypeIdCache.put(ele.getId(), ele);
			}
		}
	}

	@Override
	public IUIViewComponent loadViewComponent(Long id) {
		loadViewComponents();

		return this.viewTypeIdCache.get(id);
	}

	public IUIViewController getUIController(final String controllerClass) {
		IUIViewController maker = this.controllerCache.get(controllerClass);
		if (maker != null) {
			return maker;
		}
		try {
			String classname = controllerClass;
			if (classname.indexOf(".") <= 0) {
				classname = "com.kmetop.demsy.ui.datasource." + classname;
			}
			Class type = Cls.forName(classname);
			maker = (IUIViewController) Mirror.me(type).born();
			controllerCache.put(controllerClass, maker);
		} catch (ClassNotFoundException e) {
			throw new DemsyException("创建UI模型生成器出错! %s", e.toString());
		}

		return maker;
	}

	public UIPageView makePageView(Long pageID, Long dynamicBlockID, Long dynamicModuleID, Long dynamicDataID) {
		return makePageView(loadPageTemplate(pageID), dynamicBlockID != null && dynamicBlockID > 0 ? loadPageBlock(dynamicBlockID) : null, dynamicModuleID, dynamicDataID);
	}

	public UIPageView makePageView(IPage page, IPageBlock dynamicBlock, Long dynamicModuleID, Long dynamicDataID) {
		if (page == null) {
			return null;
		}

		UIPageView pageView = new UIPageView(MvcUtil.globalVariables);
		pageView.setId(page.getId());
		pageView.setParam(page);
		pageView.setTemplate("ui.lib.UiPage");
		pageView.addStyle(this.makeStyle("body", page.getStyleItems()));
		pageView.addStyle(page.getStyle());
		pageView.setWidth(page.getPageWidth());
		if (!Str.isEmpty(page.getUiTemplate())) {
			pageView.setTemplate(page.getUiTemplate());
		}
		pageView.set("title", page.getName());
		pageView.set("keywords", page.getKeywords());
		pageView.set("description", page.getDesc());

		IModule pathModule = null;
		Object pathData = null;
		if (dynamicModuleID != null && dynamicModuleID > 0) {
			if (dynamicDataID != null && dynamicDataID >= 0) {
				pathModule = moduleEngine.getModule(dynamicModuleID);
				IBizSystem pathSystem = moduleEngine.getSystem(pathModule);
				pathData = Demsy.orm().load(bizEngine.getType(pathSystem), Expr.eq(F_ID, dynamicDataID));
			}
		}

		List<? extends IPageBlock> blocks = loadPageBlocks(page.getId());
		if (blocks != null) {
			int absoluteHeight = 0;
			int relativeHeight = 0;
			int maxRelH = 0;

			Map<Long, UIBlockViewModel> cachedBlockViews = new HashMap();
			Map<Long, IPageBlock> cachedBlocks = new HashMap();
			for (IPageBlock block : blocks) {
				cachedBlocks.put(block.getId(), block);
			}

			for (IPageBlock block : blocks) {

				if (block.isPlaceHolder() && dynamicBlock != null) {
					CssPosition p = block.getPosition();
					CssPosition p1 = dynamicBlock.getPosition();
					p.setHeight(Math.max(p.getHeight(), p1.getHeight()));
					p.setWidth(Math.max(p.getWidth(), p1.getWidth()));
					dynamicBlock.setPosition(p);
					block = dynamicBlock;
				}

				UIBlockViewModel blockView = makeBlockViewWithParent(pageView, cachedBlockViews, cachedBlocks, null, block, dynamicModuleID, dynamicDataID, pathModule, pathData);

				CssPosition pos = block.getPosition();
				String area = "page";
				if (pos != null) {
					area = pos.getArea();
					if ("page".equals(area)) {
						String type = pos.getPosition();
						Integer h = pos.getHeight();
						Integer t = pos.getTop();
						if (t == null) {
							t = 0;
						}
						if (h == null) {
							h = 0;
						}
						if ("absolute".equals(type)) {
							if (t + h > absoluteHeight) {
								absoluteHeight = t + h;
							}
						} else {
							maxRelH += h;
							if (maxRelH + t > relativeHeight) {
								relativeHeight = maxRelH + t;
							}
						}
					}
				}
				pageView.addBlock(area, blockView);

				// 计算样式
				pageView.addStyle(makeStyle("#block" + block.getId(), block.getStyleItems()));
				pageView.addStyle(block.getStyle());
			}
			if (Str.isEmpty(page.getPageHeight()) && relativeHeight == 0)
				pageView.setHeight(absoluteHeight + "px");
			// else
			// if (!"auto".equals(uiPage.getPageHeight()))
			// pageUI.setHeight("auto");
		}

		pageView.adjust();

		return pageView;
	}

	public IStyle makeStyle(final String cssClassName, FakeSubSystem<? extends IStyleItem> itemsObj) {
		IStyle style = null;
		if (itemsObj != null) {
			List<? extends IStyleItem> items = itemsObj.getList();
			if (items != null && items.size() > 0) {
				final StringBuffer sb = new StringBuffer();
				for (IStyleItem s : items) {
					String code = s.getCode();
					String desc = s.getDesc();
					if (!Str.isEmpty(desc)) {
						if (Str.isEmpty(code))
							code = "";
						sb.append("\n").append(cssClassName).append(" ").append(code).append("{").append(desc).append("}");
					}
				}
				if (sb.length() > 0) {
					style = new SimpleStyle(null, cssClassName, sb.substring(1));
				}
			}
		}

		return style;
	}

	public UIBlockViewModel makeBlockView(IPageBlock pageBlock, Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData) {
		Map<Long, UIBlockViewModel> cachedBlockViews = new HashMap();
		Map<Long, IPageBlock> cachedBlocks = new HashMap();

		return this.makeBlockViewWithChilren(cachedBlockViews, cachedBlocks, null, pageBlock, dynamicModuleID, dynamicDataID, pathModule, pathData);
	}

	private UIBlockViewModel makeBlockViewWithChilren(Map<Long, UIBlockViewModel> cachedBlockViews, Map<Long, IPageBlock> cachedBlocks, IPageBlock defaultParentBlock, IPageBlock pageBlock,
			Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData) {

		UIBlockViewModel ret = this.makeBlockViewWithParent(null, cachedBlockViews, cachedBlocks, null, pageBlock, dynamicModuleID, dynamicDataID, pathModule, pathData);

		List<IPageBlock> children = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_UIUDF_PAGE_BLOCK), Expr.eq(F_PARENT, pageBlock).and(Expr.eq(F_DISABLED, false)).addAsc(F_ORDER_BY));
		for (IPageBlock b : children) {
			if (!cachedBlocks.containsKey(b.getId()))
				cachedBlocks.put(b.getId(), b);
		}
		for (IPageBlock b : children) {
			ret.addChild(makeBlockViewWithChilren(cachedBlockViews, cachedBlocks, defaultParentBlock, b, dynamicModuleID, dynamicDataID, pathModule, pathData));
		}

		return ret;
	}

	/**
	 * 解析板块视图及上级视图，先解析上级视图再解析板块视图
	 * 
	 * @param pageView
	 *            页面视图
	 * @param cachedBlockViews
	 *            板块视图CACHE，解析后的板块视图将被缓存到CACHE中。
	 * @param cachedBlocks
	 *            板块CACHE，解析的板块及上级板块必须在板块缓存中存在。
	 * @param defaultParentBlock
	 *            默认上级视图，通常情况下上级视图是通过block.getParent自动获取的；但对于页面引用型板块而言，
	 *            上级视图的默认视图是引用板块；对于非页面引用型板块而言，默认视图都将是null。
	 * @param block
	 *            带解析的板块
	 * @param dynamicModuleID
	 *            动态模块编号
	 * @param dynamicDataID
	 *            动态数据ID
	 * @param pathModule
	 *            URL路径中指定的模块对象
	 * @param pathData
	 *            URL路径中指定的数据对象
	 * @return
	 */
	private UIBlockViewModel makeBlockViewWithParent(UIPageView pageView, Map<Long, UIBlockViewModel> cachedBlockViews, Map<Long, IPageBlock> cachedBlocks, IPageBlock defaultParentBlock,
			IPageBlock block, Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData) {

		UIBlockViewModel blockView = cachedBlockViews.get(block.getId());

		if (blockView == null) {
			IPageBlock parentBlock = block.getParent(false);
			if (parentBlock != null && parentBlock.getId() != null) {
				parentBlock = cachedBlocks.get(parentBlock.getId());
				if (parentBlock == null) {
					parentBlock = block.getParent(true);
				}
			} else {
				parentBlock = defaultParentBlock;
			}

			if (parentBlock != null) {
				UIBlockViewModel parentBlockView = cachedBlockViews.get(parentBlock.getId());
				if (parentBlockView == null) {
					parentBlockView = makeBlockViewWithParent(pageView, cachedBlockViews, cachedBlocks, defaultParentBlock, parentBlock, dynamicModuleID, dynamicDataID, pathModule, pathData);
				}

				blockView = makeBlockView(pageView, parentBlockView, block, dynamicModuleID, dynamicDataID, pathModule, pathData);
				blockView.setParent(parentBlock.getId());
			} else {
				blockView = makeBlockView(pageView, null, block, dynamicModuleID, dynamicDataID, pathModule, pathData);
			}

			if (pathModule != null)
				blockView.set("pathModule", pathModule);
			if (pathData != null)
				blockView.set("pathData", pathData);

			cachedBlockViews.put(block.getId(), blockView);
		}

		// 处理引用页面子视图
		IPage refPage = block.getViewPage();
		if (refPage != null && refPage.getId() != null && refPage.getId() > 0) {
			List<? extends IPageBlock> childrenBlocks = loadPageBlocks(refPage.getId());
			if (childrenBlocks != null && childrenBlocks.size() > 0) {
				for (IPageBlock child : childrenBlocks) {
					cachedBlocks.put(child.getId(), child);
				}
				for (IPageBlock childBlock : childrenBlocks) {
					blockView.addChild(makeBlockViewWithParent(pageView, cachedBlockViews, cachedBlocks, block, childBlock, dynamicModuleID, dynamicDataID, pathModule, pathData));

					if (pageView != null) {
						pageView.addStyle(makeStyle("#block" + childBlock.getId(), childBlock.getStyleItems()));
						pageView.addStyle(childBlock.getStyle());
					}
				}
				blockView.adjust();
			}
		}

		return blockView;
	}

	private UIBlockViewModel makeBlockView(UIPageView pageView, UIBlockViewModel parentBlockView, IPageBlock block, Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData) {

		UIBlockView expr = new UIBlockView(MvcUtil.globalVariables, block.getId());
		if (Demsy.me().login() != null) {
			expr.set("login", Demsy.me().login());
		}
		UIBlockViewModel blockView = new UIBlockViewModel(expr, null);
		blockView.setId(block.getId());

		// 计算行内样式
		CssPosition pos = block.getPosition();
		if (pos != null) {
			StringBuffer cssStyle = new StringBuffer();
			Integer align = pos.getAlign();
			String p = "";
			if (align == null || align <= 0) {
				p = pos.getPosition();
				if (Str.isEmpty(p) && pos.getLeft() != null && pos.getTop() != null)
					p = "absolute";
				if (!Str.isEmpty(p))
					cssStyle.append("position:").append(p).append(";");
				if (pos.getLeft() != null) {
					cssStyle.append("left:").append(pos.getLeft()).append("px;");
				}
				if (pos.getTop() != null) {
					cssStyle.append("top:").append(pos.getTop()).append("px;");
				}
			} else {
				cssStyle.append("position: relative;");
				switch (align) {
				case 1:// 左
					cssStyle.append("margin: auto;margin-left: 0;");
					break;
				case 2:// 右
					cssStyle.append("margin: auto;margin-right: 0;");
					break;
				case 3:// 顶
					cssStyle.append("margin: 0 auto;");
					break;
				case 4:// 底
					cssStyle.append("margin: auto;margin-bottom: 0;");
					break;
				}
			}

			Integer h = pos.getHeight();
			if (h == null || h <= 0) {
				h = 100;
			}
			if ("relative".equals(p)) {
				boolean layoutable = false;
				if (Demsy.me().get("layoutable") != null) {
					layoutable = (Boolean) Demsy.me().get("layoutable");
				}
				if (layoutable)
					cssStyle.append("height:").append(h).append("px;overflow: hidden;");
			} else {
				cssStyle.append("height:").append(h).append("px;");
			}

			Integer w = pos.getWidth();
			if (w == null || w <= 0) {
				w = 100;
			}
			cssStyle.append("width:").append(w).append("px;");

			if (!Str.isEmpty(block.getInlineStyle()))
				cssStyle.append(block.getInlineStyle());

			expr.setCssStyle(cssStyle.toString());
		}

		// 计算板块风格
		IStyle style = block.getStyle();
		if (style != null) {
			expr.setCssClass(style.getCssClass());
		}

		// 计算展现数据和展现模版
		IUIViewComponent lib = block.getViewType();
		String controllerClass = block.getViewController();
		String viewTemplate = block.getViewTemplate();
		String viewExpression = block.getViewExpression();
		if (lib != null) {
			if (Str.isEmpty(controllerClass)) {
				controllerClass = lib.getViewController();
			}
			if (Str.isEmpty(viewTemplate) && Str.isEmpty(viewExpression)) {
				viewTemplate = lib.getViewTemplate();
				viewExpression = lib.getViewExpression();
			}
		}
		IPage refPage = block.getViewPage();
		if (refPage != null && refPage.getId() != null && refPage.getId() > 0) {
			viewTemplate = "ui.lib.RefPage";
		}
		if (Str.isEmpty(viewTemplate)) {
			viewTemplate = "ui.lib.Empty";
		}

		// 视图控制器处理业务逻辑
		if (!Str.isEmpty(controllerClass)) {
			IUIViewController controller = this.getUIController(controllerClass);
			UIBlockContext parentBlockContext = null;
			if (parentBlockView != null) {
				parentBlockContext = (UIBlockContext) parentBlockView.get("ctx");
			}
			UIBlockContext blockContext = new UIBlockContext(pageView, parentBlockContext, block, dynamicModuleID, dynamicDataID);
			try {

				blockView.setData(controller.process(blockContext));

				// blockParser不再使用： 为遗留系统保留
				blockView.set("blockParser", blockContext);
				blockView.set("ctx", blockContext);

				viewTemplate = controller.getViewTemplate(blockContext, viewTemplate);
				viewExpression = controller.getViewExpression(blockContext, viewExpression);

			} catch (Throwable e) {
				if (!appconfig.isProductMode() || log.isTraceEnabled())
					log.error("加载数据源数据出错! " + Ex.msg(e), e);
				else
					log.errorf("加载数据源数据出错! %s", Ex.msg(e));
			}
		}

		if (Str.isEmpty(viewExpression)) {
			blockView.setTemplate(viewTemplate);
		} else {
			// 这里的模版用全类名，用于解析表达式中的include指令
			blockView.setTemplate("ui.view.Empty");
			blockView.setExpression(viewExpression);
		}
		blockView.set("block", block);
		expr.setParam(block);

		return blockView;
	}

	private void addViewComponent(Node fnode, IUIViewComponent viewType, IBizField field, String var) {
		fnode.setType(viewType.getCode());
		fnode.set("dataID", viewType.getId());
		fnode.set("defaultWidth", viewType.getDefaultWidth());
		fnode.set("defaultHeight", viewType.getDefaultHeight());
		fnode.set("moduleGuid", "");
		String propName = field.getPropName();

		StringBuffer expr = new StringBuffer();
		if (bizEngine.isImage(field)) {
			expr.append("{import file=\"a.st\" href=$").append(var).append(".href target=$").append(var).append(".target img=$").append(var).append(".obj.").append(propName).append("}");
			fnode.set("expr", expr.toString());
		} else if (bizEngine.isNumber(field) && !bizEngine.isInteger(field)) {
			String pattern = field.getPattern();
			if (Str.isEmpty(pattern)) {
				pattern = "#,##0.00";
			}
			expr.append("{tostring bean=$").append(var).append(".obj.").append(propName).append(" pattern=\"").append(pattern).append("\"}");
			fnode.set("expr", expr.toString());
		} else if (bizEngine.isDate(field)) {
			String pattern = field.getPattern();
			if (Str.isEmpty(pattern)) {
				pattern = "yyyy-MM-dd HH:mm";
			}
			expr.append("{tostring bean=$").append(var).append(".obj.").append(propName).append(" pattern=\"").append(pattern).append("\"}");
			fnode.set("expr", expr.toString());
		} else if ("name".equals(propName)) {
			expr.append("{import file=\"a.st\" href=$").append(var).append(".href target=$").append(var).append(".target title=$").append(var).append(".title name=$").append(var).append(".name}");
			fnode.set("expr", expr.toString());
		} else {
			expr.append("{tostring bean=$").append(var).append(".obj.").append(propName).append("}");
			fnode.set("expr", expr.toString());
		}
	}

	private void addViewComponent(Nodes root, IModule folder, Node folderNode, IUIViewComponent viewController) {
		for (Node moduleNode : folderNode.getChildren()) {
			IModule module = (IModule) moduleNode.get("module");

			List<Node> children = moduleNode.getChildren();
			if (children != null && children.size() > 0) {
				this.addViewComponent(root, folder, moduleNode, viewController);
			}
			if (module.getType() != IModule.TYPE_BIZ) {
				continue;
			}

			Node fnode = root.addNode("bzmodule" + folder.getId(), "bzmodule" + module.getId()).setName(module.getName());

			fnode.setType(viewController.getCode());
			fnode.set("dataID", viewController.getId());
			fnode.set("defaultWidth", viewController.getDefaultWidth());
			fnode.set("defaultHeight", viewController.getDefaultHeight());
			fnode.set("moduleGuid", module.getEntityGuid());
			fnode.set("expr", "");
		}
	}

	public Nodes makeNodesOfViewCompnents(Long blockID) {
		// 加载视图组件库：如果已经加载则不重复加载
		loadViewComponents();

		Nodes root = Nodes.make();

		/*
		 * 加载板块数据源，并将板块数据源相关的“分类系统字段、系统字段”转换成“表达式视图”
		 */
		if (blockID != null) {
			IPageBlock pageBlock = this.loadPageBlock(blockID);

			// 获取上级板块视图类型
			IUIViewComponent parentViewComponent = pageBlock.getViewType();
			String parentViewCode = "";
			if (parentViewComponent != null) {
				parentViewCode = parentViewComponent.getCode();
			}

			// 获取字段板块默认视图类型
			IUIViewComponent exprView = null;
			for (IUIViewComponent t : this.viewComponentList) {
				if ("expressionView".equals(t.getCode())) {
					exprView = t;
					break;
				}
			}

			UIBlockViewModel blockView = this.makeBlockView(pageBlock, null, null, null, null);
			UIBlockContext blockContext = (UIBlockContext) blockView.get("ctx");
			if (blockContext != null) {
				IBizSystem catalogSystem = blockContext.getCatalogSystem();
				IBizSystem system = blockContext.getSystem();
				if (catalogSystem != null) {
					Node node = root.addNode(null, "catalogSystemFields").setName(catalogSystem.getName() + "(字段表达式)");
					node.setType("catalogSystemFields");
					List<? extends IBizField> fields = bizEngine.getFieldsOfEnabled(catalogSystem);
					for (IBizField f : fields) {
						Node fnode = root.addNode("catalogSystemFields", "field" + f.getId()).setName(f.getName());
						addViewComponent(fnode, exprView, f, "ctx.catalog");
					}
				}
				if (system != null) {
					Node node = root.addNode(null, "systemFields").setName(system.getName() + "(字段表达式)");
					node.setType("systemFields");
					List<? extends IBizField> fields = bizEngine.getFieldsOfEnabled(system);
					for (IBizField f : fields) {
						Node fnode = root.addNode("systemFields", "field" + f.getId()).setName(f.getName());

						// 如果上级板块为迭代视图，则子视图表达式变量不含ctx.前缀
						if ("listView".equals(parentViewCode)) {
							addViewComponent(fnode, exprView, f, "item");
						} else {
							addViewComponent(fnode, exprView, f, "ctx.item");
						}

					}
				}
			}
		}

		/*
		 * 加载业务模块并转换业务模块为“视图控制器”
		 */
		IUIViewComponent viewController = null;
		for (IUIViewComponent t : this.viewComponentList) {
			if ("viewController".equals(t.getCode())) {
				viewController = t;
				break;
			}
		}
		Nodes moduleNodes = moduleEngine.makeNodesByModule(Demsy.me().getSoft(), IUserRole.ROLE_ADMIN_ROOT);
		for (Node folderNode : moduleNodes.getChildren()) {
			IModule folder = (IModule) folderNode.get("module");
			Node bznode = root.addNode(null, "bzmodule" + folder.getId()).setName(folder.getName() + "(数据控制器)");
			bznode.setType("bzmodules");

			this.addViewComponent(root, folder, folderNode, viewController);
		}

		/*
		 * 加载视图组件库
		 */
		for (IUIViewComponent ele : viewComponentList) {
			IUIViewComponent parent = ele.getParent();
			Node node = root.addNode(parent == null ? null : parent.getId(), ele.getId()).setName(ele.getName());
			node.setType(ele.getCode());
			node.setDesc(ele.getDesc());
			node.set("dataID", ele.getId());
			if (ele.getDefaultWidth() > 0)
				node.set("defaultWidth", ele.getDefaultWidth());
			else
				node.set("defaultWidth", 200);

			if (ele.getDefaultHeight() > 0)
				node.set("defaultHeight", ele.getDefaultHeight());
			else
				node.set("defaultHeight", 200);

			node.set("expr", "");
			node.set("moduleGuid", "");
		}

		return root;
	}

	private void loadUiStyle() {
		if (this.styleCache.size() == 0 || !appconfig.isProductMode()) {
			List<IStyle> uiStyleList = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_UIUDF_STYLE), Expr.asc(F_ORDER_BY));
			for (IStyle ele : uiStyleList) {
				if (ele.getId() != null)
					this.styleCache.put(ele.getId(), ele);
			}
		}
	}

	public IStyle loadStyle(Long styleID) {
		loadUiStyle();

		return this.styleCache.get(styleID);
	}

	public void addClickNum(IOrm orm, IStatistic obj) {
		moduleEngine.increase(orm, obj, "clickNum");
	}

	public void addCommentNum(IOrm orm, IStatistic obj) {
		moduleEngine.increase(orm, obj, "commentNum");
	}

}

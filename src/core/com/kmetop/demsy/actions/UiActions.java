package com.kmetop.demsy.actions;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.Demsy.security;
import static com.kmetop.demsy.Demsy.uiEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_STYLE;
import static com.kmetop.demsy.comlib.LibConst.F_ID;
import static com.kmetop.demsy.mvc.MvcConst.VW_BIZ;

import java.util.HashMap;
import java.util.Map;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IStyle;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Nodes;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.nutz.DemsyAdaptor;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIBlockViewModel;
import com.kmetop.demsy.mvc.ui.widget.UIBlockView;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 路径格式
 * <UL>
 * <LI>页面：/ui/pageID
 * <LI>参数页面：/ui/pageID/moduleID:dataID?page=...
 * <LI>板块：/ui/pageID:blockID
 * <LI>参数板块：/ui/pageID:blockID/moduleID:dataID?page=...
 * </UL>
 * 
 * @author yongshan.ji
 */
@Ok(VW_BIZ)
@Fail(VW_BIZ)
@AdaptBy(type = DemsyAdaptor.class)
public class UiActions extends ModuleActions {

	@At(URL_UI)
	public IUIView page(String pageParam, String dataParam) throws DemsyException {
		log.debugf("访问页面... [pageParam=%s, dataParam=%s]", pageParam, dataParam);

		String[] params = parseParam(dataParam);
		String moduleID = params[0];
		String dataID = params[1];

		params = parseParam(pageParam);
		String pageID = params[0];
		String blockID = params[1];

		UIPageView ui = null;
		if (!Str.isEmpty(pageID)) {
			try {
				ui = (UIPageView) uiEngine.makePageView(Long.parseLong(pageID),
						Str.isEmpty(blockID) ? null : Long.parseLong(blockID),
						Str.isEmpty(moduleID) ? null : Long.parseLong(moduleID),
						Str.isEmpty(dataID) ? null : Long.parseLong(dataID));
				if (ui != null) {
					ui.set("loadBlockUrl", MvcUtil.contextPath(URL_UI_BLOCK, ""));
					ui.set("dataParam", Str.isEmpty(dataParam) ? "" : dataParam);
				}
			} catch (Throwable e) {
				log.error("加载页面模版出错! " + Demsy.me().request().getRequestURI(), e);
			}
		}

		log.debugf("访问页面结束. [pageParam=%s, dataParam=%s]", pageParam, dataParam);
		return ui;
	}

	@At(URL_UI_BLOCK)
	public IUIView block(String blockID, String dataParam) throws DemsyException {
		log.debugf("访问板块... [blockID=%s, dataParam=%s]", blockID, dataParam);

		String[] params = parseParam(dataParam);
		String moduleID = params[0];
		String dataID = params[1];

		Long mid = null;
		Long did = null;
		try {
			mid = Str.isEmpty(moduleID) ? null : Long.parseLong(moduleID);
			did = Str.isEmpty(dataID) ? null : Long.parseLong(dataID);
		} catch (Throwable e) {
		}

		IModule pathModule = null;
		Object pathData = null;
		if (mid != null && mid > 0) {
			if (did != null && did >= 0) {
				pathModule = moduleEngine.getModule(mid);
				IBizSystem pathSystem = moduleEngine.getSystem(pathModule);
				pathData = Demsy.orm().load(bizEngine.getType(pathSystem), Expr.eq(F_ID, did));
			}
		}
		UIBlockViewModel block = uiEngine.makeBlockView(uiEngine.loadPageBlock(Long.parseLong(blockID)), mid, did,
				pathModule, pathData);
		block.setAjaxData(true);
		block.set("dataParam", Str.isEmpty(dataParam) ? "" : dataParam);

		log.debugf("访问板块结束. [blockID=%s, dataParam=%s]", blockID, dataParam);
		return block;
	}

	@At(URL_UI_STYLE)
	@Ok("st:ui/lib/Style")
	public Map style(String styleID) throws DemsyException {
		Map context = new HashMap();
		if (!Str.isEmpty(styleID)) {
			IStyle style = uiEngine.loadStyle(Long.parseLong(styleID));

			if (style != null) {
				context.put("style", style);
			}
		}

		return context;
	}

	/**
	 * 具有【超级管理员】以上权限的用户可以在【开发模式】下排版页面，否则不允许排版页面。
	 * 
	 * @param pageID
	 * @param dataParam
	 * @return
	 * @throws DemsyException
	 */
	private boolean canLayout(Long pageID) {
		try {
			if (Demsy.appconfig.isProductMode()) {
				return false;
			}
			security.checkLogin(IUserRole.ROLE_ADMIN_ROOT);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * 具有【超级管理员】以上权限的用户可以在【开发模式】下排版页面，否则不允许排版页面。
	 * 
	 * @param pageID
	 * @param dataParam
	 * @return
	 * @throws DemsyException
	 */
	@At(URL_ADMIN_UI)
	public IUIView admin(String pageParam, String dataParam) throws DemsyException {
		String[] params = parseParam(pageParam);
		String pageID = params[0];
		if (Str.isEmpty(pageID)) {
			return null;
		}

		UIPageView ui = (UIPageView) page(pageParam, dataParam);
		if (ui == null) {
			return null;
		}
		try {
			if (!canLayout(Long.parseLong(pageID))) {
				return ui;
			}
		} catch (Throwable e) {
			return ui;
		}

		ui.set("pageID", pageID);
		ui.setTemplate(ui.getTemplate() + "Layout");
		ui.set("layoutable", true);

		Demsy me = Demsy.me();

		ui.set("loadPageUrl", MvcUtil.contextPath(URL_ADMIN_UI, ""));
		ui.set("loadUilibUrl", MvcUtil.contextPath(URL_ADMIN_UILIB, ""));

		long pageMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(BIZSYS_UIUDF_PAGE)).getId();
		ui.set("editPageUrl", MvcUtil.contextPath(URL_BZFORM_EDIT, pageMdl + ":", "e:"));
		ui.set("edit1PageUrl", MvcUtil.contextPath(URL_BZFORM_EDIT, pageMdl + ":", "e1:"));
		ui.set("savePageUrl", MvcUtil.contextPath(URL_BZ_SAVE, pageMdl, "e:"));

		long blockMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(BIZSYS_UIUDF_PAGE_BLOCK)).getId();
		ui.set("createUrl", MvcUtil.contextPath(URL_BZFORM_ADD, blockMdl + ":", "c1"));
		ui.set("editUrl", MvcUtil.contextPath(URL_BZFORM_EDIT, blockMdl + ":", "e:"));
		ui.set("edit1Url", MvcUtil.contextPath(URL_BZFORM_EDIT, blockMdl + ":", "e1:"));
		ui.set("saveUrl", MvcUtil.contextPath(URL_BZ_SAVE, blockMdl, "e:"));
		ui.set("loadUrl", MvcUtil.contextPath(URL_UI_BLOCK, ""));

		long styleMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(BIZSYS_UIUDF_STYLE)).getId();
		ui.set("createStyleUrl", MvcUtil.contextPath(URL_BZFORM_ADD, styleMdl + ":", "c"));
		ui.set("editStyleUrl", MvcUtil.contextPath(URL_BZFORM_EDIT, styleMdl + ":", "e:"));
		ui.set("saveStyleUrl", MvcUtil.contextPath(URL_BZ_SAVE, styleMdl, "e:"));
		ui.set("loadStyleUrl", MvcUtil.contextPath(URL_UI_STYLE, ""));

		long webCataMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(IWebContentCatalog.SYS_CODE))
				.getId();
		ui.set("createWebCataUrl", MvcUtil.contextPath(URL_BZFORM_ADD, webCataMdl + ":", "c"));
		ui.set("saveWebCataUrl", MvcUtil.contextPath(URL_BZ_SAVE, webCataMdl, "e:"));

		long webContMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(IWebContent.SYS_CODE)).getId();
		ui.set("createWebContUrl", MvcUtil.contextPath(URL_BZFORM_ADD, webContMdl, "e"));

		return ui;
	}

	@At(URL_ADMIN_UILIB)
	public IUIView widgetTypeDialog(String strBlockID) throws DemsyException {
		UIBlockView model = new UIBlockView(MvcUtil.globalVariables, "uilib");
		UIBlockViewModel dataModel = new UIBlockViewModel(model, null);
		dataModel.setTemplate("ui.lib.UiLib");
		dataModel.setAjaxData(true);

		Long blockID = null;
		if (!Str.isEmpty(strBlockID)) {
			blockID = Long.parseLong(strBlockID);
		}

		Nodes viewComponentNodes = uiEngine.makeNodesOfViewCompnents(blockID);
		dataModel.set("viewComponentNodes", viewComponentNodes);

		return dataModel;
	}
}

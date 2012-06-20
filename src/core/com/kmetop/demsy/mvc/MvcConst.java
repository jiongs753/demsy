package com.kmetop.demsy.mvc;

import static com.kmetop.demsy.Demsy.appconfig;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.config.IAppConfig;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.render.IRender;
import com.kmetop.demsy.mvc.template.ITemplateEngine;

public interface MvcConst {

	public static final String URL_PREFIX_BZ = "/bz/";

	public static final String URL_UI = "/ui/*";

	public static final String URL_UI_BLOCK = "/ui/block/*";

	public static final String URL_UI_STYLE = "/ui/style/*";

	public static final String URL_PREFIX_AJAX = "^";

	public static final String URL_BZMAIN = URL_PREFIX_BZ + BizConst.TYPE_BZMAIN + "/*";

	public static final String URL_BZ_ORDER_ADDTOCART = URL_PREFIX_BZ + "order/addtocart/*";

	public static final String URL_BZ_ORDER_DELETECART = URL_PREFIX_BZ + "order/deletecart/*";

	public static final String URL_BZ_ORDER_REFRESHCART = URL_PREFIX_BZ + "order/refreshcart/*";

	public static final String URL_BZ_ORDER_DIRECTBUY = URL_PREFIX_BZ + "order/directbuy/*";

	public static final String URL_BZ_ORDER_PREPARE = URL_PREFIX_BZ + "order/prepare";

	public static final String URL_BZ_ORDER_SUBMIT = URL_PREFIX_BZ + "order/submit";

	public static final String URL_BZ_ORDER_ALIPAYTO = URL_PREFIX_BZ + "order/alipayto/*";

	public static final String URL_BZ_ORDER_ALIPAYRETURN = URL_PREFIX_BZ + "order/alipayreturn";

	public static final String URL_BZ_ORDER_ALIPAYNOTIFY = URL_PREFIX_BZ + "order/alipaynotify";

	/**
	 * 模块业务窗体访问路径——主界面
	 * <UL>
	 * <LI>模块参数——模块ID:页面ID
	 * </UL>
	 */
	public static final String URL_BZSYS = URL_PREFIX_BZ + BizConst.TYPE_BZSYS + "/*";

	public static final String URL_BZSYS_COMB_EXPR = URL_PREFIX_BZ + BizConst.TYPE_BZSYS_COMB_EXPR + "/*";

	public static final String URL_BZSYS_COMB_CATALOG_EXPR = URL_PREFIX_BZ + BizConst.TYPE_BZSYS_COMB_CATALOG_EXPR + "/*";

	public static final String URL_BZSYS_COMB_FK = URL_PREFIX_BZ + BizConst.TYPE_BZSYS_COMB_FK + "/*";

	/**
	 * 模块业务窗体访问路径——数据网格
	 * <UL>
	 * <LI>模块参数——模块ID:页面ID
	 * </UL>
	 */
	public static final String URL_BZGRID = URL_PREFIX_BZ + BizConst.TYPE_BZGRID + "/*";

	/**
	 * 模块业务窗体访问路径——工具栏操作菜单
	 * <UL>
	 * <LI>模块参数——模块ID:页面ID
	 * </UL>
	 */
	public static final String URL_BZMENU = URL_PREFIX_BZ + BizConst.TYPE_BZMENU + "/*";

	/**
	 * 业务模块数据分类导航菜单
	 * <UL>
	 * <LI>模块参数——模块ID:页面ID
	 * </UL>
	 */
	public static final String URL_BZNAVI = URL_PREFIX_BZ + BizConst.TYPE_BZNAVI + "/*";

	/**
	 * 模块业务窗体访问路径——数据编辑表单
	 * <OL>
	 * <LI>模块参数——模块ID:页面ID
	 * <LI>动作参数——数据ID:动作ID
	 * </OL>
	 */
	public static final String URL_BZFORM_ADD = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_NEW + "/*";

	public static final String URL_BZFORM_EDIT = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_EDIT + "/*";

	public static final String URL_BZFORM_EDIT_N = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_EDIT_N + "/*";

	public static final String URL_BZFORM_PRINT = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_PRINT + "/*";

	public static final String URL_BZFORM_IMPORT_XLS = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_IMPORT_XLS + "/*";

	public static final String URL_BZFORM_EXPORT_XLS = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_EXPORT_XLS + "/*";

	public static final String URL_BZFORM_EXEC_SYNC = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_EXEC_SYNC + "/*";

	public static final String URL_BZFORM_EXEC_ASYN = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_EXEC_ASYN + "/*";

	// 无表单
	public static final String URL_BZ_IMPORT_XLS = URL_PREFIX_BZ + BizConst.TYPE_BZ_IMPORT_XLS + "/*";

	public static final String URL_BZ_EXPORT_XLS = URL_PREFIX_BZ + BizConst.TYPE_BZ_EXPORT_XLS + "/*";

	public static final String URL_BZ_SAVE = URL_PREFIX_BZ + BizConst.TYPE_BZ_SAVE + "/*";

	public static final String URL_BZ_DEL = URL_PREFIX_BZ + BizConst.TYPE_BZ_DEL + "/*";

	public static final String URL_BZ_CLEAR = URL_PREFIX_BZ + BizConst.TYPE_BZ_CLEAR + "/*";

	public static final String URL_BZ_ORDERBY_CANCEL = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_CANCEL + "/*";

	public static final String URL_BZ_ORDERBY_UP = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_UP + "/*";

	public static final String URL_BZ_ORDERBY_DOWN = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_DOWN + "/*";

	public static final String URL_BZ_ORDERBY_REVERSE = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_REVERSE + "/*";

	public static final String URL_BZ_ORDERBY_TOP = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_TOP + "/*";

	public static final String URL_BZ_ORDERBY_BOTTOM = URL_PREFIX_BZ + BizConst.TYPE_BZ_ORDERBY_BOTTOM + "/*";

	/**
	 * 模块业务窗体访问路径——数据保存
	 * <OL>
	 * <LI>模块参数——模块ID:页面ID
	 * <LI>动作参数——数据ID:动作ID
	 * </OL>
	 */
	public static final String URL_BZFORM_LOAD = URL_PREFIX_BZ + BizConst.TYPE_BZFORM_LOAD + "/*";

	public static final String URL_BZ_EXEC_SYNC = URL_PREFIX_BZ + BizConst.TYPE_BZ_EXEC_SYNC + "/*";

	public static final String URL_BZ_EXEC_ASYN = URL_PREFIX_BZ + BizConst.TYPE_BZ_EXEC_ASYN + "/*";

	public static final String URL_UPLOAD = "/ul/*";

	public static final String URL_CKFINDER = "/ul/ckfinder";

	public static final String URL_SEC_LOGIN_FORM = "/login/form";

	public static final String URL_SEC_LOGIN = "/login";

	public static final String URL_SEC_LOGOUT = "/logout";

	public static final String URL_ADMIN = "/admin";

	public static final String URL_INDEX = "/index";

	public static final String URL_ADMIN_MENU = URL_ADMIN + "/menu/*";

	public static final String URL_ADMIN_TOP = URL_ADMIN + "/top/*";

	public static final String URL_ADMIN_UI = URL_ADMIN + "/ui/*";

	public static final String URL_ADMIN_UILIB = URL_ADMIN + "/uilib/*";

	public static final String URL_ADMIN_CHGPWDFORM = URL_ADMIN + "/chgpwdform";

	public static final String URL_ADMIN_CHGPWD = URL_ADMIN + "/chgpwd";

	public static final String URL_CONFIG = "/config/*";

	public static final String URL_CONFIG_SAVE = "/config/save";

	public static final String URL_CONFIG_TESTCONN = "/config/testconn";

	public static final String UI_BZFORM_PREFIX = "data.";

	/*
	 * 视图类型
	 */
	/**
	 * 业务窗体
	 */
	public static final String VW_BIZ = "biz";

	/*
	 * 模版类型
	 */
	/**
	 * Smarty模版后缀
	 */
	public static final String TPL_ST = ".st";

	/**
	 * Freemarker模版后缀
	 */
	public static final String TPL_FTL = ".ftl";

	/**
	 * Velocity模版后缀
	 */
	public static final String TPL_VM = ".vm";

	/*
	 * 数据类型
	 */
	/**
	 * JSON数据
	 */
	public static final String DATA_JSON = "json";

	/**
	 * XML数据
	 */
	public static final String DATA_XML = "xml";

	/**
	 * HTML数据
	 */
	public static final String DATA_HTML = "html";

	public static enum FieldMode {
		M, E, I, S, N, P, H, R, D
	}

	public static enum Align {
		RIGHT, CENTER, LEFT, TOP, BOTTOM
	}

	public static abstract class MvcUtil {
		// template
		public static ITemplateEngine tplEngineFTL = null;

		public static ITemplateEngine tplEngineVM = null;

		public static ITemplateEngine tplEngineST = null;

		public static ITemplateEngine tplEngineDefault = null;

		// render
		public static IRender renderBizModel = null;

		public static IRender renderBizData = null;

		// MVC全局变量
		public static final Map globalVariables = new HashMap();
		static {
			initGlobalVariables();
		}

		public static void initGlobalVariables() {
			/*
			 * 设置模版环境变量——全局变量
			 */
			// 静态资源URL前缀 = 静态资源域名路径 + 环境路径
			globalVariables.put("contextPath", contextPath(""));

			globalVariables.put("themePath", appconfig.getThemePath());
			globalVariables.put("stylePath", appconfig.getCssDomainPath() + contextPath(appconfig.getThemePath()));
			globalVariables.put("imagePath", appconfig.getImgDomainPath() + contextPath(appconfig.getThemePath()));
			globalVariables.put("scriptPath", appconfig.getScriptDomainPath() + contextPath("/scripts2"));

			// UI 全局样式设置
			globalVariables.put("fontSize", "12px");
		}

		/**
		 * 计算环境路径并将路径中的/*替换成真实的路径参数，如 /main/* 用[119,12]替换后变成 /main/119/12
		 * 
		 * @param path
		 *            指定的路径
		 * @param params
		 *            路径参数：用于替换path中的/*部分
		 * @return 环境路径：即自动加上环境路径前缀
		 */
		public static String contextPath(String path, Object... params) {
			if (Str.isEmpty(path)) {
				return Demsy.contextPath;
			}

			StringBuffer urlParam = new StringBuffer();
			for (Object param : params) {
				urlParam.append("/");
				if (param == null)
					urlParam.append("");
				else
					urlParam.append(param);
			}

			if (params != null && params.length > 0) {
				path = path.replace("/*", urlParam);
			}

			if (path.startsWith("/")) {
				return Demsy.contextPath + path;
			}

			return path;
		}

		public static String requestPath(HttpServletRequest req) {
			String path = req.getPathInfo();
			if (path == null)
				return req.getServletPath();

			return path;
		}

		public static String requestQueryString(HttpServletRequest req) {
			return req.getQueryString();
		}

		public static String requestQueryJsonString(HttpServletRequest req) {
			Map<String, Object> paramMap = new HashMap();
			Enumeration<String> names = req.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String[] value = req.getParameterValues(name);
				if (value != null && value.length > 0) {
					if (value.length == 1) {
						paramMap.put(name, value[0]);
					} else {
						paramMap.put(name, value);
					}
				}
			}
			return Json.toJson(paramMap).replace("\r\n", "").replace("\n\r", "").replace("\n", "").replace("\r", "");
		}

		public static String requestURI(HttpServletRequest req) {
			String url = req.getRequestURI();
			if (!Str.isEmpty(req.getQueryString()))
				return url + "?" + req.getQueryString();

			return url;
		}

		public static String requestURL(HttpServletRequest req) {
			String url = req.getRequestURL().toString();
			if (!Str.isEmpty(req.getQueryString()))
				return url + "?" + req.getQueryString();

			return url;
		}

		public static String getUploadBasePath() {
			String folder = appconfig.get(IAppConfig.PATH_UPLOAD);
			if (Str.isEmpty(folder)) {
				folder = "/upload";
			}
			if (Demsy.me().getSoft() == null || Demsy.me().getSoft().getId() == null)
				throw new DemsyException("非法软件编号");

			String softCode = Demsy.me().getSoft().getCode();
			softCode = softCode.replace("www.", "").replace(".com", "").replace(".cn", "").replace(".", "_");
			// String softCode = "" + Demsy.me().getSoft().getId();

			folder += "/" + softCode;

			return folder;
		}

	}

}

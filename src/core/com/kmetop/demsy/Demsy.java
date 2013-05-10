package com.kmetop.demsy;

import java.io.File;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.castor.Castors;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.LoadingBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.UrlMappingBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.impl.ActionInvoker;
import org.nutz.trans.Trans;

import com.handsome.ip.IPSeeker;
import com.kmetop.demsy.actions.BizActions;
import com.kmetop.demsy.actions.ConfigActions;
import com.kmetop.demsy.actions.AppActions;
import com.kmetop.demsy.actions.IndexActions;
import com.kmetop.demsy.actions.OrderActions;
import com.kmetop.demsy.actions.SecurityActions;
import com.kmetop.demsy.actions.UiActions;
import com.kmetop.demsy.actions.UploadActions;
import com.kmetop.demsy.biz.IBizManagerFactory;
import com.kmetop.demsy.biz.IBizSession;
import com.kmetop.demsy.comlib.IBizEngine;
import com.kmetop.demsy.comlib.IModuleEngine;
import com.kmetop.demsy.comlib.IUiEngine;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAdminUser;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.config.IAppConfig;
import com.kmetop.demsy.config.IDataSource;
import com.kmetop.demsy.config.impl.AppConfig;
import com.kmetop.demsy.config.impl.DbConfig;
import com.kmetop.demsy.config.impl.LogConfig;
import com.kmetop.demsy.ctx.IIoc;
import com.kmetop.demsy.ctx.IocLoader;
import com.kmetop.demsy.lang.Assert;
import com.kmetop.demsy.lang.ConfigException;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Lists;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.nutz.DemsyActionHandler;
import com.kmetop.demsy.mvc.nutz.DemsyAdaptor;
import com.kmetop.demsy.mvc.nutz.DemsyIocProvider;
import com.kmetop.demsy.mvc.nutz.DemsyLoading;
import com.kmetop.demsy.mvc.nutz.DemsyNutConfig;
import com.kmetop.demsy.mvc.nutz.DemsySetup;
import com.kmetop.demsy.mvc.nutz.DemsyUrlMappingImpl;
import com.kmetop.demsy.mvc.render.DataRender;
import com.kmetop.demsy.mvc.render.ViewRender;
import com.kmetop.demsy.mvc.template.ITemplateEngine;
import com.kmetop.demsy.mvc.template.SmartyTemplate;
import com.kmetop.demsy.mvc.view.DemsyViewMaker;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.generator.INamingStrategy;
import com.kmetop.demsy.orm.generator.impl.EncodeNamingStrategy;
import com.kmetop.demsy.orm.listener.EntityListeners;
import com.kmetop.demsy.orm.nutz.EnMappingHolder;
import com.kmetop.demsy.orm.nutz.EnMappingMaker;
import com.kmetop.demsy.orm.nutz.impl.DemsyTransaction;
import com.kmetop.demsy.orm.nutz.impl.OrmImpl;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.security.ISecurity;
import com.kmetop.demsy.security.SecurityException;

/**
 * 【DEMSY】：包括【DEMSY平台上下文环境】(简称【DEMSY平台】) 和 【DEMSY动作上下文环境】(简称【DEMSY动作】)。
 * <p>
 * <UL>
 * <li>静态方法：用于获取【DEMSY平台】配置信息
 * <li>实例方法：用户获取【DEMSY动作】参数信息
 * </UL>
 * <p>
 * <b>可以从【DEMSY平台上下文环境】中：</b>
 * <UL>
 * <LI>初始化【DEMSY平台上下文环境】 {@link #init(ServletContext)}
 * <LI>初始化【DEMSY请求上下文环境】
 * {@link #initMe(HttpServletRequest, HttpServletResponse)}
 * <LI>获取环境相对路径 {@link #contextPath()}
 * <LI>获取环境真实路径 {@link #contextDir()}
 * <LI>根据bean名称获取IOC中的对象 {@link #bean(String)}
 * <LI>获取业务管理器工厂 {@link #bizManagerFactory()}
 * <LI>获取业务回话对象 {@link #bizSession()}
 * <LI>获取业务系统引擎 {@link #bizEngine()}
 * <LI>获取操作数据库META信息的MetaDao {@link #beanMetaDao()}
 * <LI>获取安全管理器 {@link #security()}
 * <LI>获取展现组件工厂 {@link #uiFactory()}
 * <LI>获取动作处理器 {@link #actionHandler()}
 * <LI>获取初始化参数名称列表 {@link #initParamNames()}
 * <LI>获取初始化参数 {@link #initParam(String)}
 * <LI>获取初始化配置路径 {@link #configRealPath()}
 * <LI>获取初始化日志路径 {@link #configRealPathOfLogs()}
 * <LI>获取平台环境属性名称列表 {@link #contextAttrNames()}
 * <LI>获取平台环境属性 {@link #contextAttr(String)}
 * <LI>设置平台环境属性 {@link #contextAttr(String, Object)}
 * <LI>判断【DEMSY平台】是否支持多应用 {@link #isMultipleApp()}
 * </UL>
 * <p>
 * <b>可以从【DEMSY动作上下文环境】中：</b>
 * <UL>
 * <LI>获取动作环境属性 {@link #get(String)}
 * <LI>获取ActionContext环境对象 {@link #actionContext()}
 * <LI>获取动作链 {@link #actionInvoker()}
 * <LI>获取平台目标应用系统 {@link #getSoft()}
 * <LI>获取HTTP请求 {@link #request()}
 * <LI>获取HTTP响应 {@link #response()}
 * <LI>获取登录信息 {@link #login()}
 * <LI>获取用户帐号 {@link #username()}
 * <LI>获取用户密码 {@link #password()}
 * <LI>获取用户类型 {@link #usertype()}
 * <LI>获取合法用户实体 {@link #loginUser()}
 * <LI>设置动作环境属性 {@link #put(String, Object)}
 * 
 * </UL>
 * 
 */
@Encoding(input = "utf-8", output = "utf-8")
@Fail("json")
@SetupBy(DemsySetup.class)
@Filters({ // @By(type = ActionMappingFilter.class)
})
@AdaptBy(type = DemsyAdaptor.class)
@Views(value = { DemsyViewMaker.class })
@IocBy(type = DemsyIocProvider.class, args = { "" })
@LoadingBy(DemsyLoading.class)
@Modules({ SecurityActions.class//
		, BizActions.class//
		, UploadActions.class//
		, ConfigActions.class//
		, IndexActions.class//
		, UiActions.class//
		, OrderActions.class//
		, AppActions.class //
})
@UrlMappingBy("com.kmetop.demsy.mvc.nutz.DemsyUrlMappingImpl")
@ChainBy(args = { "com/kmetop/demsy/mvc/nutz/demsy-chains.js" })
@Chain(value = "demsy")
public abstract class Demsy implements Const, MvcConst {
	private static Log log = Logs.getLog(Demsy.class);

	// 【DEMSY平台】初始化参数
	private static Map<String, String> params = new HashMap();

	// 【DEMSY动作】持有者
	private static ThreadLocal<Demsy> me = new ThreadLocal();

	private static ThreadLocal<Monitor> monitor = new ThreadLocal();

	private static Map<IDataSource, IOrm> ormMap = new HashMap();

	// 【DEMSY平台】被部署在这个SERVLET环境中
	public static ServletContext servletContext;

	public static String contextPath = "";

	public static String contextDir = "";

	/**
	 * DEMSY平台配置
	 */
	public static IAppConfig appconfig = null;

	public static INamingStrategy namingStrategy;

	// 【DEMSY平台】中的IOC容器
	public static IIoc ioc;

	public static IBizSession bizSession;

	public static ISecurity security;

	public static IBizEngine bizEngine;

	public static IBizManagerFactory bizManagerFactory;

	public static IUiEngine uiEngine;

	public static IModuleEngine moduleEngine;

	public static IDataSource dataSource = null;

	// 【DEMSY平台】请求动作处理器
	public static DemsyActionHandler actionHandler;

	public static IPSeeker ipseeker;

	// 初始化标志： 标志【DEMSY平台】是否已经成功初始化？
	private static boolean inited = false;

	private static EnMappingHolder entityHolder;

	private static EnMappingMaker entityMaker;

	public static boolean upgradding = false;

	/**
	 * 初始化【DEMSY平台上下文环境】
	 * <p>
	 * 该方法在系统启动并初始化SERVLET时调用，用于初始化【DEMSY平台上下文环境】。
	 * 
	 * @param context
	 *            ServletContext 对象
	 */
	public static void init(ServletContext context) {
		// 初始化
		synchronized (Demsy.class) {
			if (inited) {
				if (log.isWarnEnabled()) {
					log.warn("不能重复初始化DEMSY平台!");
				}
				return;
			}

			log.info("初始化DEMSY平台...");

			servletContext = context;

			init();
		}

		// 日志信息
		if (log.isInfoEnabled()) {
			Iterator<String> keys = params.keySet().iterator();
			StringBuffer initParamLog = new StringBuffer();
			initParamLog.append("---------------------------------");
			while (keys.hasNext()) {
				String key = keys.next();
				Object val = params.get(key);
				initParamLog.append(key).append("=").append(val).append("\n");
			}
			initParamLog.append("---------------------------------");
			initParamLog.append("\npath.context=").append(appconfig.getContextPath());
			initParamLog.append("\ndir.context=").append(appconfig.getContextDir());
			initParamLog.append("\ndir.config=").append(appconfig.getConfigDir());
			initParamLog.append("\ndir.logs=").append(appconfig.getLogsDir());
			initParamLog.append("\ndir.temp=").append(appconfig.getTempDir());
			initParamLog.append("\ndir.WEB-INF=").append(appconfig.getWebInfoDir());
			initParamLog.append("\ndir.classes=").append(appconfig.getClassDir());

			log.infof("初始化DEMSY平台: 配置信息:\n%s%s", appconfig, initParamLog);
		}

		log.info("初始化DEMSY平台: 结束.");
	}

	// 用指定的配置目录初始化【DEMSY平台】
	private static void init() {
		Enumeration names = servletContext.getInitParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			params.put(name, servletContext.getInitParameter(name));
		}

		//
		String webappRootKey = params.get(WEB_APP_ROOT_KEY_PARAM);
		if (Str.isEmpty(webappRootKey)) {
			webappRootKey = DEFAULT_WEB_APP_ROOT_KEY;
		}

		// 计算环境相对路径
		contextPath = servletContext.getContextPath();

		// 计算环境真实路径
		contextDir = servletContext.getRealPath("/").replace("\\", "/");
		if (contextDir.endsWith("/"))
			contextDir = contextDir.substring(0, contextDir.length() - 1);
		else if (contextDir.endsWith("/."))
			contextDir = contextDir.substring(0, contextDir.length() - 2);
		System.setProperty(webappRootKey, contextDir);

		appconfig = new AppConfig(contextPath, contextDir, params.get(IAppConfig.PATH_CONFIG));

		// 计算日志存放路径并缓存在系统属性表中
		String logsDir = params.get(IAppConfig.PATH_LOGS);
		if (Str.isEmpty(logsDir)) {
			logsDir = appconfig.getLogsDir();
		}
		System.setProperty(IAppConfig.PATH_LOGS, logsDir);

		// 计算临时目录
		String tempDir = params.get(IAppConfig.PATH_TEMP);
		if (Str.isEmpty(tempDir)) {
			tempDir = appconfig.getTempDir();
		}

		// 安装事物
		Trans.setup(DemsyTransaction.class);

		// 加载日志配置
		new LogConfig();
		log = Logs.getLog(Demsy.class);

		namingStrategy = EncodeNamingStrategy.me;
		entityHolder = new EnMappingHolder();
		entityMaker = new EnMappingMaker();
		entityMaker.setHolder(entityHolder);
		entityMaker.setNamingStrategy(namingStrategy);
		try {
			ipseeker = new IPSeeker("QQWry.Dat", contextDir + File.separator + "WEB-INF" + File.separator + "lib");
		} catch (Throwable e) {
			log.error("加载IP地址转换包失败！");
		}

		// 加载IOC
		Assert.notNull(ioc = IocLoader.load());
		Assert.notNull(bizSession = bean("bizSession"));
		Assert.notNull(security = bean("security"));
		Assert.notNull(bizEngine = bean("bizEngine"));
		Assert.notNull(bizManagerFactory = bean("bizManagerFactory"));
		Assert.notNull(uiEngine = bean("uiEngine"));
		Assert.notNull(moduleEngine = bean("moduleEngine"));

		Assert.notNull(dataSource = bean("_dbConfig"));
		Assert.notNull(bean("_entityListeners"));
		Assert.notNull(bean("_nlsConfig"));

		initMVCUtil();

		Assert.notNull(actionHandler = new DemsyActionHandler(new DemsyNutConfig()));

		inited = true;
	}

	private static void initMVCUtil() {
		// 初始化模板引擎
		MvcUtil.tplEngineST = new SmartyTemplate();
		// MVCUtil.tplEngineFTL = new FreemarkerTempate();
		// MVCUtil.tplEngineVM = new VelocityTemplate();
		MvcUtil.tplEngineDefault = new ITemplateEngine() {
			@Override
			public void render(String template, Map context, Writer out) throws Exception {
				if (template.endsWith(TPL_ST))
					MvcUtil.tplEngineST.render(template, context, out);
				else if (template.endsWith(TPL_FTL))
					MvcUtil.tplEngineVM.render(template, context, out);
				else if (template.endsWith(TPL_VM))
					MvcUtil.tplEngineFTL.render(template, context, out);
				else
					MvcUtil.tplEngineST.render(template, context, out);
			}

			@Override
			public void renderExpression(String classPath, String expression, Map context, Writer out) throws Exception {
				MvcUtil.tplEngineST.renderExpression(classPath, expression, context, out);
			}

		};

		// 初始化UI呈现器
		MvcUtil.renderBizModel = new ViewRender(MvcUtil.tplEngineDefault);
		MvcUtil.renderBizData = new DataRender(MvcUtil.tplEngineDefault);
	}

	/**
	 * 初始化 【DEMSY动作上下文环境】
	 * 
	 * @param req
	 *            HTTP请求
	 * @param resp
	 *            HTTP响应
	 * @return 【DEMSY动作上下文环境】对象
	 * @throws SecurityException
	 */
	public static Demsy initMe(HttpServletRequest req, HttpServletResponse resp) throws SecurityException {
		me.remove();

		WebDemsy context = new WebDemsy(req, resp);

		me.set(context);

		return context;
	}

	/**
	 * 获取【DEMSY动作】
	 * 
	 * @return 【DEMSY动作】对象
	 */
	public static Demsy me() {
		return me.get();
	}

	/**
	 * 重置秒表
	 * 
	 * @return 返回新秒表对象
	 */
	public static Monitor initSw() {
		synchronized (monitor) {
			Monitor obj = Monitor.begin();
			monitor.set(obj);

			// log.debug("重置秒表");

			return obj;
		}
	}

	public static long swStart() {
		return monitor.get().from;
	}

	/**
	 * 获取秒表
	 * 
	 * @return 返回秒表对象
	 */
	public static Monitor monitor() {
		return monitor.get();
	}

	/**
	 * 获取【DEMSY平台】中的初始化参数名称
	 * 
	 * @return 参数名称列表
	 */
	public static List<String> initParamNames() {
		return Lists.iterator2list(params.keySet().iterator());
	}

	/**
	 * 获取【DEMSY平台】中的初始化参数
	 * 
	 * @param name
	 *            参数名称
	 * @return 参数值
	 */
	public static String initParam(String name) {
		return (String) params.get(name);
	}

	/**
	 * 获取【DEMSY平台】中的属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public static <T> T contextAttr(String name) {
		return (T) servletContext.getAttribute(name);
	}

	/**
	 * 设置【DEMSY平台】属性
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public static void contextAttr(String name, Object value) {
		servletContext.setAttribute(name, value);
	}

	public static <T> T bean(String beanName) {
		return (T) ioc.get(beanName);
	}

	/**
	 * 获取访问默认数据库的ORM
	 * <UL>
	 * <LI>支持从当前用户获取数据库配置：当前登录用户的数据库配置优先来自请求参数，其次来自正在使用的DEMSY应用；
	 * <LI>支持获取默认数据库配置：如果当前当前用户没有指定数据库配置，则获取数据库默认配置；
	 * </UL>
	 * 
	 * @return
	 */
	public static IOrm orm() {
		WebDemsy ctx = (WebDemsy) Demsy.me();
		IDataSource db;

		if (ctx == null || ctx.getDataSource() == null)
			db = dataSource;
		else
			db = ctx.getDataSource();

		return orm(db);
	}

	/**
	 * 获取访问特定数据库的ORM
	 * 
	 * @param db
	 *            数据库配置
	 * @return
	 */
	public static IOrm orm(IDataSource db) {
		synchronized (ormMap) {
			IOrm orm = ormMap.get(db);
			if (orm == null) {
				EntityListeners ls = bean("_entityListeners");
				orm = new OrmImpl(db, entityHolder, entityMaker, ls);
				ormMap.put(db, orm);
			}

			return orm;
		}
	}

	/**
	 * 获取用于操作特定数据库的ORM
	 * 
	 * @param url
	 *            连接数据库的URL
	 * @param driver
	 *            数据库JDBC驱动
	 * @param user
	 *            数据库登录帐号
	 * @param pwd
	 *            数据库登录密码
	 * @return
	 */
	public static IOrm orm(String url, String driver, String database, String user, String pwd) {
		return orm(new DbConfig(url, driver, user, pwd));
	}

	/**
	 * 获取用于操作特定数据库的ORM
	 * 
	 * @param dbCfgName
	 *            数据库配置：IOC配置名称
	 * @return
	 */
	public static IOrm orm(String dbCfgName) {
		return orm((IDataSource) bean(dbCfgName));
	}

	public static void close() {
		if (actionHandler != null) {
			actionHandler.depose();
		}
	}

	public abstract boolean existToken(String token);

	public abstract String addToken();

	public abstract void removeToken(String token);

	/**
	 * 获取【DEMSY动作】中的 HTTP请求。
	 * 
	 * @return
	 */
	public abstract HttpServletRequest request();

	/**
	 * 获取【DEMSY动作】中的HTTP响应。
	 * 
	 * @return
	 */
	public abstract HttpServletResponse response();

	/**
	 * 获取【DEMSY动作上下文环境】参数
	 * 
	 * @param parameterName
	 *            参数名
	 * @return 参数值
	 */
	public abstract <T> T param(String parameterName, Class<T> classOfParameter, T defaultValue);

	/**
	 * 设置【DEMSY动作上下文环境】属性
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 * @return 【DEMSY动作上下文环境】对象
	 */
	public abstract Demsy set(String name, Object value);

	/**
	 * 获取【DEMSY动作上下文环境】属性
	 * 
	 * @param attributeName
	 *            属性名
	 * @return 属性值
	 */
	public abstract <T> T get(String attributeName);

	/**
	 * 获取登录信息。
	 * <p>
	 * 返回空值——表示当前客户端用户尚未登录。
	 * 
	 * @return 登录信息
	 */
	public abstract ILogin login();

	/**
	 * 获取【DEMSY动作】中的“合法”用户实体。
	 * <p>
	 * <b>即用户实体经过如下验证：</b>
	 * <UL>
	 * <LI>帐号、密码匹配；
	 * <LI>未被禁用；
	 * <LI>未被锁定；
	 * <LI>未过期；
	 * </UL>
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <li>“参数用户帐号”存在：
	 * <UL>
	 * <LI>验证成功：获取应用系统中“类型、帐号、密码”与参数用户信息匹配的用户实体；
	 * <LI>验证失败：抛出安全异常；
	 * </UL>
	 * <li>“参数用户帐号”不存在：
	 * <UL>
	 * <LI>已登录：
	 * <UL>
	 * <LI>登录信息中的用户实体未过期：则获取登录信息中的用户实体
	 * {@link com.kmetop.demsy.security.ILogin#getUser()}；
	 * <li>登录信息中的用户实体已过期：返回空值；
	 * </UL>
	 * <li>未登录：返回空值；
	 * </UL>
	 * </UL>
	 * 
	 * @return
	 * @throws SecurityException
	 */
	public abstract IUser loginUser() throws SecurityException;

	/**
	 * 获取(<i>【DEMSY平台】多应用环境下</i>)【DEMSY动作】中的应用系统，即正在访问的应用系统。
	 * <p>
	 * <b>应用系统解析说明：</b>【DEMSY动作】中的应用系统通过URI地址解析而来。
	 * <UL>
	 * <LI>【DEMSY平台】单应用环境：永远返回空值；
	 * <LI>URL第一个节点“是”应用系统编号：则按编号查找应用系统；
	 * <UL>
	 * <LI>查找成功：则转到——应用系统存在
	 * <LI>查找失败：则转到——URL第一个节点“不是”应用系统编号
	 * </UL>
	 * <LI>URL第一个节点“不是”应用系统编号：
	 * <UL>
	 * <LI>URI中域名存在：则按域名查找应用系统；
	 * <UL>
	 * <LI>查找成功：则转到——应用系统存在
	 * <LI>查找失败：则转到——应用系统不存在
	 * </UL>
	 * <LI>URI中域名不存在：则查找默认应用系统；
	 * <UL>
	 * <LI>查找成功：则转到——应用系统存在
	 * <LI>查找失败：则转到——应用系统不存在
	 * </UL>
	 * </UL>
	 * <LI>应用系统不存在：
	 * <UL>
	 * <LI>超级用户：可以访问【DEMSY平台】功能模块；
	 * <LI>普通用户：初始化【DEMSY动作】时将抛出非法URL安全异常；
	 * </UL>
	 * <LI>应用系统存在：
	 * <UL>
	 * <LI>应用系统实体对象中限定的域名存在：
	 * <UL>
	 * <LI>且URI中域名不存在：解析成功；
	 * <LI>且与URI中的域名一致：解析成功；
	 * <LI>且与URI中的域名不一致：解析失败，初始化【DEMSY动作】时将抛出非法URL安全异常；
	 * </UL>
	 * <LI>应用系统实体对象中域名不存在：解析成功；
	 * </UL>
	 * </UL>
	 * 
	 * @return
	 */
	public abstract IDemsySoft getSoft();

	/**
	 * 获取【DEMSY动作】中的 {@link org.nutz.mvc.ActionContext} 对象
	 * 
	 * @return
	 */
	public abstract ActionContext actionContext();

	/**
	 * 获取【DEMSY动作】中的 {@link org.nutz.mvc.ActionChain} 对象
	 * 
	 * @return
	 */
	public abstract ActionInvoker actionInvoker();

	/**
	 * 释放【DEMSY动作上下文环境】
	 */
	public abstract void release();

	public abstract String getDomain();

	public abstract int getPort();

	public abstract boolean isLocal();

	private static class WebDemsy extends Demsy {
		private HttpServletRequest request;

		private HttpServletResponse response;

		private IDemsySoft soft;

		private ActionContext actionContext;

		private ActionInvoker actionInvoker;

		private String url;

		private Map<String, Object> props;

		private static Pattern configPath = Pattern.compile("^/(config|login|logout)/*", Pattern.CASE_INSENSITIVE);

		//
		// private static Pattern iglorePath =
		// Pattern.compile("^/(index|index.html|ul|ui)/*",
		// Pattern.CASE_INSENSITIVE);

		private WebDemsy(HttpServletRequest req, HttpServletResponse resp) throws SecurityException {
			if (upgradding) {
				throw new DemsyException("正在升级系统......请稍候再试!");
			}
			props = new HashMap();
			request = req;
			response = resp;
			url = Mvcs.getRequestPath(req);

			initInvoker();

			IUser user = this.loginUser();
			if (user instanceof IAdminUser) {
				IAdminUser admin = (IAdminUser) user;
				if (url.startsWith(URL_BZMAIN.replace("/*", "/"))) {
					admin.setLatestUrl(url);
					Demsy.orm().save(admin, Expr.fieldRexpr("latestUrl$", false));
				}
			}
		}

		private void initInvoker() throws SecurityException {
			actionContext = new ActionContext().setRequest(request).setResponse(response).setServletContext(servletContext);
			DemsyUrlMappingImpl urlMapping = (DemsyUrlMappingImpl) actionHandler.getMapping();

			// String domain = request.getServerName();
			// if (Str.isIP(domain)) {
			String domain = "";
			// }

			// 试图将URI的第一个节点作为应用编号
			String appcode = "";
			if (!Str.isEmpty(url)) {
				actionInvoker = urlMapping.get(actionContext, url);
				if (actionInvoker == null) {
					String url1 = url;
					int appIdxFrom = 0;
					if (url.startsWith("/") || url.startsWith("\\")) {
						url1 = url.substring(1);
						appIdxFrom = 1;
					}
					int idx = url1.indexOf("/");
					if (idx > -1) {
						url1 = url1.substring(idx);
						actionInvoker = urlMapping.get(actionContext, url1);
						if (actionInvoker != null) {// 路径的第一组斜杠(/.../)之间的部分为应用编号
							appcode = url.substring(appIdxFrom, idx + appIdxFrom);
						}
					}
				}
			}

			// 路径的第一个节点不是应用编号
			try {
				soft = moduleEngine.getSoft(domain);

				if (soft == null) {
					throw new ConfigException("未知应用系统!");
				}

				if (!Str.isEmpty(domain) && !Str.isEmpty(soft.getDomain()) && !domain.equals(soft.getDomain())) {
					throw new SecurityException(404);
				}

				if (!Str.isEmpty(appcode) && !Str.isEmpty(soft.getCode()) && !appcode.equals(soft.getCode())) {
					throw new SecurityException(404);
				}
			} catch (ConfigException e) {
				if (!configPath.matcher(url).find())
					throw e;
			} catch (Throwable e) {
				log.debug(e);
			}
		}

		public String addToken() {
			HttpSession session = request.getSession();
			String token = session.getId() + "_" + Long.toHexString(System.currentTimeMillis());
			token = token.toUpperCase();

			List<String> tokens = (List<String>) session.getAttribute("tokens");
			if (tokens == null) {
				tokens = new LinkedList();
				session.setAttribute("tokens", tokens);
			}
			synchronized (tokens) {
				// 控制操作令牌的唯一性
				// tokens.clear();

				if (!tokens.contains(token)) {
					tokens.add(token);
				}
			}

			return token;
		}

		public void removeToken(String token) {
			HttpSession session = request.getSession();
			List<String> tokens = (List<String>) session.getAttribute("tokens");
			if (tokens != null) {
				synchronized (tokens) {
					tokens.remove(token);
				}
			}
		}

		public boolean existToken(String token) {
			if (Str.isEmpty(token))
				try {
					security.checkLogin(IUserRole.ROLE_ADMIN_ROOT);
					return true;
				} catch (Throwable e) {
				}

			HttpSession session = request.getSession();
			List<String> tokens = (List<String>) session.getAttribute("tokens");
			if (tokens != null) {
				synchronized (tokens) {
					return tokens.contains(token);
				}
			}

			return false;
		}

		@Override
		public HttpServletRequest request() {
			return request;
		}

		@Override
		public HttpServletResponse response() {
			return response;
		}

		@Override
		public ILogin login() {
			return security.login(request, soft);
		}

		@Override
		public IDemsySoft getSoft() {
			return soft;
		}

		public IUser loginUser() throws SecurityException {
			ILogin login = this.login();
			if (login != null)
				return login.getUser();

			return null;
		}

		public String username() {
			if (loginUser() != null)
				return loginUser().getUsername();

			return "";
		}

		@Override
		public <T> T param(String key, Class<T> cls, T defaultValue) {
			if (cls.isAssignableFrom(String[].class)) {
				return (T) request.getParameterValues(key);
			}
			String v = request.getParameter(key);
			if (Str.isEmpty(v)) {
				return defaultValue;
			}
			return (T) Castors.me().castTo(v, cls);
		}

		@Override
		public Demsy set(String name, Object value) {
			props.put(name, value);
			return this;
		}

		@Override
		public <T> T get(String name) {
			return (T) props.get(name);
		}

		@Override
		public ActionContext actionContext() {
			return actionContext;
		}

		@Override
		public ActionInvoker actionInvoker() {
			return actionInvoker;
		}

		@Override
		public void release() {
			request = null;
			response = null;
			me.set(null);
		}

		IDataSource getDataSource() {
			if (soft == null)
				return null;

			return soft.getDataSource();
		}

		@Override
		public String getDomain() {
			return request.getServerName();
		}

		public int getPort() {
			return request.getServerPort();
		}

		public boolean isLocal() {
			String domain = this.getDomain();
			return domain.equals("localhost") || domain.equals("127.0.0.1");
		}
	}

	public static class Monitor {
		private int count = 0;

		// 秒表计时
		private long from;

		private long prev;

		private Monitor() {
			from = System.currentTimeMillis();
			prev = from;
		}

		private static Monitor begin() {
			return new Monitor();
		}

		public long getElepse() {
			return System.currentTimeMillis() - prev;
		}

		public long getMemElepse() {
			return (Runtime.getRuntime().totalMemory() / 1000000) - Runtime.getRuntime().freeMemory() / 1000000;
		}

		@Override
		public String toString() {
			StringBuffer ret = new StringBuffer(100);
			ret.append(new DecimalFormat("000").format(++count)).append(":  ");

			ret.append(getElepse());
			ret.append(" / ");
			ret.append(System.currentTimeMillis() - from);

			long total = Runtime.getRuntime().totalMemory() / 1000000;
			ret.append(",   ");
			ret.append(getMemElepse());
			ret.append(" / ");
			ret.append(total);

			prev = System.currentTimeMillis();

			return ret.toString();
		}
	}

	public abstract String username();
}

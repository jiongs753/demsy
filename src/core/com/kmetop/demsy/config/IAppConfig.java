package com.kmetop.demsy.config;

/**
 * DEMSY平台配置
 * 
 * @author yongshan.ji
 * 
 */
public interface IAppConfig extends IConfig {
	/**
	 * 平台支持的上传文件后缀
	 * <p>
	 * 如：upload.filter= 7z|aiff|asf|avi|bmp|csv|doc|fla|flv|gif|gz|gzip|jpeg|jpg
	 * |mid|mov|mp3|mp4|mpc
	 * |mpeg|mpg|ods|odt|pdf|png|ppt|pxd|qt|ram|rar|rm|rmi|rmvb
	 * |rtf|sdc|sitd|swf|
	 * sxc|sxw|tar|tgz|tif|tiff|txt|vsd|wav|wma|wmv|xls|xml|zip
	 */
	public static final String UPLOAD_FILTER = "upload.filter";

	/**
	 * 数据库配置文件 配置项KEY
	 */
	static final String CONFIG_DB = "config.db";

	/**
	 * 超级用户配置文件 配置项KEY
	 */
	static final String CONFIG_USER = "config.rootuser";

	/**
	 * NSL配置文件 配置项KEY
	 */
	static final String CONFIG_NLS = "config.nls";

	/**
	 * IOC配置文件 配置项KEY
	 */
	static final String CONFIG_IOC = "config.ioc";

	/**
	 * 定时任务KEY前缀
	 */
	static final String SCHEDULE_PREFIX = "schedule.";

	/**
	 * 插件配置项KEY前缀，后缀为插件接口类简短名称。如：BBS插件配置项KEY——plugin.IBbsPlugin
	 */
	static final String PLUGIN_PREFIX = "plugin.";

	static final String DOMAIN_IMAGE = "domain.image";

	static final String DOMAIN_SCRIPT = "domain.script";

	static final String DOMAIN_CSS = "domain.css";

	/**
	 * 【DEMSY平台】上下文环境中的配置路径 配置项KEY
	 */
	static final String PATH_CONFIG = "path.config";

	/**
	 * 文件上传目录
	 * <p>
	 * 如： upload.folder=/upload
	 */
	public static final String PATH_UPLOAD = "path.upload";

	/**
	 * 日志文件存放路径 配置项KEY
	 */
	static final String PATH_LOGS = "path.logs";

	/**
	 * 临时路径 配置项KEY
	 */
	static final String PATH_TEMP = "path.temp";

	static final String PATH_THEME = "path.theme";

	static final String PKG_TPL = "pkg.templates";

	static final String MODE_PRODUCT = "mode.product";

	public IAppConfig copy();

	String getDefaultCorpName();

	String getDefaultCorpCode();

	/**
	 * 获取【DEMSY平台】名称
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>配置项KEY：{@link #DEMSY_NAME}
	 * <LI>默认返回："DEMSY.V2.x.a-snapshot-" + 当前时间(yyyyMMddHHmmss)
	 * <LI>如：{@link #DEMSY_NAME}=AERP-2.0
	 * </UL>
	 * 
	 * @return 版本名称
	 */
	String getDefaultSoftName();

	String getDefaultSoftCode();

	/**
	 * <B>获取数据库默认配置文件：</b>返回类路径下的文件全名。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>默认后缀：.properties
	 * <LI>配置项KEY：{@link #CONFIG_DB}
	 * <LI>默认返回：com.kmetop.demsy.config.db.properties
	 * <LI>加载配置文件：系统首先加载默认配置文件；然后继续加载配置目录 {@link #getConfigClassDir()} 或
	 * {@link #getConfigDir()} 下的同名的配置文件。 在配置目录下查找配置文件时使用的文件名不带任何文件夹部分。
	 * <LI>如： {@link #CONFIG_DB}=com.kmetop.demsy.config.db.properties 或
	 * {@link #CONFIG_DB} =com.kmetop.demsy.config.db
	 * </UL>
	 * 
	 * @return
	 */
	String getDBConfig();

	/**
	 * <b>获取超级用户默认配置文件：</b>返回类路径下的文件全名。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>默认后缀：.properties
	 * <LI>配置项KEY：{@link #CONFIG_USER}
	 * <LI>默认返回：com.kmetop.demsy.config.rootuser.properties
	 * <LI>加载配置文件：系统首先加载默认配置文件；然后继续加载配置目录 {@link #getConfigClassDir()} 或
	 * {@link #getConfigDir()} 下的同名的配置文件。 在配置目录下查找配置文件时使用的文件名不带任何文件夹部分。
	 * <LI>如：{@link #CONFIG_USER} =com.kmetop.demsy.config.rootuser.properties 或
	 * {@link #CONFIG_USER} =com.kmetop.demsy.config.rootuser
	 * </UL>
	 * 
	 * @return
	 */
	String getUserConfig();

	/**
	 * <b>获取默认IOC配置文件：</b>返回类路径下的文件全名。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>默认后缀：.js 或 .json
	 * <LI>配置项KEY：{@link #CONFIG_IOC}
	 * <LI>默认返回：com.kmetop.demsy.config.ioc.js
	 * <LI>加载配置文件：系统首先加载默认配置文件；然后继续加载配置目录 {@link #getConfigClassDir()} 或
	 * {@link #getConfigDir()} 下的同名的配置文件。 在配置目录下查找配置文件时使用的文件名不带任何文件夹部分。
	 * <LI>如：{@link #CONFIG_IOC}=com.kmetop.demsy.config.ioc.js 或
	 * {@link #CONFIG_IOC}=com.kmetop.demsy.config.ioc.json 或
	 * {@link #CONFIG_IOC}=com.kmetop.demsy.config.ioc
	 * </UL>
	 * 
	 * @return
	 */
	String getIOCConfig();

	/**
	 * <b>获取默认NLS国际化语言配置文件：</b>返回类路径下的文件全名。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>配置项KEY： {@link #CONFIG_NLS}
	 * <LI>默认后缀： .properties
	 * <LI>默认返回：com.kmetop.demsy.config.nls.properties
	 * <LI>加载配置文件：系统首先加载默认配置文件；然后继续加载配置目录 {@link #getConfigClassDir()} 或
	 * {@link #getConfigDir()} 下的同名的配置文件。 在配置目录下查找配置文件时使用的文件名不带任何文件夹部分。
	 * <LI>如： {@link #CONFIG_NLS}=com.kmetop.demsy.config.nls.properties 或
	 * {@link #CONFIG_NLS}=com.kmetop.demsy.config.nls
	 * </UL>
	 * 
	 * @return
	 */
	String getNLSConfig();

	String getContextPath();

	/**
	 * <B>获取运行时配置路径：</B>获取运行时配置文件存放的路径，该路径是一个文件夹而不是一个具体的文件。
	 * <p>
	 * <B>说明：</B>
	 * <UL>
	 * <LI>相对路径以'/'开头
	 * <LI>如果初始化【DEMSY平台】上下文环境时指定了配置路径，则返回该配置路径；否则将获取配置项{@link #PATH_CONFIG}
	 * 中指定的路径；如果配置文件中没有{@link #PATH_CONFIG}配置项，则返回默认值。
	 * <LI>相对路径可以是SERVLET环境路径的子目录，其真实路径通过 {@link #getConfigDir()} 获取。
	 * <LI>相对路径也可以是SERVELT环境"/WEB-INF/classes"下的子目录，其真实路径通过
	 * {@link #getConfigClassDir()} 获取。
	 * <LI>配置项KEY: {@link #PATH_CONFIG}
	 * <LI>默认返回：/config
	 * <LI>
	 * 如：{@link #PATH_CONFIG}=/config
	 * 则表示系统可以从SERVLET环境的/config目录或/WEB-INF/classes/config下加载相关配置文件。
	 * </UL>
	 * 
	 * @return
	 */
	String getConfigPath();

	/**
	 * 获取日志文件存放路径，该路径是SERVLET环境路径的子目录。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>配置项KEY：{@link #PATH_LOGS}
	 * <LI>默认返回：/logs
	 * <LI>配置项实例: {@link #PATH_LOGS}=/logs
	 * </UL>
	 * 
	 * @return
	 */
	String getLogsPath();

	/**
	 * 获取日志文件存放路径，该路径是SERVLET环境路径的子目录。
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>配置项KEY：{@link #PATH_TEMP}
	 * <LI>默认返回：/WEB-INF/tmp
	 * <LI>配置项实例: {@link #PATH_TEMP}=/WEB-INF/tmp
	 * </UL>
	 * 
	 * @return
	 */
	String getTempPath();

	String getImgDomainPath();

	String getScriptDomainPath();

	String getCssDomainPath();

	public String getThemePath();

	public String getTplPackage();

	/*
	 * 运行时配置
	 */

	String getContextDir();

	/**
	 * <b>获取环境配置路径：</b>即配置文件存放在环境路径下，该方法返回配置文件存放的真实路径
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>环境配置路径 = SERVLET环境真实路径 + 配置路径 {@link #getConfigPath()} 。
	 * <LI>环境路径下的配置文件可能会被客户端直接查看，因此除了需要兼容遗留系统外，一般不将配置文件存放在环境路径下。
	 * <LI>如：{@link #PATH_CONFIG}=/WEB-INF/config
	 * 【DEMSY平台】程序安装在D:/demsy/webapp目录下，则该方法将返回: D:/demsy/webapp/WEB-INF/config
	 * </UL>
	 * 
	 * @return
	 */
	String getConfigDir();

	// /**
	// * <B>获取类配置路径：</B>即配置目录位于/WEB-INF/classes下，该方法返回配置文件存放的真实路径。
	// * <p>
	// * <B>说明：</B>
	// * <UL>
	// * <LI>类配置路径 = SERVLET环境真实路径 + /WEB-INF/classes + 配置路径
	// * {@link #getConfigPath()} 。
	// * <LI>为了避免配置文件可能会被客户端直接查看，因此【DEMSY平台】将配置文件作为程序的一部分存放在类目录下。
	// * <LI>如：{@link #PATH_CONFIG}=/config
	// * 【DEMSY平台】程序安装在D:/demsy/webapp目录下，则该方法将返回:
	// * D:/demsy/webapp/WEB-INF/classes/config
	// * </UL>
	// *
	// * @return
	// */
	// String getConfigClassDir();

	/**
	 * <b>获取环境日志路径：</b>即日志文件存放在环境路径下，该方法返回日志文件存放的真实路径
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>环境日志路径 = SERVLET环境真实路径 + 日志路径 {@link #getCtxpathLogs()} 。
	 * <LI>
	 * 环境路径下的日志文件可能会被客户端直接查看，因此除了需要兼容遗留系统外，一般不将日志文件直接存放在环境路径下，建议在日志路径配置中加上前缀/
	 * WEB-INF
	 * <LI>如：{@link #PATH_LOGS}=/WEB-INF/logs
	 * 【DEMSY平台】程序安装在D:/demsy/webapp目录下，则该方法将返回: D:/demsy/webapp/WEB-INF/logs
	 * </UL>
	 * 
	 * @return
	 */
	String getLogsDir();

	/**
	 * <b>获取环境日志路径：</b>即日志文件存放在环境路径下，该方法返回日志文件存放的真实路径
	 * <p>
	 * <b>说明：</b>
	 * <UL>
	 * <LI>环境日志路径 = SERVLET环境真实路径 + 日志路径 {@link #getTempPath()} 。
	 * <LI>
	 * 环境路径下的日志文件可能会被客户端直接查看，因此除了需要兼容遗留系统外，一般不将日志文件直接存放在环境路径下，建议在日志路径配置中加上前缀/
	 * WEB-INF
	 * <LI>如：{@link #PATH_LOGS}=/WEB-INF/tmp
	 * 【DEMSY平台】程序安装在D:/demsy/webapp目录下，则该方法将返回: D:/demsy/webapp/WEB-INF/tmp
	 * </UL>
	 * 
	 * @return
	 */
	String getTempDir();

	String getWebInfoDir();

	String getClassDir();

	boolean isProductMode();

	String getUploadPath();

	String getCustomFieldPkg();

	String getConfigPkg();

	/**
	 * 保密模式：客户系统在某些情况下会用于给其他客户演示，为了对客户敏感数据进行保密。通过该方法判断是否需要在后台对客户敏感数据进行保密显示。
	 * 
	 * @return
	 */
	boolean isPrivacyMode();

}

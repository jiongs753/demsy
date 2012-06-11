package com.kmetop.demsy.config.impl;

import java.io.File;

import com.kmetop.demsy.config.IAppConfig;
import com.kmetop.demsy.lang.Str;

public class AppConfig extends BaseConfig implements IAppConfig {
	private static final String CONFIG_FILE = IAppConfig.class.getPackage().getName() + ".app";

	private String configPath;

	private String contextPath;

	private String contextDir;

	private void initDefault() {
		properties.put(CONFIG_DB, "com.kmetop.demsy.config.db.properties");
		properties.put(CONFIG_NLS, "com.kmetop.demsy.config.i18n.properties");
		properties.put(CONFIG_USER, "com.kmetop.demsy.config.rootuser.properties");
		properties.put(CONFIG_IOC, "com.kmetop.demsy.config.ioc.js");
		properties.put(MODE_PRODUCT, "false");

		properties.put(PATH_CONFIG, "/WEB-INF/config");
		properties.put(PATH_LOGS, "/WEB-INF/logs");
		properties.put(PATH_TEMP, "/WEB-INF/tmp");
		properties.put(PATH_THEME, "/themes2/defaults");
		properties.put(PATH_UPLOAD, "/upload");
		properties.put(PKG_TPL, "templates.defaults");
		properties.put("customFieldPkg", "com.kmetop.demsy.comlib.biz.field.");
		properties.put("configPkg", "com.kmetop.demsy.config.");

		properties.put(DOMAIN_IMAGE, "");
		properties.put(DOMAIN_SCRIPT, "");
		properties.put(DOMAIN_CSS, "");

		properties.put("demsy.corpname", "昆明易极信息技术有限公司");
		properties.put("demsy.corpcode", "www.kmetop.com");
		properties.put("demsy.softname", "企业管理系统自定义平台");
		properties.put("demsy.softcode", "www.demsy.cn");

		// 内置路径配置
		properties.put("imagepath.actionlib", "/themes2/images/actionlib");
		properties.put("imagepath.uimodellib", "/themes2/images/uimodellib");
	}

	private AppConfig() {

	}

	public AppConfig(String contextPath, String contextDir, String configPath) {
		this.initDefault();

		this.contextPath = contextPath;
		this.contextDir = contextDir;
		if (configPath == null) {
			if (Str.isEmpty(configPath)) {
				configPath = this.get(PATH_CONFIG);
			}
			configPath = configPath.replace("\\", "/").replace(".", "/");
			if (!configPath.startsWith("/")) {
				configPath = "/" + configPath;
			}
		}
		this.configPath = configPath;

		initPath(CONFIG_FILE);
		init();
	}

	@Override
	protected void init() {
		if (log.isDebugEnabled()) {
			File file = this.getExtConfigFile();
			log.debugf("加载 APP 参数配置......[%s, %s]", this.configFile, file.exists() ? file.getAbsolutePath() : "");
		}
		super.init();
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(toText());
			log.debugf("参数配置：%s", sb);
		}
	}

	@Override
	public File getExtConfigFile() {
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}
		return new File(getConfigDir() + "/" + fileName);
	}

	@Override
	public String getConfigPath() {
		return configPath;
	}

	@Override
	public String getConfigDir() {
		return contextDir + getConfigPath();
	}

	@Override
	public String getDefaultCorpName() {
		return this.get("demsy.corpname");
	}

	@Override
	public String getDefaultCorpCode() {
		return this.get("demsy.corpcode");
	}

	@Override
	public String getDefaultSoftName() {
		return this.get("demsy.softname");
	}

	@Override
	public String getDefaultSoftCode() {
		return this.get("demsy.softcode");
	}

	@Override
	public String getDBConfig() {
		return this.get(CONFIG_DB);
	}

	@Override
	public String getNLSConfig() {
		return this.get(CONFIG_NLS);
	}

	@Override
	public String getIOCConfig() {
		return this.get(CONFIG_IOC);
	}

	public String getLogsPath() {
		return this.get(PATH_LOGS);
	}

	@Override
	public String getLogsDir() {
		return contextDir + getLogsPath();
	}

	@Override
	public String getTempPath() {
		return this.get(PATH_TEMP);
	}

	@Override
	public String getTempDir() {
		return contextDir + getTempPath();
	}

	@Override
	public String getWebInfoDir() {
		return contextDir + "/WEB-INF";
	}

	@Override
	public String getClassDir() {
		return contextDir + "/WEB-INF/classes";
	}

	@Override
	public String getUserConfig() {
		return this.get(CONFIG_USER);
	}

	@Override
	public String getImgDomainPath() {
		return this.get(DOMAIN_IMAGE);
	}

	@Override
	public String getScriptDomainPath() {
		return this.get(DOMAIN_SCRIPT);
	}

	@Override
	public String getCssDomainPath() {
		return this.get(DOMAIN_CSS);
	}

	@Override
	public String getThemePath() {
		return this.get(PATH_THEME);
	}

	@Override
	public String getContextDir() {
		return contextDir;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public String getTplPackage() {
		return this.get(PKG_TPL);
	}

	protected void copyTo(AppConfig config) {
		config.configPath = this.configPath;
		config.contextDir = this.contextDir;
		config.contextPath = this.contextPath;
		super.copyTo(config);
	}

	@Override
	public IAppConfig copy() {
		AppConfig ret = new AppConfig();
		this.copyTo(ret);

		return ret;
	}

	@Override
	public boolean isProductMode() {
		return this.getBoolean(MODE_PRODUCT);
	}

	@Override
	public String getUploadPath() {
		return this.get(PATH_UPLOAD);
	}

	@Override
	public String getCustomFieldPkg() {
		return get("customFieldPkg");
	}

	@Override
	public String getConfigPkg() {
		return get("configPkg");
	}

	@Override
	public boolean isPrivacyMode() {
		return getBoolean("security.privacyMode");
	}
}

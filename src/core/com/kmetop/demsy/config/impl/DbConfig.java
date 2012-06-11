package com.kmetop.demsy.config.impl;

import static com.kmetop.demsy.Demsy.appconfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.nutz.lang.Strings;

import com.kmetop.demsy.config.IConfig;
import com.kmetop.demsy.config.IDataSource;

public class DbConfig extends BaseConfig implements IDataSource, IConfig {

	private static Map<Integer, String> drivers = new HashMap();

	private static Map<Integer, String> urls = new HashMap();

	static {
		drivers.put(DB_TYPE_MSSQL, "net.sourceforge.jtds.jdbc.Driver");
		urls.put(DB_TYPE_MSSQL, "jdbc:jtds:sqlserver://localhost:1433/");
	}

	public DbConfig() {
		super(appconfig.getDBConfig());
	}

	public DbConfig(String cfg) {
		super(cfg);
	}

	public DbConfig(int type, String database, String user, String pwd) {
		properties.put(TYPE, type);
		properties.put(DATABASE, database);
		properties.put(USER, user);
		properties.put(PWD, pwd);
	}

	public DbConfig(String url, String driver, String user, String pwd) {
		properties.put(URL, url);
		properties.put(DRIVER, driver);
		properties.put(USER, user);
		properties.put(PWD, pwd);
	}

	private int getType() {
		return getInt(TYPE, 0);
	}

	@Override
	public String getUrl() {
		int type = getType();
		switch (type) {
		case DB_TYPE_MSSQL:
			if (!Strings.isEmpty(getDatabase()))
				properties.put(URL, urls.get(DB_TYPE_MSSQL) + getDatabase().trim());
			break;
		}

		return get(URL, "");
	}

	@Override
	public String getDriver() {
		int type = getType();
		switch (type) {
		case DB_TYPE_MSSQL:
			properties.put(DRIVER, drivers.get(DB_TYPE_MSSQL));
			break;
		default:
		}

		return get(DRIVER, "");
	}

	private String getDatabase() {
		return get(DATABASE, "");
	}

	@Override
	public String getPwd() {
		return get(PWD, "");
	}

	@Override
	public String getUser() {
		return get(USER, "");
	}

	protected void init() {
		if (log.isInfoEnabled()) {
			File file = this.getExtConfigFile();
			log.infof("加载数据库配置......[%s, %s]", this.configFile, file.exists() ? file.getAbsolutePath() : "");
		}

		super.init();

		if (log.isInfoEnabled()) {
			log.infof("加载数据库配置: 信息[\n%s]", toText());

			log.info("加载数据库配置: 结束.");
		}
	}

	protected void copyTo(DbConfig config) {
		super.copyTo(config);
	}

	@Override
	public IConfig copy() {
		DbConfig ret = new DbConfig();
		this.copyTo(ret);

		return ret;
	}

	@Override
	public int hashCode() {
		return 37 * 17 + getUrl().hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (!getClass().equals(that.getClass())) {
			return false;
		}
		DbConfig thatEntity = (DbConfig) that;
		if ((this == that)) {
			return true;
		}
		String url = getUrl();
		if (Strings.isEmpty(url)) {
			return false;
		}
		String url2 = thatEntity.getUrl();
		if (thatEntity == null || Strings.isEmpty(url2)) {
			return false;
		}
		return url.equals(url2);
	}

	@Override
	public String toString() {
		return new StringBuffer().append("URL=").append(this.getUrl()).append(", DRIVER=").append(this.getDriver()).append(", USER=").append(getUser()).append(", PWD=").append(getPwd()).toString();
	}

}

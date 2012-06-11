package com.kmetop.demsy.config;

import java.util.Properties;

public interface IDataSource {
	public static String URL = "url";

	public static String DRIVER = "driver";

	public static String USER = "user";

	public static String PWD = "password";

	public static String TYPE = "type";

	public static String DATABASE = "database";

	public static final int DB_TYPE_MSSQL = 1;

	public String getUrl();

	public String getDriver();

	public String getUser();

	public String getPwd();

	public Properties getProperties();
}

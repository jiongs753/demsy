package com.kmetop.demsy.config;

import java.io.IOException;
import java.util.Properties;

public interface IConfig {

	public String get(String key);

	public String get(String key, String defaultValue);

	public int getInt(String key);

	public int getInt(String key, int defaultValue);

	public boolean getBoolean(String key);

	public boolean getBoolean(String key, boolean defaultValue);

	public Properties getProperties();

	public void save() throws IOException;

	public IConfig copy();
}

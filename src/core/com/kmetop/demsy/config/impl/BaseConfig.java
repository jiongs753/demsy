package com.kmetop.demsy.config.impl;

import static com.kmetop.demsy.Demsy.appconfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.nutz.lang.stream.StringInputStream;

import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.config.IConfig;
import com.kmetop.demsy.lang.Files;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public abstract class BaseConfig implements IConfig, IDynamic {
	protected Log log = Logs.getLog(this.getClass());

	public static final String FILE_EXT = ".properties";

	protected Properties properties = new Properties();

	protected String configFile;

	protected BaseConfig() {
	}

	protected BaseConfig(String path) {
		initPath(path);
		init();
	}

	protected void copyTo(BaseConfig config) {
		config.configFile = configFile;
		config.properties = (Properties) properties.clone();
	}

	protected void initPath(String path) {
		configFile = path;
		if (configFile.endsWith(FILE_EXT)) {
			configFile = configFile.substring(0, configFile.length() - FILE_EXT.length());
		}
		configFile = configFile.replace("\\", "/").replace(".", "/") + FILE_EXT;
	}

	@Override
	public String get(String key) {
		return properties.getProperty(key);
	}

	@Override
	public String get(String key, String defaultReturnValue) {
		String v = get(key);
		if (v == null || v.trim().length() == 0) {
			return defaultReturnValue;
		}
		return v;
	}

	@Override
	public int getInt(String key) {
		return getInt(key, 0);
	}

	@Override
	public int getInt(String key, int dft) {
		try {
			String value = get(key);
			if (value == null) {
				return dft;
			}
			return Integer.parseInt(value);
		} catch (Throwable e) {
			return dft;
		}
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(String key, boolean deflt) {
		try {
			return Boolean.parseBoolean(get(key));
		} catch (Throwable e) {
			return deflt;
		}
	}

	public void put(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public void set(String key, Object value) {
		this.properties.put(key, value);
	}

	@Override
	public Map getDynaProp() {
		return properties;
	}

	@Override
	public boolean is(byte index) {
		return false;
	}

	protected void init() {
		InputStream is = null;
		try {
			// is = Files.findFileAsStream(this.configFile);
			File file = new File(this.configFile);
			if (file.exists()) {
				String str = Files.read(file);
				is = new StringInputStream(Str.toUnicode(str));
				if (is != null)
					properties.load(is);
			}
		} catch (IOException e) {
			log.errorf("从类路径加载配置文件: 出错! [%s] %s", configFile, Ex.msg(e));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException iglore) {
					log.warn(Ex.msg(iglore));
				}
			}
		}
		is = null;
		File file = null;
		try {
			file = getExtConfigFile();
			if (file.exists()) {
				// is = new FileInputStream(file);
				String str = Files.read(file);
				is = new StringInputStream(Str.toUnicode(str));

				Properties customProps = new Properties();
				customProps.load(is);
				properties.putAll(customProps);
			}
		} catch (IOException e) {
			log.errorf("从配置文件目录加载配置文件<%s>出错! 详细信息：%s", file == null ? "NULL" : file.getAbsolutePath(), Ex.msg(e));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException iglore) {
					log.warn(Ex.msg(iglore));
				}
			}
		}
	}

	protected StringBuffer toText() {
		StringBuffer sb = new StringBuffer();
		Iterator keys = this.properties.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			sb.append(key).append("=").append(properties.get(key)).append("\n");
		}

		return sb;
	}

	public String toString() {
		return toText().toString();
	}

	protected File getExtConfigFile() {
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}
		return new File(appconfig.getConfigDir() + "/" + fileName);
	}

	public void save() throws IOException {
		File file = getExtConfigFile();
		OutputStream os = null;
		try {
			if (file != null && !file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			properties.store(os, "");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Throwable e) {
					log.warn(Ex.msg(e));
				}
			}
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}

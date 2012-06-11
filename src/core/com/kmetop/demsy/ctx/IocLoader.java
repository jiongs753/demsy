package com.kmetop.demsy.ctx;

import static com.kmetop.demsy.Demsy.appconfig;

import java.io.File;

import com.kmetop.demsy.ctx.impl.IocNutzImpl;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public abstract class IocLoader {
	private static Log log = Logs.getLog(IocLoader.class);

	private static final String FILE_EXT = ".js";

	public static IIoc load() {
		log.info("加载IOC......");
		String configFile = appconfig.getIOCConfig();
		if (configFile.endsWith(FILE_EXT)) {
			configFile = configFile.substring(0, configFile.length() - FILE_EXT.length());
		}
		configFile = configFile.replace("\\", "/").replace(".", "/") + FILE_EXT;
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}

		String[] paths = null;
		try {
			File file = new File(appconfig.getConfigDir() + "/" + fileName);
			if (!file.exists()) {
				log.tracef("加载IOC: 扩展的IOC配置文件不存在 [path=%s]", file.getAbsolutePath());
				paths = new String[1];
				paths[0] = configFile;
			} else {
				paths = new String[2];
				paths[0] = configFile;
				paths[1] = file.getAbsolutePath();
			}
			if (log.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				int i = 0;
				for (String s : paths) {
					if (i != 0) {
						sb.append(", ");
					}
					sb.append(s);
					i++;
				}
				log.infof("加载IOC: IOC配置文件 [%s]", sb.toString());
				IIoc ret = new IocNutzImpl().init(paths);
				log.infof("加载IOC: 结束.");

				return ret;
			} else
				return new IocNutzImpl().init(paths);
		} catch (Throwable e) {
			log.errorf("加载IOC出错! %s", e);
			return null;
		}
	}
}

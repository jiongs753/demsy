package com.kmetop.demsy.log;

import com.kmetop.demsy.log.impl.JdkLogAdapter;
import com.kmetop.demsy.log.impl.Log4jLogAdapter;
import com.kmetop.demsy.log.impl.LogbackLogAdapter;
import com.kmetop.demsy.log.impl.SystemLogAdapter;

public class Logs {

	private static LogAdapter adapter;

	static {
		LogbackLogAdapter logback = new LogbackLogAdapter();
		if (logback.canWork()) {
			adapter = logback;
		} else {
			Log4jLogAdapter log4j = new Log4jLogAdapter();
			if (log4j.canWork()) {
				adapter = log4j;
			} else {
				JdkLogAdapter jdklog = new JdkLogAdapter();
				if (jdklog.canWork()) {
					adapter = jdklog;
				} else {
					SystemLogAdapter systemlog = new SystemLogAdapter();
					adapter = systemlog;
				}
			}
		}
	}

	public static Log get() {
		return adapter.getLogger(new Throwable().getStackTrace()[1].getClassName());
	}

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static Log getLog(String className) {
		return adapter.getLogger(className);
	}

	public static void error(String msg) {

	}
}

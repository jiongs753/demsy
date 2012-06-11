package com.kmetop.demsy.config.impl;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import static com.kmetop.demsy.Demsy.*;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.config.ILogConfig;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class LogConfig implements ILogConfig {
	private static Log log = Logs.getLog(LogConfig.class);

	private static final String LOGBACK_CLASS_NAME = "ch.qos.logback.classic.Logger";

	private static final String LOG4J_CLASS_NAME = "org.apache.log4j.Logger";

	public LogConfig() {
		this.init();
	}

	protected void init() {
		reload();
	}

	private boolean log4jCanWork() {
		try {
			Class.forName(LOG4J_CLASS_NAME, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}

		return true;
	}

	private boolean logbackCanWork() {
		try {
			Class.forName(LOGBACK_CLASS_NAME, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public void reload() {
		log.infof("加载日志配置......");
		if (log4jCanWork()) {
			Log4jLoader.reload();
		} else {
			log.info("加载日志配置: <LOG4J不工作>");
		}
		if (logbackCanWork()) {
			LogbackLoader.reload();
		} else {
			log.info("加载日志配置: <LOGBACK不工作>");
		}
		log.infof("加载日志配置: 结束.");
	}

	public void shutdown() {
		if (log4jCanWork()) {
			Log4jLoader.shutdown();
		}
		if (logbackCanWork()) {
			LogbackLoader.shutdown();
		}
	}

	private static class Log4jLoader {
		static void reload() {
			log.infof("加载日志配置: 加载LOG4J配置...");
			String xmlfile = appconfig.getConfigDir() + "/log4j-test.properties";

			try {
				if (!Demsy.appconfig.isProductMode() && new File(xmlfile).exists()) {
					log.infof("加载日志配置: LOG4J配置文件 [%s]", xmlfile);
					PropertyConfigurator.configure(xmlfile);
				} else {
					xmlfile = appconfig.getConfigDir() + "/log4j.properties";
					if (new File(xmlfile).exists()) {
						log.infof("加载日志配置: LOG4J配置文件 [%s]", xmlfile);
						PropertyConfigurator.configure(xmlfile);
					} else
						log.infof("加载日志配置: <LOG4J配置文件不存在>");
				}
				log.infof("加载日志配置: 加载LOG4J配置: 结束.");
			} catch (Throwable e) {
				log.infof("加载日志配置: 加载LOG4J配置出错! [%s]\n错误信息：%s", xmlfile, e);
			}
		}

		static void shutdown() {
			LogManager.shutdown();
		}
	}

	private static class LogbackLoader {
		static void reload() {
			log.infof("加载日志配置: 加载LOGBACK配置...");
			String xmlfile = appconfig.getConfigDir() + "/logback-test.xml";
			try {
				if (!Demsy.appconfig.isProductMode() && new File(xmlfile).exists()) {
					log.infof("加载日志配置: LOGBACK配置文件 [%s]", xmlfile);
					ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
					LoggerContext loggerContext = (LoggerContext) loggerFactory;
					loggerContext.stop();
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(loggerContext);
					configurator.doConfigure(new File(xmlfile));
				} else {
					xmlfile = appconfig.getConfigDir() + "/logback.xml";
					if (new File(xmlfile).exists()) {
						log.infof("加载日志配置: LOGBACK配置文件 [%s]", xmlfile);
						ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
						LoggerContext loggerContext = (LoggerContext) loggerFactory;
						loggerContext.stop();
						JoranConfigurator configurator = new JoranConfigurator();
						configurator.setContext(loggerContext);
						configurator.doConfigure(new File(xmlfile));
					} else
						log.infof("加载日志配置: <LOGBACK配置文件不存在>");

				}
				log.infof("加载日志配置: 加载LOGBACK配置: 结束.");
			} catch (Throwable e) {
				log.errorf("加载日志配置: 加载LOGBACK配置出错! [%s]\n错误信息：%s", xmlfile, e);
			}
		}

		static void shutdown() {
		}
	}
}

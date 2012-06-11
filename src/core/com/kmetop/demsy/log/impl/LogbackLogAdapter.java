package com.kmetop.demsy.log.impl;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.LogAdapter;

public class LogbackLogAdapter implements LogAdapter {
	public static final String LOGBACK_CLASS_NAME = "ch.qos.logback.classic.Logger";

	public boolean canWork() {
		try {
			Class.forName(LOGBACK_CLASS_NAME, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public Log getLogger(String className) {
		return new LogbackLogger(className);
	}

	static class LogbackLogger extends AbstractLog {

		public static final String SUPER_FQCN = AbstractLog.class.getName();

		public static final String SELF_FQCN = LogbackLogger.class.getName();

		private Logger logger;

		LogbackLogger(String className) {
			logger = (Logger) LoggerFactory.getLogger(className);
			isFatalEnabled = logger.isEnabledFor(Level.ERROR);
			isErrorEnabled = logger.isEnabledFor(Level.ERROR);
			isWarnEnabled = logger.isEnabledFor(Level.WARN);
			isInfoEnabled = logger.isEnabledFor(Level.INFO);
			isDebugEnabled = logger.isEnabledFor(Level.DEBUG);
			isTraceEnabled = logger.isEnabledFor(Level.TRACE);
		}

		@Override
		public void debug(Object message, Throwable t) {
			if (this.isDebugEnabled())
				this.log(SELF_FQCN, Level.DEBUG, message, t);
		}

		@Override
		public void error(Object message, Throwable t) {
			if (this.isErrorEnabled())
				this.log(SELF_FQCN, Level.ERROR, message, t);
		}

		@Override
		public void fatal(Object message, Throwable t) {
			if (this.isErrorEnabled())
				this.log(SELF_FQCN, Level.ERROR, message, t);
		}

		@Override
		public void info(Object message, Throwable t) {
			if (this.isInfoEnabled())
				this.log(SELF_FQCN, Level.INFO, message, t);
		}

		@Override
		public void trace(Object message, Throwable t) {
			if (this.isTraceEnabled())
				this.log(SELF_FQCN, Level.TRACE, message, t);
		}

		@Override
		public void warn(Object message, Throwable t) {
			if (this.isWarnEnabled())
				this.log(SELF_FQCN, Level.WARN, message, t);
		}

		@Override
		protected void log(int levelInt, Object message, Throwable tx) {
			Level level;
			switch (levelInt) {
			case LEVEL_FATAL:
				level = Level.ERROR;
				break;
			case LEVEL_ERROR:
				level = Level.ERROR;
				break;
			case LEVEL_WARN:
				level = Level.WARN;
				break;
			case LEVEL_INFO:
				level = Level.INFO;
				break;
			case LEVEL_DEBUG:
				level = Level.DEBUG;
				break;
			case LEVEL_TRACE:
				level = Level.TRACE;
				break;
			default:
				level = Level.INFO;
				break;
			}

			this.log(SUPER_FQCN, level, message, tx);
		}

		protected void log(String FQCN, Level level, Object message, Throwable tx) {
			LoggingEvent le = new LoggingEvent(FQCN, logger, level, message.toString(), tx, null);

			logger.callAppenders(le);
		}

	}
}

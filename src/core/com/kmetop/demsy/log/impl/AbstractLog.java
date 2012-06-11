package com.kmetop.demsy.log.impl;

import com.kmetop.demsy.log.Log;

public abstract class AbstractLog implements Log {
	
	protected boolean isFatalEnabled = true;
	protected boolean isErrorEnabled = true;
	protected boolean isWarnEnabled  = true;
	protected boolean isInfoEnabled  = false;
	protected boolean isDebugEnabled = false;
	protected boolean isTraceEnabled = false;

	protected static final int LEVEL_FATAL = 5;
	protected static final int LEVEL_ERROR = 4;
	protected static final int LEVEL_WARN = 3;
	protected static final int LEVEL_INFO = 2;
	protected static final int LEVEL_DEBUG = 1;
	protected static final int LEVEL_TRACE = 0;


	public void debug(Object message) {
		if (isDebugEnabled())
			log(LEVEL_DEBUG, message, null);
	}

	public void debugf(String fmt, Object... args) {
		if (isDebugEnabled())
			log(LEVEL_DEBUG,String.format(fmt, args),null);
	}

	public void error(Object message) {
		if (isErrorEnabled())
			log(LEVEL_ERROR, message, null);
	}

	public void errorf(String fmt, Object... args) {
		if (isErrorEnabled())
			log(LEVEL_ERROR,String.format(fmt, args),null);
	}

	public void fatal(Object message) {
		if (isFatalEnabled())
			log(LEVEL_FATAL,message, null);
	}

	public void fatalf(String fmt, Object... args) {
		if (isFatalEnabled())
			log(LEVEL_FATAL,String.format(fmt, args),null);
	}

	public void info(Object message) {
		if (isInfoEnabled())
			log(LEVEL_INFO,message, null);
	}

	public void infof(String fmt, Object... args) {
		if (isInfoEnabled())
			log(LEVEL_INFO,String.format(fmt, args),null);
	}
	

	public void trace(Object message) {
		if (isTraceEnabled())
			log(LEVEL_TRACE,message, null);
	}

	public void tracef(String fmt, Object... args) {
		if (isTraceEnabled())
			log(LEVEL_TRACE,String.format(fmt, args),null);
	}

	public void warn(Object message) {
		if (isWarnEnabled())
			log(LEVEL_WARN,message, null);
	}

	public void warnf(String fmt, Object... args) {
		if (isWarnEnabled())
			log(LEVEL_WARN,String.format(fmt, args),null);
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public boolean isErrorEnabled() {
		return isErrorEnabled;
	}

	public boolean isFatalEnabled() {
		return isFatalEnabled;
	}

	public boolean isInfoEnabled() {
		return isInfoEnabled;
	}

	public boolean isTraceEnabled() {
		return isTraceEnabled;
	}

	public boolean isWarnEnabled() {
		return isWarnEnabled;
	}
	
	protected abstract void log(int level, Object message,Throwable tx);
}

package com.kmetop.demsy.log.db;

import static com.kmetop.demsy.Demsy.bizSession;

import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.base.log.RunningLog;

public class Log4jAppender extends AppenderSkeleton implements AppenderAttachable {

	private final static String NA = "?";

	private final AppenderAttachableImpl appenders;

	private boolean support = false;

	public Log4jAppender() {
		appenders = new AppenderAttachableImpl();
	}

	// 记录日志到数据库，记录失败则抛出异常
	protected void execute(RunningLog log) {
		bizSession.asynSave(log);
	}

	@Override
	protected void append(LoggingEvent event) {
		if (event == null || !support) {
			appenders.appendLoopOnAppenders(event);
			return;
		}
		try {
			RunningLog log = new RunningLog();
			log.setMessage(event.getRenderedMessage());
			ThrowableInformation ti = event.getThrowableInformation();
			Demsy me = Demsy.me();
			if (me != null) {
				log.setLoginuser(me.username());
			}
			log.setStackTrace(ti == null ? "" : ti.toString());
			log.setLoggername(event.getLoggerName());
			log.setThreadname(event.getThreadName());
			log.setLevel(event.getLevel().toString());
			log.setLogtime(new Date(event.timeStamp));
			log.setNdc(event.getNDC());
			LocationInfo li = event.getLocationInformation();
			String fileName = li.getFileName();
			String className = li.getClassName();
			String methodName = li.getMethodName();
			String lineNumber = li.getLineNumber();
			StringBuffer buf = new StringBuffer();
			appendFragment(buf, className);
			buf.append(".");
			appendFragment(buf, methodName);
			buf.append("(");
			appendFragment(buf, fileName);
			buf.append(":");
			appendFragment(buf, lineNumber);
			buf.append(")");
			String fullInfo = buf.toString();
			log.setLocationinfo(fullInfo);
			execute(log);
		} catch (Throwable e) {
			//System.err.println("保存运行日志到数据库失败! 错误信息： " + e);
			appenders.appendLoopOnAppenders(event);
		}
	}

	private static final void appendFragment(final StringBuffer buf, final String fragment) {
		if (fragment == null) {
			buf.append(NA);
		} else {
			buf.append(fragment);
		}
	}

	public void close() {
		this.closed = true;
	}

	public boolean requiresLayout() {
		return false;
	}

	public void addAppender(final Appender newAppender) {
		synchronized (appenders) {
			appenders.addAppender(newAppender);
		}
	}

	public Enumeration getAllAppenders() {
		synchronized (appenders) {
			return appenders.getAllAppenders();
		}
	}

	public Appender getAppender(final String name) {
		synchronized (appenders) {
			return appenders.getAppender(name);
		}
	}

	public boolean isAttached(final Appender appender) {
		synchronized (appenders) {
			return appenders.isAttached(appender);
		}
	}

	public void removeAllAppenders() {
		synchronized (appenders) {
			appenders.removeAllAppenders();
		}
	}

	public void removeAppender(final Appender appender) {
		synchronized (appenders) {
			appenders.removeAppender(appender);
		}
	}

	public void removeAppender(final String name) {
		synchronized (appenders) {
			appenders.removeAppender(name);
		}
	}
}

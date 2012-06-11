package com.kmetop.demsy.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.nutz.lang.Lang;

/**
 * 异常信息处理工具
 * 
 * @author yongshan.ji
 * 
 */
public abstract class Ex {
	// public static String info(Throwable e, boolean logNullPointer) {
	// StringBuffer sb = new StringBuffer();
	// info(sb, e, logNullPointer);
	// return sb.toString();
	// }

	public static String msg(Throwable e) {
		e = root(e);
		if (e.getClass().equals(NullPointerException.class)) {
			StringWriter str = new StringWriter();
			PrintWriter writer = new PrintWriter(str);
			e.printStackTrace(writer);
			return str.getBuffer().toString();
		} else {
			if (e instanceof DemsyException || e instanceof RuntimeException)
				return e.getMessage();
			else
				return e.toString();
		}
	}

	public static Throwable root(Throwable t) {
		while (true) {
			if (t.getCause() == null)
				break;
			t = t.getCause();
		}

		return t;
	}

	public static RuntimeException throwEx(Throwable e) {
		return (RuntimeException)Lang.wrapThrow(e);
	}

	public static RuntimeException throwEx(String format, Object... args) {
		return (RuntimeException)Lang.makeThrow(format, args);
	}

	public static RuntimeException throwEx(Throwable e, Class wrapper) {
		return (RuntimeException)Lang.wrapThrow(e, wrapper);
	}
}

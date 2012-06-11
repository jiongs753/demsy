package com.kmetop.demsy.ctx.aop;

import org.nutz.aop.ClassAgent;

public interface LazyClassAgent extends ClassAgent {

	String LAZY_CLASSNAME_SUFFIX = "$$LAZY" + CLASSNAME_SUFFIX;

}

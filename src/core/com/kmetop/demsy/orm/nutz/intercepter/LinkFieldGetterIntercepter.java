package com.kmetop.demsy.orm.nutz.intercepter;

import java.lang.reflect.Method;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.interceptor.AbstractMethodInterceptor;
import org.nutz.dao.entity.Link;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.nutz.IExtDao;

public abstract class LinkFieldGetterIntercepter extends AbstractMethodInterceptor {
	private static final Log log = Logs.getLog(LinkFieldGetterIntercepter.class);

	protected IExtDao dao;

	protected Link link;

	protected boolean lazy;

	@Override
	public boolean whenError(Throwable e, Object obj, Method method, Object... args) {
		log.error("调用<" + obj.getClass().getSimpleName() + "." + method.getName() + ">时出现错误! ", e);
		return super.whenError(e, obj, method, args);
	}

	@Override
	public boolean whenException(Exception e, Object obj, Method method, Object... args) {
		log.error("调用<" + obj.getClass().getSimpleName() + "." + method.getName() + ">时出现错误! ", e);
		return super.whenException(e, obj, method, args);
	}

	@Override
	public void filter(InterceptorChain chain) throws Throwable {
		super.filter(chain);
	}
}

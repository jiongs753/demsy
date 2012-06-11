package com.kmetop.demsy.ctx.impl;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.kmetop.demsy.ctx.IIoc;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class IocNutzImpl implements IIoc {
	private Log log = Logs.getLog(IocNutzImpl.class);

	private NutIoc ioc;

	public IIoc init(String... paths) {
		ioc = new NutIoc(new JsonLoader(paths));
		return this;
	}

	@Override
	public Object get(String name) {
		try {
			return ioc.get(null, name);
		} catch (NullPointerException npe) {
			return null;
		} catch (Throwable ex) {
			log.fatalf("获取BEAN<%s>失败! 错误信息： %s", name, Ex.msg(ex));
			return null;
		}
	}

	@Override
	public Object getIoc() {
		return ioc;
	}
}

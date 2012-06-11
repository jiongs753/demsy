package com.kmetop.demsy.mvc.nutz;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;

import com.kmetop.demsy.Demsy;

public class DemsyIocProvider implements IocProvider {

	@Override
	public Ioc create(NutConfig config, String[] args) {
		return (Ioc) Demsy.ioc.getIoc();
	}

}

package com.kmetop.demsy.mvc.nutz;

import java.util.List;

import javax.servlet.ServletContext;

import org.nutz.mvc.config.AbstractNutConfig;
import org.nutz.mvc.config.AtMap;
import org.nutz.resource.Scans;

import com.kmetop.demsy.Demsy;

public class DemsyNutConfig extends AbstractNutConfig {

	public DemsyNutConfig() {
		Demsy.contextAttr(AtMap.class.getName(), new AtMap());
		Scans.me().init(Demsy.servletContext);
	}

	@Override
	public Class<?> getMainModule() {
		return Demsy.class;
	}

	@Override
	public String getAppRoot() {
		return Demsy.contextDir;
	}

	@Override
	public String getAppName() {
		return Demsy.appconfig.getDefaultSoftName();
	}

	@Override
	public ServletContext getServletContext() {
		return Demsy.servletContext;
	}

	@Override
	public String getInitParameter(String name) {
		return Demsy.initParam(name);
	}

	@Override
	public List<String> getInitParameterNames() {
		return Demsy.initParamNames();
	}

}

package com.kmetop.demsy.mvc.template;

import java.io.Writer;
import java.util.Map;

import com.kmetop.demsy.lang.DemsyException;

public abstract class AbstractTemplate implements ITemplateEngine {

	@Override
	public void render(String templateName, Map context, Writer out) throws Exception {
		this.write("", templateName, context, out);
	}

	protected abstract void write(String templateDir, String templateName, Map context, Writer out) throws Exception;

	@Override
	public void renderExpression(String classPath, String expression, Map context, Writer out) throws Exception {
		throw new DemsyException("模板引擎不支持表达式解析： " + expression);
	}

}

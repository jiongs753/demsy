package com.kmetop.demsy.mvc.template;

import java.io.Writer;
import java.util.Map;

public interface ITemplateEngine {

	void render(String templateName, Map context, Writer out) throws Exception;

	void renderExpression(String classPath, String expression, Map context, Writer out) throws Exception;

}

package com.kmetop.demsy.mvc.render;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.template.ITemplateEngine;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;

public abstract class TemplateRender implements IRender, MvcConst {
	protected static Log log = Logs.getLog(TemplateRender.class);

	protected ITemplateEngine templateEngine;

	public TemplateRender(ITemplateEngine tpl) {
		this.templateEngine = tpl;
	}

	@Override
	public void render(Writer out, Throwable ex, Map context) throws Exception {
		if (log.isDebugEnabled())
			log.debug(Ex.msg(ex));

		context.putAll(MvcUtil.globalVariables);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		ex.printStackTrace(pw);
		context.put("detail", sw);
		context.put("info", Ex.msg(ex));

		templateEngine.render(Demsy.appconfig.getTplPackage().replace(".", "/") + "/error.st", context, out);
	}

	public abstract String getContentType(IUIView ui);

	protected abstract String getTemplate(IUIView ui);

	@Override
	public void render(final Writer out, final IUIView ui, final Map context) throws Exception {
		if (ui == null) {
			log.debugf("UI is null.");
			return;
		}
		IUIView model = ui;
		Object data = null;

		if (ui instanceof UIWidgetModel) {
			model = ((UIWidgetModel) ui).getModel();
			data = ((UIWidgetModel) ui).getData();

			if (model.getContext() != null) {
				context.putAll(model.getContext());
			}
		}

		if (ui.getContext() != null) {
			context.putAll(ui.getContext());
		}

		String tpl = getTemplate(ui);
		String expr = ui.getExpression();
		try {
			if (Str.isEmpty(expr))
				this.render(out, model, data, context, tpl);
			else
				this.renderExpression(out, model, data, context, tpl, expr);
		} catch (Throwable e) {
			if (!Demsy.appconfig.isProductMode()) {
				log.error("解析模版出错! " + tpl, e);
			} else {
				log.errorf("解析模版出错! [%s]\n%s", tpl, Ex.msg(e));
			}
			// throw new DemsyException("解析模版出错! [%s]\n%s", tpl,
			// Throws.info(e));
		}
	}

	protected void render(Writer out, IUIView modelOrPage, Object data, Map context, String template) throws Exception {
		log.debugf("Template Render...[model=%s]", modelOrPage);

		if (data != null)
			context.put("data", data);

		context.put("ui", modelOrPage);
		context.put("uiRender", this);

		templateEngine.render(template, context, out);

		log.debugf("Template Render: 结束. [model=%s, path=%s]", modelOrPage, template);
	}

	protected void renderExpression(Writer out, IUIView modelOrPage, Object data, Map context, String classPath, String expression) throws Exception {
		log.debugf("Template Render...[model=%s]", modelOrPage);

		if (data != null)
			context.put("data", data);

		context.put("ui", modelOrPage);
		context.put("uiRender", this);

		templateEngine.renderExpression(classPath, expression, context, out);

		log.debugf("Template Render: 结束. [model=%s, expression=%s]", modelOrPage, expression);
	}
}

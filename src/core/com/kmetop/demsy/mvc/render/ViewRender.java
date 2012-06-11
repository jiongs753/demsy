package com.kmetop.demsy.mvc.render;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.template.ITemplateEngine;
import com.kmetop.demsy.mvc.ui.IUIView;
import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;

public class ViewRender extends TemplateRender {

	public ViewRender(ITemplateEngine tpl) {
		super(tpl);
	}

	public String getContentType(IUIView ui) {
		return "text/html; charset=UTF-8";

	}

	protected String getTemplate(IUIView ui) {
		String tpl = ui.getTemplate();

		if (Str.isEmpty(tpl)) {
			if (ui instanceof UIWidgetModel) {
				ui = ((UIWidgetModel) ui).getModel();
				tpl = ui.getTemplate();
			}
		}
		if (Str.isEmpty(tpl)) {
			tpl = ui.getClass().getName().replace("com.kmetop.demsy.mvc.", "");
		}

		return (Demsy.appconfig.getTplPackage() + "." + tpl).replace(".", "/") + (Str.isEmpty(ui.getTemplateType()) ? ".st" : ui.getTemplateType());
	}

}

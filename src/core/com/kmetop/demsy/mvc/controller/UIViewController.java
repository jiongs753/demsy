package com.kmetop.demsy.mvc.controller;

import java.util.Map;

import com.kmetop.demsy.mvc.ui.IUIViewController;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.expr.CndExpr;

public class UIViewController implements IUIViewController {

	protected void init(UIBlockContext maker, Map context) {
	}

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	@Override
	public Object process(UIBlockContext blockContext) {
		blockContext.query();

		return null;
	}

	@Override
	public String getViewTemplate(UIBlockContext context, String defaultTemplate) {
		return defaultTemplate;
	}

	@Override
	public String getViewExpression(UIBlockContext context, String defaultExpression) {
		return defaultExpression;
	}

}

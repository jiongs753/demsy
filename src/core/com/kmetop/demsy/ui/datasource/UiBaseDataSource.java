package com.kmetop.demsy.ui.datasource;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.ui.IUIViewController;
import com.kmetop.demsy.mvc.ui.UIBlockContext;

public abstract class UiBaseDataSource implements IUIViewController {
	static Log log = Logs.getLog(UiBaseDataSource.class);

	@Override
	public String getViewExpression(UIBlockContext context, String defaultExpression) {
		return defaultExpression;
	}

	@Override
	public String getViewTemplate(UIBlockContext parser, String defaultTemplate) {
		return defaultTemplate;
	}
}

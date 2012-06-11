package com.kmetop.demsy.ui.datasource;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.expr.CndExpr;

public class MyBlogTopicList extends UiDataset {

	protected CndExpr getExpr(UIBlockContext parser) {
		return CndExpr.eq("createdBy", Demsy.me().username());
	}
}

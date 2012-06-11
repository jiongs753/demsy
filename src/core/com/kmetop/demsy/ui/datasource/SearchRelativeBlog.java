package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class SearchRelativeBlog extends UiDataset {

	protected CndExpr getExpr(UIBlockContext parser) {
		IOrm orm = Demsy.orm();

		IBizSystem sys = bizEngine.getSystem(IBlogPost.SYS_CODE);
		Class type = bizEngine.getType(sys);

		CndExpr expr = null;
		Long topicID = parser.getDynamicDataID();
		if (topicID != null) {
			IBlogPost topic = (IBlogPost) orm.load(type, topicID);
			String createdBy = topic.getCreatedBy();
			parser.getCatalog().setName("[" + createdBy + "]的博客文章");

			expr = Expr.eq("createdBy", createdBy);
			expr.addDesc("postat");

		} else {
			parser.getCatalog().setName("最新博客文章");
			expr = Expr.desc("postat");
		}

		return expr;
	}
}

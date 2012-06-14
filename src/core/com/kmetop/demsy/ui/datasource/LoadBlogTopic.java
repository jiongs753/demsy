package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;

import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.comlib.web.IBlogPostComment;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 全文阅读栏目信息
 * 
 * @author yongshan.ji
 * 
 */
public class LoadBlogTopic extends UiRecord {

	protected void init(UIBlockContext maker, Map context) {
		Demsy me = Demsy.me();
		IModule commentMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(IBlogPostComment.SYS_CODE));
		context.put("uploadUrl", contextPath(MvcConst.URL_UPLOAD, commentMdl.getId()));
		if (commentMdl != null) {
			context.put("commentUrl", MvcUtil.contextPath(MvcConst.URL_BZ_SAVE, commentMdl.getId() + ":", "c", Demsy.me().addToken()));
			Demsy.security.addPermission("block" + maker.getBlock().getId(), IUserRole.ROLE_ANONYMOUS, commentMdl.getId(), "c");
		}

		maker.put("commentMdl", commentMdl);
		maker.put("commentPage", me.param("commentPage", Integer.class, null));
	}

	public String getViewTemplate(UIBlockContext maker, String defaultTemplate) {
		if (maker.get("commentPage") != null) {
			return defaultTemplate + "_comments";
		}

		return defaultTemplate;
	}

	protected Object loadRecord(UIBlockContext maker, Map context) {
		IBlogPost info = (IBlogPost) super.loadRecord(maker, context);
		if (info == null) {
			return null;
		}

		IOrm orm = Demsy.orm();
		Integer cpage = maker.get("commentPage");
		if (cpage == null) {
			cpage = 1;
		}
		IModule commentMdl = maker.get("commentMdl");
		Pager pager = new Pager(bizEngine.getType(moduleEngine.getSystem(commentMdl)));
		// CndExpr expr = Expr.ne("hide", 1);
		// if (info != null) {
		// expr = expr.and(Expr.eq("post", info));
		// }
		CndExpr expr = Expr.eq("post", info);
		expr.addAsc("postat");
		expr = expr.setPager(cpage, maker.getPageSize());
		pager.setQueryExpr(expr);
		orm.query(pager);

		context.put("pager", pager);

		if (maker.getPageView() != null)
			maker.getPageView().set("subtitle", info.toString());

		Demsy.uiEngine.addClickNum(orm, (IStatistic) info);

		return info;
	}
}

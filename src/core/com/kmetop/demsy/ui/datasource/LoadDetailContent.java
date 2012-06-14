package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;

import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.impl.sft.web.content.Comment;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.web.IWebContent;
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
public class LoadDetailContent extends UiRecord {

	protected void init(UIBlockContext maker, Map context) {
		Demsy me = Demsy.me();
		IModule commentMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem("Comment"));
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
		Object info = super.loadRecord(maker, context);

		context.put("module", maker.getModule());
		context.put("subject", info);

		IOrm orm = Demsy.orm();
		Integer cpage = maker.get("commentPage");
		if (cpage != null) {
			IModule commentMdl = maker.get("commentMdl");
			Pager pager = new Pager(bizEngine.getType(moduleEngine.getSystem(commentMdl)));
			CndExpr expr = Expr.eq("status", Comment.STATUS_SHOWN);
			if (info != null) {
				expr = expr.and(Expr.eq("subjectID", info)).and(Expr.eq("module", maker.getModule()));
				if (info instanceof IWebContent) {
					expr = expr.or(Expr.eq("webContent", info));
				}
				expr.addAsc(LibConst.F_CREATED);
				expr = expr.setPager(cpage, maker.getPageSize());
				pager.setQueryExpr(expr);
				orm.query(pager);
			}

			context.put("pager", pager);
		} else {
			if (maker.getPageView() != null)
				maker.getPageView().set("subtitle", info.toString());
			try {
				moduleEngine.increase(orm, info, "clickNum");
			} catch (Throwable e) {
				log.warn(e);
			}
		}

		return info;
	}
}

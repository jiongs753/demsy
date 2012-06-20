package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.impl.sft.web.content.WebContentCategory;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.web.IBbsTopic;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class Search extends UiBaseDataSource {

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();
		Demsy me = Demsy.me();
		String keywords = me.param("keywords", String.class, null);

		UIBlockDataModel data = parser.getCatalog();
		ctx.put("data", data);
		if (!Str.isEmpty(keywords)) {
			ctx.put("keywords", keywords);

			Integer page = me.param("page", Integer.class, 1);
			String[] links = Str.toArray(parser.getBlock().getParams());

			List<WebContentCategory> clist = Demsy.orm().query(WebContentCategory.class, Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId()).and(Expr.eq("infoEnabledSearch", 1)));
			CndExpr expr = Expr.in("catalog", clist).and(Expr.isNull("refrence")).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId()));

			Pager pager = new Pager(null);
			pager.setPageIndex(page);
			pager.setPageSize(parser.getPageSize());
			ctx.put("pager", pager);

			// 检索产品
			IBizSystem sys = bizEngine.getSystem(IProduct.SYS_CODE);
			IModule module = moduleEngine.getModule(me.getSoft(), sys);
			Pager pager4 = this.query(parser, bizEngine.getType(sys), keywords, page, LibConst.F_CREATED, null);
			List data4 = new LinkedList();
			List list = pager4.getResult();
			if (list.size() > 0) {
				UIBlockDataModel item = null;
				for (Object obj : list) {
					if (links.length > 0 && module != null)
						item = parser.makeDataModel(obj, Long.parseLong(links[0]), module.getId());
					else
						item = parser.makeDataModel(obj, null, null);

					item.setName(item.getName().replace(keywords, "<span style='font-weight:bold;'>" + keywords + "</span>"));
					data4.add(item);
				}
			}
			ctx.put("data0", data4);
			pager.setTotalRecord(Math.max(pager4.getTotalRecord(), pager.getTotalRecord()));

			// 检索新闻信息
			sys = bizEngine.getSystem(IWebContent.SYS_CODE);
			module = moduleEngine.getModule(me.getSoft(), sys);
			Pager pager1 = this.query(parser, bizEngine.getType(sys), keywords, page, LibConst.F_CREATED, expr);
			List data1 = new LinkedList();
			list = pager1.getResult();
			if (list.size() > 0) {
				UIBlockDataModel item = null;
				for (Object obj : list) {
					if (links.length > 1 && module != null)
						item = parser.makeDataModel(obj, Long.parseLong(links[1]), module.getId());
					else
						item = parser.makeDataModel(obj, null, null);

					item.setName(item.getName().replace(keywords, "<span style='font-weight:bold;'>" + keywords + "</span>"));
					data1.add(item);
				}
			}
			ctx.put("data1", data1);
			pager.setTotalRecord(Math.max(pager1.getTotalRecord(), pager.getTotalRecord()));

			// 检索论坛帖子
			sys = bizEngine.getSystem(IBbsTopic.SYS_CODE);
			module = moduleEngine.getModule(me.getSoft(), sys);
			Pager pager2 = this.query(parser, bizEngine.getType(sys), keywords, page, LibConst.F_CREATED, null);
			List data2 = new LinkedList();
			list = pager2.getResult();
			if (list.size() > 0) {
				UIBlockDataModel item = null;
				for (Object obj : list) {
					if (links.length > 2 && module != null)
						item = parser.makeDataModel(obj, Long.parseLong(links[2]), module.getId());
					else
						item = parser.makeDataModel(obj, null, null);

					item.setName(item.getName().replace(keywords, "<span style='font-weight:bold;'>" + keywords + "</span>"));
					data2.add(item);
				}
			}
			ctx.put("data2", data2);
			pager.setTotalRecord(Math.max(pager2.getTotalRecord(), pager.getTotalRecord()));

			// 检索博客文章
			sys = bizEngine.getSystem(IBlogPost.SYS_CODE);
			module = moduleEngine.getModule(me.getSoft(), sys);
			Pager pager3 = this.query(parser, bizEngine.getType(sys), keywords, page, "postat", null);
			List data3 = new LinkedList();
			list = pager3.getResult();
			if (list.size() > 0) {
				UIBlockDataModel item = null;
				for (Object obj : list) {
					if (links.length > 3 && module != null)
						item = parser.makeDataModel(obj, Long.parseLong(links[3]), module.getId());
					else
						item = parser.makeDataModel(obj, null, null);

					item.setName(item.getName().replace(keywords, "<span style='font-weight:bold;'>" + keywords + "</span>"));
					data3.add(item);
				}
			}
			ctx.put("data3", data3);
			pager.setTotalRecord(Math.max(pager3.getTotalRecord(), pager.getTotalRecord()));

		}

		return ctx;
	}

	private Pager query(UIBlockContext parser, Class kls, String keywords, int page, String order, CndExpr cnd) {
		CndExpr expr = Expr.contains(LibConst.F_NAME, keywords);
		if (cnd != null) {
			expr = expr.and(cnd);
		}
		expr.setPager(page, parser.getPageSize());
		expr.addDesc(order);
		Pager pager = new Pager(kls);
		pager.setQueryExpr(expr);
		Demsy.orm().query(pager);

		return pager;
	}
}

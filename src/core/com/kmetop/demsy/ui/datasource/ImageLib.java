package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.web.content.Comment;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class ImageLib extends UiRecord {
	public Map process(UIBlockContext maker) {
		Map ctx = new HashMap();
		this.init(maker, ctx);

		this.loadRecord(maker, ctx);

		return ctx;
	}

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
		IWebContentCatalog catalog = null;
		IWebContentCatalog prevcata = null;
		IWebContentCatalog nextcata = null;
		Long linkID = null;
		Long moduleID = maker.getModule().getId();
		IPage link = maker.getBlock().getLink();
		if (link != null)
			linkID = link.getId();

		List<IWebContentCatalog> list = maker.query();
		if (list != null && list.size() > 0) {
			Long dataID = maker.getDynamicDataID();
			if (dataID == null || dataID == 0) {
				catalog = list.get(0);
				if (list.size() > 1) {
					nextcata = list.get(1);
				}
			} else {
				for (int i = list.size() - 1; i >= 0; i--) {
					catalog = list.get(i);
					if (dataID.equals(catalog.getId())) {
						if (i > 0) {
							prevcata = list.get(i - 1);
						}
						if (i + 1 < list.size()) {
							nextcata = list.get(i + 1);
						}
						break;
					}
				}
			}
			UIBlockDataModel nodelist = new UIBlockDataModel();
			int rowsize = maker.getCellCount();
			if (rowsize <= 1) {
				rowsize = 6;
			}
			int count = 0;
			UIBlockDataModel rownode = null;
			for (IWebContentCatalog cata : list) {
				if (count % rowsize == 0) {
					rownode = new UIBlockDataModel();
					nodelist.addItem(rownode);
				}
				rownode.addItem(makeNode(cata, linkID, moduleID));
				count++;
			}
			context.put("datalist", nodelist);
		}

		context.put("module", maker.getModule());
		context.put("subject", catalog);

		IOrm orm = Demsy.orm();
		Integer cpage = maker.get("commentPage");
		if (cpage != null) {
			IModule commentMdl = maker.get("commentMdl");
			Pager pager = new Pager(bizEngine.getType(moduleEngine.getSystem(commentMdl)));
			CndExpr expr = Expr.eq("status", Comment.STATUS_SHOWN);
			if (catalog != null) {
				expr = expr.and(Expr.eq("subjectID", catalog)).and(Expr.eq("module", maker.getModule()));
				expr.addAsc(LibConst.F_CREATED);
				expr = expr.setPager(cpage, maker.getPageSize());
				pager.setQueryExpr(expr);
				orm.query(pager);
			}

			context.put("pager", pager);
		} else if (catalog != null) {
			CndExpr expr = Expr.eq("catalog", catalog).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft()));
			expr.addAsc(LibConst.F_CREATED);
			List<IWebContent> items = orm.query(bizEngine.getStaticType(IWebContent.SYS_CODE), expr);

			UIBlockDataModel node = makeNode(catalog, linkID, moduleID);
			for (IWebContent info : items) {
				UIBlockDataModel item = new UIBlockDataModel();
				item.setName(info.getName());
				item.setImg(Demsy.contextPath + info.getImage().toString());
				item.setObj(info);
				node.addItem(item);
			}
			context.put("data", node);

			if (prevcata != null) {
				context.put("prev", makeNode(prevcata, linkID, moduleID));
			}

			if (nextcata != null) {
				context.put("next", makeNode(nextcata, linkID, moduleID));
			}

			if (maker.getPageView() != null)
				maker.getPageView().set("subtitle", catalog.toString());
			try {
				moduleEngine.increase(orm, catalog, "clickNum");
			} catch (Throwable e) {
				log.warn(e);
			}
		}

		return catalog;
	}

	private UIBlockDataModel makeNode(IWebContentCatalog cata, Long linkID, Long moduleID) {
		UIBlockDataModel node = new UIBlockDataModel();
		node.setName(cata.getName());
		node.setObj(cata);
		Upload img = cata.getImage();
		if (img == null || Str.isEmpty(img.toString())) {
			CndExpr exprprev = Expr.eq("catalog", cata).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft()));
			exprprev.addAsc(LibConst.F_CREATED);
			IWebContent previnfo = (IWebContent) Demsy.orm().load(bizEngine.getStaticType(IWebContent.SYS_CODE), exprprev);
			img = previnfo.getImage();
		}
		node.setImg(Demsy.contextPath + img);
		if (linkID != null) {
			node.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + cata.getId()));
		}

		return node;
	}
}

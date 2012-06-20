package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LybbsDb;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.comlib.web.IBbsForum;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class ListBbsTopicGroupByForum extends UiBaseDataSource {

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();

		IOrm orm = Demsy.orm();
		UIBlockDataModel root = parser.getCatalog();

		List<LybbsDb> catalogList = orm.query(LybbsDb.class, Expr.eq("indexforum", "1"));

		IPageBlock block = parser.getBlock();

		IModule cataModule = moduleEngine.getModule(Demsy.me().getSoft(), bizEngine.getSystem(IBbsForum.SYS_CODE));
		Long titleModuleID = null;
		if (cataModule != null)
			titleModuleID = cataModule.getId();
		Long titleLinkID = null;
		if (block.getTitleLink() != null)
			titleLinkID = block.getTitleLink().getId();
		String titleTarget = block.getTitleLinkTarget();

		IModule itemModule = parser.getModule();
		Long itemModuleID = null;
		if (itemModule != null)
			itemModuleID = itemModule.getId();
		Long itemLinkID = null;
		if (block.getLink() != null)
			itemLinkID = block.getLink().getId();
		String itemTarget = block.getLinkTarget();

		for (LybbsDb cata : catalogList) {
			UIBlockDataModel title = makeData(cata, titleLinkID, titleModuleID, titleTarget);
			root.addItem(title);
			Upload img = cata.getTeamlogo();
			if (img != null && !Str.isEmpty(img.toString())) {
				title.setImg(Demsy.contextPath + img.toString());
			} else {
				title.setImg("");
			}

			Pager pager = new Pager(parser.getType());
			CndExpr expr = Expr.eq("forum", cata);

			if (cata.getCheckPostStatus() == 1) {// 该论坛的帖子需要审核后才能显示
				expr = expr.and(Expr.eq("status", 2));
			} else {// 无需审核：查询未被屏蔽的帖子
				expr = expr.and(Expr.ne("status", 1));
			}
			expr = expr.setPager(0, parser.getPageSize());
			expr = expr.addDesc("id");

			pager.setQueryExpr(expr);

			List list = orm.query(pager);

			for (Object obj : list) {
				UIBlockDataModel item = makeData(obj, itemLinkID, itemModuleID, itemTarget);
				title.addItem(item);
			}
		}

		ctx.put("data", root);

		return ctx;
	}

	public UIBlockDataModel makeData(Object obj, Long linkID, Long moduleID, String target) {
		UIBlockDataModel item = new UIBlockDataModel();

		item.setName(obj.toString());

		if (linkID != null && moduleID != null) {
			item.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + Obj.getId(obj)));
		}
		if (!Str.isEmpty(target)) {
			item.setTarget(target);
		}

		item.setObj(obj);

		return item;
	}
}

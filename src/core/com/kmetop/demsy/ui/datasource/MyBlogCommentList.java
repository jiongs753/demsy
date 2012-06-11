package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.sft.lybbs.LyblogComment;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.expr.CndExpr;

public class MyBlogCommentList extends UiDataset {

	protected CndExpr getExpr(UIBlockContext parser) {
		return CndExpr.eq("createdBy", Demsy.me().username());
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();
		this.init(parser, ctx);

		List<LyblogComment> result = new ArrayList();
		if (parser.getPageSize() == 1) {
			LyblogComment record = (LyblogComment) parser.getItemObj();
			if (record != null) {
				result.add(record);
			}
		}
		if (result.size() == 0) {
			result = parser.query(getExpr(parser));
			if (parser.getBlock().isFillBlank())
				for (int i = result.size(); i < parser.getPageSize(); i++) {
					result.add(null);
				}
		}

		IModule infoMdl = moduleEngine.getModule(Demsy.me().getSoft(), bizEngine.getSystem(IBlogPost.SYS_CODE));
		Long linkID = null;
		IPage link = parser.getBlock().getLink();
		if (link != null)
			linkID = link.getId();

		UIBlockDataModel data = parser.getCatalog();
		if (parser.getCellCount() == 1) {
			for (LyblogComment obj : result) {
				if (obj == null)
					data.addItem(new UIBlockDataModel());
				else {
					UIBlockDataModel item = parser.makeDataModel(obj);
					if (obj.getPost() != null) {
						Integer dataID = obj.getPost().getId();
						if (dataID != null && linkID != null) {
							item.setHref(MvcUtil.contextPath(URL_UI, linkID, infoMdl.getId() + ":" + dataID));
						}
					}
					if (obj.getContent() != null)
						item.setName(obj.getContent().toString(), 40);
					else
						item.setName(obj.getName(), 40);

					data.addItem(item);
				}
			}
		} else {
			List<List> listlist = parser.querySplit(result);
			for (List<LyblogComment> list : listlist) {
				UIBlockDataModel row = new UIBlockDataModel();
				data.addItem(row);
				for (LyblogComment obj : list) {
					if (obj == null)
						row.addItem(parser.makeDataModel(null));
					else {
						UIBlockDataModel item = parser.makeDataModel(obj);
						if (obj.getPost() != null) {
							Integer dataID = obj.getPost().getId();
							if (dataID != null && linkID != null) {
								item.setHref(MvcUtil.contextPath(URL_UI, linkID, infoMdl.getId() + ":" + dataID));
							}
						}
						if (obj.getContent() != null)
							item.setName(obj.getContent().toString(), 40);
						else
							item.setName(obj.getName(), 40);

						data.addItem(item);
					}
				}
			}
		}

		ctx.put("data", data);
		ctx.put("pager", parser.getPager());

		return ctx;
	}
}

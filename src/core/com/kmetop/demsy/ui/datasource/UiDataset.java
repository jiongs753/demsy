package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.expr.CndExpr;

public class UiDataset extends UiBaseDataSource {

	protected void init(UIBlockContext maker, Map context) {
		Demsy me = Demsy.me();
		IModule voteMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(LibConst.BIZSYS_WEB_VOTE));
		if (voteMdl != null) {
			context.put("voteMdl", MvcUtil.contextPath(MvcConst.URL_BZ_SAVE, voteMdl.getId() + ":", "c"));
			context.put("voteToken", Demsy.me().addToken());
			Demsy.security.addPermission("block" + maker.getBlock().getId(), IUserRole.ROLE_ANONYMOUS, voteMdl.getId(), "c");
		}
	}

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();
		this.init(parser, ctx);

		List result = new ArrayList();
		if (parser.getPageSize() == 1) {
			Object record = parser.getItemObj();
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

		UIBlockDataModel data = parser.getCatalog();
		if (parser.getCellCount() == 1) {
			for (Object obj : result) {
				if (obj == null)
					data.addItem(new UIBlockDataModel());
				else
					data.addItem(parser.makeDataModel(obj));
			}
		} else {
			List<List> listlist = parser.querySplit(result);
			for (List list : listlist) {
				UIBlockDataModel row = new UIBlockDataModel();
				data.addItem(row);
				for (Object obj : list) {
					if (obj == null)
						row.addItem(parser.makeDataModel(null));
					else
						row.addItem(parser.makeDataModel(obj));
				}
			}
		}

		ctx.put("data", data);
		ctx.put("pager", parser.getPager());

		return ctx;
	}
}

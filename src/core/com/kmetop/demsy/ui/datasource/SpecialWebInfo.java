package com.kmetop.demsy.ui.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 全文阅读栏目信息
 * 
 * @author yongshan.ji
 * 
 */
public class SpecialWebInfo extends UiBaseDataSource {

	public Map process(UIBlockContext maker) {
		Map ctx = new HashMap();

		Object record = this.loadRecord(maker, ctx);
		UIBlockDataModel node = maker.makeDataModel(record);
		ctx.put("data", node);
		ctx.put("obj", record);

		IOrm orm = Demsy.orm();
		List result = orm.query(record.getClass(), Expr.eq(LibConst.F_PARENT, record));
		List<List> listlist = maker.querySplit(result);
		for (List list : listlist) {
			UIBlockDataModel row = new UIBlockDataModel();
			node.addItem(row);
			for (Object obj : list) {
				if (obj == null)
					row.addItem(new UIBlockDataModel());
				else
					row.addItem(maker.makeDataModel(obj));
			}
		}

		return ctx;
	}

	protected Object loadRecord(UIBlockContext maker, Map context) {
		Object record = maker.getItemObj();
		if (record == null) {
			List list = maker.query();
			if (list != null && list.size() > 0)
				record = list.get(0);
		}

		if (record instanceof IWebContent) {
			IWebContent info = (IWebContent) record;
			if (info.getRefrence() != null) {
				record = info.getRefrence();
			}
		}

		return record;
	}
}

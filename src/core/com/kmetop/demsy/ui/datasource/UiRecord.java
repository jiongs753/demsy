package com.kmetop.demsy.ui.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;

public class UiRecord extends UiBaseDataSource {

	protected void init(UIBlockContext maker, Map context) {

	}

	public Map process(UIBlockContext maker) {
		Map ctx = new HashMap();
		this.init(maker, ctx);

		Object record = this.loadRecord(maker, ctx);
		UIBlockDataModel node = maker.makeDataModel(record);

		IPageBlock block = maker.getBlock();
		String t = block.getName();
		String postFlag = "{+}";

		if (!Str.isEmpty(t) && t.startsWith(postFlag)) {
			node.setName(node.getName() + t.replace(postFlag, ""));
		}

		ctx.put("data", node);
		ctx.put("obj", record);

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

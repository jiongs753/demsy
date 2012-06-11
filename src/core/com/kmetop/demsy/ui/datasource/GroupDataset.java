package com.kmetop.demsy.ui.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.expr.CndExpr;

public class GroupDataset extends UiBaseDataSource {

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();

		List<UIBlockDataModel> group = new LinkedList();
		List result = parser.query(getExpr(parser));
		// for (int i = result.size(); i < parser.getPageSize(); i++) {
		// result.add(null);
		// }
		String groupBy = parser.getGroupBy();

		UIBlockDataModel defaultCatalogNode = parser.getCatalog();
		if (Str.isEmpty(groupBy)) {
			group.add(defaultCatalogNode);
			if (parser.getCellCount() == 1) {
				for (Object obj : result) {
					defaultCatalogNode.addItem(parser.makeDataModel(obj));
				}
			} else {
				List<List> listlist = parser.querySplit(result);
				for (List list : listlist) {
					UIBlockDataModel row = new UIBlockDataModel();
					defaultCatalogNode.addItem(row);
					for (Object obj : list) {
						row.addItem(parser.makeDataModel(obj));
					}
				}
			}
		} else {
			Object catalogObj = null;
			UIBlockDataModel catalogNode = null;
			for (Object obj : result) {
				Object tmpCatalog = Obj.getValue(obj, groupBy);
				if (tmpCatalog != null && (catalogObj == null || !catalogObj.equals(tmpCatalog))) {
					catalogObj = tmpCatalog;
					catalogNode = parser.makeDataModel(catalogObj);
					group.add(catalogNode);
				} else if (tmpCatalog == null) {
					catalogNode = defaultCatalogNode;
					if (!group.contains(catalogNode)) {
						group.add(catalogNode);
					}
				}

				catalogNode.addItem(parser.makeDataModel(obj));
			}
			if (parser.getCellCount() > 1) {
				for (UIBlockDataModel cata : group) {
					List<UIBlockDataModel> items = cata.getItems();
					List<List> listlist = parser.querySplit(items);
					cata.setItems(null);

					for (List<UIBlockDataModel> list : listlist) {
						UIBlockDataModel row = new UIBlockDataModel();
						cata.addItem(row);
						if (list != null)
							for (UIBlockDataModel obj : list) {
								row.addItem(obj);
							}
					}
				}
			}
		}

		ctx.put("group", group);
		ctx.put("pager", parser.getPager());

		return ctx;
	}
}

package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.common.IContact;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;

public class ProductSaleList extends UiDataset {

	protected void init(UIBlockContext maker, Map context) {
		super.init(maker, context);
		context.put("cfgPostFee", SoftConfigManager.me().getEshopPostFee());
		context.put("cfgNotPostFee", SoftConfigManager.me().getEshopNotPostFee());
		context.put("cfgNotPostFeeDesc", SoftConfigManager.me().getEshopNotPostFeeDesc());
		// 联系地址
		if (Demsy.me().login() != null) {
			IOrm orm = Demsy.orm();
			Class type = bizEngine.getStaticType(IContact.SYS_CODE);
			List contactList = orm.query(type, Expr.eq(LibConst.F_CREATED_BY, Demsy.me().username()).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId())));

			if (contactList != null && contactList.size() > 0)
				context.put("contactList", contactList);
		}
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
			for (List<IProduct> list : listlist) {
				UIBlockDataModel row = new UIBlockDataModel();
				data.addItem(row);
				for (IProduct obj : list) {
					UIBlockDataModel item = parser.makeDataModel(obj);
					if (obj != null) {
						StringBuffer title = new StringBuffer(item.getTitle());
						Double oldprice = obj.getOldPrice();
						Double price = obj.getNowPrice();
						Double balance = obj.getBalance();
						if (oldprice != null && !oldprice.equals(price))
							title.append(" 原价:" + Obj.format(oldprice, "#,##0.00"));
						if (price != null && oldprice != null && !oldprice.equals(price))
							title.append(" 现价:" + Obj.format(price, "#,##0.00"));
						if (balance != null && balance > 0)
							title.append(" 省:" + Obj.format(balance, "#,##0.00"));
						item.setTitle(title.toString());
					}
					row.addItem(item);
				}
			}
		}

		ctx.put("data", data);
		ctx.put("pager", parser.getPager());

		return ctx;
	}
}

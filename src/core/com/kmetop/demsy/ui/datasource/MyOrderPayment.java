package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;

import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class MyOrderPayment extends UiBaseDataSource {

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();

		Class ordClass = bizEngine.getSystemClass(IOrder.SYS_CODE);
		IOrm orm = Demsy.orm();
		IDemsySoft soft = Demsy.me().getSoft();
		IOrder order = (IOrder) orm.load(ordClass, Expr.eq(LibConst.F_ID, parser.getDynamicDataID()).and(Expr.eq(LibConst.F_SOFT_ID, soft.getId())));
		if (order.getStatus() != IOrder.STATUS_WAIT_BUYER_PAY) {
			ctx.put("orderError", String.format("不能为该订单付款！订单状态：%s", IOrder.STATUS_TITLES[order.getStatus()]));
		}

		ctx.put("orderID", parser.getDynamicModuleID() + ":" + parser.getDynamicDataID());

		ctx.put("order", order);

		return ctx;
	}
}

package com.kmetop.demsy.plugins.eshop;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.actions.OrderActions;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.impl.base.ebusiness.order.Logistics;
import com.kmetop.demsy.comlib.impl.base.ebusiness.order.Order;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 打印单据
 * 
 * @author yongshan.ji
 * 
 */
public class PrintLogisticsBill extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		Logistics en = (Logistics) event.getEntity();
		en.setPrintNum(en.getPrintNum() + 1);
		en.setPrintDate(new Date());
		// 发货处理
		String orderID = en.getOrderID();
		int idx = orderID.lastIndexOf("_");
		if (idx > -1) {
			orderID = orderID.substring(0, idx);
		}

		IOrm orm = event.getOrm();
		IOrder order = (IOrder) orm.load(Order.class, Expr.eq(LibConst.F_TIME_ID, orderID).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId())));

		List<Logistics> list = orm.query(Logistics.class, Expr.beginWith("orderID", order.getOrderID()).addAsc("orderID"));
		List<String> names = new LinkedList();
		List<String> codes = new LinkedList();
		StringBuffer note = new StringBuffer();
		for (Logistics l : list) {
			if (l.equals(en)) {
				l = en;
			}
			String name = l.getLogisticsName();
			String code = l.getCode();
			if (!Str.isEmpty(code)) {
				codes.add(code);
				names.add(name);
				note.append(", ").append(name).append("(").append(code).append(")");
			}
		}

		order.setLogisticsID(Str.join(codes, "", ", "));
		order.setLogisticsName(Str.join(names, "", ", "));
		if (note.length() > 0)
			order.setLogisticsNote(note.substring(2));
		order.setLogisticsTime(en.getPrintDate());

		OrderActions.processOrder(orm, order, order.getTradeID(), IOrder.STATUS_WAIT_BUYER_CONFIRM_GOODS);
		orm.save(order);
	}
}

package com.kmetop.demsy.plugins.activity;

import java.util.Date;
import java.util.List;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.base.ebusiness.order.Order;
import com.kmetop.demsy.comlib.impl.sft.activity.ActivityCatalog;
import com.kmetop.demsy.comlib.impl.sft.activity.StockActivity;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 查找订单号并对订单号进行兑奖
 * 
 * @author yongshan.ji
 * 
 */
public class StockGuessPrize extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		IOrm orm = event.getOrm();
		List<StockActivity> list = (List<StockActivity>) event.getEntity();
		for (StockActivity one : list) {
			Byte status = one.getStatus();

			// 无效竞猜
			if (status == null || status.equals(StockActivity.STATUS_CANCEL) || status.equals((byte) 0)) {
				continue;
			}

			// 竞猜结果尚未统计
			if (one.getResultOrder() == null || one.getResultOrder() == 0) {
				continue;
			}

			// 已领奖: 不能再兑奖
			if (status.equals(StockActivity.STATUS_SENDED_GOODS)) {
				continue;
			}

			// 领奖时间已过
			ActivityCatalog activity = one.getCatalog();
			Date from = activity.getPrizeExpiredFrom();
			Date to = activity.getPrizeExpiredTo();

			CndExpr expr = Expr.eq("name", one.getUsername()).and(Expr.eq("code", one.getTel()));// 本人的订单
			expr = expr.and(Expr.isNull("activityID").or(Expr.eq("activityID", "")).or(Expr.beginWith("activityID", activity.getId() + "-")));// 未参加过其他活动的订单
			// 已付款的订单
			expr = expr.and(Expr.ge("status", Order.STATUS_WAIT_SELLER_SEND_GOODS));
			expr = expr.and(Expr.le("status", Order.STATUS_TRADE_FINISHED));
			List<Order> orders = orm.query(Order.class, expr);

			Double total = 0.0;
			String orderID = "";
			for (Order order : orders) {
				total += order.getTotalCost();

				long now = order.getCreated().getTime();
				if (from != null && now < from.getTime()) {
					continue;
				}
				if (from != null && now > to.getTime()) {
					continue;
				}
				order.setActivityID(activity.getId() + "-" + one.getId());
				order.setActivityComment("竞猜排名 " + one.getResultOrder() + " (" + activity.getName() + ")");

				if (order.getOrderID() != null)
					orderID += order.getOrderID() + ",";
				else
					orderID += order.getTradeID() + ",";

				orm.save(order, Expr.fieldRexpr("activityID$|activityComment$"));
			}
			if (!Str.isEmpty(orderID)) {
				one.setPrizeDate(new Date());
				one.setStatus(StockActivity.STATUS_PRIZED_ORDER);
				one.setPrizeOrder("总金额 " + total + " (" + orderID + ")");

				orm.save(one, Expr.fieldRexpr("prizeDate$|status$|prizeOrder$"));
			}

		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}

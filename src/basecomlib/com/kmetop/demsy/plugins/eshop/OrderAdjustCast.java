package com.kmetop.demsy.plugins.eshop;

import java.util.LinkedList;
import java.util.List;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.eshop.IOrderItem;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 调整订单费用
 * 
 * @author yongshan.ji
 * 
 */
public class OrderAdjustCast extends BizPlugin {

	protected void evalCast(IOrder order) {
		if (order.getStatus() != IOrder.STATUS_WAIT_BUYER_PAY) {
			throw new DemsyException("调整邮寄费失败：只能对“未付款”的订单调整邮寄费！\n【%s】订单状态: %s", order, IOrder.STATUS_TITLES[order.getStatus()]);
		}
		Double postCost = order.getLogisticsCost();
		Double itemsCost = order.getItemsCost();

		if (postCost == null)
			postCost = 0.0;

		double totalPrice = itemsCost + postCost;
		order.setTotalCost(totalPrice);
	}

	protected void evalCast(IOrderItem item) {
		if (item.getOrder() == null) {
			throw new DemsyException("不能给未下单的条目调整费用");
		}
		if (item.getOrder().getStatus() != IOrder.STATUS_WAIT_BUYER_PAY) {
			throw new DemsyException("调整订单价格失败：只能对“未付款”的订单调整价格！\n【%s】订单状态: %s", item.getOrder(), IOrder.STATUS_TITLES[item.getOrder().getStatus()]);
		}

		Double price = item.getPrice();
		int amount = item.getAmount();
		Double discount = item.getDiscount();

		if (price == null)
			throw new DemsyException("未知产品单价，无法计算");

		if (discount != null) {
			if (discount > 10)
				discount = discount / 10;
			if (discount > 10 || discount <= 0) {
				throw new DemsyException("折扣数值非法，折扣必须在0-10之间");
			}

			double totalPrice = (price * amount) * discount / 10;
			item.setSubtotal(Double.parseDouble(Obj.format(totalPrice, "#.00")));// 保留两位小数
		} else {
			double sum = price * amount;
			if (sum != item.getSubtotal()) {
				discount = item.getSubtotal() / sum * 10;
				item.setDiscount(discount);
			}
		}
	}

	@Override
	public void before(BizEvent event) {
		Object data = event.getEntity();
		if (data instanceof List) {
			List list = (List) data;
			for (Object obj : list) {
				if (obj instanceof IOrder)
					evalCast((IOrder) obj);
				else if (obj instanceof IOrderItem)
					evalCast((IOrderItem) obj);
			}
		} else {
			if (data instanceof IOrder)
				evalCast((IOrder) data);
			else if (data instanceof IOrderItem)
				evalCast((IOrderItem) data);
		}
	}

	@Override
	public void after(BizEvent event) {
		Class ordertype = null;
		Class itemtype = null;

		List<Long> orderList = new LinkedList();

		Object data = event.getEntity();
		if (data instanceof List) {
			List list = (List) data;
			for (Object obj : list) {
				if (obj instanceof IOrderItem) {
					IOrderItem item = (IOrderItem) obj;
					if (ordertype == null) {
						ordertype = item.getOrder().getClass();
						itemtype = item.getClass();
					}
					Long id = item.getOrder().getId();
					if (!orderList.contains(id)) {
						orderList.add(id);
					}
				}
			}
		} else {
			Object obj = data;
			if (obj instanceof IOrderItem) {
				IOrderItem item = (IOrderItem) obj;
				if (ordertype == null) {
					ordertype = item.getOrder().getClass();
					itemtype = item.getClass();
				}
				Long id = item.getOrder().getId();
				if (!orderList.contains(id)) {
					orderList.add(id);
				}
			}
		}
		if (orderList.size() > 0) {
			IOrm orm = Demsy.orm();
			List<IOrder> orders = orm.query(ordertype, Expr.in(LibConst.F_ID, orderList));
			for (IOrder order : orders) {
				List<IOrderItem> items = orm.query(itemtype, Expr.eq("order", order));
				double itemsPrice = 0;
				for (IOrderItem item : items) {
					itemsPrice += item.getSubtotal();
				}
				order.setItemsCost(itemsPrice);
				evalCast(order);
			}
			orm.save(orders);
		}
	}

	@Override
	public void loaded(BizEvent event) {

	}

}

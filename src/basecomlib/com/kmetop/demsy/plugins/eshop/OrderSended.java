package com.kmetop.demsy.plugins.eshop;

import java.util.List;

import com.kmetop.demsy.actions.OrderActions;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.eshop.IOrderItem;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 订单已发货
 * 
 * @author yongshan.ji
 * 
 */
public class OrderSended extends BizPlugin {
	protected static Log log = Logs.get();

	protected void setStatus(IOrm orm, IOrder order) {
		if (order.getStatus() != IOrder.STATUS_WAIT_SELLER_SEND_GOODS && order.getStatus() != IOrder.STATUS_WAIT_BUYER_CONFIRM_GOODS) {
			throw new DemsyException("发货失败：只能给%s的订单发货！订单(%s),订单状态(%s)", IOrder.STATUS_TITLES[IOrder.STATUS_WAIT_SELLER_SEND_GOODS], order.getOrderID(), IOrder.STATUS_TITLES[order.getStatus()]);
		}
		order.setStatus(IOrder.STATUS_WAIT_BUYER_CONFIRM_GOODS);

		OrderActions.processOrder(orm, order, order.getTradeID(), IOrder.STATUS_WAIT_BUYER_CONFIRM_GOODS);
	}

	protected void setStatus(IOrderItem item) {
		if (item.getOrder() == null) {
			throw new DemsyException("发货失败：不能给未下单的条目备货！\n【%s】条目状态: %s", item, item.getStatus());
		}
		if (item.getStatus() != IOrderItem.STATUS_PREPARING || item.getOrder().getStatus() != IOrder.STATUS_WAIT_SELLER_SEND_GOODS) {
			throw new DemsyException("备货失败：只能给“等待卖家发货(已付款)”的订单清单备货！\n【%s】订单状态: %s, 条目状态: %s", item, IOrder.STATUS_TITLES[item.getOrder().getStatus()], item.getStatus());
		}
		item.setStatus(IOrderItem.STATUS_PREPARED);
	}

	@Override
	public void before(BizEvent event) {
		IOrm orm = event.getOrm();
		Object data = event.getEntity();
		if (data instanceof List) {
			List list = (List) data;
			for (Object obj : list) {
				if (obj instanceof IOrder)
					setStatus(orm, (IOrder) obj);
				else if (obj instanceof IOrderItem)
					setStatus((IOrderItem) obj);
			}
		} else {
			Object obj = data;
			if (obj instanceof IOrder)
				setStatus(orm, (IOrder) obj);
			else if (obj instanceof IOrderItem)
				setStatus((IOrderItem) obj);
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}

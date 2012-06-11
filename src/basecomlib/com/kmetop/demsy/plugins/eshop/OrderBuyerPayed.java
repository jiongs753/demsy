package com.kmetop.demsy.plugins.eshop;

import java.util.List;

import com.kmetop.demsy.actions.OrderActions;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 买家付款
 * 
 * @author yongshan.ji
 * 
 */
public class OrderBuyerPayed extends BizPlugin {
	protected static Log log = Logs.get();

	protected void process(IOrm orm, IOrder order) {
		if (order.getStatus() != IOrder.STATUS_WAIT_BUYER_PAY && order.getStatus() != IOrder.STATUS_WAIT_SELLER_SEND_GOODS) {
			throw new DemsyException("付款失败：只能为%s的订单付款！订单(%s),订单状态(%s)", IOrder.STATUS_TITLES[IOrder.STATUS_WAIT_BUYER_PAY], order.getOrderID(), IOrder.STATUS_TITLES[order.getStatus()]);
		}
		order.setStatus(IOrder.STATUS_WAIT_SELLER_SEND_GOODS);

		OrderActions.processOrder(orm, order, order.getTradeID(), IOrder.STATUS_WAIT_SELLER_SEND_GOODS);
	}

	@Override
	public void before(BizEvent event) {
		IOrm orm = event.getOrm();
		Object data = event.getEntity();
		if (data instanceof List) {
			List list = (List) data;
			for (Object obj : list) {
				process(orm, (IOrder) obj);
			}
		} else {
			process(orm, (IOrder) data);
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}

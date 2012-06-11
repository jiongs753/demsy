package com.kmetop.demsy.plugins.eshop;

import java.util.Date;
import java.util.List;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

/**
 * 删除3天以上的过期订单
 * 
 * @author yongshan.ji
 * 
 */
public class OrderDelete extends BizPlugin {
	protected static Log log = Logs.get();

	protected void process(IOrm orm, IOrder order) {
		Date date = order.getCreated();
		Date now = new Date();
		if (order.getStatus() != IOrder.STATUS_WAIT_BUYER_PAY//
				|| (now.getTime() - date.getTime()) < 3 * 24 * 60 * 60 * 1000//
		) {
			throw new DemsyException("删除失败：只能永久删除3天以上未付款的订单！当前订单(%s),状态(%s)", order.getOrderID(), IOrder.STATUS_TITLES[order.getStatus()]);
		}

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

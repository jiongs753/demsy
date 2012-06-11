package com.kmetop.demsy.plugins.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.activity.ActivityCatalog;
import com.kmetop.demsy.comlib.impl.sft.activity.StockActivity;
import com.kmetop.demsy.comlib.web.IActivity;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;
import com.kmetop.demsy.util.sort.SortUtils;

/**
 * 统计股价竞猜结果 并对结果进行排名
 * 
 * @author yongshan.ji
 * 
 */
public class StockGuessStat extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		ActivityCatalog activity = (ActivityCatalog) event.getEntity();
		Byte type = activity.getType();
		if (type == null || !type.equals(IActivity.TYPE_STOCK)) {
			throw new DemsyException("统计出错：活动类型非法！");
		}

		// 获取收盘价
		Double price = activity.getStockResult();
		if (price == null) {
			throw new DemsyException("统计出错：请先填写收盘价！");
		}

		IOrm orm = event.getOrm();
		// 按竞猜时间升序排序
		List<StockActivity> list = orm.query(StockActivity.class, Expr.eq("catalog", activity).addAsc("created"));
		Map<String, StockActivity> map = new HashMap();
		for (StockActivity one : list) {
			String key = one.getUsername() + "-" + one.getTel() + "-" + one.getPostcode();

			Double value = one.getGuessValue();
			if (value == null) {
				one.setGuessOffset(price);
				one.setStatus(StockActivity.STATUS_CANCEL);// 竞猜无效
			} else {
				// 计算偏差绝对值
				Double offset = value - price;
				if (offset < 0)
					offset = (-1) * offset;
				one.setGuessOffset(Double.parseDouble(Obj.format(offset, "#.00")));
				StockActivity old = map.get(key);
				if (old != null)
					old.setStatus(StockActivity.STATUS_CANCEL);// 老的竞猜无效
				map.put(key, one);
			}
		}
		// 按竞猜偏差升序排序
		int order = 1;
		SortUtils.sort(list, "guessOffset", true);
		for (StockActivity one : list) {
			Byte status = one.getStatus();
			if (status != null && status.equals(StockActivity.STATUS_CANCEL)) {
				one.setResultOrder(Integer.MAX_VALUE);
			} else {
				one.setResultOrder(order);
				one.setStatus(StockActivity.STATUS_UNPRIZED);
				order++;
			}
			orm.save(one, Expr.fieldRexpr("guessOffset$|resultOrder$|status$", true));
			// System.out.println(one.getUsername() + "-" + one.getTel() +
			// ": offset=" + one.getGuessOffset() + ", date=" +
			// Dates.formatDateTime(one.getCreated()) + ", order=" +
			// one.getResultOrder()
			// + ", value=" + one.getGuessValue() + ", status=" +
			// one.getPrizeStatus());
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}

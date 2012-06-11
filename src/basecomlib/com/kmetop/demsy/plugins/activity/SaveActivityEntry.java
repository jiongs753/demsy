package com.kmetop.demsy.plugins.activity;

import java.util.Date;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.web.IActivity;
import com.kmetop.demsy.comlib.web.IActivityEntry;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveActivityEntry extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		IActivityEntry entry = (IActivityEntry) event.getEntity();
		IActivity catalog = entry.getCatalog();

		Demsy me = Demsy.me();

		// 是否需要实名参与
		if (catalog.isLimitLogin() && Str.isEmpty(me.username())) {
			throw new DemsyException("错误：只有登录用户可以参与，请先登录！");
		}

		// 有效期
		Date now = new Date();
		Date from = catalog.getExpiredFrom();
		Date to = catalog.getExpiredTo();
		if (from != null && now.getTime() < from.getTime()) {
			throw new DemsyException("错误：活动尚未开始.");
		}
		if (to != null && now.getTime() > to.getTime()) {
			throw new DemsyException("错误：活动已经结束.");
		}

		// 次数限制
		int limitTimes = catalog.getLimitTimes();
		if (limitTimes > 0) {
			IOrm orm = event.getOrm();
			CndExpr expr = Expr.eq("catalog", catalog);
			if (catalog.isLimitLogin()) {
				expr = Expr.and(expr, Expr.eq(LibConst.F_CREATED_BY, me.username()));
			} else {
				expr = Expr.and(expr, Expr.eq(LibConst.F_CREATED_IP, me.request().getRemoteAddr()));
			}
			int count = orm.count(entry.getClass(), expr);
			if (count >= limitTimes) {
				throw new DemsyException("错误：每人只能参与 %s 次.", limitTimes);
			}
		}
	}

	@Override
	public void after(BizEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}

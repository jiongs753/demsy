package com.kmetop.demsy.plugins.security;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.base.BaseUser;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveUser extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		BaseUser user = (BaseUser) event.getEntity();
		String code = user.getCode();
		IOrm orm = event.getOrm();
		int count = orm.count(user.getClass(), Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft()).and(Expr.eq(LibConst.F_CODE, code)));
		if (count > 0) {
			throw new DemsyException("登录帐号已被使用，请重新填写登录帐号!");
		}
	}

	@Override
	public void after(BizEvent event) {
	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}

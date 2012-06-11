package com.kmetop.demsy.plugins.security;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.entity.base.BaseUser;

public class SaveStaffUser extends SaveUser {

	@Override
	public void before(BizEvent event) {
		super.before(event);

		BaseUser user = (BaseUser) event.getEntity();
		user.setDisabled(true);
	}
}

package com.kmetop.demsy.plugins.security;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.plugins.BizPlugin;

public class SavePermission extends BizPlugin {

	@Override
	public void before(BizEvent event) {
	}

	@Override
	public void after(BizEvent event) {
		Demsy.security.clearPermissions();
	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}

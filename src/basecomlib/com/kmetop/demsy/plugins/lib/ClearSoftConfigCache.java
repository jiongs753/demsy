package com.kmetop.demsy.plugins.lib;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.plugins.BizPlugin;

public class ClearSoftConfigCache extends BizPlugin {

	@Override
	public void after(BizEvent event) {
		SoftConfigManager.clearCache();
	}

}

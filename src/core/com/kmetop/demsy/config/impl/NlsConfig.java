package com.kmetop.demsy.config.impl;

import static com.kmetop.demsy.Demsy.*;

import com.kmetop.demsy.config.IConfig;
import com.kmetop.demsy.config.INlsConfig;

public class NlsConfig extends BaseConfig implements INlsConfig {
	public NlsConfig() {
		super(appconfig.getNLSConfig());
	}

	@Override
	public IConfig copy() {
		NlsConfig ret = new NlsConfig();
		this.copyTo(ret);

		return ret;
	}
}

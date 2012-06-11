package com.kmetop.demsy.log.impl;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.LogAdapter;

public class SystemLogAdapter implements LogAdapter {

	public Log getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
